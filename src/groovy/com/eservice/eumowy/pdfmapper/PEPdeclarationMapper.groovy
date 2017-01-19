package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.Process
import com.eservice.eumowy.Representative


class PEPdeclarationMapper extends AbstractPdfMapper implements Mapper{
    private Process process
    private Representative representative

    public PEPdeclarationMapper(Process process, Representative representative) {
        this.process = process
        this.representative = representative
    }

    @Override
    Map getDataForMapping() {
        Map data = [:]

        data.put("nip", [process.getData("nip")] as String[])
        data.put("akceptantNrDomu", [process.getData("akceptantNrDomu")] as String[])
        data.put("akceptantMiasto", [process.getData("akceptantMiasto")] as String[])
        if (process.hasData("akceptantPoczta")) {
            data.put("akceptantPoczta", [process.getData("akceptantPoczta")] as String[])
        }

        if (representative.isPolitician) {
            data.put("PEP_true", getCheckboxData(true))
        } else {
            data.put("PEP_false", getCheckboxData(true))
        }

        return data
    }
}
