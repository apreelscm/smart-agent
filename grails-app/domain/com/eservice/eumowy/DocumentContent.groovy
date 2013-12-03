package com.eservice.eumowy

import pdfgenerator.PdfGenerator

class DocumentContent implements Serializable {

    byte[] content
	
	static belongsTo = [document:DocumentFile]
	
	static constraints = {
		content nullable:false;
        document()
	}

	static mapping = {
		table name: "DOCUMENT_CONTENT", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.DOCUMENT_CONTENT_SEQ']
		content  sqlType: "blob"
	}
	
	public byte[] getPreviewContent() {
		return PdfGenerator.embbedAllFonts(content)
	}
	
}
