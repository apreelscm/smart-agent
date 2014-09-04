package com.eservice.eumowy.validator

public class CustomValidator {
    public static def validate = {value, cmd,  errors, propertyName, regex ->
        RegexpValidator.validate(cmd, value, errors, propertyName,regex, "default.validation.regex.error")
    }

    public static def validateRequired = {value, errors, condition, propertyName, messageCode ->
        if(!value && condition) {
            errors.rejectValue(propertyName, messageCode)
            return false
        }
        return true
    }
}
