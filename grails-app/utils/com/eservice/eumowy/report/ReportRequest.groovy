package com.eservice.eumowy.report

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class ReportRequest {
    private final Date createDateFrom
    private final Date createDateTo
    private final Date updateDateFrom
    private final Date updateDateTo
    private final String nip
    private final Integer segment
    private final String phNumber
    private final String phSurname
    private final String status
    private final Boolean bisnode
    private final Boolean acceptorChange
    private final Long activityId

    public ReportRequest(GrailsParameterMap params) {
        this.createDateFrom = params.date("createDateFrom", "dd-MM-yyyy")
        this.createDateTo = params.date("createDateTo", "dd-MM-yyyy")
        this.updateDateFrom = params.date("updateDateFrom", "dd-MM-yyyy")
        this.updateDateTo = params.date("updateDateTo", "dd-MM-yyyy")
        this.nip = params.get("nip")
        this.segment = params.getInt("salesSegment")
        this.phNumber = params.get("phNumber")
        this.phSurname = params.get("phSurname")
        this.status = params.get("status")
        this.bisnode = params.get("bisnode").empty ? null : params.boolean("bisnode")
        this.acceptorChange = params.get("acceptorChange").empty ? null : params.boolean("acceptorChange")
        this.activityId = params.long("acceptorChange")
    }

    Date getCreateDateFrom() {
        return createDateFrom
    }

    Date getCreateDateTo() {
        return createDateTo
    }

    Date getUpdateDateFrom() {
        return updateDateFrom
    }

    Date getUpdateDateTo() {
        return updateDateTo
    }

    String getNip() {
        return nip
    }

    Integer getSegment() {
        return segment
    }

    String getPhNumber() {
        return phNumber
    }

    String getPhSurname() {
        return phSurname
    }

    String getStatus() {
        return status
    }

    Boolean getBisnode() {
        return bisnode
    }

    Boolean getAcceptorChange() {
        return acceptorChange
    }

    Long getActivityId() {
        return activityId
    }
}
