package com.eservice.eumowy.command

import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorDetail
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable

@Validateable(nullable = true)
class RepresentativeCommand implements Serializable{
    transient ProcessCommand processCommand

    Long id

    String tytul
    String imie
    String nazwisko
    String stanowisko

    AcceptorDetail detail
    String pesel
    String kodKraju
    Date dataUrodzenia

    AcceptorLocation typLokalizacji

    IdentityDocumentType typDokumentu

    String seriaNrDokumentu
    String obywatelstwo
    String adres

    Boolean czyStanowiskoPolityczne

    static constraints = {
        tytul(nullable: true)
        imie(nullable: true, shared: "lettersOnly")
        nazwisko(nullable: true, shared: "lettersOnly")
        stanowisko(nullable: true)

        detail(nullable: true, validator: { value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "detail",
                    "representative.option.required")
        })

        pesel(nullable: true, shared: "number", validator: {value, cmd, errors ->
            return AcceptorDetail.PESEL.equals(detail) ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })

        typLokalizacji(nullable: true, validator: {value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "typLokalizacji",
                    "representative.typLokalizacji.required")
        })
        kodKraju(nullable: true, maxSize: 30, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorDetail.COUNTRY_CODE.equals(detail),
                    "lokalizacjaKraj", "representative.lokalizacjaKraj.required")
        })

        typDokumentu(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "typDokumentu",
                    "representative.typDokumentu.required")
        })

        seriaNrDokumentu(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPerson(),
                    "seriaNrDokumentu", "representative.seriaNrDokumentu.required")
        })
        dataUrodzenia(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorDetail.BIRTH_DATE.equals(cmd.detail),
                    "dataUrodzenia", "representative.dataUrodzenia.required")
        })
        obywatelstwo(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "obywatelstwo", "representative.obywatelstwo.required")
        })
        adres(nullable: true, maxSize: 100, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorLocation.COUNTRY.equals(cmd.typLokalizacji),
                    "adres", "representative.adres.required")
        })

        czyStanowiskoPolityczne(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorLocation.ABROAD.equals(cmd.typLokalizacji),
                    "czyStanowiskoPolityczne", "representative.czyStanowiskoPolityczne.required")
        })
    }

    public boolean isRepresentativeLocationAbroad() {
        return AcceptorLocation.ABROAD.equals(typLokalizacji)
    }
}
