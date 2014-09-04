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
        data.put("siedzibaAkceptanta", [adresAkceptanta] as String[])
        data.put("PEP_true", getCheckboxData(representative.czyStanowiskoPolityczne))
        data.put("PEP_false", getCheckboxData(!representative.czyStanowiskoPolityczne))

        return data
    }

    private String getAdresAkceptanta() {
        StringBuilder sb = new StringBuilder()

        sb.append(process.getData("akceptantUlicaTytul")).append(" ")
                .append(process.getData("akceptantUlica")).append(" ")
                .append(process.getData("akceptantNrDomu"))

        if(process.getData("akceptantNrMieszkania")) {
            sb.append("/").append(process.getData("akceptantNrMieszkania"))
        }

        sb.append(" ").append(process.getData("akceptantMiasto")).append(" ")
                .append(process.getData("akceptantKodPocztowy")).append(" ")

        if(process.getData("akceptantPoczta")) {
            sb.append(process.getData("akceptantPoczta"))
        }

        return sb.toString()
    }
}
