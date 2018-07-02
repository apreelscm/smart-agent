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

        legalFormData.put("formaOsobaFizyczna", getCheckboxData(process.akceptantOsobaFizyczna))
        legalFormData.put("formaOsobaPrawna", getCheckboxData(process.akceptantOsobaPrawna))
        legalFormData.put("formaJednostkaOrg", getCheckboxData(process.akceptantJednostkaNieposiadajacaOsobyPrawnej))

        return legalFormData
    }
}
