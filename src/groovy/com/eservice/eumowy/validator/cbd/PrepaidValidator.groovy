package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.base.Strings
import com.google.common.collect.Lists


final class PrepaidValidator extends Validator {
    private static final String LEASE = "UNA" //UMOWA NAJMU
    private static final String ATP_LEASE = "ATP"
    private static final String COOPERATION = "WUN" //UMOWA WSPOLPRACY

    PrepaidValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.hasAtLeastOne(process, Lists.newArrayList("dodaniePrepaid", "zmianaWarunkowPrepaid"))
        String umwType = cbdService.getUmwTyp(client.cbdId)

        if (COOPERATION.equals(umwType) || LEASE.equals(umwType)) {
            return true
        }

        return !hasActivity && isLeaseType(umwType)
    }

    @Override
    protected String getErrorMessageCode() {
        return 'exchange.lease.cooperation.required'
    }

    private static boolean isLeaseType(String umwType) {
        return LEASE.equals(umwType) || ATP_LEASE.equals(umwType)
    }

}
