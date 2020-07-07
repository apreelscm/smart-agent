package com.eservice.eumowy.microbisnode.model

import groovy.json.internal.LazyMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RegisteredDetail implements IgnoreUnknownProperties {

    private List<OrganizationIdentificationNumberDetail> organizationIdentificationNumberDetail = new ArrayList<>()
    private LegalFormDetails legalFormDetails

    List<OrganizationIdentificationNumberDetail> getOrganizationIdentificationNumberDetail() {
        return organizationIdentificationNumberDetail
    }

    void setOrganizationIdentificationNumberDetail(List<OrganizationIdentificationNumberDetail> organizationIdentificationNumberDetail) {
        organizationIdentificationNumberDetail?.each {it ->
            if (it instanceof LazyMap) {
                this.organizationIdentificationNumberDetail.add(new OrganizationIdentificationNumberDetail(it))
            } else {
                this.organizationIdentificationNumberDetail.add(it)
            }
        }
    }

    LegalFormDetails getLegalFormDetails() {
        return legalFormDetails
    }

    void setLegalFormDetails(LegalFormDetails legalFormDetails) {
        this.legalFormDetails = legalFormDetails
    }

    OrganizationIdentificationNumberDetail getIdentifierByType(Long type){
        if (!organizationIdentificationNumberDetail?.isEmpty()){
            return organizationIdentificationNumberDetail.find{ ((OrganizationIdentificationNumberDetail) it).id == type}
        }
        return null
    }

    OrganizationIdentificationNumberDetail getNipIdentifier(){
        return getIdentifierByType(2)
    }

    OrganizationIdentificationNumberDetail getRegonIdentifier(){
        return getIdentifierByType(1)
    }

}
