package com.eservice.eumowy.serialidreport

import groovy.transform.builder.Builder

class SerialIdRow {
    private final String acceptorNip
    private final String acceptorName
    private final String phNumber
    private final List<RepresentativeInfo> representatives

    SerialIdRow(String acceptorNip, String acceptorName, String phNumber, List<RepresentativeInfo> representatives) {
        this.acceptorNip = acceptorNip
        this.acceptorName = acceptorName
        this.phNumber = phNumber
        this.representatives = representatives
    }

    String getAcceptorNip() {
        return acceptorNip
    }

    String getAcceptorName() {
        return acceptorName
    }

    String getPhNumber() {
        return phNumber
    }

    List<RepresentativeInfo> getRepresentatives() {
        return representatives
    }
}
