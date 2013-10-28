package com.eservice.eumowy

import com.eservice.eumowy.pdfmapper.PdfPointMapper
import com.eservice.eumowy.pdfmapper.PdfPosMapper
import com.eservice.eumowy.pdfmapper.PdfProcessMapper

class MapperService {

    def calculatorService

    def mapOnlyPointData(def point){
        def data = [:]
        data.putAll(new PdfPointMapper().mapPointDataToPDFData(point))
        data.putAll(new PdfPosMapper().mapPosesDataToPDFData(point.posDatas))
        data
    }

    def mapOnlyProcessData(def processInstance, def calc){
        PdfProcessMapper processMapper = new PdfProcessMapper(calculatorService, calc, new PdfPointMapper(), new PdfPosMapper());
        def data = [:]
        data.putAll(processMapper.mapOnlyProcessData(processInstance))
        data.putAll(mapProcessCalcToPDFData(calc))
        data
    }

    private def mapProcessCalcToPDFData(def calc) {
        def data = [:]
        if (calculatorService != null){
            String value = calculatorService.getCalcProperty(calc,'E_LICZBA_MIES_ZWOL_NAJ_1')
            if (value && value.isInteger()){
                data.put('oplatyPOSMiesiacNaliczania', [String.valueOf(value.toInteger()+1)] as String[])
            } else {
                data.put('oplatyPOSMiesiacNaliczania', ["1"] as String[])
			}
        }
        return data
    }
}
