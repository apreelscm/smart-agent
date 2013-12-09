package com.eservice.eumowy.validator

public class PosesValidator extends ValidatorUtils{

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate = { value, cmd, errors ->
        def hasPosErrors = false

        value.each {  ptCmd ->
            if (ptCmd != null){
                ptCmd?.calculatorService = cmd.calculatorService
                ptCmd?.calc = cmd.calc
                ptCmd?.validate()
                if(ptCmd?.hasErrors()){
                    hasPosErrors = true
                }
            }
        }

        if(cmd.poses?.size() > 0 && ValidatorUtils.hasMorePriceGroups(MAX_PRICE_GROUP_SIZE, cmd.poses)){
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
