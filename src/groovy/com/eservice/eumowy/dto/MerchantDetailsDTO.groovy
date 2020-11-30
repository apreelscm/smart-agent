package com.eservice.eumowy.dto

import com.eservice.eumowy.enums.options.LegalForm
import com.google.common.base.MoreObjects

import static org.apache.commons.lang.StringUtils.*

class MerchantDetailsDTO implements Serializable {
    Long id
    String nip
    String akceptantRegon
    String akceptantNazwaOficjalna
    String opisMerchanta

    LegalForm formaPrawna

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
    List<MerchantBeneficiaryDTO> beneficiaries = new ArrayList<MerchantBeneficiaryDTO>()

    MerchantSearchStatus status

    MerchantDetailsDTO() {
        status = MerchantSearchStatus.SUCCESS
    }

    MerchantDetailsDTO(MerchantSearchStatus status) {
        this.status = status
    }

    boolean isValid() {
        return success() && isNotEmpty(nip) && isNotEmpty(akceptantRegon) && isNotEmpty(akceptantNazwaOficjalna)
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
        .add("formaPrawna", formaPrawna)
        .add("beneficiaries", beneficiaries.toString())
        .toString()
    }


    static MerchantDetailsDTO errorResult() {
        new MerchantDetailsDTO(MerchantSearchStatus.ERROR)
    }

    static MerchantDetailsDTO mappingProblem() {
        new MerchantDetailsDTO(MerchantSearchStatus.MAPPING_ERROR)
    }

    static MerchantDetailsDTO notFound() {
        new MerchantDetailsDTO(MerchantSearchStatus.NOT_FOUND)
    }

    boolean success(){
        return MerchantSearchStatus.SUCCESS == status
    }
}
