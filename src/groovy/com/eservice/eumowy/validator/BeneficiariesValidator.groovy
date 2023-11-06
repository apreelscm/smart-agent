package com.eservice.eumowy.validator

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import org.springframework.validation.Errors

import static com.eservice.eumowy.ActivityHelper.*

class BeneficiariesValidator {

    public static def validate = { List<BeneficiaryCommand> value, ProcessCommand cmd, Errors errors ->
        boolean beneficiariesHasErrors = false

        value.each { beneficiaryCommand ->
            if (beneficiaryCommand.name && beneficiaryCommand.surname) {
                beneficiaryCommand.processCommand = cmd
                beneficiaryCommand.validate()

                if (beneficiaryCommand.hasErrors()) {
                    beneficiariesHasErrors = true
                }
            }
        }

        if (value.size() > 0) {
            BeneficiaryCommand firstBeneficiary = value[0]

            if (!(firstBeneficiary.name && firstBeneficiary.surname)) {
                errors.reject("beneficiaries.atleast.one.required")
                return false
            }
        }

        if (beneficiariesHasErrors) {
            errors.reject("beneficiaries.has.errors")
            return false
        }

        return true
    }
}
