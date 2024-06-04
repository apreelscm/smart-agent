package com.eservice.eumowy


class SignatureHelper {

    static boolean containsAtLeastOne(Process process, List<SignatureName> signatures) {
        boolean hasAtLeastOneSignature = false

        for(SignatureName signature : signatures) {
            if(contains(process, signature)) {
                hasAtLeastOneSignature = true
                break
            }
        }

        return hasAtLeastOneSignature
    }

    static boolean contains(Process process, SignatureName signatureName) {
        return process.signatures?.any { signatureName.matches(it.name) }
    }
}
