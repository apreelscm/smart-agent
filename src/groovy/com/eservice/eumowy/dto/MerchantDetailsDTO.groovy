package com.eservice.eumowy.dto

import com.eservice.webs.client.govsync.dto.MerchantAddressDataDTO
import com.eservice.webs.client.govsync.dto.MerchantKRSDataDTO
import com.eservice.webs.client.govsync.dto.MerchantRepresentativeDataDTO

import static org.apache.commons.lang.StringUtils.*;

class MerchantDetailsDTO implements Serializable {
    String nip
    String akceptantRegon
    String akceptantNazwaOficjalna
    String opisMerchanta

    String akceptantUlicaTytul
    String akceptantUlica
    String akceptantNrDomu
    String akceptantNrMieszkania
    String akceptantKodPocztowy
    String akceptantMiasto
    String akceptantTelStacjonarny
    String akceptantTelKomorkowy
    String akceptantFax

    List<MerchantRepresentativeDTO> representatives

    public MerchantDetailsDTO(MerchantKRSDataDTO merchantData) {
        nip = merchantData.getNip()
        akceptantRegon = merchantData.getRegon()
        akceptantNazwaOficjalna = getAlphanumericName(merchantData.getName())
        opisMerchanta = merchantData.getDescription()

        if (merchantData.merchAddressDatas.size() > 0) {
            addMerchantAddressData(merchantData.merchAddressDatas.asList().first())
        }

        createRepresentativesList(merchantData)
    }

    public boolean isValid() {
        return isNotEmpty(nip) && isNotEmpty(akceptantRegon) && isNotEmpty(akceptantNazwaOficjalna)
    }

    private void addMerchantAddressData(MerchantAddressDataDTO addressDataDTO) {
        akceptantUlicaTytul = addressDataDTO.getSuffix()
        akceptantUlica = addressDataDTO.getStreet()
        akceptantNrDomu = addressDataDTO.getStreetNbr()
        akceptantNrMieszkania = addressDataDTO.getFlatNbr()
        akceptantKodPocztowy = addressDataDTO.getPostalCode()
        akceptantMiasto = addressDataDTO.getCity()
        akceptantTelStacjonarny = addressDataDTO.getPhone()
        akceptantTelKomorkowy = addressDataDTO.getMobilePhone()
        akceptantFax = addressDataDTO.getFax()
    }

    private void createRepresentativesList(MerchantKRSDataDTO merchantData) {
        representatives = new ArrayList<MerchantRepresentativeDTO>()

        for(MerchantRepresentativeDataDTO merchantRepresentative: merchantData.merchRepresentDatas) {
            representatives.add(new MerchantRepresentativeDTO(merchantRepresentative))
        }
    }

    private String getAlphanumericName(String value) {
        return value?.replaceAll("[^A-Za-z0-9 ]", "")
    }
}
