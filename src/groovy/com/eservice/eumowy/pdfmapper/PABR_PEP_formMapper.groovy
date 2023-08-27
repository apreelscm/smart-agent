package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.pdfmapper.representative.BeneficiariesMapper
import com.eservice.eumowy.pdfmapper.representative.EmployeeAnnotationsMapper
import com.eservice.eumowy.pdfmapper.representative.LegalFormMapper
import com.eservice.eumowy.pdfmapper.representative.RepresentativesDetailsMapper
import com.eservice.eumowy.Process
import org.springframework.context.MessageSource

class PABR_PEP_formMapper implements Mapper {
    private Process process
    private MessageSource messageSource;

    public PABR_PEP_formMapper(Process process, MessageSource messageSource) {
        this.process = process
        this.messageSource = messageSource
    }

    @Override
    public Map getDataForMapping() {
        Map data = [:]

        data.putAll(new LegalFormMapper(process).getDataForMapping())
        data.putAll(new RepresentativesDetailsMapper(process, messageSource).getDataForMapping())
        data.putAll(new BeneficiariesMapper(process).getDataForMapping())
        data.putAll(new EmployeeAnnotationsMapper(process).getDataForMapping())
        data.putAll(new PEPdeclarationMapper(process).getDataForMapping())

        return data
    }
}
