package com.eservice.eumowy

import com.eservice.eumowy.report.ReportRequest
import com.eservice.eumowy.salesmanreport.ProcessDetails
import com.eservice.eumowy.salesmanreport.ReportData
import com.eservice.eumowy.salesmanreport.SalesmanRow
import com.eservice.eumowy.salesmanreport.SalesmenReportCreator
import com.google.common.collect.Lists
import org.apache.poi.ss.usermodel.Workbook

class ReportService {

    def messageSource

    public Workbook generateSalesmenReport(ReportRequest request) {
        return new SalesmenReportCreator(messageSource, getReportData(request)).getWorkbook()
    }

    public ReportData getReportData(ReportRequest request) {
        List<Process> processes = getProcesses(request)
        return new ReportData(request.dateFrom, request.dateTo, getRows(processes))
    }

    private List<SalesmanRow> getRows(final List<Process> processes) {
        Map<String, List<ProcessDetails>> groupedByPh = [:]

        processes.each {
            ProcessDetails details = new ProcessDetails(it)
            if (groupedByPh.containsKey(it.phFullInfo)) {
                groupedByPh[it.phFullInfo].add(details)
            } else {
                groupedByPh.put(it.phFullInfo, Lists.newArrayList(details))
            }
        }

        return groupedByPh.collect { new SalesmanRow(it.key, it.value) }
    }

    private List<Process> getProcesses(ReportRequest request) {
        List<Process> processes = Process.createCriteria().list {
            createAlias("client", "c")
            between("lastUpdated", request.dateFrom, request.dateTo)
            if (request.phNumber) eq("phNumber", request.phNumber)
            if (request.phSurname) eq("phSurname", request.phSurname)
            if (request.nip) eq("c.nip", request.nip)
            if (request.segment) eq("saleSection", "SEGMENT " + request.segment)
            if (request.status) eq("status", Process.ProcessStatus.find { it.toString().equals(request.status) })
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

        return processes
    }
}
