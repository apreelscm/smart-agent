package com.eservice.eumowy
import com.eservice.eumowy.process.DefineActivityCommand
import org.springframework.mail.MailException

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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
                    flow.errorMessage= message(code: cmd.errors?.getFieldError("selectedActivities").code);
                    return error();
                }else{
                    flow.errorMessage = null;
                    def selectedActivities = Activity.findAllByCodeInList(cmd.selectedActivities);

                    //additional sending email
                    if(cmd.notes){
                        if(!_sendNotesToCOA(cmd.notes)) return error()
                    }

                    if(selectedActivities?.size() == 0) return emailOnly()

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

                log.info("activities:"+ processInstance.activities)
                log.info("params:"+ params)

                //SIGNATURES
                def signatures =  _getSignatures(processInstance.activities)
                processInstance.signatures = signatures

                //ACTIVE PANELS
                TreeSet activePanels = _getActivePanels(signatures)
                processInstance.panels = activePanels.toList();

                log.info("signatures:"+signatures)
                log.info("code:"+signatures*.panels)
                log.info("activePanels:"+activePanels)

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
            }.to "acceptanceInfo"
        }

        getCalculator {
            action {

                def processInstance = flow.processInstance
                flow.nip = params.nip;

                def kln_id = cbdService.findClientIdByNip(params.nip);

                if( kln_id == null){
                    flash.nipErrorMessage = "Brak klienta";
                    return error();
                }

                flash.nipInfoMessage = "Znaleziono"

                def calc = cbdService.findCalculatorByClientId(kln_id)

                if(calc == null){
                    flash.calcErrorMessage = "Kalkulator nie istnieje"
                    return error();
                }

                //TODO [mock] implement this!
                flash.calcNumber = "cal123456";

                if(!cbdService.isCalcValid(calc,processInstance.signatures)){
                    flash.calcErrorMessage = "Kalkulator nie pozwala na wykonanie wszystkich zaznaczonych czynności."
                    return error();
                }

                flash.calcInfoMessage = "Znaleziono"
            }
            on("success").to "chooseCalc"
            on("error").to "chooseCalc"
        }

        acceptanceInfo{
            on("back").to "chooseCalc"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "clientSignature"
        }

        clientSignature{
            on("back").to "acceptanceInfo"
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
        activePanels.addAll(signatures*.panels.flatten().findAll({ it != "null" && it != "[]" }));
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

        def email = [
                subject: message(code:'notesToCOA.email.subject'),
                text: notes
        ]

        try{
            emailService.sendSimpleMail(email)
            flash.infoMessage = "Wiadomość została wysłana."
            return true;
        }catch (MailException error){
            flash.errorMessage = "Wystąpił błąd podczas wysyłania wiadomości."
            log.error(flash.errorMessage + ": " + error.message)
            error.printStackTrace();
            return false;
        }
    }


}
