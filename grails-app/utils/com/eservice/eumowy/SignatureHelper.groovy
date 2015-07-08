package com.eservice.eumowy


class SignatureHelper {

    static boolean containsAtLeastOne(Process process, List<String> signatures) {
        boolean hasAtLeastOneSignature = false

        for(String signature : signatures) {
            if(contains(process, signature)) {
                hasAtLeastOneSignature = true
                break
            }
        }

        return hasAtLeastOneSignature
    }

    static boolean contains(Process process, String signatureName) {
        return process.signatures?.any { it.name.equals(signatureName) }
    }
}
