package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Process
import grails.util.Holders

final class PKOBPtrilateralValidator extends Validator{
    private final Process process
    private final CbdService cbdService

    public PKOBPtrilateralValidator(Process process) {
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
