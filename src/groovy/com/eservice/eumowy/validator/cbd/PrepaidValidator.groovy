package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.collect.Lists

import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY
import static com.eservice.eumowy.ActivityHelper.contains
import static com.eservice.eumowy.ActivityHelper.hasAtLeastOne


final class PrepaidValidator extends Validator {
    private static final String LEASE = "UNA" //UMOWA NAJMU
    private static final String ATP_LEASE = "ATP"
    private static final String COOPERATION = "WUN" //UMOWA WSPOLPRACY

    PrepaidValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = hasAtLeastOne(process, Lists.newArrayList("dodaniePrepaid", "zmianaWarunkowPrepaid"))
        boolean hasExchangeLeaseActivity = contains(process, WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY)
        List<String> umwTypes = cbdService.getUmwTypes(client.cbdId)

        if (umwTypes.contains(COOPERATION) || umwTypes.contains(LEASE) || hasExchangeLeaseActivity) {
            return true
        }

        return !hasActivity && umwTypes.contains(ATP_LEASE)
    }

    @Override
    protected String getErrorMessageCode() {
        return 'exchange.lease.cooperation.required'
    }

}
