package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process


class LegalFormMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public LegalFormMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map legalFormData = [:]

        legalFormData.put("formaOsobaFizyczna", getCheckedCheckbox(process.akceptantOsobaFizyczna))
        legalFormData.put("formaOsobaPrawna", getCheckedCheckbox(process.akceptantOsobaPrawna))
        legalFormData.put("formaJednostkaOrg", getCheckedCheckbox(process.akceptantJednostkaNieposiadajacaOsobyPrawnej))

        return legalFormData
    }

    public static String mapLegalFormFromCBD(String legalFormCbdId) {
        switch (legalFormCbdId) {
            case "8":
                LegalForm.BUDGETARY_UNIT
                break
            case "17":
                LegalForm.COOPERATIVE
                break
            case "3":
                LegalForm.CULTURAL_INSTITUTION
                break
            case "15":
                LegalForm.ECONOMIC_SELF_GOVERNMENT
                break
            case "9":
                LegalForm.EDUCATION_UNIT
                break
            case "1":
                LegalForm.FOUNDATION
                break
            case "27":
                LegalForm.HEALTHCARE_CENTER
                break
            case "5":
                LegalForm.INSTITUTE
                break
            case "21":
                LegalForm.LIMITED_COMPANY
                break
            case "22":
                LegalForm.LIMITED_STOCK_COMPANY
                break
            case "20":
                LegalForm.OPEN_COMPANY
                break
            case "23":
                LegalForm.PARTNERSHIP
                break
            case "19":
                LegalForm.PARTNERSHIP_COMPANY
                break
            case "6":
                LegalForm.PERSON
                break
            case "18":
                LegalForm.STOCK_COMPANY
                break
            case "24":
                LegalForm.ZOO_COMPANY
                break
            default:
                null
        }
    }
}
