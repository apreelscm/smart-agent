package com.eservice.eumowy.dto

import com.eservice.webs.client.govsync.dto.MerchantRepresentativeDataDTO
import com.google.common.base.MoreObjects
import com.google.common.base.Strings

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

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
        .add("title", title)
        .add("firstName", firstName)
        .add("lastName", lastName)
        .add("position", position)
        .toString()
    }

    public boolean isValid() {
        return !(Strings.isNullOrEmpty(title) || Strings.isNullOrEmpty(firstName) || Strings.isNullOrEmpty(lastName) ||
                Strings.isNullOrEmpty(position))
    }
}
