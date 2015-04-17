package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.CalculatorService
import com.eservice.eumowy.Client
import grails.util.Holders
import com.eservice.eumowy.Process


final class CashbackValidator extends Validator {
    CashbackValidator(Process process, Client client, List calculator) {
        super(process, client, calculator)
    }

    @Override
    protected boolean isValid() {
        boolean hasCashbackInCalc = hasCalcProperty("CASHBACK_A", "TAK")
        boolean hasCashbackInCbd = cbdService.czyCashback(client.cbdId)
        boolean hasCommision = cbdService.czyProwizjaDlaAkceptanta(client.cbdId)
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
