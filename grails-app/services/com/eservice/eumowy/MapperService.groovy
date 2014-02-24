package com.eservice.eumowy

import com.eservice.eumowy.pdfmapper.PdfPointMapper
import com.eservice.eumowy.pdfmapper.PdfPosExchangeMapper
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
        PdfProcessMapper processMapper = new PdfProcessMapper(calculatorService, calc, new PdfPointMapper(), new PdfPosMapper())
        def data = [:]
        data.putAll(processMapper.mapOnlyProcessData(processInstance))
        data
    }

    def mapOnlyPosExchangeData(PosExchange posExchange){
        new PdfPosExchangeMapper().mapOnlySelectedPosExchanges(posExchange)
    }

    def mapOnlyPointAddress(def point){
        new PdfPointMapper().mapPointAddresDataToPDFData(point)
    }

}
