package com.eservice.eumowy.validator

import grails.util.Environment

public class TerminalNumberValidator {

    static def validate = { value, cmd, errors ->
        def max = value ? Integer.valueOf(value) : 0
        def counter = 0

        cmd.points?.each { point ->
            counter += getTerminalCount(point)
        }

        cmd.poses?.each { point ->
            counter += getTerminalCount(point)
        }

        cmd.hirePaymentsByPoint?.each { hirePayment ->
            counter += hirePayment?.termCount ?: 0
        }

        log.info "liczba dodanych terminali w eUmowy [${counter}], dozwolona [${max}]"

        if (counter < max) {
            errors.rejectValue("liczbaTerminali", "default.notEqual.liczbaTerminali",[counter, max] as Object[], "")
            return false
        }

        return true
    }

    private static int getTerminalCount(def point) {
        int counter = 0

        counter += point?.dialupIlosc ?: 0
        counter += point?.dialupPPIlosc ?: 0
        counter += point?.vpnIlosc ?: 0
        counter += point?.vpnPPIlosc ?: 0
        counter += point?.sslIlosc ?: 0
        counter += point?.sslPPIlosc ?: 0
        counter += point?.gprsIlosc ?: 0
        counter += point?.gprsPPIlosc ?: 0
        counter += point?.pinPadIlosc ?: 0
        counter += point?.gprsIloscPortable ?: 0

        return counter
    }

}
