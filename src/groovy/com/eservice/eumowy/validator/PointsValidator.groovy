package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PointCommand

public class PointsValidator {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        boolean hasPointErrors = false

        value.each { ptCmd ->
            if (ptCmd != null) {
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if (ptCmd?.hasErrors()) {
                    ptCmd.errors.each { error ->
                        error.fieldErrors.each { fieldError ->
                            if (!fieldError.getField().equals("hasDodaniePrepaid")) { //eUmowy_ext-557
                                errors.reject(fieldError.getCode())
                                hasPointErrors = true
                                log.info(error)
                            }
                        }
                    }
                }
            }
        }

        if (cmd.points?.size() > 0 && ValidatorUtils.hasMorePriceGroups(MAX_PRICE_GROUP_SIZE, cmd.points)) {
            errors.reject("default.tooMany.groups")
        }

        if (hasPointErrors) {
            errors.rejectValue("points", "default.error.points",)
            return false
        }
        return true
    }

    public static int countOfPointsWithoutFormaDoladowania(List<PointCommand> points) {
        int pointsWithoutFormaDoladowania = 0;

        points.each { point ->
            point.errors.each { error ->  //error is grails.validation.ValidationErrors
                if (error.getAt("hasDodaniePrepaid")) {
                    pointsWithoutFormaDoladowania++;
                }
            }
        }

        return pointsWithoutFormaDoladowania
    }
}
