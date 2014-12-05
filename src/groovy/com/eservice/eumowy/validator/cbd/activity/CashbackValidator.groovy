package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process


final class CashbackValidator extends ActivityValidator {
    private final Process process
    private final List calc

    public CashbackValidator(Process process, List calc) {
        this.process = process
        this.calc = calc
    }

    @Override
    protected boolean isValid() {
        if(hasCalcProperty("CASHBACK_A", "TAK")) {
            return ActivityHelper.hasAtLeastOne(process, ["dodanieCashBack", "zmianaWarunkowCashback"])
        }

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "cashback.activity.required"
    }

    //TODO: korzystac z hasCalcProperty z AcitivyValidatora
    protected boolean hasCalcProperty(String key, String value){
        return calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }
}
