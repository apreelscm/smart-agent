package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import grails.util.Holders


final class MIDValidator extends Validator {

    MIDValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        return cbdService.isMidCorrect(client.mid)
    }

    @Override
    protected String getErrorMessageCode() {
        return "client.mid.incorrect"
    }
}
