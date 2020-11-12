package com.eservice.eumowy.command

import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.validator.ConditionValidator
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable

@Validateable(nullable = true)
class BeneficiaryCommand extends RepresentativeCommand implements Serializable {
    Boolean ownsAcceptor
    Boolean controlsAcceptor
    Boolean overQuarterOfVotes
    Integer votesPercentage

    static constraints = {
        importFrom RepresentativeCommand

        salutation(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "salutation", "beneficiary.salutation.required")
        })

        name(nullable: true, shared: "lettersOnly", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "name", "beneficiary.name.required")
        })

        surname(nullable: true, shared: "lettersOnly", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "surname", "beneficiary.surname.required")
        })

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "citizenship", "representative.obywatelstwo.required")
        })

        ownsAcceptor(nullable: true, validator: ConditionValidator.atLeastOneBeneficiaryOption)

        votesPercentage(nullable: true, min: 26, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.overQuarterOfVotes, "votesPercentage", "beneficiary.percentOfVotes.required")
        })

        position(nullable: true, validator: { return true })
        verification(nullable: true, validator: { value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "verification",
                    "representative.option.required")
        })
        pesel(nullable: true, validator: {value, cmd, errors ->
            return AcceptorVerification.PESEL.equals(cmd.verification) ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })
        documentType(nullable: true, validator: { return true })
        documentNumber(nullable: true, validator: { return true })
        documentIssueDate(nullable: true, validator: { return true })
        documentExpirationDate(nullable: true, validator: { return true })
        birthDate(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.BIRTH_DATE.equals(cmd.verification),
                    "birthDate", "beneficiary.dataUrodzenia.required")
        })
        birthCountry(nullable: true, validator: { return true })
        birthCity(nullable: true, validator: { return true })
        streetTitle(nullable: true, validator: { return true })
        street(nullable: true, validator: { return true })
        houseNumber(nullable: true, validator: { return true })
        flatNumber(nullable: true, validator: { return true })
        city(nullable: true, validator: { return true })
        postalCode(nullable: true, validator: { return true })
        postOffice(nullable: true, validator: { return true })
        country(nullable: true, validator: { return true })
        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    "isPolitician", "beneficiary.czyStanowiskoPolityczne.required")
        })
        isDirectPep(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.isPolitician,
                    propertyName, "representative.czyPepBezposredni.required")
        })
    }
}
