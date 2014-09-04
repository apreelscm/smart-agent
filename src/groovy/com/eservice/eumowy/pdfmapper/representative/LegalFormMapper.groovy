package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process


class LegalFormMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public LegalFormMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map legalFormData = [:]

        legalFormData.put("osobaFizyczna", getCheckboxData(process.akceptantOsobaFizyczna))
        legalFormData.put("osobaPrawna", getCheckboxData(process.akceptantOsobaPrawna))
        legalFormData.put("jednostkaOrg", getCheckboxData(process.akceptantJednostkaNieposiadajacaOsobyPrawnej))

        return legalFormData
    }
}
