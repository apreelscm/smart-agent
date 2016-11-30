package com.eservice.eumowy.util

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Workbook

class CellStyleBuilder {
    private final Workbook workbook
    private final CellStyle cellStyle

    public CellStyleBuilder(Workbook workbook) {
        this.workbook = workbook;
        this.cellStyle = workbook.createCellStyle()
    }

    public CellStyleBuilder center() {
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER)
        return this
    }

    public CellStyleBuilder withFont(Font font) {
        cellStyle.setFont(font)
        return this
    }

    public CellStyleBuilder withBorder(Integer borderWidth) {
        short width = (short) borderWidth
        cellStyle.setBorderTop(width)
        cellStyle.setBorderRight(width)
        cellStyle.setBorderBottom(width)
        cellStyle.setBorderLeft(width)
        return this
    }

    public CellStyle build() {
        return cellStyle
    }
}
