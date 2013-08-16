package com.eservice.eumowy

class Subscription {

    String content;

    static constraints = {
        content(maxSize: 10000)
    }

    static mapping = {
        table name: "SUBSCRIPTION", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SUBSCRIPTION_SEQ']
    }
}
