package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process


class RepresentativesNamesMapper implements Mapper{
    private Process process

    public RepresentativesNamesMapper(Process process) {
        this.process = process
    }

    @Override
    Map getDataForMapping() {
        Map representativesData = [:]

        process.allRepresentatives.eachWithIndex { representative, i->
            representativesData.put("reprezentant" + (i+1), [representative.fullName] as String[])
        }

        return representativesData
    }
}
