package com.eservice.eumowy.microbisnode.model

import groovy.json.internal.LazyMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Location implements IgnoreUnknownProperties {

    private List<Address> primaryAddress = new ArrayList<>()

    List<Address> getPrimaryAddress() {
        return primaryAddress
    }

    void setPrimaryAddress(List<Address> primaryAddress) {
        primaryAddress?.each {it ->
            if (it instanceof LazyMap) {
                this.primaryAddress.add(new Address(it))
            } else {
                this.primaryAddress.add(it)
            }
        }
    }

    Address getFirstPrimaryAddress(){
        return primaryAddress?.isEmpty() ? null : primaryAddress.get(0)
    }

}
