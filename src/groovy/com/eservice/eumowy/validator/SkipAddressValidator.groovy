package com.eservice.eumowy.validator

class SkipAddressValidator {

    public static def validate = { value, cmd, errors, propertyName ->
        if(value.isEmpty() && cmd.checkIfClientFromCbd()){
            return true
        }

        if(value.isEmpty()){ //cannot use contraint blank: true because of cbd values
            errors.rejectValue(propertyName, "default.cantBeEmpty", [ValidatorUtils.getMessage(cmd, propertyName)] as Object[], "")
            return false
        }

        return true
    }
}
