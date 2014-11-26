package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
import com.eservice.eumowy.enums.options.IdentityDocumentType
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
            beneficiariesData.put(getFieldName(i, "Adres"), [beneficiary.address] as String[])
            beneficiariesData.put(getFieldName(i, "SeriaNrDokumentu"), [beneficiary.documentNumber] as String[])
            beneficiariesData.put(getFieldName(i, "Obywatelstwo"), [beneficiary.citizenship] as String[])
            beneficiariesData.put(getFieldName(i, "ProcentUdzialow"), [beneficiary.votesPercentage] as String[])

            beneficiariesData.put(getFieldName(i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(beneficiary.documentType)))
            beneficiariesData.put(getFieldName(i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(beneficiary.documentType)))
            beneficiariesData.put(getFieldName(i, "PosiadaAkceptanta"), getCheckboxData(beneficiary.ownsAcceptor))
            beneficiariesData.put(getFieldName(i, "KontrolujeAkceptanta"), getCheckboxData(beneficiary.controlsAcceptor))
            beneficiariesData.put(getFieldName(i, "ZnaczaceUdzialy"), getCheckboxData(beneficiary.overQuarterOfVotes))
        }

        return beneficiariesData
    }

    private Set<Representative> getAllBeneficiaries() {
        return process.representatives.findAll{Representative.Type.BENEFICIARY.equals(it.type)}
    }

    public String getLokalizacjaDane(Representative beneficiary) {
        return beneficiary.pesel
    }

    private String getFieldName(Integer index, String fieldName) {
        return "beneficjent" + (index + 1) + fieldName
    }

}
