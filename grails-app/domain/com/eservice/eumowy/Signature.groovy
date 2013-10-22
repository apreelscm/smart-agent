package com.eservice.eumowy

class Signature implements Serializable {

    String name;
    Boolean active = true;
	String templatePath;
    Boolean forPoint = true;
	String description;
    Integer signatureOrder;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel,
			documentFile:DocumentFile,
            subscriptionDefinitions:SubscriptionDefinition
    ]

    static constraints = {
       name(unique:true,blank:false)
       templatePath()
	   description()
    }

    static mapping = {
        table name: "SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_SEQ']
    }

    String toString(){
        return name;
    }
}
