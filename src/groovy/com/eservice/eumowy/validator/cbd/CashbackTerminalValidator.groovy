package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.collect.Lists


final class CashbackTerminalValidator extends Validator {

    CashbackTerminalValidator(Process process, Client client, List calculator) {
        super(process, client, calculator)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.hasAtLeastOne(process, Lists.newArrayList("aneks", "dodanieAneksuKosztyPlus", "wymianaUmowyZaplaty"))
        boolean hasCashbackInCalc = hasCalcProperty("CASHBACK_A", "TAK")
        boolean hasCashbackInTerminal = cbdService.czyTermialCashback(client.mid)

        if (hasActivity && hasCashbackInCalc && hasCashbackInTerminal) return false

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "cashbackChange.required"
    }

}
