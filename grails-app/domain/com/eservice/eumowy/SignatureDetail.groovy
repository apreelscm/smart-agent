package com.eservice.eumowy

class SignatureDetail {

    SignaturePurpose typ

    static belongsTo = [Signature]

    static mapping = {
        table name: "SIGNATURE_DETAIL", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_DETAIL_SEQ']
    }

    public enum SignaturePurpose {
        POINT, POS, REPRESENTATIVE
    }
}
