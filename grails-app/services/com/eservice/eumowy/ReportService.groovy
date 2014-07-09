package com.eservice.eumowy

import com.eservice.eumowy.Process.ProcessStatus
import com.eservice.eumowy.data.SalesmenReportData
import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import org.apache.poi.ss.usermodel.Workbook

import javax.servlet.http.HttpServletResponse

class ReportService {

    def excelService

    public void generateSalesmanStatusReport(HttpServletResponse response, Date startDate, Date endDate) {
        SalesmenReportData reportData = new SalesmenReportData()
        reportData.setReportDateSpan(startDate, endDate)

        reportData.salesmenStatuses = salesmenStatuses(startDate, endDate)
        reportData.salesmenStatusesTotal =  statusesCount(startDate, endDate)

        reportData.salesmanAcceptedActivities = salesmenAcceptedActivities(startDate, endDate)
        reportData.acceptedActivitiesTotal = acceptedActivitiesCount(startDate, endDate)
        reportData.allActivities = allActivities()

        response.contentType = 'application/vnd.ms-excel'
        response.setHeader("Content-disposition", "attachment; filename=ProjectReport.xls")
        OutputStream outputStream = response.getOutputStream()

        Workbook workbook = excelService.createSalesmenReportWorkBook(reportData)

        workbook.write(outputStream)
        outputStream.close()
    }

    private List<SalesmanStatusesDTO> salesmenStatuses(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            between("lastUpdated", startDate, endDate)
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
            between("lastUpdated", startDate, endDate)
            projections {
                groupProperty("status")
                count("status")
            }
        }

        Map<ProcessStatus, Integer> statusesCount = [:]

        ProcessStatus.values().each { status ->
            statusesCount.put(status, 0)
        }

        resultsOfProcessCriteria.each { result ->
            statusesCount[result[0]] = result[1]
        }

        return statusesCount
    }

    private List<SalesmanAcceptedActivitiesDTO> salesmenAcceptedActivities(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            createAlias('activities', 'ac')
            eq('status', ProcessStatus.ACCEPTED)
            between("lastUpdated", startDate, endDate)
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
                acceptedActivities.put(phNumber, new SalesmanAcceptedActivitiesDTO(result, allActivities()))
            }
        }

        return acceptedActivities.collect { it.value }
    }

    private Map<String, Integer> acceptedActivitiesCount(Date startDate, Date endDate) {
        def processCriteria = Process.createCriteria()
        def resultsOfProcessCriteria = processCriteria.list {
            createAlias('activities', 'ac')
            eq('status', ProcessStatus.ACCEPTED)
            between("lastUpdated", startDate, endDate)
            projections {
                groupProperty("ac.code")
                count('ac.code')
            }
        }

        Map<String, Integer> activitiesCount = [:]

        allActivities().each { activityCode ->
            activitiesCount.put(activityCode, 0)
        }

        resultsOfProcessCriteria.each { result ->
            if(activitiesCount.containsKey(result[0])) {
                activitiesCount[result[0]] = result[1]
            }
        }

        return activitiesCount
    }

    private List<String> allActivities() {
        List<String> allActivities = []
        List<String> excludedFields = ["zmianaWarunkowDcc", "ekonomiczny", "komfort", "prestiz", "poprawDane",
                "odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminala", "wymianaUmowyZaplaty"]

        Activity.findAll().each { activity ->
            if(!excludedFields.contains(activity.code)) {
                allActivities.add(activity.code)
            }
        }

        return allActivities
    }
}
