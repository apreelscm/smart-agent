package com.eservice.eumowy
import com.eservice.eumowy.process.DefineActivityCommand
import com.eservice.eumowy.process.GetCalculatorCommand
import com.lucastex.grails.fileuploader.UFile

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //def emailService
    def emailService
    def cbdService

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

                def selectedActivities = Activity.findAllByCodeInList(cmd.selectedActivities);

                //sending email
                if(selectedActivities?.size() == 0){
                    _sendNotesToCOA(cmd.notes)
                    return emailOnly();
                }

                flow.notesToCOA = cmd.notes;
                flow.processInstance = new Process(activities: selectedActivities)
            }.to "chooseActivity"
            on("error").to "defineActivity"
            on("emailOnly").to "defineActivity"
        }

        chooseActivity{
            on("back").to "defineActivity"
            on("error").to "chooseActivity"
            on("continue"){
                def processInstance = flow.processInstance

                //SIGNATURES
                def signatures =  _getSignatures(processInstance.activities)
                processInstance.signatures = signatures

                //ACTIVE PANELS
                TreeSet activePanels = _getActivePanels(signatures)
                processInstance.panels = activePanels.toList();

                /** dev logs*/
                /*log.info("activities:"+ processInstance.activities)
                 log.info("params:"+ params)
                 log.info("signatures:"+signatures)
                 log.info("code:"+signatures*.panelsSignature)
                 log.info("activePanels:"+activePanels)*/

                flow.processInstance = processInstance
            }.to "chooseCalc"
        }

        chooseCalc{
            on("back").to "chooseActivity"
            on("getCalculator").to "getCalculator"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)

                processInstance.calcNumber =  flow.calcNumber
                processInstance.client =  flow.client

                ((Process)processInstance).save();

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
                if(client == null){
                    if(!hasNowaUmowa){
                        flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                        return error();
                    }
                    else{
                        flash.nipInfoMessage =  message(code:"client.new.info", default:"Now klient");
                        client = new Client(nip:cmd.nip)
                    }
                }
                else{
                    flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");
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
                }

                /**
                 * sprawdzanie czy wybrano aktywnosc popraw dane oraz istnieja zapisane podpisy akceptanta
                 **/
                def hasPoprawDane = processInstance.activities.any{it.code.equals("poprawDane")};
                def hasSubscriptions = processInstance.subscriptions?.size() > 0;
                if(hasPoprawDane && hasSubscriptions){
                    return goToClientSignature();
                }

                /**
                 * pobieranie danych o kalkulatorze
                 * */
                if(client.id != null || client.cbdId != null){
                    def calc = cbdService.findCalculatorByClientId(client.nip)

                    if(calc == null){
                        flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                        return error();
                    }

                    if(!cbdService.isCalcValid(calc,processInstance.signatures)){
                        flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                        return error();
                    }

                    flow.calcNumber =  "cal123456";
                    flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");
                }


                flow.processInstance = processInstance
                flash.isContinueEnabled = true;
            }
            on("success").to "chooseCalc"
            on("goToClientSignature").to "clientSignature"
            on("error").to "chooseCalc"
        }

        selectedPanels{
            on("back").to "chooseCalc"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "clientSignature"
            on("uploadFile").to "uploadFile"
        }

        uploadFile{
            action {
                //upload group
                def upload = params.upload

                //config handler
                def config = grailsApplication.config.fileuploader[upload]

                //request file
                def file = request.getFile("file")

                //base path to save file
                def path = config.path
                if (!path.endsWith('/'))
                    path = path+"/"

                /**************************
                 check if file exists
                 **************************/
                if (file.size == 0) {
                    def msg = messageSource.getMessage("fileupload.upload.nofile", null, request.locale)
                    log.warn msg
                    flash.uploadErrorMessage = msg
                    redirect controller: params.errorController, action: params.errorAction, id: params.id
                    return error()
                }

                /***********************
                 check extensions
                 ************************/

                println "extensions start - allowedExtensions:"+config.allowedExtensions
                def fileExtension = file.originalFilename.substring(file.originalFilename.lastIndexOf('.')+1)
                if (!config.allowedExtensions[0].equals("*")) {
                    if (!config.allowedExtensions.contains(fileExtension)) {
                        def msg = messageSource.getMessage("fileupload.upload.unauthorizedExtension", [fileExtension, config.allowedExtensions] as Object[], request.locale)
                        log.warn msg
                        flash.uploadErrorMessage = msg
                        redirect controller: params.errorController, action: params.errorAction, id: params.id
                        println "extensions end"
                        return error()
                    }
                }
                println "extensions end"

                /*********************
                 check file size
                 **********************/
                if (config.maxSize) { //if maxSize config exists
                    def maxSizeInKb = ((int) (config.maxSize/1024))
                    if (file.size > config.maxSize) { //if filesize is bigger than allowed
                        log.warn "FileUploader plugin received a file bigger than allowed. Max file size is ${maxSizeInKb} kb"
                        flash.uploadErrorMessage = messageSource.getMessage("fileupload.upload.fileBiggerThanAllowed", [maxSizeInKb] as Object[], request.locale)
                        redirect controller: params.errorController, action: params.errorAction, id: params.id
                        return error()
                    }
                }

                //reaches here if file.size is smaller or equal config.maxSize or if config.maxSize ain't configured (in this case
                //plugin will accept any size of files).

                //sets new path
                def currentTime = System.currentTimeMillis()
                path = path+currentTime+"/"
                if (!new File(path).mkdirs())
                    log.error "FileUploader plugin couldn't create directories: [${path}]"
                path = path+file.originalFilename

                //move file
                log.info "FileUploader plugin received a ${file.size}b file. Moving to ${new File(path).absolutePath}"
                file.transferTo(new File(path))

                //save it on the database
                def ufile = new UFile()
                ufile.name = file.originalFilename
                ufile.size = file.size
                ufile.extension = fileExtension
                ufile.dateUploaded = new Date(currentTime)
                ufile.path = path
                ufile.downloads = 0
                ufile.save()

                println "params.successController:"+params.successController
                flash.uploadInfoMessage = "Załącznik został dodany"
                //  redirect controller: params.successController, action: params.successAction, params:[ufileId:ufile.id, id: params.id]
            }
            on("success").to "selectedPanels"
            on("error").to "selectedPanels"
        }

        clientSignature{
            on("back").to "selectedPanels"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "finish"
            on("subscribe").to "clientSignature"
            on("noaccept").to "clientSignature"
        }

        finish{
            action{
                flow.processInstance.save()
            }
        }
    }

//--------------
//PRIVATE METHODS
//--------------

    def _sendNotesToCOA(notes) {

        log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${notes}]")
        assert notes != null;

        try{
            emailService.sendNotesToCOA(notes)
            flash.infoMessage = message(code:"email.notesToCOA.send.complete", default:"Wysłano wiadomość z uwagami do COA");
        }catch (Exception error){
            flash.errorMessage = message(code:"email.send.error", default:"Wystąpił błąd podczas wysyłania wiadomości");
            log.error(error.message)
            error.printStackTrace();
        }
    }

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
