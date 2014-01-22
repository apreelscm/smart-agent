package com.eservice.eumowy.validator

import grails.util.Environment

public class TerminalNumberValidator {

    static def validate = { value, cmd, errors ->
        if (Environment.getCurrent().equals(Environment.TEST)) {
            return true
        } else if (Environment.getCurrent().equals(Environment.DEVELOPMENT)){
            return true
        }

        def max = value ? Integer.valueOf(value) : 0
        def counter = 0

        cmd.points?.each { point ->
            counter += point?.dialupIlosc != null ? point?.dialupIlosc : 0
            counter += point?.vpnIlosc != null ? point?.vpnIlosc : 0
            counter += point?.sslIlosc != null ? point?.sslIlosc : 0
            counter += point?.gprsIlosc != null ? point?.gprsIlosc : 0
            counter += point?.pinPadIlosc != null ? point?.pinPadIlosc : 0
            counter += point?.wifiIlosc != null ? point?.wifiIlosc : 0
        }

        cmd.poses?.each { point ->
            counter += point?.dialupIlosc != null ? point?.dialupIlosc : 0
            counter += point?.vpnIlosc != null ? point?.vpnIlosc : 0
            counter += point?.sslIlosc != null ? point?.sslIlosc : 0
            counter += point?.gprsIlosc != null ? point?.gprsIlosc : 0
            counter += point?.pinPadIlosc != null ? point?.pinPadIlosc : 0
            counter += point?.wifiIlosc != null ? point?.wifiIlosc : 0
        }

        log.info "liczba dodanych terminali w eUmowy [${counter}], dozwolona [${max}]"

        if (counter < max) {
            errors.rejectValue("liczbaTerminali", "default.notEqual.liczbaTerminali",[counter, max] as Object[], "")
            return false
        }
        return true
    }

}
