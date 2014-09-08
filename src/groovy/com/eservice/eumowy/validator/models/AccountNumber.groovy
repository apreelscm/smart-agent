package com.eservice.eumowy.validator.models

import org.apache.commons.lang.StringUtils


class AccountNumber {
    private static final String PL_NUMBER = "2521"

    private String accountNumber

    public AccountNumber(String accountNumber) {
        this.accountNumber = accountNumber
    }

    public boolean isValid() {
        String trimmedAccountNumber = StringUtils.deleteWhitespace(accountNumber)

        if(!StringUtils.isNumeric(trimmedAccountNumber) || trimmedAccountNumber.length() != 26) {
            return false
        }

        String testableAccountNumber = getTestableAccountNumber()
        Integer weightsSum = getAccountNumberWeightSum(testableAccountNumber)

        return weightsSum % 97 == 1
    }

    private String getTestableAccountNumber() {
        String trimmedAccountNumber = StringUtils.deleteWhitespace(accountNumber)
        String controlSum = StringUtils.left(trimmedAccountNumber, 2)

        return trimmedAccountNumber.substring(2) + PL_NUMBER + controlSum
    }

    private Integer getAccountNumberWeightSum(String accountNumber) {
        Integer[] weights = [1,10,3,30,9,90,27,76,81,34,49,5,50,15,53,45,62,38,89,17,73,51,25,56,75,71,31,19,93,57]

        Integer weightsSum = 0;

        for(int i = 0; i < 30; i++) {
            weightsSum += (accountNumber[29-i] as int) * weights[i]
        }

        return weightsSum
    }
}
