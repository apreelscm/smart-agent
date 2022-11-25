package com.eservice.eumowy

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.TelephoneType
import com.eservice.eumowy.pdfmapper.representative.LegalFormMapper

class RepresentativeService {

    def cbdService
    def mapperService

    List<RepresentativeCommand> getRepresentativesFromCBD(String nip) {
        List<RepresentativeCommand> representativeCommands = []
        def representatives = []

        def representativesList = cbdService.getReprezentanciFromCbd(nip)

        if (representativesList.size > 0) {
            representatives.addAll(representativesList)

            representatives?.each { result ->
                RepresentativeCommand representative = new RepresentativeCommand()

                String name = result?.imie ?: ""
                String surname = result?.nazwisko ?: ""
                String position = result?.stanowisko ?: ""
                String salutation = result?.prefix ?: ""
                String email = result?.email ?: ""
                String telefonStacjonarny = result?.telefonStac ?: ""
                String telefonKomorkowy = result?.telefon ?: ""
                String ulica = result?.ulica ?: ""
                String kraj = result?.kraj ?: ""
                String typUlicy = result?.ulicaTyp ?: ""
                String numerDomu = result?.numerDomu ?: ""
                String numerLokalu = result?.numerLokalu ?: ""
                String kodPocztowy = result?.kodPocztowy ?: ""
                String miasto = result?.miasto ?: ""
                String poczta = result?.poczta ?: ""
                String pesel = result?.pesel ?: ""
                def dataUrodzenia = result?.dataUrodzenia ?: ""
                String obywatelstwo = mapCountryByCode(result?.obywatelstwo)
                String krajUrodzenia = mapCountryByCode(result?.krajUrodzenia)
                String dokumentTozsamosci = result?.dokumentTozsamosci ?: ""
                def dataWaznosciDokumentu = result?.dataWaznosciDokumentu ?: ""
                def dataWydaniaDokumentu = result?.dataWydaniaDokumetu ?: ""
                def czyPodpisalaUmowe = mapHasSignedContract(result?.czyPodpisalaUmowe)
                int legalFormCode = result?.formaPrawnaID ?: ""
                String mid = result?.mid ?: ""

                representative.setName(name)
                representative.setSurname(surname)
                representative.setPosition(mapperService.mapPositionFromCBD(position))
                representative.setSalutation(salutation)
                representative.setEmail(email)
                representative.setStreet(ulica)
                representative.setStreetTitle(typUlicy)
                representative.setCountry(kraj)
                representative.setHouseNumber(numerDomu)
                representative.setFlatNumber(numerLokalu)
                representative.setPostalCode(kodPocztowy)
                representative.setCity(miasto)
                representative.setPostOffice(poczta)
                representative.setIsCBDDataChangedManually(representative?.isCBDDataChangedManually != null ? representative?.isCBDDataChangedManually : true)
                representative.setPesel(pesel)
                representative.setBirthDate(dataUrodzenia)
                representative.setCitizenship(obywatelstwo)
                representative.setBirthCountry(krajUrodzenia)
                representative.setDocumentNumber(dokumentTozsamosci)
                representative.setDocumentExpirationDate(dataWaznosciDokumentu)
                representative.setDocumentIssueDate(dataWydaniaDokumentu)
                representative.setHasSignedContract(czyPodpisalaUmowe)
                representative.setLegalFormCBD(LegalFormMapper.mapLegalFormFromCBD(legalFormCode))
                representative.setMidCBD(mid as String)

                if (pesel?.length() > 0) {
                    representative.setVerification(AcceptorVerification.PESEL)
                } else if (dataUrodzenia?.length() > 0) {
                    representative.setVerification(AcceptorVerification.BIRTH_DATE)
                }

                if (telefonKomorkowy?.length() > 0 || telefonStacjonarny.length() > 0) {
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
                            for (int i = 0; i < telefonKomorkowy.length(); i++) {
                                if (i == 2) {
                                    maskedPhoneNumber += ") "
                                }
                                if (i == 6 || i == 8) {
                                    maskedPhoneNumber += "-"
                                }
                                maskedPhoneNumber += telefonKomorkowy.charAt(i)
                            }
                            representative.setLandlinePhone(maskedPhoneNumber)
                        } else {
                            representative.setLandlinePhone(telefonStacjonarny)
                        }
                        representative.setTelephoneType(TelephoneType.LANDLINE)
                    } else {
                        representative.setMobilePhone(null)
                        representative.setLandlinePhone(null)
                        representative.setPhoneNumber(null)
                        representative.setTelephoneType(null)
                    }
                }

                representativeCommands.add(representative)
            }
        }
        return representativeCommands
    }

    List<BeneficiaryCommand> getDaneBeneficjentaRzeczywistego(String nip) {
        List<BeneficiaryCommand> beneficiaresCommand = []

        def beneficiaryList = cbdService.getBeneficjenci(nip)

        beneficiaryList?.each { result ->
            BeneficiaryCommand representative = new BeneficiaryCommand()

            String name = result?.imie ?: ""
            String surname = result?.nazwisko ?: ""
            String salutation = result?.prefix ?: ""
            String pesel = result?.pesel ?: ""
            def dataUrodzenia = result?.dataUrodzenia ?: ""
            String obywatelstwo =  mapCountryByCode(result?.obywatelstwo as String)

            if (dataUrodzenia != null && dataUrodzenia != "") {
                dataUrodzenia = Date.parse("YYYY-mm-dd", dataUrodzenia.toString())
            }

            representative.setName(name)
            representative.setSurname(surname)
            representative.setSalutation(salutation)
            representative.setPesel(pesel)
            if (dataUrodzenia != null && dataUrodzenia != "") {
                representative.setBirthDate(dataUrodzenia as Date)
            }
            representative.setCitizenship(obywatelstwo)
            representative.setIsCBDDataChangedManually(representative?.isCBDDataChangedManually != null ? representative?.isCBDDataChangedManually : true)
            representative.setMidCBD(result?.mid as String)

            beneficiaresCommand.add(representative)
        }

        return beneficiaresCommand
    }

    private mapCountryByCode(String countryCode) {
        if (countryCode == null) {
            return null
        } else {
            def result = cbdService.getCountryByCountryCode(countryCode)
            return result?.SKR_NAZWA;
        }
    }

    private static Boolean mapHasSignedContract(result) {
        if (result == null) {
            return null
        } else return result == "T"
    }
}
