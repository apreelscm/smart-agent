package com.eservice.eumowy.validator

import org.apache.commons.lang.StringUtils

import static com.eservice.eumowy.validator.ValidatorUtils.*

class TextValidator {
    public static def isAlphanumeric = { value, cmd, errors, propertyName ->
        String withoutSpaces = StringUtils.replaceChars(value, ' ', '')

        if(!StringUtils.isAlphanumeric(withoutSpaces)) {
            errors.rejectValue(propertyName, "not.alphanumeric.error",
                    [getMessage(cmd, propertyName)] as Object[], getMessage(cmd, propertyName))
            return false
        }

        return true
    }
}
