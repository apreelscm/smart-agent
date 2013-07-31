package com.eservice.eumowy

class Signature implements Serializable {

    String name;

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "signature", schema: "CBD_UMOWY"
    }
}
