package com.eservice.eumowy.validator

import com.eservice.eumowy.validator.models.AccountNumber
import org.apache.commons.lang.StringUtils

public class NumberValidator {
    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }

    public static def accountNumber = {value, cmd, errors, propertyName ->
        AccountNumber accountNumber = new AccountNumber(value)

        if (!accountNumber.isValid()) {
            errors.rejectValue(propertyName, "account.number.invalid.message")
            return false
        }

        return true
    }
}