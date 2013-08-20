package com.eservice.eumowy

import com.eservice.eumowy.process.DefineActivityCommand
import com.eservice.eumowy.process.GetCalculatorCommand
import com.eservice.eumowy.command.ProcessCommand

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def emailService
    def cbdService
    def attachmentService
    def messageSource

    def index() {
        redirect(action: "createProcess", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [activityInstanceList: Activity.list(params), activityInstanceTotal: Activity.count()]
    }

    def createProcessFlow = {
        defineActivity{
            on("continue") { DefineActivityCommand cmd ->

                if(cmd?.hasErrors()){
                    flash.errorMessage= message(code: cmd.errors?.getFieldError("selectedActivities").code);
                    return error();
                }

                flow.processInstance =  new Process(activities: Activity.findAllByCodeInList(cmd.selectedActivities));
                flow.notesToCOA = cmd.notes;
            }.to "chooseSubFlow"
            on("error").to "defineActivity"
            on("emailOnly").to "defineActivity"
        }

        chooseSubFlow {
            action {
                def processInstance = flow.processInstance
                def hasUzupelnijPodpisy = processInstance.activities.any{it.code.equals("uzupelnijPodpisy")};

                if(processInstance.activities?.size() == 0){
                    emailOnly();
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
            on("emailOnly").to "emailOnly"
        }

        //SUBFLOWS start
        normal {
            subflow(action: "normal", input: [processInstance : { flow.processInstance }])
            on("backToStart").to "defineActivity"
            on("finish") {
                flow.processInstance = currentEvent.attributes.process
            }.to "finish"
        }

        uzupelnijPodpisy {
            subflow(action: "uzupelnijPodpisy", input: [processInstance : { flow.processInstance }])
            on("backToStart").to "defineActivity"
            on("finish") {
                flow.processInstance = currentEvent.attributes.process
            }.to "finish"
        }

        emailOnly {
            action{
                def notes = flow.notes
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
        //SUBFLOWS end

        finish{
            action{
                flow.processInstance.save()
            }
            on("success").to "defineActivity"
        }
    }

    def normalFlow = {
        input {
            processInstance()
        }
        chooseActivity{
            render(view: "../createProcess/chooseActivity")
            on("continue"){
                def processInstance = flow.processInstance

                //SIGNATURES
                def signatures =  _getSignatures(processInstance.activities)
                processInstance.signatures = signatures

                //ACTIVE PANELS
                TreeSet activePanels = _getActivePanels(signatures)
                processInstance.panels = activePanels.toList();


                flow.processInstance = processInstance
            }.to "chooseCalc"
            on("back").to "backToStart"
            on("continue").to "chooseCalc"
        }

        chooseCalc{
            render(view: "../createProcess/chooseCalc")
            on("back").to "chooseActivity"
            on("getCalculator").to "getCalculator"
            on("continue"){
                Process processInstance = flow.processInstance
                //processInstance.child = new Child(params)

                processInstance.calcNumber =  flow.calcNumber
                processInstance.client =  flow.client

                processInstance.save(flush:true);

                flow.processInstance = processInstance
            }.to "initializePanels"
        }

        getCalculator {
            action {  GetCalculatorCommand cmd ->

                flow.nip = cmd.nip;
                flow.calcNumber = null
                flow.client = null

                Process processInstance = flow.processInstance

                /**
                 * sprawdzanie poprawnosci numeru nip
                 * */
                if(cmd.hasErrors()){
                    flash.nipErrorMessage= message(code: cmd.errors?.getFieldError("nip").code);
                    return error();
                }

                /** pobieranie informacji o kliencie na podstawie numeru nip */
                def client = cbdService.findClientIdByNip(flow.nip);

                /**
                 * sprawdzanie, czy to nie jest nowa umowa
                 * */
                def hasNowaUmowa = processInstance.activities.any{it.code.equals("nowaUmowa")};


                if(client?.id != null || client?.cbdId != null){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else if(hasNowaUmowa){
                    flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                    client = new Client(nip:cmd.nip)
                }else{
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flow.client = client;

                /**
                 * sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces
                 **/
                if(client.id != null && Process.findByClientAndStatusInList(client,
                        [Process.ProcessStatus.WAITING,Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,Process.ProcessStatus.EDIT]))
                {
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                }

                /**
                 * pobieranie danych o kalkulatorze
                 * */
                if(client.id != null || client.cbdId != null){
                    def calcId = cbdService.findCalculatorIdByNip(client.nip)

                    if(calcId == null){
                        flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                        return error();
                    }

                    def calc = cbdService.findCalculatorByNip(client.nip)

                    if(!cbdService.isCalcValid(calc,processInstance.signatures)){
                        flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                        return error();
                    }

                    //TODO
                    flow.calcNumber =  calcId;
                    flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");
                }

                flow.processInstance = processInstance
                flash.isContinueEnabled = true;
            }
            on("success").to "chooseCalc"
            on("error").to "chooseCalc"
        }

           initializePanels {
               action {
                   ProcessCommand cmd ->

                   flow.cmd = cmd
                   cmd.initialize(flow.processInstance)
               }
               on("success").to "selectedPanels"
           }

        selectedPanels{
            render(view: "../createProcess/selectedPanels")
            on("back").to "chooseCalc"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)

                /* http://grails.org/doc/2.2.0/guide/single.html#dataBinding
                   http://grails.org/doc/2.2.0/ref/Controllers/bindData.html
                */
                //bindData(processInstance, params)

                //processInstance.save(flush:true);

                flow.processInstance = processInstance
            }.to "clientSignature"
        }

        clientSignature{
            render(view: "../createProcess/clientSignature")
            on("back").to "selectedPanels"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "finish"
            on("subscribe").to "clientSignature"
            on("noaccept").to "clientSignature"
            on("submit") {
                // GENERATE DOCUMENTS
                // SEND EMAILS
                // IF NOTES FOR COA - SEND THEM

                flow.processInstance.status = Process.ProcessStatus.WAITING
            }.to "finish"
        }

        finish {
            output {
                process {flow.processInstance}
            }
        }

        backToStart()
    }

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
                //processInstance.child = new Child(params)

                processInstance.calcNumber =  flow.calcNumber
                processInstance.client =  flow.client

                processInstance.save(flush:true);

                flow.processInstance = processInstance
            }.to "selectedPanels"
        }

        getCalculator {
            action {  GetCalculatorCommand cmd ->

                flow.nip = cmd.nip;
                flow.calcNumber = null
                flow.client = null

                Process processInstance = flow.processInstance

                /**
                 * sprawdzanie poprawnosci numeru nip
                 * */
                if(cmd.hasErrors()){
                    flash.nipErrorMessage= message(code: cmd.errors?.getFieldError("nip").code);
                    return error();
                }

                /** pobieranie informacji o kliencie na podstawie numeru nip */
                def client = cbdService.findClientIdByNip(cmd.nip);

                /**
                 * sprawdzanie, czy to nie jest nowa umowa
                 * */
                def hasNowaUmowa = processInstance.activities.any{it.code.equals("nowaUmowa")};


                if(client?.id != null || client?.cbdId != null){
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
                }else if(hasNowaUmowa){
                    flash.nipInfoMessage =  message(code:"client.new.info", default:"Nowy klient");
                    client = new Client(nip:cmd.nip)
                }else{
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flow.client = client;

                /**
                 * sprawdzanie, czy w eUmowy istnieje dla danego Akceptanta niezakończony Proces
                 **/
                println("client:"+client);
                if(client.id != null && Process.findByClientAndStatusInList(client,
                        [Process.ProcessStatus.WAITING,Process.ProcessStatus.WAIT_FOR_SUBSRIPTION,Process.ProcessStatus.EDIT]))
                {
                    flash.nipErrorMessage = message(code:"client.unfinishedProcess.error", default:"Dla Akceptanta istnieje niezakończony Proces");
                    return error();
                }

                flow.processInstance = processInstance
                flash.isContinueEnabled = true;
            }
            on("success").to "clientSignature"
            on("error").to "chooseCalc"
        }

        clientSignature{
            render(view: "../createProcess/clientSignature")
            on("back").to "chooseCalc"
            on("continue"){
                def processInstance = flow.processInstance
                //TODO implement logic
                flow.processInstance = processInstance
            }.to "finish"
            on("subscribe").to "clientSignature"
            on("noaccept").to "clientSignature"
            on("submit") {
                // GENERATE DOCUMENTS
                // SEND EMAILS
                // IF NOTES FOR COA - SEND THEM

                flow.processInstance.status = Process.ProcessStatus.WAITING
            }.to "finish"
        }

        finish {
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
            def process = Process.get(Integer.valueOf(params.processId));
            process.addToAttachments(msg as AttachmentFile);
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
        render(template:"../attachment/list", model:[files:attachmentService.getListByProcessId(params.processId), processId: params.processId]);
    }

    def updateProcessStatus() {
        log.info "I WAS TRIGGERED"
        def process = Process.get(Integer.valueOf(params.processId));
        if (params.processStatus.equals("WAIT_FOR_SUBSRIPTION")) {
            process.status = Process.ProcessStatus.WAIT_FOR_SUBSRIPTION
            render(text: "OK_WAIT_FOR_SUBSRIPTION")
        }
        else if (params.processStatus.equals("SUBSCRIPTIONS_DONE")) {
            process.status = Process.ProcessStatus.SUBSCRIPTIONS_DONE
            render(text: "OK_SUBSCRIPTIONS_DONE")
        }
        else if (params.processStatus.equals("REJECTED")) {
            process.status = Process.ProcessStatus.REJECTED
            //TODO
            render(text: "OK_REJECTED")
        }
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
            def activitySignatureParam = params["activitySignature_${activity.id}"]

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

                activity.activitySignatures = activitySignatures;
            }
        }
        return signatures;
    }


}
