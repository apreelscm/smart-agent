package com.eservice.eumowy.validator

import org.apache.commons.lang.StringUtils


class AddressValidator {
    public static def isAcceptorHouseNumberValid = { value, cmd, errors ->
        if(!cmd.hasNewUmowa && cmd.checkIfClientFromCbd()) {
            return StringUtils.isNotEmpty(value) && StringUtils.isAlphanumeric(value)
        }

        if(StringUtils.isEmpty(value) || !StringUtils.isNumeric(value)) {
            return false
        }

        return true
    }

    public static def isAcceptorApartmentNumberValid = {value, cmd, errors ->
        if(StringUtils.isEmpty(value)) {
            return true
        }

        if(!cmd.hasNewUmowa && cmd.checkIfClientFromCbd()) {
            return StringUtils.isAlphanumeric(value)
        }

        return StringUtils.isNumeric(value)
    }
}