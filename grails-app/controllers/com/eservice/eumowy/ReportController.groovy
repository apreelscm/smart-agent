package com.eservice.eumowy

class ReportController {

    def reportService

    def salesmenStatus() {
        Date startDate = params.date('startDate', 'dd-MM-yyyy').clearTime()
        Date endDate = params.date('endDate', 'dd-MM-yyyy').clearTime()

        reportService.generateSalesmanStatusReport(response, startDate, endDate)
    }
}
