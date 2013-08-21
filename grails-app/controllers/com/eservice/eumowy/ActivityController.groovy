package com.eservice.eumowy

import java.awt.FlowLayout;

import com.eservice.eumowy.process.DefineActivityCommand
import com.eservice.eumowy.process.GetCalculatorCommand

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //def emailService
    def emailService
    def cbdService
    def attachmentService
    def messageSource
	def appParameters
	def pdfService

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

        /*   preparePanels {
               action {
                   flow.files = attachmentService.getList();
               }
               on("success").to "selectedPanels"
           }*/

        selectedPanels{
            on("back").to "chooseCalc"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "clientSignature"
        }


        clientSignature{
            on("back").to "selectedPanels"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "finish"
            on("subscribe").to "clientSignature"
            on("noaccept") {
				flow.processInstance.status = Process.ProcessStatus.REJECTED
            }.to "finish"
			on("submit") {
				log.info "PARAMS: " + params
				
				if ("electronical".equals(params.requestVersion)) {
					flow.processInstance.signatures.each { sig ->
						log.info "SIGNATURE NAME: " + sig.name + " PDF TEMPLATE PATH: " + sig.templatePath
						byte[] documentData = pdfService.fillPdfFormFromURIWithFaksymile(sig, PdfService.FontType.ARIAL)
						int pc = pdfService.getPageCountFromPdf(documentData)
						DocumentFile df = new DocumentFile(name: sig.name, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc)
						df.content = documentData
						df.save()
					}
					
					flow.processInstance.status = Process.ProcessStatus.WAITING
				}
				else if ("paper".equals(params.requestVersion)) {
					flow.processInstance.signatures.each { sig ->
						byte[] documentData = pdfService.fillPdfFormFromURIWithBlackFaksymile(sig, PdfService.FontType.ARIAL)
						int pc = pdfService.getPageCountFromPdf(documentData)
						DocumentFile df = new DocumentFile(name: sig.name, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc)
						df.content = documentData
						df.save()
					}
					
					flow.processInstance.status = Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION
				}
				else if ("templates".equals(params.requestVersion)) {
					flow.processInstance.signatures.each { sig ->
						byte[] documentData = pdfService.fillPdfFormFromURIWithBlackFaksymile(sig, PdfService.FontType.ARIAL);
						int pc = pdfService.getPageCountFromPdf(documentData)
						DocumentFile df = new DocumentFile(name: sig.name, dateCreated: new Date(), lastUpdated: new Date(), pagesCount: pc)
						df.content = documentData
						df.save()
					}
					
					flow.processInstance.status = Process.ProcessStatus.WAIT_FOR_SUBSRIPTION
				}
				// SEND EMAILS
				// IF NOTES FOR COA - SEND THEM
				
			}.to "finish"
		}
			
        finish{
            action{
                flow.processInstance.save()
            }
			on("success").to "defineActivity"
        }
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
	
	def getDocumentPage() {
		log.info "I WAS TRIGGERED"
		def process = Process.get(Integer.valueOf(params.processId));
		
		String path = pdfService.generateImagesFromPDF(process.documents, params.processId, Integer.valueOf(params.pageNumber));
		render(text: path)
	}

    def testSql(){
        def result = cbdService.getAdresDaneDoWydruku("2354242")
        log.info("getAdresDaneDoWydrukuTest result:"+result)
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
