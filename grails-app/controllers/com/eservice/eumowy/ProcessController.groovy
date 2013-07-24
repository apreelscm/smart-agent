package com.eservice.eumowy
import grails.plugins.springsecurity.Secured

class ProcessController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['PH_ROLE','ADM_ROLE'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        log.info("Process.count() - " + Process.count());
        [processInstanceList: Process.list(params), processInstanceTotal: Process.count()]
    }

    //---------------------------------
    // PH AVAILABLE
    //---------------------------------

    @Secured(['PH_ROLE'])
    def create() {
        [processInstance: new Process(params)]
    }

    @Secured(['PH_ROLE'])
    def save() {
        //TODO implement
    }

    @Secured(['PH_ROLE'])
    def edit(Long id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'Process'), id])
            redirect(action: "list")
            return
        }

        [processInstance: processInstance]
    }

    @Secured(['PH_ROLE'])
    def update(Long id, Long version) {
        //TODO implement
    }

    @Secured(['PH_ROLE'])
    def delete() {

        def checkedBooks = params.list('selectedProcess')
        def selectedProcesses = Process.getAll(checkedBooks)

        Process.deleteAll(selectedProcesses)

        redirect(action: "list")
    }

    //---------------------------------
    // ADMIN AVAILABLE
    //---------------------------------

    @Secured(['ADM_ROLE'])
    def show(String id) {
        def processInstance = Process.findByUid(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'proces'), id])
            redirect(action: "list")
            return
        }

        [processInstance: processInstance]
    }

    @Secured(['ADM_ROLE'])
    def reject(String uid) {
        def processInstance =Process.findByUid(uid)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'test.label', default: 'proces'), uid])
            redirect(action: "list")
            return
        }

        processInstance.status = Process.ProcessStatus.REJECTED;
        processInstance.save(flush: true, validate: false)
        flash.message = message(code: 'default.rejected.message', args: [message(code: 'test.label', default: 'proces'), processInstance.uid])
        redirect(action: "list")
    }

    @Secured(['ADM_ROLE'])
    def accept(String uid) {
        def processInstance = Process.findByUid(uid)

        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'test.label', default: 'proces'), uid])
            redirect(action: "list")
            return
        }

        processInstance.status = Process.ProcessStatus.ACCEPTED;
        processInstance.save(flush: true, validate: true)
        flash.message = message(code: 'default.accepted.message', args: [message(code: 'test.label', default: 'Proces'), processInstance.uid])
        redirect(action: "list")
    }

    //---------------------------------
    // REMOTE CALLS
    //---------------------------------

    /**
     * Filtrowanie procesów po wybranym statusie
     * @return listTable html
     */
    def filterByStatus(String status) {
        def filteredProcesses = status !="" ?  Process.findAllByStatus(status) : Process.list(params);
        render template: 'table/listTable', model: [processInstanceList: filteredProcesses, processInstanceTotal: filteredProcesses.size()]
    }

    def filter = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        render(template: 'table/listTable', model:  [processInstanceList: Process.list(params), processInstanceTotal: Process.count()])
    }

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

    def downloadAtt(String id){
        log.info "downloadAtt id = ${id}";
        def attachment = AttachmentFile.get(Integer.valueOf(id));

        log.info "documentDoc name =  ${attachment.filename}";

        def fileAtt = new File("web-app\\files\\${attachment.filename}");
        log.info fileAtt.absolutePath;

        if(fileAtt.exists()){
            def fileName = fileAtt.getName();
            response.setContentType("application/pdf")
            response.setHeader "Content-disposition", "attachment; filename=\"${fileName}\"";
            response.outputStream << fileAtt.newInputStream();
            response.outputStream.flush();
        }
    }


}
