package com.eservice.eumowy.util

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress

class ExcelHelper {
    private final CellStyle mainHeaderStyle
    private final CellStyle smallHeaderStyle
    private final CellStyle dataStyle

    public ExcelHelper(Workbook workbook) {
        this.mainHeaderStyle = new CellStyleBuilder(workbook)
                .center()
                .withFont(getBoldFont(workbook, 16))
                .build()
        this.smallHeaderStyle = new CellStyleBuilder(workbook)
                .center()
                .withBorder(2)
                .withFont(getBoldFont(workbook, 9))
                .build()
        this.dataStyle = new CellStyleBuilder(workbook)
                .withBorder(1)
                .build()
    }

    private Font getBoldFont(Workbook workbook, Integer fontHeight) {
        Font font = workbook.createFont()
        font.setFontHeightInPoints((short)fontHeight)
        font.setBoldweight(Font.BOLDWEIGHT_BOLD)
        return font
    }

    public Row createNextRow(Sheet sheet) {
        Integer lastRowNumber = sheet.lastRowNum
        return sheet.createRow(lastRowNumber + 1)
    }

    public void writeBigHeader(Sheet sheet, String text, Integer width, Integer height) {
        createHeader(sheet, text, mainHeaderStyle, width, height)
    }

    public void writeHeadersToNextRow(Sheet sheet, String[] headers) {
        Row row = sheet.createRow(sheet.lastRowNum + 1)
        
        headers.each { text ->
            writeCell(row, text, smallHeaderStyle)
        }
    }

    public Cell writeDataCell(Row row, String value) {
        writeCell(row, value, dataStyle)
    }

    public CellRangeAddress createHeader(Sheet sheet, String value, CellStyle cellStyle, Integer width, Integer height) {
        Integer sheetLastRowNumber = sheet.lastRowNum ?: -1
        Integer firstHeaderRowNumber = sheetLastRowNumber + 1
        Integer lastHeaderRowNumber = sheetLastRowNumber + height

        (firstHeaderRowNumber..lastHeaderRowNumber).each { rowNumber ->
            sheet.createRow(rowNumber)
        }

        Row firstHeaderRow = sheet.getRow(firstHeaderRowNumber)

        writeCell(firstHeaderRow, value, cellStyle)

        Integer headerLastCell = width - 1 // cells and rows are 0-based

        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstHeaderRowNumber, lastHeaderRowNumber, 0, headerLastCell)
        sheet.addMergedRegion(cellRangeAddress)

        return cellRangeAddress
    }

    public Cell writeCell(Row row, String value, CellStyle style){
        Cell cell = row.createCell(row.getPhysicalNumberOfCells())
        cell.setCellValue(value)
        cell.setCellStyle(style)
        return cell
    }
}
