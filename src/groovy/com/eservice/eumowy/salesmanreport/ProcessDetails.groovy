package com.eservice.eumowy.salesmanreport

class ProcessDetails {
    private final String salesSegment
    private final Boolean bisnode
    private final Boolean acceptorChange
    private final String status
    private final String activities

    public ProcessDetails(com.eservice.eumowy.Process process) {
        this.salesSegment = process.saleSection
        this.bisnode = process.getBooleanData("isFromBisnode")
        this.acceptorChange = process.getBooleanData("isAcceptorDataChanged")
        this.status = process.status.toString()
        this.activities = process.activities.join(", ")
    }

    String getSalesSegment() {
        return salesSegment
    }

    Boolean getBisnode() {
        return bisnode
    }

    Boolean getAcceptorChange() {
        return acceptorChange
    }

    String getStatus() {
        return status
    }

    String getActivities() {
        return activities
    }
}
