package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PrincipalIdentificationNumberDetail implements IgnoreUnknownProperties {

    String typeText
    String principalIdentificationNumber

    String getTypeText() {
        return typeText
    }

    void setTypeText(String typeText) {
        this.typeText = typeText
    }

    String getPrincipalIdentificationNumber() {
        return principalIdentificationNumber
    }

    void setPrincipalIdentificationNumber(String principalIdentificationNumber) {
        this.principalIdentificationNumber = principalIdentificationNumber
    }

}
