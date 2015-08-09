package com.eservice.eumowy

import com.eservice.eumowy.util.DateUtils
import com.lowagie.text.Document
import com.lowagie.text.pdf.PdfCopy
import com.lowagie.text.pdf.PdfReader
import pdfgenerator.PdfGenerator
import pdfgenerator.PdfGenerator.FontType
import signaturepad.SignatureToImage

import java.awt.image.BufferedImage

class PdfService {
	def appParametersService
    def calculatorService
    def documentService
    def subscriptionService

    private static final EMPTY_VALUES = ["", "-"]
	
	String getImageFromPDFDocumentFile(List<DocumentFile> documents, String processId, Integer pageNumber) {
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

	def fillPdfFormFromURI(String urlTemplatePath, Map<String,String[]> dataMap, FontType fontType) {
		return PdfGenerator.generatePdfContentFromURI(urlTemplatePath, dataMap, fontType, appParametersService.getFontUri())
	}

    byte[] getDocumentWithSubscription(DocumentFile document, Subscription subscription) {
        Map<String,Object[]> subscriptionsData = new HashMap<String, Object[]>()

        subscriptionsData.putAll(attachSubscription(document.signature, subscription))

        if (subscriptionsData.isEmpty()) {
            log.info("There is no subscription definitions for signature: " + document.signature.name)
        } else {
            return PdfGenerator.addImageToPdfContent(document.signature.templatePath, document.content.content, subscriptionsData)
        }

        return document.content.content
    }

    byte[] getDocumentWithSubscriptions(DocumentFile document, Set<Subscription> subscriptions) {
        Map<String,Object[]> subscriptionsData = new HashMap<String, Object[]>()

        Signature signature = document.signature

        subscriptionsData.putAll(attachSubscriptions(document.signature, subscriptions, Subscription.PersonRole.ACCEPTANT1))
        subscriptionsData.putAll(attachSubscriptions(document.signature, subscriptions, Subscription.PersonRole.ACCEPTANT2))
        subscriptionsData.putAll(attachSubscriptions(document.signature, subscriptions, Subscription.PersonRole.ACCEPTANT3))
        subscriptionsData.putAll(attachSubscriptions(document.signature, subscriptions, Subscription.PersonRole.ACCEPTANT4))
        subscriptionsData.putAll(attachSubscriptions(document.signature, subscriptions, Subscription.PersonRole.PH))

        if (subscriptionsData.isEmpty()) {
            log.info("There is no subscription definitions for signature: " + signature.name)
        } else {
            return PdfGenerator.addImageToPdfContent(signature.templatePath, document.content.content, subscriptionsData)
        }

        return document.content.content
    }

    void addSubscriptionsToRepresentativeDocuments(Process process) {
        process.documents.findAll{it.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)}.each { document ->
            updateDataUmowyOnDocument(document, process)

            Subscription representativeSubscription = getRepresentativeSubscriptionFromDocument(document)

            if(representativeSubscription) {
                byte[] newContent = getDocumentWithSubscription(document, representativeSubscription)
                document.content.content = newContent
                document.content.discard()
            } else {
                log.error(String.format("Cannot find subscription for representative with id %s", representativeId))
            }
        }
    }

    private Subscription getRepresentativeSubscriptionFromDocument(DocumentFile document) {
        String representativeId = documentService.getRepresentativeIdFromDocumentName(document.name)
        Representative representative = document.process.representatives.find{representativeId?.equals(it.id.toString())}

        return subscriptionService.getRepresentativeSubscription(document.process, representative)
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
				log.error "Couldn't create black faksymile image from URI for " + sig.templatePath
			}

        }

        return PdfGenerator.addImageToPdfContent(sig.templatePath, documentContent, subscriptionsMap)
    }

    private Map attachSubscriptions(Signature signature, Set<Subscription> subscriptions, Subscription.PersonRole personRole) {
        Subscription subscription = subscriptions.find { it.personRole == personRole }

        if (subscription?.content == null) {
            log.error(String.format("Looking for subscription for role %s and cannot find subscription content for " +
                    "signature %s and subscription with id %s and role %s", personRole.name(), signature.name,
                    subscription?.id, subscription?.personRole?.name()))
            return [:]
        }

        return attachSubscription(signature, subscription)
    }

    private Map attachSubscription(Signature signature, Subscription subscription) {
        Map result = [:]

        Set<SubscriptionDefinition> definitions = signature.subscriptionDefinitions.findAll { it.role == subscription?.personRole && it?.subscriptionPageNumber > -1}

        if (definitions.isEmpty()) {
            log.error(String.format("Cannot find definitions for signature %s and subscription role %s", signature.name, subscription?.personRole?.name()))
            return result
        }

        definitions.each{
            BufferedImage img = SignatureToImage.convertDataToImage(subscription.content)
            result.put("subscriber_"+it.id, [img, it.subscriptionPageNumber, it.subscriptionX, it.subscriptionY, it.scaleX, it.scaleY] as Object[])
        }

        return result
    }

    void createMergedBeneficiaryPDF(Process process) {
        List<DocumentFile> documentsToMerge = process.documents.findAll{it.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)}
        documentsToMerge.add(process.documents.find{it.name.startsWith("Formularz_PABR")})

        log.info(String.format("Found %s documents in process %s for merging into single beneficiary document.",
                documentsToMerge.size(), process.id))

        if(documentsToMerge.size() == 0) {
            return
        }

        Document mergedDocument = new Document()
        try {
            String outputFilename = getBeneficiaryPDFfilepath(process)

            PdfCopy copy = new PdfCopy(mergedDocument, new FileOutputStream(outputFilename))

            mergedDocument.open()

            log.info(String.format("Trying to create merged beneficiary document in location %s for process %s", outputFilename, process.id))

            PdfReader reader
            int numberOfPages

            documentsToMerge.each { document ->
                reader = new PdfReader(document?.content?.content)
                numberOfPages = reader.getNumberOfPages()

                for (int pageNo = 0; pageNo < numberOfPages; ) {
                    copy.addPage(copy.getImportedPage(reader, ++pageNo));
                }
            }
        } catch (IOException e) {
            throw e
        } finally {
            mergedDocument?.close()
        }
    }

    private String getBeneficiaryPDFfilepath(Process process) {
        String nip = process.getData("nip")
        String dataUmowy = DateUtils.getFormattedDate(DateUtils.parseWithTimezone(process.getData("dataUmowy")), "yyyyMMdd")

        return appParametersService.getBeneficiaryPath(String.format("%s-%s.pdf", nip, dataUmowy))
    }

    def cleanAgrementDateContent(DocumentContent dc){
        //pola zapleniane na podstawie 'dataUmowy'
        def fieldsToClean = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        PdfGenerator.updateValuesContent(dc, fieldsToClean, "")
    }

    def updateDataUmowyOnDocument(DocumentFile document, Process process){
        String dataUmowy = DateUtils.getFormattedDate(DateUtils.parseWithTimezone(process.getData("dataUmowy")), DateUtils.YYYY_MM_DD)

        List candidatesForUpdate = ['dataUmowy', 'wydrukGrafikiData', 'dzialaniaMatematyczneData',
                'pierwszaSesjaData', 'systemKasowyData', 'weryfikacjaPINData', 'czasObslugiData'];
        ProcessData processData
        List fieldsToUpdate = []

        candidatesForUpdate.each { field ->
            processData = process.processData?.find { data -> data.name.equals(field)}
            if(processData && !EMPTY_VALUES.contains(processData.value)){
                fieldsToUpdate.add(field)
            }
        }

        document.setContent(PdfGenerator.updateValuesContent(document.content, fieldsToUpdate, dataUmowy))
        document.save(flush: true)
    }

    void reloadDataAndSubscriptionsOnDocuments(Process process, def calculator) {
        log.info("Renewing documents")
        documentService.getSavedDocumentsInProcess(process, calculator)

        byte[] newContent

        process.documents.each { DocumentFile document ->
            if(document.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)) {
                newContent = getDocumentWithSubscription(document, getRepresentativeSubscriptionFromDocument(document))
            } else {
                newContent = getDocumentWithSubscriptions(document, process.subscriptions)
            }

            document.content.content = newContent
            document.content.discard()
        }
        log.info("Subscriptions and documents data renewed.")
    }
}
