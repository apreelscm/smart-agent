package com.eservice.eumowy.validator

public class CustomValidator {

    public static def validate = {value, cmd,  errors, propertyName, regex ->
        RegexpValidator.validate(cmd, value, errors, propertyName,regex, "default.validation.regex.error")
    }

}
