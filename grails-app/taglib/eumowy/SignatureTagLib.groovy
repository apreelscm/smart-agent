package eumowy

import com.eservice.eumowy.Activity
import com.eservice.eumowy.Signature

class SignatureTagLib {
    def signatureService

    static namespace = "sig"

    Closure list = { attrs ->
        int listNumber = Integer.valueOf(attrs.listNumber)
        Activity activity = attrs.activity

        Set<Signature> signaturesList = signatureService.getSignatures(activity, listNumber)
        Long selectedOption = activity?.selectedActivitySignatures?.find { it.numberOfList == listNumber }?.id

        out << render(template: '/tagLib/sig/list',
                model: [activity: activity, signaturesList: signaturesList, selectedOption: selectedOption,
                        listNumber: listNumber])
    }

    Closure mandatory = { attrs ->
        Activity activity = attrs.activity

        Set<Long> mandatorySignaturesIds = signatureService.getMandatorySignatures(activity)*.id

        out << render(template: '/tagLib/sig/mandatory', model: [activity: activity, mandatorySignatures: mandatorySignaturesIds])
    }
}
