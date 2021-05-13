package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Process
import com.eservice.eumowy.util.DateUtils

import static java.lang.String.format
import static java.lang.String.format
import static java.lang.String.format

class ServiceStartRequestMapper extends AbstractPdfMapper implements Mapper {

    private final Process process

    public ServiceStartRequestMapper(Process process) {
        this.process = process
    }

    @Override
    Map getDataForMapping() {
        if (!process.isAkceptantOsobaFizyczna()) return [:]

        Map data = [:]

        String fullStreetName = "${process.getData("akceptantUlicaTytul")} ${process.getData("akceptantUlica")}"
        String date = DateUtils.getFormattedDate(DateUtils.parseWithTimezone(process.getData("dataUmowy")), DateUtils.YYYY_MM_DD)

        data.put("ssr_nip", [process.getData("nip")] as String[])
        data.put("ssr_akceptantNazwaOficjalna", [process.getData("akceptantNazwaOficjalna")] as String[])
        data.put("ssr_akceptantUlica", [fullStreetName] as String[])
        data.put("ssr_akceptantNrDomu", [process.getData("akceptantNrDomu")] as String[])
        data.put("ssr_akceptantNrMieszkania", [process.getData("akceptantNrMieszkania")] as String[])
        data.put("ssr_akceptantMiasto", [process.getData("akceptantMiasto")] as String[])
        data.put("ssr_akceptantKodPocztowy", [process.getData("akceptantKodPocztowy")] as String[])
        data.put("ssr_dataUmowy", [date] as String[])

        if (process.getBooleanData("klauWykonaniaUslugi")) {
            data.put("ssr_true", checkedCheckbox)
        } else {
            data.put("ssr_false", checkedCheckbox)
        }

        boolean hasNewAgreement = ActivityHelper.isNewAgreement(process)
        boolean hasPABRDocument = process.documents?.any {it.signature.name.contains("PABR") }
        boolean hasAPUPZorAPUW = process.documents?.any {it.signature.name.contains("AP/UPZT") || it.name.contains("AP/UW") }

        int index = 0 //TODO Replace i -> index, to skip empty rows in document, but then you need to adjust subscriptions positions and that requires bigger refactoring

        process.allRepresentatives.eachWithIndex { representative, i->
            if (hasPABRDocument || hasNewAgreement || hasAPUPZorAPUW) {
                if (Boolean.TRUE == representative.hasSignedContract) {
                    data.put(format("ssr_reprezentant%d", (i + 1)), [representative.fullName] as String[])
                    index++
                }
            } else {
                data.put(format("ssr_reprezentant%d", (i + 1)), [representative.fullName] as String[])
                index++
            }
        }

        return data
    }
}
