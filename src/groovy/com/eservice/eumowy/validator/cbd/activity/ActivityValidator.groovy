package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.Validator


abstract class ActivityValidator extends Validator {
    protected final List calc

    ActivityValidator(Process process, List calc) {
        super(process)
        this.calc = calc
    }
}
