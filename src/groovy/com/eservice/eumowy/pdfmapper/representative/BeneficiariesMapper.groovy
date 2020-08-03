package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
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
            beneficiariesData.put(getFieldName(i, "Obywatelstwo"), [beneficiary.citizenship] as String[])

            beneficiariesData.put(getFieldName(i, "PosiadaAkceptanta"), getCheckedCheckbox(beneficiary.ownsAcceptor))
            beneficiariesData.put(getFieldName(i, "KontrolujeAkceptanta"), getCheckedCheckbox(beneficiary.controlsAcceptor))
            beneficiariesData.put(getFieldName(i, "ZnaczaceUdzialy"), getCheckedCheckbox(beneficiary.overQuarterOfVotes))
            beneficiariesData.put(getFieldName(i, "ProcentUdzialow"), [beneficiary.votesPercentage] as String[])
        }

        return beneficiariesData
    }

    private Set<Representative> getAllBeneficiaries() {
        return process.representatives.findAll{Representative.Type.BENEFICIARY.equals(it.type)}
    }

    private String getFieldName(Integer index, String fieldName) {
        return "beneficjent" + (index + 1) + fieldName
    }

}
