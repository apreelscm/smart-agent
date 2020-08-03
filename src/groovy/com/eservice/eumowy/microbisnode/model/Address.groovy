package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Address implements IgnoreUnknownProperties {

    private List<String> streetAddressLine
    private String postalCode
    private String primaryTownName

    List<String> getStreetAddressLine() {
        return streetAddressLine
    }

    void setStreetAddressLine(List<String> streetAddressLine) {
        this.streetAddressLine = streetAddressLine
    }

    String getPostalCode() {
        return postalCode
    }

    void setPostalCode(String postalCode) {
        this.postalCode = postalCode
    }

    String getPrimaryTownName() {
        return primaryTownName
    }

    void setPrimaryTownName(String primaryTownName) {
        this.primaryTownName = primaryTownName
    }
}
