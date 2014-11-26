package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.Validator


abstract class ActivityValidator extends Validator {
    protected abstract final Process process
    protected abstract final List calc

    protected boolean hasCalcProperty(String key, String value){
        return calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }
}
