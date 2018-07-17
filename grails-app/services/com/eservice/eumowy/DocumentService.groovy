package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.pdfmapper.PEPdeclarationMapper
import com.google.common.collect.Lists
import com.lowagie.text.pdf.PdfReader
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import pdfgenerator.PdfGenerator
import pdfgenerator.PdfGenerator.FontType

import static com.eservice.eumowy.ActivityHelper.DODATKOWY_POS
import static com.eservice.eumowy.ActivityHelper.DODATKOWY_PUNKT
import static com.eservice.eumowy.ActivityHelper.NOWA_UMOWA
import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY
import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_PLATNICZEJ
import static com.eservice.eumowy.ActivityHelper.hasAtLeastOne
import static com.eservice.eumowy.SignatureDetail.SignaturePurpose.ADDITIONAL_POINTS

class DocumentService {

    private static Logger LOG = Logger.getLogger(DocumentService.class)

    def mapperService
    def appParametersService

    private static final int POSES_COUNT_ON_RENT_REDUCTION = 20
    private static final int POINTS_COUNT_ON_DOCUMENT = 30

    def download(def id) {

        log.info "Download document id=[${id}]"

        DocumentFile file =  DocumentFile.get(id)

        if (!file) {
            throw new NoSuchElementException()
        }

        file
    }

    boolean isDocumentExistsInProcess(DocumentFile documentFile, Process process) {
        return DocumentFile.findByNameAndProcess(documentFile?.name, process) != null
    }

    public String getRepresentativeIdFromDocumentName(String name) {
        return name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf('.pdf'))
    }

    public Set<DocumentFile> getSavedDocumentsInProcess(Process processInstance, def calc) {
        Set<DocumentFile> documents = []
        Map dataFromProcess = mapperService.mapOnlyProcessData(processInstance, calc)

        dataFromProcess.each { key, value ->
            LOG.info "Mapping < " + key + " : " + value + " >"
        }

        Set<DocumentFile> rentReductionDocuments = getRentReductionDocuments(processInstance, dataFromProcess)
        documents.addAll(rentReductionDocuments)

        Set<DocumentFile> documentsWithoutPurpose = getDocumentsWithoutPurpose(processInstance, dataFromProcess)
        documents.addAll(documentsWithoutPurpose)

        Set<DocumentFile> pointDocuments = getPointDocuments(processInstance, dataFromProcess)
        documents.addAll(pointDocuments)

        Set<DocumentFile> posExchangeDocuments = getPosExchangeDocuments(processInstance)
        documents.addAll(posExchangeDocuments)

//        Set<DocumentFile> pointsDocuments = getAdditionalPointsDocuments(processInstance)
//        documents.addAll(pointsDocuments)

        addNewDocumentsToProcess(documents, processInstance)

        removeObsoleteDocuments(processInstance)

        processInstance.save(flush: true)

        return documents
    }

    private Set<DocumentFile> getRentReductionDocuments(Process processInstance, Map dataFromProcess) {
        Signature signature = processInstance.signatures.find{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.RENT_REDUCTION)}
        Set<DocumentFile> documents = []

        if(!signature) return documents

        List<PosData> chosenPoses = processInstance.getChosenPoses()

        if(chosenPoses.size() > POSES_COUNT_ON_RENT_REDUCTION) {
            log.info(String.format("Pos exchange count is larger than %s for process %s", POSES_COUNT_ON_RENT_REDUCTION, processInstance.id))
        }

        Lists.partition(chosenPoses, POSES_COUNT_ON_RENT_REDUCTION).eachWithIndex { List<PosData> poses, int i ->
            Map posData = mapperService.mapPosData(poses)
            String name = i == 0 ? signature.templatePath : i + "_" + signature.templatePath
            String clientName = i == 0 ? signature.filename : i + "_" + signature.filename

            posData.putAll(dataFromProcess)

            documents.add(getDocumentFile(processInstance, signature, posData, name, clientName))
        }

        return documents
    }

    private Set<DocumentFile> getAdditionalPointsDocuments(Process process) {
        List<PointData> localPoints = process.points.findAll {it.czyLokalny || it.hasLocalPoses() || it.czyWybranyWymianaUmowy}.toList()

        if (localPoints.size() <= POINTS_COUNT_ON_DOCUMENT || !hasAtLeastOne(process, [NOWA_UMOWA, WYMIANA_UMOWY_PLATNICZEJ])) {
            return []
        }

        log.info(String.format("Points count is larger than %s for process %s", POINTS_COUNT_ON_DOCUMENT, process.id))

        Signature signature = Signature.findAll().find {it.hasPurpose(ADDITIONAL_POINTS)}
        String[] listNumbers = ["a", "b", "c", "d", "e", "f"]

        int localPointsCount = localPoints.size()
        List<PointData> points = localPoints
                .sort { it.nazwa }
                .subList(POINTS_COUNT_ON_DOCUMENT, localPointsCount)

        Set<DocumentFile> documents = []

        Lists.partition(points, POINTS_COUNT_ON_DOCUMENT).eachWithIndex { List<PointData> data, int i ->
            Map pointData = mapperService.mapPointData(points)

            pointData.put("numerListy", [listNumbers[i]] as String[])

            String name = String.format("%s_%s", listNumbers[i], signature.templatePath)
            String clientName = signature.filename.split('\\.')[0] + listNumbers[i] + ".pdf"

            documents.add(getDocumentFile(process, signature, pointData, name, clientName))
        }

        return documents
    }

    private Set<DocumentFile> getDocumentsWithoutPurpose(Process processInstance, Map dataFromProcess) {
        Set signaturesWithoutPurpose = processInstance.signatures.findAll{ sig -> !sig.hasDetails()}
        Set<DocumentFile> documents = []

        if(signaturesWithoutPurpose.size() == 0) return documents

        signaturesWithoutPurpose.each { Signature signature ->
            log.info(String.format("New single document from signature %s.", signature.name))
            DocumentFile documentFile = getDocumentFile(processInstance, signature, dataFromProcess)

            if(documentFile) {
                documents.add(documentFile)
            }
        }

        return documents
    }

    private Set<DocumentFile> getPosExchangeDocuments(Process processInstance) {
        Signature posSignature = processInstance.signatures.find{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POS)}
        Set<DocumentFile> documents = []

        if(!posSignature) return documents


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

            log.info(String.format("New Pos Exchange document %s from signature %s.", documentClientName, posSignature.name))

            DocumentFile documentFile = getDocumentFile(processInstance, posSignature, data, documentName, documentClientName)
            if(documentFile) {
                documents.add(documentFile)
            }
        }

        return documents
    }

    private Set<DocumentFile> getPointDocuments(Process processInstance, Map dataFromProcess) {
        Set<DocumentFile> documents = []
        Set<Signature> pointSignatures = processInstance.signatures.findAll{ sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POINT)}

        if(pointSignatures.size() == 0) return documents

        if (ActivityHelper.containsAll(processInstance, Lists.newArrayList(DODATKOWY_POS, DODATKOWY_PUNKT))) {
            pointSignatures.remove(pointSignatures.find {sig -> sig.name.equals("virtualPos")})
        }

        processInstance.points.each { PointData point ->
            if ((point?.isLocal()) || point?.hasLocalPoses()) {
                Map dataFromPoint = mapperService.mapOnlyPointData(point)

                final Map<String, String> data = new HashMap<String, String>();
                data.putAll(dataFromProcess)
                data.putAll(dataFromPoint)

                pointSignatures.each { Signature signature ->
                    String path = signature.templatePath
                    String begin = path.substring(0, path.lastIndexOf('.'));
                    String end = path.substring(path.lastIndexOf('.'));
                    String documentName = begin + "_" + point.id + end

                    String pathClient = signature.filename
                    String beginClient = pathClient.substring(0, pathClient.lastIndexOf('.'));
                    String endClient = pathClient.substring(pathClient.lastIndexOf('.'));
                    String documentClientName = beginClient + "_" + point.id + endClient

                    log.info(String.format("New Point document %s from signature %s.", documentClientName, signature.name))

                    DocumentFile documentFile = getDocumentFile(processInstance, signature, data, documentName, documentClientName)
                    if(documentFile) {
                        documents.add(documentFile)
                    }
                }
            }
        }

        return documents
    }

    private DocumentFile getDocumentFile(Process processInstance, Signature signature, Map data) {
        return getDocumentFile(processInstance, signature, data, signature.templatePath, signature.filename)
    }

    private DocumentFile getDocumentFile(Process processInstance, Signature sig, Map data, String documentName, String documentClientName) {
        byte[] documentData = getDocumentContent(sig.id, data)

        if(!documentData) {
            return null
        }

        String nameWithNip = String.format("NIP %s %s", processInstance.client.nip, documentClientName)
        DocumentFile documentFile = DocumentFile.findByNameAndProcess(nameWithNip, processInstance)

        if (!documentFile) {
            documentFile = DocumentFile.findByNameAndProcess(documentName, processInstance) //compatibility
        }

        if (documentFile) {
            log.info(String.format("Updating existing document file %s", documentFile.id))
            documentFile.content.setContent(documentData)
            documentFile.lastUpdated = new Date()
            documentFile.save(flush: true)
        } else {
            documentFile = new DocumentFile(name: nameWithNip, clientName: nameWithNip, dateCreated: new Date(),
                    lastUpdated: new Date(), pagesCount: getDocumentPageCount(documentData), signature: sig)
            documentFile.setContent(new DocumentContent(content: documentData))
            documentFile.save(flush: true)

            log.info(String.format("New document file created %s for process %s", documentFile.id, processInstance.id))
        }

        return documentFile
    }

    byte[] getDocumentContent(Long sigId, Map<String,String[]> panelData) {
        String subscriptionsPath = appParametersService.getSubscriptionsPath()
        Signature signature = Signature.findById(sigId)

        Map<String,String[]> dataMap = new HashMap<String, String[]>()

        if (!signature.templatePath){
            log.debug(String.format("Signature %s is virtual signature. Skipping...", signature.name))
            return
        }

        signature.subscriptionDefinitions.findAll { (it.role == Subscription.PersonRole.ZARZAD1 || it.role == Subscription.PersonRole.ZARZAD2) && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1}
                .eachWithIndex{ SubscriptionDefinition it, int i ->
            dataMap.put(it.role.name() + i, [new File(subscriptionsPath + File.separator + it.fileName).toURI().toURL(), "", "signature", it.subscriptionPageNumber.toString(), (it.subscriptionX).toString(), it.subscriptionY.toString(), it.scaleX, it.scaleY] as String[])
        }

        if (panelData) {
            dataMap.putAll(panelData)
        }

        String pdfTemplatePath = appParametersService.getPdfTemplatePath(signature.templatePath)

        return PdfGenerator.generatePdfContentFromURI(pdfTemplatePath, dataMap, FontType.ARIAL, appParametersService.getFontUri())
    }

    int getDocumentPageCount(byte[] content) {
        int numberOfPages = 0

        try {
            PdfReader reader = new PdfReader(content)
            numberOfPages = reader.getNumberOfPages()
            reader.close()
        } catch (Exception e) {
            log.warn "getDocumentPageCount - Error while loading PDF file from byte array: " + e
        }

        return numberOfPages
    }

    private void addNewDocumentsToProcess(Set<DocumentFile> documents, Process process) {
        documents.each { DocumentFile file ->
            if(file != null && !isDocumentExistsInProcess(file, process)) {
                process.addToDocuments(file)
            }
        }
    }

    int getPreviewDocumentsPageCount(Set<DocumentFile> documents) {
        int totalPageCount = 0

        documents.each { DocumentFile document ->
            totalPageCount += (document.signature?.showOnPreview ? document.pagesCount : 0)
        }

        return totalPageCount
    }

    public void removeObsoleteDocuments(Process processInstance) {
        Set<DocumentFile> obsoleteDocuments = getObsoletePointDocuments(processInstance)
        obsoleteDocuments.addAll(getObsoletePosExchangeDocuments(processInstance))
        obsoleteDocuments.addAll(getObsoleteRepresentativesDocuments(processInstance))
        obsoleteDocuments.addAll(getObsoleteRentReducutionDocuments(processInstance))
//        obsoleteDocuments.addAll(getObsoleteAdditionalPointsDocuments(processInstance))

        obsoleteDocuments.each { DocumentFile file ->
            log.info(String.format("Removing obsolete document %s with id %s from process %s", file.name, file.id, processInstance.id))
            processInstance.removeFromDocuments(file)
        }
    }

    private Set<DocumentFile> getObsoletePointDocuments(Process processInstance) {
        Set<DocumentFile> documents = []

        processInstance.documents.findAll { it.signature.hasPurpose(SignatureDetail.SignaturePurpose.POINT) }.each {
            long idFromName = fetchPointIdFromName(it.clientName)

            if (idFromName != -1) {
                boolean toDelete = true
                for (PointData pd : processInstance.points) {
                    if (idFromName == pd.id) {
                        toDelete = false
                        break;
                    }
                }

                if (toDelete) {
                    documents.add(it)
                }
            }
        }

        return documents
    }

    private Set<DocumentFile> getObsoletePosExchangeDocuments(Process processInstance) {
        Set<DocumentFile> documents = []

        processInstance.documents.findAll { it.signature.hasPurpose(SignatureDetail.SignaturePurpose.POS) }.each {
            long idFromName = fetchPosExchangeIdFromName(it.clientName)

            if (idFromName != -1) {
                boolean toDelete = true
                for (PosExchange pe : processInstance.posExchanges) {
                    if (idFromName == pe.id) {
                        toDelete = false
                        break
                    }
                }

                if (toDelete) {
                    documents.add(it)
                }
            }
        }

        return documents
    }

    private Set<DocumentFile> getObsoleteRepresentativesDocuments(Process processInstance) {
        Set<DocumentFile> documents = []

        processInstance.documents.findAll { it.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE) }.each {
            String representativeId = getRepresentativeIdFromDocumentName(it.name)

            boolean toDelete = true
            for (Representative representative : processInstance.representatives) {
                if (representativeId.equals(representative.id.toString())) {
                    toDelete = false
                    break
                }
            }

            if (toDelete) {
                documents.add(it)
            }
        }

        return documents
    }

    private Set<DocumentFile> getObsoleteRentReducutionDocuments(Process processInstance) {
        Set<DocumentFile> documents = processInstance.documents.findAll { it.signature.hasPurpose(SignatureDetail.SignaturePurpose.RENT_REDUCTION) }
        Set<DocumentFile> obsoleteDocuments = []

        if(!documents) return obsoleteDocuments

        List<PosData> chosenPoses = processInstance.getChosenPoses()
        int maxDocumentOrdinalNumber = Math.floor((chosenPoses.size() - 1) / POSES_COUNT_ON_RENT_REDUCTION) as int

        //if chosePoses.size() > 100 then this will fail
        documents.findAll {StringUtils.isNumeric(it.name[0])}.each { DocumentFile file ->
            int ordinal = file.name[0] as int

            if(ordinal > maxDocumentOrdinalNumber) obsoleteDocuments.add(file)
        }

        return obsoleteDocuments
    }

    private Set<DocumentFile> getObsoleteAdditionalPointsDocuments(Process process) {
        Set<DocumentFile> documents = process.documents.findAll { it.signature.hasPurpose(ADDITIONAL_POINTS) }
        Set<DocumentFile> obsoleteDocuments = []
        int localPointsCount = process.points.findAll {it.czyLokalny || it.hasLocalPoses() || it.czyWybranyWymianaUmowy}.size()

        if (!documents) {
            return obsoleteDocuments
        } else if (localPointsCount <= POINTS_COUNT_ON_DOCUMENT) {
            return documents
        }

        int listsCount = Math.ceil((localPointsCount - POINTS_COUNT_ON_DOCUMENT) / POINTS_COUNT_ON_DOCUMENT) as int

        documents.eachWithIndex{ DocumentFile doc, int i ->
            if (i + 1 > listsCount) {
                obsoleteDocuments.add(doc)
            }
        }

        return obsoleteDocuments
    }

    private long fetchPointIdFromName(String name){
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_')+1, name.lastIndexOf(".pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID Punktu z nazwy: ' + name)
        }

        return result;
    }

    private long fetchPosExchangeIdFromName(String name){
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_')+1, name.lastIndexOf("-p.pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID PosExchange z nazwy: ' + name)
        }

        return result;
    }
}