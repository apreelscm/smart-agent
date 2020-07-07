package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Organization implements IgnoreUnknownProperties {

    private OrganizationName organizationName
    private RegisteredDetail registeredDetail
    private Location location
    private PrincipalsAndManagement principalsAndManagement
    private Linkage linkage

    OrganizationName getOrganizationName() {
        return organizationName
    }

    void setOrganizationName(OrganizationName organizationName) {
        this.organizationName = organizationName
    }

    RegisteredDetail getRegisteredDetail() {
        return registeredDetail
    }

    void setRegisteredDetail(RegisteredDetail registeredDetail) {
        this.registeredDetail = registeredDetail
    }

    Location getLocation() {
        return location
    }

    void setLocation(Location location) {
        this.location = location
    }

    Linkage getLinkage() {
        return linkage
    }

    void setLinkage(Linkage linkage) {
        this.linkage = linkage
    }

    PrincipalsAndManagement getPrincipalsAndManagement() {
        return principalsAndManagement
    }

    void setPrincipalsAndManagement(PrincipalsAndManagement principalsAndManagement) {
        this.principalsAndManagement = principalsAndManagement
    }

    boolean isLegalFormKnown(){
        return ! registeredDetail?.legalFormDetails?.legalFormText?.isEmpty()
    }

    boolean isSOHO(){
        return isLegalFormKnown() && registeredDetail.legalFormDetails.legalFormText.startsWith("Jednoosobowa")
    }
}
