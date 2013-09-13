package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder


class Signature implements Serializable {

    String name;
    Boolean active = true;
	String templatePath;
	
	Integer subscriptionX;
	Integer subscriptionY;
	Integer subscriptionPageNumber;
	
	String managementSubscription1;
	String managementSubscription2;

    static hasMany = [
            calcFieldsSignature:CalcFieldSignature,
            panelsSignature:SignaturePanel
    ]

    static constraints = {
       name(unique:true,blank:false)
       templatePath()
       subscriptionX()
       subscriptionY()
       subscriptionPageNumber()
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
