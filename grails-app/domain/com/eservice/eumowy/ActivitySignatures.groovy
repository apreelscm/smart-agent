package com.eservice.eumowy

class ActivitySignatures implements Serializable {

    Signature signature;

    boolean mandatory // not shown on any of list

    Integer numberOfList = 0;

    static belongsTo = [activity:Activity]

    Activity required

    static constraints = {
        mandatory()
        numberOfList(range: 1..2)
        signature()
    }

    static mapping = {
        table name: "ACTIVITY_SIGNATURES", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ACTIVITY_SIGNATURES_SEQ']
        sort id: "desc"
        required column: 'required_activity'
    }

}
