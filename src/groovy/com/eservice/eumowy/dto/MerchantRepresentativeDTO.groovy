package com.eservice.eumowy.dto

import com.eservice.webs.dto.MerchantRepresentativeDataDTO


class MerchantRepresentativeDTO implements Serializable{
    String title
    String firstName
    String lastName
    String position

    public MerchantRepresentativeDTO() {}

    public MerchantRepresentativeDTO(MerchantRepresentativeDataDTO merchantRepresentativeDTO) {
        title = merchantRepresentativeDTO.title
        firstName = merchantRepresentativeDTO.firstName
        lastName = merchantRepresentativeDTO.lastName
        position = merchantRepresentativeDTO.position
    }
}
