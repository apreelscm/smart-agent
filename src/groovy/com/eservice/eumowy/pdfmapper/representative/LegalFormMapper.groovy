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

        addCheckbox(legalFormData, "osobaFizyczna", true, [processCommand.isOsobaFizyczna()])
        addCheckbox(legalFormData, "osobaPrawna", true, [processCommand.isOsobaPrawna()])
        addCheckbox(legalFormData, "jednostkaOrg", true, [processCommand.isJednostkaNieposiadajacaOsobyPrawnej()])

        return legalFormData
    }
}
