package com.eservice.eumowy.validator


class RepresentativesValidator {
    public static def validate = { value, cmd, errors ->
        boolean isAkceptantAbroad = cmd.isAkceptantAbroad()

        value.each { representative ->

        }
    }
}
