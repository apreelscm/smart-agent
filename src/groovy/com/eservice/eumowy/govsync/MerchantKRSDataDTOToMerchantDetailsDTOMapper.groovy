package com.eservice.eumowy.govsync

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.webs.client.govsync.dto.MerchantAddressDataDTO
import com.eservice.webs.client.govsync.dto.MerchantKRSDataDTO
import com.eservice.webs.client.govsync.dto.MerchantRepresentativeDataDTO

@Deprecated
class MerchantKRSDataDTOToMerchantDetailsDTOMapper {

    static MerchantDetailsDTO map(MerchantKRSDataDTO merchantData){
        MerchantDetailsDTO merchantDetailsDTO = new MerchantDetailsDTO()
        merchantDetailsDTO.id = merchantData.getId()
        merchantDetailsDTO.nip = merchantData.getNip()
        merchantDetailsDTO.akceptantRegon = merchantData.getRegon()
        merchantDetailsDTO.akceptantNazwaOficjalna = merchantData.getName()
        merchantDetailsDTO.opisMerchanta = merchantData.getDescription()

        if (merchantData.merchAddressDatas.size() > 0) {
            addMerchantAddressData(merchantDetailsDTO, merchantData.merchAddressDatas.asList().first())
        }

        merchantDetailsDTO.representatives = createRepresentativesList(merchantData)
    }


    static private void addMerchantAddressData(MerchantDetailsDTO merchantDetailsDTO, MerchantAddressDataDTO addressDataDTO) {
        merchantDetailsDTO.akceptantUlicaTytul = addressDataDTO.getSuffix()
        merchantDetailsDTO.akceptantUlica = addressDataDTO.getStreet()
        merchantDetailsDTO.akceptantNrDomu = addressDataDTO.getStreetNbr()
        merchantDetailsDTO.akceptantNrMieszkania = addressDataDTO.getFlatNbr()
        merchantDetailsDTO.akceptantKodPocztowy = addressDataDTO.getPostalCode()
        merchantDetailsDTO.akceptantMiasto = addressDataDTO.getCity()
        merchantDetailsDTO.akceptantTelStacjonarny = addressDataDTO.getPhone()
        merchantDetailsDTO.akceptantTelKomorkowy = addressDataDTO.getMobilePhone()
        merchantDetailsDTO.akceptantFax = addressDataDTO.getFax()
    }

    static private List<MerchantRepresentativeDTO> createRepresentativesList(MerchantKRSDataDTO merchantData) {
        List<MerchantRepresentativeDTO> list = new ArrayList<>()
        for(MerchantRepresentativeDataDTO merchantRepresentative: merchantData.merchRepresentDatas) {
            MerchantRepresentativeDTO representative = new MerchantRepresentativeDTO(merchantRepresentative)

            if (representative.isValid()) {
                list.add(new MerchantRepresentativeDTO(merchantRepresentative))
            }
        }
        return list
    }

}
