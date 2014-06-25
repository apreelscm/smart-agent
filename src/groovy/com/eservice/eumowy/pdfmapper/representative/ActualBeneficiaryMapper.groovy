package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper

class ActualBeneficiaryMapper extends AbstractPdfMapper implements Mapper {
    private ProcessCommand processCommand

    public ActualBeneficiaryMapper(ProcessCommand processCommand) {
        this.processCommand = processCommand
    }

    @Override
    public Map getDataForMapping() {
        Map actualBeneficiaryData = [:]

        addCheckbox(actualBeneficiaryData, "akceptantJestSpolka", true, processCommand.akceptantJestSpolka)
        actualBeneficiaryData.put("nazwaGieldy", [processCommand.nazwaGieldy] as String[])
        actualBeneficiaryData.put("isinAkceptanta", [processCommand.isinAkceptanta] as String[])

        addCheckbox(actualBeneficiaryData, "akceptantJestPodmiotem", true, processCommand.akceptantJestPodmiotem)
        addCheckbox(actualBeneficiaryData, "akceptantJestOrganem", true, processCommand.akceptantJestOrganem)
        addCheckbox(actualBeneficiaryData, "akceptantNieMaBeneficjenta", true, processCommand.akceptantNieMaBeneficjenta)

        return actualBeneficiaryData
    }
}
