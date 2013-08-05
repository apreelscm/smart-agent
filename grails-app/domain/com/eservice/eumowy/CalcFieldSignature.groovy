package com.eservice.eumowy

class CalcFieldSignature implements Serializable {

    CalcField calcField;

    static belongsTo = [signature:Signature]

    static constraints = {
    }

    static mapping = {
        table name: "calcfield_signature", schema: "CBD_UMOWY"
    }

}
