package com.eservice.eumowy.pdfmapper.representative

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.pdfmapper.AbstractPdfMapper
import com.eservice.eumowy.pdfmapper.Mapper
import org.apache.commons.lang.StringUtils


class RepresentativesMapper extends AbstractPdfMapper implements Mapper {
    private ProcessCommand processCommand

    public RepresentativesMapper(ProcessCommand processCommand) {
        this.processCommand = processCommand
    }

    @Override
    public Map getDataForMapping() {
        Map representativesData = [:]

        String prefix = getPrefix(processCommand)

        processCommand.representatives.eachWithIndex { representative, i->
            representativesData.put(getFieldName(prefix, i, "Nazwa"), [representative.getFullName()] as String[])
            representativesData.put(getFieldName(prefix, i, "LokalizacjaDane"), [getLokalizacjaDane(representative)] as String[])

            if(processCommand.isOsobaFizyczna()) {
                addCheckbox(representativesData, getFieldName(prefix, i, "DowOsob"), true, IdentityDocumentType.IDENTITY_CARD.equals(representative.typDokumentu))
                addCheckbox(representativesData, getFieldName(prefix, i, "Paszport"), true, IdentityDocumentType.PASSPORT.equals(representative.typDokumentu))
                representativesData.put(getFieldName(prefix, i, "SeriaNrDokumentu"), [representative.seriaNrDokumentu] as String[])
                representativesData.put(getFieldName(prefix, i, "Obywatelstwo"), [representative.obywatelstwo] as String[])
            } else {
                addCheckbox(representativesData, getFieldName(prefix, i, "PozaRP"), true, representative.isRepresentativeLocationAbroad())
            }
        }

        return representativesData
    }

    private String getPrefix(ProcessCommand processCommand) {
        String prefix

        if(processCommand.isOsobaFizyczna()) {
            prefix = "osFiz_"
        } else if (processCommand.isOsobaPrawna() || processCommand.isJednostkaNieposiadajacaOsobyPrawnej()) {
            prefix = "osPraw_"
        }

        return prefix
    }

    public String getLokalizacjaDane(RepresentativeCommand representative) {
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
