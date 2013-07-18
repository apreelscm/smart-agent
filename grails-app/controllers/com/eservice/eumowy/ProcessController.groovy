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
        [processInstanceList: Process.list(params), processInstanceTotal: Process.count()]
    }

    @Secured(['PH_ROLE'])
    def create() {
        [processInstance: new Process(params)]
    }

    @Secured(['PH_ROLE'])
    def save() {
        //TODO implement
       /* def processInstance = new Process(params)
        if (!processInstance.save(flush: true)) {
            render(view: "create", model: [processInstance: processInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'process.label', default: 'Process'), processInstance.id])
        redirect(action: "show", id: processInstance.id)*/
    }
    @Secured(['PH_ROLE','ADM_ROLE'])
    def show(Long id) {
        def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'Process'), id])
            redirect(action: "list")
            return
        }

        [processInstance: processInstance]
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

        selectedProcesses.each() {
            log.info "trying to delete selected process id = " + it.id;
        };

        Process.deleteAll(selectedProcesses)

        redirect(action: "list")
    }

    @Secured(['ADM_ROLE'])
    def reject(Long id) {

        //TODO implement

     /*   def processInstance = Process.get(id)
        if (!processInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'process.label', default: 'Process'), id])
            redirect(action: "list")
            return
        }

        try {
            processInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'process.label', default: 'Process'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'process.label', default: 'Process'), id])
            redirect(action: "show", id: id)
        }*/
    }

    @Secured(['ADM_ROLE'])
    def accept(Long id) {
    }
}
