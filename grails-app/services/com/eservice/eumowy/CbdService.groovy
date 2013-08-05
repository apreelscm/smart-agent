package com.eservice.eumowy

class CbdService {

/*    def kln_id
    def kln_nip
    def kln_nazwa*/

    def findClientIdByNip(def nip) {
        def kln_id;

        if(nip == "1"){
            kln_id = "11";
        }

        if(nip == "2"){
            kln_id = "22";
        }

        if(nip == "3"){
            kln_id = "33";
        }

        return kln_id;
    }

    def findCalculatorByClientId(def kln_id) {
        def calc;

        if(kln_id == "11"){
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
        }
        else if(kln_id == "22"){
            calc = []
            calc.add("STAWKA_PP_PLUS");
            calc.add("STAWKA_PP_GALENA");
            calc.add("DEKLARACJA_SPRZEDAZY_PP");
        }

        return calc;
    }

    def isCalcValid(def calc, def signatures) {

        Set signaturesCalcNames = []
        signatures.each{signature ->
            signaturesCalcNames.addAll(signature.calcFieldsSignature*.calcField.name);
        }

        println("calc:"+calc)
        println("calcNames:"+signaturesCalcNames)
        println("containsAll:"+signaturesCalcNames.every { calc.contains(it) });

        return signaturesCalcNames.every { calc.contains(it) };
    }
}
