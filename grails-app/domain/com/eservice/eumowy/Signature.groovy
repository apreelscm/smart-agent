package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder


class Signature implements Serializable {

    String name;
    Boolean active = true;
	String templatePath;
    Boolean forPoint = true;

	Integer subscriptionX;
	Integer subscriptionY;
	Integer subscriptionPageNumber;
	
	Integer phSubscriptionX;
	Integer phSubscriptionY;
	Integer phSubscriptionPageNumber;

	String managementSubscription1;
	String managementSubscription2;

    Integer signatureOrder;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel,
			documentFile:DocumentFile
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
    }

    static mapping = {
        table name: "SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_SEQ']
    }

    String toString(){
        return name;
    }
	
}
