package com.eservice.eumowy
import com.eservice.eumowy.dao.CbdDAO
import com.google.common.base.Function
import com.google.common.collect.FluentIterable
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.sql.GroovyRowResult
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Nullable

class CbdService {

    CbdDAO cbdDAO

    private static final String FIND_CLIENT_ID_BY_NIP = "findClientIdByNip"
    private static final String FIND_CALC_ID_BY_NIP = "findCalcIdByNip"
    private static final String FIND_CALC_BY_NIP = "findCalcByNip"
    private static final String GET_ADRES_DANE_DO_WYDRUKU = "getAdresDaneDoWydruku"
    private static final String GET_ADRES_DO_KORESPONDENCJI = "getAdresDoKorespondencji"
    private static final String GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM = "getAdresDoKorespondencjizAkceptantem"
    private static final String GET_SIEDZIBA_AKCEPTANTA = "getSiedzibaAkceptanta"
    private static final String GET_DANE_AKCEPTANTA = "getDaneAkceptanta"
    private static final String GET_NAZWA_BANKU = "getNazwaBanku"
    private static final String GET_MIASTO = "getMiastoComboBox"
    private static final String GET_NUMER_RACHUNKU_BANKOWEGO = "getNumerRachunkuBankowego"
    private static final String GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba1UprawnionaDoPodpisaniaUmowy"
    private static final String GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY = "getOsoba2UprawnionaDoPodpisaniaUmowy"
    private static final String GET_OSOBA_DO_KONTAKTU = "getOsobaDoKontaktu"
    private static final String GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA = "getOsobaKtoraPozyskalaAkceptanta"
    private static final String GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID = "getPromocyjneObinzenieOplatGrid"
    private static final String GET_WYKAZ_PUNKTOW_GRID = "getWykazPunktowGrid"
    private static final String GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID = "getZakresUruchomieniaPunktyGrid"
    private static final String GET_RODZAJ_DZIALALNOSCI_BY_MCC = "getRodzajDzialanosciByMCC"
    private static final String GET_ANEKS_DO_UMOWY_NAJMU_ZESTAWU_POS = "getAneksDoUmowyNajmuZestawuPos"
    private static final String GET_ANEKS_DO_UMOWY_PREPAID = "getAneksDoUmowyPrepaid"
    private static final String GET_KALKULATOR_TYP_URZADZEN = "getKalkulatorTypUrzadzen"
    private static final String GET_CBD_POINT_BY_ID = "getCbdPointById"
    private static final String SPRAWDZ_DZIALANIE = "sprawdzDzialanie"
    private static final String GET_NUMER_SPRZEDAZOWY = "getNumerSprzedazowy"
    private static final String CZY_GIFT = "czyGift"
    private static final String SET_KALKULATOR_ACCEPTED = "setKalkulatorAccepted"
    private static final String SET_KALKULATOR_USED = "setKalkulatorUsed"
    private static final String GET_TERMINAL_PRICES_AND_COUNTS = "getTerminalPriceAndCountByNip"
    private static final String GET_HIRE_PAYMENT_BY_POINT = "getHirePaymentByPoint"
    private static final String GET_HIRE_PAYMENT_BY_POS = "getHirePaymentByPos"
    private static final String GET_HIRE_PAYMENT_BY_PROCESS = "getHirePaymentByProcess"
    private static final String GET_PREPAID_EVOUCHER = "getPrepaidEvoucher"
    private static final String GET_PREPAID_TOPUP = "getPrepaidTopup"
    private static final String GET_OPIEKA_SERWISOWA_I = "getOpiekaSerwisowa1"
    private static final String GET_OPIEKA_SERWISOWA_II = "getOpiekaSerwisowa2"
    private static final String MIN_CENA_NAJMU = "minCenaNajmu"
    private static final String CZY_POPRAWNY_MID = "cbd_validation/czyPoprawnyMid"
    private static final String CZY_CASHBACK = "cbd_validation/czyCashback"
    private static final String CZY_PROWIZJA_DLA_AKCEPTANTA = "cbd_validation/czyProwizjaDlaAkceptanta"
    private static final String CZY_TERMINAL_CASHBACK = "cbd_validation/czyTerminalCashback"
    private static final String CZY_TERMINAL_DCC = "cbd_validation/czyTerminalDCC"
    private static final String GET_RODZAJ_UMOWY = "cbd_validation/getRodzajUmowy"
    private static final String GET_UMW_TYP = "cbd_validation/getUmwTyp"

    def logggger = Logger.getLogger("calcAppender")

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findCalculatorByNip(def clientNip) {
        def calcId = findCalculatorIdByNip(clientNip)
        def calc = cbdDAO.selectMany(FIND_CALC_BY_NIP,[nip:clientNip]).collect{ [POLEAPREEL:it.POLE,WARTOSCAPREEL:it.WARTOSC]}
        logggger.info("Getting calculator for NIP ${clientNip}, calculatorId: ${calcId} with values: ${calc}")
        return calc
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def findCalculatorIdByNip(def clientNip) {
        return cbdDAO.selectOne(FIND_CALC_ID_BY_NIP,[nip:clientNip])?.get("KAK_ID")
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    Client findClientByNip(def clientNip) {
        def rowResult = cbdDAO.selectOne(FIND_CLIENT_ID_BY_NIP,[nip:clientNip])
        def cbdClient = new Client(rowResult)
        return cbdClient
    }

    @Cacheable(value="eumowyCacheShort", key = "'getAdresDaneDoWydruku_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def  getAdresDaneDoWydruku(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DANE_DO_WYDRUKU,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getAdresDoKorespondencji_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencji(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJI,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getAdresDoKorespondencjizAkceptantem_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAdresDoKorespondencjizAkceptantem(def clientNip) {
        return cbdDAO.selectOne(GET_ADRES_DO_KORESPONDENCJIZ_AKCEPTANTEM,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getDaneAkceptanta_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getDaneAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_DANE_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getNazwaBanku_'.concat(#accountShortNum)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNazwaBanku(def accountShortNum) {
        return cbdDAO.selectOne(GET_NAZWA_BANKU,[num: accountShortNum])
    }

    @Cacheable(value="eumowyCacheLong", key = "'getMiasto_'.concat(#code)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getMiasto(def code) {
        return cbdDAO.selectMany(GET_MIASTO,[code: code])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getNumerRachunkuBankowego_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNumerRachunkuBankowego(def clientNip) {
        return cbdDAO.selectOne(GET_NUMER_RACHUNKU_BANKOWEGO,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOsoba1UprawnionaDoPodpisaniaUmowy_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba1UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA1_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOsoba2UprawnionaDoPodpisaniaUmowy_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsoba2UprawnionaDoPodpisaniaUmowy(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA2_UPRAWNIONA_DO_PODPISANIA_UMOWY,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOsobaDoKontaktu_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaDoKontaktu(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA_DO_KONTAKTU,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOsobaKtoraPozyskalaAkceptanta_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOsobaKtoraPozyskalaAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_OSOBA_KTORA_POZYSKALA_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getPromocyjneObinzenieOplatGrid_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPromocyjneObinzenieOplatGrid(def clientNip) {
        return cbdDAO.selectMany(GET_PROMOCYJNE_OBINZENIE_OPLAT_GRID,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getSiedzibaAkceptanta_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getSiedzibaAkceptanta(def clientNip) {
        return cbdDAO.selectOne(GET_SIEDZIBA_AKCEPTANTA,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getWykazPunktowGrid_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getWykazPunktowGrid(def clientNip) {
        return cbdDAO.selectMany(GET_WYKAZ_PUNKTOW_GRID,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getZakresUruchomieniaPunktyGrid_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getZakresUruchomieniaPunktyGrid(def clientNip) {
        return cbdDAO.selectMany(GET_ZAKRES_URUCHOMIENIA_PUNKTY_GRID,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheLong", key = "'getRodzajDzialalnosciByMCC_'.concat(#mcc)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getRodzajDzialalnosciByMCC(def mcc) {
        return cbdDAO.selectOne(GET_RODZAJ_DZIALALNOSCI_BY_MCC,[mcc: mcc])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getAneksDoUmowyNajmuZestawuPos_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAneksDoUmowyNajmuZestawuPos(def clientNip) {
        return cbdDAO.selectOne(GET_ANEKS_DO_UMOWY_NAJMU_ZESTAWU_POS,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getAneksDoUmowyPrepaid_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getAneksDoUmowyPrepaid(def clientNip) {
        return cbdDAO.selectOne(GET_ANEKS_DO_UMOWY_PREPAID,[nip:clientNip])
    }

    @Cacheable(value="eumowyCacheLong", key = "'getPosTypes_'.concat(#medium)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPosTypes(def query, def medium) {
        return cbdDAO.selectMany(query,[medium: medium]);
    }

    @Cacheable(value="eumowyCacheLong", key = "'getExtPosTypes_'.concat(#medium).concat(#type)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPosTypes(def query, def medium, def type ) {
        return cbdDAO.selectMany(query,[medium: medium, type: type]);
    }

    @Cacheable(value="eumowyCacheLong", key="'getCalculatorDevicesTypes_'.concat(#medium)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getCalculatorDevicesTypes(def medium) {
        return cbdDAO.selectMany(GET_KALKULATOR_TYP_URZADZEN, [medium: medium])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getCbdPoints_'.concat(#clientNip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getCbdPoints(def query, def clientNip) {
        return cbdDAO.selectMany(query,[nip:clientNip]);
    }

    @Cacheable(value="eumowyCacheShort", key = "'getCbdPointById_'.concat(#clientNip).concat('_').concat(#cbdId)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getCbdPointById(def clientNip, def cbdId) {
        return cbdDAO.selectOne(GET_CBD_POINT_BY_ID, [nip: clientNip, cbdid: cbdId])
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def checkActivities(def activitiesString, def calcId, def signaturesString) {
        def rowResult = cbdDAO.selectOne(SPRAWDZ_DZIALANIE, [activities: activitiesString, calcid: calcId, signatures: signaturesString])
        def result = rowResult != null && rowResult.get("result") == 1
        logggger.info("Checking activities for calcId ${calcId} with result: ${result}")
        return result
    }

    @Cacheable(value="eumowyCacheShort", key = "'getNumerSprzedazowy_'.concat(#auwId)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getNumerSprzedazowy(def auwId) {
        def rowResult = cbdDAO.selectOne(GET_NUMER_SPRZEDAZOWY, [auwId: auwId])
        return rowResult.get("numer")
    }

    @Cacheable(value="eumowyCacheShort", key = "'czyGift_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def czyGift(def nip) {
        def rowResult = cbdDAO.selectOne(CZY_GIFT, [nip: nip])
        return rowResult != null && rowResult.get("result") == 1
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean acceptKalkulatorAndGetResult(String calcId) {
        def rowResult = cbdDAO.selectOne(SET_KALKULATOR_ACCEPTED, [calcid: calcId])
        return rowResult != null && rowResult.get("result") == 0
    }

    @Cacheable(value="eumowyCacheShort", key = "'setKalkulatorUsed_'.concat(#calcId)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def setKalkulatorUsed(def calcId) {
        def rowResult = cbdDAO.selectOne(SET_KALKULATOR_USED, [calcid: calcId])
        return rowResult != null && rowResult.get("result")
    }

    @Cacheable(value="eumowyCacheShort", key = "'getTerminalPricesAndCounts_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getTerminalPricesAndCounts(def nip) {
        return cbdDAO.selectMany(GET_TERMINAL_PRICES_AND_COUNTS, [nip: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getHirePaymentByPoint_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByPoint(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_POINT, [nip: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getHirePaymentByPos_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByPos(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_POS, [nip: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getHirePaymentByProcess_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getHirePaymentByProcess(def nip) {
        return cbdDAO.selectMany(GET_HIRE_PAYMENT_BY_PROCESS, [nip: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getPrepaidEvoucher_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPrepaidEvoucher(def nip) {
        return cbdDAO.selectOne(GET_PREPAID_EVOUCHER, [NIP: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getPrepaidTopup_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getPrepaidTopup(def nip) {
        return cbdDAO.selectOne(GET_PREPAID_TOPUP, [NIP: nip])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOpiekaSerwisowa1_'.concat(#code)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOpiekaSerwisowaOne(def code) {
        return cbdDAO.selectOne(GET_OPIEKA_SERWISOWA_I,[kod: code])
    }

    @Cacheable(value="eumowyCacheShort", key = "'getOpiekaSerwisowa2_'.concat(#code)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getOpiekaSerwisowaTwo(def code) {
        return cbdDAO.selectOne(GET_OPIEKA_SERWISOWA_II,[kod: code])
    }

    @Cacheable(value="eumowyCacheLong", key = "'getMccComboBox'")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    def getMccCodes(def query) {
        return cbdDAO.selectMany(query, [])
    }

    @Cacheable(value="eumowyCacheShort", key = "'minCenaNajmu_'.concat(#nip)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    BigDecimal getMinRentPrice(def nip) {
        GroovyRowResult cena = cbdDAO.selectOne(MIN_CENA_NAJMU, [nip: nip])
        return cena ? cena[0] : BigDecimal.ZERO
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean isMidCorrect(String mid) {
        GroovyRowResult result = cbdDAO.selectOne(CZY_POPRAWNY_MID, [mid: mid])
        return result ? !result.isEmpty() : false
    }

    @Cacheable(value="eumowyCacheLong", key = "'czyCashback_'.concat(#clientId)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean czyCashback(Long clientId) {
        GroovyRowResult result = cbdDAO.selectOne(CZY_CASHBACK, [clientId: clientId])
        return result ? !result.isEmpty() : false
    }

    @Cacheable(value="eumowyCacheLong", key = "'czyProwizjaDlaAkceptanta_'.concat(#clientId)")
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean czyProwizjaDlaAkceptanta(Long clientId) {
        GroovyRowResult result = cbdDAO.selectOne(CZY_PROWIZJA_DLA_AKCEPTANTA, [clientId: clientId])
        return result ? !result?.isEmpty() : false
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean czyTermialCashback(String mid) {
        GroovyRowResult result = cbdDAO.selectOne(CZY_TERMINAL_CASHBACK, [mid: mid])
        return result ? !result?.isEmpty() : false
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    boolean czyTerminalDcc(String mid) {
        GroovyRowResult result = cbdDAO.selectOne(CZY_TERMINAL_DCC, [mid: mid])
        return result ? !result?.isEmpty() : false
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    String getRodzajUmowy(Long clientId) {
        GroovyRowResult result = cbdDAO.selectOne(GET_RODZAJ_UMOWY, [clientId: clientId])

        if (result?.containsKey("KDD_RODZAJ_UMOWY")) {
            return result.getProperty("KDD_RODZAJ_UMOWY")
        }

        return ""
    }


    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<String> getUmwTypes(Long clientId) {
        ArrayList<GroovyRowResult> result = cbdDAO.selectMany(GET_UMW_TYP, [clientId: clientId])

        return FluentIterable
            .from(result)
            .transform(new Function<GroovyRowResult, String>() {
                @Override
                String apply(@Nullable GroovyRowResult groovyRowResult) {
                    return groovyRowResult.getProperty("UMW_TYP")
                }
             })
            .toList()
    }

    @CacheEvict(value=["eumowyCacheShort","eumowyCacheLong"], allEntries=true)
    def invalidateCaches(){
        log.info("Invalidating caches!!!")
    }
}