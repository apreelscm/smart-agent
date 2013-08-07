package com.eservice.eumowy

import groovy.transform.ToString

@ToString
class Client implements Serializable{

    String name
    String cbdId
    String nip

    static constraints = {
        name(unique:true,blank:false)
        nip(unique:true,blank:false)
        cbdId(unique:true,blank:true)
    }

    static mapping = {
        table name: "client", schema: "CBD_UMOWY"
    }
}
