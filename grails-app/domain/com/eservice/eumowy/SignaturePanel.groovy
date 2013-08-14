package com.eservice.eumowy

class SignaturePanel implements Serializable {

    Panel panel;

    static belongsTo = [signature:Signature]

    static constraints = {
    }

    static mapping = {
        table name: "SIGNATURE_PANEL", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'SIGNATURE_PANEL_SEQ']
    }
}
