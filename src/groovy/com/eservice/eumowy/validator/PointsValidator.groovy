package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PointCommand
import grails.validation.ValidationErrors

import java.text.DecimalFormat

public class PointsValidator {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        boolean hasPointErrors = false

        value.each { ptCmd ->
            if (ptCmd != null) {
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.minCenaNajmu = cmd.minCenaNajmu ? new BigDecimal(cmd.minCenaNajmu) : null
                ptCmd?.validate()
                if (ptCmd?.hasErrors()) {
                    ptCmd.errors.each { error ->
                        error.fieldErrors.each { fieldError ->
                            if (!fieldError.getField().equals("hasDodaniePrepaid")) { //eUmowy_ext-557
                                errors.reject(fieldError.getCode())
                                hasPointErrors = true
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
            point.errors.each { ValidationErrors error ->
                if (error.getAt("hasDodaniePrepaid")) {
                    pointsWithoutFormaDoladowania++;
                }
            }
        }

        return pointsWithoutFormaDoladowania
    }
}
