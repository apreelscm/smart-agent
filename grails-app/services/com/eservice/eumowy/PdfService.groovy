package com.eservice.eumowy

import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.pdfmapper.PABRformMapper
import com.eservice.eumowy.pdfmapper.PEPdeclarationMapper
import org.apache.log4j.Logger

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
    def processService
	
	private ExecutorService executor;
    private static final Logger LOG = Logger.getLogger(PdfService.class)

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

        return PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
    }

    private def attachSignatures(Signature sig, Set<Subscription> subscriptions, Subscription.PersonRole personRole) {
        Map result = [:]
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

    def workWithDocuments(Process processInstance, def calc){
        Integer totalPagesCount = 0
        Map dataFromProcess = mapperService.mapOnlyProcessData(processInstance, calc)

        if(processService.hasNowaUmowa(processInstance)) {
            dataFromProcess.putAll(new PABRformMapper(processInstance).getDataForMapping())
        }

        //takie rozbicie bylo konieczne aby ograniczyc wywolania wolnego mappera
        Set singleDocuments = processInstance.signatures.findAll{ sig -> !sig.hasDetails()}
        Set multiPointDocuments = processInstance.signatures.findAll{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POINT)}
        Signature posSignature = processInstance.signatures.find{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POS)}
        Signature pepSignature = processInstance.signatures.find{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)}

        singleDocuments.each { sig ->
            log.info "SINGLE DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath
            totalPagesCount += workWithOneDocument(processInstance, sig, dataFromProcess)
        }

        processInstance.points.each{ PointData point ->
			if ((point?.isLocal()) || (point.posDatas && point.posDatas?.findAll{ pos -> pos != null && pos?.isLocal() == true}.size()>0)){
                //generujemy tylko dokumenty dla tych punktow, ktore nie sa z CBD
                Map dataFromPoint = mapperService.mapOnlyPointData(point)

                final Map<String, String> data = new HashMap<String, String>();
                data.putAll(dataFromProcess);
                data.putAll(dataFromPoint);

                multiPointDocuments.each { sig ->
                    String path = sig.templatePath
                    String begin = path.substring(0, path.lastIndexOf('.'));
                    String end = path.substring(path.lastIndexOf('.'));
                    String documentName = begin +  "_" + point.id + end

                    String pathClient = sig.filename
                    String beginClient = pathClient.substring(0, pathClient.lastIndexOf('.'));
                    String endClient = pathClient.substring(pathClient.lastIndexOf('.'));
                    String documentClientName = beginClient +  "_" + point.id + endClient

                    log.info "MULTI DOCUMENT --> SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath + " WITH NEW NAME: " + documentName +" CLIENT NAME:"+documentClientName
                    totalPagesCount += workWithOneDocument(processInstance, sig, data, documentName, documentClientName)
                }
            }
        }

        if(posSignature) {
            processInstance.posExchanges.findAll{it.isChoosen}.each {
                PointData point = PointData.findByCbdIdAndProcess(it.cbdId, processInstance)

                Map data = [:]
                data.putAll(mapperService.mapOnlyPointAddress(point))
                data.putAll(mapperService.mapOnlyPosExchangeData(it))

                String path = posSignature.templatePath
                String begin = path.substring(0, path.lastIndexOf('.'));
                String end = path.substring(path.lastIndexOf('.'));
                String documentName = String.format("%s_%s_%s-p%s", begin, point.id, it.id, end)

                String pathClient = posSignature.filename
                String beginClient = pathClient.substring(0, pathClient.lastIndexOf('.'));
                String endClient = pathClient.substring(pathClient.lastIndexOf('.'));
                String documentClientName = String.format("%s_%s_%s-p%s", beginClient, point.id, it.id, endClient)

                log.info "MULTI POS DOCUMENT --> SIGNATURE NAME: " + posSignature.name + " PDF TEMPLATE PATH: " + posSignature.templatePath + " WITH NEW NAME: " + documentName +" CLIENT NAME:"+documentClientName
                totalPagesCount += workWithOneDocument(processInstance, posSignature, data, documentName, documentClientName)
            }
        }

        if(pepSignature) {
            processInstance.representatives.findAll{AcceptorLocation.ABROAD.equals(it.typLokalizacji)}.each { representative ->
                Map pepData = new PEPdeclarationMapper(processInstance, representative).getDataForMapping()

                String documentName = String.format("Oswiadczenie PEP_%s.pdf", representative.id)

                totalPagesCount += workWithOneDocument(processInstance, pepSignature, pepData, documentName, documentName)
            }
        }

        //usuwamy dokumenty, ktore byly wygenerowane dla obecnie usunietych punktow/posow
        processInstance.documents.findAll{it.signature.hasPurpose(SignatureDetail.SignaturePurpose.POINT)}.each {
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
                    LOG.info('Usuwam plik dla nieistniejacego POSa: ' + idFromName + ' (' + it.clientName + ')')
                    processInstance.removeFromDocuments(it)
                }
            }
        }

        //usuwamy dokumenty, ktore byly wygenerowane dla obecnie usunietych posExchange
        processInstance.documents.findAll{it.signature.hasPurpose(SignatureDetail.SignaturePurpose.POS)}.each {
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
                    LOG.info('Usuwam plik dla nieistniejacego Pos Exchange: ' + idFromName + ' (' + it.clientName + ')')
                    processInstance.removeFromDocuments(it)
                }
            }
        }

        processInstance.documents.findAll{it.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)}.each {
            String representativeId = it.name.substring(it.name.indexOf('_') + 1, it.name.indexOf('.pdf'))

            boolean toDelete = true
            for (Representative representative : processInstance.representatives){
                if(AcceptorLocation.COUNTRY.equals(representative.typLokalizacji)) { //oswiadczenie PEP generowane jest tylko dla AceeptorLocation.ABROAD
                    break
                } else if (representativeId.equals(representative.id.toString())) {
                    toDelete = false
                    break
                }
            }

            if (toDelete) {
                LOG.info(String.format("Usuwam plik dla Reprezentanta: %s (%s)", representativeId, it.clientName))
                processInstance.removeFromDocuments(it)
            }
        }

        processInstance.save(flush: true)

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

    private Integer workWithOneDocument(Process processInstance, Signature signature, Map data) {
        return workWithOneDocument(processInstance, signature, data, signature.templatePath, signature.filename)
    }

    private Integer workWithOneDocument(Process processInstance, Signature sig, Map data, String documentName, String documentClientName){
        byte[] documentData = this.fillPdfFormFromURIWithFaksymile(sig.id, data, PdfService.FontType.ARIAL)
        if(!documentData) return 0

        int pc = this.getPageCountFromPdf(documentData)

        if (documentService.findDocumentByName(processInstance.documents, documentName) == null) {
            log.info "Creating new document [${sig.templatePath}]"
            DocumentFile df = new DocumentFile(name: documentName, clientName:documentClientName, dateCreated: new Date(),
                    lastUpdated: new Date(), pagesCount: pc, signature: sig)
            df.setContent(new DocumentContent(content: documentData))
            df.save(flush: true)
            log.info "DF id: " + df.id + " PageCount: " + df.pagesCount + " ProcessID: " + processInstance.id
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

    def fillPdfFormFromURIWithFaksymile(def sigId, Map<String,String[]> panelData, FontType fontType) {
        fillPdfFormFromURI2(sigId, panelData, fontType, false)
    }

    private def fillPdfFormFromURI2(def sigId, Map<String,String[]> panelData, FontType fontType, boolean withBlackFaksymile) {
        String subscriptionsPath = appParametersService.getSubscriptionsPath()
        String subscriptionsBlackNamePrefix = withBlackFaksymile ? appParametersService.getSubscriptionsBlackPrefix() : "";

        Map<String,String[]> dataMap = new HashMap<String, String[]>()

        Signature sig = Signature.get(sigId);
        if (!sig.templatePath){
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

}
