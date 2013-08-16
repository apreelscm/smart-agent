package com.eservice.eumowy

class ActivitySignatures implements Serializable {

    Signature signature;

    boolean mandatory // not shown on any of list

    Integer numberOfList = 0;

    static belongsTo = [activity:Activity]

    static constraints = {
    }

    static mapping = {
        table name: "ACTIVITY_SIGNATURES", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ACTIVITY_SIGNATURES_SEQ']
    }

}
