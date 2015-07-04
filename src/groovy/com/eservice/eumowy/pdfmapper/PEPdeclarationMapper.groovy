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
        data.put("akceptantNazwaOficjalna", [process.getData("akceptantNazwaOficjalna")] as String[])
        data.put("siedzibaAkceptanta", [getAdresAkceptanta(process)] as String[])

        if (representative.isPolitician) {
            data.put("PEP_true", getCheckboxData(true))
        } else {
            data.put("PEP_false", getCheckboxData(true))
        }

        return data
    }
}
