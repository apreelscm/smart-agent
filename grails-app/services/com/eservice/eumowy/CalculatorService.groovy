package com.eservice.eumowy

import grails.util.Environment

class CalculatorService implements Serializable{

    static transactional = false

    static scope = "session"

    def calc

    static final BRAK_LABEL = "BRAK"

    def isCalcValid(def calcExt, def signatures) {

        // TODO tymczasowo
        if(Environment.isDevelopmentMode() ||
                Environment.TEST.getName().equalsIgnoreCase(Environment.getCurrent().name)){ return true }

        Set signaturesCalcNames = []
        signatures.each{signature ->
            signaturesCalcNames.addAll(signature.calcFieldsSignature?.collect{it.calcField.name});
        }

        println(calcExt)
        def calcKeyList = calcExt.collect { it.POLEAPREEL }

        println("calcKeyList:"+calcKeyList+ " size:"+calcKeyList.size())
        println("calcNames:"+signaturesCalcNames+ " size:"+signaturesCalcNames.size())
        println("contains ALL:"+calcKeyList.containsAll(signaturesCalcNames))


        return calcKeyList.containsAll(signaturesCalcNames)
    }

    def hasCalcProperty(def key, def value){
        calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }


    def getCalcProperty( def key){
        calc?.findResult{ (it.POLEAPREEL == key && it.WARTOSCAPREEL != BRAK_LABEL  ) ? it.WARTOSCAPREEL : null }
    }
}