package com.eservice.eumowy.microbisnode.model

import groovy.json.internal.LazyMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PrincipalsAndManagement implements IgnoreUnknownProperties {

    private List<Principal> currentPrincipal = new ArrayList<>()

    List<Principal> getCurrentPrincipal() {
        return currentPrincipal
    }

    void setCurrentPrincipal(List<Principal> currentPrincipal) {
        currentPrincipal?.each {it ->
            if (it instanceof LazyMap) {
                this.currentPrincipal.add(new Principal(it))
            } else {
                this.currentPrincipal.add(it)
            }
        }
    }
}
