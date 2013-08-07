package com.eservice.eumowy

class CbdService {

    boolean transactional = false

    def findClientIdByNip(String nip) {

        def cbdId;

        if(nip == "1234567819"){
            cbdId = "11";
        }

        if(nip == "8946001495"){
            cbdId = "22";
        }

        if(nip == "7343597142"){
            cbdId = "33";
        }

        if(nip == "3558335706"){
            cbdId = "44";
        }

        //2258064349

        def client = Client.findByNip(nip);

        if(client == null && cbdId != null){
            client = new Client(cbdId: cbdId, nip: nip, name: Math.random()+"testName" );
        }

        println("cbdId:"+cbdId)
        println("cl:"+client)
        println("cl1:"+(client == null ))
        println("cl2:"+(cbdId != null))

        return client;
    }

    def findCalculatorByClientId(def kln_id) {
        def calc;

        if(kln_id == "1234567819"){
            calc = []
            calc.add("STAWKA_PP_ORANGE");
            calc.add("OPLATA_ZA_APL_PP");
            calc.add("NIP");
            calc.add("CZY_TELEPOMPKA");
            calc.add("OPLATA_IFPLUS_DINERSCLUB");
            calc.add("STAWKA_PP_MUNDIO");
            calc.add("STAWKA_PP_LYCA");
            calc.add("STAWKA_PP_T_MOBILE");
            calc.add("STAWKA_PP_VIRGIN");
            calc.add("STAWKA_PP_PLAY");
            calc.add("STAWKA_PP_PLUS");
            calc.add("STAWKA_PP_GALENA");
            calc.add("DEKLARACJA_SPRZEDAZY_PP");
            calc.add("OPLATA_DCC");
            calc.add("NULL");
        }
        else if(kln_id == "8946001495"){
            calc = []
            calc.add("STAWKA_PP_PLUS");
            calc.add("STAWKA_PP_GALENA");
            calc.add("DEKLARACJA_SPRZEDAZY_PP");
        }

        else if(kln_id == "7343597142"){
            calc =  CalcField.findAll();
        }

        return calc;
    }

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