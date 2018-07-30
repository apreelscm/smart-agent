package com.eservice.eumowy.command

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
    Date birthDate
    String birthCountry
    String birthCity

    IdentityDocumentType documentType
    String documentNumber
    Date documentIssueDate
    Date documentExpirationDate

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

        documentType(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() || cmd.position == "Pełnomocnik", "documentType",
                    "representative.typDokumentu.required")
        })
        documentNumber(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() || cmd.position == "Pełnomocnik",
                    "documentNumber", "representative.seriaNrDokumentu.required")
        })
        documentIssueDate(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD,
                    "documentIssueDate", "representative.dataWydaniaDokumentu.required")
        })
        documentExpirationDate(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD,
                    "documentExpirationDate", "representative.dataWaznosciDokumentu.required")
        })

        birthDate(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.BIRTH_DATE.equals(cmd.verification),
                    "birthDate", "representative.dataUrodzenia.required")
        })
        birthCountry(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "birthCountry", "representative.krajUrodzenia.required")
        })
        birthCity(nullable: true, maxSize: 255, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    "birthCity", "representative.miastoUrodzenia.required")
        })

        address(nullable: true, maxSize: 100, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.adres.required")
        })

        country(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.kraj.required")
        })

        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.czyStanowiskoPolityczne.required")
        })

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    "citizenship", "representative.obywatelstwo.required")
        })
    }
}
