package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import grails.util.Holders
import com.eservice.eumowy.Process


final class PKOBPbilateralValidator extends Validator {
    private final CbdService cbdService
    private final Process process

    public PKOBPbilateralValidator(Process process) {
        this.process = process
        cbdService = Holders.grailsApplication.mainContext.getBean("cbdService")
    }

    @Override
    protected boolean isValid() {
        return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getErrorMessageCode() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }
}
