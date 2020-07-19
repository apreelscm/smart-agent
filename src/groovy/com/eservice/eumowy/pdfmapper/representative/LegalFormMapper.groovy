package com.eservice.eumowy.pdfmapper.representative


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

        legalFormData.put("formaOsobaFizyczna", getCheckedCheckbox(process.akceptantOsobaFizyczna))
        legalFormData.put("formaOsobaPrawna", getCheckedCheckbox(process.akceptantOsobaPrawna))
        legalFormData.put("formaJednostkaOrg", getCheckedCheckbox(process.akceptantJednostkaNieposiadajacaOsobyPrawnej))

        return legalFormData
    }
}
