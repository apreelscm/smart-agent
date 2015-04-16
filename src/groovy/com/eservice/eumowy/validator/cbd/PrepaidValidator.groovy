package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process
import com.google.common.base.Strings
import com.google.common.collect.Lists


class PrepaidValidator extends Validator {
    private static final String LEASE = "UNA" //UMOWA NAJMU
    private static final String COOPERATION = "WUN" //UMOWA WSPOLPRACY

    PrepaidValidator(Process process) {
        super(process)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.hasAtLeastOne(process, Lists.newArrayList("dodaniePrepaid", "zmianaWarunkowPrepaid"))
        String umwType = cbdService.getUmwTyp(process.client.cbdId)

        if (COOPERATION.equals(umwType) || (!LEASE.equals(umwType) && !COOPERATION.equals(umwType))) {
            return true
        }

        return !hasActivity && LEASE.equals(umwType)
    }

    @Override
    protected String getErrorMessageCode() {
        return 'exchange.lease.cooperation.required'
    }

}
