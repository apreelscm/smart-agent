package com.eservice.eumowy

enum SignatureName {
    APUW("AP/UW/1.008/21-01-01")

    final String currentVersion

    private SignatureName(final String currentVersion) {
        this.currentVersion = currentVersion
    }
}