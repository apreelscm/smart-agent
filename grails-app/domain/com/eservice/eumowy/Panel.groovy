package com.eservice.eumowy

import groovy.transform.ToString

@ToString
class Panel implements Serializable{

    String name;
    Integer orderNo;

    static constraints = {
        name(unique:true,blank:false)
        orderNo()
    }

    static mapping = {
        table name: "PANEL", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PANEL_SEQ']
    }
	
}
