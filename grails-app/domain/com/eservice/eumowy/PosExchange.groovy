package com.eservice.eumowy

import groovy.transform.ToString

@ToString(includeNames = true,ignoreNulls = true)
class PosExchange implements Serializable{

    Integer cbdId
    Integer tpsId

    String posNumber

    String name
    String address

    String model

    String newType
    String newModel

    String simType

    BigDecimal currentPrice
    BigDecimal newTermPayment

    Boolean isChoosen

    Boolean integrationWithCashSystem
    String integrationType
    String integrationSystemSupplier

    static belongsTo = [process: Process]

    static mapping = {
        sort "ID"
        table name: "POS_EXCHANGE", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_EXCHANGE_SEQ']

        cbdId column: "CBD_ID"
        tpsId column: "TPS_ID"
        posNumber column: "POS_NUMBER"
        name column: "FULL_NAME"
        address column: "ADDRESS"
        model column: "MODEL"
        newType column: "NEW_TYPE"
        newModel column: "NEW_MODEL"
        simType column: "SIM_TYPE"
        currentPrice column: "CURRENT_PRICE"
        newTermPayment column: "NEW_TERM_PAYMENT"
        isChoosen column: "IS_CHOOSEN"

        integrationWithCashSystem column: "CASH_SYSTEM_INTEGRATION"
        integrationType column: "INTEGRATION_TYPE"
        integrationSystemSupplier column: "INTEGRATION_SYSTEM_SUPPLIER"
    }

    static constraints = {
        cbdId(nullable: true)
        tpsId(nullable: true)
        posNumber(nullable: true)
        name(nullable: true)
        address(nullable: true)
        model(nullable: true)
        newType(nullable: true)
        newModel(nullable: true)
        simType(nullable: true)
        currentPrice(nullable: true)
        newTermPayment(nullable: true)
        isChoosen(nullable: true)
        integrationWithCashSystem (nullable: true)
        integrationType (nullable: true)
        integrationSystemSupplier (nullable: true)
    }
}
