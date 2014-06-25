package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper

class EmployeeAnnotationsMapper extends AbstractPdfMapper implements Mapper{
    private ProcessCommand processCommand

    public EmployeeAnnotationsMapper(ProcessCommand processCommand) {
        this.processCommand = processCommand
    }

    @Override
    public Map getDataForMapping() {
        Map employeeAnnotations = [:]

        if(processCommand.isOsobaFizyczna() || processCommand.isJednostkaNieposiadajacaOsobyPrawnej()) {
            addCheckbox(employeeAnnotations, "akceptantWeryfikacjaDokumenty")
        } else if (processCommand.isOsobaPrawna()) {
            addCheckbox(employeeAnnotations, "akceptantWeryfikacjaKRS")
        }

        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaKRS", true, processCommand.beneficjentWeryfikacjaKRS)
        employeeAnnotations.put("beneficjentKRS", [processCommand.beneficjentKRS] as String[])
        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaDokumentTozsamosci", true, processCommand.beneficjentWeryfikacjaDokumentTozsamosci)
        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaGielda", true, processCommand.beneficjentWeryfikacjaGielda)
        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaSpolka", true, processCommand.beneficjentWeryfikacjaSpolka)
        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaKsiega", true, processCommand.beneficjentWeryfikacjaKsiega)
        addCheckbox(employeeAnnotations, "beneficjentWeryfikacjaSchemat", true, processCommand.beneficjentWeryfikacjaSchemat)

        return employeeAnnotations
    }
}
