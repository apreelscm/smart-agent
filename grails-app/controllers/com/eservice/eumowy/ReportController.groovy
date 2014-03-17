package com.eservice.eumowy

import org.apache.commons.lang.time.DateUtils

class ReportController {

    def reportService

    def salesmenStatus() {
        Date startDate = params.date('startDate', 'dd-MM-yyyy').clearTime()
        Date endDate = params.date('endDate', 'dd-MM-yyyy').clearTime()

        reportService.generateSalesmanStatusReport(response, startDate, endDate)
    }
}
