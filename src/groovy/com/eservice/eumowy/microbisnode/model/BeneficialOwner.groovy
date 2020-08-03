package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BeneficialOwner implements IgnoreUnknownProperties {

    SubjectTypeDescription subjectTypeDescription
    BigDecimal beneficialOwnershipPercentage
    String personId
    String nationality
    String primaryName

    SubjectTypeDescription getSubjectTypeDescription() {
        return subjectTypeDescription
    }

    void setSubjectTypeDescription(SubjectTypeDescription subjectTypeDescription) {
        this.subjectTypeDescription = subjectTypeDescription
    }

    BigDecimal getBeneficialOwnershipPercentage() {
        return beneficialOwnershipPercentage
    }

    void setBeneficialOwnershipPercentage(BigDecimal beneficialOwnershipPercentage) {
        this.beneficialOwnershipPercentage = beneficialOwnershipPercentage
    }

    String getPersonId() {
        return personId
    }

    void setPersonId(String personId) {
        this.personId = personId
    }

    String getNationality() {
        return nationality
    }

    void setNationality(String nationality) {
        this.nationality = nationality
    }

    String getPrimaryName() {
        return primaryName
    }

    void setPrimaryName(String primaryName) {
        this.primaryName = primaryName
    }

    boolean isSignificant(){
        return subjectTypeDescription?.code == 119 && beneficialOwnershipPercentage >=25
    }
}
