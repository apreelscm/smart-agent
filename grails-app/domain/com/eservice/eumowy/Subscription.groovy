package com.eservice.eumowy

class Subscription {

    String content;

    static constraints = {
        content(maxSize: 10000)
    }

    static mapping = {
        table name: "SUBSCRIPTION", schema: DomainConsts.SHEMA_NAME
    }
}
