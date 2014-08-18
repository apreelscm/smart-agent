package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import org.apache.commons.lang.StringUtils
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
                representativesData.put(getFieldName(i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(representative.typDokumentu)))
                representativesData.put(getFieldName(i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(representative.typDokumentu)))
                representativesData.put(getFieldName(i, "SeriaNrDokumentu"), [representative.seriaNrDokumentu] as String[])
                representativesData.put(getFieldName(i, "Adres"), [representative.adres] as String[])
                representativesData.put(getFieldName(i, "Obywatelstwo"), [representative.obywatelstwo] as String[])
            } else {
                representativesData.put(getFieldName(i, "PozaRP"), getCheckboxData(AcceptorLocation.ABROAD.equals(representative.typLokalizacji)))
            }
        }

        return representativesData
    }

    private String getPrefix() {
        String prefix

        if(process.akceptantOsobaFizyczna) {
            prefix = "osFiz_"
        } else if (process.akceptantOsobaPrawna || process.akceptantJednostkaNieposiadajacaOsobyPrawnej) {
            prefix = "osPraw_"
        }

        return prefix
    }

    public String getLokalizacjaDane(Representative representative) {
        String peselNumber = StringUtils.isEmpty(representative.pesel) ? representative.lokalizacjaPesel : representative.pesel

        if(IdentityDocumentType.PASSPORT.equals(representative.typDokumentu)) {
            return representative.lokalizacjaKraj
        } else if (IdentityDocumentType.IDENTITY_CARD.equals(representative.typDokumentu)) {
            return peselNumber
        }

        return peselNumber ?: representative.dataUrodzenia
    }

    private String getFieldName(Integer index, String fieldName) {
        return prefix + "reprezentant" + (index+1) + fieldName
    }
}
