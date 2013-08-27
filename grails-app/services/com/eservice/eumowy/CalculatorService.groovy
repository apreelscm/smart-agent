package com.eservice.eumowy

import grails.util.Environment

class CalculatorService {

    static transactional = false

    def isCalcValid(def calc, def signatures) {

        if(!Environment.isDevelopmentMode()){ return true }

        Set signaturesCalcNames = []
        signatures.each{signature ->
            signaturesCalcNames.addAll(signature.calcFieldsSignature*.calcField);
        }

        def calcKeyList = calc.collect { it.POLEAPREEL };

        println("calc:"+calc)
        println("calcNames:"+signaturesCalcNames)
        println("containsAll:"+signaturesCalcNames.every {calcKeyList.contains(it) });

        return signaturesCalcNames.every { calcKeyList.contains(it) };
    }

    def hasCalcProperty(def calc, def key, def value){
        //println("has ${key} = ${ calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])}")
        calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }


    def getCalcProperty(def calc, def key){
        //println("has ${key} = ${ calc.contains([POLEAPREEL:key, WARTOSCAPREEL:value])}")
        calc.findResult{ it.POLEAPREEL == key ? it.WARTOSCAPREEL : null }
    }

}
