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

        if (posExchanges?.size() > 0 && priceLessThanCalcValue(posExchanges, cmd, errors, propertyName)) {
            errors.reject("posExchange.priceLessThanRequired")
            return false
        }

        return true
    }

    private static boolean priceLessThanCalcValue(List<PosExchangeCommand> posExchangeCommands, cmd, errors, propertyName) {
        List booleans = []

        posExchangeCommands.findAll({ it -> it.isChoosen })
            .collect({ it ->
                booleans.add(ConditionValidator.getBigDecimalValue(it.newTermPayment).compareTo(it.currentPrice) == -1)}
            )
        return booleans.contains(false)
    }
}
