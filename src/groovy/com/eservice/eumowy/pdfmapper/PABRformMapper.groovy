package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.pdfmapper.representative.ActualBeneficiaryMapper
import com.eservice.eumowy.pdfmapper.representative.BeneficiariesMapper
import com.eservice.eumowy.pdfmapper.representative.EmployeeAnnotationsMapper
import com.eservice.eumowy.pdfmapper.representative.LegalFormMapper
import com.eservice.eumowy.pdfmapper.representative.RepresentativesMapper

class PABRformMapper implements Mapper {
    private Process process

    public PABRformMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map data = [:]

        data.putAll(new LegalFormMapper(process).getDataForMapping())
        data.putAll(new RepresentativesMapper(process).getDataForMapping())
        data.putAll(new ActualBeneficiaryMapper(process).getDataForMapping())
        data.putAll(new BeneficiariesMapper(process).getDataForMapping())
        data.putAll(new EmployeeAnnotationsMapper(process).getDataForMapping())

        return data
    }
}
