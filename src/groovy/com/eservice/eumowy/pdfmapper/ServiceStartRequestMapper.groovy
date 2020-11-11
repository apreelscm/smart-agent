package com.eservice.eumowy.pdfmapper

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

        if (process.getBooleanData("zadanieRozpoczeciaWykonaniaUslugi")) {
            data.put("ssr_true", checkedCheckbox)
        } else {
            data.put("ssr_false", checkedCheckbox)
        }

        process.allRepresentatives.eachWithIndex { representative, i->
            data.put(format("ssr_reprezentant%d", (i+1)), [representative.fullName] as String[])
        }

        return data
    }
}
