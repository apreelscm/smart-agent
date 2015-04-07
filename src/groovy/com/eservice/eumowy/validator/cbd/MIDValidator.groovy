package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Process
import grails.util.Holders


final class MIDValidator extends Validator {

    MIDValidator(Process process) {
        super(process)
    }

    @Override
    protected boolean isValid() {
        return cbdService.isMidCorrect(process.client.mid)
    }

    @Override
    protected String getErrorMessageCode() {
        return "client.mid.incorrect"
    }
}
