package com.eservice.eumowy

import groovy.transform.ToString

@ToString(includeNames = true,ignoreNulls = true)
class HirePayment implements Serializable{

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
    Boolean isVisible

    static belongsTo = [process: Process]

    static mapping = {
        sort "ID"
        table name: "HIRE_PAYMENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.HIRE_PAYMENT_SEQ']

        cbdId column: "CBD_ID"
        tpsId column: "TPS_ID"
        name column: "FULL_NAME"
        address column: "ADDRESS"
        type column: "TYPE"
        posNumber column: "POS_NUMBER"
        termCount column: "TERM_COUNT"
        ppCount  column: "PP_COUNT"
        currentTermPayment column: "CURRENT_TERM_PAYMENT"
        currentPpPayment column: "CURRENT_PP_PAYMENT"
        newTermPayment column: "NEW_TERM_PAYMENT"
        newPpPayment column: "NEW_PP_PAYMENT"
        isChoosen column: "IS_CHOOSEN"
        isVisible column: "IS_VISIBLE"
    }

    static constraints = {
        cbdId(nullable: true)
        tpsId(nullable: true)
        name(nullable: true)
        address(nullable: true)
        type(nullable: true)
        posNumber(nullable: true)
        termCount(nullable: true)
        ppCount(nullable: true)
        currentTermPayment(nullable: true)
        currentPpPayment(nullable: true)
        newTermPayment(nullable: true)
        newPpPayment(nullable: true)
        isChoosen(nullable: true)
        isVisible(nullable: true)
    }
}
