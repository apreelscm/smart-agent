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
	
	def generateImageFromPDF(List<DocumentFile> documents, String processId, Integer pageNumber) {
		def data = getDocumentAndPageCountFromGlobalPageNumber(documents, pageNumber)
		return generateImageFromPDF(data.document.content, data.document.name, processId, data.page)
	}
	
	def generateImageFromPDF(byte[] pdf, String pdfName, String processId, Integer pageNumber) {
		PDDocument document = null
		ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
		document = PDDocument.load(bis)
		int resolution = 300
		
		PDFImageWriter imageWriter = new PDFImageWriter()
		
		boolean success = imageWriter.writeImage(document, "png", "",
			pageNumber, pageNumber, appParametersService.getPdfImagePath()+pdfName+"-"+processId+"-", BufferedImage.TYPE_INT_RGB, resolution)
	
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		return new File(appParametersService.getPdfImagePath()+pdfName+"-"+processId+"-"+pageNumber).toURI().toURL().toString()
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
		
		return new File(appParametersService.getPdfImagePath()+pdfName+"-"+processId+"-"+pageNumber).toURI().toURL().toString()
	}
	
	def getDocumentAndPageCountFromGlobalPageNumber(List<DocumentFile> documents, Integer pageNumber) {
		Integer pagesCount = 0
		
		for(DocumentFile doc : documents) {
			if (pageNumber >= pagesCount && pageNumber <= pagesCount + doc.pagesCount) {
				return [document: doc, page: pageNumber - pagesCount]
			}
				
			pagesCount += doc.pagesCount		
		}
	}
	
	def getPageCountFromPdf(byte[] pdf) {
		PDDocument document = null
		ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
		document = PDDocument.load(bis)
		
		return document.getNumberOfPages()
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
		
		dataMap.put("subscriptionManagement1", [new File(subscriptionsPath+sig.subscriptionManagement1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX, sig.subscriptionY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		dataMap.put("subscriptionManagement2", [new File(subscriptionsPath+sig.subscriptionManagement2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX+subscriptionDeltaX, sig.subscriptionY+subscriptionDeltaY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithoutFaksymile(Signature sig, FontType fontType) {
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		return PdfGenerator.generatePdfContentFromURI(sig.getTemplatePath(), dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithBlackFaksymile(Signature sig, FontType fontType) {
		int subscriptionDeltaX = 50
		int subscriptionDeltaY = 0
		
		String subscriptionsPath = appParametersService.getSubscriptionsBlackPath()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>();
		
		dataMap.put("subscriptionManagement1", [new File(subscriptionsPath+sig.subscriptionManagement1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX, sig.subscriptionY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		dataMap.put("subscriptionManagement2", [new File(subscriptionsPath+sig.subscriptionManagement2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber, sig.subscriptionX+subscriptionDeltaX, sig.subscriptionY+subscriptionDeltaY, sig.subscriptionXScale, sig.subscriptionYScale] as String[])
		
		return PdfGenerator.generatePdfContentFromURI(sig.getTemplatePath(), dataMap, getFont(fontType))
	}
}
