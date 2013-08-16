package com.eservice.eumowy


class Signature implements Serializable {

    String name;
    Boolean active = true;
	
	Integer subscriptionX;
	Integer subscriptionY;
	Integer subscriptionXScale;
	Integer subscriptionYScale;
	Integer subscriptionPageNumber;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel
    ]

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_SEQ']
    }

    String toString(){
        return name;
    }
}
