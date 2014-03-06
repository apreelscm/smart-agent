package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PointCommand

public class PosesValidator {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        boolean hasPosErrors = false

        value.each { ptCmd ->
            if (ptCmd != null) {
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if (ptCmd?.hasErrors()) {
                    ptCmd.errors.each { error ->  //error is grails.validation.ValidationErrors
                        error.fieldErrors.each { fieldError ->
                            if (!fieldError.getField().equals("hasDodaniePrepaid")) { //eUmowy_ext-557
                                errors.reject(fieldError.getCode())
                                hasPosErrors = true
                                log.info(error)
                            }
                        }
                    }
                }
            }
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

    public static int countOfPosesWithoutFormaDoladowania(List<PointCommand> poses) {
        int posesWithoutFormaDoladowania = 0;

        poses.each { point ->
            point.errors.each { error ->  //error is grails.validation.ValidationErrors
                if (error.getAt("hasDodaniePrepaid")) {
                    posesWithoutFormaDoladowania++;
                }
            }
        }

        return posesWithoutFormaDoladowania
    }
}
