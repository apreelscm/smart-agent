package com.eservice.eumowy.util

import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFRegionUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress

class ExcelHelper {

    public static void borderWholeRegion(Integer borderWidth, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        HSSFRegionUtil.setBorderBottom(borderWidth, region, sheet, workbook)
        HSSFRegionUtil.setBorderTop(borderWidth, region, sheet, workbook)
        HSSFRegionUtil.setBorderLeft(borderWidth, region, sheet, workbook)
        HSSFRegionUtil.setBorderRight(borderWidth, region, sheet, workbook)
    }

    public static Font createBoldFont(Workbook workbook, Integer fontHeight) {
        Font font = workbook.createFont()
        font.setFontHeightInPoints((short)fontHeight)
        font.setBoldweight(Font.BOLDWEIGHT_BOLD)
        return font
    }

    public static CellStyle createCenteredCellStyle(Workbook workbook, Font font = null) {
        HSSFCellStyle centeredStyle = workbook.createCellStyle()
        centeredStyle.setAlignment(CellStyle.ALIGN_CENTER)
        centeredStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER)
        if(font) {
            centeredStyle.setFont(font)
        }
        return centeredStyle
    }

    public static void setAllColumnsWidth(Sheet sheet, Integer columnsCount, Integer columnSize) {
        (0..columnsCount - 1).each { columnNumber ->
            sheet.setColumnWidth(columnNumber, columnSize)
        }
    }

    public static Row createNextRow(Sheet sheet) {
        Integer lastRowNumber = sheet.lastRowNum
        return sheet.createRow(lastRowNumber + 1)
    }

    public static CellRangeAddress createHeader(HSSFSheet sheet, String value, Integer width, Integer height) {
        return createHeader(sheet, value, null, width, height, 0, 0)
    }

    public static CellRangeAddress createHeader(HSSFSheet sheet, String value, CellStyle cellStyle, Integer width, Integer height) {
        return createHeader(sheet, value, cellStyle, width, height, 0, 0)
    }

    public static CellRangeAddress createHeader(HSSFSheet sheet, String value, CellStyle cellStyle, Integer width, Integer height, Integer leftOffset, Integer topOffset) {
        Integer sheetLastRowNumber = sheet.lastRowNum ?: -1
        Integer firstHeaderRowNumber = sheetLastRowNumber + 1 + topOffset
        Integer lastHeaderRowNumber = sheetLastRowNumber + height

        (firstHeaderRowNumber..lastHeaderRowNumber).each { rowNumber ->
            sheet.createRow(rowNumber)
        }

        Row firstHeaderRow = sheet.getRow(firstHeaderRowNumber)

        ExcelHelper.createCell(firstHeaderRow, value, leftOffset, cellStyle)

        Integer headerLastCell = leftOffset + width - 1 // cells and rows are 0-based

        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstHeaderRowNumber, lastHeaderRowNumber, leftOffset, headerLastCell)
        sheet.addMergedRegion(cellRangeAddress)

        return cellRangeAddress
    }

    public static Cell createCell(Row row, Object value, HSSFCellStyle style = null){
        createCell(row, value, row.getPhysicalNumberOfCells(), style)
    }

    public static Cell createCell(Row row, Object value, int cellNumber, HSSFCellStyle style = null){
        Cell cell = row.createCell(cellNumber)
        cell.setCellValue(value)
        if (style){
            cell.setCellStyle(style)
        }
        return cell
    }
}
