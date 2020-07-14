package com.eservice.eumowy.dto

class MerchantBeneficiaryDTO extends MerchantRepresentativeDTO {

    Integer ownershipPercentage

    Integer getOwnershipPercentage() {
        return ownershipPercentage
    }

    void setOwnershipPercentage(Integer ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage
    }
}
