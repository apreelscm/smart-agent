package com.eservice.eumowy.salesmanreport

import com.eservice.eumowy.util.ExcelHelper
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.springframework.context.MessageSource

import static com.eservice.eumowy.util.ExcelHelper.writeDataCell

class SalesmenReportCreator {
    private static final String[] HEADERS = ["salesmen.report.header.ph", "salesmen.report.header.nip",
                                             "salesmen.report.header.segment", "salesmen.report.header.status",
                                             "salesmen.report.header.bisnode", "salesmen.report.header.acceptorChange",
                                             "salesmen.report.header.activity"]
    private static final int TITLE_HEADER_HEIGHT = 2

    private final MessageSource messageSource
    private final ReportData reportData
    private final Workbook workbook
    private final Sheet sheet

    public SalesmenReportCreator(MessageSource messageSource, ReportData reportData) {
        this.messageSource = messageSource
        this.reportData = reportData
        this.workbook = new HSSFWorkbook()
        this.sheet = workbook.createSheet(getMessage("salesman.report.process"))
    }

    public Workbook getWorkbook() {
        ExcelHelper.writeBigHeader(sheet, reportData.reportHeader, HEADERS.length, TITLE_HEADER_HEIGHT)
        ExcelHelper.writeHeadersToNextRow(sheet, translatedHeaders)

        reportData.rows.each { writeRow(it) }

        return workbook
    }

    private void writeRow(SalesmanRow salesmanRow) {
        Row firstRow = ExcelHelper.createNextRow(sheet)

        writePhDetails(firstRow, salesmanRow.ph, salesmanRow.detailsCount)

        salesmanRow.details.eachWithIndex { ProcessDetails details, Integer i ->
            Row row = sheet.getRow(firstRow.rowNum + i)

            if (!row) {
                row = sheet.createRow(firstRow.rowNum + i)
                writeDataCell(sheet, row, "")
            }

            writeDataCell(sheet, row, details.clientNip)
            writeDataCell(sheet, row, details.salesSegment)
            writeDataCell(sheet, row, details.status)
            writeDataCell(sheet, row, getText(details.bisnode))
            writeDataCell(sheet, row, getText(details.acceptorChange))
            writeDataCell(sheet, row, details.activities)
        }
    }

    private void writePhDetails(Row row, String phDetails, int height) {
        Cell cell = writeDataCell(sheet, row, phDetails)
        cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_CENTER)
        sheet.addMergedRegion(new CellRangeAddress(row.rowNum, row.rowNum + height - 1, 0, 0))
    }

    private String[] getTranslatedHeaders() {
        HEADERS.collect { getMessage(it) }
    }

    private String getText(Boolean test) {
        return test ? getMessage("yes") : getMessage("no")
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault())
    }
}
