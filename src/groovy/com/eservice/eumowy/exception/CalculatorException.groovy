package com.eservice.eumowy.exception

import com.google.common.collect.Lists


class CalculatorException extends Exception {
    private final List<String> errors

    public CalculatorException(String message) {
        super(message)
        errors = Lists.newArrayList(message)
    }

    public CalculatorException(List<String> errors) {
        this.errors = errors
    }

    List<String> getErrors() {
        return errors
    }
}
