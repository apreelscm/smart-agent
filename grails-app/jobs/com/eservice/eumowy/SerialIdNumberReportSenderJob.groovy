package com.eservice.eumowy

import org.apache.poi.ss.usermodel.Workbook

class SerialIdNumberReportSenderJob {

    def serialIdNumberReportService
    def emailService

    static triggers = {
        cron name: 'serialIdNumberReportSenderTrigger', cronExpression: '0 59 23 1/1 * ? *'
    }

    def execute() {
        log.info 'Sending serial id number report'
        Workbook workbook = serialIdNumberReportService.getReport()
        emailService.sendSerialIdReport(workbook)
    }
}
