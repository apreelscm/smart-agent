package com.eservice.eumowy.validator

import com.eservice.eumowy.command.HirePaymentCommand

class HirePaymentValidator {

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
        Set<Integer> normalPriceGroups = new HashSet<Integer>()

        hirePaymentCommands.each { HirePaymentCommand hpc ->
            if (hpc.isChoosen){
                normalPriceGroups.add(ValidatorUtils.getGroupValue(hpc.newTermPayment?.toInteger(), hpc.newPpPayment?.toInteger()))
            } else {
                normalPriceGroups.add(ValidatorUtils.getGroupValue(hpc.currentTermPayment, hpc.currentPpPayment))
            }
        }

        normalPriceGroups.removeAll(Collections.singleton(0)) //jesli obie ceny sa nullem to dostajemy 0

        normalPriceGroups.size() > maxSize;
    }
}
