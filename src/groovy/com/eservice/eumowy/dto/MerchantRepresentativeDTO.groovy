package com.eservice.eumowy.dto

import com.eservice.webs.dto.MerchantRepresentativeDataDTO


class MerchantRepresentativeDTO implements Serializable{
    String firstName
    String lastName
    String position

    public MerchantRepresentativeDTO(MerchantRepresentativeDataDTO merchantRepresentativeDTO) {
        firstName = merchantRepresentativeDTO.firstName
        lastName = merchantRepresentativeDTO.lastName
        position = merchantRepresentativeDTO.position
    }
}
