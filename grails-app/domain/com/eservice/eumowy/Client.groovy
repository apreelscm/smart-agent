package com.eservice.eumowy

class Client implements Serializable{

    String name
    String nip

    static constraints = {
        name(unique:true,blank:false)
        nip(unique:true,blank:false)
    }

    static mapping = {
        table name: "client", schema: "CBD_UMOWY"
    }
}
