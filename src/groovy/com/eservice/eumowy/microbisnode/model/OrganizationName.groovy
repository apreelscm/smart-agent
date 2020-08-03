package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class OrganizationName implements IgnoreUnknownProperties {

    private List<String> organizationPrimaryName

    List<String> getOrganizationPrimaryName() {
        return organizationPrimaryName
    }

    void setOrganizationPrimaryName(List<String> organizationPrimaryName) {
        this.organizationPrimaryName = organizationPrimaryName
    }

}
