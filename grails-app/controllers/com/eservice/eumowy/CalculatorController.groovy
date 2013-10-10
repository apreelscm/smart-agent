package com.eservice.eumowy

import grails.util.Environment

class CalculatorController {

    def cbdService

    def index() {
        if (Environment.PRODUCTION != Environment.getCurrentEnvironment()){
            def nip = params.nip
            def result = []
            if (nip){
                result = cbdService.findCalculatorByNip(nip);
            }
            [
                    result: result,
                    nip: nip
            ]
        } else {
            [
                    result: [],
                    nip: '-1'
            ]
        }
    }
}
