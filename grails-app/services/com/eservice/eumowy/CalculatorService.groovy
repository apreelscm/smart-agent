package com.eservice.eumowy

import grails.util.Environment

class CalculatorService {

    static transactional = false

    static final BRAK_LABEL = "BRAK"

    def isCalcValid(def calc, def signatures) {

        // TODO tymczasowo
        if(Environment.isDevelopmentMode() ||
                Environment.TEST.getName().equalsIgnoreCase(Environment.getCurrent().name)){ return true }

        Set signaturesCalcNames = []
        signatures.each{signature ->
            signaturesCalcNames.addAll(signature.calcFieldsSignature?.collect{it.calcField.name});
        }

        println(calc)
        def calcKeyList = calc.collect { it.POLEAPREEL }

        println("calcKeyList:"+calcKeyList+ " size:"+calcKeyList.size())
        println("calcNames:"+signaturesCalcNames+ " size:"+signaturesCalcNames.size())
        println("contains ALL:"+calcKeyList.containsAll(signaturesCalcNames))


        return calcKeyList.containsAll(signaturesCalcNames)
    }

    def hasCalcProperty(def calc, def key, def value){
        //println("has ${key} = ${ calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])}")
        calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }


    def getCalcProperty(def calc, def key){
        //println("has ${key} = ${ calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])}")
        calc.findResult{ (it.POLEAPREEL == key && it.WARTOSCAPREEL != BRAK_LABEL  ) ? it.WARTOSCAPREEL : null }
    }
}