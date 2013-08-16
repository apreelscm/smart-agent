package com.eservice.eumowy

import com.eservice.eumowy.dao.CbdDAO
import grails.plugin.cache.Cacheable
import grails.util.Environment
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class CbdService {

    CbdDAO cbdDAO

    private static final def FIND_CLIENT_ID_BY_NIP = "findClientIdByNip"
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

    @Cacheable(value="getAdresDaneDoWydruku")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findClientIdByNip(def clientNip) {
        switch (Environment.getCurrent()) {
            case Environment.DEVELOPMENT:
               findClientIdByNipMock(clientNip);
            case Environment.TEST:
                def rowResult = cbdDAO.selectOne(FIND_CLIENT_ID_BY_NIP,[nip:clientNip])
                return new Client(rowResult)
        }
    }

    @Cacheable(value="getAdresDaneDoWydruku")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def  getAdresDaneDoWydruku(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DANE_DO_WYDRUKU,[nip:clientNip])
    }

    @Cacheable(value="getAdresDoKorespondencji")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencji(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJI,[nip:clientNip])
    }

    @Cacheable(value="getAdresDoKorespondencjizAkceptantem")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencjizAkceptantem(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM,[nip:clientNip])
    }

    @Cacheable(value="getDaneAkceptanta")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getDaneAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_DANE_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="getNazwaBanku")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNazwaBanku(def clientNip) {
        return cbdDAO.selectOne(GET_NAZWA_BANKU,[nip:clientNip])
    }

    @Cacheable(value="getNumerRachunkuBankowego")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNumerRachunkuBankowego(def clientNip) {
        return cbdDAO.selectOne(GET_NUMER_RACHUNKU_BANKOWEGO,[nip:clientNip])
    }

    @Cacheable(value="getOsoba1UprawnionaDoPodpisaniaUmowy")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba1UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    @Cacheable(value="getOsoba2UprawnionaDoPodpisaniaUmowy")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba2UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    @Cacheable(value="getOsobaDoKontaktu")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaDoKontaktu(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA_DO_KONTAKTU,[nip:clientNip])
    }

    @Cacheable(value="getOsobaKtoraPozyskalaAkceptanta")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaKtoraPozyskalaAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="getPromocyjneObinzenieOplatGrid")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPromocyjneObinzenieOplatGrid(def clientNip) {
        return cbdDAO.selectOne(GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID,[nip:clientNip])
    }

    @Cacheable(value="getSiedzibaAkceptanta")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getSiedzibaAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM,[nip:clientNip])
    }

    @Cacheable(value="getWykazPunktowGrid")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getWykazPunktowGrid(def clientNip) {
        return cbdDAO.selectOne(GET_WYKAZ_PUNKTOW_GRID,[nip:clientNip])
    }

   @Cacheable(value="getZakresUruchomieniaPunktyGrid")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getZakresUruchomieniaPunktyGrid(def clientNip) {
        return cbdDAO.selectOne(GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID,[nip:clientNip])
    }

    /**
     * MOCK
     * */
    def findClientIdByNipMock(String nip) {
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