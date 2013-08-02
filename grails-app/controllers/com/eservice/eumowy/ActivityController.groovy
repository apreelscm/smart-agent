package com.eservice.eumowy

import com.eservice.eumowy.process.DefineActivityCommand
import org.springframework.mail.MailException

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def emailService

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
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "chooseCalc"
        }

        chooseCalc{
            on("back").to "chooseActivity"
            on("continue"){
                def processInstance = flow.processInstance
                //processInstance.child = new Child(params)
                flow.processInstance = processInstance
            }.to "acceptanceInfo"
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
    //OTHERS
    //--------------
    def verifyClientNIP(String nipNumber) {
        //TODO implement proper logic
        flash.clientNip = nipNumber;
        if(nipNumber == "1"){
            flash.nipInfoMessage = message(code: 'todo', default: 'Znaleziono')
            flash.calcInfoMessage = message(code: 'todo', default: 'Znaleziono')
            //flash.calcErrorMessage = message(code: 'todo', default: 'Klient już istnieje / Istnieją niezaakcpetowane dokumenty')

            flash.clientCalcNumber = 123456
        }
        else{
            flash.nipErrorMessage = message(code: 'todo', default: 'Klient już istnieje / Istnieją niezaakcpetowane dokumenty')
        }

        redirect(action: "chooseCalc")
    }

    //--------------
    //PRIVATE METHODS
    //--------------

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
