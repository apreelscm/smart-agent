package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Linkage implements IgnoreUnknownProperties {

    private BeneficialOwnership beneficialOwnership

    BeneficialOwnership getBeneficialOwnership() {
        return beneficialOwnership
    }

    void setBeneficialOwnership(BeneficialOwnership beneficialOwnership) {
        this.beneficialOwnership = beneficialOwnership
    }
}
