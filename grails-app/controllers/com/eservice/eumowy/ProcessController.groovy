package com.eservice.eumowy

import com.eservice.eum.ws.xml.Result
import com.eservice.eumowy.util.DateUtils
import org.springframework.security.access.annotation.Secured
import pdfgenerator.PdfGenerator

import java.nio.charset.Charset

class ProcessController {
    def attachmentService
    def documentService
    def emailService
	def appParametersService
    def springSecurityService
    def webServiceClient
    def processService
    def pdfService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def invalidateCaches(){
        processService.invalidateCaches()
        render(text: '')
    }

    @Secured(['EUM_ZRD'])
    def list() {
        params.remove('_action_list')

        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        if(params.filterStatus == null){
            params.filterStatus = Process.ProcessStatus.WAITING.name()
        }

        def processes = processService.searchProcessByFilters(params)
        [
            filterStatus: params.filterStatus,
            filterObserved:params.filterObserved,
            filterNip:params.filterNip,
            filterPhNo:params.filterPhNo,
            filterDateFrom: params.filterDateFrom?params.filterDateFrom: DateUtils.getFormattedDate(DateUtils.addDays(new Date(), -30), DateUtils.DD_MM_YYYY),
            filterDateTo: params.filterDateTo?params.filterDateTo: DateUtils.getFormattedDate(new Date(), DateUtils.DD_MM_YYYY),
            sort:params.sort,
            order:params.order,
            max:params.max,
            offset:params.offset,
            processInstanceList: processes.searchResults,
            processInstanceTotal: processes.searchResultSize
        ]
    }

    //---------------------------------
    // PH AVAILABLE
    //---------------------------------

    @Secured(['EUM_ZRD'])
    def edit(Long id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list", params: params )
            return
        }

        def result = [
            processInstance: processInstance,
            filterStatus: params.filterStatus,
            filterObserved:params.filterObserved,
            filterNip:params.filterNip,
            filterPhNo:params.filterPhNo,
            filterDateFrom: params.filterDateFrom?params.filterDateFrom: DateUtils.getFormattedDate(DateUtils.addDays(new Date(), -30), DateUtils.DD_MM_YYYY),
            filterDateTo: params.filterDateTo?params.filterDateTo: DateUtils.getFormattedDate(new Date(), DateUtils.DD_MM_YYYY)
        ]

        if (params.sort){
            result.put('sort', params.sort)
        }
        if (params.order){
            result.put('order', params.order)
        }
        if (params.offset){
            result.put('offset', params.offset)
        }

        result
    }

    //---------------------------------
    // ADMIN AVAILABLE
    //---------------------------------

    @Secured(['EUM_ZRD'])
    def show(String id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list", params: params)
            return
        }

        boolean isRepresentativesChangedManually = processInstance.processData.find {"isRepresentativesChangedManually".equals(it.name)}?.value

        if(isRepresentativesChangedManually) {
            flash.message = message(code: 'representatives.changed.manually')
        }

        def result = [
            processInstance: processInstance
        ]
        result
    }

    @Secured(['EUM_ZRD'])
    def reject() {
        def processInstance = Process.get(params.id)
        params.remove('_action_reject')
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), params.id])
            redirect(action: "list", params: params)
            return
        }

        if (!params.notesFromZrd){
            flash.error = message(code: 'notes.empty')
            redirect(action: "show", params: params)
            return
        }

        if (processInstance.status in [Process.ProcessStatus.ACCEPTED,Process.ProcessStatus.REJECTED]){
            flash.error = "Nie można odrzucić zakończonego procesu"
            redirect(action: "show", params: params)
            return
        }

        processInstance.status = Process.ProcessStatus.REJECTED
        processInstance.updateDate = new Date()
        processInstance.observed = (params.observed == "on")
        processInstance.notesFromZrd = params.notesFromZrd

		/* Delete subscriptions */
		processInstance.subscriptions?.clear()
        processInstance.save(flush: true, validate: false)
		
        flash.message = message(code: 'default.rejected.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])

        def mailBodyParams = [merchantName: processInstance.client.name, merchantNip: processInstance.client.nip, activities: processService.getActivities(processInstance), rejectReason: params.notesFromZrd]

        if(!emailService.sendDocumentsRejected(processInstance.phEmail, processInstance.client.name, processInstance.client.nip, mailBodyParams)){
            flash.error = "Błąd podczas wysyłania maila na adres ${processInstance.phEmail}"
            redirect(action: "show", params: params)
            return
        }

        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def correction() {
        def processInstance = Process.get(params.id)
        params.remove('_action_correction')
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), params.id])
            redirect(action: "list", params: params)
            return
        }

        processInstance.status = Process.ProcessStatus.CORRECTION
        processInstance.updateDate = new Date()
        processInstance.observed = (params.observed == "on")

        processInstance.save(flush: true, validate: false)

        flash.message = message(code: 'default.correction.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])

        def mailBodyParams = [merchantName: processInstance.client.name, merchantNip: processInstance.client.nip,
                              activities: processService.getActivities(processInstance), rejectReason: message(code: 'correction.email.body')]

        if(!emailService.sendDocumentsRejected(processInstance.phEmail, processInstance.client.name, processInstance.client.nip, mailBodyParams)){
            flash.error = "Błąd podczas wysyłania maila na adres ${processInstance.phEmail}"
            redirect(action: "show", params: params)
            return
        }

        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def accept() {
        Process processInstance = Process.get(params.id)
        params.remove('_action_accept')

        log.info(params)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list", params: params)
            return
        }

        if (!params.notesFromZrd){
            flash.error = message(code: 'notes.empty')
            redirect(action: "show", params: params)
            return
        }

        def isWaitingProcess = processInstance.status in [Process.ProcessStatus.WAITING,Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION]
        if (!isWaitingProcess){
            flash.error = "Można zaakceptować jedynie proces ze statusem - Oczekujący / Oczekujący na podpis"
            redirect(action: "show", params: params)
            return
        } else if (processInstance.status == Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION){
            if (params.dataUmowy) {
                if (!isDate(params.dataUmowy)){
                    flash.error = "Można zaakceptować jedynie proces ze statusem Oczekujący na podpis i podaną poprawną datą zawarcia umowy"
                    redirect(action: "show", params: params)
                    return
                }
            } else {
                flash.error = "Można zaakceptować jedynie proces ze statusem Oczekujący na podpis i podaną datą zawarcia umowy"
                redirect(action: "show", params: params)
                return
            }
        }

        def hasDocuments = processInstance.documents?.size() > 0
        if (!hasDocuments){
            flash.error = "Nie można zaakceptować procesu bez dokumentów"
            redirect(action: "show", params: params)
            return
        }

        if(processService.hasNowaUmowa(processInstance)) {
            pdfService.createMergedBeneficiaryPDF(processInstance)
        }

        if (processInstance.status == Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION){
            log.info('Zapisuje do bazy: dataUmowy -> ' + params.dataUmowy)
            ProcessData pd = processInstance.processData.find{ProcessData pd -> "dataUmowy".equals(pd.name)}
            if (pd){
                pd.value = DateUtils.formatWithTimezoneFromStr(params.dataUmowy)
                pd.save(flush:true);
            } else {
                ProcessData newPd = new ProcessData(name: "dataUmowy", value:"${DateUtils.formatWithTimezoneFromStr(params.dataUmowy)}");
                newPd.process = processInstance
                processInstance.addToProcessData(newPd)
                newPd.save(flush:true)
            }
        }

        log.info("wywolanie synchronizacji dla procesu [${processInstance.id}] oraz auwId [${springSecurityService.principal.auwId}]")
        Result result = webServiceClient.acceptUmowa(processInstance.id, springSecurityService.principal.auwId)
        if (result.wynik < 0 || !result.wynikString){
            flash.error = result.wynikString
            log.error("Wystąpił błąd poczas synchronizacji procesu. WynikString: " + result.wynikString)
            log.error(result.stackString)
            redirect(action: "show", params: params)
            return
        }

        log.info("Wynik synchronizacji procesu [" + processInstance.id + "] : " + result.wynikString)

        processInstance.status = Process.ProcessStatus.ACCEPTED;
        processInstance.acceptanceDate = new Date()
        processInstance.observed = (params.observed == "on")
        processInstance.notesFromZrd = params.notesFromZrd

        processInstance.save(validate: true)
        flash.message = message(code: 'default.accepted.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])


        if(!emailService.sendProcessAcceptedMails(processInstance, params.notesFromZrd)){
            flash.error = "Błąd podczas wysyłania wiadomości email."
            redirect(action: "show", params: params)
            return
        }

        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def reloadDocuments() {
        Process processInstance = Process.get(params.id)

        log.info("Trying to reload documents for process " + params.id + " with status " + processInstance.status)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])
            redirect(action: "list", params: params)
            return
        }

        Boolean isWaitingProcess = processInstance.status.equals(Process.ProcessStatus.WAITING)
        if (!isWaitingProcess){
            flash.error = g.message(code: 'renewSubscriptions.wrong.status')
            redirect(action: "show", params: params)
            return
        }

        Integer requiredNumberOfSubscriptions = processService.requiredNumberOfSubscriptions(processInstance)
        Integer savedSubscriptionsCount = processService.savedSubscriptionsCount(processInstance)
        Boolean hasRequiredNumberOfSubscriptions = (requiredNumberOfSubscriptions == savedSubscriptionsCount)
        if(!hasRequiredNumberOfSubscriptions) {
            log.error("Niewystarczajaca liczba podpisow. Wymaganych: " + requiredNumberOfSubscriptions + " Zapisanych w bazie: " + savedSubscriptionsCount)
            flash.error = g.message(code: 'renewSubscriptions.subscriptions.deficit', args: [requiredNumberOfSubscriptions, savedSubscriptionsCount])
            redirect(action: "show", params: params)
            return
        }

        def calculator = processService.calculatorForProcess(processInstance)
        pdfService.reloadDataAndSubscriptionsOnDocuments(processInstance, calculator)

        Map mailParametersForElectronicalVersion = processService.createMailParametersForElectronicalVersion(processInstance)
        String recipient = mailParametersForElectronicalVersion.recipient
        String phEmail = processInstance.phEmail

        if (recipient) {
            List<String> recipients = []
            recipients.add(recipient)

            if (phEmail) {
                recipients.add(phEmail)
            }

            Boolean isNewAgreement = processService.isProcessHasActivity(processInstance, "nowaUmowa")
            List<DocumentFile> documents = processInstance.documents?.findAll{it.signature?.sendToClient}
            if (isNewAgreement){
                emailService.sendDocumentsElectronicalVersion(recipients, documents, mailParametersForElectronicalVersion)
            } else {
                emailService.sendDocumentsNotNewAggrementElectronicalVersion(recipients, documents, mailParametersForElectronicalVersion)
            }
        } else {
            emailService.sendDocumentsAcceptedToPostSend(processInstance.documents, mailParametersForElectronicalVersion)
        }

        log.info("Reloading documents successful.")
        flash.message = g.message(code: 'renewSubscriptions.success')
        redirect(action: "show", params: params)
    }

    @Secured(['EUM_ZRD'])
    def saveNotes() {
        Process processInstance = Process.findById(params.id)
        params.remove('_action_saveNotes')

        log.info(String.format("Trying to save notes for process %s", processInstance.id))

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])
            redirect(action: "list", params: params)
            return
        }

        processInstance.notesFromZrd = params.notesFromZrd

        if(!processInstance.save(flush: true)) {
            flash.error = processInstance.errors.getFieldError().defaultMessage
            redirect(action: "show", params: params)
            return
        }

        log.info(String.format("Notes for process %s saved successfully", processInstance.id))

        flash.message = g.message(code: 'notes.saved.successfully')
        redirect(action: "show", params: params)
    }

    @Secured(['EUM_ZRD'])
    def resendEmail() {
        Process processInstance = Process.get(params.id)

        log.info("Trying to resend emails for process " + params.id + " with status " + processInstance.status)

        if (!processInstance) {
            flash.error = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])
            redirect(action: "list", params: params)
            return
        }

        Boolean isProcessWaitingForPaperVersion = processInstance.status.equals(Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION)

        if(!isProcessWaitingForPaperVersion) {
            flash.error = g.message(code: 'resendEmails.wrong.status')
            redirect(action: "show", params: params)
            return
        }

        Map mailParametersForPaperVersion = processService.createMailParametersForPaperVersion(processInstance)
        List documents = processInstance.documents
        emailService.sendDocumentsPaperVersion(documents, mailParametersForPaperVersion)

        flash.message = g.message(code: 'resendEmails.success')
        redirect(action: "show", params: params)
    }

    //---------------------------------
    // REMOTE CALLS
    //---------------------------------

    def servletContext

    def showPdfByDocumentId(String id){
        log.info( "pdf document = " + id);
        def documentFile = DocumentFile.get(id);
		def fileName = "${id}_${documentFile.version}.pdf"
        def fileDir = appParametersService.getPdfPreviewPath(fileName)
        def fileUri = appParametersService.getPdfPreviewUri(fileName)
        def tmpRes = new File(fileDir)

        def tmpPdfFile = tmpRes
        tmpPdfFile.withOutputStream { s ->
            s << documentFile.content.content
        }

		render(template: '../forms/pdf/embedDocument', model:  [pdfDocument: fileUri]);
    }

    def downloadDoc(){
        log.info( "downloadDoc = " +  params.id);
        DocumentFile file = documentService.download(params.id)
		
        if(!(file?.content?.content)){
            redirect(action: "show")
        }

        String filename = URLEncoder.encode(file.name, "UTF-8").replaceAll("\\+", " ")

        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=\"${filename}\"")
        response.outputStream << PdfGenerator.getClosedContent(file.content.content)
    }

    def downloadAttachment(){
        log.info( "downloadAttachment = " +  params.id);
        AttachmentFile file = attachmentService.download( params.id, request)

        if(!(file?.file?.content)){
            redirect(action: "show")
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=\"${URLEncoder.encode(file.name, "UTF-8")}\"")
        response.outputStream << file.file.content
    }

    private boolean isDate(def dateStr){
        return !"".equals(DateUtils.formatWithTimezoneFromStr(dateStr))
    }
}
