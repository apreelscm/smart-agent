package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.PdfMapper
import com.eservice.eumowy.process.DefineActivityCommand

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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

        defineActivity{
            onEntry{
                flow.processInstance = flow.processInstance ?: new Process();
                println(" flow.processInstance: "+ flow.processInstance)
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

                if(processInstance.activities?.size() == 0){
                    emailOnly();
                }
                else if(hasPoprawDane){
                    poprawDane();
                }
                else if(hasUzupelnijPodpisy){
                    uzupelnijPodpisy();
                }
                else{
                    normal();
                }
            }
            on("error").to "defineActivity"
            on("normal").to "normal"
            on("uzupelnijPodpisy").to "uzupelnijPodpisy"
            on("poprawDane").to "poprawDane"
            on("emailOnly").to "emailOnly"
        }

        /** default full subflow */
        normal {
            subflow(action: "normal", input: [processInstance : { flow.processInstance }])
            on("backToStart").to "defineActivity"
            on("clientSignature") {
                flow.processInstance = currentEvent.attributes.process
            }.to "clientSignature"
        }

        /** popraw dane subflow */
        poprawDane {
            subflow(action: "poprawDane", input: [processInstance : { flow.processInstance }])
            on("backToStart").to "defineActivity"
            on("finish") {
                flow.processInstance = currentEvent.attributes.process
            }.to "finish"
        }

        /** uzupelnij podpisy subflow */
        uzupelnijPodpisy {
            subflow(action: "uzupelnijPodpisy", input: [processInstance : { flow.processInstance }])
            on("backToStart").to "defineActivity"
            on("finish") {
                flow.processInstance = currentEvent.attributes.process
            }.to "finish"
        }

        /** send email only subflow*/
        emailOnly {
            action{
                def notes = process.notesToCoa
                log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${notes}]")
                try{
                    emailService.sendNotesToCOA(notes)
                    flash.infoMessage = message(code:"email.notesToCOA.send.complete", default:"Wysłano wiadomość z uwagami do COA");
                    log.info(flash.infoMessage)
                }catch (Exception error){
                    flash.errorMessage = message(code:"email.send.error", default:"Wystąpił błąd podczas wysyłania wiadomości");
                    log.info(flash.errorMessage)
                    error.printStackTrace();
                }
            }
            on("success").to "defineActivity"
        }

        clientSignature {
            onEntry {
                def processInstance = flow.processInstance
                def totalPagesCount = 0
                def data = PdfMapper.mapAllDataToPDFData(processInstance.processData, processInstance.points)

                processInstance.signatures.each { sig ->
                    log.info "SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath

                    byte[] documentData = pdfService.fillPdfFormFromURIWithFaksymile(sig, data, PdfService.FontType.ARIAL)

                    if(!documentData) return

                    int pc = pdfService.getPageCountFromPdf(documentData)
                    totalPagesCount += pc

                    DocumentFile df = new DocumentFile(name: sig.templatePath, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc)
                    df.setContent(new DocumentContent(content: documentData))
                    df.save(flush: true)
                    log.info "DF id: " + df.id + " PageCount: " + df.pagesCount
					log.info "Process ID: " + processInstance.id
                    processInstance.addToDocuments(df)
                    processInstance.discard();
                }

                flow.totalPagesCount = totalPagesCount;
                processInstance.save(flush:true)
                flow.processInstance = processInstance
            }
            render(view: "../createProcess/clientSignature", model: [processInstance: flow.processInstance, totalPagesCount: flow.totalPagesCount])
            on("back").to "selectedPanels"
            on("subscribe").to "clientSignature"
            on("updateProcessStatus") {
                log.info params
                if (params.processStatus.equals("WAIT_FOR_SUBSCRIPTION")) {
                    Subscription sub = Subscription.get(params.subscriptionId)
                    flow.processInstance.addToSubscriptions(sub)
                    flow.processInstance.status = Process.ProcessStatus.WAIT_FOR_SUBSRIPTION
                }
                else if (params.processStatus.equals("SUBSCRIPTIONS_DONE")) {
                    Subscription sub = Subscription.get(params.subscriptionId)
                    flow.processInstance.addToSubscriptions(sub)
                    flow.processInstance.status = Process.ProcessStatus.SUBSCRIPTIONS_DONE
                }
                else if (params.processStatus.equals("REJECTED")) {
                    flow.processInstance.status = Process.ProcessStatus.REJECTED
                }
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


                processInstance.status = Process.ProcessStatus.WAITING
                flow.processInstance = processInstance

            }.to "finish"
        }

        /** final operations and process save*/
        finish{
            action{
                Process processInstance = flow.processInstance

                if (!processInstance.save(flush:true)){
                    processInstance.errors.each {
                        println it
                    }
                    return "error"
                }

                if (processInstance.notesToCoa) {
                    log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${processInstance.notesToCoa}]")
                    emailService.sendNotesToCOA(processInstance.notesToCoa)
                }

                flow.processInstance = null;
            }
            on("success").to "beforeRestart"
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
        }
        chooseActivity{
            render(view: "../createProcess/chooseActivity")
            on("continue"){
                def processInstance = flow.processInstance

                //SIGNATURES
                def signatures =  _getSignatures(processInstance.activities)
                processInstance.signatures = signatures

                flow.processInstance = processInstance
            }.to "chooseCalc"
            on("back").to "backToStart"
        }

        chooseCalc{
            render(view: "../createProcess/chooseCalc")
            on("back").to "chooseActivity"
            on("getCalculator").to "getCalculator"
            on("continue"){

                Process processInstance = flow.processInstance
                processInstance.calcNumber =  flow.calcNumber

                Client client = flow.client
                log.info("client id:"+client)
                client.save(flush:true);

                println("err:"+client.errors)

                processInstance.client =  client
                processInstance.status = Process.ProcessStatus.NEW

                def user = springSecurityService.principal
                processInstance.phNumber = user.nr//sec.loggedInUserInfo(field: 'nr')
                processInstance.phFirstName = user.imie// sec.loggedInUserInfo(field: 'imie')
                processInstance.phSurname = user.nazwisko//sec.loggedInUserInfo(field: 'nazwisko')

                if (!processInstance.save(flush:true)){
                    processInstance.errors.each {
                        log.error(it)
                    }
                    return "error"
                }

                flow.processInstance = processInstance

            }.to "selectedPanels"
            on("error").to "chooseCalc"
        }

        getCalculator {
            action {
                flow.nip = params.nip;
                flow.calcNumber = null
                flow.client = null
                flow.isContinueEnabled = false;

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
                        client = client ?: new Client(nip:params.nip, name:"NEW_CLIENT")
                    }else{
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        return error();
                    }
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */
				//TODO Odkomentowac to pozniej
              /*  if(processService.hasIncompleteProcessForClient(client)){
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                    return error();
                }*/

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

                flow.isContinueEnabled = true;
            }
            on("success").to "chooseCalc"
            on("error").to "chooseCalc"
        }

        selectedPanels{
            onEntry {
                println "selectedPanels enterview"

                if(!flow.slipPanelsInit){
                    log.info("slipPanelsInit - false")
                    def processInstance = flow.processInstance;
                    def calc = flow.calc;

                    //ACTIVE PANELS
                    TreeSet activePanels = _getActivePanels(processInstance.signatures)
                    processInstance.panels = activePanels.toList();

                    def processCmd = processService.createNewProcessCommand(processInstance,calc)
                    flow.data = processCmd
                }
                else{
                    log.info("slipPanelsInit - true")
                    flow.slipPanelsInit = false
                }

            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("error").to "selectedPanels"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("saveOnly"){ ProcessCommand cmd ->
                def processInstance = flow.processInstance
                def processDataList = processService.getDataFromPanels(cmd)

                processInstance.processData?.clear()
                processDataList.each { data ->
                    processInstance.addToProcessData(data)
                    processInstance.discard();
                }

                //TODO Save cmd.points to PointData, PointDataDetails, PosData
                processInstance.save();

                flow.processInstance = processInstance
                flow.data = cmd
                flow.slipPanelsInit = true;
            }to "selectedPanels"
            on("continue"){ ProcessCommand cmd ->

                if(cmd?.hasErrors()){
                    log.info(params)
                    return error();
                }

                def processInstance = flow.processInstance
                processInstance.notesToCoa = cmd.notes;
                def processDataList = processService.getDataFromPanels(cmd)
				def pointsDataList = processService.getPointAndPosData(cmd)

                processInstance.processData?.clear()
                processDataList.each { data ->
                    processInstance.addToProcessData(data)
                    processInstance.discard()
                }

				processInstance.points?.clear()
				pointsDataList.each { data ->
					processInstance.addToPoints(data)
					processInstance.discard()
				}
				
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
        }
        chooseCalc{
            render(view: "../createProcess/chooseCalc")
            on("back").to "chooseActivity"
            on("getCalculator").to "getCalculator"
            on("continue"){
                def processInstance = flow.processInstance
                processInstance.calcNumber =  flow.calcNumber
                flow.processInstance = processInstance
            }.to "selectedPanels"
            on("error").to "chooseCalc"
        }

        getCalculator {
            action {
                flow.nip = params.nip;
                flow.calcNumber = null
                //  flow.client = null
                flow.isContinueEnabled = false;

                if(!clientService.isClientNipValid(flow.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);

                if(clientService.clientExists(client)){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error()
                }

                //flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */

                def lastProcessId = processService.getLastIfIncompleteProcessForClientNip(client.nip)
                if(!lastProcessId){
                    flash.nipErrorMessage = message(code:"client.todo.error",
                            default:"Brak możliwości poprawy danych, istnieją inne nowsze zaakceptowane procesy dla tego Akceptanta");
                    return error()
                }

                def lastProcess = Process.get(lastProcessId)

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
                flow.processInstance = lastProcess
                flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");

                flow.isContinueEnabled = true;
            }
            on("success").to "chooseCalc"
            on("error").to "chooseCalc"
        }

        selectedPanels{
            onEntry {
                def processInstance = flow.processInstance;
                def calc = flow.calc;
                def processCmd = processService.createSavedProcessCommand(processInstance,calc);
                flow.data = processCmd
            }
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("acceptPointsButton") {
                log.info "acceptPointsButton TRIGGERED"
            }.to "selectedPanels"
            on("continue"){ ProcessCommand cmd ->

                if(cmd?.hasErrors()){
                    log.info(params)
                    return error();
                }

                def processInstance = flow.processInstance
                processInstance.notesToCoa = cmd.notes;

                //_createPointDatas(flow.processInstance)
                //_createPosDatas(flow.processInstance)

                def processDataList = processService.getDataFromPanels(cmd)

                //TODO optymalizacja
                processInstance.processData?.clear()
                processDataList.each { data ->
                    processInstance.addToProcessData(data)
                    processInstance.discard();
                }

                processInstance.save();

                flow.processInstance = processInstance
            }.to "clientSignature"
        }


        clientSignature {
            output {
                process {flow.processInstance}
            }
        }
        backToStart()
    }


/** UZUPELNIJ PODPISY SUBFLOW */
    def uzupelnijPodpisyFlow = {
        input {
            processInstance()
        }
        chooseCalc{
            render(view: "../createProcess/chooseCalc")
            on("back").to "backToStart"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.processInstance
                processInstance.calcNumber =  flow.calcNumber
                processInstance.client =  flow.client
                processInstance.save();

                flow.processInstance = processInstance
            }.to "selectedPanels"
        }

        getCalculator {
            action {
                flow.nip = params.nip;
                flow.calcNumber = null
                flow.client = null

                def processInstance = flow.processInstance

                if(!clientService.isClientNipValid(params.nip)){
                    flash.nipErrorMessage= message(code: 'GetCalculatorCommand.nip.validator.invalid');
                    return error();
                }

                def client = cbdService.findClientByNip(flow.nip);

                if(clientService.clientExists(client)){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else {
                    /** sprawdzanie, czy to nie jest nowa umowa */
                    def hasNowaUmowa = processService.containsActivity(processInstance.activities,"nowaUmowa")
                    if(hasNowaUmowa){
                        flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                        client = new Client(nip: params.nip)
                    }else{
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        return error();
                    }
                }

                flow.client = client;

                /** sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces */

                if(processService.hasIncompleteProcessForClient(client)){
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                    return error();
                }

                flash.isContinueEnabled = true;
            }
            on("success").to "clientSignature"
            on("error").to "chooseCalc"
        }

        clientSignature {
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
            attachment.processId = Long.valueOf(params.processId)
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
        println(params)
        render(template:"../attachment/list", model:[files:attachmentService.getListByProcessId(params.processId), processId: params.processId]);
    }

    def getDocumentPage() {
        def process = Process.get(Integer.valueOf(params.processId));
        String path = pdfService.generateImageFromPDFDocumentFile(process.documents,
                params.processId as String,
                Integer.valueOf(params.pageNumber));
        render(text: path)
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

        Process.ProcessStatus newStatus;
        if ("electronical".equals(requestVersion)) {
            //TODO Check signatures and update documents in DB
            newStatus = Process.ProcessStatus.WAITING

            process.documents.each { doc ->
                //TODO Update document content from Data Map
            }

            //TODO Send emails
            emailService.sendDocumentsElectronicalVersion(process.documents)

        }
        else if ("paper".equals(requestVersion)) {
            //Documents are already in DB
            newStatus = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION

            emailService.sendDocumentsPaperVersion(process.documents)
        }
        else if ("templates".equals(requestVersion)) {
            //TODO Documents are already in DB
            newStatus = Process.ProcessStatus.WAIT_FOR_SUBSRIPTION
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
                dfwof.setContent(new DocumentContent(content: documentDataWithBlackFaksymile))
                dfwof.discard()
                documentFilesWithoutFaksymileList.add(dfwof)
            }

            emailService.sendDocumentsTemplateVersionWithBlackFaksymile(documentFilesWithBlackFaksymileList)
            emailService.sendDocumentsTemplateVersionWithoutFaksymile(documentFilesWithoutFaksymileList)
        }
    }
}
