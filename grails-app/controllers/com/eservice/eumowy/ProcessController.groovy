package com.eservice.eumowy

import com.eservice.eumowy.util.DateUtils
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.ApplicationHolder

class ProcessController {

    def messageSource
    def attachmentService
    def documentService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['PH_ROLE','ADM_ROLE'])
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

//        log.info("PSZKUP in Controller --> filterPhNo: " + params.filterPhNo + "; filterDateFrom: " + params.filterDateFrom + "; filterDateTo: " + params.filterDateTo)

        if(params.filterStatus == null){
            params.filterStatus = Process.ProcessStatus.REJECTED.name()
        }

        def processes = new ProcessService().searchProcessByFilters(params)

        [   filterStatus: params.filterStatus,
            filterObserved:params.filterObserved,
            filterNip:params.filterNip,
            filterPhNo:params.filterPhNo,
            filterDateFrom: params.filterDateFrom?params.filterDateFrom: DateUtils.getFormattedDate(DateUtils.addDays(new Date(), -30), DateUtils.DD_MM_YYYY),
            filterDateTo: params.filterDateTo?params.filterDateTo: DateUtils.getFormattedDate(new Date(), DateUtils.DD_MM_YYYY),
            processInstanceList: processes.searchResults,
            processInstanceTotal: processes.searchResultSize]
    }

    //---------------------------------
    // PH AVAILABLE
    //---------------------------------

    @Secured(['PH_ROLE'])
    def edit(Long id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list" )
            return
        }

        [processInstance: processInstance]
    }

    //---------------------------------
    // ADMIN AVAILABLE
    //---------------------------------

    @Secured(['ADM_ROLE'])
    def show(String id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list")
            return
        }

        [processInstance: processInstance]
    }

    @Secured(['ADM_ROLE'])
    def reject(String id) {
        def processInstance =Process.get(id)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list")
            return
        }

        processInstance.status = Process.ProcessStatus.REJECTED;
        processInstance.save(flush: true, validate: false)
        flash.message = message(code: 'default.rejected.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])
        redirect(action: "list")
    }

    @Secured(['ADM_ROLE'])
    def accept(String id) {
        def processInstance = Process.get(id)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args:[ message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list")
            return
        }

        processInstance.status = Process.ProcessStatus.ACCEPTED;
        processInstance.save(flush: true, validate: true)
        flash.message = message(code: 'default.accepted.message', args:[ message(code: 'process.label', default: 'proces'), processInstance.id])
        redirect(action: "list")
    }

    //---------------------------------
    // REMOTE CALLS
    //---------------------------------

    def showPdfByDocumentId(String id){
        log.info( "pdf document = " + id);
        def documentFile = DocumentFile.get(id);

        def dir = "tmp"
        def fileName = "${id}_${documentFile.version}_${documentFile.name}"
        def tmpPdf = ApplicationHolder.getApplication().getParentContext().getResource("${dir}/${fileName}").getFile()

        if(!tmpPdf.exists()){
            tmpPdf.withOutputStream { s ->
                s << documentFile.content.content
            }
        }

        render(template: '../forms/pdf/embedDocument', model:  [pdfDocument: resource(dir:dir, file:fileName)]);
    }

    def downloadDoc(){
        DocumentFile file = documentService.download(params.id)

        if(!file?.content?.content){
            return;
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=${file.name}.pdf")
        response.outputStream << file.content.content
    }

    def downloadAttachment(){
        AttachmentFile file = attachmentService.download( params.id, request , messageSource)

        if(file?.file?.content){
            return;
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=${file.name}")
        response.outputStream << file.file.content
    }
}
