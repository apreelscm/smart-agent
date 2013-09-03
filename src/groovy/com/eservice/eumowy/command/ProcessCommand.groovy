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

//    adresacjaSeciowa
//    adresDoKorespondencjizAkecptantem - FINISH
    String akceptantKontaktUlicaTytul
    String akceptantKontaktUlica
    String akceptantKontaktNrDomu
    String akceptantKontaktNrMieszkania

    String akceptantKontaktMiasto
    String akceptantKontaktKodPocztowy
    String akceptantKontaktPoczta

//    aneksDoUmowyNajmuZestawuPos - FINISH
    String dataAneksowanejUmowyPos

//    aneksDoUmowyPrepaid - FINISH (ale trzeba zobaczyc w dokumentach czy jest dobrze wpisany)
    String dataAneksowanejUmowyPrepaid

//    czasObowiazywaniaUmowy - FINISH
    String umowaCzas //TODO -  MOZLIWE, ze pdfach jest to umowaOzn, umowaNieOzn
    String umowaOznOd
    String umowaOznDo



//    daneAkceptanta
    String akceptantNazwaOficjalna
    String akceptantNazwaSieciowa
    //String akceptantNip - trzymane we wspolnym polu nip
    String akceptantRegon

    String akceptantNazwaOficjalnaCbd
    String akceptantNazwaSieciowaCbd
    String akceptantRegonCbd

//    daneDoWydruku
    String nazwaDoWydrukuZTerminalaPos
    String wydrukNazwaDoWyszukwarki

    String wydrukUlicaTytul
    String wydrukUlica
    String wydrukNrDomu
    String wydrukNrMieszkania

    String wydrukMiasto
    String wydrukKodPocztowy
    String wydrukPoczta

    String wydrukLinia1
    String wydrukLinia2


//    danePunktu
//    dcc - FINISH
    String oplataVISA
    String oplataVISAPr
    String oplataMasterCard
    String oplataMasterCardPr
    String oplataMaestro
    String oplataMasteroPr

//    dccZakresUruchomienia
    String dccZakresUruchomienia

//    deklaracjeAkceptanta - FINISH
    String informacjaHandlowa
    //TODO - w pdf wykorzystujemy informacjaHandlowaTak i informacjaHandlowaNie (checkboxy)


//    dodajPunkt
//    dodatkoweUslugi - FINISH
    String oplataZaDzienneZestawienieTransakcji
    String oplataZaMiesieczneZestawienieTransakcji
    String oplataZaPotwierdzenieWykonaniaPrzelewu
    String oplataZaDostarczeniePapieru
    String oplataZaZmianeGrafiki
    String oplataZaInstalacjePOS
    String oplataZaInstalacjeGPRS
    String oplataZaUruchomienieWalutyObcej

//    dodatkoweUslugi2 - FINISH (ale trzeba jeszcze daty startu pobrac)
    String wydrukGrafikiCena
    String dzialaniaMatematyczneCena
    String tytulPlatnosciCena
    String pierwszaSesjaCena

//    dodatkoweUslugiMud - FINISH
    String mudCena

//    dodatkoweUslugiUTAIntegracja - FINISH (ale trzeba jeszcze daty startu pobrac)
    String weryfikacjaPINCena
    String systemKasowyCena

//    formaDoladowania - FINISH
    String doladowania_tp
    String doladowania_tk
    String srednia_sprzedaz_doladowan
    String srednia_sprzedaz_doladowan_slownie

//    ifplus - FINISH (ale zmiany w dokumentach)
    String ifOplataVISA
    String ifOplataMasterCard
    String ifOplataDinersClub
    String ifOplataIKO
    String ifOplataPKOPB

//    informacjeDodatkowe - FINISH
    String dzialalnoscForma
    String dzialalnoscFormaInna
    String dzialalnoscDokument
    String dzialalnoscDokumentInny

//    okresLojalnosciowy
    String okresLojalnosciowy  //  TODO - czy to jest dobrze????

//    oplatyDCC
    String oplataZaPlatnoscWInnejWalucie
//    osobaDoKontaktu
    String kontaktTytul
    String kontaktImie
    String kontaktNazwisko
    String kontaktTelStacjonarny
    String kontaktTelKomorkowy
    String kontaktEmail

//    osobaKtoraPozyskalaAkceptanta  - FINISH (nie wystepuje w pdfach)
    String pozyskujacyTytul
    String pozyskujacyImie
    String pozyskujacyNazwisko
    String pozyskujacyNumer


//    osobaUprawnionaDoPodpisaniaUmowy - FINISH
    String reprezentant1Tytul
    String reprezentant1Imie
    String reprezentant1Nazwisko
    String reprezentant2Tytul
    String reprezentant2Imie
    String reprezentant2Nazwisko
    //TODO - w pdf wykorzystujemy reprezentant1 i reprezentant2 przechowujace imie i nazwisko

//    poziomOplatiWarunkiPlatnosciKarty - FINISH
    String visaEUKKOPr
	String visaEUKDPr
	String visaEUKBPr
	String visaOutEUKKOPr
	String visaOutEUKDPr
	String visaOutEUKBPr
	String visaPolskaKKO1Pr
	String visaPolskaKKO2Pr
	String visaPolskaKD1Pr
	String visaPolskaKD2Pr
	String visaPolskaKBPr
	String mastercardEUKKPr
	String mastercardEUKDPr
	String mastercardEUKBLPr
	String mastercardEUMPr
	String mastercardOutEUKKPr
	String mastercardOutEUKDPr
	String mastercardOutEUKBPr
	String mastercardOutEUMPr
	String mastercardPolskaKK1Pr
	String mastercardPolskaKK2Pr
	String mastercardPolskaKK3Pr
	String mastercardPolskaKD1Pr
	String mastercardPolskaKD2Pr
	String mastercardPolskaKD3Pr
	String mastercardPolskaKBPr
	String mastercardPolskaM1Pr
	String mastercardPolskaM2Pr
	String mastercardPolskaM3Pr
	String visaPKOBPKKO1Pr
	String visaPKOBPKKO2Pr
	String visaPKOBPKD1Pr
	String visaPKOBPKD2Pr
	String visaPKOBPKB3Pr
	String mastercardPKOBPKK1Pr
	String mastercardPKOBPKK2Pr
	String mastercardPKOBPKK3Pr
	String mastercardPKOBPKD1Pr
	String mastercardPKOBPKD2LPr
	String mastercardPKOBPKD3Pr
	String mastercardPKOBPKBPr
	String mastercardPKOBPM1Pr
	String mastercardPKOBPM2Pr
	String mastercardPKOBPM3Pr
	String dinersClubPr
	String ikoPr
	String visaEUKKOSt
	String visaEUKDSt
	String visaEUKBSt
	String visaOutEUKKOSt
	String visaOutEUKDSt
	String visaOutEUKBSt
	String visaPolskaKKO1St
	String visaPolskaKKO2St
	String visaPolskaKD1St
	String visaPolskaKD2St
	String visaPolskaKBSt
	String mastercardEUKKSt
	String mastercardEUKDSt
	String mastercardEUKBLSt
	String mastercardEUMSt
	String mastercardOutEUKKSt
	String mastercardOutEUKDSt
	String mastercardOutEUKBSt
	String mastercardOutEUMSt
	String mastercardPolskaKK1St
	String mastercardPolskaKK2St
	String mastercardPolskaKK3St
	String mastercardPolskaKD1St
	String mastercardPolskaKD2St
	String mastercardPolskaKD3St
	String mastercardPolskaKBSt
	String mastercardPolskaM1St
	String mastercardPolskaM2St
	String mastercardPolskaM3St
	String visaPKOBPKKO1St
	String visaPKOBPKKO2St
	String visaPKOBPKD1St
	String visaPKOBPKD2St
	String visaPKOBPKB3St
	String mastercardPKOBPKK1St
	String mastercardPKOBPKK2St
	String mastercardPKOBPKK3St
	String mastercardPKOBPKD1St
	String mastercardPKOBPKD2LSt
	String mastercardPKOBPKD3St
	String mastercardPKOBPKBSt
	String mastercardPKOBPM1St
	String mastercardPKOBPM2St
	String mastercardPKOBPM3St
	String dinersClubSt
	String ikoSt

//    poziomOplatIWarunkiPlatnosciPP - FINISH
    String pp_orange_tk
    String pp_orange_tp
    String pp_plus_tk
    String pp_plus_tp
    String pp_tmobile_tk
    String pp_tmobile_tp
    String pp_heyah_tk
    String pp_heyah_tp
    String pp_play_tk
    String pp_play_tp
    String pp_telegrosik_tk
    String pp_virginmobile_tk
    String pp_lycamobile_tk
    String pp_gtmobile_tk
    String pp_vectonemobile_tk
    String pp_delightmobile_tk
    String oplataZaOprogramowanieDoDoladowan

//    promocyjneObnizenieOplatyZaZestawPos
//    scoring
//    TODO - co wpisac w scoringMidFirmy i scoringNrUmowy????

    String scoringMcc
    String scoringDzialalnosc
    String scoringSzczegolyDzialalnosci
//    TODO - w pdf uzywamy pol: handel, uslugi (checkboxy)
//    TODO - co to sa za pola: MCC, szczegolowyRodzajDzialalnosciWPraktyce ??

    String scoringWlasnosc = "1"

//    TODO - w pdf uzywamy pol: wlasnosc, wynajem (checkboxy)

    String scoringDzialalnoscCzas
//    TODO - w pdf uzywamy pol: powyzej5lat, od1do5lat, ponizejRoku

    String scoringKoncesja
//    TODO - w pdf dzialalnoscWymagaLicencjiTak, dzialalnoscWymagaLicencjiNie

    String rodzajZezwolenia
    String scoringCharakterystyka
//    TODO - w pdf salon, sklep, stoisko, stacjaPaliw, charakterystykaInny

    String scoringCharakterystykaInna
    String scoringWielkoscPunktu
//    TODO - w pfd powyzej400m2, od50do400m2, do50m2

    String scoringAkceptacja
//    TODO - w pdf akceptacjaKartPlatniczychTak, akceptacjaKartPlatniczychNie

    String scoringMonitoring
//    TODO - w pdf wPunktachMonitoringTak, wPunktachMonitoringNie

    String scoringLokalizacjaPunktu
//    TODO - w pdf trasaPrzelotowa, centrumMiasta, peryferiaMiasta

    String scoringTypPunktu
//    TODO - w pdf centrumHandlowe, pawilonyHandlowe, budynekWolnoStojacy, osiedleMieszkaniowe, targowisko
    String scoringTypPunktuInny
    String scoringWielkoscMiejscowosci
//    TODO - w pdf miastoPonad500tysChb, miastoOd100Do500tysChb, miastoOd50Do99tysChb, miastoPonizej50tysChb

    String scoringOtwartyZamkniety
//    TODO - w pdf czynne, nieczynne

    String scoringStanZadbany
//    TODO - w pdf stanZadbany

    String scoringSprzedazTowarowEkskluzywnych
    String scoringPonad50ProcentObrotowWNocy
    String scoringRuchTurystycznyPrzygraniczny
    String scoringUslugiPlatneZGory
//    TODO - w pdf sprzedazTowarowEkskluzywnych, ponad50ProcentObrotowWNocy, ruchTurystycznyPrzygraniczny, uslugiPlatneZGory

    String scoringCzestoscTransakcji
//    TODO - w pdf kilkaRazyWMiesiacu, kilkaRazyWTygodniu, coDrugiDzien, codziennie

    String scoringIloscTransakcji
//    TODO - w pdf od0do4, od5do10, powyzej10

    String scoringDochodowosc
    String scoringDeklaracjaFinansowa
//    TODO - w pdf wartosciWlasciwe, wartosciDeklarowane

    String scoringDeklaracjaFinansowaObrotOgolem
    String scoringDeklaracjaFinansowaObrotNaKarty
    String scoringDeklaracjaFinansowaSredniObrot
    String scoringDeklaracjaFinansowaSredniaTransakcja

//    serwisEkonomiczny - FINISH
//    serwisKomfort - FINISH
//    serwisPrzestiz - FINISH

//    siedzibaAkceptanta - FINISH
    String akceptantUlicaTytul
    String akceptantUlica
    String akceptantNrDomu
    String akceptantNrMieszkania

    String akceptantMiasto
    String akceptantKodPocztowy
    String akceptantPoczta

    String akceptantTelStacjonarny
    String akceptantFax
    String akceptantTelKomorkowy

    String akceptantUlicaTytulCbd
    String akceptantUlicaCbd
    String akceptantNrDomuCbd
    String akceptantNrMieszkaniaCbd

    String akceptantMiastoCbd
    String akceptantKodPocztowyCbd
    String akceptantPocztaCbd

    String akceptantTelStacjonarnyCbd
    String akceptantFaxCbd
    String akceptantTelKomorkowyCbd

    //TODO - w pdf wykorzystujemy pole akceptantSiedziba, w ktore wsadzamy to co potrzeba.

//    umowa2 - FINISH
    String miejsceUmowy //nie jest uzywane w pdf

//    wykazPunktowAkceptujacychKartyPlatnicze
    //TODO - to zmiany
    List punktyTytulPlatnosci
    //TODO - to zmiany
    List punktySystemKasowy
    //TODO - to zmiany
    List punktyUta
    //TODO - to zmiany
    List punktyWybrane

//    zestawPosOdplatneUzywanie
    String oplPOSDialUpTyp
    String oplPOSDialUpIlosc
    String oplPOSDialUpIloscPP
    String oplPOSDialUpNormalneMies
    String oplPOSDialUpNormalnePP
    String oplPOSDialUpPreferencyjneMies
    String oplPOSDialUpPreferencyjnePP

    String oplPOSVPNTyp
    String oplPOSVPNIlosc
    String oplPOSVPNIloscPP
    String oplPOSVPNNormalneMies
    String oplPOSVPNNormalnePP
    String oplPOSVPNPreferencyjneMies
    String oplPOSVPNPreferencyjnePP

    String oplPOSSSLTyp
    String oplPOSSSLIlosc
    String oplPOSSSLIloscPP
    String oplPOSSSLNormalneMies
    String oplPOSSSLNormalnePP
    String oplPOSSSLPreferencyjneMies
    String oplPOSSSLPreferencyjnePP

    String oplPOSWiFiTyp
    String oplPOSWiFiIlosc
    String oplPOSWiFiIloscPP
    String oplPOSWiFiNormalneMies
    String oplPOSWiFiNormalnePP
    String oplPOSWiFiPreferencyjneMies
    String oplPOSWiFiPreferencyjnePP

    String oplPOSGPRSTyp
    String oplPOSGPRSIlosc
    String oplPOSGPRSIloscPP
    String oplPOSGPRSNormalneMies
    String oplPOSGPRSNormalnePP
    String oplPOSGPRSPreferencyjneMies
    String oplPOSGPRSPreferencyjnePP

    String oplPOSBaza

//    serwis - FINISH
    String obslugaTyp
    String obslugaEkonomicznyCena
    //TODO - w pdf wykorzystujemy obsugaPrestiz, obslugaKomfort, obslugaEkonomiczny (checkboxy)

//    rachunekBankowyKlienta
    String numerRachunkuBankowegoKlienta
    String bankKlienta

//    oplataDCCZaUruchomienie
    //TODO - to w pdfach sie nazywa inaczej - nie wiem jak...
    String oplataZaUruchomienieDCC

//    liczbaMiesiecyZwolnieniaZNajmu

    String nip

//    uwagi
    String notes

    transient Process process

    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
	List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand)) 
	List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))
	
    static constraints = {
        notes(nullable:true)
        akceptantFax(nullable:true)
        akceptantFaxCbd(nullable:true)
        akceptantKodPocztowy(nullable:true)
        akceptantKodPocztowyCbd(nullable:true)
        akceptantKontaktKodPocztowy(nullable:true)
        akceptantKontaktMiasto(nullable:true)
        akceptantKontaktNrDomu(nullable:true)
        akceptantKontaktNrMieszkania(nullable:true)
        akceptantKontaktPoczta(nullable:true)
        akceptantKontaktUlica(nullable:true)
        akceptantMiasto(nullable:true)
        akceptantMiastoCbd(nullable:true)
        akceptantNazwaOficjalna(nullable:true)
        akceptantNazwaOficjalnaCbd(nullable:true)
        akceptantNazwaSieciowa(nullable:true)
        akceptantNazwaSieciowaCbd(nullable:true)
        akceptantNrDomu(nullable:true)
        akceptantNrDomuCbd(nullable:true)
        akceptantNrMieszkania(nullable:true)
        akceptantNrMieszkaniaCbd(nullable:true)
        akceptantPoczta(nullable:true)
        akceptantPocztaCbd(nullable:true)
        akceptantRegon(nullable:true)
        akceptantRegonCbd(nullable:true)
        akceptantTelKomorkowy(nullable:true)
        akceptantTelKomorkowyCbd(nullable:true)
        akceptantTelStacjonarny(nullable:true)
        akceptantTelStacjonarnyCbd(nullable:true)
        akceptantUlica(nullable:true)
        akceptantUlicaCbd(nullable:true)
        akceptantUlicaTytulCbd(nullable:true)
        dataAneksowanejUmowyPos(nullable:true)
        dzialalnoscDokumentInny(nullable:true)
        dzialalnoscForma(nullable:true)
        dzialalnoscFormaInna(nullable:true)
        dzialaniaMatematyczneCena(nullable:true)
        ifOplataDinersClub(nullable:true)
        ifOplataIKO(nullable:true)
        ifOplataMasterCard(nullable:true)
        ifOplataPKOPB(nullable:true)
        ifOplataVISA(nullable:true)
        informacjaHandlowa(nullable:true)
        kontaktEmail(nullable:true)
        kontaktImie(nullable:true)
        kontaktNazwisko(nullable:true)
        kontaktTelKomorkowy(nullable:true)
        kontaktTelStacjonarny(nullable:true)
        kontaktTytul(nullable:true)
        obslugaEkonomicznyCena(nullable:true)
        obslugaTyp(nullable:true)
        oplPOSDialUpIlosc(nullable:true)
        oplPOSDialUpIloscPP(nullable:true)
        oplPOSDialUpNormalneMies(nullable:true)
        oplPOSDialUpNormalnePP(nullable:true)
        oplPOSDialUpPreferencyjneMies(nullable:true)
        oplPOSDialUpPreferencyjnePP(nullable:true)
        oplPOSGPRSIlosc(nullable:true)
        oplPOSGPRSIloscPP(nullable:true)
        oplPOSGPRSNormalneMies(nullable:true)
        oplPOSGPRSNormalnePP(nullable:true)
        oplPOSGPRSPreferencyjneMies(nullable:true)
        oplPOSGPRSPreferencyjnePP(nullable:true)
        oplPOSSSLIlosc(nullable:true)
        oplPOSSSLIloscPP(nullable:true)
        oplPOSSSLNormalneMies(nullable:true)
        oplPOSSSLNormalnePP(nullable:true)
        oplPOSSSLPreferencyjneMies(nullable:true)
        oplPOSSSLPreferencyjnePP(nullable:true)
        oplPOSVPNIlosc(nullable:true)
        oplPOSVPNIloscPP(nullable:true)
        oplPOSVPNNormalneMies(nullable:true)
        oplPOSVPNNormalnePP(nullable:true)
        oplPOSVPNPreferencyjneMies(nullable:true)
        oplPOSVPNPreferencyjnePP(nullable:true)
        oplPOSWiFiIlosc(nullable:true)
        oplPOSWiFiIloscPP(nullable:true)
        oplPOSWiFiNormalneMies(nullable:true)
        oplPOSWiFiNormalnePP(nullable:true)
        oplPOSWiFiPreferencyjneMies(nullable:true)
        oplPOSWiFiPreferencyjnePP(nullable:true)
        pierwszaSesjaCena(nullable:true)
        pozyskujacyImie(nullable:true)
        pozyskujacyNazwisko(nullable:true)
        pozyskujacyNumer(nullable:true)
        pozyskujacyTytul(nullable:true)
        reprezentant1Imie(nullable:true)
        reprezentant1Nazwisko(nullable:true)
        reprezentant1Tytul(nullable:true)
        reprezentant2Imie(nullable:true)
        reprezentant2Nazwisko(nullable:true)
        reprezentant2Tytul(nullable:true)
        rodzajZezwolenia(nullable:true)
        scoringAkceptacja(nullable:true)
        scoringCharakterystyka(nullable:true)
        scoringCharakterystykaInna(nullable:true)
        scoringCzestoscTransakcji(nullable:true)
        scoringDeklaracjaFinansowa(nullable:true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable:true)
        scoringDeklaracjaFinansowaObrotOgolem(nullable:true)
        scoringDeklaracjaFinansowaSredniObrot(nullable:true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable:true)
        scoringDochodowosc(nullable:true)
        scoringDzialalnosc(nullable:true)
        scoringDzialalnoscCzas(nullable:true)
        scoringIloscTransakcji(nullable:true)
        scoringKoncesja(nullable:true)
        scoringLokalizacjaPunktu(nullable:true)
        scoringMcc(nullable:true)
        scoringMonitoring(nullable:true)
        scoringOtwartyZamkniety(nullable:true)
        scoringRuchTurystycznyPrzygraniczny(nullable:true)
        scoringSprzedazTowarowEkskluzywnych(nullable:true)
        scoringStanZadbany(nullable:true)
        scoringSzczegolyDzialalnosci(nullable:true)
        scoringTypPunktu(nullable:true)
        scoringWielkoscMiejscowosci(nullable:true)
        scoringWielkoscPunktu(nullable:true)
        scoringWlasnosc(nullable:true)
        tytulPlatnosciCena(nullable:true)
        umowaCzas(nullable:true)
        umowaOznDo(nullable:true)
        umowaOznOd(nullable:true)
        wydrukGrafikiCena(nullable:true)
    }

    def isFromCbd(def property){
        def cbdName = property+"Cbd"
        if (this.metaClass.hasProperty(this, cbdName) && this."$cbdName"?.trim()){
            true
        } else {
            false
        }
    }

	ProcessCommand() {
	}

    /*def initialize(Process process){

        this.process = process
        this.nip = process.client.nip

        *//* process.data.each { ProcessData field ->
             properties.hasProperty(field.name){
                 properties[field.name] = field.value;
             }
         }*//*

        // pobranie danych dla wybranych paneli

        process.panels.each { panel ->

            def functionName = "get" + WordUtils.capitalize(panel.name);
            println("functionName:"+functionName)
         //   cbdService."$functionName"()
        }

        int i = 1
        //def adresDoKorespondencji = cbdService.getAdresDoKorespondencji(process.client.nip);
    }*/
}
