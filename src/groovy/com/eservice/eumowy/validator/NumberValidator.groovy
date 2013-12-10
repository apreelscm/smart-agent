package com.eservice.eumowy.validator

public class NumberValidator {

    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }
}