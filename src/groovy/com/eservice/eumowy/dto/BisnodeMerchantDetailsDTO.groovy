package com.eservice.eumowy.dto

import com.eservice.webs.dto.MerchantAddressDataDTO
import com.eservice.webs.dto.MerchantKRSDataDTO
import com.eservice.webs.dto.MerchantRepresentativeDataDTO


class BisnodeMerchantDetailsDTO {
    String nip
    String regon
    String nazwaAkceptanta
    String opisMerchanta

    String typUlicy
    String ulica
    String nrDomu
    String nrLokalu
    String kodPocztowy
    String miasto
    String telefon
    String telefonKom
    String fax

    Set<MerchantRepresentativeDataDTO> representatives

    public BisnodeMerchantDetailsDTO(MerchantKRSDataDTO merchantData) {
        nip = merchantData.getNip()
        regon = merchantData.getRegon()
        nazwaAkceptanta = merchantData.getName()
        opisMerchanta = merchantData.getDescription()

        if (merchantData.merchAddressDatas.size() > 0) {
            addMerchantAddressData(merchantData.merchAddressDatas[0])
        }

        representatives = merchantData.merchRepresentDatas
    }

    private void addMerchantAddressData(MerchantAddressDataDTO addressDataDTO) {
        typUlicy = addressDataDTO.getSuffix()
        ulica = addressDataDTO.getStreet()
        nrDomu = addressDataDTO.getStreetNbr()
        nrLokalu = addressDataDTO.getFlatNbr()
        kodPocztowy = addressDataDTO.getPostalCode()
        miasto = addressDataDTO.getCity()
        telefon = addressDataDTO.getPhone()
        telefonKom = addressDataDTO.getMobilePhone()
        fax = addressDataDTO.getFax()
    }
}
