package com.eservice.eumowy

import org.springframework.dao.DataIntegrityViolationException

class PanelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def processService

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {

        def process = new Process();
        process.panels = Panel.list();
        process.client = new Client(nip: '334455667')

        println("process.panels:"+ process.panels)

        def processCmd =  processService.getDataForPanels(process)
        params.max = Math.min(max ?: 10, 100)
        [panelInstanceList: Panel.list(params), panelInstanceTotal: Panel.count(), data: processCmd]
    }

    def create() {
        [panelInstance: new Panel(params)]
    }

    def save() {
        def panelInstance = new Panel(params)
        if (!panelInstance.save(flush: true)) {
            render(view: "create", model: [panelInstance: panelInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'panel.label', default: 'Panel'), panelInstance.id])
        redirect(action: "show", id: panelInstance.id)
    }

    def show(Long id) {
        def panelInstance = Panel.get(id)
        if (!panelInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "list")
            return
        }

        [panelInstance: panelInstance]
    }

    def edit(Long id) {
        def panelInstance = Panel.get(id)
        if (!panelInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "list")
            return
        }

        [panelInstance: panelInstance]
    }

    def update(Long id, Long version) {
        def panelInstance = Panel.get(id)
        if (!panelInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (panelInstance.version > version) {
                panelInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'panel.label', default: 'Panel')] as Object[],
                        "Another user has updated this Panel while you were editing")
                render(view: "edit", model: [panelInstance: panelInstance])
                return
            }
        }

        panelInstance.properties = params

        if (!panelInstance.save(flush: true)) {
            render(view: "edit", model: [panelInstance: panelInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'panel.label', default: 'Panel'), panelInstance.id])
        redirect(action: "show", id: panelInstance.id)
    }

    def delete(Long id) {
        def panelInstance = Panel.get(id)
        if (!panelInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "list")
            return
        }

        try {
            panelInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'panel.label', default: 'Panel'), id])
            redirect(action: "show", id: id)
        }
    }
}
