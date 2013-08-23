package com.eservice.eumowy

import java.awt.image.BufferedImage

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import org.springframework.context.ApplicationContext

import pdfgenerator.PdfGenerator

class PdfService {
	def appParametersService
	
	public static enum FontType {
		HELVETICA,
		ARIAL,
		ARIALBOLD
	}
	
	ApplicationContext applicationContext
	
	def generateImageFromPDFDocumentFile(List<DocumentFile> documents, String processId, Integer pageNumber) {
		String result = ""
		log.info documents
		def data = getDocumentAndPageCountFromGlobalPageNumber(documents, pageNumber)
		if (data.document != null) {
			result = generateImageFromPDF(data.document.content.content, data.document.name, processId, data.page)
		}
		else {
			log.warn "generateImageFromPDFDocumentFile - document == null"
			result = ""
		}
		
		return result
	}
	
	def generateImageFromPDF(byte[] pdf, String pdfName, String processId, Integer pageNumber) {
		PDDocument document = null
		ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
		document = PDDocument.load(bis)
		int resolution = 300
		log.info document
		PDFImageWriter imageWriter = new PDFImageWriter()
		
		boolean success = imageWriter.writeImage(document, "png", "",
			pageNumber, pageNumber, appParametersService.getPdfImagePath()+pdfName+"-"+processId+"-", BufferedImage.TYPE_INT_RGB, resolution)
	
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
		
		return appParametersService.getPdfImageUri()+pdfName+"-"+processId+"-"+pageNumber+".png"
	}
	
	def generateImageFromPDF(String pdfPath, String pdfName, String processId, Integer pageNumber) {
		PDDocument document = null
		document = PDDocument.load(pdfPath+pdfName)
		int resolution = 300

		PDFImageWriter imageWriter = new PDFImageWriter()
		boolean success = imageWriter.writeImage(document, "png", "",
				pageNumber, pageNumber, appParametersService.getPdfImagePath()+pdfName+"-"+processId+"-", BufferedImage.TYPE_INT_RGB, resolution)
		
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
		
		return appParametersService.getPdfImageUri()+pdfName+"-"+processId+"-"+pageNumber+".png"
	}
	
	def getDocumentAndPageCountFromGlobalPageNumber(List<DocumentFile> documents, Integer pageNumber) {
		Integer pagesCount = 0
		
		for(DocumentFile doc : documents) {
			log.info "Document: " + doc + " PageCount: " + doc.pagesCount
			if (pageNumber >= pagesCount && pageNumber <= pagesCount + doc.pagesCount) {
				return [document: doc, page: pageNumber - pagesCount]
			}
				
			pagesCount += doc.pagesCount		
		}
		
		return [document: null, page: 0]
	}
	
	def getPageCountFromPdf(byte[] pdf) {
		int numberOfPages = 0
		try {
			PDDocument document = null
			ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
			document = PDDocument.load(bis)
			numberOfPages = document.getNumberOfPages()
			document.close()
		}
		catch (Exception e) {
			log.warn "getPageCountFromPdf - Error while loading PDF file from byte array: " + e
			//e.printStackTrace()
		}
		
		return numberOfPages
	}
	
	private String getFont(FontType fontType) {
		String f = null
		
		switch(fontType) {
			case FontType.HELVETICA:
				f = "HELVETICA"
				break;
				
			case FontType.ARIAL:
				f = "ARIAL"
				break;
			
			case FontType.ARIALBOLD:
				f = "ARIALBOLD"
				break;
			
		}
		
		return f
	}
	
	def fillPdfFormFromURI(String urlTemplatePath, Map<String,String[]> dataMap, FontType fontType) {
		return PdfGenerator.generatePdfContentFromURI(urlTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromFile(String fileTemplatePath, Map<String,String[]> dataMap, FontType fontType) {
		return PdfGenerator.generatePdfContentFromFile(fileTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithFaksymile(Signature sig, FontType fontType) {
		int subscriptionDeltaX = 50
		int subscriptionDeltaY = 0
		
		String subscriptionsPath = appParametersService.getSubscriptionsPath()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		dataMap.put("managementSubscription1", [new File(subscriptionsPath+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), sig.subscriptionX.toString(), sig.subscriptionY.toString(), sig.subscriptionXScale.toString(), sig.subscriptionYScale.toString()] as String[])
		dataMap.put("managementSubscription2", [new File(subscriptionsPath+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), sig.subscriptionX+subscriptionDeltaX.toString(), sig.subscriptionY+subscriptionDeltaY.toString(), sig.subscriptionXScale.toString(), sig.subscriptionYScale.toString()] as String[])
		dataMap.put("akceptantNazwa", ["Nazwa Akceptanta Teeest :)"] as String[])
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithoutFaksymile(Signature sig, FontType fontType) {
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithBlackFaksymile(Signature sig, FontType fontType) {
		int subscriptionDeltaX = 50
		int subscriptionDeltaY = 0
		
		String subscriptionsPath = appParametersService.getSubscriptionsBlackPath()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>();
		
		dataMap.put("managementSubscription1", [new File(subscriptionsPath+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX, sig.subscriptionY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		dataMap.put("managementSubscription2", [new File(subscriptionsPath+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX+subscriptionDeltaX, sig.subscriptionY+subscriptionDeltaY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
}
