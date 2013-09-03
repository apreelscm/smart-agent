package com.eservice.eumowy

import groovy.transform.ToString

@ToString
class Client implements Serializable{

    String name
    Long cbdId
    String nip

    static constraints = {
        name(nullable:true,blank:false)
        nip(blank:false)
        cbdId(nullable: true,blank:true)
    }

    static mapping = {
        table name: "CLIENT", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CLIENT_SEQ']
    }
}
