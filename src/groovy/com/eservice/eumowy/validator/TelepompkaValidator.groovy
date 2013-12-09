package com.eservice.eumowy.validator

public class TelepompkaValidator {

    public static def validate = { value, cmd, errors, propertyName ->
        if(cmd.doladowania_tp && value?.isEmpty() ){
            errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
            return false
        }
        return true
    }

}
