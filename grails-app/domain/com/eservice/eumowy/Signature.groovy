package com.eservice.eumowy

class Signature implements Serializable {

    String name;
    Boolean active = true;
	String templatePath;
    Boolean forPoint = true;
	String description;
    String filename;
    Integer signatureOrder;
    Boolean sendToClient = true;
    Boolean showOnPreview = true;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel,
			documentFile:DocumentFile,
            subscriptionDefinitions:SubscriptionDefinition
    ]

    static constraints = {
       name(unique:true,blank:false)
       templatePath()
       filename(nullable:true,blank:true)
	   description()
    }

    static mapping = {
        table name: "SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_SEQ']
        sendToClient column: "SEND_TO_CLIENT"
        showOnPreview column: "SHOW_ON_PREVIEW"
    }

    String toString(){
        return name;
    }
}
