package com.eservice.eumowy

import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import com.eservice.eumowy.util.ExcelHelper
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress

class ExcelService {
    private static final PROCESS_STATUS_COLUMNS_COUNT = 10
    private static final ACCEPTED_ACTIVITIES_SHEET_WIDTH = 21
    private HSSFCellStyle HEADER_CELL_STYLE
    private Workbook workbook

    public Workbook createSalesmenReportWorkBook(List<SalesmanStatusesDTO> statuses, List<SalesmanAcceptedActivitiesDTO> salesmanAcceptedActivities) {
        workbook = new HSSFWorkbook()

        Font headerFont = ExcelHelper.createBoldFont(workbook, 16)
        HEADER_CELL_STYLE = ExcelHelper.createCenteredCellStyle(workbook, headerFont)

        HSSFSheet processStatus = workbook.createSheet("Procesy status")
        HSSFSheet acceptedActivities = workbook.createSheet("Działania zaakceptowane")

        fillProcessStatusSheet(processStatus, statuses)
        fillAcceptedActivitiesSheet(acceptedActivities, salesmanAcceptedActivities)

        return workbook
    }

    private void fillProcessStatusSheet(HSSFSheet processStatusSheet, List<SalesmanStatusesDTO> salesmenStatuses) {
        Integer subHeaderLeftOffset = 3
        Integer subHeaderWidth = PROCESS_STATUS_COLUMNS_COUNT - subHeaderLeftOffset

        List<String> columnsHeaders = ["Nazwisko PH", "Imię PH", "Nr PH"]
        Process.ProcessStatus.values().each { status ->
            columnsHeaders.add(status.toString())
        }

        Font subHeaderFont = ExcelHelper.createBoldFont(workbook, 9)
        CellStyle subHeaderCellStyle = ExcelHelper.createCenteredCellStyle(subHeaderFont)

        CellRangeAddress mainHeader = ExcelHelper.createHeader(processStatusSheet, "Test", HEADER_CELL_STYLE, PROCESS_STATUS_COLUMNS_COUNT, 2)
        CellRangeAddress subHeader = ExcelHelper.createHeader(processStatusSheet, "Status procesu", subHeaderCellStyle, subHeaderWidth, 1, subHeaderLeftOffset, 0)
        ExcelHelper.borderWholeRegion(2, subHeader, processStatusSheet, workbook)

        createColumnsHeaders(processStatusSheet, columnsHeaders)

        salesmenStatuses.each { salesmanStatus ->
            Row salesmanRow = ExcelHelper.createNextRow(processStatusSheet)
            ExcelHelper.createCell(salesmanRow, salesmanStatus.phSurname)
            ExcelHelper.createCell(salesmanRow, salesmanStatus.phFirstName)
            ExcelHelper.createCell(salesmanRow, salesmanStatus.phNumber)
            salesmanStatus.statusesCount.each { statusCount ->
                ExcelHelper.createCell(salesmanRow, statusCount.value)
            }
        }

        ExcelHelper.setAllColumnsWidth(processStatusSheet, PROCESS_STATUS_COLUMNS_COUNT, 3500)
    }

    private void fillAcceptedActivitiesSheet(HSSFSheet acceptedActivities, List<SalesmanAcceptedActivitiesDTO> salesmanAcceptedActivities) {
        ExcelHelper.createHeader(acceptedActivities, "TEST", HEADER_CELL_STYLE, ACCEPTED_ACTIVITIES_SHEET_WIDTH, 2)
    }

    private void createColumnsHeaders(HSSFSheet sheet, List<String> columnsHeaders) {
        Integer headersRowNumber = sheet.lastRowNum + 1
        Row headersRow = sheet.createRow(headersRowNumber)

        Font headerFont = ExcelHelper.createBoldFont(workbook, 9)
        HSSFCellStyle headerCellStyle = ExcelHelper.createCenteredCellStyle(workbook, headerFont)

        columnsHeaders.each { header ->
            ExcelHelper.createCell(headersRow, header, headerCellStyle)
        }
    }
}
