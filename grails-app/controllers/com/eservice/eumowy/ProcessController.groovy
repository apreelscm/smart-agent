package com.eservice.eumowy
import com.eservice.eumowy.util.DateUtils
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility
import grails.plugins.springsecurity.Secured

class ProcessController {

    def messageSource
    def attachmentService
    def documentService
    def emailService
	def appParametersService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def list() {
        params.remove('_action_list')

        params.max = Math.min(params.max ? params.int('max') : 10, 100)

//        log.info("PSZKUP in Controller --> filterPhNo: " + params.filterPhNo + "; filterDateFrom: " + params.filterDateFrom + "; filterDateTo: " + params.filterDateTo)

        if(params.filterStatus == null){
            params.filterStatus = Process.ProcessStatus.REJECTED.name()
        }

        def processes = new ProcessService().searchProcessByFilters(params)

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
        processInstance.observed = (params.observed == "on")
        processInstance.notesFromZrd = params.notesFromZrd

		/* Delete subscriptions */
		processInstance.subscriptions?.clear()
        processInstance.save(flush: true, validate: false)
		
        flash.message = message(code: 'default.rejected.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])

        List<String> activities = new ArrayList<String>();
        processInstance.activities.each {a ->
            def key = 'activity.' + a.code + '.name'
            activities.add(messageSource.getMessage(key, [] as Object[], request.locale))
        }

        if(!emailService.sendDocumentsRejected(processInstance.phEmail, processInstance.client.name, processInstance.client.nip, params.notesFromZrd, activities)){
            flash.error = "Błąd podczas wysyłania maila na adres ${processInstance.phEmail}"
            redirect(action: "show", params: params)
            return
        }

        redirect(action: "list", params: params)
    }

    @Secured(['EUM_ZRD'])
    def accept() {
        def processInstance = Process.get(params.id)
        params.remove('_action_accept')

        println(params)
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
        }

        def hasDocuments = processInstance.documents?.size() > 0
        if (!hasDocuments){
            flash.error = "Nie można zaakceptować procesu bez dokumentów"
            redirect(action: "show", params: params)
            return
        }

        processInstance.status = Process.ProcessStatus.ACCEPTED;
        processInstance.observed = (params.observed == "on")
        processInstance.notesFromZrd = params.notesFromZrd

        processInstance.save(validate: true)
        flash.message = message(code: 'default.accepted.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])


        if(!emailService.sendDocumentsAccepted(processInstance.phEmail, null , processInstance.client.name)){
            flash.error = "Błąd podczas wysyłania maila na adres ${processInstance.phEmail}"
            redirect(action: "show", params: params)
            return
        }

        redirect(action: "list", params: params)
    }

    //---------------------------------
    // REMOTE CALLS
    //---------------------------------

    def servletContext

    def showPdfByDocumentId(String id){
        log.info( "pdf document = " + id);
        def documentFile = DocumentFile.get(id);

        def dir = appParametersService.getPdfPreviewPath()
        def fileName = "${id}_${documentFile.version}.pdf"

        def tmpRes = new File(grailsApplication.mainContext.getServletContext().getRealPath(dir+File.separator+fileName))

        if(!tmpRes.exists()){
            def tmpPdfFile = tmpRes
            tmpPdfFile.withOutputStream { s ->
                s << documentFile.content.content
            }
        }

        //TODO zmienic
        //while(!tmpRes.exists()){ System.sleep(2000) }
		System.sleep(10000)

        //render(template: '../forms/pdf/embedDocument', model:  [pdfDocument: resource(dir: dir ,file: fileName)]);
		render(template: '../forms/pdf/embedDocument', model:  [pdfDocument: "/eumowy/tmp/"+fileName]);
    }

    def downloadDoc(){
        log.info( "downloadDoc = " +  params.id);
        DocumentFile file = documentService.download(params.id)

        if(!(file?.content?.content)){
            redirect(action: "show")
        }

        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=\"${file.name}\"")
        response.outputStream << file.content.content
    }

    def downloadAttachment(){
        log.info( "downloadAttachment = " +  params.id);
        AttachmentFile file = attachmentService.download( params.id, request , messageSource)

        if(!(file?.file?.content)){
            redirect(action: "show")
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=\"${MimeUtility.encodeWord(file.name)}\"")
        response.outputStream << file.file.content
    }
/*
    private boolean isNotesEmpty(def notes){
        if(notes == null || notes == ""){
            return true
        }
        return false
    }*/
}
