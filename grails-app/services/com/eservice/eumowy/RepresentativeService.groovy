package com.eservice.eumowy

import com.eservice.eumowy.command.RepresentativeCommand
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
                def dataWydaniaDokumetu = result?.dataWydaniaDokumetu ?: ""
                def czyPodpisalaUmowe = mapHasSignedContract(result?.czyPodpisalaUmowe)
                int legalFormCode = result?.formaPrawnaID ?: ""

                representative.setName(name)
                representative.setSurname(surname)
                representative.setPosition(mapperService.mapPositionFromCBD(position))
                representative.setSalutation(salutation)
                representative.setEmail(email)
                representative.setLandlinePhone(telefonStacjonarny)
                representative.setMobilePhone(telefonKomorkowy)
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
                representative.setDocumentIssueDate(dataWydaniaDokumetu)
                representative.setHasSignedContract(czyPodpisalaUmowe)
                representative.setLegalFormCBD(LegalFormMapper.mapLegalFormFromCBD(legalFormCode))
                representative.setNameCBD(name)
                representative.setSurnameCBD(surname)
                representative.setPositionCBD(mapperService.mapPositionFromCBD(position))
                representative.setSalutationCBD(salutation)
                representative.setEmailCBD(email)
                representative.setLandlinePhoneCBD(telefonStacjonarny)
                representative.setMobilePhoneCBD(telefonKomorkowy)
                representative.setStreetCBD(ulica)
                representative.setStreetTitleCBD(typUlicy)
                representative.setCountryCBD(kraj)
                representative.setHouseNumberCBD(numerDomu)
                representative.setFlatNumberCBD(numerLokalu)
                representative.setPostalCodeCBD(kodPocztowy)
                representative.setCityCBD(miasto)
                representative.setPostOfficeCBD(poczta)
                representative.setIsCBDDataChangedManually(representative?.isCBDDataChangedManually != null ? representative?.isCBDDataChangedManually : true)
                representative.setPeselCBD(pesel)
                representative.setBirthDateCBD(dataUrodzenia)
                representative.setCitizenshipCBD(obywatelstwo)
                representative.setBirthCountryCBD(krajUrodzenia)
                representative.setDocumentNumberCBD(dokumentTozsamosci)
                representative.setDocumentExpirationDateCBD(dataWaznosciDokumentu)
                representative.setDocumentIssueDateCBD(dataWydaniaDokumetu)
                representative.setHasSignedContractCBD(czyPodpisalaUmowe)

                representativeCommands.add(representative)
            }
        }
        return representativeCommands
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
