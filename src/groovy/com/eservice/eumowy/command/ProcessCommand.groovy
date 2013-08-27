package com.eservice.eumowy.command

import grails.validation.Validateable

import com.eservice.eumowy.Process

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:22
 *
 */

@Validateable
class ProcessCommand implements Serializable{

//    adresacjaSeciowa
//    adresDoKorespondencjiPunktu
//    adresDoKorespondencjizAkecptantem - FINISH
    String korespondencjaUlicaTytul
    String korespondencjaUlica
    String korespondencjaNrDomu
    String korespondencjaNrMieszkania

    String korespondencjaMiasto
    String korespondencjaKodPocztowy
    String korespondencjaPoczta

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

//    daneDoWydruku
    String wydrukNazwaPunktu
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

//    dodatkoweWyposazenie
//    formaDoladowania - FINISH
    String doladowania_tp
    String doladowania_tk
    String srednia_sprzedaz_doladowan
    String srednia_sprzedaz_doladowan_slownie

//    funkcjeTerminala
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

//    informacjeTechniczne

//    okresLojalnosciowy
    String okresLojalnosciowy  //  TODO - czy to jest dobrze????

//    opieka
//    oplatyDCC
    String oplataZaPlatnoscWInnejWalucie
//    osobaDoKontaktu
    String kontaktTytul
    String kontaktImie
    String kontaktNazwisko
    String kontaktTelStacjonarny
    String kontaktTelKomorkowy
    String kontaktEmail

//    osobaDoKontaktuWPunkcie

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
    String card_p_1_1
    String card_f_1_1
    String card_p_1_2
    String card_f_1_2
    String card_p_1_3
    String card_f_1_3

    String card_p_2_1
    String card_f_2_1
    String card_p_2_2
    String card_f_2_2
    String card_p_2_3
    String card_f_2_3

    String card_p_3_1_1
    String card_f_3_1_1
    String card_p_3_1_2
    String card_f_3_1_2
    String card_p_3_2_1
    String card_f_3_2_1
    String card_p_3_2_2
    String card_f_3_2_2
    String card_p_3_3
    String card_f_3_3

    String card_p_4_1
    String card_f_4_1
    String card_p_4_2
    String card_f_4_2
    String card_p_4_3
    String card_f_4_3
    String card_p_4_4
    String card_f_4_4

    String card_p_5_1
    String card_f_5_1
    String card_p_5_2
    String card_f_5_2
    String card_p_5_3
    String card_f_5_3
    String card_p_5_4
    String card_f_5_4

    String card_p_6_1_1
    String card_f_6_1_1
    String card_p_6_1_2
    String card_f_6_1_2
    String card_p_6_1_3
    String card_f_6_1_3
    String card_p_6_2_1
    String card_f_6_2_1
    String card_p_6_2_2
    String card_f_6_2_2
    String card_p_6_2_3
    String card_f_6_2_3
    String card_p_6_4_1
    String card_f_6_4_1
    String card_p_6_4_2
    String card_f_6_4_2
    String card_p_6_4_3
    String card_f_6_4_3

    String card_p_7_1_1
    String card_f_7_1_1
    String card_p_7_1_2
    String card_f_7_1_2
    String card_p_7_2_1
    String card_f_7_2_1
    String card_p_7_2_2
    String card_f_7_2_2
    String card_p_7_3
    String card_f_7_3

    String card_p_8_1_1
    String card_f_8_1_1
    String card_p_8_1_2
    String card_f_8_1_2
    String card_p_8_1_3
    String card_f_8_1_3
    String card_p_8_2_1
    String card_f_8_2_1
    String card_p_8_2_2
    String card_f_8_2_2
    String card_p_8_2_3
    String card_f_8_2_3
    String card_p_8_3
    String card_f_8_3
    String card_p_8_4_1
    String card_f_8_4_1
    String card_p_8_4_2
    String card_f_8_4_2
    String card_p_8_4_3
    String card_f_8_4_3
    String card_p_9
    String card_p_10

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


    String scoringDzialalnosc
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
    //TODO - w pdf wykorzystujemy pole akceptantSiedziba, w ktore wsadzamy to co potrzeba.

//    umowa2 - FINISH
    Date dataUmowy
    String miejsceUmowy //nie uzywane w pdf

//    wyborDzialania
//    wykazPunktowAkceptujacychKartyPlatnicze
    //TODO - to zmiany
    List punktyTytulPlatnosci
    //TODO - to zmiany
    List punktySystemKasowy
    //TODO - to zmiany
    List punktyUta
    //TODO - to zmiany
    List punktyWybrane

//    zestawPos
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


//    zestawPosStawkiPreferencyjne

//    serwis - FINISH
    String obslugaTyp
    String obslugaEkonomicznyCena
    //TODO - w pdf wykorzystujemy obsugaPrestiz, obslugaKomfort, obslugaEkonomiczny (checkboxy)

//    rachunekBankowyKlienta
    String numerRachunkuBankowego
    String bank

//    oplataDCCZaUruchomienie
    //TODO - to w pdfach sie nazywa inaczej - nie wiem jak...
    String oplataZaUruchomienieDCC

//    liczbaMiesiecyZwolnieniaZNajmu

    String nip

//    uwagi
    String notes

    transient Process process

    List<PointCommand> points = [] //  points[n].someProperty
	List<AllPointsCommand> allPoints = []
	List<AllPosCommand> allPoses = []
	
    static constraints = {
        notes()
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
