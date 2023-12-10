package com.eservice.eumowy.command

import com.eservice.eumowy.enums.options.AcceptorRelation
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
    AcceptorRelation acceptorRelation

    static constraints = {
        importFrom RepresentativeCommand

        salutation(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, true,
                    "salutation", "beneficiary.salutation.required")
        })

        name(nullable: true, shared: "lettersOnly", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, true,
                    "name", "beneficiary.name.required")
        })

        surname(nullable: true, shared: "lettersOnly", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, true,
                    "surname", "beneficiary.surname.required")
        })

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, true,
                    "citizenship", "representative.obywatelstwo.required")
        })

        votesPercentage(nullable: true, min: 25, max: 100, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.acceptorRelation == AcceptorRelation.HAS_OVER_QUARTER_OF_VOTES,
                    "votesPercentage", "beneficiary.percentOfVotes.required")
        })

        acceptorRelation(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.isCBDDataChangedManually,
                    "acceptorRelation", "beneficiary.acceptorRelation.required")
        })

        position(nullable: true, validator: { return true })
        verification(nullable: true, validator: { return true })
        pesel(nullable: true, validator: { return true })
        documentType(nullable: true, validator: { return true })
        documentNumber(nullable: true, validator: { return true })
        documentIssueDate(nullable: true, validator: { return true })
        documentExpirationDate(nullable: true, validator: { return true })
        birthDate(nullable: true, validator: { return true })
        birthCountry(nullable: true, validator: { return true })
        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, true,
                    "isPolitician", "beneficiary.czyStanowiskoPolityczne.required")
        })
        isDirectPep(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.isPolitician,
                    propertyName, "representative.czyPepBezposredni.required")
        })

        hasSignedContract(nullable: true, validator: { return true })
    }
}
