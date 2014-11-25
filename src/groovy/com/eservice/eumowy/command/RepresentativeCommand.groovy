package com.eservice.eumowy.command

import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable

@Validateable(nullable = true)
class RepresentativeCommand implements Serializable{
    transient ProcessCommand processCommand

    Long id

    String salutation
    String name
    String surname
    String position

    AcceptorVerification verification
    String pesel
    String countryCode
    Date birthDate

    AcceptorLocation locationType

    IdentityDocumentType documentType

    String documentNumber
    String citizenship
    String address

    Boolean isPolitician

    static constraints = {
        salutation(nullable: true)
        name(nullable: true, shared: "lettersOnly")
        surname(nullable: true, shared: "lettersOnly")
        position(nullable: true)

        verification(nullable: true, validator: { value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "verification",
                    "representative.option.required")
        })

        pesel(nullable: true, shared: "number", validator: {value, cmd, errors ->
            return AcceptorVerification.PESEL.equals(verification) ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })

        locationType(nullable: true, validator: {value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "locationType",
                    "representative.typLokalizacji.required")
        })
        countryCode(nullable: true, maxSize: 30, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.COUNTRY_CODE.equals(verification),
                    "lokalizacjaKraj", "representative.lokalizacjaKraj.required")
        })

        documentType(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "documentType",
                    "representative.typDokumentu.required")
        })

        documentNumber(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPerson(),
                    "documentNumber", "representative.seriaNrDokumentu.required")
        })
        birthDate(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.BIRTH_DATE.equals(cmd.verification),
                    "birthDate", "representative.dataUrodzenia.required")
        })
        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "citizenship", "representative.obywatelstwo.required")
        })
        address(nullable: true, maxSize: 100, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorLocation.COUNTRY.equals(cmd.locationType),
                    "address", "representative.adres.required")
        })

        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorLocation.ABROAD.equals(cmd.locationType),
                    "isPolitician", "representative.czyStanowiskoPolityczne.required")
        })
    }

    public boolean isRepresentativeLocationAbroad() {
        return AcceptorLocation.ABROAD.equals(locationType)
    }
}
