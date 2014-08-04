package com.eservice.eumowy.mocks

import com.eservice.webs.client.WebsClient
import com.eservice.webs.client.govsync.dto.MerchantAddressDataDTO
import com.eservice.webs.client.govsync.dto.MerchantKRSDataDTO
import com.eservice.webs.client.govsync.dto.MerchantRepresentativeDataDTO
import org.apache.commons.lang.RandomStringUtils

class WebsClientMock extends WebsClient {
    public WebsClientMock() {}

    @Override
    MerchantKRSDataDTO searchMerchantData(String nip, Long userId) {
        MerchantKRSDataDTO merchantKRSDataDTO = new MerchantKRSDataDTO()
        merchantKRSDataDTO.id = 1
        merchantKRSDataDTO.nip = nip
        merchantKRSDataDTO.regon = "317411108"
        merchantKRSDataDTO.name = "Testowy merchant"
        merchantKRSDataDTO.description = "Opis testowego merchanta"

        merchantKRSDataDTO.merchAddressDatas = new HashSet<MerchantAddressDataDTO>(Arrays.asList(createAddressDTO(), createAddressDTO()))
        merchantKRSDataDTO.merchRepresentDatas = new HashSet<MerchantRepresentativeDataDTO>(Arrays.asList(createMerchantRepresentative(), createMerchantRepresentative()))

        return merchantKRSDataDTO
    }

    private MerchantAddressDataDTO createAddressDTO() {
        MerchantAddressDataDTO merchantAddressDataDTO = new MerchantAddressDataDTO()
        merchantAddressDataDTO.id = Random.newInstance().nextLong()
        merchantAddressDataDTO.suffix = "UL"
        merchantAddressDataDTO.street = "Przykladowa"
        merchantAddressDataDTO.streetNbr = RandomStringUtils.randomNumeric(2)
        merchantAddressDataDTO.flatNbr = RandomStringUtils.randomNumeric(2)
        merchantAddressDataDTO.postalCode = "01-650"
        merchantAddressDataDTO.city = "Warszawa"
        merchantAddressDataDTO.phone = RandomStringUtils.randomNumeric(7)
        merchantAddressDataDTO.mobilePhone = RandomStringUtils.randomNumeric(9)
        merchantAddressDataDTO.fax = RandomStringUtils.randomNumeric(9)
        merchantAddressDataDTO.addressType = "JakisTam"

        return merchantAddressDataDTO
    }

    private MerchantRepresentativeDataDTO createMerchantRepresentative() {
        MerchantRepresentativeDataDTO merchantRepresentativeDataDTO = new MerchantRepresentativeDataDTO()
        merchantRepresentativeDataDTO.id = Random.newInstance().nextLong()
        merchantRepresentativeDataDTO.email = "test@email.com"
        merchantRepresentativeDataDTO.firstName = RandomStringUtils.randomAlphabetic(7)
        merchantRepresentativeDataDTO.lastName = RandomStringUtils.randomAlphabetic(10)
        merchantRepresentativeDataDTO.mobilePhone = RandomStringUtils.randomNumeric(9)
        merchantRepresentativeDataDTO.title = "Pan"
        merchantRepresentativeDataDTO.phone = RandomStringUtils.randomNumeric(7)
        merchantRepresentativeDataDTO.position = RandomStringUtils.randomAlphabetic(5)

        return merchantRepresentativeDataDTO
    }
}
