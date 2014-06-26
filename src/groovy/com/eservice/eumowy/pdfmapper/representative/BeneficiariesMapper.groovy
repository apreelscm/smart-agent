package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process


class BeneficiariesMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public BeneficiariesMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map beneficiariesData = [:]

        allBeneficiaries.eachWithIndex { beneficiary, i ->
            beneficiariesData.put(getFieldName(i, "Nazwa"), [beneficiary.fullName] as String[])
            beneficiariesData.put(getFieldName(i, "LokalizacjaDane"), [getLokalizacjaDane(beneficiary)] as String[])
            beneficiariesData.put(getFieldName(i, "Adres"), [beneficiary.adres] as String[])
            beneficiariesData.put(getFieldName(i, "SeriaNrDokumentu"), [beneficiary.seriaNrDokumentu] as String[])
            beneficiariesData.put(getFieldName(i, "Obywatelstwo"), [beneficiary.obywatelstwo] as String[])
            beneficiariesData.put(getFieldName(i, "ProcentUdzialow"), [beneficiary.procentUdzialow] as String[])

            beneficiariesData.put(getFieldName(i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(beneficiary.typDokumentu)))
            beneficiariesData.put(getFieldName(i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(beneficiary.typDokumentu)))
            beneficiariesData.put(getFieldName(i, "PosiadaAkceptanta"), getCheckboxData(beneficiary.posiadaAkceptanta))
            beneficiariesData.put(getFieldName(i, "KontrolujeAkceptanta"), getCheckboxData(beneficiary.kontrolujeAkceptanta))
            beneficiariesData.put(getFieldName(i, "ZnaczaceUdzialy"), getCheckboxData(beneficiary.znaczaceUdzialy))
        }

        return beneficiariesData
    }

    private Set<Representative> getAllBeneficiaries() {
        return process.representatives.findAll{Representative.Type.BENEFICIARY.equals(it.typ)}
    }

    public String getLokalizacjaDane(Representative beneficiary) {
        if(IdentityDocumentType.PASSPORT.equals(beneficiary.typDokumentu)) {
            return beneficiary.lokalizacjaKraj
        }

        return beneficiary.lokalizacjaPesel
    }

    private String getFieldName(Integer index, String fieldName) {
        return "beneficjent" + (index + 1) + fieldName
    }

}
