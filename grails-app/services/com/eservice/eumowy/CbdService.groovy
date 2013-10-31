package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.plugin.cache.Cacheable
import grails.util.Environment
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class CbdService {

    CbdDAO cbdDAO

    private static final def FIND_CLIENT_ID_BY_NIP = "findClientIdByNip"
    private static final def FIND_CALC_ID_BY_NIP = "findCalcIdByNip"
    private static final def FIND_CALC_BY_NIP = "findCalcByNip"
    private static final def GET_ADRES_DANE_DO_WYDRUKU = "getAdresDaneDoWydruku"
    private static final def GET_ADRES_DO_KORESPONDENCJI = "getAdresDoKorespondencji"
    private static final def GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM = "getAdresDoKorespondencjizAkceptantem"
    private static final def GET_SIEDZIBA_AKCEPTANTA = "getSiedzibaAkceptanta"
    private static final def GET_DANE_AKCEPTANTA = "getDaneAkceptanta"
    private static final def GET_NAZWA_BANKU = "getNazwaBanku"
    private static final def GET_MIASTO = "getMiastoComboBox"
    private static final def GET_NUMER_RACHUNKU_BANKOWEGO = "getNumerRachunkuBankowego"
    private static final def GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba1UprawnionaDoPodpisaniaUmowy"
    private static final def GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba2UprawnionaDoPodpisaniaUmowy"
    private static final def GET_OSOBA_DO_KONTAKTU = "getOsobaDoKontaktu"
    private static final def GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA = "getOsobaKtoraPozyskalaAkceptanta"
    private static final def GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID = "getPromocyjneObinzenieOplatGrid"
    private static final def GET_WYKAZ_PUNKTOW_GRID = "getWykazPunktowGrid"
    private static final def GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID = "getZakresUruchomieniaPunktyGrid"
    private static final def GET_RODZAJ_DZIALALNOSCI_BY_MCC = "getRodzajDzialanosciByMCC"
	private static final def GET_ANEKS_DO_UMOWY_NAJMU_ZESTAWU_POS = "getAneksDoUmowyNajmuZestawuPos"
	private static final def GET_ANEKS_DO_UMOWY_PREPAID = "getAneksDoUmowyPrepaid"
	private static final def GET_CBD_POINT_BY_ID = "getCbdPointById"
	private static final def SPRAWDZ_DZIALANIE = "sprawdzDzialanie"
    private static final def GET_NUMER_SPRZEDAZOWY = "getNumerSprzedazowy"
    private static final def CZY_GIFT = "czyGift"
    private static final def GET_TERMINAL_PRICES_AND_COUNTS = "getTerminalPriceAndCountByNip"
    private static final def GET_HIRE_PAYMENT_BY_POINT = "getHirePaymentByPoint"
    private static final def GET_HIRE_PAYMENT_BY_POS = "getHirePaymentByPos"
    private static final def GET_HIRE_PAYMENT_BY_PROCESS = "getHirePaymentByProcess"

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findCalculatorByNip(def clientNip) {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return findCalculatorByNipMock(clientNip);
            default:
                return cbdDAO.selectMany(FIND_CALC_BY_NIP,[nip:clientNip]).collect{ [POLEAPREEL:it.POLE,WARTOSCAPREEL:it.WARTOSC]}//*.POLEAPREEL
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findCalculatorIdByNip(def clientNip) {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
                return findCalculatorIdByNipMock(clientNip);
            default:
                return cbdDAO.selectOne(FIND_CALC_ID_BY_NIP,[nip:clientNip])?.get("KAK_ID")
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findClientByNip(def clientNip) {
        switch (Environment.getCurrent().getName()) {
            case EumowyCustomEnvironment.MOCK.getName():
               return findClientIdByNipMock(clientNip);
            default:
                def rowResult = cbdDAO.selectOne(FIND_CLIENT_ID_BY_NIP,[nip:clientNip])
                def cbdClient = new Client(rowResult)
                return cbdClient
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
    def getNazwaBanku(def accountShortNum) {
        return cbdDAO.selectOne(GET_NAZWA_BANKU,[num: accountShortNum])
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getMiasto(def code) {
        return cbdDAO.selectMany(GET_MIASTO,[code: code])
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
        return cbdDAO.selectMany(GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID,[nip:clientNip])
    }

  //  @Cacheable(value="getSiedzibaAkceptanta")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getSiedzibaAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_SIEDZIBA_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="getWykazPunktowGrid")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getWykazPunktowGrid(def clientNip) {
        return cbdDAO.selectMany(GET_WYKAZ_PUNKTOW_GRID,[nip:clientNip])
    }

    @Cacheable(value="getZakresUruchomieniaPunktyGrid")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getZakresUruchomieniaPunktyGrid(def clientNip) {
        return cbdDAO.selectMany(GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID,[nip:clientNip])
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getRodzajDzialalnosciByMCC(def mcc) {
        return cbdDAO.selectOne(GET_RODZAJ_DZIALALNOSCI_BY_MCC,[mcc: mcc])
    }
	
	@Cacheable(value="getAneksDoUmowyNajmuZestawuPos")
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
	def getAneksDoUmowyNajmuZestawuPos(def clientNip) {
		return cbdDAO.selectOne(GET_ANEKS_DO_UMOWY_NAJMU_ZESTAWU_POS,[nip:clientNip])
	}
	
	@Cacheable(value="getAneksDoUmowyPrepaid")
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
	def getAneksDoUmowyPrepaid(def clientNip) {
		return cbdDAO.selectOne(GET_ANEKS_DO_UMOWY_PREPAID,[nip:clientNip])
	}

    @Cacheable(value="getPosTypes")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPosTypes(def query, def clientNip, def medium) {
        return cbdDAO.selectMany(query,[nip:clientNip, medium: medium]);
    }

    @Cacheable(value="getCbdPoints")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getCbdPoints(def query, def clientNip) {
        return cbdDAO.selectMany(query,[nip:clientNip]);
    }
	
	@Cacheable(value="getCbdPointById")
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
	def getCbdPointById(def clientNip, def cbdId) {
		return cbdDAO.selectOne(GET_CBD_POINT_BY_ID, [nip: clientNip, cbdid: cbdId])
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
	def checkActivities(def activitiesString, def calcId) {
		def rowResult = cbdDAO.selectOne(SPRAWDZ_DZIALANIE, [activities: activitiesString, calcid: calcId])
		return rowResult != null && rowResult.get("result") == 1
	}

    @Cacheable(value="getNumerSprzedazowy")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNumerSprzedazowy(def auwId) {
        def rowResult = cbdDAO.selectOne(GET_NUMER_SPRZEDAZOWY, [auwId: auwId])
        return rowResult.get("numer")
    }

    @Cacheable(value="czyGift")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def czyGift(def nip) {
        def rowResult = cbdDAO.selectOne(CZY_GIFT, [nip: nip])
        return rowResult != null && rowResult.get("result") == 1
    }

    @Cacheable(value="getTerminalPricesAndCounts")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getTerminalPricesAndCounts(def nip) {
        return cbdDAO.selectOne(GET_TERMINAL_PRICES_AND_COUNTS, [nip: nip])
    }

    @Cacheable(value="getHirePaymentByPoint")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByPoint(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_POINT, [nip: nip])
    }

    @Cacheable(value="getHirePaymentByPos")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByPos(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_POS, [nip: nip])
    }

    @Cacheable(value="getHirePaymentByProcess")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByProcess(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_PROCESS, [nip: nip])
    }

    /**
     * MOCK
     * */
    def findClientIdByNipMock(String nip) {
        def cbdId;
        def id

        if(nip.equals("1234567819")){
            cbdId = "11";
        }
        else if(nip.equals( "8946001495")){
            cbdId = "22";
        }
        else if(nip.equals( "7343597142")){
            cbdId = "33";
        }
        else if(nip.equals( "3558335706")){
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

    def findCalculatorIdByNipMock(def kln_id) {
        if(kln_id == "1234567819"){
            return "1111111111111111111";
        }
        else if(kln_id == "8946001495"){
            return "2222222222222222222";
        }
        else if(kln_id == "7343597142"){
            return "1234567890123456789";
        }
    }

    def findCalculatorByNipMock(def kln_id) {
        def calc;

        if(kln_id == "1234567819"){
            calc = []
            calc.add([POLEAPREEL:"STAWKA_PP_ORANGE",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"OPLATA_ZA_APL_PP",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"NIP",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"CZY_TELEPOMPKA",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"OPLATA_IFPLUS_DINERSCLUB",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_MUNDIO",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_LYCA",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_T_MOBILE",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_VIRGIN",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_PLAY",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_PLUS",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_GALENA",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"DEKLARACJA_SPRZEDAZY_PP",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"OPLATA_DCC",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"NULL",WARTOSCAPREEL:"BRAK"]);
        }
        else if(kln_id == "8946001495"){
            calc = []
            calc.add([POLEAPREEL:"STAWKA_PP_PLUS",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"STAWKA_PP_GALENA",WARTOSCAPREEL:"BRAK"]);
            calc.add([POLEAPREEL:"DEKLARACJA_SPRZEDAZY_PP",WARTOSCAPREEL:"BRAK"]);
        }

        else if(kln_id == "7343597142"){
            def calcFields =  CalcField.findAll();
            calc = calcFields.collect{[POLEAPREEL:it.name,WARTOSCAPREEL:"BRAK"]}
        }

        return calc;
    }

}