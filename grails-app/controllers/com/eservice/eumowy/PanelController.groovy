package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import org.springframework.dao.DataIntegrityViolationException

class PanelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def processService
    def cbdService

    def index() {
        redirect(action: "list", params: params)

        def process =new Process(phFirstName: "Jerzy",
                saleSection: 'segment1', phNumber: 12345, observed: true,
                phSurname: 'Kowalski', calcNumber: '44444' , status: Process.ProcessStatus.WAITING,
                client: Client.findByName("clientName1"))


        process.save(flush: true);
        println("errors:"+process.errors)
    }

    def list(Integer max) {

        if(flash.data){
            [panelInstanceList: Panel.list(params), panelInstanceTotal: Panel.count(), data: flash.data]
        }
        else{
            def process = new Process();
            process.panels = Panel.list();
            process.client = new Client(nip: '4457490660')

            def calc = cbdService.findCalculatorByNip('4457490660')

            def processCmd =  new ProcessCommand()//processService.getSavedProcessCommand(process,calc)
            processCmd.umowaOznOd = ""
            processCmd.umowaOznDo = ""
            processCmd.akceptantTelStacjonarny = ""
            processCmd.akceptantTelKomorkowy = ""
            processCmd.kontaktTelKomorkowy = ""
            processCmd.kontaktTelStacjonarny = ""


            params.max = Math.min(max ?: 10, 100)
            [panelInstanceList: Panel.list(params), panelInstanceTotal: Panel.count(), data: processCmd]
        }
    }

    def create() {
        [panelInstance: new Panel(params)]
    }

    def save(ProcessCommand cmd) {
        log.info(params)
        log.info(cmd.properties)

        if(cmd.hasErrors()){
            cmd.errors.each {
                log.error(it)
            }
        }

        flash.data = cmd

        redirect(action: "list")
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
