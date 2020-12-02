package com.eservice.eumowy.dto

import com.google.common.base.Strings

class MerchantBeneficiaryDTO extends MerchantRepresentativeDTO {

    Integer ownershipPercentage

    Integer getOwnershipPercentage() {
        return ownershipPercentage
    }

    void setOwnershipPercentage(Integer ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage
    }

    boolean isValid() {
        return !(Strings.isNullOrEmpty(title) || Strings.isNullOrEmpty(firstName) || Strings.isNullOrEmpty(lastName))
    }
}
