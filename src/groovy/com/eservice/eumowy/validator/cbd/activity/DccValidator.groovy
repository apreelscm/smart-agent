package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process

final class DccValidator extends ActivityValidator {

    DccValidator(Process process, Client client, List calc) {
        super(process, client, calc)
    }

    @Override
    protected boolean isValid() {
        if(hasCalcProperty("CZY_DCC", "TAK")) {
            return ActivityHelper.hasAtLeastOne(process, ["dodanieDcc", "zmianaWarunkowDcc"])
        }

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "dcc.activity.required"
    }
}
