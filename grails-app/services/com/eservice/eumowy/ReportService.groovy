package com.eservice.eumowy

import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import com.eservice.eumowy.Process.ProcessStatus
import org.apache.poi.ss.usermodel.Workbook

import javax.servlet.http.HttpServletResponse

class ReportService {

    def excelService

    public void generateSalesmanStatusReport(HttpServletResponse response, Date startDate, Date endDate) {
        List<SalesmanStatusesDTO> statuses = salesmenStatuses(startDate, endDate)
        Map<ProcessStatus, Integer> statusesTotal = statusesCount(startDate, endDate)
        List<SalesmanAcceptedActivitiesDTO> salesmanAcceptedActivities = salesmenAcceptedActivities(startDate, endDate)

        response.contentType = 'application/vnd.ms-excel'
        response.setHeader("Content-disposition", "attachment; filename=ProjectReport.xls")
        OutputStream outputStream = response.getOutputStream()

        Workbook workbook = excelService.createSalesmenReportWorkBook(statuses, salesmanAcceptedActivities)

        workbook.write(outputStream)
        outputStream.close()
    }

    private List<SalesmanStatusesDTO> salesmenStatuses(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            if(startDate) {
                ge('lastUpdated', startDate)
            }
            if(endDate) {
                le('lastUpdated', endDate)
            }
            projections {
                groupProperty("phNumber")
                groupProperty("phFirstName")
                groupProperty("phSurname")
                groupProperty("status")
                count("status")
            }
            order('phSurname', 'asc')
        }

        Map<String, SalesmanStatusesDTO> detailsOfPhNumber = [:]

        resultsOfProcessCriteria.each { result ->
            String phNumber = result[0]
            if(detailsOfPhNumber.containsKey(phNumber)) {
                detailsOfPhNumber[phNumber].addStatus(result)
            } else {
                detailsOfPhNumber.put(phNumber, new SalesmanStatusesDTO(result))
            }
        }

        return detailsOfPhNumber.collect { it.value }
    }

    private Map<ProcessStatus, Integer> statusesCount(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            if(startDate) {
                ge('lastUpdated', startDate)
            }
            if(endDate) {
                le('lastUpdated', endDate)
            }
            projections {
                groupProperty("status")
                count("status")
            }
        }

        Map<ProcessStatus, Integer> statusesCount = [:]

        resultsOfProcessCriteria.each { result ->
            statusesCount.put(result[0], result[1])
        }

        return statusesCount
    }

    private List<SalesmanAcceptedActivitiesDTO> salesmenAcceptedActivities(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            createAlias('activities', 'ac')
            eq('status', ProcessStatus.ACCEPTED)
            if(startDate) {
                ge('lastUpdated', startDate)
            }
            if(endDate) {
                le('lastUpdated', endDate)
            }
            projections {
                groupProperty("phNumber")
                groupProperty("phFirstName")
                groupProperty("phSurname")
                groupProperty("ac.code")
                count('ac.code')
            }
            order('phSurname', 'asc')
        }

        Map<String, SalesmanAcceptedActivitiesDTO> acceptedActivities = [:]

        resultsOfProcessCriteria.each { result ->
            String phNumber = result[0]
            if(acceptedActivities.containsKey(phNumber)) {
                acceptedActivities[phNumber].addActivity(result)
            } else {
                acceptedActivities.put(phNumber, new SalesmanAcceptedActivitiesDTO(result))
            }
        }

        return acceptedActivities.collect { it.value }
    }
}
