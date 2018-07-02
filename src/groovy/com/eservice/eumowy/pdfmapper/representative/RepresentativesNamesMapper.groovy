package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process

import static java.lang.String.format


class RepresentativesNamesMapper implements Mapper{
    private Process process

    public RepresentativesNamesMapper(Process process) {
        this.process = process
    }

    @Override
    Map getDataForMapping() {
        Map representativesData = [:]

        process.allRepresentatives.eachWithIndex { representative, i->
            representativesData.put(format("reprezentant%dSalutation", (i+1)), [representative.fullNameWithSalutation] as String[])
            representativesData.put(format("reprezentant%d", (i+1)), [representative.fullName] as String[])
            representativesData.put(format("reprezentant%dFull", (i+1)) , [representative.description] as String[])
        }

        return representativesData
    }
}
