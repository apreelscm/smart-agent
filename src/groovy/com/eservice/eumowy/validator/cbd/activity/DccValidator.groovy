package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process

final class DccValidator extends ActivityValidator {
    private final Process process
    private final List calc

    public DccValidator(Process process, List calc) {
        this.process = process
        this.calc = calc
    }

    @Override
    protected boolean isValid() {
        return hasCalcProperty("S_DCC", "TAK") && !ActivityHelper.hasAtLeastOne(process, ["dodanieDcc", "zmianaWarunkowDcc"])
    }

    @Override
    protected String getErrorMessageCode() {
        return "dcc.activity.required"
    }
}
