package com.eservice.eumowy.microbisnode.model

/**
 * support for LazyMap mapping to object when response model from microBisnode is bigger than eUmowy
 */
trait IgnoreUnknownProperties {

    def propertyMissing(String name, value){
        // do nothing
    }
}