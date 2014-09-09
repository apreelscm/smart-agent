package com.eservice.eumowy.validator

public class AtLeastValidator {

    static def DEFAULT_VALUE = "~"

    public static def validate = { value, cmd, errors, property, calcProperty ->
        def calcValue = cmd.calculatorService.getCalcProperty(cmd.calc, calcProperty)

        log.info("property: ${property}, value: ${value}, cal:${calcValue}")

        //warunek na brak wartości w kalkulatorze lub wartość domyślną w panelu
        if (DEFAULT_VALUE.equals(value) || !calcValue) {
            return true
        }

        def minValue = calcValue?.toString()?.isNumber() ? calcValue.toString().toBigDecimal() : BigDecimal.ZERO
        def currValue = value?.toString()?.isNumber() ? value.toString().toBigDecimal() : BigDecimal.ZERO

        if (currValue.compareTo(minValue) < 0) {
            errors.rejectValue(property, "default.atLeast.asCalc",[ValidatorUtils.getMessage(cmd, property)] as Object[], ValidatorUtils.getMessage(cmd, property))
            return false
        }
        return true
    }

    public static def validateAgainstMinValue = { currentValue, minValue, cmd, errors, propertyName ->
        if (currentValue.compareTo(minValue) < 0) {
            errors.rejectValue(propertyName, "default.atLeast.minValue",
                    [ValidatorUtils.getMessage(cmd, propertyName), minValue] as Object[], ValidatorUtils.getMessage(cmd, propertyName))
            return false
        }

        return true
    }

    /**
     * Wersja przepuszczajaca takze nulle.
     */
    public static def validateWithNull = { value, cmd, errors, property, calcProperty ->
        return (value == null) ? true: AtLeastValidator.validate(value, cmd, errors, property, calcProperty)
    }

    public static def atLeastOneBeneficiaryOption = { value, cmd, errors ->
        if(!value && !cmd.kontrolujeAkceptanta && !cmd.znaczaceUdzialy) {
            errors.rejectValue("posiadaAkceptanta", "atleast.one.relation.required")
            return false
        }
        return true
    }

    public static def oneVerificationDocument = {value, cmd, errors ->
        List<Boolean> fieldsToCheck = [cmd.beneficjentWeryfikacjaKRS, cmd.beneficjentWeryfikacjaDokumentTozsamosci,
                cmd.beneficjentWeryfikacjaGielda, cmd.beneficjentWeryfikacjaSpolka, cmd.beneficjentWeryfikacjaKsiega,
                cmd.beneficjentWeryfikacjaSchemat]

        if(cmd.isAkceptantAbroad() && ValidatorUtils.hasNotFilledField(fieldsToCheck)) {
            errors.reject("atleast.one.verification.document.required")
            return false
        }

        return true
    }

}
