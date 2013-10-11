package com.eservice.eumowy

import com.eservice.eumowy.pdfmapper.PdfMapper
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import pdfgenerator.PdfGenerator
import signaturepad.SignatureToImage

import java.awt.image.BufferedImage

class PdfService {
	def appParametersService
    def processService
    def calculatorService

	public static enum FontType {
        HELVETICA(""),
        ARIAL("arial.ttf"),
        ARIALBOLD("arialbd.ttf"),
        TIMES_NEW_ROMAN_PSMT("TimesNewRomanPSMT.ttf")

        public String field;

        FontType(final String field){
            this.field = field;
        }
	}
	
	def generateImageFromPDFDocumentFile(List<DocumentFile> documents, String processId, Integer pageNumber) {
		String result = ""
		log.info documents
		def data = getDocumentAndPageCountFromGlobalPageNumber(documents, pageNumber)
        if (data.document != null) {
			result = generateImageFromPDF(data.document.content.content, data.document.id, processId, data.page)
		}else {
			log.warn "generateImageFromPDFDocumentFile - document == null"
			result = ""
		}
		
		return result
	}
	
	def generateImageFromPDF(byte[] pdf, Long documentId, String processId, Integer pageNumber) {
		ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
        PDDocument document = PDDocument.load(bis)
		int resolution = 300
		log.info document
		PDFImageWriter imageWriter = new PDFImageWriter()
		
		boolean success = imageWriter.writeImage(document, "png", "",
			pageNumber, pageNumber, appParametersService.getPdfImagePath()+documentId+"-"+processId+"-", BufferedImage.TYPE_INT_RGB, resolution)
	
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
		
		return appParametersService.getPdfImageUri()+documentId+"-"+processId+"-"+pageNumber+".png"
	}

    //METODA PRAWDOPODOBNIE NIE UZYWANA
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

        def docs = documents.sort(false) {it.signature.signatureOrder}
		
		for(DocumentFile doc : docs) {
			log.info "Document: " + doc + " PageCount: " + doc.pagesCount + " Signature_order: " + doc.signature.signatureOrder
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
			ByteArrayInputStream bis = new ByteArrayInputStream(pdf)
            PDDocument document = PDDocument.load(bis)
			numberOfPages = document.getNumberOfPages()
			document.close()
		} catch (Exception e) {
			log.warn "getPageCountFromPdf - Error while loading PDF file from byte array: " + e
		}
		
		return numberOfPages
	}

	def fillPdfFormFromURI(String urlTemplatePath, Map<String,String[]> dataMap, FontType fontType) {
		return PdfGenerator.generatePdfContentFromURI(urlTemplatePath, dataMap, fontType, appParametersService.getFontUri())
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
		
		if (sig.subscriptionPageNumber != null && sig.subscriptionPageNumber > -1) {
			dataMap.put("managementSubscription1", [new File(subscriptionsPath+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX1).toString(), sig.subscriptionY.toString(), subscriptionXScale1, subscriptionYScale1] as String[])
			dataMap.put("managementSubscription2", [new File(subscriptionsPath+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX2).toString(), sig.subscriptionY.toString(), subscriptionXScale2, subscriptionYScale2] as String[])
		}
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}

        String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
	}
	
	def fillPdfFormFromURIWithoutFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
	}
	
	def fillPdfFormFromURIWithBlackFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
		int subscriptionDeltaX1 = 250
		int subscriptionDeltaX2 = 380
		
		String subscriptionsPath = appParametersService.getSubscriptionsPath()
		String subscriptionsBlackNamePrefix = appParametersService.getSubscriptionsBlackPrefix()
		
		Map<String,String[]> dataMap = new HashMap<String, String[]>()
		
		String subscriptionXScale1 = appParametersService.getManagementSubscriptionFirstScaleX()
		String subscriptionXScale2 = appParametersService.getManagementSubscriptionSecondScaleX()
		String subscriptionYScale1 = appParametersService.getManagementSubscriptionFirstScaleY()
		String subscriptionYScale2 = appParametersService.getManagementSubscriptionSecondScaleY()
		
		if (sig.subscriptionPageNumber != null && sig.subscriptionPageNumber > -1) {
			dataMap.put("managementSubscription1", [new File(subscriptionsPath+subscriptionsBlackNamePrefix+sig.managementSubscription1).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX1).toString(), sig.subscriptionY.toString(), subscriptionXScale1, subscriptionYScale1] as String[])
			dataMap.put("managementSubscription2", [new File(subscriptionsPath+subscriptionsBlackNamePrefix+sig.managementSubscription2).toURI().toURL(), "", "signature", sig.subscriptionPageNumber.toString(), (sig.subscriptionX+subscriptionDeltaX2).toString(), sig.subscriptionY.toString(), subscriptionXScale2, subscriptionYScale2] as String[])
		}
		
		if (panelData != null) {
			dataMap.putAll(panelData)
		}
		
		String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath
		
		return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
	}
	
	def addClientSubscriptionsToDocument(byte[] documentContent, Signature sig, Set<Subscription> subscriptions) {
		byte[] updatedContent = documentContent
		int subscriptionDeltaX = 120
		
		Map<String,Object[]> subscriptionsMap = new HashMap<String, Object[]>()
		int xScale = appParametersService.SUBSCRIPTION_SCALE_X
		int yScale = appParametersService.SUBSCRIPTION_SCALE_Y

		if (sig.subscriptionPageNumber != null && sig.subscriptionPageNumber > -1) {
			Subscription s = subscriptions.find { it.personRole?.equals(Subscription.PersonRole.ACCEPTANT1) == true }
			if (s?.content != null) {
				BufferedImage img = SignatureToImage.convertJsonToImage(s.content)
				subscriptionsMap.put("subscriber"+s.id, [img, sig.subscriptionPageNumber, sig.subscriptionX, sig.subscriptionY, xScale, yScale] as Object[])
				s = null
			}
			else {
				log.info "Subscription without subscription content found for ACCEPTANT1! Template path: " + sig.templatePath
			}
			
			s = subscriptions.find { it.personRole?.equals(Subscription.PersonRole.ACCEPTANT2) == true }
			if (s?.content != null) {
				BufferedImage img = SignatureToImage.convertJsonToImage(s.content)
				subscriptionsMap.put("subscriber"+s.id, [img, sig.subscriptionPageNumber, sig.subscriptionX+subscriptionDeltaX, sig.subscriptionY, xScale, yScale] as Object[])
				s = null
			}
			else {
				log.info "Subscription without subscription content found for ACCEPTANT2! Template path: " + sig.templatePath
			}
		}
		else {
			log.info "No page number for ACCEPTANTS specified - template: " + sig.templatePath
		}
		
		if (sig.phSubscriptionPageNumber != null && sig.phSubscriptionPageNumber > -1) {
			Subscription s = subscriptions.find { it.personRole?.equals(Subscription.PersonRole.PH) == true }
			if (s?.content != null) {
				BufferedImage img = SignatureToImage.convertJsonToImage(s.content)
				subscriptionsMap.put("subscriber"+s.id, [img, sig.phSubscriptionPageNumber, sig.phSubscriptionX, sig.phSubscriptionY, xScale, yScale] as Object[])
				s = null
			}
			else {
				log.info "Subscription without subscription content found for PH! Template path: " + sig.templatePath
			}
		}
		else {
			log.info "No page number for PH specified - template: " + sig.templatePath
		}
		
		if (subscriptionsMap.isEmpty() == false) {	
			updatedContent = PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
		}
		
		return updatedContent
	}

    def workWithDocuments(def processInstance, def calc){
        def totalPagesCount = 0
        def dataFromProcess = new PdfMapper(calculatorService,calc).mapOnlyProcessData(processInstance);

        //takie rozbicie bylo konieczne aby ograniczyc wywolania wolnego mappera
        def singleDocuments = processInstance.signatures.findAll{ sig -> !sig.forPoint}
        def multiDocuments = processInstance.signatures.findAll{ sig -> sig.forPoint}

        singleDocuments.each { sig ->
            log.info "SINGLE DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath
            totalPagesCount += workWithOneDocument(processInstance, sig, dataFromProcess, sig.templatePath)
        }

        processInstance.points.each{ point ->

            if (point.cbdId == null || (point.posDatas && point.posDatas.findAll{ pos -> pos.tpsId == null}.size()>0)){
                //generujemy tylko dokumenty dla tych punktow, ktore nie sa z CBD
                def dataFromPoint = new PdfMapper(calculatorService,calc).mapOnlyPointData(point);

                final Map<String, String> data = new HashMap<String, String>();
                data.putAll(dataFromProcess);
                data.putAll(dataFromPoint);

                multiDocuments.each { sig ->

                    def path = sig.templatePath
                    def begin = path.substring(0, path.lastIndexOf('.'));
                    def end = path.substring(path.lastIndexOf('.'));
                    def documentName = begin +  "_" + point.id + end

                    log.info "MULTI DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath + " WITH NEW NAME: " + documentName
                    totalPagesCount += workWithOneDocument(processInstance, sig, data, documentName)
                }
            }
        }
		processInstance.discard()
        ['totalPagesCount': totalPagesCount, 'processInstance': processInstance]
    }


    private def workWithOneDocument(def processInstance, def sig, def data, def documentName){
        byte[] documentData = this.fillPdfFormFromURIWithFaksymile(sig, data, PdfService.FontType.ARIAL)
        if(!documentData) return 0

        int pc = this.getPageCountFromPdf(documentData)

        if (processService.findDocumentByName(processInstance.documents, documentName) == null) {
            log.info "Creating new document [${sig.templatePath}]"
            DocumentFile df = new DocumentFile(name: documentName, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc, signature: sig)
            df.setContent(new DocumentContent(content: documentData))
            df.save(flush: true)
            log.info "DF id: " + df.id + " PageCount: " + df.pagesCount
            log.info "Process ID: " + processInstance.id
            processInstance.addToDocuments(df)
			processInstance.save(flush: true)
        } else {
            log.info "Updating existing document [${sig.templatePath}]"
            DocumentFile df = processService.findDocumentByName(processInstance.documents, documentName)
            df.content.setContent(documentData)
            df.lastUpdated = new Date()
            df.save(flush: true)
			processInstance.save(flush: true)
        }
        pc
    }

}
