package com.eservice.eumowy.report

import com.eservice.eumowy.Activity
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import com.eservice.eumowy.Process.ProcessStatus;

class ReportRequest {
    private final Date dateFrom
    private final Date dateTo
    private final String nip
    private final Integer segment
    private final String phNumber
    private final String phSurname
    private final String status
    private final Boolean bisnode
    private final Boolean acceptorChange
    private final Long activityId

    public ReportRequest(GrailsParameterMap params) {
        this.dateFrom = params.date("dateFrom", "dd-MM-yyyy")
        this.dateTo = params.date("dateTo", "dd-MM-yyyy")
        this.nip = params.get("nip")
        this.segment = params.getInt("salesSegment")
        this.phNumber = params.get("phNumber")
        this.phSurname = params.get("phSurname")
        this.status = params.get("status")
        this.bisnode = params.get("bisnode").empty ? null : params.boolean("bisnode")
        this.acceptorChange = params.get("acceptorChange").empty ? null : params.boolean("acceptorChange")
        this.activityId = params.long("acceptorChange")
    }

    Date getDateFrom() {
        return dateFrom
    }

    Date getDateTo() {
        return dateTo
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
