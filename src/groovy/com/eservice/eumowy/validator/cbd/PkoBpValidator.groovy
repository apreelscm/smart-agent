package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Client
import com.google.common.collect.Lists
import grails.util.Holders
import com.eservice.eumowy.Process


final class PkoBpValidator extends Validator {
    private static final String BILATERAL = "P";
    private static final String TRILATERAL = "E";

    PkoBpValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.contains(process, "wymianaUmowyZaplaty")
        String agreementType = cbdService.getRodzajUmowy(client.cbdId)

        if (TRILATERAL.equals(agreementType) || (!TRILATERAL.equals(agreementType) && !BILATERAL.equals(agreementType))) {
            return true
        }

        return hasActivity && BILATERAL.equals(agreementType)
    }

    @Override
    protected String getErrorMessageCode() {
        return "exchange.payment.required"
    }
}
