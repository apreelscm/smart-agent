package com.eservice.eumowy

import eservice.docx.converter.DocxConverter
import eservice.docx.converter.DocxMapper

class DocxService {

    def convert(templateFileName, outputFileName, domainObject) {
        DocxConverter.convert(templateFileName, outputFileName, DocxMapper.process(domainObject))
    }

    def convertWithDocxObject(templateFileName, outputFileName, docxObject) {
        DocxConverter.convert(templateFileName, outputFileName, docxObject)
    }
}
