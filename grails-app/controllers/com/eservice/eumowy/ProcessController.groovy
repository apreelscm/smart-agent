package com.eservice.eumowy
import grails.plugins.springsecurity.Secured

class ProcessController {

    def messageSource
    def attachmentService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['PH_ROLE','ADM_ROLE'])
    def list() {
        /*  params.max = Math.min(max ?: 10, 100)

          log.info("params.status - " + params?.filterStatus);

          [processInstanceList: findProcessListByStatus(params?.filterStatus),
                  processInstanceTotal: Process.count(),
                  filterStatus : Process.ProcessStatus.REJECTED.name()]*/


        params.max = Math.min(params.max ? params.int('max') : 10, 100)


        if(params.filterStatus == null){
            params.filterStatus = Process.ProcessStatus.REJECTED.name()
        }

        if(params.filterStatus.equals("")) [
                filterStatus:"",
                filterObserved:"",
                filterNip:"",
                filterPhNo:"",
                filterDateFromDF:"",
                filterDateToDF:"",
                processInstanceList: Process.list(params),
                processInstanceTotal: Process.count()]
        else{

            def processService = new ProcessService()
            println params;

            def processes = processService.searchProcessByFilters(params)

            [ filterStatus:params.filterStatus,
                    filterObserved: params.filterObserved,
                    filterNip:params.filterNip,
                    filterPhNo:params.filterPhNo,
                    filterDateFromDF:params.filterDateFromDF,
                    filterDateToDF:params.filterDateToDF,
                    processInstanceList: processes.searchResults ,
                    processInstanceTotal: processes.searchResultSize]
        }
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

    /**
     * Filtrowanie procesów po wybranym statusie
     * @return listTable html
     */
    /*   def filterByStatus(String filterStatus) {
           def filteredProcesses = findProcessListByStatus(filterStatus)
           render template: 'table/listTable', model: [processInstanceList: filteredProcesses, processInstanceTotal: filteredProcesses.size()]
       }*/
/*
    def filter = {
        log.info("params.status - " + params?.filterStatus);
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        render(template: 'table/listTable', model:  [processInstanceList: Process.list(params), processInstanceTotal: Process.count()])
    }*/

    def showPdfByDocumentId(String id){
        log.info( "pdf document = " + id);
        def documentfile = DocumentFile.get(id);
        render(template: '../forms/pdf/embedDocument', model:  [pdfDocument: resource(dir:'files', file:documentfile.filename)]);
    }


    def downloadDoc(String id){
        log.info "downloadDoc id = ${id}";
        def document = DocumentFile.get(Integer.valueOf(id));

        log.info "documentDoc name =  ${document.filename}";

        def fileDoc = new File("web-app\\files\\${document.filename}");
        log.info fileDoc.absolutePath;

        if(fileDoc.exists()){
            // force download
            def fileName = fileDoc.getName();
            response.setContentType("application/pdf")
            response.setHeader "Content-disposition", "attachment; filename=\"${fileName}\"";
            response.outputStream << fileDoc.newInputStream();
            response.outputStream.flush();
        }
    }

    def downloadAttachment(){
        AttachmentFile ufile = attachmentService.downloadFile( params.id, request , messageSource)
        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "${params.contentDisposition}; filename=${ufile.name}")
        response.outputStream << ufile.file.content
    }

}
