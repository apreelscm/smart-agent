package com.eservice.eumowy

import java.awt.image.BufferedImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch

import pdfgenerator.PdfGenerator
import signaturepad.SignatureToImage

class PdfService {
	def appParametersService
    def processService
    def calculatorService
    def mapperService
	
	private ExecutorService executor;

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
	
	def generateAllPreviews(List<DocumentFile> documents, Long processId, Integer totalPagesCount) {
		def imageUris = new HashMap<Integer, String>()
		// Cleaning old previews
		List<File> filesToClean = FileUtils.listFiles(new File(appParametersService.getPdfImagePath("")), new WildcardFileFilter("*-"+processId+"-*.png"), null)
		filesToClean.each { File f ->
			log.info "Removing old preview image: " + f.toString()
			FileUtils.deleteQuietly(f)
		}
		log.info "Generating Pdf Previews - " + totalPagesCount + " pages in total!"
		executor = Executors.newFixedThreadPool(appParametersService.getPdfPreviewThreadWorkersCount());
		def threadMethod = { content, did, pid, docpage, globalpage ->
			log.info "GlobalPage: " + globalpage
			generateImageFromPDF(content, did, pid, docpage)
			File oldFile = new File(appParametersService.getPdfImagePath("lock-"+did+"-"+pid+"-"+docpage+".png"))
			File newFile = new File(appParametersService.getPdfImagePath(did+"-"+pid+"-"+docpage+".png"))
			log.info "Moving from " + oldFile.toString() + " to " + newFile.toString()
			FileUtils.copyFile(oldFile, newFile) //Overwrites
			FileUtils.deleteQuietly(oldFile)
			return
		}
		for (int i = 1; i <= totalPagesCount; i++) {
			def data = getDocumentAndPageCountFromGlobalPageNumber(documents, i)
			if (data.document != null) {
				executor.execute { threadMethod(data.document.content.content.clone(), new Long(data.document.id), processId.toString(), new Integer(data.page), new Integer(i)) }
			} else {
				log.warn "generateAllPreviews - document == null"
			}
		}
	}
	
	def getImageFromPDFDocumentFile(List<DocumentFile> documents, String processId, Integer pageNumber) {
		String result = ""
		log.info documents
		def data = getDocumentAndPageCountFromGlobalPageNumber(documents, pageNumber)
		if (data.document != null) {
			File f = new File(appParametersService.getPdfImagePath(data.document.id+"-"+processId+"-"+data.page+".png"))
			while(f.exists() == false) {}
			result = appParametersService.getPdfImageUri(data.document.id+"-"+processId+"-"+data.page+".png")
		}else {
			log.warn "generateImageFromPDFDocumentFile - document == null"
		}
		return result
	}
	
	def generateImageFromPDFDocumentFile(List<DocumentFile> documents, String processId, Integer pageNumber) {
		String result = ""
		log.info documents
		def data = getDocumentAndPageCountFromGlobalPageNumber(documents, pageNumber)
        if (data.document != null) {
			result = generateImageFromPDF(data.document.content.content, data.document.id, processId, data.page)
		}else {
			log.error "generateImageFromPDFDocumentFile - document == null"
		}
		return result
	}
	
	def generateImageFromPDF(byte[] pdf, Long documentId, String processId, Integer pageNumber) {
        StopWatch stopWatch = new Log4JStopWatch()
		ByteArrayInputStream bis = new ByteArrayInputStream(PdfGenerator.closeContent(pdf.clone()))
        PDDocument document = PDDocument.load(bis)
		int resolution = appParametersService.getPdfPreviewImageResolution()
		log.info document
		PDFImageWriter imageWriter = new PDFImageWriter()
		
		boolean success = imageWriter.writeImage(document, "png", "",
			pageNumber, pageNumber, appParametersService.getPdfImagePath("lock-"+documentId+"-"+processId+"-"), BufferedImage.TYPE_INT_RGB, resolution)
	
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
        stopWatch.stop('generateImageFromPDF')
		return appParametersService.getPdfImageUri(documentId+"-"+processId+"-"+pageNumber+".png")
	}

    //METODA PRAWDOPODOBNIE NIE UZYWANA
	def generateImageFromPDF(String pdfPath, String pdfName, String processId, Integer pageNumber) {
		PDDocument document = null
		document = PDDocument.load(pdfPath+pdfName)
		int resolution = appParametersService.getPdfPreviewImageResolution()

		PDFImageWriter imageWriter = new PDFImageWriter()
		boolean success = imageWriter.writeImage(document, "png", "",
				pageNumber, pageNumber, appParametersService.getPdfImagePath(pdfName+"-"+processId+"-"), BufferedImage.TYPE_INT_RGB, resolution)
		
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
		
		return appParametersService.getPdfImageUri(pdfName+"-"+processId+"-"+pageNumber+".png")
	}
	
	def getDocumentAndPageCountFromGlobalPageNumber(List<DocumentFile> documents, Integer pageNumber) {

        def docs = documents.sort(false) {it.signature.signatureOrder}
        def filteredDocs = docs.findAll{it.signature?.showOnPreview}

        if (filteredDocs.size>0){
            Integer pagesCount = 0
            for(DocumentFile doc : filteredDocs) {
                log.info "Document: " + doc + " PageCount: " + doc.pagesCount + " Signature_order: " + doc.signature.signatureOrder
                if (pageNumber >= pagesCount && pageNumber <= pagesCount + doc.pagesCount) {
                    return [document: doc, page: pageNumber - pagesCount]
                }

                pagesCount += doc.pagesCount
            }
        } else {
            log.warn "No documents to show on preview"
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
        if (! sig.templatePath) {
            log.debug('skiping virtual signature')
            return
        }

        Map<String,String[]> dataMap = new HashMap<String, String[]>()

        if (panelData != null) {
            dataMap.putAll(panelData)
        }

        String pdfTemplatePath = appParametersService.getPdfTemplatePath(sig.templatePath)

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
        if (! sig.templatePath){
            log.debug('skiping virtual signature')
            return
        }

        sig.subscriptionDefinitions.findAll { (it.role == Subscription.PersonRole.ZARZAD1 || it.role == Subscription.PersonRole.ZARZAD2) && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
            .eachWithIndex{ SubscriptionDefinition it, int i ->
                dataMap.put(it.role.name() + i, [new File(subscriptionsPath+File.separator+subscriptionsBlackNamePrefix+it.fileName).toURI().toURL(), "", "signature", it.subscriptionPageNumber.toString(), (it.subscriptionX).toString(), it.subscriptionY.toString(), it.scaleX, it.scaleY] as String[])
        }

        if (panelData != null) {
            dataMap.putAll(panelData)
        }

        String pdfTemplatePath = appParametersService.getPdfTemplatePath(sig.templatePath)

        return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, fontType, appParametersService.getFontUri())
    }

    def addClientSubscriptionsToDocument(byte[] documentContent, def sigId, Set<Subscription> subscriptions) {
        byte[] updatedContent = documentContent
        Map<String,Object[]> subscriptionsMap = new HashMap<String, Object[]>()

        Signature sig = Signature.get(sigId)

        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.ACCEPTANT1))
        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.ACCEPTANT2))
        subscriptionsMap.putAll(attachSignatures(sig, subscriptions, Subscription.PersonRole.PH))

        if (!subscriptionsMap.isEmpty()) {
            updatedContent = PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
        }

        return updatedContent
    }
	
	def addBlackFaksymileToDocument(byte[] documentContent, def sigId) {
		byte[] updatedContent = documentContent
		Map<String,Object[]> subscriptionsMap = new HashMap<String, Object[]>()
		String subscriptionsPath = appParametersService.getSubscriptionsPath()
		String subscriptionsBlackNamePrefix = appParametersService.getSubscriptionsBlackPrefix()
		
		Signature sig = Signature.get(sigId)

        sig.subscriptionDefinitions.findAll { (it.role == Subscription.PersonRole.ZARZAD1 || it.role == Subscription.PersonRole.ZARZAD2) && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
                .eachWithIndex{ SubscriptionDefinition it, int i ->
			
			BufferedImage img = SignatureToImage.convertDataToImage(new File(subscriptionsPath+File.separator+subscriptionsBlackNamePrefix+it.fileName).toURI().toURL())
            if (img != null) {
				subscriptionsMap.put(it.role.name() + i, [
					img,
	                it.subscriptionPageNumber,
	                it.subscriptionX,
	                it.subscriptionY,
	                it.scaleX,
	                it.scaleY] as Object[])
            }
			else {
				log.info "Couldn't create black faksymile image from URI for " + sig.templatePath
			}

        }
        updatedContent = PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
        return updatedContent
    }

    private def attachSignatures(Signature sig, Set<Subscription> subscriptions, Subscription.PersonRole personRole) {
        def result = [:]
        Subscription s = subscriptions.find { it.personRole == personRole }
        Set<SubscriptionDefinition> definitions = sig.subscriptionDefinitions.findAll { it.role == personRole && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
        if (s?.content != null && !definitions.isEmpty()) {
            definitions.each{
				BufferedImage img = SignatureToImage.convertDataToImage(s.content)
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
            totalPagesCount += workWithOneDocument(processInstance, sig, dataFromProcess, sig.templatePath, sig.filename)
        }

        processInstance.points.each{ point ->

            if ((point.cbdId == null && point.pointDetails != null) || (point.posDatas && point.posDatas.findAll{ pos -> pos.tpsId == null}.size()>0)){
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

                    def pathClient = sig.filename
                    def beginClient = pathClient.substring(0, pathClient.lastIndexOf('.'));
                    def endClient = pathClient.substring(pathClient.lastIndexOf('.'));
                    def documentClientName = beginClient +  "_" + point.id + endClient

                    log.info "MULTI DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath + " WITH NEW NAME: " + documentName +" CLIENT NAME:"+documentClientName
                    totalPagesCount += workWithOneDocument(processInstance, sig, data, documentName, documentClientName)
                }
            }
        }
		processInstance.discard()
        ['totalPagesCount': totalPagesCount, 'processInstance': processInstance]
    }

    def cleanAgrementDateContent(DocumentContent dc){
        //pola zapleniane na podstawie 'dataUmowy'
        def fieldsToClean = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        PdfGenerator.updateValuesContent(dc, fieldsToClean, "")
    }

    def updateDataUmowyOnDocument(DocumentContent documentContent, String dataUmowy){
        def fieldsToUpdate = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        PdfGenerator.updateValuesContent(documentContent, fieldsToUpdate, dataUmowy)

    }

    private def workWithOneDocument(def processInstance, def sig, def data, def documentName, def documentClientName){
        byte[] documentData = this.fillPdfFormFromURIWithFaksymile(sig.id, data, PdfService.FontType.ARIAL)
        if(!documentData) return 0

        int pc = this.getPageCountFromPdf(documentData)

        if (processService.findDocumentByName(processInstance.documents, documentName) == null) {
            log.info "Creating new document [${sig.templatePath}]"
            DocumentFile df = new DocumentFile(name: documentName, clientName:documentClientName, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc, signature: sig)
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
        return sig.showOnPreview ? pc : 0
    }

}
