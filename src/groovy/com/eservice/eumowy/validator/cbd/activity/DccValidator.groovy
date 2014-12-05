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
        if(hasCalcProperty("S_DCC", "TAK")) {
            return ActivityHelper.hasAtLeastOne(process, ["dodanieDcc", "zmianaWarunkowDcc"])
        }

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "dcc.activity.required"
    }

    //TODO: korzystac z hasCalcProperty z AcitivyValidatora
    protected boolean hasCalcProperty(String key, String value){
        return calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }
}
