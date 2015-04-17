package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Client
import com.google.common.collect.Lists
import grails.util.Holders
import org.apache.commons.lang.StringUtils
import com.eservice.eumowy.Process;


abstract class Validator {
    protected Validator next
    protected CbdService cbdService;
    protected Client client;
    protected final Process process;
    protected final List calculator;

    protected abstract boolean isValid()
    protected abstract String getErrorMessageCode()

    public Validator(Process process, Client client) {
        this(process, client, Lists.newArrayList())
    }

    public Validator(Process process, Client client, List calculator) {
        this.process = process;
        this.calculator = calculator;
        this.client = client;
        cbdService = Holders.grailsApplication.mainContext.getBean("cbdService")
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
