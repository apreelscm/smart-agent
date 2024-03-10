package com.eservice.eumowy

import com.eservice.eumowy.enums.options.LegalForm
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.lowagie.text.pdf.PdfReader
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFMergerUtility
import org.springframework.context.i18n.LocaleContextHolder
import pdfgenerator.PdfGenerator
import pdfgenerator.PdfGenerator.FontType

import static com.eservice.eumowy.ActivityHelper.DODATKOWY_POS
import static com.eservice.eumowy.ActivityHelper.DODATKOWY_PUNKT
import static com.eservice.eumowy.ActivityHelper.NOWA_UMOWA
import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_PLATNICZEJ
import static com.eservice.eumowy.ActivityHelper.hasAtLeastOne
import static com.eservice.eumowy.ActivityHelper.isNewAgreement
import static com.eservice.eumowy.SignatureDetail.SignaturePurpose.ADDITIONAL_POINTS

class DocumentService {

    private static Logger LOG = Logger.getLogger(DocumentService.class)

    def mapperService
    def appParametersService
    def messageSource

    private static final int POSES_COUNT_ON_RENT_REDUCTION = 20
    private static final String PABR_PEP_DOCUMENT_NAME_CONTAINS = "PABR+PEP"

    def download(def id) {

        log.info "Download document id=[${id}]"

        DocumentFile file = DocumentFile.get(id)

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
        boolean isRepOrBenDataChanged = processInstance.isAnyRepresentativeOrBeneficiaryDataChanged()
        Map dataFromProcess = mapperService.mapOnlyProcessData(processInstance, calc, isRepOrBenDataChanged)

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

        ProcessData legalFormType = processInstance.getProcessData("dzialalnoscForma")
        List<String> expectedOzwuFormTypes = Arrays.asList(LegalForm.PARTNERSHIP_COMPANY.name(), LegalForm.PERSON.name())

        if (!expectedOzwuFormTypes.contains(legalFormType?.value)) {
            documents.removeAll() { it -> it.signature.name.contains("OŻWU")}
        }

        if (isNewAgreement(processInstance)) {
            DocumentFile mergedFile = getMergedDocument(processInstance, documents)
            documents.addAll(mergedFile)
        } else { // PABR is always included in new agreement activity
            if (isRepOrBenDataChanged) {
                Signature pabrSignature = Signature.findByNameLikeAndActive("%${PABR_PEP_DOCUMENT_NAME_CONTAINS}%", true)
                DocumentFile pabrDocument = getDocumentFile(processInstance, pabrSignature, dataFromProcess)
                documents.add(pabrDocument)
            } else {
                documents.removeAll() { it -> it.signature.name.contains(PABR_PEP_DOCUMENT_NAME_CONTAINS)}
            }
        }

        addNewDocumentsToProcess(documents, processInstance)
        removeObsoleteDocuments(processInstance)

        processInstance.save(flush: true)

        return documents
    }

    private Set<DocumentFile> getRentReductionDocuments(Process processInstance, Map dataFromProcess) {
        Signature signature = processInstance.signatures.find { sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.RENT_REDUCTION) }
        Set<DocumentFile> documents = []

        if (!signature) return documents

        List<PosData> chosenPoses = processInstance.getChosenPoses()

        if (chosenPoses.size() > POSES_COUNT_ON_RENT_REDUCTION) {
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

    private Set<DocumentFile> getDocumentsWithoutPurpose(Process processInstance, Map dataFromProcess) {
        Set signaturesWithoutPurpose = processInstance.signatures.findAll {
            sig -> !sig.hasDetails() && sig.forPos != true && sig.forPoint != true
        }
        Set<DocumentFile> documents = []

        if (signaturesWithoutPurpose.size() == 0) return documents

        signaturesWithoutPurpose.each { Signature signature ->
            log.info(String.format("New single document from signature %s.", signature.name))
            DocumentFile documentFile = getDocumentFile(processInstance, signature, dataFromProcess)

            if (documentFile) {
                documents.add(documentFile)
            }
        }

        return documents
    }

    private Set<DocumentFile> getPosExchangeDocuments(Process processInstance) {
        Signature posSignature = processInstance.signatures.find { sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POS) }
        Set<DocumentFile> documents = []

        if (!posSignature) return documents

        processInstance.posExchanges.findAll { it.isChoosen }.each {
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
            if (documentFile) {
                documents.add(documentFile)
            }
        }

        return documents
    }

    private Set<DocumentFile> getPointDocuments(Process processInstance, Map dataFromProcess) {
        return getLocalPointDocuments(processInstance, dataFromProcess) +
                getNotLocalPointDocuments(processInstance, dataFromProcess)
    }

    private Set<DocumentFile> getLocalPointDocuments(Process processInstance, Map dataFromProcess) {
        Set<Signature> pointSignatures = processInstance.signatures.findAll {
            sig -> sig.hasPurpose(SignatureDetail.SignaturePurpose.POINT)
        }

        if (pointSignatures.size() == 0) return []

        Set<PointData> points = processInstance.localPoints.findAll {it?.isLocal() || it?.hasLocalPoses() }

        return getPointDocuments(processInstance, dataFromProcess, points, pointSignatures)
    }

    private Set<DocumentFile> getNotLocalPointDocuments(Process processInstance, Map dataFromProcess) {
        Set<Signature> virtualPointSignatures = processInstance.signatures.findAll() {
            sig -> sig.name == "virtualPoint" || sig.name == "virtualPos"
        }

        if (ActivityHelper.containsAll(processInstance, Lists.newArrayList(DODATKOWY_POS, DODATKOWY_PUNKT))) {
            virtualPointSignatures.remove(virtualPointSignatures.find { sig -> sig.name == "virtualPos" })
        }

        if (virtualPointSignatures.size() == 0) return []

        Set<PointData> points = processInstance.points.findAll { !it.local }

        return getPointDocuments(processInstance, dataFromProcess, points, virtualPointSignatures)
    }

    private Set<DocumentFile> getPointDocuments(Process processInstance, Map dataFromProcess,
                                                Set<PointData> points, Set<Signature> signatures) {
        Set<DocumentFile> documents = []

        if (signatures.size() == 0) return documents

        points.each { PointData point ->
            Map dataFromPoint = mapperService.mapOnlyPointData(point)

            final Map<String, String> data = new HashMap<String, String>();
            data.putAll(dataFromProcess)
            data.putAll(dataFromPoint)

            signatures.each { Signature signature ->
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
                if (documentFile) {
                    documents.add(documentFile)
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

        if (!documentData) {
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

    byte[] getDocumentContent(Long sigId, Map<String, String[]> panelData) {
        String subscriptionsPath = appParametersService.getSubscriptionsPath()
        Signature signature = Signature.findById(sigId)

        Map<String, String[]> dataMap = new HashMap<String, String[]>()

        if (!signature.templatePath) {
            log.debug(String.format("Signature %s is virtual signature. Skipping...", signature.name))
            return
        }

        signature.subscriptionDefinitions.findAll { (it.role == Subscription.PersonRole.ZARZAD1 || it.role == Subscription.PersonRole.ZARZAD2) && it.subscriptionPageNumber != null && it.subscriptionPageNumber > -1 }
            .eachWithIndex { SubscriptionDefinition it, int i ->
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
            if (file != null && !isDocumentExistsInProcess(file, process)) {
                process.addToDocuments(file)
            }
        }
    }

    private DocumentFile getMergedDocument(Process process, Set<DocumentFile> processDocuments) {
        String pdfTemplatePath = "/opt/eumowy/pdf_templates/";
        List<DocumentFile> documentsToMerge = processDocuments.findAll {
            it.signature.shouldBeMerged || it.signature.name.contains(PABR_PEP_DOCUMENT_NAME_CONTAINS)
        }?.sort(false) { a, b -> (a.signature.signatureOrder <=> b.signature.signatureOrder) }
        String documentName = messageSource.getMessage('document.merged.base.name' as String,
            [process.client?.nip, this._getUPZTDocumentModelName(documentsToMerge)] as Object[],
            LocaleContextHolder.locale as Locale)

        PDFMergerUtility pdm = new PDFMergerUtility()
        pdm.setDestinationFileName(pdfTemplatePath + documentName)
        PDDocument mergedDoc = new PDDocument()

        for (int i = 0; i < documentsToMerge?.size(); i++) {
            log.info(String.format("Merging document %s", documentsToMerge[i].name))
            ByteArrayInputStream bais = new ByteArrayInputStream(documentsToMerge[i].getContent().getContent())
            PDDocument document = PDDocument.load(bais)
            pdm.appendDocument(mergedDoc, document)
            document.close()
        }

        DocumentFile documentFile = DocumentFile.findByNameAndProcess(documentName, process)

        if (documentFile) {
            log.info(String.format("Updating existing document file %s", documentFile.id))
            documentFile.content.setContent(getBytesContent(mergedDoc))
            documentFile.lastUpdated = new Date()
            documentFile.save(flush: true)
        } else {
            documentFile = new DocumentFile(name: documentName, clientName: documentName, dateCreated: new Date(),
                lastUpdated: new Date(), pagesCount: mergedDoc.getNumberOfPages(), signature: 3)
            documentFile.setContent(new DocumentContent(content: getBytesContent(mergedDoc)))
            documentFile.save(flush: true)
            log.info(String.format("New document file created %s for process %s", documentFile.id, process.id))
            mergedDoc.close()
        }

        return documentFile
    }

    private static byte[] getBytesContent(PDDocument pdDocument) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            pdDocument.save(out);
            pdDocument.close();
        } catch (Exception ex) {
            LOG.error(ex);
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return out.toByteArray();
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

        obsoleteDocuments.each { DocumentFile file ->
            log.info(String.format("Removing obsolete document %s with id %s from process %s", file.name, file.id, processInstance.id))
            processInstance.removeFromDocuments(file)
        }
    }

    private Set<DocumentFile> getObsoletePointDocuments(Process processInstance) {
        Set<DocumentFile> documents = []

        processInstance.documents.findAll {
            it.signature.name == "virtualPoint" || it.signature.name == "virtualPos"
        }.each {
            long idFromName = fetchPointIdFromName(it.clientName)

            if (idFromName != -1) {
                boolean toDelete = true
                for (PointData pd : processInstance.points.findAll { !it.local }) {
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

        processInstance.documents.findAll {
            it.signature.hasPurpose(SignatureDetail.SignaturePurpose.POINT)
        }.each {
            long idFromName = fetchPointIdFromName(it.clientName)

            if (idFromName != -1) {
                boolean toDelete = true
                for (PointData pd : processInstance.localPoints) {
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

        if (!documents) return obsoleteDocuments

        List<PosData> chosenPoses = processInstance.getChosenPoses()
        int maxDocumentOrdinalNumber = Math.floor((chosenPoses.size() - 1) / POSES_COUNT_ON_RENT_REDUCTION) as int

        //if chosePoses.size() > 100 then this will fail
        documents.findAll { StringUtils.isNumeric(it.name[0]) }.each { DocumentFile file ->
            int ordinal = file.name[0] as int

            if (ordinal > maxDocumentOrdinalNumber) obsoleteDocuments.add(file)
        }

        return obsoleteDocuments
    }

    private long fetchPointIdFromName(String name) {
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf(".pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID Punktu z nazwy: ' + name)
        }

        return result;
    }

    private long fetchPosExchangeIdFromName(String name) {
        long result = -1l;

        try {
            result = Long.valueOf(name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf("-p.pdf")));
        } catch (Exception e) {
            log.info('Nie udalo sie pobrac ID PosExchange z nazwy: ' + name)
        }

        return result;
    }

    private String _getUPZTDocumentModelName(List<DocumentFile> documentFiles) {
        def upztFile = documentFiles?.find { it?.signature?.name?.contains("UPZT") }
        if (upztFile?.name != null) {
            def modelNameRegex = upztFile.name =~/\([model T\d*)]+\)/
            if (modelNameRegex.size() > 0) {
                return ' ' + modelNameRegex[0]
            }
        }
        return '';
    }
}
