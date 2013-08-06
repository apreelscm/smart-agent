package com.eservice.eumowy


class Signature implements Serializable {

    String name;
    Boolean active = true;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel
    ]

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "signature", schema: "CBD_UMOWY"
    }

    String toString(){
        return name;
    }
}
