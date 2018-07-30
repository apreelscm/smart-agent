package com.eservice.eumowy.pdfmapper.representative


import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import com.eservice.eumowy.Process

import java.text.SimpleDateFormat

import static com.google.common.base.Strings.isNullOrEmpty


class RepresentativesDetailsMapper extends AbstractPdfMapper implements Mapper {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd")
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
            representativesData.put(getFieldName(i, "Stanowisko"), [representative.position] as String[])
            representativesData.put(getFieldName(i, "CzyPesel"), getCheckboxData(!isNullOrEmpty(representative.pesel)))
            representativesData.put(getFieldName(i, "Pesel"), [representative.pesel] as String[])
            representativesData.put(getFieldName(i, "CzyDataUrodzenia"), getCheckboxData(representative.birthDate != null))
            representativesData.put(getFieldName(i, "PanstwoUrodzenia"), [representative.birthCountry] as String[])
            representativesData.put(getFieldName(i, "MiejscowoscUrodzenia"), [representative.birthCity] as String[])
            representativesData.put(getFieldName(i, "DowOsob"), getCheckboxData(IdentityDocumentType.IDENTITY_CARD.equals(representative.documentType)))
            representativesData.put(getFieldName(i, "Paszport"), getCheckboxData(IdentityDocumentType.PASSPORT.equals(representative.documentType)))
            representativesData.put(getFieldName(i, "SeriaNrDokumentu"), [representative.documentNumber] as String[])

            if (representative.birthDate) {
                representativesData.put(getFieldName(i, "DataUrodzenia"), [DATE_FORMATTER.format(representative.birthDate)] as String[])
            }

            if (representative.documentIssueDate) {
                representativesData.put(getFieldName(i, "DataWydaniaDokumentu"), [DATE_FORMATTER.format(representative.documentIssueDate)] as String[])
            }

            if (representative.documentExpirationDate) {
                representativesData.put(getFieldName(i, "DataWaznosciDokumentu"), [DATE_FORMATTER.format(representative.documentExpirationDate)] as String[])
            }

            if (process.akceptantOsobaFizyczna) {
                representativesData.put(getFieldName(i, "Adres"), [representative.address] as String[])
                representativesData.put(getFieldName(i, "Obywatelstwo"), [representative.citizenship] as String[])
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

    private String getFieldName(Integer index, String fieldName) {
        return prefix + "reprezentant" + (index+1) + fieldName
    }
}
