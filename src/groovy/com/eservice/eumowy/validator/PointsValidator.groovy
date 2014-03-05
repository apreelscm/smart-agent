package com.eservice.eumowy.validator

public class PointsValidator {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        boolean hasPointErrors = false
        int dodaniePrepaidErrorCount = 0 //eUmowy_ext-557
        int pointsCount = value.size()

        value.each {  ptCmd ->
            if (ptCmd != null){
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if(ptCmd?.hasErrors()){
                    ptCmd.errors.each { error -> //error is grails.validation.ValidationErrors
                        if (error.getAt("hasDodaniePrepaid")) {
                            dodaniePrepaidErrorCount++;
                        } else {
                            error.fieldErrors.each { fieldError ->
                                errors.reject(fieldError.getCode())
                            }
                            log.info(error)
                            hasPointErrors = true
                        }
                    }
                }
            }
        }

        if(pointsCount > 0 && (dodaniePrepaidErrorCount == pointsCount)) {
            errors.reject("default.atLeastOne.doladowania.funkcjaTerminala.point")
            hasPointErrors = true
        }

        if(cmd.points?.size() > 0 && ValidatorUtils.hasMorePriceGroups(MAX_PRICE_GROUP_SIZE, cmd.points)){
            errors.reject("default.tooMany.groups")
        }

        if (hasPointErrors) {
            errors.rejectValue("points", "default.error.points",)
            return false
        }
        return true
    }
}
