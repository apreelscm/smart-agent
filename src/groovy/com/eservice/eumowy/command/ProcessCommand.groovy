package com.eservice.eumowy.command

import com.eservice.eumowy.Process
import grails.validation.Validateable

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
    Date dataAneksowanejUmowyPos

//    aneksDoUmowyPrepaid - FINISH (ale trzeba zobaczyc w dokumentach czy jest dobrze wpisany)
    Date dataAneksowanejUmowyPrepaid

//    czasObowiazywaniaUmowy - FINISH
    String umowaCzas //TODO -  MOZLIWE, ze pdfach jest to umowaOzn, umowaNieOzn
    Date umowaOznOd
    Date umowaOznDo



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
    BigDecimal oplataVISA
    BigDecimal oplataVISAPr
    BigDecimal oplataMasterCard
    BigDecimal oplataMasterCardPr
    BigDecimal oplataMaestro
    BigDecimal oplataMasteroPr

//    dccZakresUruchomienia
    String dccZakresUruchomienia

//    deklaracjeAkceptanta - FINISH
    String informacjaHandlowa
    //TODO - w pdf wykorzystujemy informacjaHandlowaTak i informacjaHandlowaNie (checkboxy)


//    dodajPunkt
//    dodatkoweUslugi - FINISH
    BigDecimal oplataZaDzienneZestawienieTransakcji
    BigDecimal oplataZaMiesieczneZestawienieTransakcji
    BigDecimal oplataZaPotwierdzenieWykonaniaPrzelewu
    BigDecimal oplataZaDostarczeniePapieru
    BigDecimal oplataZaZmianeGrafiki
    BigDecimal oplataZaInstalacjePOS
    BigDecimal oplataZaInstalacjeGPRS
    BigDecimal oplataZaUruchomienieWalutyObcej

//    dodatkoweUslugi2 - FINISH (ale trzeba jeszcze daty startu pobrac)
    BigDecimal wydrukGrafikiCena
    BigDecimal dzialaniaMatematyczneCena
    BigDecimal tytulPlatnosciCena
    BigDecimal pierwszaSesjaCena

//    dodatkoweUslugiMud - FINISH
    BigDecimal mudCena

//    dodatkoweUslugiUTAIntegracja - FINISH (ale trzeba jeszcze daty startu pobrac)
    BigDecimal weryfikacjaPINCena
    BigDecimal systemKasowyCena

//    dodatkoweWyposazenie
//    formaDoladowania - FINISH
    Boolean doladowania_tp
    Boolean doladowania_tk
    Double srednia_sprzedaz_doladowan
    String srednia_sprzedaz_doladowan_slownie

//    funkcjeTerminala
//    ifplus - FINISH (ale zmiany w dokumentach)
    BigDecimal ifOplataVISA
    BigDecimal ifOplataMasterCard
    BigDecimal ifOplataDinersClub
    BigDecimal ifOplataIKO
    BigDecimal ifOplataPKOPB

//    informacjeDodatkowe - FINISH
    String dzialalnoscForma
    String dzialalnoscFormaInna
    String dzialalnoscDokument
    String dzialalnoscDokumentInny

//    informacjeTechniczne

//    okresLojalnosciowy
    Integer okresLojalnosciowy  //  TODO - czy to jest dobrze????

//    opieka
//    oplatyDCC
    BigDecimal oplataZaPlatnoscWInnejWalucie
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
    Double card_p_1_1
    BigDecimal card_f_1_1
    Double card_p_1_2
    BigDecimal card_f_1_2
    Double card_p_1_3
    BigDecimal card_f_1_3

    Double card_p_2_1
    BigDecimal card_f_2_1
    Double card_p_2_2
    BigDecimal card_f_2_2
    Double card_p_2_3
    BigDecimal card_f_2_3

    Double card_p_3_1_1
    BigDecimal card_f_3_1_1
    Double card_p_3_1_2
    BigDecimal card_f_3_1_2
    Double card_p_3_2_1
    BigDecimal card_f_3_2_1
    Double card_p_3_2_2
    BigDecimal card_f_3_2_2
    Double card_p_3_3
    BigDecimal card_f_3_3

    Double card_p_4_1
    BigDecimal card_f_4_1
    Double card_p_4_2
    BigDecimal card_f_4_2
    Double card_p_4_3
    BigDecimal card_f_4_3
    Double card_p_4_4
    BigDecimal card_f_4_4

    Double card_p_5_1
    BigDecimal card_f_5_1
    Double card_p_5_2
    BigDecimal card_f_5_2
    Double card_p_5_3
    BigDecimal card_f_5_3
    Double card_p_5_4
    BigDecimal card_f_5_4

    Double card_p_6_1_1
    BigDecimal card_f_6_1_1
    Double card_p_6_1_2
    BigDecimal card_f_6_1_2
    Double card_p_6_1_3
    BigDecimal card_f_6_1_3
    Double card_p_6_2_1
    BigDecimal card_f_6_2_1
    Double card_p_6_2_2
    BigDecimal card_f_6_2_2
    Double card_p_6_2_3
    BigDecimal card_f_6_2_3
    Double card_p_6_4_1
    BigDecimal card_f_6_4_1
    Double card_p_6_4_2
    BigDecimal card_f_6_4_2
    Double card_p_6_4_3
    BigDecimal card_f_6_4_3

    Double card_p_7_1_1
    BigDecimal card_f_7_1_1
    Double card_p_7_1_2
    BigDecimal card_f_7_1_2
    Double card_p_7_2_1
    BigDecimal card_f_7_2_1
    Double card_p_7_2_2
    BigDecimal card_f_7_2_2
    Double card_p_7_3
    BigDecimal card_f_7_3

    Double card_p_8_1_1
    BigDecimal card_f_8_1_1
    Double card_p_8_1_2
    BigDecimal card_f_8_1_2
    Double card_p_8_1_3
    BigDecimal card_f_8_1_3
    Double card_p_8_2_1
    BigDecimal card_f_8_2_1
    Double card_p_8_2_2
    BigDecimal card_f_8_2_2
    Double card_p_8_2_3
    BigDecimal card_f_8_2_3
    Double card_p_8_3
    BigDecimal card_f_8_3
    Double card_p_8_4_1
    BigDecimal card_f_8_4_1
    Double card_p_8_4_2
    BigDecimal card_f_8_4_2
    Double card_p_8_4_3
    BigDecimal card_f_8_4_3
    Double card_p_9
    Double card_p_10

//    poziomOplatIWarunkiPlatnosciPP - FINISH
    Double pp_orange_tk
    Double pp_orange_tp
    Double pp_plus_tk
    Double pp_plus_tp
    Double pp_tmobile_tk
    Double pp_tmobile_tp
    Double pp_heyah_tk
    Double pp_heyah_tp
    Double pp_play_tk
    Double pp_play_tp
    Double pp_telegrosik_tk
    Double pp_virginmobile_tk
    Double pp_lycamobile_tk
    Double pp_gtmobile_tk
    Double pp_vectonemobile_tk
    Double pp_delightmobile_tk
    BigDecimal oplataZaOprogramowanieDoDoladowan

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

    Boolean scoringStanZadbany
//    TODO - w pdf stanZadbany

    Boolean scoringSprzedazTowarowEkskluzywnych
    Boolean scoringPonad50ProcentObrotowWNocy
    Boolean scoringRuchTurystycznyPrzygraniczny
    Boolean scoringUslugiPlatneZGory
//    TODO - w pdf sprzedazTowarowEkskluzywnych, ponad50ProcentObrotowWNocy, ruchTurystycznyPrzygraniczny, uslugiPlatneZGory

    String scoringCzestoscTransakcji
//    TODO - w pdf kilkaRazyWMiesiacu, kilkaRazyWTygodniu, coDrugiDzien, codziennie

    String scoringIloscTransakcji
//    TODO - w pdf od0do4, od5do10, powyzej10

    BigDecimal scoringDochodowosc
    String scoringDeklaracjaFinansowa
//    TODO - w pdf wartosciWlasciwe, wartosciDeklarowane

    BigDecimal scoringDeklaracjaFinansowaObrotOgolem
    BigDecimal scoringDeklaracjaFinansowaObrotNaKarty
    BigDecimal scoringDeklaracjaFinansowaSredniObrot
    BigDecimal scoringDeklaracjaFinansowaSredniaTransakcja

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
//    zestawPos
//    zestawPosOdplatneUzywanie
    String oplPOSDialUpTyp
    Integer oplPOSDialUpIlosc
    Integer oplPOSDialUpIloscPP
    BigDecimal oplPOSDialUpNormalneMies
    BigDecimal oplPOSDialUpNormalnePP
    BigDecimal oplPOSDialUpPreferencyjneMies
    BigDecimal oplPOSDialUpPreferencyjnePP

    String oplPOSVPNTyp
    Integer oplPOSVPNIlosc
    Integer oplPOSVPNIloscPP
    BigDecimal oplPOSVPNNormalneMies
    BigDecimal oplPOSVPNNormalnePP
    BigDecimal oplPOSVPNPreferencyjneMies
    BigDecimal oplPOSVPNPreferencyjnePP

    String oplPOSSSLTyp
    Integer oplPOSSSLIlosc
    Integer oplPOSSSLIloscPP
    BigDecimal oplPOSSSLNormalneMies
    BigDecimal oplPOSSSLNormalnePP
    BigDecimal oplPOSSSLPreferencyjneMies
    BigDecimal oplPOSSSLPreferencyjnePP

    String oplPOSWiFiTyp
    Integer oplPOSWiFiIlosc
    Integer oplPOSWiFiIloscPP
    BigDecimal oplPOSWiFiNormalneMies
    BigDecimal oplPOSWiFiNormalnePP
    BigDecimal oplPOSWiFiPreferencyjneMies
    BigDecimal oplPOSWiFiPreferencyjnePP

    String oplPOSGPRSTyp
    Integer oplPOSGPRSIlosc
    Integer oplPOSGPRSIloscPP
    BigDecimal oplPOSGPRSNormalneMies
    BigDecimal oplPOSGPRSNormalnePP
    BigDecimal oplPOSGPRSPreferencyjneMies
    BigDecimal oplPOSGPRSPreferencyjnePP

    Integer oplPOSBaza


//    zestawPosStawkiPreferencyjne

//    serwis - FINISH
    String obslugaTyp
    BigDecimal obslugaEkonomicznyCena
    //TODO - w pdf wykorzystujemy obsugaPrestiz, obslugaKomfort, obslugaEkonomiczny (checkboxy)

//    rachunekBankowyKlienta
    String numerRachunkuBankowego
    String bank

//    oplataDCCZaUruchomienie
    //TODO - to w pdfach sie nazywa inaczej - nie wiem jak...
    BigDecimal oplataZaUruchomienieDCC

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
		for(int i = 0; i < 10; i++) {
			points.add(new PointCommand())
		}
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
