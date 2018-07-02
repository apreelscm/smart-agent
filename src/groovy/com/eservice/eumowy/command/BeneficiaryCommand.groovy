package com.eservice.eumowy.command

import com.eservice.eumowy.validator.ConditionValidator
import com.eservice.eumowy.validator.CustomValidator
import grails.validation.Validateable

@Validateable(nullable = true)
class BeneficiaryCommand extends RepresentativeCommand implements Serializable {
    Boolean ownsAcceptor
    Boolean controlsAcceptor
    Boolean overQuarterOfVotes
    Integer votesPercentage

    static constraints = {
        importFrom RepresentativeCommand

        salutation(nullable: true)
        name(nullable: true, shared: "lettersOnly")
        surname(nullable: true, shared: "lettersOnly")

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "citizenship", "representative.obywatelstwo.required")
        })

        ownsAcceptor(nullable: true, validator: ConditionValidator.atLeastOneBeneficiaryOption)

        votesPercentage(nullable: true, min: 26, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.overQuarterOfVotes, "votesPercentage", "beneficiary.percentOfVotes.required")
        })

        position(nullable: true, validator: { return true })
        verification(nullable: true, validator: { return true })
        pesel(nullable: true, validator: { return true })
        documentType(nullable: true, validator: { return true })
        documentNumber(nullable: true, validator: { return true })
        birthDate(nullable: true, validator: { return true })
        birthCountry(nullable: true, validator: { return true })
        address(nullable: true, validator: { return true })
        country(nullable: true, validator: { return true })
        isPolitician(nullable: true, validator: { return true })
    }
}
