package com.eservice.eumowy.command

import com.eservice.eumowy.Process
import com.eservice.eumowy.annotation.DateField
import com.eservice.eumowy.annotation.Omit
import grails.util.Environment
import grails.validation.Validateable
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils

import java.util.regex.Pattern

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:22
 *
 */

@Validateable
class ProcessCommand implements Serializable {

    @Omit()
    transient def calculatorService
    @Omit
    transient def calc

    @Omit
    static int MAX_PRICE_GROUP_SIZE = 3

    //UWAGA - kazde nowe pole, ktore ma byc pomijane w zapisie do bazy trzeba dodac tez w
    //ProcessService.getDataFromPanels(). Gdy sie tego nie zrobi zapisuja sie dane a pozniej leci
    // NoSuchFieldException z ProcessService.loadProcessData() przy probie usuniecia zbednej metody
    @Omit
    static def nullableTrueBlankFalse = {/** puste ale potrzebne */}

    @Omit
    static def atLeastClosure = { value, cmd, errors, property, calcProperty ->
        def calcValue = cmd.calculatorService.getCalcProperty(cmd.calc, calcProperty)

        log.info("property: ${property}, value: ${value}, cal:${calcValue}")

        //warunek na brak wartości w kalkulatorze lub wartość domyślną w panelu
        if (DEFAULT_VALUE.equals(value) || !calcValue) {
            return true
        }

        def minValue = calcValue?.toString()?.isNumber() ? calcValue.toString().toBigDecimal() : BigDecimal.ZERO
        def currValue = value?.toString()?.isNumber() ? value.toString().toBigDecimal() : BigDecimal.ZERO

        if (currValue.compareTo(minValue) < 0) {
            //errors.rejectValue(property, "default.atLeast.asCalc", [property] as Object[], "Podana warto\u015B\u0107 dla pola {0} nie mo\u017Ce by\u0107 mniejsza ni\u017C pobrana z kalkulatora.")
            errors.rejectValue(property, "default.atLeast.asCalc",[property] as Object[], "")
            return false
        }
        return true
    }

    @Omit
    static def checkTerminalNumber = { value, cmd, errors ->
        if (Environment.getCurrent().equals(Environment.TEST)) {
            return true
        }

        def max = value ? Integer.valueOf(value) : 0
        def counter = 0

        cmd.points?.each { point ->
            counter += point?.dialupIlosc != null ? point?.dialupIlosc : 0
            counter += point?.vpnIlosc != null ? point?.vpnIlosc : 0
            counter += point?.sslIlosc != null ? point?.sslIlosc : 0
            counter += point?.gprsIlosc != null ? point?.gprsIlosc : 0
            counter += point?.pinPadIlosc != null ? point?.pinPadIlosc : 0
            counter += point?.wifiIlosc != null ? point?.wifiIlosc : 0
        }

        cmd.poses?.each { point ->
            counter += point?.dialupIlosc != null ? point?.dialupIlosc : 0
            counter += point?.vpnIlosc != null ? point?.vpnIlosc : 0
            counter += point?.sslIlosc != null ? point?.sslIlosc : 0
            counter += point?.gprsIlosc != null ? point?.gprsIlosc : 0
            counter += point?.pinPadIlosc != null ? point?.pinPadIlosc : 0
            counter += point?.wifiIlosc != null ? point?.wifiIlosc : 0
        }

        if (counter == 0 ){
            // warunek o tyle poprawny ze z poziomu klienta uniemozliwamy przejsie dalej bez dodania jakiegokolwiek punktu lub pos
            log.debug "no points/pos were added"
            return true
        }


        log.info "liczba dodanych terminali w eUmowy [${counter}], dozwolona [${max}]"

        if (counter != max) {
            errors.rejectValue("liczbaTerminali", "default.notEqual.liczbaTerminali",[counter, max] as Object[], "")
            return false
        }
        return true
    }

    @Omit
    static def maxLengthClosure = { value, cmd, errors, maxValue, propertyName, message ->
        if (value.length() > maxValue) {
            errors.rejectValue(propertyName, message)
            return false
        }
        return true
    }

    @Omit
    static def skipAddressValidationClosure = { value, cmd, errors, propertyName, message ->
        if(value.isEmpty() && cmd.checkIfClientFromCbd()){
            return true
        }
        if(value.isEmpty()){ //cannot use contraint blank: true because of cbd values
            errors.rejectValue(propertyName, message)
            return false
        }
        return true
    }

    @Omit
    static def checkTelekodzik = { value, cmd, errors, propertyName ->
        if(cmd.doladowania_tk && value?.isEmpty() ){
            errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
            return false
        }
        return true
    }

    @Omit
    static def checkTelepompka = { value, cmd, errors, propertyName ->
        if(cmd.doladowania_tp && value?.isEmpty() ){
            errors.rejectValue(propertyName, "default.validation.required.error", "Pole wymagane")
            return false
        }
        return true
    }

    @Omit
    static def regexpValidationClosure = { value, errors, propertyName, patternStr, message ->
        log.trace("regexpValidationClosure - propertyName:"+propertyName+" value:"+value + " pattern:"+patternStr)

        if(!value){
            return true;
        }

        Pattern pattern = Pattern.compile(patternStr)
        if (!pattern.matcher(value).matches()){
            errors.rejectValue(propertyName, message,[value, propertyName] as Object[], "")
            return false
        }
        return true
    }

    @Omit
    static def numberValidationClosure = {value, cmd,  errors, propertyName ->
        return cmd.regexpValidationClosure.call(value, errors, propertyName, '~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$',"default.validation.number.error")
    }

    @Omit
    static def percentageValidationClosure = {value, cmd,  errors, propertyName ->
        cmd.regexpValidationClosure.call(value, errors, propertyName, "~|\\-|^(?:100(?:.0(?:0)?)?|\\d{1,2}(?:.\\d{1,2})?)", "default.validation.number.error")
    }

    @Omit
    static def customValidationClosure = {value, cmd,  errors, propertyName, regex ->
        cmd.regexpValidationClosure.call(value, errors, propertyName,regex, "default.validation.regex.error")
    }

    @Omit
    static def DEFAULT_VALUE = "~"

    //adresDoKorespondencjizAkecptantem - FINISH
    String akceptantKontaktUlicaTytul = DEFAULT_VALUE
    String akceptantKontaktUlica = DEFAULT_VALUE
    String akceptantKontaktNrDomu = DEFAULT_VALUE
    String akceptantKontaktNrMieszkania = DEFAULT_VALUE

    String akceptantKontaktMiasto = DEFAULT_VALUE
    String akceptantKontaktKodPocztowy = DEFAULT_VALUE
    String akceptantKontaktPoczta = DEFAULT_VALUE

//    aneksDoUmowyNajmuZestawuPos - FINISH
    @DateField
    String dataAneksowanejUmowyPos = DEFAULT_VALUE

//    aneksDoUmowyPrepaid - FINISH
    @DateField
    String dataAneksowanejUmowyPrepaid = DEFAULT_VALUE

//    czasObowiazywaniaUmowy - FINISH
    String umowaCzas = DEFAULT_VALUE
    @DateField
    String umowaOznOd = DEFAULT_VALUE
    @DateField
    String umowaOznDo = DEFAULT_VALUE

//    daneAkceptanta
    String akceptantNazwaOficjalna = DEFAULT_VALUE
    String akceptantNazwaSieciowa = DEFAULT_VALUE
    //String akceptantNip - trzymane we wspolnym polu nip
    String akceptantRegon = DEFAULT_VALUE

    String akceptantNazwaOficjalnaCbd = DEFAULT_VALUE
    String akceptantNazwaSieciowaCbd = DEFAULT_VALUE
    String akceptantRegonCbd = DEFAULT_VALUE

//    daneDoWydruku
    String nazwaDoWydrukuZTerminalaPos = DEFAULT_VALUE
    String wydrukNazwaDoWyszukwarki = DEFAULT_VALUE

    String wydrukUlicaTytul = DEFAULT_VALUE
    String wydrukUlica = DEFAULT_VALUE
    String wydrukNrDomu = DEFAULT_VALUE
    String wydrukNrMieszkania = DEFAULT_VALUE

    String wydrukMiasto = DEFAULT_VALUE
    String wydrukKodPocztowy = DEFAULT_VALUE
    String wydrukPoczta = DEFAULT_VALUE

    String wydrukLinia1 = DEFAULT_VALUE
    String wydrukLinia2 = DEFAULT_VALUE

//    danePunktu

    //TODO - co to jest???
    boolean czyDcc

    String oplataVISA = DEFAULT_VALUE
    String oplataVISAPr = DEFAULT_VALUE
    String oplataMasterCard = DEFAULT_VALUE
    String oplataMasterCardPr = DEFAULT_VALUE
    String oplataMaestro = DEFAULT_VALUE
    String oplataMaestroPr = DEFAULT_VALUE

//    dccZakresUruchomienia
    String dccZakresUruchomienia = DEFAULT_VALUE

//    deklaracjeAkceptanta - FINISH
    String informacjaHandlowa = DEFAULT_VALUE

//    dodajPunkt
//    dodatkoweUslugi - FINISH
    String oplataZaDzienneZestawienieTransakcji = DEFAULT_VALUE
    String oplataZaMiesieczneZestawienieTransakcji = DEFAULT_VALUE
    String oplataZaPotwierdzenieWykonaniaPrzelewu = DEFAULT_VALUE
    String oplataZaDostarczeniePapieru = DEFAULT_VALUE
    String oplataZaZmianeGrafiki = DEFAULT_VALUE
    String oplataZaInstalacjePOS = DEFAULT_VALUE
    String oplataZaInstalacjeGPRS = DEFAULT_VALUE
    String oplataZaUruchomienieWalutyObcej = DEFAULT_VALUE

//    dodatkoweUslugi2
    String wydrukGrafikiCena = DEFAULT_VALUE
    String dzialaniaMatematyczneCena = DEFAULT_VALUE
    String pierwszaSesjaCena = DEFAULT_VALUE

//    dodatkoweUslugiMud - FINISH
    String mudCena = DEFAULT_VALUE

//    dodatkoweUslugiUTAIntegracja
    String weryfikacjaPINCena = DEFAULT_VALUE
    String systemKasowyCena = DEFAULT_VALUE

//    formaDoladowania - FINISH
    Boolean doladowania_tp
    Boolean doladowania_tk
    String srednia_sprzedaz_doladowan = DEFAULT_VALUE
    String srednia_sprzedaz_doladowan_slownie = DEFAULT_VALUE

//    ifplus - FINISH
    String ifOplataVISA = DEFAULT_VALUE
    String ifOplataMasterCard = DEFAULT_VALUE
    String ifOplataDinersClub = DEFAULT_VALUE
    String ifOplataIKO = DEFAULT_VALUE
    String ifOplataPKOPB = DEFAULT_VALUE

//    informacjeDodatkowe - FINISH
    String dzialalnoscForma = DEFAULT_VALUE
    String dzialalnoscFormaInna = DEFAULT_VALUE
    String dzialalnoscDokument = DEFAULT_VALUE
    String dzialalnoscDokumentInny = DEFAULT_VALUE

//    okresLojalnosciowy
    String okresLojalnosciowy = DEFAULT_VALUE

//    oplatyDCC
    String oplataZaPlatnoscWInnejWalucie = DEFAULT_VALUE
//    osobaDoKontaktu
    String kontaktTytul = DEFAULT_VALUE
    String kontaktImie = DEFAULT_VALUE
    String kontaktNazwisko = DEFAULT_VALUE
    String kontaktTelStacjonarny = DEFAULT_VALUE
    String kontaktTelKomorkowy = DEFAULT_VALUE
    String kontaktEmail = DEFAULT_VALUE

//    osobaKtoraPozyskalaAkceptanta  - FINISH (nie wystepuje w pdfach)
    String pozyskujacyTytul = DEFAULT_VALUE
    String pozyskujacyImie = DEFAULT_VALUE
    String pozyskujacyNazwisko = DEFAULT_VALUE
    String pozyskujacyNumer = DEFAULT_VALUE

//    osobaUprawnionaDoPodpisaniaUmowy - FINISH
    String reprezentant1Tytul = DEFAULT_VALUE
    String reprezentant1Imie = DEFAULT_VALUE
    String reprezentant1Nazwisko = DEFAULT_VALUE
    String reprezentant2Tytul = DEFAULT_VALUE
    String reprezentant2Imie = DEFAULT_VALUE
    String reprezentant2Nazwisko = DEFAULT_VALUE
    String emailDoWysylkiDokumentu = DEFAULT_VALUE

//    poziomOplatiWarunkiPlatnosciKarty - FINISH
    String visaEUKKOPr = DEFAULT_VALUE
    String visaEUKDPr = DEFAULT_VALUE
    String visaEUKBPr = DEFAULT_VALUE
    String visaOutEUKKOPr = DEFAULT_VALUE
    String visaOutEUKDPr = DEFAULT_VALUE
    String visaOutEUKBPr = DEFAULT_VALUE
    String visaPolskaKKO1Pr = DEFAULT_VALUE
    String visaPolskaKKO2Pr = DEFAULT_VALUE
    String visaPolskaKD1Pr = DEFAULT_VALUE
    String visaPolskaKD2Pr = DEFAULT_VALUE
    String visaPolskaKBPr = DEFAULT_VALUE
    String mastercardEUKKPr = DEFAULT_VALUE
    String mastercardEUKDPr = DEFAULT_VALUE
    String mastercardEUKBLPr = DEFAULT_VALUE
    String mastercardEUMPr = DEFAULT_VALUE
    String mastercardOutEUKKPr = DEFAULT_VALUE
    String mastercardOutEUKDPr = DEFAULT_VALUE
    String mastercardOutEUKBPr = DEFAULT_VALUE
    String mastercardOutEUMPr = DEFAULT_VALUE
    String mastercardPolskaKK1Pr = DEFAULT_VALUE
    String mastercardPolskaKK2Pr = DEFAULT_VALUE
    String mastercardPolskaKK3Pr = DEFAULT_VALUE
    String mastercardPolskaKD1Pr = DEFAULT_VALUE
    String mastercardPolskaKD2Pr = DEFAULT_VALUE
    String mastercardPolskaKD3Pr = DEFAULT_VALUE
    String mastercardPolskaKBPr = DEFAULT_VALUE
    String mastercardPolskaM1Pr = DEFAULT_VALUE
    String mastercardPolskaM2Pr = DEFAULT_VALUE
    String mastercardPolskaM3Pr = DEFAULT_VALUE
    String visaPKOBPKKO1Pr = DEFAULT_VALUE
    String visaPKOBPKKO2Pr = DEFAULT_VALUE
    String visaPKOBPKD1Pr = DEFAULT_VALUE
    String visaPKOBPKD2Pr = DEFAULT_VALUE
    String visaPKOBPKB3Pr = DEFAULT_VALUE
    String mastercardPKOBPKK1Pr = DEFAULT_VALUE
    String mastercardPKOBPKK2Pr = DEFAULT_VALUE
    String mastercardPKOBPKK3Pr = DEFAULT_VALUE
    String mastercardPKOBPKD1Pr = DEFAULT_VALUE
    String mastercardPKOBPKD2LPr = DEFAULT_VALUE
    String mastercardPKOBPKD3Pr = DEFAULT_VALUE
    String mastercardPKOBPKBPr = DEFAULT_VALUE
    String mastercardPKOBPM1Pr = DEFAULT_VALUE
    String mastercardPKOBPM2Pr = DEFAULT_VALUE
    String mastercardPKOBPM3Pr = DEFAULT_VALUE
    String dinersClubPr = DEFAULT_VALUE
    String ikoPr = DEFAULT_VALUE
    String visaEUKKOSt = DEFAULT_VALUE
    String visaEUKDSt = DEFAULT_VALUE
    String visaEUKBSt = DEFAULT_VALUE
    String visaOutEUKKOSt = DEFAULT_VALUE
    String visaOutEUKDSt = DEFAULT_VALUE
    String visaOutEUKBSt = DEFAULT_VALUE
    String visaPolskaKKO1St = DEFAULT_VALUE
    String visaPolskaKKO2St = DEFAULT_VALUE
    String visaPolskaKD1St = DEFAULT_VALUE
    String visaPolskaKD2St = DEFAULT_VALUE
    String visaPolskaKBSt = DEFAULT_VALUE
    String mastercardEUKKSt = DEFAULT_VALUE
    String mastercardEUKDSt = DEFAULT_VALUE
    String mastercardEUKBLSt = DEFAULT_VALUE
    String mastercardEUMSt = DEFAULT_VALUE
    String mastercardOutEUKKSt = DEFAULT_VALUE
    String mastercardOutEUKDSt = DEFAULT_VALUE
    String mastercardOutEUKBSt = DEFAULT_VALUE
    String mastercardOutEUMSt = DEFAULT_VALUE
    String mastercardPolskaKK1St = DEFAULT_VALUE
    String mastercardPolskaKK2St = DEFAULT_VALUE
    String mastercardPolskaKK3St = DEFAULT_VALUE
    String mastercardPolskaKD1St = DEFAULT_VALUE
    String mastercardPolskaKD2St = DEFAULT_VALUE
    String mastercardPolskaKD3St = DEFAULT_VALUE
    String mastercardPolskaKBSt = DEFAULT_VALUE
    String mastercardPolskaM1St = DEFAULT_VALUE
    String mastercardPolskaM2St = DEFAULT_VALUE
    String mastercardPolskaM3St = DEFAULT_VALUE
    String visaPKOBPKKO1St = DEFAULT_VALUE
    String visaPKOBPKKO2St = DEFAULT_VALUE
    String visaPKOBPKD1St = DEFAULT_VALUE
    String visaPKOBPKD2St = DEFAULT_VALUE
    String visaPKOBPKB3St = DEFAULT_VALUE
    String mastercardPKOBPKK1St = DEFAULT_VALUE
    String mastercardPKOBPKK2St = DEFAULT_VALUE
    String mastercardPKOBPKK3St = DEFAULT_VALUE
    String mastercardPKOBPKD1St = DEFAULT_VALUE
    String mastercardPKOBPKD2LSt = DEFAULT_VALUE
    String mastercardPKOBPKD3St = DEFAULT_VALUE
    String mastercardPKOBPKBSt = DEFAULT_VALUE
    String mastercardPKOBPM1St = DEFAULT_VALUE
    String mastercardPKOBPM2St = DEFAULT_VALUE
    String mastercardPKOBPM3St = DEFAULT_VALUE
    String dinersClubSt = DEFAULT_VALUE
    String ikoSt = DEFAULT_VALUE

//    poziomOplatIWarunkiPlatnosciPP - FINISH
    String pp_orange_tk = DEFAULT_VALUE
    String pp_orange_tp = DEFAULT_VALUE
    String pp_plus_tk = DEFAULT_VALUE
    String pp_plus_tp = DEFAULT_VALUE
    String pp_tmobile_tk = DEFAULT_VALUE
    String pp_tmobile_tp = DEFAULT_VALUE
    String pp_heyah_tk = DEFAULT_VALUE
    String pp_heyah_tp = DEFAULT_VALUE
    String pp_play_tk = DEFAULT_VALUE
    String pp_play_tp = DEFAULT_VALUE
    String pp_telegrosik_tk = DEFAULT_VALUE
    String pp_virginmobile_tk = DEFAULT_VALUE
    String pp_lycamobile_tk = DEFAULT_VALUE
    String pp_gtmobile_tk = DEFAULT_VALUE
    String pp_vectonemobile_tk = DEFAULT_VALUE
    String pp_delightmobile_tk = DEFAULT_VALUE
    String oplataZaOprogramowanieDoDoladowan = DEFAULT_VALUE

//    promocyjneObnizenieOplatyZaZestawPos
//    scoring

    String scoringMcc = DEFAULT_VALUE
    String scoringDzialalnosc = DEFAULT_VALUE
    String scoringSzczegolyDzialalnosci = DEFAULT_VALUE
    String scoringWlasnosc = DEFAULT_VALUE
    String scoringDzialalnoscCzas = DEFAULT_VALUE
    String scoringKoncesja = DEFAULT_VALUE
    String rodzajZezwolenia = DEFAULT_VALUE
    String scoringCharakterystyka = DEFAULT_VALUE
    String scoringCharakterystykaInna = DEFAULT_VALUE
    String scoringWielkoscPunktu = DEFAULT_VALUE
    String scoringAkceptacja = DEFAULT_VALUE
    String scoringMonitoring = DEFAULT_VALUE
    String scoringLokalizacjaPunktu = DEFAULT_VALUE
    String scoringTypPunktu = DEFAULT_VALUE
    String scoringTypPunktuInny = DEFAULT_VALUE
    String scoringWielkoscMiejscowosci = DEFAULT_VALUE
    String scoringOtwartyZamkniety = DEFAULT_VALUE

    Boolean scoringStanZadbany
    Boolean scoringSprzedazTowarowEkskluzywnych
    Boolean scoringPonad50ProcentObrotowWNocy
    Boolean scoringRuchTurystycznyPrzygraniczny
    Boolean scoringUslugiPlatneZGory

    String scoringCzestoscTransakcji = DEFAULT_VALUE
    String scoringIloscTransakcji = DEFAULT_VALUE
    String scoringDochodowosc = DEFAULT_VALUE
    String scoringDeklaracjaFinansowa = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotOgolem = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotNaKarty = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniObrot = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniaTransakcja = DEFAULT_VALUE

    //used only for calculation
    String progrnozaMiesieczna = DEFAULT_VALUE
    String liczbaPtkCbd = DEFAULT_VALUE

//    serwisEkonomiczny - FINISH
//    serwisKomfort - FINISH
//    serwisPrzestiz - FINISH

//    siedzibaAkceptanta - FINISH
    String akceptantUlicaTytul = DEFAULT_VALUE
    String akceptantUlica = DEFAULT_VALUE
    String akceptantNrDomu = DEFAULT_VALUE
    String akceptantNrMieszkania = DEFAULT_VALUE

    String akceptantMiasto = DEFAULT_VALUE
    String akceptantKodPocztowy = DEFAULT_VALUE
    String akceptantPoczta = DEFAULT_VALUE

    String akceptantTelStacjonarny = DEFAULT_VALUE
    String akceptantFax = DEFAULT_VALUE
    String akceptantTelKomorkowy = DEFAULT_VALUE

    String akceptantUlicaTytulCbd = DEFAULT_VALUE
    String akceptantUlicaCbd = DEFAULT_VALUE
    String akceptantNrDomuCbd = DEFAULT_VALUE
    String akceptantNrMieszkaniaCbd = DEFAULT_VALUE

    String akceptantMiastoCbd = DEFAULT_VALUE
    String akceptantKodPocztowyCbd = DEFAULT_VALUE
    String akceptantPocztaCbd = DEFAULT_VALUE

    String akceptantTelStacjonarnyCbd = DEFAULT_VALUE
    String akceptantFaxCbd = DEFAULT_VALUE
    String akceptantTelKomorkowyCbd = DEFAULT_VALUE

//    umowa2 - FINISH
    String miejsceUmowy //nie jest uzywane w pdf

//    zestawPosOdplatneUzywanie
    String isOdplatneUzywanieShown = DEFAULT_VALUE
    String odplatneUzywanie = DEFAULT_VALUE
    String odplatneUzywanieLiczbaTerminali = DEFAULT_VALUE
    String odplatneUzywanieCenaTerminal = DEFAULT_VALUE
    String odplatneUzywanieCenaPinpad = DEFAULT_VALUE

    String odpUzyTermSzt = DEFAULT_VALUE
    String odpUzyPpSzt = DEFAULT_VALUE
    String odpUzyTermMies = DEFAULT_VALUE
    String odpUzyPpMies = DEFAULT_VALUE

//    serwis - FINISH
    Boolean serwisZablokowany
    String obslugaTyp = DEFAULT_VALUE
    String obslugaEkonomicznyCena = DEFAULT_VALUE

//    rachunekBankowyKlienta
    String numerRachunkuBankowegoKlienta = DEFAULT_VALUE
    String bankKlienta = DEFAULT_VALUE

//    oplataDCCZaUruchomienie
    //TODO - to w pdfach sie nazywa inaczej - nie wiem jak...	 = DEFAULT_VALUE
    String oplataZaUruchomienieDCC = DEFAULT_VALUE

//    liczbaMiesiecyZwolnieniaZNajm

    String nip = DEFAULT_VALUE

//    uwagi
    @Omit
    String notes = DEFAULT_VALUE

    @Omit
    transient Process process

    @Omit(inSave = true, inPopulate = true)
    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    @Omit(inSave = true, inPopulate = true)
    List<PointCommand> poses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    @Omit(inSave = true, inPopulate = true)
    List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand))
    @Omit(inSave = true, inPopulate = true)
    List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))
    @Omit(inSave = true, inPopulate = true)
    List<HirePaymentCommand> hirePaymentsByPoint = ListUtils.lazyList([], FactoryUtils.instantiateFactory(HirePaymentCommand))
    @Omit(inSave = true, inPopulate = true)
    List<HirePaymentCommand> hirePaymentsByPos = ListUtils.lazyList([], FactoryUtils.instantiateFactory(HirePaymentCommand))

    @Omit
    String hasObslugaTyp
    @Omit
    String hasUmowaCzas
    @Omit
    String hasScoringAkceptacja
    @Omit
    String hasKontaktTel
    @Omit
    String hasDoladowania
    @Omit
    String hasAkceptantTel
    @Omit
    String hasInformacjaHandlowa
    @Omit
    String hasDccZakresUruchomienia
    @Omit
    Boolean hasAtLeastOneDoladowanie
    @Omit
    Boolean czyGift
    @Omit
    Boolean isRozszerzenie
    @Omit
    Boolean hasNewUmowaAndPrepaid

    @Omit
    String liczbaTerminali

    @Omit
    String liczbaPosZCbd

    @Omit(inPopulate = true)
    Boolean isDoladowania_tp

    @Omit(inPopulate = true)
    Boolean isDoladowania_tk

    @Omit
    Boolean hasPrepaid

    Boolean korespondencjaJakDlaMerchanta

    @Omit
    def defaultPointData
    @Omit
    def defaultPosData
    


    @Omit
    static constraints = {

        oplataZaDzienneZestawienieTransakcji(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaDzienneZestawienieTransakcji")})
        oplataZaMiesieczneZestawienieTransakcji(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaMiesieczneZestawienieTransakcji")})
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaPotwierdzenieWykonaniaPrzelewu")})
        oplataZaDostarczeniePapieru(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaDostarczeniePapieru")})
        oplataZaZmianeGrafiki(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaZmianeGrafiki")})
        oplataZaInstalacjePOS(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaInstalacjePOS")})
        oplataZaInstalacjeGPRS(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaInstalacjeGPRS")})
        oplataZaUruchomienieWalutyObcej(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaUruchomienieWalutyObcej")})

        // FIXME pola prezentowane warunkowo na panelu z Kalkulatora do odczytu, ponizsza walidacja nie dziala
        wydrukGrafikiCena(nullable:true, blank:false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "wydrukGrafikiCena")})
        dzialaniaMatematyczneCena(nullable:true, blank:false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "dzialaniaMatematyczneCena")})

        pierwszaSesjaCena(nullable: false, blank: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "pierwszaSesjaCena")})

        akceptantKontaktUlicaTytul(nullable: true, blank: true)
        akceptantKontaktUlica(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 40, "akceptantKontaktUlica", "default.nameTooLong.street")
        })
        akceptantKontaktNrDomu(nullable: false, blank: false, shared: "alphanumeric")
        akceptantKontaktNrMieszkania(nullable: true, blank: false, shared: "alphanumeric")
        akceptantKontaktMiasto(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 33, "akceptantKontaktMiasto", "default.nameTooLong.city")
        })
        akceptantKontaktKodPocztowy(nullable: false, blank: false, shared: "postalCodeValidator")
        akceptantKontaktPoczta(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 33, "akceptantKontaktPoczta", "default.nameTooLong.postalTown")
        })
        dataAneksowanejUmowyPos(nullable: false, blank: false, shared: "date")
        dataAneksowanejUmowyPrepaid(nullable: false, blank: false, shared: "date")

        hasUmowaCzas(nullable: true, validator: { value, cmd, errors ->
            if (value && cmd.umowaCzas == DEFAULT_VALUE) {
                errors.rejectValue("hasUmowaCzas", "default.atLeastOne.czasUmowy")
                return false
            }
            return true
        })

        hasScoringAkceptacja(nullable: true, validator: { value, cmd, errors ->
            if (value && cmd.scoringAkceptacja == DEFAULT_VALUE) {
                errors.rejectValue("hasScoringAkceptacja", "panel.scoring.accept.required")
                return false
            }
            return true
        })

        umowaCzas(nullable: false, blank: false)

        umowaOznOd(nullable: true, blank: true, shared: "date", validator: { value, cmd, errors ->
            if (value == null && cmd.umowaCzas == "oznaczony") {
                errors.rejectValue("umowaOznOd", "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        umowaOznDo(nullable: true, blank: true, shared: "date", validator: { value, cmd, errors ->
            if (value == null && cmd.umowaCzas == "oznaczony") {
                errors.rejectValue("umowaOznDo", "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        akceptantNazwaOficjalna(nullable: false, blank: false) //a1!, M
        akceptantNazwaSieciowa(nullable: false, blank: true) //a1!
        akceptantRegon(nullable: false, blank: false, matches: "~|[0-9]{9}") //111111111, M
        akceptantNazwaOficjalnaCbd(nullable: true)
        akceptantNazwaSieciowaCbd(nullable: true)
        akceptantRegonCbd(nullable: true)
        nazwaDoWydrukuZTerminalaPos(nullable: true)
        wydrukNazwaDoWyszukwarki(nullable: true)
        wydrukUlicaTytul(nullable: true, blank: true)
        wydrukUlica(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 40, "wyrdukUlica", "default.nameTooLong.city")
        })
        wydrukNrDomu(nullable: false, blank: false, shared: "alphanumeric")
        wydrukNrMieszkania(nullable: true, blank: false, shared: "alphanumeric")
        wydrukMiasto(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 33, "wydrukMiasto", "default.nameTooLong.postalTown")
        })
        wydrukKodPocztowy(nullable: false, blank: false, shared: "postalCodeValidator")
        wydrukPoczta(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            maxLengthClosure.call(value, cmd, errors, 33, "wydrukPoczta", "default.nameTooLong.postalTown")
        })
        wydrukLinia1(nullable: true, blank: true)
        wydrukLinia2(nullable: true, blank: true)
        oplataVISA(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataVISA")})
        oplataVISAPr(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataVISAPr")})
        oplataMasterCard(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataMasterCard")})
        oplataMasterCardPr(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataMasterCardPr")})
        oplataMaestro(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataMaestro")})
        oplataMaestroPr(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataMaestroPr")})

        dccZakresUruchomienia(nullable: false, blank: false)
        hasDccZakresUruchomienia(nullable: true, validator: { value, cmd, errors ->
            if(!value){
                return true
            }
            if (cmd.dccZakresUruchomienia == DEFAULT_VALUE) {
                errors.rejectValue("dccZakresUruchomienia", "default.atLeastOne.dccZakresUruchomienia")
                return false
            }
        })
        dccZakresUruchomienia(nullable: false, blank: false)
        odplatneUzywanie(nullable: true, blank: false)
        odplatneUzywanieLiczbaTerminali(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odplatneUzywanieLiczbaTerminali")})
        odplatneUzywanieCenaTerminal(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odplatneUzywanieCenaTerminal")})
        odplatneUzywanieCenaPinpad(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odplatneUzywanieCenaPinpad")})

        odpUzyTermSzt(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odpUzyTermSzt")})
        odpUzyPpSzt(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odpUzyPpSzt")})
        isOdplatneUzywanieShown(nullable: true, blank: true, validator: { value, cmd, errors ->
            if ("tak".equals(value)){
                if ("one_for_all_terminals".equals(cmd.odplatneUzywanie)){
                    atLeastClosure.call(cmd.odpUzyTermMies, cmd, errors, "odpUzyTermMies", "CENA_NAJMU")
                } else if ("one_for_all_terminals_in_point".equals(cmd.odplatneUzywanie) && hasMoreThanPriceGroups(MAX_PRICE_GROUP_SIZE, cmd.hirePaymentsByPoint)){
                    errors.reject("default.tooMany.groups")
                    return false
                } else if ("other_for_selected_terminals".equals(cmd.odplatneUzywanie) && hasMoreThanPriceGroups(MAX_PRICE_GROUP_SIZE, cmd.hirePaymentsByPos)){
                    errors.reject("default.tooMany.groups")
                    return false
                }
            }
            return true
        })

  /*      odpUzyTermMies(nullable: false, blank: false, validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "odpUzyTermMies") &&
                    atLeastClosure.call(value, cmd, errors, "odpUzyTermMies", "CENA_NAJMU")
        })*/

        odpUzyPpMies(nullable: true, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "odpUzyPpMies")})

        informacjaHandlowa(nullable: false, blank: false)

        hasInformacjaHandlowa(nullable: true, validator: { value, cmd, errors ->
            if(!value){
                return true
            }

            if (cmd.informacjaHandlowa == DEFAULT_VALUE) {
                errors.rejectValue("hasInformacjaHandlowa", "default.atLeastOne.informacjaHandlowa")
                return false
            }

            /*CR - uncomment when ready*/
            /*if (cmd.informacjaHandlowa == "true" && (!cmd.kontaktEmail || cmd.kontaktEmail == DEFAULT_VALUE)) {
                    errors.rejectValue("hasInformacjaHandlowa", "default.noEmail.informacjaHandlowa")
                    return false
                }*/
            return true
        })

        mudCena(nullable: false, blank: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mudCena")})
        weryfikacjaPINCena(nullable: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "weryfikacjaPINCena")})
        systemKasowyCena(nullable: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "systemKasowyCena")})

        doladowania_tk(nullable: true)
        doladowania_tp(nullable: true)
        hasDoladowania(nullable: true, validator: { value, cmd, errors ->

            if(value && (cmd.isDoladowania_tp || cmd.isDoladowania_tk)){
                if(!cmd.hasAtLeastOneDoladowanie){
                    errors.rejectValue("hasDoladowania", "default.atLeastOne.doladowania")
                    return false
                }
            }
            return true
        })

        srednia_sprzedaz_doladowan(nullable:false, blank:false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "srednia_sprzedaz_doladowan")})
        srednia_sprzedaz_doladowan_slownie(nullable:false, blank:false, shared: "lettersOnly")
        ifOplataVISA(nullable:false, blank:false, shared: "number4Precision") //1.11 %, M
        ifOplataMasterCard(nullable:false, blank:false, shared: "number4Precision") //1.11 %, M
        ifOplataDinersClub(nullable:false, blank:false, shared: "number4Precision") //1.11 %, M
        ifOplataIKO(nullable:false, blank:false, shared: "number4Precision") //1.11 %, M
        ifOplataPKOPB(nullable:false, blank:false, shared: "number4Precision") //1.11 %, M
        dzialalnoscForma(nullable:false, blank:true)
        dzialalnoscFormaInna(nullable:true, blank:true, shared: "alphanumeric")
        dzialalnoscDokument(nullable:false, blank:true)
        dzialalnoscDokumentInny(nullable:true, blank:true, shared: "alphanumeric")

        //okresLojalnosciowy(nullable:false, blank:false) FIXME do wyjasnienia znaczenie BRAK vs 0
        oplataZaPlatnoscWInnejWalucie(nullable: false, blank: false)
        kontaktTytul(nullable: false, blank: false)
        kontaktImie(nullable: false, blank: false, shared: "lettersOnly")
        kontaktNazwisko(nullable: false, blank: false, shared: "lettersOnly")

        hasKontaktTel(nullable: true, validator: { value, process, errors ->
            if (value == null) {
                return true
            }

            if (!process.kontaktTelKomorkowy && !process.kontaktTelStacjonarny) {
                errors.rejectValue("hasKontaktTel", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        kontaktTelKomorkowy(nullable: true)
        kontaktTelStacjonarny(nullable: true)
        kontaktEmail(nullable: true, blank: true, shared: "email")
        pozyskujacyTytul(nullable: false, blank: false)
        pozyskujacyImie(nullable: false, blank: false, shared: "lettersOnly", maxSize: 40)
        pozyskujacyNazwisko(nullable: false, blank: false, shared: "lettersOnly", maxSize: 100)
        pozyskujacyNumer(nullable: false, blank: false, maxSize: 12)
        reprezentant1Tytul(nullable: false, blank: false)
        reprezentant1Imie(nullable: false, blank: false, shared: "lettersOnly")
        reprezentant1Nazwisko(nullable: false, blank: false, shared: "lettersOnly")
        reprezentant2Tytul(nullable: false, blank: false)
        reprezentant2Imie(nullable: true, blank: true, shared: "lettersOnly")
        reprezentant2Nazwisko(nullable: true, blank: true, shared: "lettersOnly")
        emailDoWysylkiDokumentu(nullable: true, blank: true, shared: "email")

        visaEUKKOSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaEUKKOSt") &&
            atLeastClosure.call(value, cmd, errors, "visaEUKKOSt", "OPLATA_MSC_53_ZL")
        })

        visaEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaEUKDSt") &&
            atLeastClosure.call(value, cmd, errors, "visaEUKDSt", "OPLATA_MSC_12_ZL")
        })

        visaEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaEUKBSt") &&
            atLeastClosure.call(value, cmd, errors, "visaEUKBSt", "OPLATA_MSC_13_ZL")
        })

        visaOutEUKKOSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaOutEUKKOSt") &&
            atLeastClosure.call(value, cmd, errors, "visaOutEUKKOSt", "OPLATA_MSC_21_ZL")
        })

        visaOutEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaOutEUKDSt") &&
            atLeastClosure.call(value, cmd, errors, "visaOutEUKDSt", "OPLATA_MSC_22_ZL")
        })

        visaOutEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "visaOutEUKBSt") &&
            atLeastClosure.call(value, cmd, errors, "visaOutEUKBSt", "OPLATA_MSC_23_ZL")
        })

        visaPolskaKBSt(nullable: false, blank: false, matches: "~|\\-|^(?:[1-9]\\d*|0|\\-)?(?:\\.\\d{1,2})?\$", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaPolskaKBSt", "OPLATA_MSC_33_ZL")
        })

        mastercardEUKKSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardEUKKSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardEUKKSt", "OPLATA_MSC_41_ZL")
        })

        mastercardEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardEUKDSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardEUKDSt", "OPLATA_MSC_42_ZL")
        })
        mastercardEUKBLSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardEUKBLSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardEUKBLSt", "OPLATA_MSC_43_ZL")
        })
        mastercardEUMSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardEUMSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardEUMSt", "OPLATA_MSC_44_ZL")
        })
        mastercardOutEUKKSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardOutEUKKSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKKSt", "OPLATA_MSC_51_ZL")
        })
        mastercardOutEUKDSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardOutEUKDSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKDSt", "OPLATA_MSC_52_ZL")
        })
        mastercardOutEUKBSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardOutEUKBSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKBSt", "OPLATA_MSC_53_ZL")
        })
        mastercardOutEUMSt(nullable: false, blank: false,  validator: { value, cmd, errors ->
            cmd.numberValidationClosure(value, cmd, errors, "mastercardOutEUMSt") &&
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUMSt", "OPLATA_MSC_54_ZL")
        })

        dinersClubSt(nullable: true, blank: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "dinersClubSt")
        })

        ikoSt(nullable: true, blank: true,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "ikoSt")})
        visaEUKKOPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaEUKKOPr")})
        visaEUKKOPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaEUKKOPr")})
        visaEUKDPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaEUKDPr")})
        visaEUKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaEUKBPr")})
        visaOutEUKKOPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaOutEUKKOPr")})
        visaOutEUKDPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaOutEUKDPr")})
        visaOutEUKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaOutEUKBPr")})
        visaPolskaKKO1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPolskaKKO1Pr")})
        visaPolskaKKO2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPolskaKKO2Pr")})
        visaPolskaKD1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPolskaKD1Pr")})
        visaPolskaKD2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPolskaKD2Pr")})
        visaPolskaKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPolskaKBPr")})
        mastercardEUKKPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardEUKKPr")})
        mastercardEUKDPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardEUKDPr")})
        mastercardEUKBLPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardEUKBLPr")})
        mastercardEUMPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardEUMPr")})
        mastercardOutEUKKPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardOutEUKKPr")})
        mastercardOutEUKDPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardOutEUKDPr")})
        mastercardOutEUKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardOutEUKBPr")})
        mastercardOutEUMPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardOutEUMPr")})
        mastercardPolskaKK1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKK1Pr")})
        mastercardPolskaKK2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKK2Pr")})
        mastercardPolskaKK3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKK3Pr")})
        mastercardPolskaKD1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKD1Pr")})
        mastercardPolskaKD2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKD2Pr")})
        mastercardPolskaKD3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKD3Pr")})
        mastercardPolskaKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaKBPr")})
        mastercardPolskaM1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaM1Pr")})
        mastercardPolskaM2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaM2Pr")})
        mastercardPolskaM3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPolskaM3Pr")})
        visaPKOBPKKO1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPKOBPKKO1Pr")})
        visaPKOBPKKO2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPKOBPKKO2Pr")})
        visaPKOBPKD1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPKOBPKD1Pr")})
        visaPKOBPKD2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPKOBPKD2Pr")})
        visaPKOBPKB3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "visaPKOBPKB3Pr")})
        mastercardPKOBPKK1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKK1Pr")})
        mastercardPKOBPKK2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKK2Pr")})
        mastercardPKOBPKK3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKK3Pr")})
        mastercardPKOBPKD1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKD1Pr")})
        mastercardPKOBPKD2LPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKD2LPr")})
        mastercardPKOBPKD3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKD3Pr")})
        mastercardPKOBPKBPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPKBPr")})
        mastercardPKOBPM1Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPM1Pr")})
        mastercardPKOBPM2Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPM2Pr")})
        mastercardPKOBPM3Pr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "mastercardPKOBPM3Pr")})
        dinersClubPr(nullable: false, blank: false, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "dinersClubPr")})
        ikoPr(nullable: true, blank: true, validator: { value, cmd, errors -> cmd.percentageValidationClosure(value, cmd, errors, "ikoPr")})


        visaPolskaKKO1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPolskaKKO1St")})
        visaPolskaKKO2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPolskaKKO2St")})
        visaPolskaKD1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPolskaKD1St")})
        visaPolskaKD2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPolskaKD2St")})
        visaPolskaKBSt(nullable: false, blank: false, matches: "~|\\-|^(?:[1-9]\\d*|0|\\-)?(?:\\.\\d{1,2})?\$")
        mastercardPolskaKK1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKK1St")})
        mastercardPolskaKK2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKK2St")})
        mastercardPolskaKK3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKK3St")})
        mastercardPolskaKD1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKD1St")})
        mastercardPolskaKD2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKD2St")})
        mastercardPolskaKD3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKD3St")})
        mastercardPolskaKBSt(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaKBSt")})
        mastercardPolskaM1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaM1St")})
        mastercardPolskaM2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaM2St")})
        mastercardPolskaM3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPolskaM3St")})
        visaPKOBPKKO1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPKOBPKKO1St")})
        visaPKOBPKKO2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPKOBPKKO2St")})
        visaPKOBPKD1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPKOBPKD1St")})
        visaPKOBPKD2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPKOBPKD2St")})
        visaPKOBPKB3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "visaPKOBPKB3St")})
        mastercardPKOBPKK1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKK1St")})
        mastercardPKOBPKK2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKK2St")})
        mastercardPKOBPKK3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKK3St")})
        mastercardPKOBPKD1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKD1St")})
        mastercardPKOBPKD2LSt(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKD2LSt")})
        mastercardPKOBPKD3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKD3St")})
        mastercardPKOBPKBSt(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPKBSt")})
        mastercardPKOBPM1St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPM1St")})
        mastercardPKOBPM2St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPM2St")})
        mastercardPKOBPM3St(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "mastercardPKOBPM3St")})


        pp_orange_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_orange_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_orange_tk")
        })
        pp_orange_tp(nullable: true, blank: true, validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_orange_tp") &&
            cmd.checkTelepompka(value, cmd, errors, "pp_orange_tp")
        })
        pp_plus_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_plus_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_plus_tk")
        })
        pp_plus_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_plus_tp") &&
            cmd.checkTelepompka(value, cmd, errors, "pp_plus_tp")
        })
        pp_tmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_tmobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_tmobile_tk")
        })
        pp_tmobile_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_tmobile_tp") &&
            cmd.checkTelepompka(value, cmd, errors, "pp_tmobile_tp")
        })
        pp_heyah_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_heyah_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_heyah_tk")
        })
        pp_heyah_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_heyah_tp") &&
            cmd.checkTelepompka(value, cmd, errors, "pp_heyah_tp")
        })
        pp_play_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_play_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_play_tk")
        })
        pp_play_tp(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_play_tp") &&
            cmd.checkTelepompka(value, cmd, errors, "pp_play_tp")
        })
        pp_telegrosik_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_telegrosik_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_telegrosik_tk")
        })
        pp_virginmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_virginmobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_virginmobile_tk")
        })
        pp_lycamobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_lycamobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_lycamobile_tk")
        })
        pp_gtmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_gtmobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_gtmobile_tk")
        })
        pp_vectonemobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_vectonemobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_vectonemobile_tk")
        })
        pp_delightmobile_tk(nullable: true, blank: true,  validator: { value, cmd, errors ->
            cmd.percentageValidationClosure(value, cmd, errors, "pp_delightmobile_tk") &&
            cmd.checkTelekodzik(value, cmd, errors, "pp_delightmobile_tk")
        })

        oplataZaOprogramowanieDoDoladowan(nullable: false, blank: false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "oplataZaOprogramowanieDoDoladowan")})


        scoringMcc(nullable: false, blank: false, validator: { value, cmd, errors ->
            cmd.customValidationClosure(value, cmd, errors, "scoringMcc", "~|[0-9]{4}")
        })

        scoringDzialalnosc(nullable: false, blank: false)
        scoringSzczegolyDzialalnosci(nullable: true, blank: true)
        scoringWlasnosc(nullable: false, blank: false)
        scoringDzialalnoscCzas(nullable: false, blank: false)
        scoringKoncesja(nullable: false, blank: false)
        rodzajZezwolenia(nullable: true, blank: true)
        scoringCharakterystyka(nullable: false, blank: false)
        scoringCharakterystykaInna(nullable: true, blank: true)
        scoringWielkoscPunktu(nullable: false, blank: false)
        scoringAkceptacja(nullable: false, blank: false)
        scoringMonitoring(nullable: false, blank: false)
        scoringLokalizacjaPunktu(nullable: false, blank: false)
        scoringTypPunktu(nullable: false, blank: false)
        scoringTypPunktuInny(nullable: true, blank: true)
        scoringWielkoscMiejscowosci(nullable: false, blank: false)
        scoringOtwartyZamkniety(nullable: false, blank: false)
        scoringStanZadbany(nullable: true)
        scoringSprzedazTowarowEkskluzywnych(nullable: true) //Boolean
        scoringPonad50ProcentObrotowWNocy(nullable: true) //Boolean
        scoringRuchTurystycznyPrzygraniczny(nullable: true)//Boolean
        scoringUslugiPlatneZGory(nullable: true)//Boolean
        scoringCzestoscTransakcji(nullable: false, blank: false)
        scoringIloscTransakcji(nullable: false, blank: false)
        scoringDochodowosc(nullable: true, blank: true)
        scoringDeklaracjaFinansowa(nullable: false, blank: false)
        scoringDeklaracjaFinansowaObrotOgolem(nullable: true, blank: true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable: true, blank: true)
        scoringDeklaracjaFinansowaSredniObrot(nullable: true, blank: true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable: true, blank: true)
        akceptantUlicaTytul(nullable: true, blank: true)
        akceptantUlica(nullable: false, blank: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            skipAddressValidationClosure.call(value, cmd, errors, "akceptantUlica", "default.cantBeEmpty.akceptantPoczta")
            maxLengthClosure.call(value, cmd, errors, 40, "akceptantUlica", "default.nameTooLong.street")
        })
        akceptantNrDomu(nullable:false, shared: "alphanumeric", validator: {value, cmd, errors ->
            skipAddressValidationClosure.call(value, cmd, errors, "akceptantNrDomu", "default.cantBeEmpty.akceptantNrDomu")
        })
        akceptantNrMieszkania(nullable: true, blank: false, shared: "alphanumeric")
        akceptantMiasto(nullable: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            skipAddressValidationClosure.call(value, cmd, errors, "akceptantMiasto", "default.cantBeEmpty.akceptantNrDomu")
            maxLengthClosure.call(value, cmd, errors, 33, "akceptantMiasto", "default.nameTooLong.city")
        })
        akceptantKodPocztowy(nullable:false, shared: "postalCodeValidator", validator: {value, cmd, errors ->
            skipAddressValidationClosure.call(value, cmd, errors, "akceptantKodPocztowy", "default.cantBeEmpty.akceptantKodPocztowy")
        })
        akceptantPoczta(nullable: false, shared: "alphanumeric", validator: { value, cmd, errors ->
            skipAddressValidationClosure.call(value, cmd, errors, "akceptantPoczta", "default.cantBeEmpty.akceptantPoczta")

            maxLengthClosure.call(value, cmd, errors, 33, "akceptantPoczta", "default.nameTooLong.postalTown")
        })

        hasAkceptantTel(validator: { value, process, errors ->
            if (value == null || process.akceptantTelKomorkowy == null) {
                return true
            }
            if (value.isEmpty() && process.akceptantTelKomorkowy.isEmpty()) {
                errors.rejectValue("akceptantTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                errors.rejectValue("akceptantTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                return false
            }
            return true
        })

        hasAkceptantTel(nullable: true, validator: { value, process, errors ->
            if (value == null) {
                return true
            }

            if (!process.akceptantTelStacjonarny && !process.akceptantTelKomorkowy) {
                errors.rejectValue("hasAkceptantTel", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        akceptantTelKomorkowy(nullable: true)
        akceptantTelStacjonarny(nullable: true)
        akceptantFax(nullable: true, blank: true)
        akceptantUlicaTytulCbd(nullable: true)
        akceptantUlicaCbd(nullable: true)
        akceptantNrDomuCbd(nullable: true)
        akceptantNrMieszkaniaCbd(nullable: true)
        akceptantMiastoCbd(nullable: true)
        akceptantKodPocztowyCbd(nullable: true)
        akceptantPocztaCbd(nullable: true)
        akceptantTelStacjonarnyCbd(nullable:true)
        akceptantFaxCbd(nullable: true)
        akceptantTelKomorkowyCbd(nullable:true)
        miejsceUmowy(nullable: true)
        hasObslugaTyp(nullable: true, validator: { value, cmd, errors ->
            if (value == null) {
                return true;
            }
            if (!cmd.obslugaTyp) {
                errors.rejectValue("hasObslugaTyp", "default.atLeastOne.obslugaTyp")
                return false
            }
            return true
        })
        obslugaEkonomicznyCena(nullable:true, blank:false,  validator: { value, cmd, errors -> cmd.numberValidationClosure(value, cmd, errors, "xxx")}) //TODO VERIFY
        numerRachunkuBankowegoKlienta(nullable:true, blank:false, matches: "~|\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}")
        bankKlienta(nullable:true, blank:false)
        oplataZaUruchomienieDCC(nullable:false, blank:false, matches: "~|\\-|^(?:[1-9]\\d*|0|\\-)?(?:\\.\\d{1,2})?\$")
        nip(nullable:true)
        notes(nullable:true, maxSize: 1000) //a1!
        points(nullable:true, validator: { value, cmd, errors ->
            def hasPointErrors = false

            value.each {  ptCmd ->
                ptCmd?.validate()
                if(ptCmd?.hasErrors()){
                    ptCmd.errors.each {
                        log.info(it)
                    }
                    hasPointErrors = true
                }
            }

            if(cmd.points?.size() > 0 && cmd.hasMoreThanThreePriceGroups(cmd.points)){
                errors.reject("default.tooMany.groups")
                return false
            }

            if (hasPointErrors) {
                errors.rejectValue("points", "default.error.points",)
                return false
            }
            return true
        })

        poses(nullable:true, validator: { value, cmd, errors ->
            if(cmd.poses?.size() > 0 && cmd.hasMoreThanThreePriceGroups(cmd.poses)){
                errors.reject("default.tooMany.groups")
                return false
            }
            return true
        })
        allPoints(nullable:true)
        allPoses(nullable:true)
        liczbaPosZCbd(nullable:true)
        korespondencjaJakDlaMerchanta(nullable:true)
        serwisZablokowany(nullable: true)

        liczbaTerminali(nullable:true, validator: { value, cmd, errors ->
            checkTerminalNumber.call(value, cmd, errors)
        })

    }

    def checkIfFromCbd(def property) {
        def cbdName = property + "Cbd"
        return (this.metaClass.hasProperty(this, cbdName) && this."$cbdName"?.trim())
    }

    private boolean checkIfClientFromCbd(){
        return this.checkIfFromCbd("akceptantNazwaOficjalna")
    }

    private boolean hasMoreThanThreePriceGroups(def pointCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()
        Set<BigDecimal> prefPriceGroups = new HashSet<BigDecimal>()

        pointCommands.each { pos ->
            normalPriceGroups.add(getGroupValue(pos.dialupCena, pos.dialupPPCena))
            normalPriceGroups.add(getGroupValue(pos.vpnCena, pos.vpnPPCena))
            normalPriceGroups.add(getGroupValue(pos.sslCena, pos.sslPPCena))
            normalPriceGroups.add(getGroupValue(pos.gprsCena, pos.gprsPPCena))
            normalPriceGroups.add(getGroupValue(pos.pinPadCena, BigDecimal.ZERO))
            normalPriceGroups.add(getGroupValue(pos.wifiCena, BigDecimal.ZERO))

            prefPriceGroups.add(getGroupValue(pos.dialupCenaPreferencyjna, pos.dialupPPCenaPreferencyjna))
            prefPriceGroups.add(getGroupValue(pos.vpnCenaPreferencyjna, pos.vpnPPCenaPreferencyjna))
            prefPriceGroups.add(getGroupValue(pos.sslCenaPreferencyjna, pos.sslPPCenaPreferencyjna))
            prefPriceGroups.add(getGroupValue(pos.gprsCenaPreferencyjna, pos.gprsPPCenaPreferencyjna))
            prefPriceGroups.add(getGroupValue(pos.pinPadCenaPreferencyjna, BigDecimal.ZERO))
            prefPriceGroups.add(getGroupValue(pos.wifiCenaPreferencyjna, BigDecimal.ZERO))
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO)) //jesli obie ceny sa nullem to dostajemy 0
        prefPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO))
        if(normalPriceGroups.size() > MAX_PRICE_GROUP_SIZE || prefPriceGroups.size() > MAX_PRICE_GROUP_SIZE){
            return true
        }
        return false
    }

    private static boolean hasMoreThanPriceGroups(int maxSize, List<HirePaymentCommand> hirePaymentCommands){
        Set<BigDecimal> normalPriceGroups = new HashSet<BigDecimal>()

        hirePaymentCommands.each { HirePaymentCommand hpc ->
            normalPriceGroups.add(getGroupValue(hpc.newTermPayment, hpc.newPpPayment))
        }

        normalPriceGroups.removeAll(Collections.singleton(BigDecimal.ZERO)) //jesli obie ceny sa nullem to dostajemy 0

        normalPriceGroups.size() > maxSize? true : false;
    }


    private static def getGroupValue(def normalPrice, def ppPrice){
        if(normalPrice == null){
            normalPrice = BigDecimal.ZERO
        }
        if (ppPrice == null){
            ppPrice = BigDecimal.ZERO
        }
        return normalPrice + ppPrice
    }

}