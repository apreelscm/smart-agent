package com.eservice.eumowy.command

import com.eservice.eumowy.validator.AtLeastValidator
import com.eservice.eumowy.validator.CustomValidator
import grails.validation.Validateable

@Validateable
class BeneficiaryCommand extends RepresentativeCommand implements Serializable {
    Boolean posiadaAkceptanta
    Boolean kontrolujeAkceptanta

    Boolean znaczaceUdzialy
    Integer procentUdzialow

    static constraints = {
        importFrom RepresentativeCommand

        posiadaAkceptanta(nullable: true, validator: AtLeastValidator.atLeastOneBeneficiaryOption)

        procentUdzialow(nullable: true, min: 26, shared: "percentage", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.znaczaceUdzialy, "procentUdzialow", "beneficiary.percentOfVotes.required")
        })
    }
}
