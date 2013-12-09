package com.eservice.eumowy.validator

public class PointsValidator extends ValidatorUtils {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        def hasPointErrors = false

        value.each {  ptCmd ->
            if (ptCmd != null){
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if(ptCmd?.hasErrors()){
                    ptCmd.errors.each {
                        it.fieldErrors.each {fieldError ->
                            errors.reject(fieldError.getCode())
                        }
                        log.info(it)
                    }
                    hasPointErrors = true
                }
            }
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
