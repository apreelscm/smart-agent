package com.eservice.eumowy.validator

public class ConditionValidator {

    static def DEFAULT_VALUE = "~"

    public static def atLeastCalcValue = { value, cmd, errors, property, calcProperty ->
        String calcValue = cmd.calculatorService.getCalcProperty(cmd.calc, calcProperty)

        if (DEFAULT_VALUE.equals(value) || !calcValue) return true

        BigDecimal minValue = getBigDecimalValue(calcValue)
        BigDecimal currValue = getBigDecimalValue(value)

        if (currValue.compareTo(minValue) == -1) {
            errors.rejectValue(property, "default.atLeast.asCalc",[ValidatorUtils.getMessage(cmd, property), minValue] as Object[], "default.atLeast.asCalc")
            return false
        }

        return true
    }

    public static def atMostCalcValue = { value, cmd, errors, property, calcProperty ->
        String calcValue = cmd.calculatorService.getCalcProperty(cmd.calc, calcProperty)

        if (DEFAULT_VALUE.equals(value) || !calcValue) return true

        BigDecimal maxValue = getBigDecimalValue(calcValue)
        BigDecimal currValue = getBigDecimalValue(value)

        if (currValue.compareTo(maxValue) == 1) {
            errors.rejectValue(property, "default.atMost.asCalc",[ValidatorUtils.getMessage(cmd, property), maxValue] as Object[], "default.atMost.asCalc")
            return false
        }

        return true
    }

    public static def atLeastMinValue = { currentValue, cmd, errors, propertyName, minValue ->
        if (currentValue.compareTo(minValue) == -1) {
            errors.rejectValue(propertyName, "default.atLeast.minValue",
                    [ValidatorUtils.getMessage(cmd, propertyName), minValue] as Object[], "default.atLeast.minValue")
            return false
        }

        return true
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

    private static BigDecimal getBigDecimalValue(def value) {
        return value?.toString()?.isNumber() ? value.toString().toBigDecimal() : BigDecimal.ZERO
    }
}
