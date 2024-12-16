package com.eservice.eumowy

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.TelephoneType
import com.eservice.eumowy.pdfmapper.representative.LegalFormMapper
import com.google.common.base.Strings
import groovy.sql.GroovyRowResult
import org.apache.log4j.Logger

import java.sql.Timestamp

import static com.google.common.base.Strings.isNullOrEmpty

class RepresentativeService {

    def log = Logger.getLogger(RepresentativeService.class)

    def cbdService
    def mapperService
    def dictionaryService

    List<RepresentativeCommand> getRepresentativesFromCBD(String nip) {
        log.info("Fetching representatives from CBD for nip ${nip}")
        List<GroovyRowResult> representatives = cbdService.getReprezentanciFromCbd(nip)

        log.info("Found ${representatives.size()} representatives for nip ${nip}")

        return representatives.collect { result ->
            log.info("Creating representative for result '${result}'")

            RepresentativeCommand representative = new RepresentativeCommand()

            representative.setName(result.imie)
            representative.setSurname(result.nazwisko)
            representative.setPosition(result.stanowisko ? mapperService.mapPositionFromCBD(result.stanowisko) : null)
            representative.setSalutation(result.prefix)
            representative.setEmail(result.email)
            representative.setPesel(result.pesel)
            representative.setBirthDate(getDate(result.dataUrodzenia))
            representative.setCitizenship(result.obywatelstwo)
            representative.setBirthCountry(getCountryOrNull(result.krajUrodzenia))
            representative.setDocumentType(getDocumentType(result.typDokumentu))
            representative.setDocumentNumber(result.dokumentTozsamosci)
            representative.setDocumentExpirationDate(getDate(result.dataWaznosciDokumentu))
            representative.setDocumentIssueDate(getDate(result.dataWydaniaDokumetu))
            representative.setHasSignedContract(result.czyPodpisalaUmowe ? result.czyPodpisalaUmowe == "T" : null)
            representative.setMidCBD(result.mid)

            if (representative.pesel) {
                representative.setVerification(AcceptorVerification.PESEL)
            } else if (representative.birthDate) {
                representative.setVerification(AcceptorVerification.BIRTH_DATE)
            }

            String telefonStacjonarny = result.telefonStac
            String telefonKomorkowy = result.telefon

            if (telefonKomorkowy) {
                if (!telefonKomorkowy.contains("-")) {
                    def maskedPhoneNumber = "";
                    for (int i = 0; i < telefonKomorkowy.length(); i++) {
                        if (i == 3 || i == 6) {
                            maskedPhoneNumber += "-"
                        }
                        maskedPhoneNumber += telefonKomorkowy.charAt(i)
                    }
                    representative.setMobilePhone(maskedPhoneNumber)
                } else {
                    representative.setMobilePhone(telefonKomorkowy)
                }
                representative.setTelephoneType(TelephoneType.MOBILE)
            } else if (telefonStacjonarny) {
                if (!telefonStacjonarny.contains("-") && telefonStacjonarny.length() == 9) {
                    def maskedPhoneNumber = "(";
                    for (int i = 0; i < telefonStacjonarny.length(); i++) {
                        if (i == 2) {
                            maskedPhoneNumber += ") "
                        }
                        if (i == 6 || i == 8) {
                            maskedPhoneNumber += "-"
                        }
                        maskedPhoneNumber += telefonStacjonarny.charAt(i)
                    }
                    representative.setLandlinePhone(maskedPhoneNumber)
                } else {
                    representative.setLandlinePhone(telefonStacjonarny)
                }
                representative.setTelephoneType(TelephoneType.LANDLINE)
            }

            return representative
        }
    }

    List<BeneficiaryCommand> getDaneBeneficjentaRzeczywistego(String nip) {
        log.info("Fetching beneficiaries from CBD for nip ${nip}")

        List<GroovyRowResult> beneficiaries = cbdService.getBeneficjenci(nip)

        log.info("Found ${beneficiaries.size()} beneficiaries for nip ${nip}")

        return beneficiaries.collect { result ->
            log.info("Creating beneficiary for result '${result}'")

            BeneficiaryCommand beneficiary = new BeneficiaryCommand()

            beneficiary.setName(result.imie)
            beneficiary.setSurname(result.nazwisko)
            beneficiary.setSalutation(result.prefix)
            beneficiary.setCitizenship(result.obywatelstwo)
            beneficiary.setMidCBD(result.mid)

            return beneficiary
        }
    }

    List<RepresentativeCommand> getRepresentativesFromBisnode(List<MerchantRepresentativeDTO> representatives) {
        return representatives.collect {
            log.info("Creating representative from bisnode representative '${it}'")

            RepresentativeCommand representative = new RepresentativeCommand()
            representative.salutation = it.title
            representative.name = it.firstName
            representative.surname = it.lastName
            representative.position = it.position
            representative.pesel = it.pesel

            if (!isNullOrEmpty(it.pesel)) {
                representative.verification = AcceptorVerification.PESEL
            }

            representative.birthCountry = it.nationality

            return representative
        }
    }

    private String getCountryOrNull(String countryCode) {
        if (countryCode == null) return null

        return dictionaryService.getCountries()
                .find { it.code == countryCode }
                ?.value
    }

    private Date getDate(String dateAsString) {
        if (isNullOrEmpty(dateAsString)) return null

        return Date.parse("YYYY-mm-dd", dateAsString)
    }

    private Date getDate(Timestamp timestamp) {
        if (timestamp == null) return null

        return new Date(timestamp.time)
    }

    IdentityDocumentType getDocumentType(String value) {
        if (value == null) return null

        switch (value) {
            case "DOOS":
                return IdentityDocumentType.IDENTITY_CARD
                break
            case "KAPO":
                return IdentityDocumentType.RESIDENCE_CARD
                break
            case "PASS":
                return IdentityDocumentType.PASSPORT
                break
            default: null
        }
    }
}
