package com.eservice.eumowy.validator

import org.apache.commons.lang.StringUtils
import com.eservice.eumowy.validator.models.AccountNumber

import java.util.regex.Pattern

public class NumberValidator {
    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }

    public static def validateWithDash = { value, cmd, errors, propertyName ->

    }

    public static def validatePesel = {pesel, cmd, errors, propertyName ->
        if(!pesel) {
            errors.rejectValue(propertyName, "pesel.invalid")
            return false
        }

        String peselNumber = StringUtils.trim(pesel)
        int length = peselNumber.length()

        if(length != 11) {
            errors.rejectValue(propertyName, "pesel.invalid")
            return false
        }

        int[] weights = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3]
        int controlSum = peselNumber.substring(length - 1) as int

        int sum = 0
        for (int i = 0; i < 10; i++) {
            sum += (peselNumber[i] as int) * weights[i];
        }

        int control = 10 - (sum % 10);
        if (control == 10) {
            control = 0;
        }

        if(control != controlSum) {
            errors.rejectValue(propertyName, "pesel.invalid")
            return false
        }

        return true
    }

    public static def validateIsin = {isin, cmd, errors, propertyName ->
        Pattern ISIN_PATTERN = Pattern.compile("[A-Z]{2}([A-Z0-9]){9}[0-9]")

        if(StringUtils.isEmpty(isin)) {
            errors.rejectValue(propertyName, "isin.empty")
            return false
        }

        if(!ISIN_PATTERN.matcher(isin).matches()) {
            errors.rejectValue(propertyName, "wrong.isin.format")
            return false
        }

        return true
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