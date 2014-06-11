package com.eservice.eumowy.validator

import org.apache.commons.lang.StringUtils

public class NumberValidator {

    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
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
        for (int i = 0; i < length - 1; i++) {
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
}