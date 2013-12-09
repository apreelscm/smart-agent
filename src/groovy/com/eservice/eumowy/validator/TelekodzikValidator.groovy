package com.eservice.eumowy.validator

public class TelekodzikValidator {

    public static def validate = { value, cmd, errors, propertyName ->
        if(cmd.doladowania_tk && value?.isEmpty() ){
            errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
            return false
        }
        return true
    }
}
