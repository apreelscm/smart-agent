package com.eservice.eumowy.data

import com.eservice.eumowy.Process

class SalesmenReportRow {
    private final String ph
    private final String salesSegment
    private final String phNumber
    private final String phSurname
    private final Boolean bisnode
    private final Boolean acceptorChange
    private final Map<String, Integer> statuses
    private final Map<String, Integer> activities

    public SalesmenReportRow(Process process) {
        this.clientNip = process.client.nip
        this.salesSegment = process.saleSection
        this.phNumber = process.phNumber
        this.phSurname = process.phSurname
        this.bisnode = process.getBooleanData("isFromBisnode")
        this.acceptorChange = process.getBooleanData("isAcceptorDataChanged")
        this.statuses = process.sta
    }
}
