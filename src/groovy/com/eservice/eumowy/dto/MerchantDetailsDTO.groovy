package com.eservice.eumowy.dto

import com.google.common.base.MoreObjects

import static org.apache.commons.lang.StringUtils.*

class MerchantDetailsDTO implements Serializable {
    Long id
    String nip
    String akceptantRegon
    String akceptantNazwaOficjalna
    String opisMerchanta

    String formaPrawna

    String akceptantUlicaTytul
    String akceptantUlica
    String akceptantNrDomu
    String akceptantNrMieszkania
    String akceptantKodPocztowy
    String akceptantMiasto
    String akceptantTelStacjonarny
    String akceptantTelKomorkowy
    String akceptantFax

    List<MerchantRepresentativeDTO> representatives = new ArrayList<MerchantRepresentativeDTO>()
    List<MerchantRepresentativeDTO> beneficiaries = new ArrayList<MerchantRepresentativeDTO>()

    boolean isValid() {
        return isNotEmpty(nip) && isNotEmpty(akceptantRegon) && isNotEmpty(akceptantNazwaOficjalna)
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("nip", nip)
        .add("akceptantRegon", akceptantRegon)
        .add("akceptantNazwaOficjalna", akceptantNazwaOficjalna)
        .add("opisMerchanta", opisMerchanta)
        .add("akceptantUlicaTytul", akceptantUlicaTytul)
        .add("akceptantUlica", akceptantUlica)
        .add("akceptantNrDomu", akceptantNrDomu)
        .add("akceptantNrMieszkania", akceptantNrMieszkania)
        .add("akceptantKodPocztowy", akceptantKodPocztowy)
        .add("akceptantMiasto", akceptantMiasto)
        .add("akceptantTelStacjonarny", akceptantTelStacjonarny)
        .add("akceptantTelKomorkowy", akceptantTelKomorkowy)
        .add("akceptantFax", akceptantFax)
        .add("representatives", representatives.toString())
        .toString()
    }
}
