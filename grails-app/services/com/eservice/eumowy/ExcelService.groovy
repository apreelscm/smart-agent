package com.eservice.eumowy

import com.eservice.eumowy.data.SalesmenReportData
import com.eservice.eumowy.util.ExcelHelper
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress

class ExcelService {
    def messageSource

    private static final Integer PH_COLUMNS_COUNT = 3
    private static final Integer COLUMN_WIDTH = 3500

    private Integer PROCESS_STATUS_COLUMNS_COUNT
    private CellStyle HEADER_CELL_STYLE
    private CellStyle SUMMARY_CELL_STYLE
    private CellStyle BORDERED_CELL_STYLE
    private CellStyle COLUMN_HEADER_STYLE
    private Workbook workbook

    public Workbook createSalesmenReportWorkBook(SalesmenReportData reportData) {
        workbook = new HSSFWorkbook()

        initializeCellStyles()

        HSSFSheet processStatus = workbook.createSheet(getMessage("salesman.report.processStatus"))
        getReportSheet(processStatus, reportData)

        return workbook
    }

    private void initializeCellStyles() {
        Font headerFont = ExcelHelper.createBoldFont(workbook, 16)
        HEADER_CELL_STYLE = ExcelHelper.createCenteredCellStyle(workbook, headerFont)

        CellStyle sumCellStyle = ExcelHelper.createBorderedCellStyle(workbook, 1, ExcelHelper.Border.WHOLE)
        ExcelHelper.addBackground(sumCellStyle, IndexedColors.AQUA.getIndex())
        ExcelHelper.addBold(workbook, sumCellStyle)
        SUMMARY_CELL_STYLE = sumCellStyle

        BORDERED_CELL_STYLE = ExcelHelper.createBorderedCellStyle(workbook, 1, ExcelHelper.Border.WHOLE)

        Font columnHeaderFont = ExcelHelper.createBoldFont(workbook, 9)
        CellStyle columnHeaderStyle = ExcelHelper.createCenteredCellStyle(workbook, columnHeaderFont)
        ExcelHelper.addBorder(columnHeaderStyle, 2, ExcelHelper.Border.WHOLE)
        COLUMN_HEADER_STYLE = columnHeaderStyle
    }

    private HSSFSheet getReportSheet(HSSFSheet processStatusSheet, SalesmenReportData reportData) {

        Integer subHeaderLeftOffset = 3
        Integer subHeaderWidth = PROCESS_STATUS_COLUMNS_COUNT - subHeaderLeftOffset

        Font subHeaderFont = ExcelHelper.createBoldFont(workbook, 9)
        CellStyle subHeaderCellStyle = ExcelHelper.createCenteredCellStyle(workbook, subHeaderFont)

        ExcelHelper.createHeader(processStatusSheet, reportData.getReportHeader(), HEADER_CELL_STYLE, PROCESS_STATUS_COLUMNS_COUNT, 2)

        CellRangeAddress subHeader = ExcelHelper.createHeader(processStatusSheet, getMessage("salesman.report.statusProcess"), subHeaderCellStyle,
                subHeaderWidth, 1, subHeaderLeftOffset, 0)
        ExcelHelper.addBorder(workbook, processStatusSheet, subHeader, 2, ExcelHelper.Border.WHOLE)

        List<String> columnsHeaders = ["Nazwisko PH", "Imię PH", "Nr PH"]
        Process.ProcessStatus.values().each { status ->
            columnsHeaders.add(status.toString())
        }
        createColumnsHeaders(ExcelHelper.createNextRow(processStatusSheet), columnsHeaders)

        reportData.salesmenStatuses.each { salesman ->
            Row salesmanRow = ExcelHelper.createNextRow(processStatusSheet)
            ExcelHelper.createCell(salesmanRow, salesman.phSurname, BORDERED_CELL_STYLE)
            ExcelHelper.createCell(salesmanRow, salesman.phFirstName, BORDERED_CELL_STYLE)
            ExcelHelper.createCell(salesmanRow, salesman.phNumber, BORDERED_CELL_STYLE)
            salesman.statusesCount.each {
                ExcelHelper.createCell(salesmanRow, it.value, BORDERED_CELL_STYLE)
            }
        }

        createSummaryRow(processStatusSheet, reportData.salesmenStatusesTotal)

        ExcelHelper.setAllColumnsWidth(processStatusSheet, PROCESS_STATUS_COLUMNS_COUNT, COLUMN_WIDTH)
    }

    private void createColumnsHeaders(Row headersRow, List<String> columnsHeaders) {
        columnsHeaders.each { header ->
            ExcelHelper.createCell(headersRow, header, COLUMN_HEADER_STYLE)
        }
    }

    private void createSummaryRow(Sheet sheet, Map summaryData) {
        Row sumRow = ExcelHelper.createNextRow(sheet)
        ExcelHelper.createCell(sumRow, getMessage("salesman.report.sumBS"), SUMMARY_CELL_STYLE)
        ExcelHelper.createCell(sumRow, "", SUMMARY_CELL_STYLE)
        ExcelHelper.createCell(sumRow, getMessage("salesman.report.sum"), SUMMARY_CELL_STYLE)
        summaryData.each { entry ->
            ExcelHelper.createCell(sumRow, entry.value, SUMMARY_CELL_STYLE)
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault())
    }
}
