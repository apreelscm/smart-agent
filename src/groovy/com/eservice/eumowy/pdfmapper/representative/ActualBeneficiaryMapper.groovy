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

        actualBeneficiaryData.put("nazwaGieldy", [processCommand.nazwaGieldy] as String[])
        actualBeneficiaryData.put("isinAkceptanta", [processCommand.isinAkceptanta] as String[])

        actualBeneficiaryData.put("akceptantJestSpolka", getCheckboxData(processCommand.akceptantJestSpolka))
        actualBeneficiaryData.put("akceptantJestPodmiotem", getCheckboxData(processCommand.akceptantJestPodmiotem))
        actualBeneficiaryData.put("akceptantJestOrganem", getCheckboxData(processCommand.akceptantJestOrganem))
        actualBeneficiaryData.put("akceptantNieMaBeneficjenta", getCheckboxData(processCommand.akceptantNieMaBeneficjenta))

        return actualBeneficiaryData
    }
}
