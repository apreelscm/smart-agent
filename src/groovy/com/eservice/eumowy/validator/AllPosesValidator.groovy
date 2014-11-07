package com.eservice.eumowy.validator

import com.eservice.eumowy.command.AllPosCommand


class AllPosesValidator {
    public static def validate = { value, cmd, errors ->
        String maxCalcValue = cmd.calculatorService.getCalcProperty(cmd.calc, "LICZBA_ZEST_POS_PROM_CEN_NAJ_1")
        int max = maxCalcValue?.isNumber() ? (maxCalcValue as int) : 0

        int chosenPoses = value?.findAll {it.czyWybrany}?.size()

        def hasPointErrors = false

        if(chosenPoses > max) {
            errors.reject("default.promo.rent.reduction.error")
            hasPointErrors = true
        }

        value.each { AllPosCommand posCommand ->
            if(posCommand.numerZestawuPos) {
                posCommand?.calculatorService = cmd.calculatorService
                posCommand?.calc = cmd.calc
                posCommand?.validate()
                if(posCommand?.hasErrors()){
                    hasPointErrors = true
                }
            }
        }

        if (hasPointErrors) {
            errors.rejectValue(propertyName, "default.error.allposes",)
            return false
        }

        return true
    }
}
