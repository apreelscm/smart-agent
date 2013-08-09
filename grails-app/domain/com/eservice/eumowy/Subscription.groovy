package com.eservice.eumowy

class Subscription {

    String content;

    static constraints = {
        content(maxSize: 10000)
    }
}
