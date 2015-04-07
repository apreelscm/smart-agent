package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process

final class DccValidator extends ActivityValidator {

    DccValidator(Process process, List calc) {
        super(process, calc)
    }

    @Override
    protected boolean isValid() {
        if(hasCalcProperty("S_DCC", "TAK")) {
            return ActivityHelper.hasAtLeastOne(process, ["dodanieDcc", "zmianaWarunkowDcc"])
        }

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "dcc.activity.required"
    }
}
