package com.eservice.eumowy.validator

import java.util.regex.Pattern

public class RegexpValidator {

    public static def validate = { cmd, value, errors, propertyName, patternStr, message ->
        log.trace("regexpValidationClosure - propertyName:"+propertyName+" value:"+value + " pattern:"+patternStr)

        if(!value){
            return true;
        }

        Pattern pattern = Pattern.compile(patternStr)
        if (!pattern.matcher(value).matches()){
            errors.rejectValue(propertyName, message, [value, ValidatorUtils.getMessage(cmd, propertyName)] as Object[], "")
            return false
        }
        return true
    }

}