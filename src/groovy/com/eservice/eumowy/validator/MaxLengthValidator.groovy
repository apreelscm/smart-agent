package com.eservice.eumowy.validator

public class MaxLengthValidator {

    public static def validate = { value, cmd, errors, maxValue, propertyName ->
        if (value){
            if (value.length() > maxValue) {
                errors.rejectValue(propertyName, "default.nameTooLong", [ValidatorUtils.getMessage(cmd, propertyName), maxValue] as Object[], "")
                return false
            } else {
                return true
            }
        }
        return true
    }

}
