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

        println("calc:"+calc)
        println("calcNames:"+signaturesCalcNames)
        println("containsAll:"+signaturesCalcNames.every {calc.contains(it) });

        return signaturesCalcNames.every { calc.contains(it) };
    }
}
