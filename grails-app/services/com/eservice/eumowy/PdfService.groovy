package com.eservice.eumowy

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import org.springframework.context.ApplicationContext
import pdfgenerator.PdfGenerator

import java.awt.image.BufferedImage

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
	
	def fillPdfFormFromURIWithFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
		int subscriptionDeltaX1 = 250
		int subscriptionDeltaX2 = 380
		
		String subscriptionsPath = appParametersService.getSubscriptionsPath()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		String subscriptionXScale1 = appParametersService.getManagementSubscriptionFirstScaleX()
		String subscriptionXScale2 = appParametersService.getManagementSubscriptionSecondScaleX()
		String subscriptionYScale1 = appParametersService.getManagementSubscriptionFirstScaleY()
		String subscriptionYScale2 = appParametersService.getManagementSubscriptionSecondScaleY()
		
		if (sig.subscriptionPageNumber > -1) {
			dataMap.put("managementSubscription1", [new File(subscriptionsPath+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX1).toString(), sig.subscriptionY.toString(), subscriptionXScale1, subscriptionYScale1] as String[])
			dataMap.put("managementSubscription2", [new File(subscriptionsPath+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX2).toString(), sig.subscriptionY.toString(), subscriptionXScale2, subscriptionYScale2] as String[])
		}
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithoutFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
	
	def fillPdfFormFromURIWithBlackFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
		int subscriptionDeltaX1 = 250
		int subscriptionDeltaX2 = 380
		
		String subscriptionsPath = appParametersService.getSubscriptionsBlackPath()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		String subscriptionXScale1 = appParametersService.getManagementSubscriptionFirstScaleX()
		String subscriptionXScale2 = appParametersService.getManagementSubscriptionSecondScaleX()
		String subscriptionYScale1 = appParametersService.getManagementSubscriptionFirstScaleY()
		String subscriptionYScale2 = appParametersService.getManagementSubscriptionSecondScaleY()
		
		if (sig.subscriptionPageNumber > -1) {
			dataMap.put("managementSubscription1", [new File(subscriptionsPath+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX1).toString(), sig.subscriptionY.toString(), subscriptionXScale1, subscriptionYScale1] as String[])
			dataMap.put("managementSubscription2", [new File(subscriptionsPath+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX2).toString(), sig.subscriptionY.toString(), subscriptionXScale2, subscriptionYScale2] as String[])
		}
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, getFont(fontType))
	}
}
