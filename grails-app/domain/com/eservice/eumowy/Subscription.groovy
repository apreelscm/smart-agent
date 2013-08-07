package com.eservice.eumowy

class Subscription implements Serializable {

    def signature

    static mapping = {
        table name: "subscription", schema: "CBD_UMOWY"
    }
}