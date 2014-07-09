package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PosExchangeCommand


class PosExchangeValidator {
    public static def validate = {posExchanges, cmd, errors ->
        Boolean hasChosenPosExchange = false

        for(PosExchangeCommand posExchange : posExchanges) {
            if(posExchange.isChoosen) {
                hasChosenPosExchange = true
                break
            }
        }

        if (posExchanges.size() > 0 && !hasChosenPosExchange) {
            errors.reject("at.least.one.pos.exchange.required")
            return false
        }

        return true
    }
}
