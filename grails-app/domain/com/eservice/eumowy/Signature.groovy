package com.eservice.eumowy

class Signature implements Serializable {

    String name;
    Boolean active = true;
	String templatePath;
    Boolean forPoint = true;

    //TO DELETE in future - begin
	Integer subscriptionX;
	Integer subscriptionY;
	Integer subscriptionPageNumber;
	
	Integer phSubscriptionX;
	Integer phSubscriptionY;
	Integer phSubscriptionPageNumber;

	String managementSubscription1;
	String managementSubscription2;
	String description;
    //TO DELETE in future - end

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
       subscriptionX(nullable: true)
       subscriptionY(nullable: true)
	   phSubscriptionX(nullable: true)
	   phSubscriptionY(nullable: true)
	   phSubscriptionPageNumber(nullable: true)
       subscriptionPageNumber(nullable: true)
       managementSubscription1()
       managementSubscription2()
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
