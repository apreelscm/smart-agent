package com.eservice.eumowy

import com.eservice.eumowy.report.ReportRequest
import com.eservice.eumowy.salesmanreport.ProcessDetails
import com.eservice.eumowy.salesmanreport.ReportData
import com.eservice.eumowy.salesmanreport.Row
import com.eservice.eumowy.salesmanreport.SalesmenReportCreator
import org.apache.poi.ss.usermodel.Workbook

class ReportService {

    def messageSource

    public Workbook generateSalesmenReport(ReportRequest request) {
        return new SalesmenReportCreator(messageSource, getReportData(request)).createAndGetWorkbook()
    }

    public ReportData getReportData(ReportRequest request) {
        Map<String, List<Process>> processes = getProcessesGroupedByPh(request)
        List<Row> rows =  processes.collect { new Row(it.key, it.value.collect { new ProcessDetails(it) }) }
        return new ReportData(request.dateFrom, request.dateTo, rows)
    }

    private Map<String, List<Process>> getProcessesGroupedByPh(ReportRequest request) {
        List<Process> processes = Process.createCriteria().list {
            between("lastUpdated", request.dateFrom, request.dateTo)
            if (request.phNumber) eq("phNumber", request.phNumber)
            if (request.phSurname) eq("phSurname", request.phSurname)
            if (request.nip) eq("c.nip", request.nip)
            if (request.segment) eq("saleSection", "SEGMENT " + request.segment)
            if (request.status) eq("status", request.status)
            if (request.activityId) {
                activities {
                    idEq(request.activityId)
                }
            }
            order('phSurname', 'asc')
        }

        if (request.bisnode) {
            processes = processes.findAll { it.getBooleanData("isFromBisnode") }
        }

        if (request.acceptorChange) {
            processes = processes.findAll { it.getBooleanData("isAcceptorDataChanged") }
        }

        return processes.groupBy { it.phFullInfo }
    }
}
