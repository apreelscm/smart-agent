package com.eservice.eumowy

import com.eservice.eumowy.activity.ActivityTree
import org.springframework.mail.MailException

import java.awt.print.Book

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
            on("continue") {
                //TODO MOVE TO CMD OBJECT
                def activityTree = new ActivityTree(); bindData(activityTree, params)
                def selectedItems = activityTree.properties.findResults { it.value == "on" ? it.key : null };
                def selectedActivities = Activity.findAllByCodeInList(selectedItems);
                def notes = params.notes;

                //validate controls
                if(selectedActivities?.size() == 0 && notes?.length() == 0){
                    flash.errorMessage = "Należy wybrać aktywności i/lub wypełnić uwagi do COA."
                    return error()
                }

                //only send notes email
                if(selectedActivities?.size() == 0){
                    _sendNotesToCOA(notes);
                    return emailOnly()
                }

                //additional sending email
                if(notes){
                   if(!_sendNotesToCOA(notes)){
                       return error()
                   }
                }

               [processInstance: new Process(activities: selectedActivities)]

            }.to "chooseCalc"
            on("error").to "defineActivity"
            on("emailOnly").to "defineActivity"
        }

        chooseCalc{
            on("continue").to "chooseActivity"
            on("back").to "defineActivity"
        }

        listBooks {
            action {
                [bookList: Book.list()]
            }
            on("success").to "showCatalogue"
            on(Exception).to "handleError"
        }


        chooseActivity{
            on("continue").to "acceptanceInfo"
            on("back").to "chooseCalc"
        }

        acceptanceInfo{
            on("continue").to "clientSignature"
            on("back").to "chooseActivity"
        }

        clientSignature{
            on("continue").to "clientSignature"
            on("back").to "acceptanceInfo"
            on("subscribe").to "clientSignature"
            on("noaccept").to "clientSignature"
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
