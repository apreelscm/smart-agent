package com.eservice.eumowy.validator

import com.eservice.eumowy.command.HirePaymentCommand

class HirePaymentValidator extends ValidatorUtils {

    static int MAX_PRICE_GROUP_SIZE = 3

    public static def validate =  { value, cmd, errors ->
        def hasError = false

        value.each { hpCmd ->
            hpCmd?.calculatorService = cmd.calculatorService
            hpCmd?.validate()
            if(hpCmd?.hasErrors()){
                hpCmd.errors.each {
                    log.info(it)
                }
                hasError = true
            }
        }

        if (hasError) {
            errors.reject("default.error.hire.payments",)
            return false
        }

        if(value?.size() > 0 && hasMoreThanPriceGroups(MAX_PRICE_GROUP_SIZE, value)){
            errors.reject("default.tooMany.groups")
            return false
        }

        return true
    }

    private static boolean hasMoreThanPriceGroups(int maxSize, List<HirePaymentCommand> hirePaymentCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()

        hirePaymentCommands.each { HirePaymentCommand hpc ->
            if (hpc.isChoosen){
                normalPriceGroups.add(getGroupValue(hpc.newTermPayment, hpc.newPpPayment))
            } else {
                normalPriceGroups.add(getGroupValue(hpc.currentTermPayment, hpc.currentPpPayment))
            }
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO)) //jesli obie ceny sa nullem to dostajemy 0

        normalPriceGroups.size() > maxSize? true : false;
    }
}
