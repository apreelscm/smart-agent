package com.eservice.eumowy.microbisnode.model

import groovy.json.internal.LazyMap
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Principal implements IgnoreUnknownProperties {

    private List<PrincipalIdentificationNumberDetail> principalIdentificationNumberDetail = new ArrayList<>()

    private PrincipalName principalName

    private List<String> currentManagementResponsibility

    private String nationality;

    List<PrincipalIdentificationNumberDetail> getPrincipalIdentificationNumberDetail() {
        return principalIdentificationNumberDetail
    }

    void setPrincipalIdentificationNumberDetail(List<PrincipalIdentificationNumberDetail> principalIdentificationNumberDetail) {
        principalIdentificationNumberDetail?.each {it ->
            if (it instanceof LazyMap) {
                this.principalIdentificationNumberDetail.add(new PrincipalIdentificationNumberDetail(it))
            } else {
                this.principalIdentificationNumberDetail.add(it)
            }
        }
    }

    PrincipalIdentificationNumberDetail getIdentifierByType(String type){
        if (!principalIdentificationNumberDetail?.isEmpty()){
            return principalIdentificationNumberDetail.find{ ((PrincipalIdentificationNumberDetail) it).typeText == type}
        }
        return null
    }

    PrincipalIdentificationNumberDetail getPeselIdentifier(){
        return getIdentifierByType("PESEL")
    }

    PrincipalName getPrincipalName() {
        return principalName
    }

    void setPrincipalName(PrincipalName principalName) {
        this.principalName = principalName
    }

    List<String> getCurrentManagementResponsibility() {
        return currentManagementResponsibility
    }

    void setCurrentManagementResponsibility(List<String> currentManagementResponsibility) {
        this.currentManagementResponsibility = currentManagementResponsibility
    }

    String getNationality() {
        return nationality
    }

    void setNationality(String nationality) {
        this.nationality = nationality
    }

    boolean isOwner(){
        return currentManagementResponsibility?.contains("W")
    }

}
