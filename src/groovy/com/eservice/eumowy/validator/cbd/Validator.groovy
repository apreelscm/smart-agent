package com.eservice.eumowy.validator.cbd

import org.apache.commons.lang.StringUtils


abstract class Validator {
    protected Validator next

    protected abstract boolean isValid()
    protected abstract String getErrorMessageCode()

    public Validator addNext(Validator validator) {
        next = validator

        return this
    }

    public final String getValidationErrorCode() {
        if(isValid()) {
            if(next) return next.getValidationErrorCode()
        } else {
            return getErrorMessageCode()
        }

        return ""
    }
}
