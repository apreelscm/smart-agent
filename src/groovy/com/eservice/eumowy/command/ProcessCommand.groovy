package com.eservice.eumowy.command

import com.eservice.eumowy.Process
import grails.validation.Validateable
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:22
 *
 */

@Validateable
class ProcessCommand implements Serializable{

    def calculatorService

    //UWAGA - kazde nowe pole, ktore ma byc pomijane w zapisie do bazy trzeba dodac tez w
    //ProcessService.getDataFromPanels(). Gdy sie tego nie zrobi zapisuja sie dane a pozniej leci
    // NoSuchFieldException z ProcessService.loadProcessData() przy probie usuniecia zbednej metody
    static def nullableTrueBlankFalse = {return it == null || it.toString()?.size() > 0}

    static def atLeastClosure = { value, cmd, errors, property, calcProperty ->
        def calcValue = cmd.calculatorService.getCalcProperty(calcProperty)
        def minValue = calcValue?.toString()?.isNumber() ? Integer.valueOf(calcValue) : 0
        def currValue = value?.toString()?.isNumber() ? Integer.valueOf(value) : 0

        if (currValue < minValue) {
            errors.rejectValue(property, "default.atLeast.asCalc")
            return false
        }
        return true
    }

    static def DEFAULT_VALUE = "~"

//    adresDoKorespondencjizAkecptantem - FINISH
    String akceptantKontaktUlicaTytul	 = DEFAULT_VALUE
    String akceptantKontaktUlica	 = DEFAULT_VALUE
    String akceptantKontaktNrDomu	 = DEFAULT_VALUE
    String akceptantKontaktNrMieszkania	 = DEFAULT_VALUE

    String akceptantKontaktMiasto	 = DEFAULT_VALUE
    String akceptantKontaktKodPocztowy	 = DEFAULT_VALUE
    String akceptantKontaktPoczta	 = DEFAULT_VALUE

//    aneksDoUmowyNajmuZestawuPos - FINISH
    String dataAneksowanejUmowyPos	 = DEFAULT_VALUE

//    aneksDoUmowyPrepaid - FINISH
    String dataAneksowanejUmowyPrepaid	 = DEFAULT_VALUE

//    czasObowiazywaniaUmowy - FINISH
    String umowaCzas	 = DEFAULT_VALUE
    String umowaOznOd	 = DEFAULT_VALUE
    String umowaOznDo	 = DEFAULT_VALUE

//    daneAkceptanta
    String akceptantNazwaOficjalna	 = DEFAULT_VALUE
    String akceptantNazwaSieciowa	 = DEFAULT_VALUE
    //String akceptantNip - trzymane we wspolnym polu nip
    String akceptantRegon	 = DEFAULT_VALUE

    String akceptantNazwaOficjalnaCbd	 = DEFAULT_VALUE
    String akceptantNazwaSieciowaCbd	 = DEFAULT_VALUE
    String akceptantRegonCbd	 = DEFAULT_VALUE

//    daneDoWydruku
    String nazwaDoWydrukuZTerminalaPos	 = DEFAULT_VALUE
    String wydrukNazwaDoWyszukwarki	 = DEFAULT_VALUE

    String wydrukUlicaTytul	 = DEFAULT_VALUE
    String wydrukUlica	 = DEFAULT_VALUE
    String wydrukNrDomu	 = DEFAULT_VALUE
    String wydrukNrMieszkania	 = DEFAULT_VALUE

    String wydrukMiasto	 = DEFAULT_VALUE
    String wydrukKodPocztowy	 = DEFAULT_VALUE
    String wydrukPoczta	 = DEFAULT_VALUE

    String wydrukLinia1	 = DEFAULT_VALUE
    String wydrukLinia2	 = DEFAULT_VALUE

//    danePunktu
//    dcc - FINISH
    String oplataVISA	 = DEFAULT_VALUE
    String oplataVISAPr	 = DEFAULT_VALUE
    String oplataMasterCard	 = DEFAULT_VALUE
    String oplataMasterCardPr	 = DEFAULT_VALUE
    String oplataMaestro	 = DEFAULT_VALUE
    String oplataMasteroPr	 = DEFAULT_VALUE

//    dccZakresUruchomienia
    String dccZakresUruchomienia	 = DEFAULT_VALUE

//    deklaracjeAkceptanta - FINISH
    String informacjaHandlowa	 = DEFAULT_VALUE

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

//    dodatkoweUslugi2 - FINISH (ale trzeba jeszcze daty startu pobrac)
    String wydrukGrafikiCena = DEFAULT_VALUE
    String dzialaniaMatematyczneCena = DEFAULT_VALUE
    String tytulPlatnosciCena = DEFAULT_VALUE
    String pierwszaSesjaCena = DEFAULT_VALUE

//    dodatkoweUslugiMud - FINISH
    String mudCena = DEFAULT_VALUE

//    dodatkoweUslugiUTAIntegracja - FINISH (ale trzeba jeszcze daty startu pobrac)
    String weryfikacjaPINCena = DEFAULT_VALUE
    String systemKasowyCena = DEFAULT_VALUE

//    formaDoladowania - FINISH
    Boolean doladowania_tp
    Boolean doladowania_tk
    String srednia_sprzedaz_doladowan = DEFAULT_VALUE
    String srednia_sprzedaz_doladowan_slownie = DEFAULT_VALUE

//    ifplus - FINISH (ale zmiany w dokumentach)
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
    String okresLojalnosciowy  = DEFAULT_VALUE //  TODO - czy to jest dobrze????

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
    String mastercardPKOBPKK1St	 = DEFAULT_VALUE
    String mastercardPKOBPKK2St	 = DEFAULT_VALUE
    String mastercardPKOBPKK3St	 = DEFAULT_VALUE
    String mastercardPKOBPKD1St	 = DEFAULT_VALUE
    String mastercardPKOBPKD2LSt	 = DEFAULT_VALUE
    String mastercardPKOBPKD3St	 = DEFAULT_VALUE
    String mastercardPKOBPKBSt	 = DEFAULT_VALUE
    String mastercardPKOBPM1St	 = DEFAULT_VALUE
    String mastercardPKOBPM2St	 = DEFAULT_VALUE
    String mastercardPKOBPM3St	 = DEFAULT_VALUE
    String dinersClubSt	 = DEFAULT_VALUE
    String ikoSt	 = DEFAULT_VALUE

//    poziomOplatIWarunkiPlatnosciPP - FINISH
    String pp_orange_tk	 = DEFAULT_VALUE
    String pp_orange_tp	 = DEFAULT_VALUE
    String pp_plus_tk	 = DEFAULT_VALUE
    String pp_plus_tp	 = DEFAULT_VALUE
    String pp_tmobile_tk	 = DEFAULT_VALUE
    String pp_tmobile_tp	 = DEFAULT_VALUE
    String pp_heyah_tk	 = DEFAULT_VALUE
    String pp_heyah_tp	 = DEFAULT_VALUE
    String pp_play_tk	 = DEFAULT_VALUE
    String pp_play_tp	 = DEFAULT_VALUE
    String pp_telegrosik_tk	 = DEFAULT_VALUE
    String pp_virginmobile_tk	 = DEFAULT_VALUE
    String pp_lycamobile_tk	 = DEFAULT_VALUE
    String pp_gtmobile_tk	 = DEFAULT_VALUE
    String pp_vectonemobile_tk	 = DEFAULT_VALUE
    String pp_delightmobile_tk	 = DEFAULT_VALUE
    String oplataZaOprogramowanieDoDoladowan	 = DEFAULT_VALUE

//    promocyjneObnizenieOplatyZaZestawPos
//    scoring
//    TODO - co wpisac w scoringNrUmowy????

    String scoringMcc	 = DEFAULT_VALUE
    String scoringDzialalnosc	 = DEFAULT_VALUE
    String scoringSzczegolyDzialalnosci	 = DEFAULT_VALUE
//    TODO - co to sa za pola: szczegolowyRodzajDzialalnosciWPraktyce ??

    String scoringWlasnosc	 = DEFAULT_VALUE
    String scoringDzialalnoscCzas	 = DEFAULT_VALUE
    String scoringKoncesja	 = DEFAULT_VALUE
    String rodzajZezwolenia	 = DEFAULT_VALUE
    String scoringCharakterystyka	 = DEFAULT_VALUE
    String scoringCharakterystykaInna	 = DEFAULT_VALUE
    String scoringWielkoscPunktu	 = DEFAULT_VALUE
    String scoringAkceptacja	 = DEFAULT_VALUE
    String scoringMonitoring	 = DEFAULT_VALUE
    String scoringLokalizacjaPunktu	 = DEFAULT_VALUE
    String scoringTypPunktu	 = DEFAULT_VALUE
    String scoringTypPunktuInny	 = DEFAULT_VALUE
    String scoringWielkoscMiejscowosci	 = DEFAULT_VALUE
    String scoringOtwartyZamkniety	 = DEFAULT_VALUE

    Boolean scoringStanZadbany
    Boolean scoringSprzedazTowarowEkskluzywnych
    Boolean scoringPonad50ProcentObrotowWNocy
    Boolean scoringRuchTurystycznyPrzygraniczny
    Boolean scoringUslugiPlatneZGory

    String scoringCzestoscTransakcji	 = DEFAULT_VALUE
    String scoringIloscTransakcji	 = DEFAULT_VALUE
    String scoringDochodowosc	 = DEFAULT_VALUE
    String scoringDeklaracjaFinansowa	 = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotOgolem	 = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaObrotNaKarty	 = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniObrot	 = DEFAULT_VALUE
    String scoringDeklaracjaFinansowaSredniaTransakcja	 = DEFAULT_VALUE

//    serwisEkonomiczny - FINISH
//    serwisKomfort - FINISH
//    serwisPrzestiz - FINISH

//    siedzibaAkceptanta - FINISH
    String akceptantUlicaTytul	 = DEFAULT_VALUE
    String akceptantUlica	 = DEFAULT_VALUE
    String akceptantNrDomu	 = DEFAULT_VALUE
    String akceptantNrMieszkania	 = DEFAULT_VALUE

    String akceptantMiasto	 = DEFAULT_VALUE
    String akceptantKodPocztowy	 = DEFAULT_VALUE
    String akceptantPoczta	 = DEFAULT_VALUE

    String akceptantTelStacjonarny	 = DEFAULT_VALUE
    String akceptantFax	 = DEFAULT_VALUE
    String akceptantTelKomorkowy	 = DEFAULT_VALUE

    String akceptantUlicaTytulCbd	 = DEFAULT_VALUE
    String akceptantUlicaCbd	 = DEFAULT_VALUE
    String akceptantNrDomuCbd	 = DEFAULT_VALUE
    String akceptantNrMieszkaniaCbd	 = DEFAULT_VALUE

    String akceptantMiastoCbd	 = DEFAULT_VALUE
    String akceptantKodPocztowyCbd	 = DEFAULT_VALUE
    String akceptantPocztaCbd	 = DEFAULT_VALUE

    String akceptantTelStacjonarnyCbd	 = DEFAULT_VALUE
    String akceptantFaxCbd	 = DEFAULT_VALUE
    String akceptantTelKomorkowyCbd	 = DEFAULT_VALUE

//    umowa2 - FINISH
    String miejsceUmowy //nie jest uzywane w pdf

//    zestawPosOdplatneUzywanie
    String oplPOSDialUpTyp	 = DEFAULT_VALUE
    String oplPOSDialUpIlosc	 = DEFAULT_VALUE
    String oplPOSDialUpIloscPP	 = DEFAULT_VALUE
    String oplPOSDialUpNormalneMies	 = DEFAULT_VALUE
    String oplPOSDialUpNormalnePP	 = DEFAULT_VALUE
    String oplPOSDialUpPreferencyjneMies	 = DEFAULT_VALUE
    String oplPOSDialUpPreferencyjnePP	 = DEFAULT_VALUE

    String oplPOSVPNTyp	 = DEFAULT_VALUE
    String oplPOSVPNIlosc	 = DEFAULT_VALUE
    String oplPOSVPNIloscPP	 = DEFAULT_VALUE
    String oplPOSVPNNormalneMies	 = DEFAULT_VALUE
    String oplPOSVPNNormalnePP	 = DEFAULT_VALUE
    String oplPOSVPNPreferencyjneMies	 = DEFAULT_VALUE
    String oplPOSVPNPreferencyjnePP	 = DEFAULT_VALUE

    String oplPOSSSLTyp	 = DEFAULT_VALUE
    String oplPOSSSLIlosc	 = DEFAULT_VALUE
    String oplPOSSSLIloscPP	 = DEFAULT_VALUE
    String oplPOSSSLNormalneMies	 = DEFAULT_VALUE
    String oplPOSSSLNormalnePP	 = DEFAULT_VALUE
    String oplPOSSSLPreferencyjneMies	 = DEFAULT_VALUE
    String oplPOSSSLPreferencyjnePP	 = DEFAULT_VALUE

    String oplPOSWiFiTyp	 = DEFAULT_VALUE
    String oplPOSWiFiIlosc	 = DEFAULT_VALUE
    String oplPOSWiFiIloscPP	 = DEFAULT_VALUE
    String oplPOSWiFiNormalneMies	 = DEFAULT_VALUE
    String oplPOSWiFiNormalnePP	 = DEFAULT_VALUE
    String oplPOSWiFiPreferencyjneMies	 = DEFAULT_VALUE
    String oplPOSWiFiPreferencyjnePP	 = DEFAULT_VALUE

    String oplPOSGPRSTyp	 = DEFAULT_VALUE
    String oplPOSGPRSIlosc	 = DEFAULT_VALUE
    String oplPOSGPRSIloscPP	 = DEFAULT_VALUE
    String oplPOSGPRSNormalneMies	 = DEFAULT_VALUE
    String oplPOSGPRSNormalnePP	 = DEFAULT_VALUE
    String oplPOSGPRSPreferencyjneMies	 = DEFAULT_VALUE
    String oplPOSGPRSPreferencyjnePP	 = DEFAULT_VALUE

    String oplPOSBaza	 = DEFAULT_VALUE

//    serwis - FINISH
    String obslugaTyp	 = DEFAULT_VALUE
    String obslugaEkonomicznyCena	 = DEFAULT_VALUE

//    rachunekBankowyKlienta
    String numerRachunkuBankowegoKlienta	 = DEFAULT_VALUE
    String bankKlienta	 = DEFAULT_VALUE

//    oplataDCCZaUruchomienie
    //TODO - to w pdfach sie nazywa inaczej - nie wiem jak...	 = DEFAULT_VALUE
    String oplataZaUruchomienieDCC	 = DEFAULT_VALUE

//    liczbaMiesiecyZwolnieniaZNajm

    String nip	 = DEFAULT_VALUE

//    uwagi
    String notes	 = DEFAULT_VALUE


    transient Process process

    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    List<PointCommand> poses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
    List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand))
    List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))

    String hasUmowaCzas
    String hasKontaktTel
    String hasDoladowania
    String hasAkceptantTel
    String hasInformacjaHandlowa
    String liczbaTerminali

    static constraints = {

        oplataZaDzienneZestawienieTransakcji(nullable:false, blank:false, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:false, blank:false, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:false, blank:false, shared: "number")
        oplataZaDostarczeniePapieru(nullable:false, blank:false, shared: "number")
        oplataZaZmianeGrafiki(nullable:false, blank:false, shared: "number")
        oplataZaInstalacjePOS(nullable:false, blank:false, shared: "number")
        oplataZaInstalacjeGPRS(nullable:false, blank:false, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:false, blank:false, shared: "number")
        // wydrukGrafikiCena(nullable:false, blank:false, shared: "number")
        // dzialaniaMatematyczneCena(nullable:false, blank:false, shared: "number")
        tytulPlatnosciCena(nullable:false, blank:false, shared: "number")
        pierwszaSesjaCena(nullable:false, blank:false, shared: "number")

        akceptantKontaktUlicaTytul(nullable:false, blank:false)
        akceptantKontaktUlica(nullable:false, blank:false, shared: "alpha")
        akceptantKontaktNrDomu(nullable:false, blank:false, shared: "alpha")
        akceptantKontaktNrMieszkania(nullable:true, blank:false, shared: "alpha")
        akceptantKontaktMiasto(nullable:false, blank:false, shared: "alpha")
        akceptantKontaktKodPocztowy(nullable:false, blank:false)
        akceptantKontaktPoczta(nullable:false, blank:false, shared: "alpha")
        /*  dataAneksowanejUmowyPos(nullable:false, blank:false, shared: "date")
            dataAneksowanejUmowyPrepaid(nullable:false, blank:false, shared: "date")*/

        hasUmowaCzas(nullable:true, validator: { value, cmd, errors ->
            if(value && cmd.umowaCzas == DEFAULT_VALUE){
                errors.rejectValue( "hasUmowaCzas", "default.atLeastOne.czasUmowy")
                return false
            }
            return true
        })

        umowaCzas(nullable:false, blank:false)

        umowaOznOd(nullable:true, blank:true, shared: "date",validator: { value, cmd, errors ->
            if(value == null && cmd.umowaCzas == "oznaczony"){
                errors.rejectValue( "umowaOznOd", "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        umowaOznDo(nullable:true, blank:true, shared: "date",validator: { value, cmd, errors ->
            if(value == null && cmd.umowaCzas == "oznaczony"){
                errors.rejectValue( "umowaOznDo", "default.validation.required.error", "Pole wymagane")
                return false
            }
            return true
        })

        akceptantNazwaOficjalna(nullable:false, blank:false) //a1!, M
        akceptantNazwaSieciowa(nullable:false, blank:true) //a1!
        akceptantRegon(nullable:false, blank:false, matches:"~|[0-9]{9}") //111111111, M
        akceptantNazwaOficjalnaCbd(nullable:true)
        akceptantNazwaSieciowaCbd(nullable:true)
        akceptantRegonCbd(nullable:true)
        nazwaDoWydrukuZTerminalaPos(nullable:true)
        wydrukNazwaDoWyszukwarki(nullable:true)
        wydrukUlicaTytul(nullable:false, blank:false)
        wydrukUlica(nullable:false, blank:false, shared: "alpha")
        wydrukNrDomu(nullable:false, blank:false, shared: "alpha")
        wydrukNrMieszkania(nullable:true, blank:false, shared: "alpha")
        wydrukMiasto(nullable:false, blank:false, shared: "alpha")
        wydrukKodPocztowy(nullable:false, blank:false)
        wydrukPoczta(nullable:false, blank:false, shared: "alpha")
        wydrukLinia1(nullable:true, blank:true)
        wydrukLinia2(nullable:true, blank:true)
        /*   oplataVISA(nullable:false, blank:false, shared: "number")
           oplataVISAPr(nullable:false, blank:false, shared: "number")
           oplataMasterCard(nullable:false, blank:false, shared: "number")
           oplataMasterCardPr(nullable:false, blank:false, shared: "number")
           oplataMaestro(nullable:false, blank:false, shared: "number")
           oplataMasteroPr(nullable:false, blank:false, shared: "number")*/
        dccZakresUruchomienia(nullable:false, blank:false)

        informacjaHandlowa(nullable:false, blank:false)

        hasInformacjaHandlowa(nullable:true, validator: { value, cmd, errors ->
            if(value && cmd.informacjaHandlowa == DEFAULT_VALUE){
                errors.rejectValue( "hasInformacjaHandlowa", "default.atLeastOne.informacjaHandlowa")
                return false
            }
            return true
        })


        oplataZaDzienneZestawienieTransakcji(nullable:false, blank:false, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:false, blank:false, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:false, blank:false, shared: "number")
        oplataZaDostarczeniePapieru(nullable:false, blank:false, shared: "number")
        oplataZaZmianeGrafiki(nullable:false, blank:false, shared: "number")
        oplataZaInstalacjePOS(nullable:false, blank:false, shared: "number")
        oplataZaInstalacjeGPRS(nullable:false, blank:false, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:false, blank:false, shared: "number")
        // wydrukGrafikiCena(nullable:false, blank:false, shared: "number")
        // dzialaniaMatematyczneCena(nullable:false, blank:false, shared: "number")
        tytulPlatnosciCena(nullable:false, blank:true, shared: "number")
        pierwszaSesjaCena(nullable:false, blank:true, shared: "number")
        mudCena(nullable:false, blank:true, shared:"number")
        weryfikacjaPINCena(nullable:true)
        systemKasowyCena(nullable:true)

        doladowania_tk(nullable:true)
        doladowania_tp(nullable:true)
        hasDoladowania(nullable:true, validator: { value, process, errors ->
            if(value == null){
                return true
            }

            if (!process.doladowania_tp && !process.doladowania_tk) {
                errors.rejectValue( "hasDoladowania", "default.atLeastOne.doladowania")
                return false
            }
            return true
        })

        /*srednia_sprzedaz_doladowan(nullable:false, blank:false, shared:"number")*/
        srednia_sprzedaz_doladowan_slownie(nullable:false, blank:false, shared: "lettersonly")
/*        ifOplataVISA(nullable:false, blank:false, shared: "number") //1.11 %, M
        ifOplataMasterCard(nullable:false, blank:false, shared: "number") //1.11 %, M
        ifOplataDinersClub(nullable:false, blank:false, shared: "number") //1.11 %, M
        ifOplataIKO(nullable:false, blank:false, shared: "number") //1.11 %, M
        ifOplataPKOPB(nullable:false, blank:false, shared: "number") //1.11 %, M*/
        dzialalnoscForma(nullable:false, blank:true)
        dzialalnoscFormaInna(nullable:true, blank:true, shared: "alpha")
        dzialalnoscDokument(nullable:false, blank:true)
        dzialalnoscDokumentInny(nullable:true, blank:true, shared: "alpha")
        //okresLojalnosciowy(nullable:false, blank:false)
        /*oplataZaPlatnoscWInnejWalucie(nullable:false, blank:false)*/
        kontaktTytul(nullable:false, blank:false)
        kontaktImie(nullable:false, blank:false, shared: "lettersonly")
        kontaktNazwisko(nullable:false, blank:false, shared: "lettersonly")

        hasKontaktTel(nullable:true, validator: { value, process, errors ->
            if(value == null){
                return true
            }

            if (!process.kontaktTelKomorkowy && !process.kontaktTelStacjonarny) {
                errors.rejectValue( "hasKontaktTel", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        kontaktTelKomorkowy(nullable:true)
        kontaktTelStacjonarny(nullable:true)
        kontaktEmail(nullable:true, blank:true, shared: "email")
        pozyskujacyTytul(nullable:false, blank:false)
        pozyskujacyImie(nullable:false, blank:false, shared: "lettersonly")
        pozyskujacyNazwisko(nullable:false, blank:false, shared: "lettersonly")
        pozyskujacyNumer(nullable:false, blank:false)
        reprezentant1Tytul(nullable:false, blank:false)
        reprezentant1Imie(nullable:false, blank:false, shared: "lettersonly")
        reprezentant1Nazwisko(nullable:false, blank:false, shared: "lettersonly")
        reprezentant2Tytul(nullable:false, blank:false)
        reprezentant2Imie(nullable:true, blank:true, shared: "lettersonly")
        reprezentant2Nazwisko(nullable:true, blank:true, shared: "lettersonly")

        visaEUKKOSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaEUKKOSt", "OPLATA_MSC_53_ZL")
        })

        visaEUKDSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaEUKDSt", "OPLATA_MSC_12_ZL")
        })

        visaEUKBSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaEUKBSt", "OPLATA_MSC_13_ZL")
        })

        visaOutEUKKOSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaOutEUKKOSt", "OPLATA_MSC_21_ZL")
        })

        visaOutEUKDSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaOutEUKDSt", "OPLATA_MSC_22_ZL")
        })

        visaOutEUKBSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaOutEUKBSt", "OPLATA_MSC_23_ZL")
        })

        visaPolskaKBSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "visaPolskaKBSt", "OPLATA_MSC_33_ZL")
        })

        mastercardEUKKSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardEUKKSt", "OPLATA_MSC_41_ZL")
        })

        mastercardEUKDSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardEUKDSt", "OPLATA_MSC_42_ZL")
        })
        mastercardEUKBLSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardEUKBLSt", "OPLATA_MSC_43_ZL")
        })
        mastercardEUMSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardEUMSt", "OPLATA_MSC_44_ZL")
        })
        mastercardOutEUKKSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKKSt", "OPLATA_MSC_51_ZL")
        })
        mastercardOutEUKDSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKDSt", "OPLATA_MSC_52_ZL")
        })
        mastercardOutEUKBSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUKBSt", "OPLATA_MSC_53_ZL")
        })
        mastercardOutEUMSt(nullable:false, blank:false, shared: "number", validator: { value, cmd, errors ->
            atLeastClosure.call(value, cmd, errors, "mastercardOutEUMSt", "OPLATA_MSC_54_ZL")
        })

        dinersClubSt(nullable:true,blank:true,shared: "number")
        ikoSt(nullable:true, blank:true,shared: "number")

        /*
        visaEUKKOPr(nullable:false, blank:false, shared: "percentage")
         visaEUKKOPr(nullable:false, blank:false, shared: "percentage")
         visaEUKDPr(nullable:false, blank:false, shared: "percentage")
            visaEUKBPr(nullable:false, blank:false, shared: "percentage")
            visaOutEUKKOPr(nullable:false, blank:false, shared: "percentage")
            visaOutEUKDPr(nullable:false, blank:false, shared: "percentage")
            visaOutEUKBPr(nullable:false, blank:false, shared: "percentage")
            visaPolskaKKO1Pr(nullable:false, blank:false, shared: "percentage")
            visaPolskaKKO2Pr(nullable:false, blank:false, shared: "percentage")
            visaPolskaKD1Pr(nullable:false, blank:false, shared: "percentage")
            visaPolskaKD2Pr(nullable:false, blank:false, shared: "percentage")
            visaPolskaKBPr(nullable:false, blank:false, shared: "percentage")
            mastercardEUKKPr(nullable:false, blank:false, shared: "percentage")
            mastercardEUKDPr(nullable:false, blank:false, shared: "percentage")
            mastercardEUKBLPr(nullable:false, blank:false, shared: "percentage")
            mastercardEUMPr(nullable:false, blank:false, shared: "percentage")
            mastercardOutEUKKPr(nullable:false, blank:false, shared: "percentage")
            mastercardOutEUKDPr(nullable:false, blank:false, shared: "percentage")
            mastercardOutEUKBPr(nullable:false, blank:false, shared: "percentage")
            mastercardOutEUMPr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKK1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKK2Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKK3Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKD1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKD2Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKD3Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaKBPr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaM1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaM2Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPolskaM3Pr(nullable:false, blank:false, shared: "percentage")
            visaPKOBPKKO1Pr(nullable:false, blank:false, shared: "percentage")
            visaPKOBPKKO2Pr(nullable:false, blank:false, shared: "percentage")
            visaPKOBPKD1Pr(nullable:false, blank:false, shared: "percentage")
            visaPKOBPKD2Pr(nullable:false, blank:false, shared: "percentage")
            visaPKOBPKB3Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKK1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKK2Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKK3Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKD1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKD2LPr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKD3Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPKBPr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPM1Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPM2Pr(nullable:false, blank:false, shared: "percentage")
            mastercardPKOBPM3Pr(nullable:false, blank:false, shared: "percentage")
            dinersClubPr(nullable:false, blank:false, shared: "percentage")
            ikoPr(nullable:true, blank:true,  shared: "percentage")
            visaEUKKOSt(nullable:false, blank:false, shared: "number")
            visaEUKDSt(nullable:false, blank:false, shared: "number")
            visaEUKBSt(nullable:false, blank:false, shared: "number")
            visaOutEUKKOSt(nullable:false, blank:false, shared: "number")
            visaOutEUKDSt(nullable:false, blank:false, shared: "number")
            visaOutEUKBSt(nullable:false, blank:false, shared: "number")
            visaPolskaKKO1St(nullable:false, blank:false, shared: "number")
            visaPolskaKKO2St(nullable:false, blank:false, shared: "number")
            visaPolskaKD1St(nullable:false, blank:false, shared: "number")
            visaPolskaKD2St(nullable:false, blank:false, shared: "number")
            visaPolskaKBSt(nullable:false, blank:false, shared: "number")
            mastercardEUKKSt(nullable:false, blank:false, shared: "number")
            mastercardEUKDSt(nullable:false, blank:false, shared: "number")
            mastercardEUKBLSt(nullable:false, blank:false, shared: "number")
            mastercardEUMSt(nullable:false, blank:false, shared: "number")
            mastercardOutEUKKSt(nullable:false, blank:false, shared: "number")
            mastercardOutEUKDSt(nullable:false, blank:false, shared: "number")
            mastercardOutEUKBSt(nullable:false, blank:false, shared: "number")
            mastercardOutEUMSt(nullable:false, blank:false, shared: "number")
            mastercardPolskaKK1St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKK2St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKK3St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKD1St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKD2St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKD3St(nullable:false, blank:false, shared: "number")
            mastercardPolskaKBSt(nullable:false, blank:false, shared: "number")
            mastercardPolskaM1St(nullable:false, blank:false, shared: "number")
            mastercardPolskaM2St(nullable:false, blank:false, shared: "number")
            mastercardPolskaM3St(nullable:false, blank:false, shared: "number")
            visaPKOBPKKO1St(nullable:false, blank:false, shared: "number")
            visaPKOBPKKO2St(nullable:false, blank:false, shared: "number")
            visaPKOBPKD1St(nullable:false, blank:false, shared: "number")
            visaPKOBPKD2St(nullable:false, blank:false, shared: "number")
            visaPKOBPKB3St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKK1St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKK2St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKK3St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKD1St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKD2LSt(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKD3St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPKBSt(nullable:false, blank:false, shared: "number")
            mastercardPKOBPM1St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPM2St(nullable:false, blank:false, shared: "number")
            mastercardPKOBPM3St(nullable:false, blank:false, shared: "number")
            dinersClubSt(nullable:true,blank:true,shared: "number")
            ikoSt(nullable:true, blank:true,shared: "number")*/

        /*  pp_orange_tk(nullable:false, blank:false, shared: "percentage")
          pp_orange_tp(nullable:false, blank:false, shared: "percentage")
          pp_plus_tk(nullable:false, blank:false, shared: "percentage")
          pp_plus_tp(nullable:false, blank:false, shared: "percentage")
          pp_tmobile_tk(nullable:false, blank:false, shared: "percentage")
          pp_tmobile_tp(nullable:false, blank:false, shared: "percentage")
          pp_heyah_tk(nullable:false, blank:false, shared: "percentage")
          pp_heyah_tp(nullable:false, blank:false, shared: "percentage")
          pp_play_tk(nullable:false, blank:false, shared: "percentage")
          pp_play_tp(nullable:false, blank:false, shared: "percentage")
          pp_telegrosik_tk(nullable:false, blank:false, shared: "percentage")
          pp_virginmobile_tk(nullable:false, blank:false, shared: "percentage")
          pp_lycamobile_tk(nullable:false, blank:false, shared: "percentage")
          pp_gtmobile_tk(nullable:false, blank:false, shared: "percentage")
          pp_vectonemobile_tk(nullable:false, blank:false, shared: "percentage")
          pp_delightmobile_tk(nullable:false, blank:false, shared: "percentage")*/

        //oplataZaOprogramowanieDoDoladowan(nullable:false, blank:false, shared: "number" )
        //scoringMcc(nullable:false, blank:false, matches:"~|[0-9]{4}")
        scoringDzialalnosc(nullable:false, blank:false)
        scoringSzczegolyDzialalnosci(nullable:true, blank:true)
        scoringWlasnosc(nullable:false, blank:false)
        scoringDzialalnoscCzas(nullable:false, blank:false)
        scoringKoncesja(nullable:false, blank:false)
        rodzajZezwolenia(nullable:true, blank:true)
        scoringCharakterystyka(nullable:false, blank:false)
        scoringCharakterystykaInna(nullable:true, blank:true)
        scoringWielkoscPunktu(nullable:false, blank:false)
        scoringAkceptacja(nullable:false, blank:false)
        scoringMonitoring(nullable:false, blank:false)
        scoringLokalizacjaPunktu(nullable:false, blank:false)
        scoringTypPunktu(nullable:false, blank:false)
        scoringTypPunktuInny(nullable:true, blank:true)
        scoringWielkoscMiejscowosci(nullable:false, blank:false)
        scoringOtwartyZamkniety(nullable:false, blank:false)
        scoringStanZadbany(nullable:true)
        scoringSprzedazTowarowEkskluzywnych(nullable:true) //Boolean
        scoringPonad50ProcentObrotowWNocy(nullable:true) //Boolean
        scoringRuchTurystycznyPrzygraniczny(nullable:true)//Boolean
        scoringUslugiPlatneZGory(nullable:true)//Boolean
        scoringCzestoscTransakcji(nullable:false, blank:false)
        scoringIloscTransakcji(nullable:false, blank:false)
        scoringDochodowosc(nullable:true, blank:true)
        scoringDeklaracjaFinansowa(nullable:false, blank:false)
        scoringDeklaracjaFinansowaObrotOgolem(nullable:true, blank:true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniObrot(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable:true, blank:true)
        akceptantUlicaTytul(nullable:false, blank:false)
        akceptantUlica(nullable:false, blank:false, shared: "alpha")
        akceptantNrDomu(nullable:false, blank:false, shared: "alpha")
        akceptantNrMieszkania(nullable:true, blank:false, shared: "alpha")
        akceptantMiasto(nullable:false, blank:false, shared: "alpha")
        akceptantKodPocztowy(nullable:false, blank:false)
        akceptantPoczta(nullable:false, blank:false, shared: "alpha")

     /*   hasAkceptantTel(validator: { value, process, errors ->
            if (value == null || process.akceptantTelKomorkowy == null) {
                return true
            }
            if (value.isEmpty() && process.akceptantTelKomorkowy.isEmpty()){
                errors.rejectValue( "akceptantTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                errors.rejectValue( "akceptantTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                return false
            }
            return true
        })*/

        hasAkceptantTel(nullable:true, validator: { value, process, errors ->
            if(value == null){
                return true
            }

            if (!process.akceptantTelStacjonarny && !process.akceptantTelKomorkowy) {
                errors.rejectValue( "hasAkceptantTel", "default.atLeastOne.phoneNumber")
                return false
            }
            return true
        })

        akceptantTelKomorkowy(nullable:true)
        akceptantTelStacjonarny(nullable:true)

        akceptantFax(nullable:true, blank:true)
        akceptantUlicaTytulCbd(nullable:true)
        akceptantUlicaCbd(nullable:true)
        akceptantNrDomuCbd(nullable:true)
        akceptantNrMieszkaniaCbd(nullable:true)
        akceptantMiastoCbd(nullable:true)
        akceptantKodPocztowyCbd(nullable:true)
        akceptantPocztaCbd(nullable:true)
        akceptantTelStacjonarnyCbd(nullable:true)
        akceptantFaxCbd(nullable:true)
        akceptantTelKomorkowyCbd(nullable:true)
        miejsceUmowy(nullable:true)
        oplPOSDialUpTyp(nullable:true)
        oplPOSDialUpIlosc(nullable:true, shared: "natural")
        oplPOSDialUpIloscPP(nullable:true, shared: "natural")
        oplPOSDialUpNormalneMies(nullable:true, shared: "number")
        oplPOSDialUpNormalnePP(nullable:true, shared: "number")
        oplPOSDialUpPreferencyjneMies(nullable:true, shared: "number")
        oplPOSDialUpPreferencyjnePP(nullable:true, shared: "number")
        oplPOSVPNTyp(nullable:true)
        oplPOSVPNIlosc(nullable:true, shared: "natural")
        oplPOSVPNIloscPP(nullable:true, shared: "natural")
        oplPOSVPNNormalneMies(nullable:true, shared: "number")
        oplPOSVPNNormalnePP(nullable:true, shared: "number")
        oplPOSVPNPreferencyjneMies(nullable:true, shared: "number")
        oplPOSVPNPreferencyjnePP(nullable:true, shared: "number")
        oplPOSSSLTyp(nullable:true)
        oplPOSSSLIlosc(nullable:true, shared: "natural")
        oplPOSSSLIloscPP(nullable:true, shared: "natural")
        oplPOSSSLNormalneMies(nullable:true, shared: "number")
        oplPOSSSLNormalnePP(nullable:true, shared: "number")
        oplPOSSSLPreferencyjneMies(nullable:true, shared: "number")
        oplPOSSSLPreferencyjnePP(nullable:true, shared: "number")
        oplPOSWiFiTyp(nullable:true)
        oplPOSWiFiIlosc(nullable:true, shared: "natural")
        oplPOSWiFiIloscPP(nullable:true, shared: "natural")
        oplPOSWiFiNormalneMies(nullable:true, shared: "number")
        oplPOSWiFiNormalnePP(nullable:true, shared: "number")
        oplPOSWiFiPreferencyjneMies(nullable:true, shared: "number")
        oplPOSWiFiPreferencyjnePP(nullable:true, shared: "number")
        oplPOSGPRSTyp(nullable:true)
        oplPOSGPRSIlosc(nullable:true, shared: "natural")
        oplPOSGPRSIloscPP(nullable:true, shared: "natural")
        oplPOSGPRSNormalneMies(nullable:true, shared: "number")
        oplPOSGPRSNormalnePP(nullable:true, shared: "number")
        oplPOSGPRSPreferencyjneMies(nullable:true, shared: "number")
        oplPOSGPRSPreferencyjnePP(nullable:true, shared: "number")
        oplPOSBaza(nullable:true, shared: "number")
        obslugaTyp(nullable:false, blank:false)
        obslugaEkonomicznyCena(nullable:true, blank:false, shared: "number") //TODO VERIFY
        numerRachunkuBankowegoKlienta(nullable:false, blank:false, matches: "~|\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}")
        bankKlienta(nullable:false, blank:false)
        oplataZaUruchomienieDCC(nullable:false, blank:false, shared: "number")
        //nip(nullable:true)
        notes(nullable:true) //a1!
        points(nullable:true)
        poses(nullable:true)
        allPoints(nullable:true)
        allPoses(nullable:true)

        liczbaTerminali(nullable:true, validator: { value, cmd, errors ->
            //println("liczbaTerminali : " + value)
            def max = value ? Integer.valueOf(value) : 0
            def counter = 0

            cmd.points?.each{ point ->
                //println("point.terminalIlosc : " + point.terminalIlosc)
                if(point.terminalIlosc?.toString()?.isNumber()){
                    counter += Integer.valueOf(point.terminalIlosc)
                }
            }
            /* cmd.allPoses?.each{ PosData pos ->
                if(pos.posDetails.terminalIlosc){
                    counter += pos.pointDetails.terminalIlosc;
                }
            }*/

            if( counter > max) {
                errors.rejectValue( "liczbaTerminali", "default.tooMuch.liczbaTerminali",)
                return false
            }
            return true
        })

    }

    def isFromCbd(def property){
        def cbdName = property+"Cbd"
        return (this.metaClass.hasProperty(this, cbdName) && this."$cbdName"?.trim())
    }

    /* static nullableNotBlank(value){
         return value == null || value.size() > 0
     }*/
}