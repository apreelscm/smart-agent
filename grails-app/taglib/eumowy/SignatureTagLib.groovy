package eumowy

import com.eservice.eumowy.Activity
import com.eservice.eumowy.Signature
import com.eservice.eumowy.Process

class SignatureTagLib {
    def signatureService

    static namespace = "sig"

    Closure list = { attrs ->
        int listNumber = Integer.valueOf(attrs.listNumber)
        Activity activity = attrs.activity
        Process process = attrs.process

        Set<Signature> signaturesList = signatureService.getActivitySignatures(process, activity, listNumber)
        Long selectedOption = activity?.selectedActivitySignatures?.find { it.numberOfList == listNumber }?.id

        out << render(template: '/tagLib/sig/list',
                model: [activity: activity, signaturesList: signaturesList, selectedOption: selectedOption,
                        listNumber: listNumber])
    }

    Closure mandatory = { attrs ->
        Activity activity = attrs.activity
        Process process = attrs.process

        Set<Long> mandatorySignaturesIds = signatureService.getMandatoryActivitySignatures(process, activity)*.id

        out << render(template: '/tagLib/sig/mandatory', model: [activity: activity, mandatorySignatures: mandatorySignaturesIds])
    }
}
