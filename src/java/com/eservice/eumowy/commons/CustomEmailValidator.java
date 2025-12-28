package com.eservice.eumowy.commons;

import org.apache.commons.validator.routines.EmailValidator;

public class CustomEmailValidator extends EmailValidator {
    private static final CustomEmailValidator CUSTOM_EMAIL_VALIDATOR = new CustomEmailValidator(false);

    public static CustomEmailValidator getInstance() {
        return CUSTOM_EMAIL_VALIDATOR;
    }

    protected CustomEmailValidator(boolean allowLocal) {
        super(allowLocal);
    }

    @Override
    protected boolean isValidDomain(String domain) {
        return true;
    }
}
