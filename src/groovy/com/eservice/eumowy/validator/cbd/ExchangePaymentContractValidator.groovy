package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process

import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_PLATNICZEJ

class ExchangePaymentContractValidator extends Validator {
    private static final String TRILATERAL = "E"

    ExchangePaymentContractValidator(Process process, Client client) {
        super(process, client)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.contains(process, WYMIANA_UMOWY_PLATNICZEJ)
        String agreementType = cbdService.getRodzajUmowy(client.cbdId)

        if (hasActivity) {
            return agreementType != TRILATERAL
        }

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "exchange.payment.trilateral.error"
    }
}
