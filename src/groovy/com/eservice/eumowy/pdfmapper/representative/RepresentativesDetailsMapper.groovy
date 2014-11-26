package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process


class RepresentativesDetailsMapper extends AbstractPdfMapper implements Mapper {
    private Process process
    private String prefix

    public RepresentativesDetailsMapper(Process process) {
        this.process = process
        prefix = getPrefix()
    }

    @Override
    public Map getDataForMapping() {
        Map representativesData = [:]

        process.allRepresentatives.eachWithIndex { representative, i->
            representativesData.put(getFieldName(i, "Nazwa"), [representative.fullName] as String[])
            representativesData.put(getFieldName(i, "LokalizacjaDane"), [getLokalizacjaDane(representative)] as String[])

            if(process.akceptantOsobaFizyczna) {
                representativesData.put(getFieldName(i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(representative.documentType)))
                representativesData.put(getFieldName(i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(representative.documentType)))
                representativesData.put(getFieldName(i, "SeriaNrDokumentu"), [representative.documentNumber] as String[])
                representativesData.put(getFieldName(i, "Adres"), [representative.address] as String[])
                representativesData.put(getFieldName(i, "Obywatelstwo"), [representative.citizenship] as String[])
            } else {
                representativesData.put(getFieldName(i, "PozaRP"), getCheckboxData(AcceptorLocation.ABROAD.equals(representative.locationType)))
            }
        }

        return representativesData
    }

    private String getPrefix() {
        if(process.akceptantOsobaFizyczna) {
            return  "osFiz_"
        } else if (process.akceptantOsobaPrawna || process.akceptantJednostkaNieposiadajacaOsobyPrawnej) {
            return  "osPraw_"
        }

        throw new IllegalStateException("Nie wybrano prefixu")
    }

    public String getLokalizacjaDane(Representative representative) {
        return representative.pesel
    }

    private String getFieldName(Integer index, String fieldName) {
        return prefix + "reprezentant" + (index+1) + fieldName
    }
}
