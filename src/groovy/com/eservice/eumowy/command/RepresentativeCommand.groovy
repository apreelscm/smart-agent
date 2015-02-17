package com.eservice.eumowy.command

import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable
import org.apache.commons.lang.StringUtils

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
    String country

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
            return AcceptorVerification.PESEL.equals(cmd.verification) ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })

        locationType(nullable: true, validator: {value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "locationType",
                    "representative.typLokalizacji.required")
        })
        countryCode(nullable: true, maxSize: 30, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.COUNTRY_CODE.equals(cmd.verification),
                    "countryCode", "representative.lokalizacjaKraj.required")
        })

        documentType(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(), "documentType",
                    "representative.typDokumentu.required")
        })

        documentNumber(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
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
        address(nullable: false, maxSize: 100, blank: false)
        country(nullable: false, blank: false)

        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, !"Polska".equals(cmd.country),
                    "isPolitician", "representative.czyStanowiskoPolityczne.required")
        })
    }

    public boolean isRepresentativeLocationAbroad() {
        return AcceptorLocation.ABROAD.equals(locationType)
    }

    public boolean isFromPoland() {
        return "Polska".equals(country)
    }
}
