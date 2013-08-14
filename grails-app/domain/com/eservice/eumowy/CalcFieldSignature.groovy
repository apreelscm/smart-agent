package com.eservice.eumowy

class CalcFieldSignature implements Serializable {

    CalcField calcField;

    static belongsTo = [signature:Signature]

    static constraints = {
    }

    static mapping = {
        table name: "CALCFIELD_SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'CALCFIELD_SIGNATURE_SEQ']
    }

}
