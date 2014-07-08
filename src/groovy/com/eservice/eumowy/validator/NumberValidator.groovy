package com.eservice.eumowy.validator

import org.apache.commons.lang.StringUtils

public class NumberValidator {
    private static final String PL_NUMBER = "2521"

    public static def validate = {value, cmd,  errors, propertyName ->
        RegexpValidator.validate(cmd, value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }

    public static def accountNumber = {value, cmd, errors, propertyName ->
        if (isAccountNumberInvalid(value)) {
            errors.rejectValue(propertyName, "account.number.invalid.message")
            return false
        }

        return true
    }

    private static boolean isAccountNumberInvalid(String accountNumber) {
        String trimmedAccountNumber = StringUtils.deleteWhitespace(accountNumber)

        if(!StringUtils.isNumeric(trimmedAccountNumber) || trimmedAccountNumber.length() != 26) {
            return true
        }

        String testableAccountNumber = getTestableAccountNumber(accountNumber)
        Integer weightsSum = getAccountNumberWeightSum(testableAccountNumber)

        return weightsSum % 97 != 1
    }

    private static String getTestableAccountNumber(String accountNumber) {
        String trimmedAccountNumber = StringUtils.deleteWhitespace(accountNumber)
        String controlSum = StringUtils.left(trimmedAccountNumber, 2)

        return trimmedAccountNumber.substring(2) + PL_NUMBER + controlSum
    }

    private static Integer getAccountNumberWeightSum(String accountNumber) {
        Integer[] weights = [1,10,3,30,9,90,27,76,81,34,49,5,50,15,53,45,62,38,89,17,73,51,25,56,75,71,31,19,93,57]

        Integer weightsSum = 0;

        for(int i = 0; i < 30; i++) {
            weightsSum += (accountNumber[29-i] as int) * weights[i]
        }

        return weightsSum
    }
}