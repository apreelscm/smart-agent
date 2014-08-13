
package com.eservice.eumowy

import com.eservice.eumowy.auth.EServiceUserDetails
import com.eservice.eumowy.dto.MerchantDetailsDTO
import grails.converters.JSON
import groovy.sql.GroovyRowResult
import org.codehaus.groovy.grails.web.json.JSONObject
import org.hibernate.StaleObjectStateException
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException
import pdfgenerator.PdfGenerator

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.process.DefineActivityCommand
import com.eservice.eumowy.util.DateUtils

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    private static final String TEMPLATES="templates"
    private static final String ELECTRIONICAL="electronical"
    private static final String PAPER="paper"

    def grailsApplication
    def emailService
    def cbdService
    def attachmentService
    def clientService
    def processService
    def calculatorService
    def pdfService
	def documentService
    def dictionaryService
    def bisnodeService
    def mailBodyCreatorService

    def springSecurityService

    def index() {
        params.remove("errorMessage")
        redirect(action: "createProcess", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [activityInstanceList: Activity.list(params), activityInstanceTotal: Activity.count()]
    }

    def flowExceptionHandler() {
        String message = message(code: 'flow.excecution.exception')
        redirect(action: "createProcess", params: [errorMessage: message])
    }

    /**
     * MAIN PROCESS FLOW
     * */
    def createProcessFlow = {
        init{
            action {
                log.info("init new flow")
                log.info("init conversation.calc"+conversation.calc)
                log.info("init params:"+params)
                log.info("init params.message:"+ params.message)
                log.info("init flow.message:"+ flow.message)
                log.info("init flash.message:"+ flash.message)
                log.info("init flow.prevActivityMessage:"+flow.prevActivityMessage)
                if (params.message) {
                    flow.prevActivityMessage = params.message
                }
                if(params.errorMessage) {
                    flash.errorMessage = params.errorMessage
                }
                flash.infoMessage =  flow.prevActivityMessage
                flow.isGoBack = false
            }
            on("success").to "defineActivity"
        }
        defineActivity{
            onEntry{
                if(flow.isGoBack == false){
                    log.info("start new flow")
                    flow.processInstance =  new Process();
                    flow.newProcessFlow = true
                }
            }
            on("continue") { DefineActivityCommand cmd ->
                def processInstance = flow.processInstance
                if(cmd?.hasErrors()){
                    flash.errorMessage= message(code: cmd.errors?.getFieldError("selectedActivities").code);
                    return error();
                }

                processInstance.activities = Activity.findAllByCodeInList(cmd.selectedActivities);
                processInstance.activities*.selectedActivitySignatures = null;
                processInstance.notesToCoa = cmd.notes;
                flow.processInstance = processInstance
            }.to "chooseSubFlow"
            on("error").to "defineActivity"
        }

        chooseSubFlow {
            action {
                def processInstance = flow.processInstance
                def hasPoprawDane = processService.containsActivity(processInstance.activities,"poprawDane")
                def hasUzupelnijPodpisy = processService.containsActivity(processInstance.activities,"uzupelnijPodpisy")
                def hasOdrzucDokumenty = processService.containsActivity(processInstance.activities,"odrzucDokumenty")

                if(processInstance.activities?.size() == 0){
                    emailOnly();
                }
                else if(hasPoprawDane){
                    poprawDane();
                }
                else if(hasUzupelnijPodpisy){
                    uzupelnijPodpisy();
                }
                else if(hasOdrzucDokumenty){
                    odrzucDokumenty();
                }
                else{
                    normal();
                }
            }
            on("error").to "defineActivity"
            on("normal").to "normal"
            on("uzupelnijPodpisy").to "uzupelnijPodpisy"
            on("poprawDane").to "poprawDane"
            on("odrzucDokumenty").to "odrzucDokumenty"
            on("emailOnly").to "emailOnly"
        }

        /** send email only subflow*/
        emailOnly {
            action{
                Process processInstance = flow.processInstance
                log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${processInstance.notesToCoa}]")

                EServiceUserDetails user = springSecurityService.principal
                Map bodyParams = mailBodyCreatorService.notesToCoa(processInstance, user)
                if (!emailService.sendNotesToCOA(bodyParams)) {
                    return error()
                }
            }
            on("error"){
                flash.errorMessage = message(code:"email.send.error")
            }to "defineActivity"
            on("success"){
                flow.prevActivityMessage = message(code:"email.notesToCOA.send.complete")
            }to "beforeRestart"
        }

        /** default full subflow */
        normal {
            subflow(action: "normal", input: [processInstance : { flow.processInstance }, newProcessFlow : {flow.newProcessFlow}, calcId: {flow.calcId}])
            on("backToStart"){
                flow.isGoBack = true;
            }to "defineActivity"
            on("clientSignature") {
                flow.processInstance = currentEvent.attributes.process
                flow.representative1 = currentEvent.attributes.representative1
                flow.representative2 = currentEvent.attributes.representative2
                flow.calcId = currentEvent.attributes.calcId
                flow.prevActivityMessage = message(code: 'process.completed', args:[flow.processInstance.client.nip])
            }.to "clientSignature"
            on("reject"){
                flow.processInstance = currentEvent.attributes.process
                flow.prevActivityMessage = message(code: 'process.reject', args:[flow.processInstance.client.nip])
            }.to "finish"
        }

        /** popraw dane subflow */
        poprawDane {
            subflow(action: "poprawDane", input: [processInstance : { flow.processInstance }, newProcessFlow : {flow.newProcessFlow}])
            on("backToStart"){
                flow.isGoBack = true;
            }to "defineActivity"
            on("clientSignature") {
                flow.processInstance = currentEvent.attributes.process
                flow.representative1 = currentEvent.attributes.representative1
                flow.representative2 = currentEvent.attributes.representative2
                flow.prevActivityMessage = message(code: 'process.udpated', args:[flow.processInstance.client.nip])
            }.to "clientSignature"
            on("reject"){
                flow.processInstance = currentEvent.attributes.process
                flow.prevActivityMessage = message(code: 'process.reject', args:[flow.processInstance.client.nip])
            }.to "finish"
        }

        /** uzupelnij podpisy subflow */
        uzupelnijPodpisy {
            subflow(action: "uzupelnijPodpisy", input: [processInstance : { flow.processInstance }, newProcessFlow : {flow.newProcessFlow}])
            on("backToStart"){
                flow.isGoBack = true;
            }to "defineActivity"
            on("clientSignature") {
                flow.processInstance = currentEvent.attributes.process
                flow.representative1 = currentEvent.attributes.representative1
                flow.representative2 = currentEvent.attributes.representative2
                flow.prevActivityMessage = message(code: 'process.udpated.signatures', args:[flow.processInstance.client.nip])
                flow.isUzupelnijPodpisy = true
            }.to "clientSignature"
        }

        /** popraw dane subflow */
        odrzucDokumenty {
            subflow(action: "odrzucDokumenty", input: [processInstance : { flow.processInstance }])
            on("backToStart"){
                flow.isGoBack = true;
            }to "defineActivity"
            on("finish"){
                flow.processInstance = currentEvent.attributes.process
                flow.prevActivityMessage = message(code: 'process.reject', args:[flow.processInstance.client.nip])
            }.to "finish"
        }

        clientSignature {
            onEntry {
                Process processInstance = flow.processInstance
                
                setRepresentatives(flow)
                
                flow.requiredNumberOfSubscriptions = 1 //PH subscription is always required

                boolean isWymianaTerminalaOnly = processService.hasOnlyConcreteActivity(processInstance, "wymianaTermianala")

                if (!isWymianaTerminalaOnly){
                    if (flow.representative1) {
                        flow.requiredNumberOfSubscriptions++
                    }

                    if (flow.representative2) {
                        flow.requiredNumberOfSubscriptions++
                    }
                }

                if (!flow.skipDocumentGeneration && !flow.isUzupelnijPodpisy) {
                    Map processWithPages = pdfService.workWithDocuments(processInstance, conversation.calc)
                    flow.totalPagesCount = processWithPages.totalPagesCount
                    processInstance = processWithPages.processInstance
                    processInstance.save(flush: true)
                }
                flow.skipDocumentGeneration = false
                flow.processInstance = processInstance
                flow.rejectedDocumentsMessage = message(code: 'process.reject', args:[flow.processInstance.client.nip])
            }
            render(view: "../createProcess/clientSignature", model: [
                    processInstance: flow.processInstance,
                    totalPagesCount: flow.totalPagesCount,
                    representative1: flow.representative1,
                    representative2: flow.representative2,
                    requiredNumberOfSubscriptions: flow.requiredNumberOfSubscriptions,
                    clientNip: flow.rejectedDocumentsMessage,
                    isUzupelnijPodpisy: flow.isUzupelnijPodpisy
            ])
            on("back"){
                flow.newProcessFlow = false
            }to "chooseSubFlow"
            on("subscribe").to "clientSignature"
            on("updateProcessStatus") {
                log.info params
                Process processInstance = flow.processInstance
                if (params.processStatus.equals("WAIT_FOR_SUBSCRIPTION")) {
                    processInstance.status = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
                    Subscription sub = Subscription.get(params.subscriptionId)
                    if (sub == null) {
                        log.info "PUSTE ID!"
                    }
                    processInstance.addToSubscriptions(sub)
                    sub.save(flush: true)
                } else if (params.processStatus.equals("SUBSCRIPTIONS_DONE")) {
                    processInstance.status = Process.ProcessStatus.SUBSCRIPTIONS_DONE
                    Subscription subscription = Subscription.get(params.subscriptionId)

                    if (!subscription) {
                        log.error String.format("Nie znaleziono podpisu o id %s dla procesu %s", params.subscriptionId, processInstance.id)
                    }

                    processInstance.addToSubscriptions(subscription)
                    subscription.save(flush: true)

                    String currentDate = DateUtils.formatWithTimezone(DateUtils.getCurrentDate());
                    log.info 'Zapisuje formatowana dateUmowy: ' + currentDate

                    ProcessData savedAgreementDate = processInstance.getProcessData("dataUmowy")

                    if (savedAgreementDate) {
                        savedAgreementDate.value = currentDate
                    } else {
                        ProcessData actualDataUmowy = new ProcessData(name: "dataUmowy", value: currentDate)

                        processInstance.addToProcessData(actualDataUmowy)
                    }
                } else if (params.processStatus.equals("REJECTED")) {
                    processInstance.status = Process.ProcessStatus.REJECTED
                }

                if (!processInstance.save(flush: true)) {
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.skipDocumentGeneration = true
                flow.processInstance = processInstance
            }.to "clientSignature"
            on("noaccept") {
                Process processInstance = flow.processInstance
                processInstance.status = Process.ProcessStatus.REJECTED
                flow.processInstance = processInstance
            }.to "finish"
            on("submit") {
                log.info "PARAMS: " + params
                Process processInstance = flow.processInstance
                _processDocumentCreation(processInstance, params.requestVersion, flow.requiredNumberOfSubscriptions)
                processInstance.status = _getNewProcessStatus(params, flow.requiredNumberOfSubscriptions)
                flow.processInstance = processInstance
            }.to "finish"
        }

        /** final operations and process save*/
        finish{
            action{
                Process processInstance = flow.processInstance

                if (!processInstance.save(flush:true)){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return "error"
                }

                if (processInstance.notesToCoa) {
                    log.info(String.format("Wysyłanie wiadomości email z uwagami do COA [notes: %s]", ${processInstance.notesToCoa}))
                    EServiceUserDetails user = springSecurityService.principal
                    Map bodyParams = mailBodyCreatorService.notesToCoa(processInstance, user)
                    emailService.sendNotesToCOA(bodyParams)
                }

                flow.processInstance = null;
            }
            on("error"){
                flow.newProcessFlow = false
            }to "chooseSubFlow"
            on("success"){
                flow.newProcessFlow = true
            }.to "beforeRestart"
        }

        beforeRestart{
            action {
                redirect action: "createProcess", controller: "activity", params: [message:flow.prevActivityMessage]
            }
            on("success").to "restartFlow"
        }

        restartFlow()
    }
/**
     * DEFAULT FULL SUBFLOW
     * */
    def normalFlow = {
        input {
            processInstance(required: true)
            newProcessFlow(required: true)
            calcId(required: false)
        }

        init {
            action {
                log.info("init - normalFlow - newProcessFlow : ${flow.newProcessFlow}" )
                flow.newProcessFlow ? chooseActivity() : selectedPanels()
            }
            on("chooseActivity").to "chooseActivity"
            on("selectedPanels"){
                flow.skipPanelsInit = true;
            }.to "selectedPanels"
        }

        chooseActivity {
            render(view: "../createProcess/chooseActivity")
            on("continue"){
                Process processInstance = flow.processInstance

                Set<Signature> signatures =  _getSignatures(processInstance.activities)

                log.info(String.format("Found signatures: %s", signatures))

                processInstance.signatures = signatures

                flow.processInstance = processInstance
            }.to "chooseCalc"
            on("back").to "backToStart"
        }

        chooseCalc{
            onEntry{
                if(!flow.getCalculatorSucces){
                    flow.isContinueEnabled = false;
                    flow.calcNumber = null
                    flow.client = null
                }else{
                    flow.getCalculatorSucces = false
                }
            }
            render(view: "../createProcess/chooseCalc")
            on("back").to "chooseActivity"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.processInstance

                processInstance.calcNumber = flow.calcNumber

                Client client = flow.client

                log.info(String.format("Found client: ", client))

                if (!client.id && !client.save(flush:true)) {
                    client.errors.each { log.error(it) }
                    return "error"
                }

                processService.setPhDetailsFromUser(processInstance, springSecurityService.principal)

                processInstance.client =  client
                processInstance.status = Process.ProcessStatus.NEW

                if (!processInstance.save(flush:true)){
                    processInstance.errors.each { log.error(it) }
                    return "error"
                }

                log.info("NOWY PROCES - utworzenie procesu - nip = ${flow.nip} , processId = ${processInstance?.id}, status = ${processInstance?.status}")

                flow.processInstance = processInstance

            }.to "selectedPanels"
            on("error").to "chooseCalc"
        }

        getCalculator {
            action {
                flow.nip = params.nip;

                Process processInstance = flow.processInstance

                if(clientService.isClientNipInvalid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                Client client = cbdService.findClientByNip(flow.nip)

                boolean hasNowaUmowa = ActivityHelper.isNewAgreement(processInstance)

                if(client?.cbdId){
                    if(hasNowaUmowa || ActivityHelper.isBundleActivity(processInstance)) {
                        flash.nipErrorMessage = message(code:"client.newAgreementAndClientCBD.error", default:"Nowa umowa dla klienta CBD");
                        log.info(message(code:"client.newAgreementAndClientCBD.error") + " - " + flow.nip)
                        return error();
                    } else {
                        flash.nipInfoMessage = message(code:"client.found.info")
                    }
                } else {
                    if(hasNowaUmowa || ActivityHelper.isClientRedundant(processInstance)) {
                        flash.nipInfoMessage =  message(code:"client.new.info")
                        client = new Client(nip:params.nip)
                    } else {
                        flash.nipErrorMessage = message(code:"client.notFound.error")
                        log.info(message(code:"client.notFound.error") + " - " + flow.nip)
                        return error();
                    }
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                Process lastProcess = processService.getLastProcessWhenNotInStatus(params.nip,[Process.ProcessStatus.REJECTED,Process.ProcessStatus.ACCEPTED])

                if (lastProcess) {
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error")
                    lastProcess?.discard() // drop it from session
                    return error();
                }

                lastProcess?.discard() // drop it from session

                /** pobieranie danych o kalkulatorze */
                if (ActivityHelper.isCalculatorRedundant(processInstance)){
                    conversation.calc = [:]
                    flow.calcNumber = -1
                    flash.calcInfoMessage = message(code:"calc.not.needed.info")
                } else {
                    def calcId = cbdService.findCalculatorIdByNip(client.nip)

                    if(!calcId){
                        flash.calcErrorMessage = message(code:"calc.notFound.error")
                        return error();
                    }

                    def calc = cbdService.findCalculatorByNip(client.nip)

                    if(calc == []) {
                        flash.calcErrorMessage = message(code:"calc.fetch.error")
                        return error();
                    }

                    if(!calculatorService.isCalcValid(calc,calcId,processInstance)){
                        flash.calcErrorMessage =  message(code:"calc.notEnough.error")
                        return error();
                    }

                    conversation.calc = calc
                    flow.calcNumber =  calcId
                    flash.calcInfoMessage = message(code:"calc.found.info")
                }

                if(hasNowaUmowa) {
                    MerchantDetailsDTO merchantDetails = bisnodeService.getMerchantDetails(flow.nip)
                    if (merchantDetails) {
                        flash.bisnodeMessage = message(code: 'bisnode.merchant.found')
                        flow.bisnodeMerchantDetails = merchantDetails
                        flow.representativesBisnode = bisnodeService.getRepresentatives(merchantDetails)

                        if(merchantDetails?.representatives.size() == 0) {
                            flash.representativesNotFound = message(code: 'bisnode.representatives.not.found')
                        }
                    } else {
                        flash.bisnodeMessage = message(code: 'bisnode.merchant.not.found')
                    }
                }
            }
            on("success"){
                flow.isContinueEnabled = true
                flow.getCalculatorSucces = true
            }.to "chooseCalc"
            on("error"){
                flow.getCalculatorSucces = false
            }.to "chooseCalc"
        }

        selectedPanels{
            onEntry {
                Process processInstance = flow.processInstance

                ProcessCommand processCommand

                if(!flow.skipPanelsInit) {
                    log.info("skipPanelsInit - false")
                    TreeSet beforeExclusionPanels = _getActivePanels(processInstance.signatures)
                    processInstance.panels = processService.filterExcludedPanels(processInstance, beforeExclusionPanels.toList())
                    processCommand = processService.getNewProcessCommand(processInstance, flow.calcNumber, conversation.calc)

                    if(flow.bisnodeMerchantDetails) {
                        processService.fillCommandWithBisnodeData(processCommand, flow.bisnodeMerchantDetails)
                    }

                    //inicjacyjne zapisanie danych pobranych z cbd i calc
                    processInstance = processService.populateProcessWithData(processInstance, processCommand, conversation.calc)

                    if (!processInstance.save()) {
                        processInstance.errors.each { log.error(it) }
                        return error();
                    }

                    processService.updatePointAndPosIds(processInstance, processCommand)

                    processInstance.posExchanges?.each { PosExchange pe ->
                        processCommand.posExchanges.find {it.tpsId == pe.tpsId}?.setId(pe.id)
                    }
                } else {
                    log.info("skipPanelsInit - true")
                    flow.skipPanelsInit = false
                    processCommand = processService.getSavedProcessCommand(processInstance, processInstance.calcNumber, conversation.calc, flow.newProcessFlow)

                    if(processCommand.isFromBisnode && !flow.representativesBisnode) {
                        log.info(String.format("Process with ID %s and NIP %s is from BISNODE. Filling flow with representatives from BISNODE.", processInstance.id, processCommand.nip))
                        flow.representativesBisnode = bisnodeService.getRepresentatives(processCommand.nip)
                    }

                    processCommand.nip = processInstance.client.nip
                }

                processCommand.liczbaPosZCbd = processCommand.getPosCountFromCBD()

                flow.data = processCommand
                flow.processInstance = processInstance
                flow.czyNowaUmowa = processService.isProcessHasActivity(processInstance, "nowaUmowa")
            }
            render(view: "../createProcess/selectedPanels")
            on("reject"){
                Process processInstance = flow.processInstance;
                processInstance.status = Process.ProcessStatus.REJECTED;
                flow.processInstance = processInstance
            }.to "reject"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("deletePoint") {
                Process processInstance = flow.processInstance
                ProcessCommand processCommand = flow.data
                PointData point = PointData.get(Integer.valueOf(params.pointId))

                if (point != null) {
                    log.info "DeletePoint - Usuwam punkt o id: " + params.pointId
                    processInstance.removeFromPoints(point)
                    point.delete()
                    processInstance.save(flush: true)

                    processCommand?.points?.removeAll { it.id == point.id }
                } else {
                    log.info "DeletePoint - Nie znalazłem punktu o id: " + params.pointId
                }

                flow.data = processCommand
                flow.processInstance = processInstance
            }.to "selectedPanels"
			on("deletePos") {
				Process processInstance = flow.processInstance
				ProcessCommand processCommand = flow.data
				PosData pos = PosData.get(Integer.valueOf(params.posId))

				if (pos) {
					log.info "DeletePos - Usuwam pos o id: " + params.posId
					PointData point = pos.point
					//pos.removeFromPoint(point)
					
					if (point == null) {
						point = PointData.find {
							process == processInstance &&
							posDatas { id == pos.id }
						}
					}
					
					if (point) {
						point.posDatas?.each {
							if (it?.parentPosId == pos?.id) {
								it.delete()
							}
						}
						point.posDatas?.removeAll { it == null || it?.id == pos?.id || it?.parentPosId == pos?.id }
						//point.removeFromPosDatas(pos)
						pos.delete()
						if (point.posDatas != null) {
							point.liczbaPos = point.posDatas.size()
						}
						point.save()
						processInstance.save(flush: true)
					}
	
					processCommand?.poses?.removeAll { it == null || it.id == pos.id }
				} else {
					log.info "DeletePos - Nie znalazłem pos o id: " + params.posId
				}
				flow.data = processCommand
				flow.processInstance = processInstance
			}.to "selectedPanels"
            on("saveOnly"){ ProcessCommand processCommand ->
                log.info params
                log.info params.get('allPoses[0]')
                Process processInstance = processService.populateProcessWithData(flow.processInstance, processCommand, conversation.calc)
                log.info "Zapisuje dane paneli"

                processCommand.calculatorService = calculatorService
                processInstance.save(flush: true, validate: false)

                log.info "Zapisano dane paneli"

                // Update
                processService.updatePointAndPosIds(processInstance, processCommand)

                flow.processInstance = processInstance
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){
                ProcessCommand processCommand = crateProcessCommand(params, conversation.calc)
                processCommand.validate()
                flow.data = processCommand

                if(grailsApplication.config.isPanelsValidationOn && processCommand.hasErrors()){
                    processCommand.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                Process processInstance = flow.processInstance

                clientService.updateClientName(processInstance.client, cmd)
                processInstance = processService.populateProcessWithData(processInstance, cmd, conversation.calc)
                processInstance.notesToCoa = cmd.notes;

                processInstance.client.name = cmd.akceptantNazwaOficjalna;
                processInstance.saleSection = calculatorService.getCalcProperty(conversation.calc,'SEGMENT_SPRZEDAZOWY')

                flow.representative1 = processService.getRepresentative(processInstance, 0)
                flow.representative2 = processService.getRepresentative(processInstance, 1)

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.processInstance = processInstance

            }.to "clientSignature"
            on(HibernateOptimisticLockingFailureException).to "optimisticLockingHandler"
            on(StaleObjectStateException).to "optimisticLockingHandler"
        }

        clientSignature {
            output {
                process {flow.processInstance}
                representative1 { flow.representative1 }
                representative2 { flow.representative2 }
                calcId { flow.calcId }
            }
        }

        reject{
            output {
                process {flow.processInstance}
            }
        }

        optimisticLockingHandler {
            action {
                log.info(String.format("Optimistic locking for process %s", flow?.processInstance?.id))
                flash.errorMessage = message(code: 'optimistic.locking')
                "success"
            }
            on("success").to "backToStart"
        }

        backToStart()
    }

    /**
     * POPRAW DANE
     * */

    def poprawDaneFlow = {
        input {
            processInstance(required: true)
            newProcessFlow(required: true)
        }

        init {
            action {
                log.info("init - poprawDaneFlow - newProcessFlow : ${flow.newProcessFlow}" )
                flow.newProcessFlow ? chooseCalc() : selectedPanels()
            }
            on("chooseCalc").to "chooseCalc"
            on("selectedPanels"){
                flow.skipPanelsInit = true;
            }.to "selectedPanels"
        }

        chooseCalc{
            onEntry{
                if(!flow.getCalculatorSucces){
                    flow.isContinueEnabled = false;
                    flow.calcNumber = null
                    flow.client = null
                }else{
                    flow.getCalculatorSucces = false
                }
            }
            render(view: "../createProcess/chooseCalc")
            on("back").to "backToStart"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.savedProcess

                log.info("POPRAW DANE - wczytanie procesu - nip = ${flow.nip} , processId = ${processInstance?.id}, status = ${processInstance?.status}")

                processService.setPhDetailsFromUser(processInstance, springSecurityService.principal)

                processInstance.calcNumber =  flow.calcNumber
                if (!processInstance.save(flush:true)){
                    processInstance.errors.each { log.error(it) }
                    return "error"
                }

                flow.processInstance = processInstance
            }.to "selectedPanels"
            on("error").to "chooseCalc"
        }

        getCalculator {
            action {
                flow.nip = params.nip

                if(clientService.isClientNipInvalid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                Client client = cbdService.findClientByNip(flow.nip);
                Process lastProcess = processService.getLastProcessForClient(params.nip)

                log.info("POPRAW DANE - Znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(!client?.cbdId){
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    boolean hasNowaUmowa = ActivityHelper.isNewAgreement(lastProcess)
                    if(hasNowaUmowa){
                        flash.nipInfoMessage =  message(code:"client.new.info")
                        client = new Client(nip:params.nip)
                    }else {
                        flash.nipErrorMessage = message(code:"client.notFound.error")
                        log.info(message(code:"client.notFound.error") + " - " + flow.nip)
                    }
                } else {
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono klienta w CBD");
                }

                if (!lastProcess?.id){
                    flash.nipErrorMessage = message(code:"process.openNotFound.error")
                    log.info(message(code:"process.openNotFound.error") + " - " + flow.nip)
                    return error()
                } else if (Process.ProcessStatus.ACCEPTED.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"process.youngerAcceptedProcess.error")
                    log.info(message(code:"process.youngerAcceptedProcess.error") + " - " + flow.nip)
                    return error()
                } else if (Process.ProcessStatus.WAITING.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"process.waitingForAccept.error")
                    log.info(message(code:"process.waitingForAccept.error") + " - " + flow.nip)
                    return error()
                }

                /** pobieranie danych o kalkulatorze */
                if (ActivityHelper.isCalculatorRedundant(lastProcess)){
                    conversation.calc = [:]
                    flow.calcNumber = -1
                    flash.calcInfoMessage = message(code:"calc.not.needed.info")
                } else {
                    def calcId = cbdService.findCalculatorIdByNip(client.nip)

                    if(!calcId){
                        flash.calcErrorMessage = message(code:"calc.notFound.error");
                        log.info(message(code:"calc.notFound.error") + " - " + client.nip)
                        return error()
                    }

                    def calc = cbdService.findCalculatorByNip(client.nip)

                    log.info("pobrano kalkulator " + calcId)

                    if(!calculatorService.isCalcValid(calc,calcId,lastProcess)){
                        flash.calcErrorMessage =  message(code:"calc.notEnough.error")
                        return error()
                    }

                    conversation.calc = calc
                    flow.calcNumber =  calcId;
                    flash.calcInfoMessage = message(code:"calc.found.info")
                }
                lastProcess?.discard() // drop it from session

                flow.savedProcess = lastProcess

                // aktualizacja danych klienta w procesie danymi z CBD
                lastProcess.client = client
                flow.client = lastProcess.client;
            }
            on("success"){
                flow.isContinueEnabled = true
                flow.getCalculatorSucces = true
            }.to "chooseCalc"
            on("error"){
                flow.getCalculatorSucces = false;
            }.to "chooseCalc"
        }

        selectedPanels{
            onEntry {
                log.info "SkipPanelsInit: " + flow.skipPanelsInit
                Process processInstance = flow.processInstance;
                ProcessCommand processCmd = processService.getSavedProcessCommand(processInstance, flow.calcNumber, conversation.calc, flow.newProcessFlow)

                if(processCmd.isFromBisnode && !flow.representativesBisnode) {
                    log.info(String.format("Process with ID %s and NIP %s is from BISNODE. Filling flow with representatives from BISNODE.", processInstance.id, processCmd.nip))
                    flow.representativesBisnode = bisnodeService.getRepresentatives(processCmd.nip)
                }

                processCmd.liczbaPosZCbd = processCmd.getPosCountFromCBD()

                flow.data = processCmd
                flow.czyNowaUmowa = processService.isProcessHasActivity(processInstance, "nowaUmowa")
            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("deletePoint") {
                Process processInstance = flow.processInstance
                ProcessCommand processCommand = flow.data
                PointData point = PointData.get(Integer.valueOf(params.pointId))

                if (point) {
                    log.info "DeletePoint - Usuwam punkt o id: " + params.pointId
                    processInstance.removeFromPoints(point)
                    point.delete()
                    processInstance.save(flush: true)

                    processCommand?.points?.removeAll { it.id == point.id }
                }
                else {
                    log.info "DeletePoint - Nie znalazłem punktu o id: " + params.pointId
                }
                flow.data = processCommand
                flow.processInstance = processInstance
            }.to "selectedPanels"
			on("deletePos") {
				Process processInstance = flow.processInstance
				ProcessCommand processCommand = flow.data
				PosData pos = PosData.get(Integer.valueOf(params.posId))

				if (pos) {
					log.info "DeletePos - Usuwam pos o id: " + params.posId
					PointData point = pos.point
					if (point) {
						point = PointData.find {
							process == processInstance &&
							posDatas { id == pos.id }
						}
					}
					
					if (point) {
						point.posDatas?.each {
							if (it?.parentPosId == pos?.id) {
								it.delete()
							}
						}
						point.posDatas?.removeAll { it == null || it?.id == pos?.id || it?.parentPosId == pos?.id }
						pos.delete()
						if (point.posDatas != null) {
							point.liczbaPos = point.posDatas.size()
						}
						
						point.save()
						processInstance.save(flush: true)
					}

                    processCommand?.poses?.removeAll { it == null || it.id == pos.id }
				}
				else {
					log.info "DeletePos - Nie znalazłem pos o id: " + params.posId
				}
				flow.data = processCommand
				flow.processInstance = processInstance
			}.to "selectedPanels"
            on("saveOnly"){ ProcessCommand cmd ->
                Process processInstance = processService.populateProcessWithData(flow.processInstance, cmd, conversation.calc)

                processInstance.save(flush: true, validate: false)

                log.info "Zapisano dane paneli"

                // Update
                processInstance.points?.each { point ->
                    if (point.cbdId != null) {
                        def foundApc = cmd.allPoints?.find { apc -> apc.cbdId == point.cbdId }
                        foundApc?.id = point.id
                    }
                }
				
				processInstance.points?.each { point ->
					point.posDatas?.each { pos ->
						if (pos?.tpsId != null) {
							def foundApc = cmd.allPoses?.find { apc -> apc.tpsId == pos.tpsId }
							if (foundApc != null) {
								foundApc.id = pos.id
							}
						}
					}
				}

                flow.processInstance = processInstance
                //  flow.data = cmd
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){
                ProcessCommand processCommand = crateProcessCommand(params, conversation.calc)
                processCommand.validate()
                flow.data = processCommand

                if(processCommand?.hasErrors()){
                    log.info(params)
                    return error();
                }

                Process processInstance = flow.processInstance

                processInstance = processService.populateProcessWithData(processInstance, processCommand, conversation.calc)
                processInstance.notesToCoa = processCommand.notes;

                flow.representative1 = processService.getRepresentative(processInstance, 0)
                flow.representative2 = processService.getRepresentative(processInstance, 1)

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                /* Delete subscriptions */
				List<Subscription> subscriptions = Subscription.findAll {
					process == processInstance || uniqueKey =~ processInstance.id.toString()+"%"
				}
				List subscriptionIds = subscriptions*.collect {subscription -> subscription.id}.flatten()

				if (subscriptionIds.size() > 0) {
					Subscription.executeUpdate("delete Subscription where id in (:list)",[list: subscriptionIds])
				}

				processInstance.subscriptions?.clear()
                flow.processInstance = processInstance
            }.to "clientSignature"
            on("reject"){
                Process processInstance = flow.processInstance;
                processInstance.status = Process.ProcessStatus.REJECTED
                flow.processInstance = processInstance
            }.to "reject"
            on(HibernateOptimisticLockingFailureException).to "optimisticLockingHandler"
            on(StaleObjectStateException).to "optimisticLockingHandler"
        }

        clientSignature {
            output {
                process {flow.processInstance}
                representative1 { flow.representative1 }
                representative2 { flow.representative2 }
            }
        }

        reject{
            output {
                process {flow.processInstance}
            }
        }


        optimisticLockingHandler {
            action {
                log.info(String.format("Optimistic locking for process %s", flow?.processInstance?.id))
                flash.errorMessage = message(code: 'optimistic.locking')
                "success"
            }
            on("success").to "backToStart"
        }


        backToStart()
    }

    /** UZUPELNIJ PODPISY SUBFLOW */
    def uzupelnijPodpisyFlow = {
        input {
            processInstance(required: true)
            newProcessFlow(required: true)
        }

        init {
            action {
                log.info("init - uzupelnijPodpisyFlow - newProcessFlow : ${flow.newProcessFlow}" )
                chooseCalc()
            }
            on("chooseCalc").to "chooseCalc"
        }

        chooseCalc{
            onEntry{
                if(!flow.getCalculatorSucces){
                    flow.isContinueEnabled = false;
                    flow.calcNumber = null
                    flow.client = null
                }else{
                    flow.getCalculatorSucces = false
                }
            }
            render(view: "../createProcess/chooseCalc")
            on("back").to "backToStart"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.savedProcess

                log.info("UZUPELNIJ PODPISY - wczytanie procesu - nip = ${flow.nip} , processId = ${processInstance?.id}, status = ${processInstance?.status}")

                flow.processInstance = processInstance
                flow.isUzupelnijPodpisy = true
            }.to "clientSignature"
        }

        getCalculator {
            action {
                flow.nip = params.nip;

                if(clientService.isClientNipInvalid(params.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                Client client = cbdService.findClientByNip(flow.nip)
                if(client?.cbdId){
                    flash.nipInfoMessage =  message(code:"client.found.info")
                }

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta Proces w statusie oczekiwania na podpis */
                Process lastProcess = processService.getLastProcessForClient(params.nip)
                log.info("UZUPELNIJ PODPISY - znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(!Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"client.addSignatures.error")
                    log.info(message(code:"client.addSignatures.error") + " - " + flow.nip)
                    return error()
                }

                flow.savedProcess = lastProcess
                flow.client = lastProcess.client
            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.omitting.info")
                flow.isContinueEnabled = true
                flow.getCalculatorSucces = true
            }.to "chooseCalc"
            on("error"){
                flow.getCalculatorSucces = false;
            }.to "chooseCalc"
        }

        clientSignature {
            output {
                process {flow.processInstance}
                representative1 { flow.representative1 }
                representative2 { flow.representative2 }
                isUzupelnijPodpisy { flow.isUzupelnijPodpisy }
            }
        }

        backToStart()
    }


    /** ODZRUC DOKUMENTY SUBFLOW */
    def odrzucDokumentyFlow = {
        input {
            processInstance(required: true)
        }

        init {
            action {
                log.info("init - odrzucDokumenty" )
                chooseCalc()
            }
            on("chooseCalc").to "chooseCalc"
        }

        chooseCalc{
            onEntry{
                if(!flow.getCalculatorSucces){
                    flow.isContinueEnabled = false;
                    flow.calcNumber = null
                    flow.client = null
                }else{
                    flow.getCalculatorSucces = false
                }
            }
            render(view: "../createProcess/chooseCalc")
            on("back").to "backToStart"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.savedProcess

                log.info("ODRZUCENIE PROCESU - wczytanie procesu - nip = ${flow.nip} , processId = ${processInstance?.id}, status = ${processInstance?.status}")

                processInstance.status = Process.ProcessStatus.REJECTED;
                flow.processInstance = processInstance
            }.to "finish"
        }

        getCalculator {
            action {
                flow.nip = params.nip

                if(clientService.isClientNipInvalid(params.nip)) {
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                Client client = cbdService.findClientByNip(flow.nip)

                if(client?.cbdId){
                    flash.nipInfoMessage = message(code:"client.found.info")
                }

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta jakis Proces */
                Process lastProcess = processService.getLastProcessForClient(params.nip)
                log.info("ODRZUCENIE PROCESU - znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(!lastProcess?.id){
                    flash.nipErrorMessage = message(code:"client.eUmowyNotFound.error")
                    log.info(message(code:"client.eUmowyNotFound.error") + " - " + flow.nip)
                    return error();
                }
                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                else if (Process.ProcessStatus.ACCEPTED.equals(lastProcess?.status)){
                    /* ostatni proces jest zaakceptowany */
                    flash.nipErrorMessage = message(code:"process.rejectAccepted.error")
                    log.info(message(code:"process.rejectAccepted.error") + " - " + flow.nip)
                    return error()
                }
                else if (Process.ProcessStatus.REJECTED.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"process.openNotFound.error")
                    log.info(message(code:"process.openNotFound.error") + " - " + flow.nip)
                    return error()
                }
                flow.savedProcess = lastProcess
                flow.client = lastProcess.client;
            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.omitting.info")
                flow.isContinueEnabled = true
                flow.getCalculatorSucces = true
            }.to "chooseCalc"
            on("error"){
                flow.getCalculatorSucces = false;
            }.to "chooseCalc"
        }

        finish{
            output {
                process {flow.processInstance}
            }
        }

        backToStart()
    }


//--------------
//REMOTE METHODS
//--------------

    def upload() {
        def config = grailsApplication.config.fileuploader[params.upload]
        def msg = attachmentService.uploadFile(config,request);

        if(msg instanceof AttachmentFile){
            def attachment = msg as AttachmentFile
            attachment.process = Process.read(Long.valueOf(params.processId))
            log.info("attachment.processId : ${attachment.processId}")
            attachment.save(flush:true)
            render "";
        }
        else{
            render(template:"message/errorMessage", model:[message:msg]);
        }
    }

    def deleteFile(){
        attachmentService.deleteFile(params.id,params.processId);
        getAttachmentList()
    }

    def getAttachmentList(){
        log.info(params)
        render(template:"../attachment/list", model:[files:attachmentService.getListByProcessId(params.processId), processId: params.processId]);
    }

    def getDocumentPage() {
        def process = Process.get(Integer.valueOf(params.processId));
        String path = pdfService.getImageFromPDFDocumentFile(process.documents,
                params.processId as String,
                Integer.valueOf(params.pageNumber));
        render(text: path)
    }

    def getBankName() {
        String accountNumber = params.accountNo
        String shortAccountNumber = accountNumber.substring(2, 10)

        def bankData = cbdService.getNazwaBanku(shortAccountNumber)
        if (bankData != null) {
            JSONObject data = new JSONObject()
            data.put("id", bankData.get("id"))
            data.put("name", bankData.get("name"))
            render(text: data.toString())
        }
        render(text: '')
    }

    def getCity() {
        String code = params.code
        def result = []
        def citiesData = cbdService.getMiasto(code)
        if (citiesData) {
            citiesData.each { GroovyRowResult row ->
                result.push("\""+row.get("NAME")+"\"")
            }
        }
		log.info "Dla kodu: " + code + " znaleziono miasta: " + result.toString()
        render(text: result.toString())
    }

    def getOpiekaSerwisowa() {
        String code = params.code
        def result = [:]
        def opiekaOne = cbdService.getOpiekaSerwisowaOne(code)
        def opiekaTwo = cbdService.getOpiekaSerwisowaTwo(code)
        result.put('opiekaOneCode', opiekaOne ? opiekaOne[0] : '')
        result.put('opiekaTwoCode', opiekaTwo ? opiekaTwo[0] : '')
        render result as JSON
    }

    def getRodzajDzialalnosci() {
        String mcc = params.mcc
        def result = cbdService.getRodzajDzialalnosciByMCC(mcc)
        if (result != null) {
            JSONObject data = new JSONObject()
            data.put("id", result.slm_nazwa)
            render(text: data.toString())
        }
        render(text: '')
    }

    def getTerminalModels(){
        log.info( "getTerminalModels nip = " +  params.nip + ", type: " + params.type);

        render dictionaryService.getCalculatorDevicesTypes(params.type) as JSON
    }

	def downloadDoc(){
		log.info( "downloadDoc = " +  params.id);
		DocumentFile file = documentService.download(params.id)

		if(!(file?.content?.content)){
			redirect(action: "show")
		}

		response.setContentType("application/pdf")
		response.setHeader("Content-disposition", "${params.contentDisposition}; filename=\"${file.name}\"")
		response.outputStream << PdfGenerator.closeContent(file.content.content)
	}

    def testSql(){
        /*   def result = cbdService.findCalculatorByNip("1570321560")
           log.info("findCalculatorByNip result:"+result)*/

        def process = new Process()
        process.save(flush:true)

        process.status = Process.ProcessStatus.EDIT;
        process.save(flush:true)
    }


//--------------
//PRIVATE METHODS
//--------------

    def  _getActivePanels(def signatures) {
        def orderComparator = [
                compare: { Panel a, Panel b -> a.orderNo <=> b.orderNo }
        ] as Comparator

        def activePanels = new TreeSet(orderComparator)
        activePanels.addAll(signatures*.panelsSignature.flatten().findResults {
            it != "null" && it != "[]" ? it.panel : null
        })

        return activePanels;
    }

    Set<Signature> _getSignatures(def activities) {
        Set<Signature> signatures = []
        activities.each() { activity ->

            def activitySignatureParam = params["activitySignature_${activity.id}"];

            if (activitySignatureParam != null) {

                def activitySignaturesIdsMap = ([activitySignatureParam].flatten().findAll { it != "null" && it != "[]" })

                def activitySignaturesIdsList = [];
                activitySignaturesIdsMap.each { item ->
                    def evalItem = Eval.me(item);
                    if (evalItem instanceof ArrayList) {
                        activitySignaturesIdsList.addAll(evalItem)
                    } else {
                        activitySignaturesIdsList.add(evalItem)
                    }
                }

                def activitySignatures = ActivitySignatures.findAllByIdInList(activitySignaturesIdsList.findResults { new Long(it) });

                if (!signatures.contains(activitySignatures*.signature)) {
                    signatures.addAll(activitySignatures.signature)
                }

                activity.selectedActivitySignatures = activitySignatures;
            }
        }
        return signatures;
    }

    def _processDocumentCreation(Process process, String requestVersion, def requiredNumberOfSubscriptions)	{

        if (ELECTRIONICAL.equals(requestVersion)) {
            if (params?.numberOfSubscriptions?.toInteger() == requiredNumberOfSubscriptions) {
                process.documents.findAll{!it.signature.hasPurpose(SignatureDetail.SignaturePurpose.REPRESENTATIVE)}.each { DocumentFile doc ->
                    pdfService.updateDataUmowyOnDocument(doc, process)

                    byte[] newContent = pdfService.getDocumentWithSubscriptions(doc, process.subscriptions)
                    doc.content.content = newContent
                    doc.content.discard()
                }

                log.info "ELECTRONICAL VERSION for process " + process.id
                pdfService.addSubscriptionsToRepresentativeDocuments(process)

                log.info "ELECTRONICAL VERSION for process " + process.id

                Map mailBodyParams = processService.createMailParametersForElectronicalVersion(process)
                String recipient = mailBodyParams.recipient

                List<DocumentFile> documents = process.documents?.findAll{it.signature?.sendToClient}

                def recipients = [];
                def user = springSecurityService.principal
                if (user.email) {
                    recipients.add(user.email)
                } else {
                    log.info 'Brak emaila dla zalogowanego usera.'
                }

                if (processService.isProcessHasActivity(process, "wymianaTerminala")){
                    emailService.sendDocumentsElectronicalVersion(recipients, documents, mailBodyParams)
                } else {
                    if (recipient){
                        recipients.add(recipient)

                        Boolean isNewAgreement = processService.isProcessHasActivity(process, "nowaUmowa")
                        if (isNewAgreement){
                            emailService.sendDocumentsElectronicalVersion(recipients, documents, mailBodyParams)
                        } else {
                            emailService.sendDocumentsNotNewAggrementElectronicalVersion(recipients, documents, mailBodyParams)
                        }
                    } else {
                        emailService.sendDocumentsAcceptedToPostSend(process.documents, mailBodyParams)
                    }
                }
            }
        } else if (PAPER.equals(requestVersion)) {
            process.documents.each { DocumentFile df ->
                DocumentContent ndc = pdfService.cleanAgrementDateContent(df.content);
                ndc.setDocument(df)
                ndc.save(flush: true)
                df.setContent(ndc);
                df.save(flush: true)
            }

            log.info "PAPER VERSION for process" + process.id

            Map mailBodyParams = processService.createMailParametersForPaperVersion(process)
            List<DocumentFile> documents = process.documents?.findAll{it.signature?.sendToClient}

            def isWymianaTerminala = processService.isProcessHasActivity(process, "wymianaTerminala")
            if (isWymianaTerminala){
                def user = springSecurityService.principal
                if (user.email) {
                    emailService.sendDocumentsPaperVersion(user.email, documents, mailBodyParams)
                } else {
                    log.info 'Brak emaila dla zalogowanego usera.'
                }
            } else {
                emailService.sendDocumentsPaperVersion(documents, mailBodyParams)
            }
        } else if (TEMPLATES.equals(requestVersion)) {
			List<DocumentFile> documentFilesWithBlackFaksymileList = new ArrayList<DocumentFile>()
			process.documents.findAll{it.signature?.sendToClient}?.each { DocumentFile doc ->
                pdfService.updateDataUmowyOnDocument(doc, process)

				DocumentFile dfwbf = new DocumentFile(name: doc.name, clientName: doc.clientName , dateCreated: doc.dateCreated, lastUpdated: doc.lastUpdated, pagesCount: doc.pagesCount)
				byte[] newContent = pdfService.addBlackFaksymileToDocument(doc.content.content, doc.signature.id)
				dfwbf.setContent(new DocumentContent(content: newContent))
				dfwbf.discard()
				documentFilesWithBlackFaksymileList.add(dfwbf)
			}

            log.info "TEMPLATE VERSION for process" + process.id

            Map mailBodyParams = processService.createMailParametersForElectronicalVersion(process)

            String recipient = mailBodyParams.recipient
            def isWymianaTerminala = processService.isProcessHasActivity(process, "wymianaTerminala")
            if (isWymianaTerminala){
                def user = springSecurityService.principal
                if (user.email) {
                    recipient = user.email
                } else {
                    log.info 'Brak emaila dla zalogowanego usera.'
                }
            } else {
                recipient = mailBodyParams.recipient
            }

            if (recipient){
                emailService.sendDocumentsTemplateVersion(recipient, documentFilesWithBlackFaksymileList, mailBodyParams)
            }
    	}
    }

    def _getNewProcessStatus(def params, def requiredNumberOfSubscriptions) {
        Process.ProcessStatus newStatus

        if (ELECTRIONICAL.equals(params?.requestVersion)) {
            if (params?.numberOfSubscriptions?.toInteger() < requiredNumberOfSubscriptions) {
                newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
            } else {
                newStatus = Process.ProcessStatus.WAITING
            }
        } else if (PAPER.equals(params?.requestVersion)) {
            newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION
        } else if (TEMPLATES.equals(params?.requestVersion)) {
            newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
        }

        return newStatus
    }

    private def crateProcessCommand(def params, def calc) {
        def cmd = new ProcessCommand()
        bindData(cmd,params)
        cmd.calc = calc
        cmd.calculatorService = calculatorService
        addCalculatorFields(cmd.points, calc)
        addCalculatorFields(cmd.poses, calc)
        addCalculatorFields(cmd.hirePaymentsByPoint, calc)
        addCalculatorFields(cmd.hirePaymentsByPos, calc)
        cmd
    }

    private def addCalculatorFields(def commands, def calc){
        commands?.each{
            if (it != null){
                it.calc = calc
                it.calculatorService =calculatorService
            }
        }
    }

    private void setRepresentatives(Map flow) {
        flow.representative1 = flow.representative1 != null ? flow.representative1 : processService.getRepresentative(flow.processInstance, 0)
        flow.representative2 = flow.representative2 != null ? flow.representative2 : processService.getRepresentative(flow.processInstance, 1)
    }

}