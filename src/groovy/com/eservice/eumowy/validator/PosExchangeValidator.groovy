package com.eservice.eumowy.validator

import com.eservice.eumowy.command.PosExchangeCommand

class PosExchangeValidator {
    public static def validate = { posExchanges, cmd, errors ->
        Boolean hasChosenPosExchange = false

        for (PosExchangeCommand posExchange : posExchanges) {
            if (posExchange.isChoosen) {
                hasChosenPosExchange = true
                break
            }
        }

        if (posExchanges.size() > 0 && !hasChosenPosExchange) {
            errors.reject("at.least.one.pos.exchange.required")
            return false
        }

        if (posExchanges?.size() > 0 && anyPosExchangePriceIsLessThanCalcValue(posExchanges as List<PosExchangeCommand>)) {
            errors.reject("posExchange.priceLessThanRequired")
            return false
        }

        return true
    }

    private static boolean anyPosExchangePriceIsLessThanCalcValue(List<PosExchangeCommand> posExchangeCommands) {
        def posExchangesWithInvalidPrices = posExchangeCommands
            .findAll({ it -> it.isChoosen })
            .findAll({ it ->
                def currentPrice = it.currentPrice == null ? BigDecimal.ZERO : it.currentPrice
                def newPrice = it.newTermPayment == null ? BigDecimal.ZERO : it.newTermPayment

                return newPrice.compareTo(currentPrice) < 0
            })

        return !posExchangesWithInvalidPrices.isEmpty()
    }
}
