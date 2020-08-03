package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.Process
import com.eservice.eumowy.Representative


class PEPdeclarationMapper extends AbstractPdfMapper implements Mapper{
    private Process process

    public PEPdeclarationMapper(Process process) {
        this.process = process
    }

    @Override
    Map getDataForMapping() {
        Map data = [:]

        if (process.representatives.any {it.isPolitician}) {
            data.put("PEP_true", getCheckedCheckbox())
        } else {
            data.put("PEP_false", getCheckedCheckbox())
        }

        return data
    }
}
