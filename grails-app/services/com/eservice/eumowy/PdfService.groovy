package com.eservice.eumowy

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import pdfgenerator.PdfGenerator
import signaturepad.SignatureToImage

import java.awt.image.BufferedImage

class PdfService {
	def appParametersService
    def processService
    def calculatorService
    def mapperService

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

    def fillPdfFormFromURIWithoutFaksymile(Signature sig, Map<String,String[]> panelData, FontType fontType) {
        Map<String,String[]> dataMap = new HashMap<String, String[]>()

        if (panelData != null) {
            dataMap.putAll(panelData)
        }

        String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath

        return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
    }

	def fillPdfFormFromURIWithFaksymile(def sigId, Map<String,String[]> panelData, FontType fontType) {
        fillPdfFormFromURI2(sigId, panelData, fontType, false)
	}

	def fillPdfFormFromURIWithBlackFaksymile(def sigId, Map<String,String[]> panelData, FontType fontType) {
        fillPdfFormFromURI2(sigId, panelData, fontType, true)
	}

    private def fillPdfFormFromURI2(def sigId, Map<String,String[]> panelData, FontType fontType, boolean withBlackFaksymile) {

        String subscriptionsPath = appParametersService.getSubscriptionsPath()
        String subscriptionsBlackNamePrefix = withBlackFaksymile ? appParametersService.getSubscriptionsBlackPrefix() : "";

        Map<String,String[]> dataMap = new HashMap<String, String[]>()

        Signature sig = Signature.get(sigId);
        sig.subscriptionDefinitions.findAll { (it.role == Subscription.PersonRole.ZARZAD1 || it.role == Subscription.PersonRole.ZARZAD2) && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
            .eachWithIndex{ SubscriptionDefinition it, int i ->
                dataMap.put(it.role.name() + i, [new File(subscriptionsPath+subscriptionsBlackNamePrefix+it.fileName).toURI().toURL(), "", "signature", it.subscriptionPageNumber.toString(), (it.subscriptionX).toString(), it.subscriptionY.toString(), it.scaleX, it.scaleY] as String[])
        }

        if (panelData != null) {
            dataMap.putAll(panelData)
        }

        String pdfTemplatePath = appParametersService.getPdfTemplatePath() + sig.templatePath

        return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
    }

    def addClientSubscriptionsToDocument(byte[] documentContent, def sigId, Set<Subscription> subscriptions) {
        byte[] updatedContent = documentContent
        Map<String,Object[]> subscriptionsMap = new HashMap<String, Object[]>()

        Signature sig = Signature.get(sigId);

        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.ACCEPTANT1))
        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.ACCEPTANT2))
        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.PH))

        if (!subscriptionsMap.isEmpty()) {
            updatedContent = PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
        }

        return updatedContent
    }

    private def attachSignatures(Signature sig, Set<Subscription> subscriptions, Subscription.PersonRole personRole) {
        def result = [:]
        Subscription s = subscriptions.find { it.personRole == personRole }
        Set<SubscriptionDefinition> definitions = sig.subscriptionDefinitions.findAll { it.role == personRole && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
        if (s?.content != null && !definitions.isEmpty()) {
            definitions.each{
                BufferedImage img = SignatureToImage.convertJsonToImage(s.content)
                result.put("subscriber_"+it.id, [img, it.subscriptionPageNumber, it.subscriptionX, it.subscriptionY, it.scaleX, it.scaleY] as Object[])
            }
        } else {
            log.info "Subscription without definitions or subscription content found for" + personRole.name() +"! Template path: " + sig.templatePath
        }

        result;
    }

    def workWithDocuments(def processInstance, def calc){
        def totalPagesCount = 0
        def dataFromProcess = mapperService.mapOnlyProcessData(processInstance, calc);

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
                def dataFromPoint = mapperService.mapOnlyPointData(point)

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
        byte[] documentData = this.fillPdfFormFromURIWithFaksymile(sig.id, data, PdfService.FontType.ARIAL)
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
