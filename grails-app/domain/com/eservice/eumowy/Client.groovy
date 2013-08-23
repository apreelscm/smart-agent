package com.eservice.eumowy

import groovy.transform.ToString

@ToString
class Client implements Serializable{

    String name
    Long cbdId
    String nip

    static constraints = {
        name(unique:true,blank:false)
        nip(unique:true,blank:false)
        cbdId(unique:true,nullable: true,blank:true)
    }

    static mapping = {
        table name: "CLIENT", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CLIENT_SEQ']
    }
}
