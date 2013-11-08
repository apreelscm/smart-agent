
package com.eservice.eumowy

import groovy.sql.GroovyRowResult

import org.codehaus.groovy.grails.web.json.JSONObject

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
    def messageSource
    def pdfService

    def springSecurityService

    def index() {
        redirect(action: "createProcess", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [activityInstanceList: Activity.list(params), activityInstanceTotal: Activity.count()]
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
                if (params.message != null) {
                    flow.prevActivityMessage = params.message
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
                def processInstance = flow.processInstance
                def notes = processInstance.notesToCoa
                log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${notes}]")

                def user = springSecurityService.principal
                if(!emailService.sendNotesToCOA(notes, user.nr, user.imie + ' ' + user.nazwisko)){
                    return error()
                }
            }
            on("error"){
                flash.errorMessage = message(code:"email.send.error", default:"Wystąpił błąd podczas wysyłania wiadomości");
            }to "defineActivity"
            on("success"){
                flow.prevActivityMessage = message(code:"email.notesToCOA.send.complete", default:"Wysłano wiadomość z uwagami do COA");
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
                def processInstance = flow.processInstance
                flow.representative1 = flow.representative1 != null ? flow.representative1 : processService.getRepresentative1(processInstance)
                flow.representative2 = flow.representative2 != null ? flow.representative2 : processService.getRepresentative2(processInstance)
                flow.requiredNumberOfSubscriptions = 1 //PH subscription is always required

                if (flow.representative1.name != null && flow.representative1.surname != null) {
                    flow.requiredNumberOfSubscriptions++
                }

                if (flow.representative2.name != null && flow.representative2.surname != null) {
                    flow.requiredNumberOfSubscriptions++
                }

                if (!flow.skipDocumentGeneration) {
                    def processWithPages = pdfService.workWithDocuments(processInstance, conversation.calc)
                    flow.totalPagesCount = processWithPages.totalPagesCount
                    //processInstance.discard()
                    //processInstance.save(flush: true)
                    processInstance = processWithPages.processInstance
                    processInstance.save(flush: true)
                    //flow.processInstance = processInstance

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
                    clientNip: flow.rejectedDocumentsMessage
            ])
            on("back"){
                flow.newProcessFlow = false
            }to "chooseSubFlow"
            on("subscribe").to "clientSignature"
            on("updateProcessStatus") {
                log.info params
                def processInstance = flow.processInstance
                if (params.processStatus.equals("WAIT_FOR_SUBSCRIPTION")) {
                    processInstance.status = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
                    Subscription sub = Subscription.get(params.subscriptionId)
                    if (sub == null) {
                        log.info "PUSTE ID!"
                    }
                    processInstance.addToSubscriptions(sub)
                    sub.save()
                }
                else if (params.processStatus.equals("SUBSCRIPTIONS_DONE")) {
                    processInstance.status = Process.ProcessStatus.SUBSCRIPTIONS_DONE
                    Subscription sub = Subscription.get(params.subscriptionId)
                    if (sub == null) {
                        log.info "PUSTE ID!"
                    }
                    processInstance.addToSubscriptions(sub)
                    //      processInstance.discard()
                    sub.save()

                    //w momencie gdy zlozymy ostatni podpis zapisujemy date jako dataUmowy
                    def aggrementDate = DateUtils.formatWithTimezone(DateUtils.getCurrentDate());
                    log.info 'Zapisuje formatowana dateUmowy: ' + aggrementDate

                    def aggrementDateProcessData = processInstance.processData?.find{ pData -> 'dataUmowy'.equals(pData.name)};
                    if (aggrementDateProcessData){
                        aggrementDateProcessData.value = aggrementDate
                    } else {

                        def dataUmowyPD = new ProcessData(name: 'dataUmowy', value: aggrementDate)

                        ProcessData foundData = processInstance.processData.find { it.name == dataUmowyPD.name }
                        if(!foundData){
                            processInstance.addToProcessData(dataUmowyPD)
                        }else if(dataUmowyPD.value != foundData.value){
                            foundData.value = dataUmowyPD.value
                        }

                    }
                }
                else if (params.processStatus.equals("REJECTED")){
                    processInstance.status = Process.ProcessStatus.REJECTED
                }

                if (!processInstance.save(flush:true)){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.skipDocumentGeneration = true
                flow.processInstance = processInstance
            }.to "clientSignature"
            on("noaccept") {
                def processInstance = flow.processInstance
                processInstance.status = Process.ProcessStatus.REJECTED
                flow.processInstance = processInstance
            }.to "finish"
            on("submit") {
                log.info "PARAMS: " + params
                def processInstance = flow.processInstance
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
                    log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${processInstance.notesToCoa}]")
                    def user = springSecurityService.principal
                    emailService.sendNotesToCOA(processInstance.notesToCoa, user.nr, user.imie + ' ' + user.nazwisko)
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
                def processInstance = flow.processInstance

                //SIGNATURES
                def signatures =  _getSignatures(processInstance.activities)

                log.info("signatures:"+signatures);

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

                processInstance.calcNumber =  flow.calcNumber

                Client client = flow.client
                log.info("flow client id:"+client)
                if (!client.id && !client.save(flush:true)){
                    client.errors.each { log.error(it) }
                    return "error"
                }

                def user = springSecurityService.principal
                processInstance.phNumber = user.nr
                processInstance.phFirstName = user.imie
                processInstance.phSurname = user.nazwisko
                processInstance.phEmail = user.email
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

                def processInstance = flow.processInstance

                if(!clientService.isClientNipValid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);
                log.info(flow?.processInstance)
                /** pobranie wartości, czy to jest nowa umowa*/
                def hasNowaUmowa = processService.containsActivity(processInstance.activities,"nowaUmowa")

                if(client?.cbdId){
                    /** sprawdzanie, czy to nie jest nowa umowa dla klienta CBD*/
                    if(hasNowaUmowa){
                        flash.nipErrorMessage = message(code:"client.newAgreementAndClientCBD.error", default:"Nowa umowa dla klienta CBD");
                        log.info(message(code:"client.newAgreementAndClientCBD.error") + " - " + flow.nip)
                        return error();
                    }
                    else{
                        flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono klienta w CBD");
                    }

                }else {
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    if(hasNowaUmowa){
                        flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                        client = new Client(nip:params.nip)
                    }else{
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        log.info(message(code:"client.notFound.error") + " - " + flow.nip)
                        return error();
                    }
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                def lastProcess = processService.getLastProcessWhenNotInStatus(params.nip,[Process.ProcessStatus.REJECTED,Process.ProcessStatus.ACCEPTED])
                if(lastProcess){
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                    lastProcess?.discard() // drop it from session
                    return error();
                }
                lastProcess?.discard() // drop it from session

                /** pobieranie danych o kalkulatorze */
                def calcId = cbdService.findCalculatorIdByNip(client.nip)

                if(!calcId){
                    flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                    return error();
                }

                def calc = cbdService.findCalculatorByNip(client.nip)

                if(calc == []){
                    flash.calcErrorMessage = message(code:"calc.fetch.error", default:"Wystąpił błąd podczas próby pobrania kalkulatora");
                    return error();
                }

				// Validation 1 & 2
                if(!calculatorService.isCalcValid(calc,calcId,processInstance)){
                    flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                    return error();
                }
				

                conversation.calc = calc
                flow.calcNumber =  calcId;
                flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");
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
                println "selectedPanels enterview"
                def processInstance = flow.processInstance;

                def processCmd
                if(!flow.skipPanelsInit) {
                    log.info("skipPanelsInit - false")
                    TreeSet activePanels = _getActivePanels(processInstance.signatures)
                    processInstance.panels = activePanels.toList();
                    processCmd = processService.getNewProcessCommand(processInstance, conversation.calc)

                    //inicjacyjne zapisanie danych pobranych z cbd i calc
                    processInstance = processService.populateProcessWithData(processInstance,processCmd)

                    if (!processInstance.save()){
                        processInstance.errors.each { log.error(it) }
                        return error();
                    }

                    // Update
                    processInstance.points?.each { point ->
                        if (point.cbdId != null) {
                            def foundApc = processCmd.allPoints?.find { apc -> apc.cbdId == point.cbdId }
                            if (foundApc != null) {
                                foundApc.id = point.id
                            }
                        }
                    }
                }
                else{
                    log.info("skipPanelsInit - true")
                    flow.skipPanelsInit = false
                    processCmd = processService.getSavedProcessCommand(processInstance, conversation.calc, flow.newProcessFlow)
                    processCmd.nip = processInstance.client.nip
                }
				
				// Calculate pos count from cbd
				def counter = 0
				processCmd.liczbaPosZCbd = 0
				processCmd.allPoints?.each { allPoint ->
					if (allPoint.cbdId != null) {
						counter += allPoint?.liczbaPos != null ? allPoint?.liczbaPos : 0
					}
				}
				processCmd.liczbaPosZCbd = Integer.valueOf(processCmd.liczbaPosZCbd) != null ? Integer.valueOf(processCmd.liczbaPosZCbd) + counter : counter
				
                flow.data = processCmd
                flow.processInstance = processInstance
            }
            render(view: "../createProcess/selectedPanels")
            on("reject"){
                def processInstance = flow.processInstance;
                processInstance.status = Process.ProcessStatus.REJECTED;
                flow.processInstance = processInstance
            }.to "reject"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("deletePoint") {
                def processInstance = flow.processInstance
				def cmd = flow.data
                def point = PointData.get(Integer.valueOf(params.pointId));
                if (point != null) {
                    log.info "DeletePoint - Usuwam punkt o id: " + params.pointId
                    processInstance.removeFromPoints(point)
                    point.delete()
                    processInstance.save(flush: true)
					
					cmd?.points?.removeAll { it.id == point.id }
                }
                else {
                    log.info "DeletePoint - Nie znalazłem punktu o id: " + params.pointId
                }
				flow.data = cmd
                flow.processInstance = processInstance
            }.to "saveOnly"
            on("saveOnly"){ ProcessCommand cmd ->
                log.info params
                log.info params.get('allPoses[0]')
                Process processInstance = processService.populateProcessWithData(flow.processInstance, cmd)
                log.info "Zapisuje dane paneli"

                //TEST start
                /*  cmd.calc = conversation.calc
                  cmd.calculatorService = calculatorService
                  cmd.validate()
                  flow.data = cmd
                  if(cmd.hasErrors()){
                      cmd.errors.each {
                          log.error(it)
                      }
                      return error();
                  }*/
                //TEST end

                processInstance.save(flush: true, validate: false)

                log.info "Zapisano dane paneli"

                // Update
                processInstance.points?.each { point ->
                    if (point.cbdId != null) {
                        def foundApc = cmd.allPoints?.find { apc -> apc.cbdId == point.cbdId }
                        foundApc?.id = point.id
                    }
                }

                flow.processInstance = processInstance
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){
                def cmd = crateProcessCommand(params, conversation.calc)
                cmd.validate()
                flow.data = cmd

                if(grailsApplication.config.isPanelsValidationOn && cmd.hasErrors()){
                    cmd.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                def processInstance = flow.processInstance

                clientService.updateClientName(processInstance.client, cmd)
                processInstance = processService.populateProcessWithData(processInstance,cmd)
                processInstance.notesToCoa = cmd.notes;

                processInstance.client.name = cmd.akceptantNazwaOficjalna;
                processInstance.saleSection = calculatorService.getCalcProperty(conversation.calc,'SEGMENT_SPRZEDAZOWY')

                flow.representative1 = [name: cmd.reprezentant1Imie, surname: cmd.reprezentant1Nazwisko]
                flow.representative2 = [name: cmd.reprezentant2Imie, surname: cmd.reprezentant2Nazwisko]

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.processInstance = processInstance

            }.to "clientSignature"
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
                def processInstance = flow.savedProcess

                log.info("POPRAW DANE - wczytanie procesu - nip = ${flow.nip} , processId = ${processInstance?.id}, status = ${processInstance?.status}")

                def user = springSecurityService.principal
                processInstance.phNumber = user.nr
                processInstance.phFirstName = user.imie
                processInstance.phSurname = user.nazwisko
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
                flow.nip = params.nip;

                if(!clientService.isClientNipValid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);
                def lastProcess = processService.getLastProcessForClient(params.nip)

                log.info("POPRAW DANE - Znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(! client?.cbdId){
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    def hasNowaUmowa = processService.containsActivity(lastProcess.activities,"nowaUmowa")
                    if(hasNowaUmowa){
                        flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                        client = new Client(nip:params.nip)
                    }else {
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        log.info(message(code:"client.notFound.error") + " - " + flow.nip)
                        return error();
                    }
                }
                else {
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono klienta w CBD");
                }


                if (! lastProcess?.id){
                    /* brak otwartego procesu */
                    flash.nipErrorMessage = message(code:"process.openNotFound.error",
                            default:"Brak otwartego procesu dla Akceptant" + flow.nip);
                    log.info(message(code:"process.openNotFound.error") + " - " + flow.nip)
                    return error()
                }
                else if (Process.ProcessStatus.ACCEPTED.equals(lastProcess?.status)){
                    /* ostatni proces jest zaakceptowany */
                    flash.nipErrorMessage = message(code:"process.youngerAcceptedProcess.error",
                            default:"Brak możliwości poprawy danych, istnieją inne nowsze zaakceptowane procesy dla tego Akceptanta");
                    log.info(message(code:"process.youngerAcceptedProcess.error") + " - " + flow.nip)
                    return error()
                }

                /** pobieranie danych o kalkulatorze */
                def calcId = cbdService.findCalculatorIdByNip(client.nip)

                if(!calcId){
                    flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                    log.info(message(code:"calc.notFound.error") + " - " + client.nip)
                    return error()
                }

                def calc = cbdService.findCalculatorByNip(client.nip)

                log.info("pobrano kalkulator " + calcId)

                if(!calculatorService.isCalcValid(calc,calcId,lastProcess)){
                    flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                    return error()
                }
                lastProcess?.discard() // drop it from session

                conversation.calc = calc
                flow.calcNumber =  calcId;
                flow.savedProcess = lastProcess

                // aktualizacja danych klienta w procesie danymi z CBD
                lastProcess.client = client
                flow.client = lastProcess.client;

                flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");
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
                def processInstance = flow.processInstance;
                def processCmd = processService.getSavedProcessCommand(processInstance, conversation.calc, flow.newProcessFlow);
				
				// Calculate pos count from cbd
				def counter = 0
				processCmd.liczbaPosZCbd = 0
				processCmd.allPoints?.each { allPoint ->
					if (allPoint.cbdId != null) {
						counter += allPoint?.liczbaPos != null ? allPoint?.liczbaPos : 0
					}
				}
				processCmd.liczbaPosZCbd = Integer.valueOf(processCmd.liczbaPosZCbd) != null ? Integer.valueOf(processCmd.liczbaPosZCbd) + counter : counter
				
                flow.data = processCmd
            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("deletePoint") {
                def processInstance = flow.processInstance
				def cmd = flow.data
                def point = PointData.get(Integer.valueOf(params.pointId));
                if (point != null) {
                    log.info "DeletePoint - Usuwam punkt o id: " + params.pointId
                    processInstance.removeFromPoints(point)
                    point.delete()
                    processInstance.save(flush: true)
					
					cmd?.points?.removeAll { it.id == point.id }
                }
                else {
                    log.info "DeletePoint - Nie znalazłem punktu o id: " + params.pointId
                }
				flow.data = cmd
                flow.processInstance = processInstance
            }.to "selectedPanels"
            on("saveOnly"){ ProcessCommand cmd ->
                Process processInstance = processService.populateProcessWithData(flow.processInstance,cmd)

                processInstance.save(flush: true, validate: false)

                log.info "Zapisano dane paneli"

                // Update
                processInstance.points?.each { point ->
                    if (point.cbdId != null) {
                        def foundApc = cmd.allPoints?.find { apc -> apc.cbdId == point.cbdId }
                        foundApc?.id = point.id
                    }
                }

                flow.processInstance = processInstance
                //  flow.data = cmd
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){
                def cmd = crateProcessCommand(params, conversation.calc)
                cmd.validate()
                flow.data = cmd

                if(cmd?.hasErrors()){
                    log.info(params)
                    return error();
                }

                def processInstance = flow.processInstance

                //clientService.updateClientName(processInstance.client, cmd)
                processInstance = processService.populateProcessWithData(processInstance,cmd)
                processInstance.notesToCoa = cmd.notes;

                flow.representative1 = [name: cmd.reprezentant1Imie, surname: cmd.reprezentant1Nazwisko]
                flow.representative2 = [name: cmd.reprezentant2Imie, surname: cmd.reprezentant2Nazwisko]

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                /* Delete subscriptions */
                processInstance.subscriptions?.clear()
                flow.processInstance = processInstance
            }.to "clientSignature"
            on("reject"){
                def processInstance = flow.processInstance;
                processInstance.status = Process.ProcessStatus.REJECTED;
                flow.processInstance = processInstance
            }.to "reject"
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
            }.to "clientSignature"
        }

        getCalculator {
            action {
                flow.nip = params.nip;

                def processInstance = flow.processInstance

                if(!clientService.isClientNipValid(params.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);
                if(client?.cbdId){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono klienta w CBD");
                }

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta Proces w statusie oczekiwania na podpis */
                def lastProcess = processService.getLastProcessForClient(params.nip)
                log.info("UZUPELNIJ PODPISY - znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(! Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"client.addSignatures.error",
                            default:"Brak możliwości uzupełnienia podpisów");
                    log.info(message(code:"client.addSignatures.error") + " - " + flow.nip)
                    return error()
                }

                flow.savedProcess = lastProcess
                flow.client = lastProcess.client

            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.omitting.info", default:"Pomijanie kalkulatora");
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
                flow.nip = params.nip;

                if(!clientService.isClientNipValid(params.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);
                if(client?.cbdId){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono klienta w CBD");
                }

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta jakis Proces */
                def lastProcess = processService.getLastProcessForClient(params.nip)
                log.info("ODRZUCENIE PROCESU - znaleziono proces - nip = ${flow.nip} , processId = ${lastProcess?.id}, status = ${lastProcess?.status}")

                if(! lastProcess?.id){
                    flash.nipErrorMessage = message(code:"client.eUmowyNotFound.error", default:"Brak klienta w eUmowy");
                    log.info(message(code:"client.eUmowyNotFound.error") + " - " + flow.nip)
                    return error();
                }
                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                else if (Process.ProcessStatus.ACCEPTED.equals(lastProcess?.status)){
                    /* ostatni proces jest zaakceptowany */
                    flash.nipErrorMessage = message(code:"process.rejectAccepted.error",
                            default:"Nie można odrzucić dokumentów już zaakceptowanych")
                    log.info(message(code:"process.rejectAccepted.error") + " - " + flow.nip)
                    return error()

                }
                else if (Process.ProcessStatus.REJECTED.equals(lastProcess?.status)){
                    flash.nipErrorMessage = message(code:"process.openNotFound.error",
                            default:"Brak otwartego procesu dla tego Akceptanta")
                    log.info(message(code:"process.openNotFound.error") + " - " + flow.nip)
                    return error()
                }
                flow.savedProcess = lastProcess
                flow.client = lastProcess.client;
            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.omitting.info", default:"Pomijanie kalkulatora");
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
        def msg = attachmentService.uploadFile(config,request, messageSource);

        if(msg instanceof AttachmentFile){
            def attachment = msg as AttachmentFile
            attachment.process = Process.read(Long.valueOf(params.processId))
            println("attachment.processId : ${attachment.processId}")
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
        //println(params)
        render(template:"../attachment/list", model:[files:attachmentService.getListByProcessId(params.processId), processId: params.processId]);
    }

    def getDocumentPage() {
        def process = Process.get(Integer.valueOf(params.processId));
        String path = pdfService.generateImageFromPDFDocumentFile(process.documents,
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

        def citiesData = cbdService.getMiasto(code)
        if (citiesData) {
            def result = []
            citiesData.each { GroovyRowResult row ->
                result.push("\""+row.get("NAME")+"\"")
            }
            render(text: result.toString())
        }
        render(text: '')
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

    def _getSignatures(def activities) {
        def signatures = []
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

                process.documents.each { DocumentFile doc ->
                    byte[] newContent = pdfService.addClientSubscriptionsToDocument(doc.content.content, doc.signature.id, process.subscriptions)
                    doc.content.content = newContent
                    doc.content.discard()
                }

                def recipient = getFromProcessData(process, 'kontaktEmail') ?: getFromProcessData(process, 'emailDoWysylkiDokumentu')

                if (recipient){
                    emailService.sendDocumentsElectronicalVersion(recipient, process.documents)
                } else {
                    def merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna');
                    def merchantNip = getFromProcessData(process, 'nip');
                    emailService.sendDocumentsAcceptedToPostSend(process.documents, merchantName, merchantNip)
                }
            }
        }
        else if (PAPER.equals(requestVersion)) {
            //Documents are already in DB
            def merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna');

            process.documents.each{ DocumentFile df ->
                DocumentContent ndc = pdfService.cleanAgrementDateContent(df.content);
                ndc.setDocument(df)
                ndc.save(flush: true)
                df.setContent(ndc);
                df.save(flush: true)
            }
            emailService.sendDocumentsPaperVersion(process.phEmail, process.documents, merchantName)
        }
        else if (TEMPLATES.equals(requestVersion)) {
            //Documents are already in DB
            List<DocumentFile> documentFilesWithBlackFaksymileList = new ArrayList<DocumentFile>()
            List<DocumentFile> documentFilesWithoutFaksymileList = new ArrayList<DocumentFile>()

            process.signatures.each { sig ->
                // Generate documents with black faksymile for PH
                byte[] documentDataWithBlackFaksymile = pdfService.fillPdfFormFromURIWithBlackFaksymile(sig.id, null, PdfService.FontType.ARIAL)
                DocumentFile dfwbf = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: 0)
                dfwbf.setContent(new DocumentContent(content: documentDataWithBlackFaksymile))
                dfwbf.discard()
                documentFilesWithBlackFaksymileList.add(dfwbf)

                // Generate documents without faksymile for acceptant
                byte[] documentDataWithoutFaksymile = pdfService.fillPdfFormFromURIWithoutFaksymile(sig, null, PdfService.FontType.ARIAL)
                DocumentFile dfwof = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: 0)
                dfwof.setContent(new DocumentContent(content: documentDataWithoutFaksymile))
                dfwof.discard()
                documentFilesWithoutFaksymileList.add(dfwof)
            }

            emailService.sendDocumentsTemplateVersion(process.phEmail, documentFilesWithBlackFaksymileList)

            //for acceptant
            def recipientUser = getFromProcessData(process, 'kontaktEmail')
            if(recipientUser != ""){
                emailService.sendDocumentsTemplateVersion(recipientUser, documentFilesWithoutFaksymileList)
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

    def getFromProcessData(def process, def key){
        def result = process.processData.find{ pd -> pd.name.equals(key)}
        return (result && result?.value)?result?.value:""
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
            it.calc = calc
            it.calculatorService =calculatorService
        }
    }

}