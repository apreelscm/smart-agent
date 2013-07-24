package com.eservice.eumowy

class ActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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

    def create_chooseCalc() {
        [activityInstance: new Activity(params)]
    }

    def create_clientSignature() {
        [activityInstance: new Activity(params)]
    }

    def create_chooseActivity() {
        [activityInstance: new Activity(params)]
    }

    def save() {
        //TODO implement
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
}
