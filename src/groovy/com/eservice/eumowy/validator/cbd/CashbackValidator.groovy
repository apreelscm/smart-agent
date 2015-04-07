package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.CalculatorService
import grails.util.Holders
import com.eservice.eumowy.Process


final class CashbackValidator extends Validator {
    private final CalculatorService calculatorService

    CashbackValidator(Process process, List calculator) {
        super(process, calculator)
    }

    @Override
    protected boolean isValid() {
        boolean hasCashbackInCalc = hasCalcProperty("CASHBACK_A", "TAK")
        boolean hasCashbackInCbd = cbdService.czyCashback(process.client.cbdId)
        boolean hasCommision = cbdService.czyProwizjaDlaAkceptanta(process.client.cbdId)
        boolean hasCashbackActivity = ActivityHelper.contains(process, "dodanieCashBack")

        if (!hasCashbackInCalc) return true
        if (!hasCashbackInCbd && !hasCommision && !hasCashbackActivity) return false

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "addCashback.required"
    }
}
