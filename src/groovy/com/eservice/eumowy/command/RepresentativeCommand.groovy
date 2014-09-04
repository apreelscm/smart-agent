package com.eservice.eumowy.command

import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.enums.AcceptorLocation
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

    String pesel

    AcceptorLocation typLokalizacji
    String lokalizacjaPesel
    String lokalizacjaKraj

    IdentityDocumentType typDokumentu

    String seriaNrDokumentu
    Date dataUrodzenia
    String obywatelstwo
    String adres

    Boolean czyStanowiskoPolityczne

    static constraints = {
        tytul(nullable: true)
        imie(nullable: true, shared: "lettersOnly")
        nazwisko(nullable: true, shared: "lettersOnly")
        stanowisko(nullable: true)

        pesel(nullable: true, blank: true, validator: {value, cmd, errors ->
            if(!value || cmd.processCommand.isAkceptantAbroad()) return true

            return NumberValidator.validatePesel(value, cmd, errors, "pesel")
        })

        typLokalizacji(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isAkceptantAbroad(),
                    "typLokalizacji", "representative.typLokalizacji.required")
        })
        lokalizacjaPesel(nullable: true, shared: "number", validator: {value, cmd, errors ->
            cmd.processCommand.isAkceptantAbroad() && !cmd.isRepresentativeLocationAbroad() ?
                NumberValidator.validatePesel(value, cmd, errors, "lokalizacjaPesel") : true
        })
        lokalizacjaKraj(nullable: true, maxSize: 30, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, (cmd.processCommand.isAkceptantAbroad() && cmd.isRepresentativeLocationAbroad()),
                    "lokalizacjaKraj", "representative.lokalizacjaKraj.required")
        })

        typDokumentu(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isAkceptantAbroad(), "typDokumentu",
                    "representative.typDokumentu.required")
        })

        seriaNrDokumentu(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isAkceptantAbroad(),
                    "seriaNrDokumentu", "representative.seriaNrDokumentu.required")
        })
        dataUrodzenia(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, (cmd.processCommand.isAkceptantAbroad() && cmd.isRepresentativeLocationAbroad()),
                    "dataUrodzenia", "representative.dataUrodzenia.required")
        })
        obywatelstwo(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isAkceptantAbroad(),
                    "obywatelstwo", "representative.obywatelstwo.required")
        })
        adres(nullable: true, maxSize: 100, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isAkceptantAbroad(),
                    "adres", "representative.adres.required")
        })

        czyStanowiskoPolityczne(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, (cmd.processCommand.isAkceptantAbroad() && cmd.isRepresentativeLocationAbroad()),
                    "czyStanowiskoPolityczne", "representative.czyStanowiskoPolityczne.required")
        })
    }

    public boolean isRepresentativeLocationAbroad() {
        if(typLokalizacji == null) {
            return null
        }

        return AcceptorLocation.ABROAD.equals(typLokalizacji)
    }

    public boolean isRepresentativeLocationCountry() {
        if(typLokalizacji == null) {
            return null
        }

        return AcceptorLocation.COUNTRY.equals(typLokalizacji)
    }

    public boolean isIdentityDocumentPassport() {
        if(typDokumentu == null) {
            return null
        }

        return IdentityDocumentType.PASSPORT.equals(typDokumentu)
    }

}
