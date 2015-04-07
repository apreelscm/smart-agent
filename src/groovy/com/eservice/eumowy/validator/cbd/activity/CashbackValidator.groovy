package com.eservice.eumowy.validator.cbd.activity

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process


final class CashbackValidator extends ActivityValidator {

    CashbackValidator(Process process, List calc) {
        super(process, calc)
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
}
