package com.eservice.eumowy

class CalcField implements Serializable {

    String name;

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "calcfield", schema: "CBD_UMOWY"
    }

    String toString(){
        return name;
    }
}
