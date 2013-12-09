package com.eservice.eumowy.validator

public class ScoringValidator {

    static def DEFAULT_VALUE = "~"

    public static def validate = { value, cmd, errors, propertyName, scoringPropertyName, scoringValue ->
        if (value && scoringValue == DEFAULT_VALUE) {
            errors.rejectValue(propertyName, "panel.scoring.field.required", [ValidatorUtils.getMessage(cmd, scoringPropertyName)] as Object[], "")
            return false
        }
        return true
    }
}
