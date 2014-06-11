package com.eservice.eumowy.command

import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.enums.LocationType
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable

@Validateable
class RepresentativeCommand implements Serializable{
    String tytul
    String imie
    String nazwisko
    String stanowisko

    String pesel

    LocationType typLokalizacji
    String lokalizacjaPesel
    String lokalizacjaKraj

    IdentityDocumentType typDokumentu

    String seriaNrDokumentu
    String dataUrodzenia
    String obywatelstwo
    String adres

    Boolean czyStanowiskoPolityczne

    static constraints = {
        tytul(nullable: true)
        imie(nullable: true, shared: "lettersOnly")
        nazwisko(nullable: true, shared: "lettersOnly")
        stanowisko(nullable: true)

        pesel(nullable: true, shared: "number", validator: {value, cmd, errors ->
            cmd.pesel ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })

        typLokalizacji(nullable: false)
        lokalizacjaPesel(shared: "number", validator: {value, cmd, errors ->
            !cmd.isLocationTypeAbroad() ? NumberValidator.validatePesel(value, cmd, errors, "lokalizacjaPesel") : true
        })
        lokalizacjaKraj(maxSize: 30, validator: { value, cmd, erros ->
            return !(cmd.isLocationTypeAbroad() && !value)
        })

        seriaNrDokumentu(blank: false, maxSize: 20, shared: "alphanumeric")
        dataUrodzenia(shared: "date", validator: { value, cmd, errors ->
            return !(cmd.isLocationTypeAbroad() && !value)
        })
        obywatelstwo(blank: false, maxSize: 30)

        czyStanowiskoPolityczne(validator: {value, cmd, errors ->
            return !(cmd.isLocationTypeAbroad() && !value)
        })
    }

    private boolean isLocationTypeAbroad() {
        return LocationType.ABROAD.equals(typLokalizacji)
    }
}
