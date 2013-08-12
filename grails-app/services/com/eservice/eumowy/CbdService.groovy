package com.eservice.eumowy

import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class CbdService {

    def cbdSqlService

    private static final def GET_ADRES_DANE_DO_WYDRUKU = "getAdresDaneDoWydruku"
    private static final def GET_ADRES_DO_KORESPONDENCJI = "getAdresDoKorespondencji"
    private static final def GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM = "getAdresDoKorespondencjizAkceptantem"
    private static final def GET_DANE_AKCEPTANTA = "getDaneAkceptanta"
    private static final def GET_NAZWA_BANKU = "getNazwaBanku"
    private static final def GET_NUMER_RACHUNKU_BANKOWEGO = "getNumerRachunkuBankowego"
    private static final def GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba1UprawnionaDoPodpisaniaUmowy"
    private static final def GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba2UprawnionaDoPodpisaniaUmowy"
    private static final def GET_OSOBA_DO_KONTAKTU = "getOsobaDoKontaktu"
    private static final def GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA = "getOsobaKtoraPozyskalaAkceptanta"
    private static final def GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID = "getPromocyjneObinzenieOplatGrid"
    private static final def GET_WYKAZ_PUNKTOW_GRID = "getWykazPunktowGrid"
    private static final def GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID = "getZakresUruchomieniaPunktyGrid"


    //@Cacheable(value="getAdresDaneDoWydruku", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def  getAdresDaneDoWydruku(def clientNip) {
        return cbdSqlService.selectOne(GET_ADRES_DANE_DO_WYDRUKU,[nip:clientNip])
    }

    //@Cacheable(value="getAdresDoKorespondencji", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencji(def clientNip) {
        return cbdSqlService.selectOne(GET_ADRES_DO_KORESPONDENCJI,[nip:clientNip])
    }

    //@Cacheable(value="getAdresDoKorespondencjizAkceptantem", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencjizAkceptantem(def clientNip) {
        return cbdSqlService.selectOne(GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM,[nip:clientNip])
    }

    //@Cacheable(value="getDaneAkceptanta", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getDaneAkceptanta(def clientNip) {
        return cbdSqlService.selectOne(GET_DANE_AKCEPTANTA,[nip:clientNip])
    }

    //@Cacheable(value="getNazwaBanku", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNazwaBanku(def clientNip) {
        return cbdSqlService.selectOne(GET_NAZWA_BANKU,[nip:clientNip])
    }

    //@Cacheable(value="getNumerRachunkuBankowego", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNumerRachunkuBankowego(def clientNip) {
        return cbdSqlService.selectOne(GET_NUMER_RACHUNKU_BANKOWEGO,[nip:clientNip])
    }

    //@Cacheable(value="getOsoba1UprawnionaDoPodpisaniaUmowy", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba1UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdSqlService.selectOne(GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    //@Cacheable(value="getOsoba2UprawnionaDoPodpisaniaUmowy", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba2UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdSqlService.selectOne(GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    //@Cacheable(value="getOsobaDoKontaktu", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaDoKontaktu(def clientNip) {
        return cbdSqlService.selectOne(GET_OSOBA_DO_KONTAKTU,[nip:clientNip])
    }

    //@Cacheable(value="getOsobaKtoraPozyskalaAkceptanta", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaKtoraPozyskalaAkceptanta(def clientNip) {
        return cbdSqlService.selectOne(GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA,[nip:clientNip])
    }

    //@Cacheable(value="getPromocyjneObinzenieOplatGrid", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPromocyjneObinzenieOplatGrid(def clientNip) {
        return cbdSqlService.selectOne(GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID,[nip:clientNip])
    }

    //@Cacheable(value="getSiedzibaAkceptanta", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getSiedzibaAkceptanta(def clientNip) {
        return cbdSqlService.selectOne(GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM,[nip:clientNip])
    }

    //@Cacheable(value="getWykazPunktowGrid", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getWykazPunktowGrid(def clientNip) {
        return cbdSqlService.selectOne(GET_WYKAZ_PUNKTOW_GRID,[nip:clientNip])
    }

   //@Cacheable(value="getZakresUruchomieniaPunktyGrid", key="#clientNip")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getZakresUruchomieniaPunktyGrid(def clientNip) {
        return cbdSqlService.selectOne(GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID,[nip:clientNip])
    }

    /**
     * MOCK
     * */

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