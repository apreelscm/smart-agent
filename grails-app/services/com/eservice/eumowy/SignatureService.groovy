package com.eservice.eumowy

import com.eservice.eumowy.Process

class SignatureService {

    Set<Signature> getSignatures(Activity activity, int listNumber) {
        Set<Signature> signatures = []

        signatures.addAll(activity.activitySignatures.findAll{it.numberOfList == listNumber && it.signature.active})

        return signatures
    }

    Set<Signature> getMandatorySignatures(Activity activity) {
        Set<Signature> signatures = []

        signatures.addAll(activity.activitySignatures.findAll{it.mandatory && it.signature.active})

        return signatures
    }
}
