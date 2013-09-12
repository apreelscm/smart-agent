package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class CalcField implements Serializable {

    String name;

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "CALCFIELD", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CALCFIELD_SEQ']
    }
	
}
