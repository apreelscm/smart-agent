package com.eservice.eumowy

import com.eservice.eumowy.pdfmapper.FacilitiesMapper
import com.eservice.eumowy.pdfmapper.PABRformMapper
import com.eservice.eumowy.pdfmapper.PdfPointMapper
import com.eservice.eumowy.pdfmapper.PdfPosExchangeMapper
import com.eservice.eumowy.pdfmapper.PdfPosMapper
import com.eservice.eumowy.pdfmapper.PdfProcessMapper
import com.eservice.eumowy.pdfmapper.representative.RepresentativesNamesMapper
import com.eservice.eumowy.util.DateUtils

class MapperService {

    def calculatorService
    def cbdService
    def messageSource

    def mapOnlyPointData(PointData point){
        def data = [:]
        data.putAll(new PdfPointMapper().mapPointDataToPDFData(point))
        data.putAll(new PdfPosMapper(messageSource).mapPosesDataToPDFData(point.posDatas))
        data
    }

    def mapOnlyProcessData(Process processInstance, def calc){
        PdfProcessMapper processMapper = new PdfProcessMapper(processInstance, calculatorService, calc)
        def data = [:]

        data.putAll(processMapper.mapOnlyProcessData())
        data.putAll(new RepresentativesNamesMapper(processInstance).getDataForMapping())

        if(ActivityHelper.isNewAgreement(processInstance)) {
            data.putAll(new PABRformMapper(processInstance).getDataForMapping())
        }

        if(ActivityHelper.isBundleActivity(processInstance)) {
            data.putAll(new FacilitiesMapper(processInstance).getDataForMapping())
        }

        data
    }

    def mapOnlyPosExchangeData(PosExchange posExchange){
        new PdfPosExchangeMapper().mapOnlySelectedPosExchanges(posExchange)
    }

    def mapOnlyPointAddress(def point){
        def data = [:]
        data.putAll(new PdfPointMapper().mapPointAddresDataToPDFData(point))

        // zapisanie danych, ktore nie sa pobrane bezposrednio z pdfow (czy to moze tak zostac)
        def opiekaOne = cbdService.getOpiekaSerwisowaOne(point.kodPocztowy)
        data.put("opiekaSerwisowaIII", [opiekaOne ? opiekaOne[0] : ''] as String[])
        data
    }

    public Map mapPosData(List<PosData> poses) {
        Map data = [:]

        if (poses.size() > 0) {
            data.put("dataPoczatkuUzywaniaPOZ", [DateUtils.getFormattedDate(poses[0].dataOd, DateUtils.DD_MM_YYYY)] as String[])
            data.put("dataKoncaUzywaniaPOZ", [DateUtils.getFormattedDate(poses[0].dataDo, DateUtils.DD_MM_YYYY)] as String[])

            poses.eachWithIndex{ pos, i ->
                data.put("numerPOS" + i, [pos.numerZestawuPos] as String[])
                data.put("oplataPOS" + i, [pos.wysokoscOplaty] as String[])
            }
        }

        return data
    }

    public Map mapPointData(List<PointData> points) {
        if (points.size() < 0) return [:]

        Map data = [:]

        points.eachWithIndex{ point, i ->
            data.put("punktAkceptacjaKart" + i, [point.nazwa] as String[])
            data.put("adresAkceptacjaKart" + i, [point.getAddress()] as String[])
        }

        return data
    }

}
