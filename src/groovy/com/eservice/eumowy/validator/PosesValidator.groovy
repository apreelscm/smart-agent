package com.eservice.eumowy.validator

public class PosesValidator {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        boolean hasPosErrors = false
        int dodaniePrepaidErrorCount = 0; //eUmowy_ext-557
        int posesCount = value.size()

        value.each { ptCmd ->
            if (ptCmd != null) {
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if (ptCmd?.hasErrors()) {
                    ptCmd.errors.each { error ->  //error is grails.validation.ValidationErrors
                        if (error.getAt("hasDodaniePrepaid")) {
                            dodaniePrepaidErrorCount++;
                        } else {
                            error.fieldErrors.each { fieldError ->
                                errors.reject(fieldError.getCode())
                            }
                            log.info(error)
                            hasPosErrors = true
                        }
                    }
                }
            }
        }

        if(posesCount > 0 && (dodaniePrepaidErrorCount == posesCount)) {
            errors.reject("default.atLeastOne.doladowania.funkcjaTerminala.pos")
            hasPosErrors = true
        }

        if (cmd.poses?.size() > 0 && ValidatorUtils.hasMorePriceGroups(MAX_PRICE_GROUP_SIZE, cmd.poses)) {
            errors.reject("default.tooMany.groups")
            return false
        }

        if (hasPosErrors) {
            errors.rejectValue("poses", "default.error.poses",)
            return false
        }

        return true
    }
}
