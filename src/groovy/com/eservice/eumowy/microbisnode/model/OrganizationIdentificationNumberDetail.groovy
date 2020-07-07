package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OrganizationIdentificationNumberDetail implements IgnoreUnknownProperties {

    private Long id
    private String organizationIdentificationNumber

    String getOrganizationIdentificationNumber() {
        return organizationIdentificationNumber
    }

    void setOrganizationIdentificationNumber(String organizationIdentificationNumber) {
        this.organizationIdentificationNumber = organizationIdentificationNumber
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

}
