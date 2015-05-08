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
        merchantKRSDataDTO.nip = "5841003695"
        merchantKRSDataDTO.regon = "190431244"
        merchantKRSDataDTO.name = "PPHU DRUK"
        merchantKRSDataDTO.description = "Opis testowego merchanta"

        merchantKRSDataDTO.merchAddressDatas = new HashSet<MerchantAddressDataDTO>(Arrays.asList(createAddressDTO()))
        merchantKRSDataDTO.merchRepresentDatas = new HashSet<MerchantRepresentativeDataDTO>(Arrays.asList(createMerchantRepresentative()))

        return merchantKRSDataDTO
    }

    private MerchantAddressDataDTO createAddressDTO() {
        MerchantAddressDataDTO merchantAddressDataDTO = new MerchantAddressDataDTO()
        merchantAddressDataDTO.suffix = "Ul."
        merchantAddressDataDTO.street = "Lektykarska"
        merchantAddressDataDTO.streetNbr = "25"
        merchantAddressDataDTO.flatNbr = "8"
        merchantAddressDataDTO.postalCode = "01-687"
        merchantAddressDataDTO.city = "Warszawa"
        merchantAddressDataDTO.addressType = "Adres operacyjny"

        return merchantAddressDataDTO
    }

    private MerchantRepresentativeDataDTO createMerchantRepresentative() {
        MerchantRepresentativeDataDTO merchantRepresentativeDataDTO = new MerchantRepresentativeDataDTO()
        merchantRepresentativeDataDTO.firstName = "Piotr"
        merchantRepresentativeDataDTO.lastName = "Lewicki"
        merchantRepresentativeDataDTO.title = "Pan"
        merchantRepresentativeDataDTO.position = "Właściciel"

        return merchantRepresentativeDataDTO
    }
}
