package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.collect.Lists


final class ExchangeLeaseToCooperationValidator extends Validator {
    private static final String LEASE = "UNA" //UMOWA NAJMU
    private static final String COOPERATION = "WUN" //UMOWA WSPOLPRACY

    ExchangeLeaseToCooperationValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.contains(process, "wymianaUmowyNajmu")
        String umwType = cbdService.getUmwTyp(client.cbdId)

        if (COOPERATION.equals(umwType) || (!LEASE.equals(umwType) && !COOPERATION.equals(umwType))) {
            return true
        }

        return hasActivity && LEASE.equals(umwType)
    }

    @Override
    protected String getErrorMessageCode() {
        return 'exchange.lease.cooperation.required'
    }
}
