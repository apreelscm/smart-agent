package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.PdfMapper
import com.eservice.eumowy.process.DefineActivityCommand
import com.eservice.eumowy.util.DateUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.codehaus.groovy.grails.web.json.JSONObject

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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
            action{
                log.info("init new flow")
                flow.isGoBack = false;
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
                try{
                    def user = springSecurityService.principal
                    emailService.sendNotesToCOA(notes, user.nr, user.imie + ' ' + user.nazwisko)
                    log.info(flash.infoMessage)
                }catch (Exception error){
                    log.info(flash.errorMessage)
                    error.printStackTrace();
                    return error()
                }
            }
            on("error"){
                flash.errorMessage = message(code:"email.send.error", default:"Wystąpił błąd podczas wysyłania wiadomości");
            }to "defineActivity"
            on("success"){
                flash.infoMessage = message(code:"email.notesToCOA.send.complete", default:"Wysłano wiadomość z uwagami do COA");
            }to "defineActivity"
        }

        /** default full subflow */
        normal {
            subflow(action: "normal", input: [processInstance : { flow.processInstance }, newProcessFlow : {flow.newProcessFlow}])
            on("backToStart"){
                flow.isGoBack = true;
            }to "defineActivity"
            on("clientSignature") {
                flow.processInstance = currentEvent.attributes.process
                flow.representative1 = currentEvent.attributes.representative1
                flow.representative2 = currentEvent.attributes.representative2
            }.to "clientSignature"
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
            }.to "clientSignature"
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
					def totalPagesCount = 0
	                def data = new PdfMapper().mapAllDataToPDFData(processInstance.processData, processInstance.points)

                    data.each{ key, value ->
                        println 'DO GENERATORA PDF -> ' + key + ' = ' + value
                    }


                    processInstance.signatures.each { sig ->
	                    log.info "SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath
	
	                    byte[] documentData = pdfService.fillPdfFormFromURIWithFaksymile(sig, data, PdfService.FontType.ARIAL)
	
	                    if(!documentData) return
	
	                    int pc = pdfService.getPageCountFromPdf(documentData)
	                    totalPagesCount += pc

//                        try {
//                            log.info "Saving " + sig.name + " to disk - start"
//                            ByteArrayInputStream bis = new ByteArrayInputStream(documentData)
//                            PDDocument document = PDDocument.load(bis)
//                            document.save('/home/tomcat/aaa/'+sig.templatePath)
//                            document.close();
//                            log.info "Saving " + sig.name + " to disk - finish"
//                        } catch (Exception e){
//                            log.info("Nie udalo sie zapisac pliku do katalgu!!!! " + sig.templatePath)
//                        }


	                    if (processService.findDocumentByName(processInstance.documents, sig.templatePath) == null) {
	                        log.info "Creating new document [${sig.templatePath}]"
	                        DocumentFile df = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc)
	                        df.setContent(new DocumentContent(content: documentData))
	                        df.save(flush: true)
	                        log.info "DF id: " + df.id + " PageCount: " + df.pagesCount
	                        log.info "Process ID: " + processInstance.id
	                        processInstance.addToDocuments(df)
	                        processInstance.discard();
	                    }
	                    else {
	                        log.info "Updating existing document [${sig.templatePath}]"
	                        DocumentFile df = processService.findDocumentByName(processInstance.documents, sig.templatePath)
	                        df.content.setContent(documentData)
	                        df.save(flush: true)
	                    }
	                }
	
	                flow.totalPagesCount = totalPagesCount;
	                processInstance.save(flush:true)
	                flow.processInstance = processInstance
				}
				flow.skipDocumentGeneration = false
            }
            render(view: "../createProcess/clientSignature", model: [
                    processInstance: flow.processInstance,
                    totalPagesCount: flow.totalPagesCount,
                    representative1: flow.representative1,
                    representative2: flow.representative2,
					requiredNumberOfSubscriptions: flow.requiredNumberOfSubscriptions
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
                    processInstance.addToSubscriptions(sub)
                }
                else if (params.processStatus.equals("SUBSCRIPTIONS_DONE")) {
					processInstance.status = Process.ProcessStatus.SUBSCRIPTIONS_DONE
                    Subscription sub = Subscription.get(params.subscriptionId)
                    processInstance.addToSubscriptions(sub)

                    //w momencie gdy zlozymy ostatni podpis zapisujemy date jako dataUmowy
                    def aggrementDate = DateUtils.formatWithTimezone(DateUtils.getCurrentDate());
                    log.info 'Zapisuje formatowana dateUmowy: ' + aggrementDate

                    def aggrementDateProcessData = processInstance.processData?.find{ pData -> 'dataUmowy'.equals(pData.name)};
                    if (aggrementDateProcessData){
                        aggrementDateProcessData.value = aggrementDate
                    } else {
                        processInstance.addToProcessData(new ProcessData(name: 'dataUmowy', value: aggrementDate))
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
                _processDocumentCreation(processInstance, params.requestVersion)
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
                redirect action: "createProcess", controller: "activity"
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

        chooseActivity{
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
                processInstance.client =  client
                processInstance.status = Process.ProcessStatus.NEW

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

                def processInstance = flow.processInstance

                if(!clientService.isClientNipValid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);

                if(client?.id){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    def hasNowaUmowa = processService.containsActivity(processInstance.activities,"nowaUmowa")
                    if(hasNowaUmowa){
                        flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                        client = new Client(nip:params.nip)
                    }else{
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        return error();
                    }
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                if(processService.getLastProcessNotStatus(client,[Process.ProcessStatus.REJECTED,Process.ProcessStatus.ACCEPTED])){
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                    return error();
                }

                /** pobieranie danych o kalkulatorze */
                def calcId = cbdService.findCalculatorIdByNip(client.nip)

                if(!calcId){
                    flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                    return error();
                }

                def calc = cbdService.findCalculatorByNip(client.nip)

                if(!calculatorService.isCalcValid(calc,processInstance.signatures)){
                    flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                    return error();
                }

                flow.calc = calc;
                flow.calcNumber =  calcId;
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
                println "selectedPanels enterview"
                def processInstance = flow.processInstance;
                def calc = flow.calc;

                def processCmd
                if(!flow.skipPanelsInit){
                    log.info("skipPanelsInit - false")
                    TreeSet activePanels = _getActivePanels(processInstance.signatures)
                    processInstance.panels = activePanels.toList();
                    processCmd = processService.getNewProcessCommand(processInstance,calc)

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
							foundApc.id = point.id
						}
					}
                }
                else{
                    log.info("skipPanelsInit - true")
                    flow.skipPanelsInit = false
                    processCmd = processService.getSavedProcessCommand(processInstance,calc)
                }

                flow.data = processCmd
                flow.processInstance = processInstance
            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("saveOnly"){ ProcessCommand cmd ->
                Process processInstance = processService.populateProcessWithData(flow.processInstance,cmd)

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.processInstance = processInstance
                flow.data = cmd
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){ ProcessCommand cmd ->

                flow.data = cmd;

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


                if(client?.id){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */

                def lastProcess = processService.getLastProcessNotStatus(client,[Process.ProcessStatus.ACCEPTED])
                if(!lastProcess){
                    flash.nipErrorMessage = message(code:"client.todo.error",
                            default:"Brak możliwości poprawy danych, istnieją inne nowsze zaakceptowane procesy dla tego Akceptanta");
                    return error()
                }

                /** pobieranie danych o kalkulatorze */
                def calcId = cbdService.findCalculatorIdByNip(client.nip)

                if(!calcId){
                    flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                    return error()
                }

                def calc = cbdService.findCalculatorByNip(client.nip)

                if(!calculatorService.isCalcValid(calc,lastProcess.signatures)){
                    flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                    return error()
                }

                flow.calc = calc;
                flow.calcNumber =  calcId;
                flow.savedProcess = lastProcess
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
                def calc = flow.calc;
                def processCmd = processService.getSavedProcessCommand(processInstance,calc);
                flow.data = processCmd
            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("saveOnly"){ ProcessCommand cmd ->
                Process processInstance = processService.populateProcessWithData(flow.processInstance,cmd)

                if (!processInstance.save()){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return error();
                }

                flow.processInstance = processInstance
                flow.data = cmd
                flow.skipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){ ProcessCommand cmd ->

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
                processInstance.calcNumber =  flow.calcNumber
                processInstance.client =  flow.client
                processInstance.save()
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

                if(client?.id){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flow.client = client;
                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                def lastProcess = processService.getLastProcessWithStatus(client, [Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION])
                if(!lastProcess){
                    flash.nipErrorMessage = message(code:"client.todo.error",
                            default:"Brak możliwości uzupełnienia podpisów.");
                    return error()
                }

                flow.savedProcess = lastProcess
            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.todo.info", default:"Pomijanie kalkulatora");
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


/** UZUPELNIJ PODPISY SUBFLOW */
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

                if(client?.id){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
                def lastProcess = processService.getLastProcessNotStatus(client,[Process.ProcessStatus.ACCEPTED,Process.ProcessStatus.REJECTED])
                if(!lastProcess){
                    flash.nipErrorMessage = message(code:"client.todo.error",
                            default:"Nie można odrzucić dokumentów już zaakceptowanych");
                    return error()
                }

                flow.savedProcess = lastProcess
            }
            on("success"){
                flash.calcInfoMessage = message(code:"calc.todo.info", default:"Pomijanie kalkulatora");
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
        attachmentService.deleteFile(params.id);
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

    def _processDocumentCreation(Process process, String requestVersion)	{

        if ("electronical".equals(requestVersion)) {
            //TODO Check signatures and update documents in DB

            process.documents.each { doc ->
                //TODO Update document content from Data Map
            }

            //TODO Send emails
            def recipient = getFromProcessData(process, 'kontaktEmail');
            if (recipient){
                emailService.sendDocumentsElectronicalVersion(recipient, process.documents)
            } else {
                def merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna');
                def merchantNip = getFromProcessData(process, 'nip');
                emailService.sendDocumentsAcceptedToPostSend(null, process.documents, merchantName, merchantNip)
            }

        }
        else if ("paper".equals(requestVersion)) {
            //Documents are already in DB
            def merchantName = getFromProcessData(process, 'akceptantNazwaOficjalna');

            //TODO PSZKUP - phEmail
            def recipientPh = "szkup.pawel@gmail.com"
            emailService.sendDocumentsPaperVersion(recipientPh, process.documents, merchantName)
        }
        else if ("templates".equals(requestVersion)) {
            //TODO Documents are already in DB
            List<DocumentFile> documentFilesWithBlackFaksymileList = new ArrayList<DocumentFile>()
            List<DocumentFile> documentFilesWithoutFaksymileList = new ArrayList<DocumentFile>()

            process.signatures.each { sig ->
                byte[] documentDataWithBlackFaksymile = pdfService.fillPdfFormFromURIWithBlackFaksymile(sig, null, PdfService.FontType.ARIAL)
                byte[] documentDataWithoutFaksymile = pdfService.fillPdfFormFromURIWithoutFaksymile(sig, null, PdfService.FontType.ARIAL)

                // Generate documents with black faksymile for PH
                DocumentFile dfwbf = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: 0)
                dfwbf.setContent(new DocumentContent(content: documentDataWithBlackFaksymile))
                dfwbf.discard()
                documentFilesWithBlackFaksymileList.add(dfwbf)

                // Generate documents without faksymile for acceptant
                DocumentFile dfwof = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: 0)
                dfwof.setContent(new DocumentContent(content: documentDataWithoutFaksymile))
                dfwof.discard()
                documentFilesWithoutFaksymileList.add(dfwof)
            }

            //for ph
            //TODO - dodac recipient ph
            def recipientPh = "szkup.pawel@gmail.com"
            emailService.sendDocumentsTemplateVersion(recipientPh, documentFilesWithBlackFaksymileList)

            //for acceptant
            def recipientUser = getFromProcessData(process, 'kontaktEmail');
            emailService.sendDocumentsTemplateVersion(recipientUser, documentFilesWithoutFaksymileList)
        }
    }
	
	def _getNewProcessStatus(def params, def requiredNumberOfSubscriptions) {
		Process.ProcessStatus newStatus
		
		if ("electronical".equals(params?.requestVersion)) {
			if (params?.numberOfSubscriptions < requiredNumberOfSubscriptions) {
				newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
			}
			else {
				newStatus = Process.ProcessStatus.WAITING
			}
		}
		else if ("paper".equals(params?.requestVersion)) {
			newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION
		}
		else if ("templates".equals(params?.requestVersion)) {
			newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION
		}
		
		return newStatus
	}

    def getFromProcessData(def process, def key){
        def result = process.processData.find{ pd -> pd.name.equals(key)}
        return (result && result?.value)?result?.value:""
    }
}