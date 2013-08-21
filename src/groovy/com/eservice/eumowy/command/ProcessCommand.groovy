package com.eservice.eumowy.command
import com.eservice.eumowy.Panel
import com.eservice.eumowy.Process
/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:22
 *
 */
class ProcessCommand implements Serializable{

    def transient cbdService

//    adresacjaSeciowa
//    adresDoKorespondencjiPunktu
//    adresDoKorespondencjizAkecptantem

//    aneksDoUmowyNajmuZestawuPos - FINISH
    Date dataPodpisaniaAneksuPOZ

//    aneksDoUmowyPrepaid
//    czasObowiazywaniaUmowy
//    daneAkceptanta
//    daneDoWydruku
//    danePunktu
//    dcc
//    dccZakresUruchomienia

//    deklaracjeAkceptanta - FINISH
    String informacjaHandlowa
    //TODO - w pdf wykorzystujemy informacjaHandlowaTak i informacjaHandlowaNie (checkboxy)



//    dodajPunkt
//    dodatkoweUslugi - FINISH
    Double oplataZaDzienneZestawienieTransakcji
    Double oplataZaMiesieczneZestawienieTransakcji
    Double oplataZaPotwierdzenieWykonaniaPrzelewu
    Double oplataZaDostarczeniePapieru
    Double oplataZaZmianeGrafiki
    Double oplataZaInstalacjePOS
    Double oplataZaInstalacjeGPRS
    Double oplataZaUruchomienieWalutyObcej

//    dodatkoweUslugi2

//    dodatkoweUslugiMud - FINISH
    Double mudCena

//    dodatkoweUslugiUTAIntegracja
//    dodatkoweWyposazenie
//    formaDoladowania - FINISH
    Boolean doladowania_tp
    Boolean doladowania_tk
    Double srednia_sprzedaz_doladowan
    String srednia_sprzedaz_doladowan_slownie

//    funkcjeTerminala
//    ifplus
//    informacjeDodatkowe
//    informacjeTechniczne
//    okresLojalnosciowy
//    opieka
//    oplatyDCC
//    osobaDoKontaktu
//    osobaDoKontaktuWPunkcie
//    osobaKtoraPozyskalaAkceptanta

//    osobaUprawnionaDoPodpisaniaUmowy - FINISH
    String reprezentant1Tytul
    String reprezentant1Imie
    String reprezentant1Nazwisko
    String reprezentant2Tytul
    String reprezentant2Imie
    String reprezentant2Nazwisko
    //TODO - w pdf wykorzystujemy reprezentant1 i reprezentant2 przechowujace imie i nazwisko

//    poziomOplatiWarunkiPlatnosciKarty
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
    Double oplataZaOprogramowanieDoDoladowan

//    promocyjneObnizenieOplatyZaZestawPos
//    scoring
//    TODO - co wpisac w scoringMidFirmy i scoringNrUmowy????


    String scoringDzialalnosc
//    TODO - w pdf uzywamy pol: handel, uslugi (checkboxy)
//    TODO - co to sa za pola: MCC, szczegolowyRodzajDzialalnosciWPraktyce ??

    String scoringWlasnosc
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

    List<String> scoringWazneDane
//    TODO - w pdf sprzedazTowarowEkskluzywnych, ponad50ProcentObrotowWNocy, ruchTurystycznyPrzygraniczny, uslugiPlatneZGory

    String scoringCzestoscTransakcji
//    TODO - w pdf kilkaRazyWMiesiacu, kilkaRazyWTygodniu, coDrugiDzien, codziennie

    String scoringIloscTransakcji
//    TODO - w pdf od0do4, od5do10, powyzej10

    String scoringDochodowosc
    String scoringDeklaracjaFinansowa
//    TODO - w pdf wartosciWlasciwe, wartosciDeklarowane

    Double scoringDeklaracjaFinansowaObrotOgolem
    Double scoringDeklaracjaFinansowaObrotNaKarty
    Double scoringDeklaracjaFinansowaSredniObrot
    Double scoringDeklaracjaFinansowaSredniaTransakcja



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

//    uwagi
//    wyborDzialania
//    wykazPunktowAkceptujacychKartyPlatnicze
//    zestawPos
//    zestawPosOdplatneUzywanie
//    zestawPosStawkiPreferencyjne

//    serwis - FINISH
    String obslugaTyp
    Double obslugaEkonomicznyCena
    //TODO - w pdf wykorzystujemy obsugaPrestiz, obslugaKomfort, obslugaEkonomiczny (checkboxy)

//    rachunekBankowyKlienta
//    oplataDCCZaUruchomienie
//    liczbaMiesiecyZwolnieniaZNajmu

    String nip

    transient Process process

    List<PointCommand> points = [] //  points[n].someProperty

    def initialize(Process process){
        this.process = process
        this.nip = process.client.nip

        // pobranie danych dla wybranych paneli
        for (Panel panel : process.panels){

        }
        int i = 1
        //def adresDoKorespondencji = cbdService.getAdresDoKorespondencji(process.client.nip);
    }



}
