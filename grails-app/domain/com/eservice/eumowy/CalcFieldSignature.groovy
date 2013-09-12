package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class CalcFieldSignature implements Serializable {

    CalcField calcField;

    static belongsTo = [signature:Signature]

    static constraints = {
        calcField()
    }

    static mapping = {
        table name: "CALCFIELD_SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CALCFIELD_SIGNATURE_SEQ']
    }
	
}