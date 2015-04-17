package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.Validator


abstract class ActivityValidator extends Validator {
    protected final List calc

    ActivityValidator(Process process, Client client, List calc) {
        super(process, client)
        this.calc = calc
    }
}
