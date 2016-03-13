package com.eservice.eumowy.util

import org.apache.poi.hssf.util.HSSFRegionUtil
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress

import static com.eservice.eumowy.util.ExcelHelper.Border.WHOLE

class ExcelHelper {
    public static enum Border {
        TOP, RIGHT, BOTTOM, LEFT, WHOLE
    }

    public static Font getBoldFont(Workbook workbook, Integer fontHeight) {
        Font font = workbook.createFont()
        font.setFontHeightInPoints((short)fontHeight)
        font.setBoldweight(Font.BOLDWEIGHT_BOLD)
        return font
    }

    public static CellStyle getCenteredCellStyle(Workbook workbook, Font font = null) {
        CellStyle centeredStyle = workbook.createCellStyle()
        centeredStyle.setAlignment(CellStyle.ALIGN_CENTER)
        centeredStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER)
        if(font) {
            centeredStyle.setFont(font)
        }
        return centeredStyle
    }

    public static CellStyle getBorderedCellStyle(Workbook workbook, Integer borderWidth, Border border) {
        CellStyle style = workbook.createCellStyle()
        return addBorder(style, borderWidth, border)
    }

    public static Row createNextRow(Sheet sheet) {
        Integer lastRowNumber = sheet.lastRowNum
        return sheet.createRow(lastRowNumber + 1)
    }

    public static void writeBigHeader(Sheet sheet, String text, Integer width, Integer height) {
        Workbook workbook = sheet.getWorkbook()
        Font bold = getBoldFont(workbook, 16)
        CellStyle headerStyle = getCenteredCellStyle(sheet.getWorkbook(), bold)

        createHeader(sheet, text, headerStyle, width, height)
    }

    public static void writeHeadersToNextRow(Sheet sheet, String[] headers) {
        Workbook workbook = sheet.workbook
        Row row = sheet.createRow(sheet.lastRowNum + 1)

        Font font = getBoldFont(workbook, 9)
        CellStyle style = getCenteredCellStyle(workbook, font)
        addBorder(style, 2, WHOLE)
        
        headers.each { text ->
            writeCell(row, text, style)
        }
    }

    public static Cell writeDataCell(Sheet sheet, Row row, String value) {
        CellStyle style = getBorderedCellStyle(sheet.workbook, 1, WHOLE)
        writeCell(row, value, style)
    }

    public static CellRangeAddress createHeader(Sheet sheet, String value, CellStyle cellStyle, Integer width, Integer height) {
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

    public static Cell writeCell(Row row, String value, CellStyle style){
        Cell cell = row.createCell(row.getPhysicalNumberOfCells())
        cell.setCellValue(value)
        cell.setCellStyle(style)
        return cell
    }

    public static CellStyle addBorder(CellStyle cellStyle, Integer borderWidth, Border border) {
        short shortBorderWidth = (short) borderWidth

        switch (border) {
            case Border.TOP:
                cellStyle.setBorderTop(shortBorderWidth)
                break
            case Border.RIGHT:
                cellStyle.setBorderRight(shortBorderWidth)
                break
            case Border.BOTTOM:
                cellStyle.setBorderBottom(shortBorderWidth)
                break
            case Border.LEFT:
                cellStyle.setBorderLeft(shortBorderWidth)
                break
            case WHOLE:
                cellStyle.setBorderTop(shortBorderWidth)
                cellStyle.setBorderRight(shortBorderWidth)
                cellStyle.setBorderBottom(shortBorderWidth)
                cellStyle.setBorderLeft(shortBorderWidth)
        }

        return cellStyle
    }
}
