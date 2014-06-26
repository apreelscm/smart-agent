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
            employeeAnnotations.put("akceptantWeryfikacjaDokumenty", getCheckboxData(true))
        } else if (processCommand.isOsobaPrawna()) {
            employeeAnnotations.put("akceptantWeryfikacjaKRS", getCheckboxData(true))
        }

        employeeAnnotations.put("beneficjentWeryfikacjaKRS", getCheckboxData(processCommand.beneficjentWeryfikacjaKRS))
        employeeAnnotations.put("beneficjentKRS", [processCommand.beneficjentKRS] as String[])
        employeeAnnotations.put("beneficjentWeryfikacjaDokumentTozsamosci", getCheckboxData(processCommand.beneficjentWeryfikacjaDokumentTozsamosci))
        employeeAnnotations.put("beneficjentWeryfikacjaGielda", getCheckboxData(processCommand.beneficjentWeryfikacjaGielda))
        employeeAnnotations.put("beneficjentWeryfikacjaSpolka", getCheckboxData(processCommand.beneficjentWeryfikacjaSpolka))
        employeeAnnotations.put("beneficjentWeryfikacjaKsiega", getCheckboxData(processCommand.beneficjentWeryfikacjaKsiega))
        employeeAnnotations.put("beneficjentWeryfikacjaSchemat", getCheckboxData(processCommand.beneficjentWeryfikacjaSchemat))

        return employeeAnnotations
    }
}
