package com.eservice.eumowy

import groovy.transform.ToString

@ToString
class Panel implements Serializable{

    String name;
    String view;
    Integer orderNo;

    static belongsTo = [signature:Signature]

    static constraints = {
        name(unique:true,blank:false)
        view(blank:false)
    }

    static mapping = {
        table name: "panel", schema: "CBD_UMOWY"
    }

}
