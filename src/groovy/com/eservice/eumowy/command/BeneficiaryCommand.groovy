package com.eservice.eumowy.command

import com.eservice.eumowy.validator.CustomValidator


class BeneficiaryCommand extends RepresentativeCommand {
    Boolean ownsAcceptor
    Boolean controlAcceptor

    Boolean overQuarterOfVotes
    Integer percentOfVotes

    static constraints = {
        importFrom RepresentativeCommand

        ownsAcceptor(nullable: true, validator: {value, cmd, errors ->
            if(!value && !cmd.controlAcceptor && !cmd.overQuarterOfVotes) {
                errors.rejectValue("ownsAcceptor", "atleast.one.relation.required")
                return false
            }
            return true
        })

        percentOfVotes(nullable: true, min: 26, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.overQuarterOfVotes, "percentOfVotes", "beneficiary.percentOfVotes.required")
        })
    }
}
