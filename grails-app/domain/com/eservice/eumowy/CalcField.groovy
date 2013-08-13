package com.eservice.eumowy

class CalcField implements Serializable {

    String name;

    static constraints = {
        name(unique:true,blank:false)
    }

    static mapping = {
        table name: "CALCFIELD", schema: DomainConsts.SHEMA_NAME
    }

    String toString(){
        return name;
    }
}
