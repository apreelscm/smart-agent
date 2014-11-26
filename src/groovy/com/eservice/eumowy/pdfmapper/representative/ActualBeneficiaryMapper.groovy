package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process

class ActualBeneficiaryMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public ActualBeneficiaryMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map actualBeneficiaryData = [:]

        actualBeneficiaryData.put("nazwaGieldy", [process.getData("nazwaGieldy")] as String[])
        actualBeneficiaryData.put("isinAkceptanta", [process.getData("isinAkceptanta")] as String[])

        actualBeneficiaryData.put("akceptantJestSpolka", getCheckboxData(process.getBooleanData("akceptantJestSpolka")))
        actualBeneficiaryData.put("akceptantJestPodmiotem", getCheckboxData(process.getBooleanData("akceptantJestPodmiotem")))
        actualBeneficiaryData.put("akceptantJestOrganem", getCheckboxData(process.getBooleanData("akceptantJestOrganem")))
        actualBeneficiaryData.put("akceptantNieMaBeneficjenta", getCheckboxData(process.getBooleanData("akceptantNieMaBeneficjenta")))

        return actualBeneficiaryData
    }
}
