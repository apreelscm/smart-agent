package com.eservice.eumowy.command

import com.eservice.eumowy.validator.ConditionValidator
import grails.validation.Validateable

/**
 * User: pszkup
 * Date: 30.10.13
 * Time: 14:23
 */
@Validateable(nullable = true)
class HirePaymentCommand implements Serializable{

    transient def calculatorService
    transient def calc

    Integer id
    Integer tpsId
    String posNumber

    Integer cbdId
    String name
    String address
    String type
    Integer termCount
    Integer ppCount
    Integer currentTermPayment
    Integer currentPpPayment
    Integer newTermPayment
    Integer newPpPayment
    Boolean isChoosen

    static constraints = {
        newTermPayment(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.newTermPayment ? ConditionValidator.atLeastCalcValue(value, cmd, errors, "newTermPayment", "CENA_NAJMU") : true;
        })
        newPpPayment(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.newPpPayment ? ConditionValidator.atLeastCalcValue(value, cmd, errors, "newPpPayment", "CENA_NAJMU_PP") : true;
        })
    }

}
