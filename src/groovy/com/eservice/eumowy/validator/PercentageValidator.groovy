package com.eservice.eumowy.validator

public class PercentageValidator extends RegexpValidator{

    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, "~|\\-|^(?:100(?:.0(?:0)?)?|\\d{1,2}(?:.\\d{1,2})?)", "default.validation.number.error")
    }

}
