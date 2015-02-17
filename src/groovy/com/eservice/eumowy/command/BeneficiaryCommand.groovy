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

        documentNumber(nullable: false, maxSize: 20, shared: "alphanumeric")

        ownsAcceptor(nullable: true, validator: ConditionValidator.atLeastOneBeneficiaryOption)

        votesPercentage(nullable: true, min: 26, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.overQuarterOfVotes, "votesPercentage", "beneficiary.percentOfVotes.required")
        })

        locationType(nullable: true, validator: { return true })

        country(nullable: true, blank: true)
    }
}
