package com.eservice.eumowy

import com.eservice.eumowy.activity.ActivityTree
import org.springframework.mail.MailException

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def emailService

    def index() {
        redirect(action: "create_defineActivity", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [activityInstanceList: Activity.list(params), activityInstanceTotal: Activity.count()]
    }

    def create_defineActivity() {
        [activityInstance: new Activity(params)]
    }

    def validate_create_defineActivity() {
        //populate activityTree props from params
        def activityTree = new ActivityTree(); bindData(activityTree, params)
        def selectedActivities = activityTree.properties.findResults { it.value == "on" ? it.key : null };
        def notes = params.notes;

        //validate selected checkboxes
        if(selectedActivities?.size() == 0 && notes?.length() == 0){
            flash.errorMessage = "Należy wybrać aktywności i/lub wypełnić uwagi do COA."
            redirect(action: "create_defineActivity", params: params)
            return;
        }

        //sending notes
        if(notes && !_sendNotesToCOA(notes)){
            redirect(action: "create_defineActivity", params: params)
            return;
        }

        //OK -> move on to create_chooseCalc
        redirect(action: "create_chooseCalc")
    }

    def create_chooseCalc() {
        [activityInstance: new Activity(params)]
    }

    def create_chooseActivity() {
        [activityInstance: new Activity(params)]
    }

    def create_acceptanceInfo() {
        [activityInstance: new Activity(params)]
    }

    def create_clientSignature() {
        [activityInstance: new Activity(params)]
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

        redirect(action: "create_chooseCalc")
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
