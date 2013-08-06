package com.eservice.eumowy

class SignaturePanel implements Serializable {

    Panel panel;

    static belongsTo = [signature:Signature]

    static constraints = {
    }

    static mapping = {
        table name: "signaturepanel", schema: "CBD_UMOWY"
    }
}
