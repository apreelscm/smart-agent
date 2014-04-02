package com.eservice.eumowy

import java.awt.image.BufferedImage
import java.util.concurrent.ExecutorService
import org.apache.pdfbox.pdmodel.PDDocument

import pdfgenerator.PdfGenerator
import signaturepad.SignatureToImage

class PdfService {
	def appParametersService
    def calculatorService
    def mapperService
    def documentService
	
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

    private static final EMPTY_VALUES = ["", "-"]
	
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
        } else {
            log.info("There is no subscription definitions for signature: " + sig.name)
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
        def singleDocuments = processInstance.signatures.findAll{ sig -> !sig.forPoint && !sig.forPos}
        def multiPointDocuments = processInstance.signatures.findAll{ sig -> sig.forPoint}
        def multiPosDocuments = processInstance.signatures.findAll{ sig -> sig.forPos}

        singleDocuments.each { sig ->
            log.info "SINGLE DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath
            totalPagesCount += workWithOneDocument(processInstance, sig, dataFromProcess, sig.templatePath, sig.filename)
        }

        processInstance.points.each{ PointData point ->

            //if ((point.cbdId == null && point.pointDetails != null) || (point.posDatas && point.posDatas.findAll{ pos -> pos.tpsId == null}.size()>0)){
			if ((point?.isLocal()) || (point.posDatas && point.posDatas?.findAll{ pos -> pos != null && pos?.isLocal() == true}.size()>0)){
                //generujemy tylko dokumenty dla tych punktow, ktore nie sa z CBD
                def dataFromPoint = mapperService.mapOnlyPointData(point)

                final Map<String, String> data = new HashMap<String, String>();
                data.putAll(dataFromProcess);
                data.putAll(dataFromPoint);

                multiPointDocuments.each { sig ->
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

        multiPosDocuments.each { sig ->
            processInstance.posExchanges.findAll{it.isChoosen}.each {
                PointData point = PointData.findByCbdIdAndProcess(it.cbdId, processInstance)

                def data = [:]
                data.putAll(mapperService.mapOnlyPointAddress(point))
                data.putAll(mapperService.mapOnlyPosExchangeData(it))

                def path = sig.templatePath
                def begin = path.substring(0, path.lastIndexOf('.'));
                def end = path.substring(path.lastIndexOf('.'));
                def documentName = begin +  "_" + point.id + "_" + it.id + "-p" + end

                def pathClient = sig.filename
                def beginClient = pathClient.substring(0, pathClient.lastIndexOf('.'));
                def endClient = pathClient.substring(pathClient.lastIndexOf('.'));
                def documentClientName = beginClient +  "_" + point.id + "_" + it.id +"-p"+ endClient

                log.info "MULTI POS DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath + " WITH NEW NAME: " + documentName +" CLIENT NAME:"+documentClientName
                totalPagesCount += workWithOneDocument(processInstance, sig, data, documentName, documentClientName)
            }
        }

        //usuwamy dokumenty, ktore byly wygenerowane dla obecnie usunietych punktow/posow
        processInstance.documents.findAll{it.signature.forPoint}.each {
            long idFromName = fetchPointIdFromName(it.clientName)

            if (idFromName != -1){
                def toDelete = true;
                for (PointData pd : processInstance.points){
                    if (idFromName == pd.id){
                        toDelete = false;
                        break;
                    }
                }

                if (toDelete){
                    println 'Usuwam plik dla nieistniejacego POSa: ' + idFromName + ' (' + it.clientName + ')'
                    processInstance.removeFromDocuments(it)
                    processInstance.save(flush: true)
                }
            }
        }

        //usuwamy dokumenty, ktore byly wygenerowane dla obecnie usunietych posExchange
        processInstance.documents.findAll{it.signature.forPos}.each {
            long idFromName = fetchPosExchangeIdFromName(it.clientName)

            if (idFromName != -1){
                def toDelete = true;
                for (PosExchange pe : processInstance.posExchanges){
                    if (idFromName == pe.id){
                        toDelete = false;
                        break;
                    }
                }

                if (toDelete){
                    println 'Usuwam plik dla nieistniejacego Pos Exchange: ' + idFromName + ' (' + it.clientName + ')'
                    processInstance.removeFromDocuments(it)
                    processInstance.save(flush: true)
                }
            }
        }

		processInstance.discard()
        ['totalPagesCount': totalPagesCount, 'processInstance': processInstance]
    }

    def fetchPointIdFromName(String name){
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_')+1, name.lastIndexOf(".pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID Punktu z nazwy: ' + name)
        }

        return result;
    }

    def fetchPosExchangeIdFromName(String name){
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_')+1, name.lastIndexOf("-p.pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID PosExchange z nazwy: ' + name)
        }

        return result;
    }

    def cleanAgrementDateContent(DocumentContent dc){
        //pola zapleniane na podstawie 'dataUmowy'
        def fieldsToClean = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        PdfGenerator.updateValuesContent(dc, fieldsToClean, "")
    }

    def updateDataUmowyOnDocument(DocumentContent documentContent, Process process, String dataUmowy){
        def candidatesForUpdate = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        def processData
        def fieldsToUpdate = []

        candidatesForUpdate.each { field ->
            processData = process.processData?.find { data -> data.name.equals(field)}
            if(processData && !EMPTY_VALUES.contains(processData.value)){
                fieldsToUpdate.add(field)
            }
        }
        PdfGenerator.updateValuesContent(documentContent, fieldsToUpdate, dataUmowy)
    }

    void reloadDataAndSubscriptionsOnDocuments(Process process, def calculator) {
        log.info("Renewing documents")
        Map processWithPages = workWithDocuments(process, calculator)
        process = processWithPages.processInstance
        process.save(flush: true)

        process.documents.each { DocumentFile doc ->
            byte[] newContent = addClientSubscriptionsToDocument(doc.content.content, doc.signature.id, process.subscriptions)
            doc.content.content = newContent
            doc.content.discard()
        }
        log.info("Subscriptions and documents data renewed.")
    }

    private def workWithOneDocument(def processInstance, def sig, def data, def documentName, def documentClientName){
        byte[] documentData = this.fillPdfFormFromURIWithFaksymile(sig.id, data, PdfService.FontType.ARIAL)
        if(!documentData) return 0

        int pc = this.getPageCountFromPdf(documentData)

        if (documentService.findDocumentByName(processInstance.documents, documentName) == null) {
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
            DocumentFile df = documentService.findDocumentByName(processInstance.documents, documentName)
            df.content.setContent(documentData)
            df.lastUpdated = new Date()
            df.save(flush: true)
            processInstance.save(flush: true)
        }
        return sig.showOnPreview ? pc : 0
    }

}
