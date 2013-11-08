package com.eservice.eumowy.command

import grails.validation.Validateable

/**
 * User: pszkup
 * Date: 30.10.13
 * Time: 14:23
 */
@Validateable
class HirePaymentCommand extends BaseCommand{

    Integer id
    Integer tpsId
    String posNumber

    Integer cbdId
    String name
    String address
    String type
    Integer termCount
    Integer ppCount
    BigDecimal currentTermPayment
    BigDecimal currentPpPayment
    BigDecimal newTermPayment
    BigDecimal newPpPayment
    Boolean isChoosen

    static constraints = {
        newTermPayment(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.newTermPayment ? atLeastClosure.call(value, cmd, errors, "newTermPayment", "CENA_NAJMU") : true;
        })
        newPpPayment(nullable:true, shared: "number", validator: { value, cmd, errors ->
            cmd.newPpPayment ? atLeastClosure.call(value, cmd, errors, "newPpPayment", "CENA_NAJMU_PP") : true;
        })
    }

}
