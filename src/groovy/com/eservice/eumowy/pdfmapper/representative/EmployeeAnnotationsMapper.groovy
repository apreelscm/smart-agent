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
            employeeAnnotations.put("akceptantWeryfikacjaDokumenty", getCheckedCheckbox(true))
        } else if (process.akceptantOsobaPrawna) {
            employeeAnnotations.put("akceptantWeryfikacjaKRS", getCheckedCheckbox(true))
        }

        employeeAnnotations.put("beneficjentWeryfikacjaKRS", getCheckedCheckbox(process.getBooleanData("beneficjentWeryfikacjaKRS")))
        employeeAnnotations.put("beneficjentKRS", [process.getData("beneficjentKRS")] as String[])
        employeeAnnotations.put("beneficjentWeryfikacjaGielda", getCheckedCheckbox(process.getBooleanData("beneficjentWeryfikacjaGielda")))
        employeeAnnotations.put("beneficjentWeryfikacjaSpolka", getCheckedCheckbox(process.getBooleanData("beneficjentWeryfikacjaSpolka")))
        employeeAnnotations.put("beneficjentWeryfikacjaKsiega", getCheckedCheckbox(process.getBooleanData("beneficjentWeryfikacjaKsiega")))
        employeeAnnotations.put("beneficjentWeryfikacjaSchemat", getCheckedCheckbox(process.getBooleanData("beneficjentWeryfikacjaSchemat")))

        return employeeAnnotations
    }
}
