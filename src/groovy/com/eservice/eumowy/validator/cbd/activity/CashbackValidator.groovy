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
        return hasCalcProperty("CASHBACK_A", "TAK") && !ActivityHelper.hasAtLeastOne(process, ["dodanieCashBack", "zmianaWarunkowCashback"])
    }

    @Override
    protected String getErrorMessageCode() {
        return "cashback.activity.required"
    }
}
