package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import com.google.common.collect.Lists
import grails.util.Holders
import org.apache.commons.lang.StringUtils
import com.eservice.eumowy.Process;


abstract class Validator {
    protected Validator next
    protected CbdService cbdService;
    protected final Process process;
    protected final List calculator;

    protected abstract boolean isValid()
    protected abstract String getErrorMessageCode()

    public Validator(Process process) {
        this(process, Lists.newArrayList())
    }

    public Validator(Process process, List calculator) {
        this.process = process;
        this.calculator = calculator;
        cbdService = Holders.grailsApplication.mainContext.getBean("cbdService")
    }

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

    protected boolean hasCalcProperty(String key, String value){
        return calculator?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }
}
