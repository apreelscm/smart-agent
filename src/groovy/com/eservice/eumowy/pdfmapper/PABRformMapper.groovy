package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.representative.ActualBeneficiaryMapper
import com.eservice.eumowy.pdfmapper.representative.BeneficiariesMapper
import com.eservice.eumowy.pdfmapper.representative.EmployeeAnnotationsMapper
import com.eservice.eumowy.pdfmapper.representative.LegalFormMapper
import com.eservice.eumowy.pdfmapper.representative.RepresentativesMapper

class PABRformMapper implements Mapper {
    private ProcessCommand processCommand

    public PABRformMapper(ProcessCommand processCommand) {
        this.processCommand = processCommand
    }

    @Override
    public Map getDataForMapping() {
        def data = [:]

        data.putAll(new LegalFormMapper(processCommand).getDataForMapping())
        data.putAll(new RepresentativesMapper(processCommand).getDataForMapping())
        data.putAll(new ActualBeneficiaryMapper(processCommand).getDataForMapping())
        data.putAll(new BeneficiariesMapper(processCommand).getDataForMapping())
        data.putAll(new EmployeeAnnotationsMapper(processCommand).getDataForMapping())

        return data
    }
}
