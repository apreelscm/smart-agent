package com.eservice.eumowy

import com.eservice.eumowy.report.ReportRequest
import org.apache.poi.ss.usermodel.Workbook

class ReportController {

    def reportService

    def salesmenStatus() {
        ReportRequest request = new ReportRequest(params)

        Workbook workbook = reportService.generateSalesmenReport(request)

        response.contentType = 'application/vnd.ms-excel'
        response.setHeader("Content-disposition", "attachment; filename=ProjectReport.xls")

        OutputStream outputStream = response.getOutputStream()
        workbook.write(outputStream)
        outputStream.close()
    }
}
