package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process

class EmployeeAnnotationsMapper extends AbstractPdfMapper implements Mapper{
    private Process process

    public EmployeeAnnotationsMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map employeeAnnotations = [:]

        if(process.akceptantOsobaFizyczna || process.akceptantJednostkaNieposiadajacaOsobyPrawnej) {
            employeeAnnotations.put("akceptantWeryfikacjaDokumenty", getCheckboxData(true))
        } else if (process.akceptantOsobaPrawna) {
            employeeAnnotations.put("akceptantWeryfikacjaKRS", getCheckboxData(true))
        }

        employeeAnnotations.put("beneficjentWeryfikacjaKRS", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaKRS")))
        employeeAnnotations.put("beneficjentKRS", [process.getData("beneficjentKRS")] as String[])
        employeeAnnotations.put("beneficjentWeryfikacjaDokumentTozsamosci", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaDokumentTozsamosci")))
        employeeAnnotations.put("beneficjentWeryfikacjaGielda", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaGielda")))
        employeeAnnotations.put("beneficjentWeryfikacjaSpolka", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaSpolka")))
        employeeAnnotations.put("beneficjentWeryfikacjaKsiega", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaKsiega")))
        employeeAnnotations.put("beneficjentWeryfikacjaSchemat", getCheckboxData(process.getBooleanData("beneficjentWeryfikacjaSchemat")))

        return employeeAnnotations
    }
}
