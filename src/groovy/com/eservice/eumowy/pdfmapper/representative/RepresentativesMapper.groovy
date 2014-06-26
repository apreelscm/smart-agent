package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.Representative
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import org.apache.commons.lang.StringUtils
import com.eservice.eumowy.Process


class RepresentativesMapper extends AbstractPdfMapper implements Mapper {
    private Process process

    public RepresentativesMapper(Process process) {
        this.process = process
    }

    @Override
    public Map getDataForMapping() {
        Map representativesData = [:]

        String prefix = getPrefix()

        allRepresentatives.eachWithIndex { representative, i->
            representativesData.put(getFieldName(prefix, i, "Nazwa"), [representative.fullName] as String[])
            representativesData.put(getFieldName(prefix, i, "LokalizacjaDane"), [getLokalizacjaDane(representative)] as String[])

            if(process.akceptantOsobaFizyczna) {
                representativesData.put(getFieldName(prefix, i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(representative.typDokumentu)))
                representativesData.put(getFieldName(prefix, i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(representative.typDokumentu)))
                representativesData.put(getFieldName(prefix, i, "SeriaNrDokumentu"), [representative.seriaNrDokumentu] as String[])
                representativesData.put(getFieldName(prefix, i, "Adres"), [representative.adres] as String[])
                representativesData.put(getFieldName(prefix, i, "Obywatelstwo"), [representative.obywatelstwo] as String[])
            } else {
                representativesData.put(getFieldName(prefix, i, "PozaRP"), getCheckboxData(AcceptorLocation.ABROAD.equals(representative.typLokalizacji)))
            }
        }

        return representativesData
    }
    
    private Set<Representative> getAllRepresentatives() {
        return process.representatives.findAll{Representative.Type.REPRESENTATIVE.equals(it.typ)}
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

    private String getFieldName(String prefix, Integer index, String fieldName) {
        return prefix + "reprezentant" + (index+1) + fieldName
    }
}
