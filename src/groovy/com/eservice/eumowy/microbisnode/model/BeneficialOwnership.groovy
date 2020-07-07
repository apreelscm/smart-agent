package com.eservice.eumowy.microbisnode.model

import groovy.json.internal.LazyMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BeneficialOwnership implements IgnoreUnknownProperties {

    List<BeneficialOwner> beneficialOwners = new ArrayList<>()

    List<BeneficialOwner> getBeneficialOwners() {
        return beneficialOwners
    }

    void setBeneficialOwners(List<BeneficialOwner> beneficialOwners) {
        beneficialOwners?.each {it ->
            if (it instanceof LazyMap) {
                this.beneficialOwners.add(new BeneficialOwner(it))
            } else {
                this.beneficialOwners.add(it)
            }
        }
    }

}
