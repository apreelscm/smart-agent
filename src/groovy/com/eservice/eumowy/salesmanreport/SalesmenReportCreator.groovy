package com.eservice.eumowy.salesmanreport

import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.context.MessageSource

class SalesmenReportCreator {
    private static final String[] HEADERS = ["salesmen.report.header.ph", "salesmen.report.header.nip",
                                             "salesmen.report.header.segment", "salesmen.report.header.status",
                                             "salesmen.report.header.bisnode", "salesmen.report.header.acceptorChange",
                                             "salesmen.report.header.activity"]

    private final MessageSource messageSource
    private final ReportData reportData

    public SalesmenReportCreator(MessageSource messageSource, ReportData reportData) {
        this.messageSource = messageSource
        this.reportData = reportData
    }

    public Workbook createAndGetWorkbook() {
        Workbook workbook = new HSSFWorkbook()
        HSSFSheet sheet = workbook.createSheet(getMessage("salesman.report.process"))


        return workbook
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault())
    }
}
