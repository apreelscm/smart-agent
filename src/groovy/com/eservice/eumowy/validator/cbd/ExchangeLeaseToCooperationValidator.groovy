package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process

final class ExchangeLeaseToCooperationValidator extends Validator {
    private static final String LEASE = "UNA" //UMOWA NAJMU
    private static final String ATP_LEASE = "ATP"
    private static final String COOPERATION = "WUN" //UMOWA WSPOLPRACY

    ExchangeLeaseToCooperationValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.contains(process, "wymianaUmowyNajmu")
        List<String> umwTypes = cbdService.getUmwTypes(client.cbdId)

        if (umwTypes.contains(COOPERATION) || umwTypes.contains(LEASE)) {
            return true
        }

        return hasActivity && umwTypes.contains(ATP_LEASE)
    }

    @Override
    protected String getErrorMessageCode() {
        return 'exchange.lease.cooperation.required'
    }
}
