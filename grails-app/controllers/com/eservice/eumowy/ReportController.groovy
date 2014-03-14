package com.eservice.eumowy

class ReportController {

    def reportService

    def salesmenStatus() {
        Date startDate = params.date('startDate', 'dd-mm-yyyy')
        Date endDate = params.date('endDate', 'dd-mm-yyyy')

        reportService.generateSalesmanStatusReport(response, startDate, endDate)
    }
}
