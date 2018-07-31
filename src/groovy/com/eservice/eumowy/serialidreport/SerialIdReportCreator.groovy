package com.eservice.eumowy.serialidreport

import com.eservice.eumowy.salesmanreport.ProcessDetails
import com.eservice.eumowy.util.ExcelHelper
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.springframework.context.MessageSource

import java.text.SimpleDateFormat

class SerialIdReportCreator {
    private static final String[] HEADERS = ["serialid.report.header.nip", "serialid.report.header.name", "serialid.report.header.ph",
                                             "serialid.report.header.representative.name", "serialid.report.header.representative.document"]
    private static final int TITLE_HEADER_HEIGHT = 2
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy")

    private final MessageSource messageSource
    private final List<SerialIdRow> rows
    private final Workbook workbook
    private final Sheet sheet
    private final ExcelHelper excelHelper

    public SerialIdReportCreator(MessageSource messageSource, List<SerialIdRow> rows) {
        this.messageSource = messageSource
        this.rows = rows
        this.workbook = new HSSFWorkbook()
        this.sheet = workbook.createSheet(getMessage("serialid.report.title"))
        this.excelHelper = new ExcelHelper(this.workbook)
    }

    public Workbook getWorkbook() {
        excelHelper.writeBigHeader(sheet, getMessage("serialid.report.header"), HEADERS.length, TITLE_HEADER_HEIGHT)
        excelHelper.writeHeadersToNextRow(sheet, translatedHeaders)

        rows.each { writeRow(it) }

        return workbook
    }

    private void writeRow(SerialIdRow data) {
        Row firstRow = excelHelper.createNextRow(sheet)

        writeHighCell(firstRow, data.acceptorNip, data.representatives.size(), 0)
        writeHighCell(firstRow, data.acceptorName, data.representatives.size(), 1)
        writeHighCell(firstRow, data.phNumber, data.representatives.size(), 2)

        data.representatives.eachWithIndex{ RepresentativeInfo entry, int i ->
            Row row = sheet.getRow(firstRow.rowNum + i)

            if (!row) {
                row = sheet.createRow(firstRow.rowNum + i)
                excelHelper.writeDataCell(row, "")
            }

            excelHelper.writeDataCell(row, entry.name)
            excelHelper.writeDataCell(row, entry.documentNumber)
            if (entry.expirationDate) excelHelper.writeDataCell(row, DATE_FORMATTER.format(entry.expirationDate))
            if (entry.issueDate) excelHelper.writeDataCell(row, DATE_FORMATTER.format(entry.issueDate))
        }
    }

    private void writeHighCell(Row row, String text, int height, int col) {
        Cell cell = excelHelper.writeDataCell(row, text)
        cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_CENTER)
        sheet.addMergedRegion(new CellRangeAddress(row.rowNum, row.rowNum + height - 1, col, col))
    }

    private String[] getTranslatedHeaders() {
        HEADERS.collect { getMessage(it) }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault())
    }
}
