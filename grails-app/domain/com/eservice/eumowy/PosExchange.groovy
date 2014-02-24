package com.eservice.eumowy

import groovy.transform.ToString

@ToString(includeNames = true,ignoreNulls = true)
class PosExchange implements Serializable{

    Integer cbdId
    Integer tpsId

    Integer posNumber;

    String name
    String address

    String type
    String model

    String newType
    String newModel

    String simType

    Boolean isChoosen

    static belongsTo = [process: Process]

//    static transients = ['simType']

    static mapping = {
        sort "ID"
        table name: "POS_EXCHANGE", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_EXCHANGE_SEQ']

        cbdId column: "CBD_ID"
        tpsId column: "TPS_ID"
        posNumber column: "POS_NUMBER"
        name column: "FULL_NAME"
        address column: "ADDRESS"
        type column: "TYPE"
        model column: "MODEL"
        newType column: "NEW_TYPE"
        newModel column: "NEW_MODEL"
        simType column: "SIM_TYPE"
        isChoosen column: "IS_CHOOSEN"
    }

    static constraints = {
        cbdId(nullable: true)
        tpsId(nullable: true)
        posNumber(nullable: true)
        name(nullable: true)
        address(nullable: true)
        type(nullable: true)
        model(nullable: true)
        newType(nullable: true)
        newModel(nullable: true)
        simType(nullable: true)
        isChoosen(nullable: true)
    }
}
