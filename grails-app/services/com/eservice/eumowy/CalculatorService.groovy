package com.eservice.eumowy

class CalculatorService {

    static transactional = false

    def isCalcValid(def calc, def signatures) {

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
