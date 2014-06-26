package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper


class LegalFormMapper extends AbstractPdfMapper implements Mapper {
    private ProcessCommand processCommand

    public LegalFormMapper(ProcessCommand processCommand) {
        this.processCommand = processCommand
    }

    @Override
    public Map getDataForMapping() {
        Map legalFormData = [:]

        legalFormData.put("osobaFizyczna", getCheckboxData(processCommand.isOsobaFizyczna()))
        legalFormData.put("osobaPrawna", getCheckboxData(processCommand.isOsobaPrawna()))
        legalFormData.put("jednostkaOrg", getCheckboxData(processCommand.isJednostkaNieposiadajacaOsobyPrawnej()))

        return legalFormData
    }
}
