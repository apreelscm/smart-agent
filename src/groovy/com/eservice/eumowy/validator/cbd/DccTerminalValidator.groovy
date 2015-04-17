package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.collect.Lists


final class DccTerminalValidator extends Validator {

    DccTerminalValidator(Process process, Client client, List calculator) {
        super(process, client, calculator)
    }

    @Override
    protected boolean isValid() {
        boolean hasActivity = ActivityHelper.hasAtLeastOne(process, Lists.newArrayList("aneks", "dodanieAneksuKosztyPlus", "wymianaUmowyZaplaty"))
        boolean hasDccInCalc = hasCalcProperty("DCC_A", "TAK")
        boolean hasDccInTerminal = cbdService.czyTerminalDcc(client.mid)

        if (hasActivity && hasDccInCalc && hasDccInTerminal) return false

        return true
    }

    @Override
    protected String getErrorMessageCode() {
        return "dccChange.required"
    }

}
