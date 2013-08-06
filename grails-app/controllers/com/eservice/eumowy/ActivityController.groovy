package com.eservice.eumowy
import com.eservice.eumowy.process.DefineActivityCommand
import org.springframework.mail.MailException

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //def emailService
    def mailService
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
                }else{
                    def selectedActivities = Activity.findAllByCodeInList(cmd.selectedActivities);

                    //sending email
                    if(selectedActivities?.size() == 0){
                        _sendNotesToCOA(cmd.notes)
                        return emailOnly();
                    }

                    flow.notesToCOA = cmd.notes;
                    flow.processInstance = new Process(activities: selectedActivities)
                }
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
                flow.processInstance = processInstance
            }.to "selectedPanels"
        }

        getCalculator {
            action {

                def processInstance = flow.processInstance
                flow.nip = params.nip;

                def kln_id = cbdService.findClientIdByNip(params.nip);

                if( kln_id == null){
                    flash.nipErrorMessage = message(code:"client.notFound.error", default:"Brak klienta");
                    return error();
                }

                flash.nipInfoMessage =  message(code:"client.found.info", default:"Znaleziono");

                def calc = cbdService.findCalculatorByClientId(kln_id)

                if(calc == null){
                    flash.calcErrorMessage = message(code:"calc.notFound.error", default:"Kalkulator nie istnieje");
                    return error();
                }

                //TODO [mock] implement this!
                flash.calcNumber = "cal123456";

                if(!cbdService.isCalcValid(calc,processInstance.signatures)){
                    flash.calcErrorMessage =  message(code:"calc.notEnough.error", default:"Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności");
                    return error();
                }

                flash.calcInfoMessage = message(code:"calc.found.info", default:"Znaleziono");
            }
            on("success").to "chooseCalc"
            on("error").to "chooseCalc"
        }

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

    def _sendNotesToCOA(notes) {
        assert notes != null;

        log.info("wysyłanie wiadomości email z uwagami do COA [notes: ${notes}]")
        log.info(g.render(template:"myMailTemplate", model:[text : notes]));

        try{
            mailService.sendMail {
                to "apreel.eUmowy@gmail.com"
                from "apreel.eUmowy@gmail.com"
                subject message(code:'notesToCOA.email.subject')
                html g.render(template:"myMailTemplate", model:[text : notes])
            }
            flash.infoMessage = message(code:"email.notesToCOA.send.complete", default:"Wysłano wiadomość z uwagami do COA");
            return true;
        }catch (MailException error){
            flash.errorMessage = message(code:"email.send.error", default:"Wystąpił błąd podczas wysyłania wiadomości");
            error.printStackTrace();
            return false;
        }
    }
}
