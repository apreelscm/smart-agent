package com.eservice.eumowy.command

import com.eservice.eumowy.annotation.Omit

import java.util.regex.Pattern

/**
 * User: pszkup
 * Date: 08.11.13
 * Time: 09:35
 */
abstract class BaseCommand implements Serializable{

    @Omit
    static def DEFAULT_VALUE = "~"
    @Omit
    transient def calculatorService
    @Omit
    transient def calc

    @Omit
    static def atLeastClosure = { value, cmd, errors, property, calcProperty ->
        def calcValue = cmd.calculatorService.getCalcProperty(cmd.calc, calcProperty)

        log.info("property: ${property}, value: ${value}, cal:${calcValue}")

        //warunek na brak wartości w kalkulatorze lub wartość domyślną w panelu
        if (DEFAULT_VALUE.equals(value) || !calcValue) {
            return true
        }

        def minValue = calcValue?.toString()?.isNumber() ? calcValue.toString().toBigDecimal() : BigDecimal.ZERO
        def currValue = value?.toString()?.isNumber() ? value.toString().toBigDecimal() : BigDecimal.ZERO

        if (currValue.compareTo(minValue) < 0) {
            errors.rejectValue(property, "default.atLeast.asCalc",[property] as Object[], "")
            return false
        }
        return true
    }

    @Omit
    static def maxLengthClosure = { value, cmd, errors, maxValue, propertyName, message ->
        if (value.length() > maxValue) {
            errors.rejectValue(propertyName, message)
            return false
        }
        return true
    }

    @Omit
    static def regexpValidationClosure = { value, errors, propertyName, patternStr, message ->
        log.trace("regexpValidationClosure - propertyName:"+propertyName+" value:"+value + " pattern:"+patternStr)

        if(!value){
            return true;
        }

        Pattern pattern = Pattern.compile(patternStr)
        if (!pattern.matcher(value).matches()){
            errors.rejectValue(propertyName, message,[value, propertyName] as Object[], "")
            return false
        }
        return true
    }

    @Omit
    static def numberValidationClosure = {value, cmd,  errors, propertyName ->
        regexpValidationClosure.call(value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }

    @Omit
    static def percentageValidationClosure = {value, cmd,  errors, propertyName ->
        regexpValidationClosure.call(value, errors, propertyName, "~|\\-|^(?:100(?:.0(?:0)?)?|\\d{1,2}(?:.\\d{1,2})?)", "default.validation.number.error")
    }

    @Omit
    static def customValidationClosure = {value, cmd,  errors, propertyName, regex ->
        regexpValidationClosure.call(value, errors, propertyName,regex, "default.validation.regex.error")
    }

}
