package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.PointData
import com.eservice.eumowy.Process

class FacilitiesMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public FacilitiesMapper(Process process) {
        this.process = process
    }

    @Override
    Map getDataForMapping() {
        Map data = [:]

        process.localPoints.eachWithIndex { PointData point, int i ->
            data.put("punktNazwa" + (i+1), [point.nazwa] as String[])
            data.put("punktAdres" + (i+1), [getAdresPunktu(point)] as String[])
        }

        return data
    }
}
