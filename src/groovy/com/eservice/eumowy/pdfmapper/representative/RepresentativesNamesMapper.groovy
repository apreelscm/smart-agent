package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Representative
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

        Map<Integer, Representative> representativesToSignDocuments = [:]

        boolean hasNewAgreement = ActivityHelper.isNewAgreement(process)
        boolean hasPABRDocument = process.documents?.any {it.signature.name.contains("PABR") }
        boolean hasAPUPZorAPUW = process.documents?.any {it.signature.name.contains("AP/UPZT") || it.name.contains("AP/UW") }

        int index = 0 //TODO Replace i -> index, to skip empty rows in document, but then you need to adjust subscriptions positions and that requires bigger refactoring
        process.allRepresentatives.eachWithIndex { representative, i ->
            if (hasPABRDocument || hasNewAgreement || hasAPUPZorAPUW) {
                if (Boolean.TRUE == representative.hasSignedContract) {
                    representativesData.put(format("reprezentant%dSalutation", (i + 1)), [representative.fullNameWithSalutation] as String[])
                    representativesData.put(format("reprezentant%d", (i + 1)), [representative.fullName] as String[])
                    representativesData.put(format("reprezentant%dFull", (i + 1)), [representative.description] as String[])
                    index++
                }
            } else {
                representativesData.put(format("reprezentant%dSalutation", (i + 1)), [representative.fullNameWithSalutation] as String[])
                representativesData.put(format("reprezentant%d", (i + 1)), [representative.fullName] as String[])
                representativesData.put(format("reprezentant%dFull", (i + 1)), [representative.description] as String[])
                index++
            }
        }

        return representativesData
    }
}
