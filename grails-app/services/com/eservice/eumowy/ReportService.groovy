package com.eservice.eumowy

import com.eservice.eumowy.Process.ProcessStatus
import com.eservice.eumowy.data.SalesmenReportData
import com.eservice.eumowy.report.ReportRequest
import org.apache.poi.ss.usermodel.Workbook
import org.hibernate.criterion.Restrictions

import javax.servlet.http.HttpServletResponse

class ReportService {

    def excelService

    public Workbook generateSalesmenReport(ReportRequest request) {
//        SalesmenReportData reportData = new SalesmenReportData()
//        reportData.setReportDateSpan(request.dateFrom, request.dateTo)
//
//        reportData.salesmenStatuses = salesmenStatuses(request)

        getProcesses(request)
//        return excelService.createSalesmenReportWorkBook(reportData)
        return null
    }

    private List<Process> getProcesses(ReportRequest request) {
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

        Map<String, List<Process>> byPhNumber = processes.groupBy { it.phNumber }

        return []
    }

    private def get() {
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
}
