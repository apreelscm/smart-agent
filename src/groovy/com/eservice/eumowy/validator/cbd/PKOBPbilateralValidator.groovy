package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import grails.util.Holders
import com.eservice.eumowy.Process


final class PKOBPbilateralValidator extends Validator {

    PKOBPbilateralValidator(Process process, List calculator) {
        super(process, calculator)
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
