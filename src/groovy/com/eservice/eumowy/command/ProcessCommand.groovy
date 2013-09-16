package com.eservice.eumowy.command
import com.eservice.eumowy.Process
import grails.validation.Validateable
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils
import org.springframework.validation.Errors
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
	List<PointCommand> poses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))
	List<AllPointsCommand> allPoints = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPointsCommand)) 
	List<AllPosCommand> allPoses = ListUtils.lazyList([], FactoryUtils.instantiateFactory(AllPosCommand))


    static constraints = {

        oplataZaDzienneZestawienieTransakcji(nullable:true, blank:false, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:true, blank:false, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:true, blank:false, shared: "number")
        oplataZaDostarczeniePapieru(nullable:true, blank:false, shared: "number")
        oplataZaZmianeGrafiki(nullable:true, blank:false, shared: "number")
        oplataZaInstalacjePOS(nullable:true, blank:false, shared: "number")
        oplataZaInstalacjeGPRS(nullable:true, blank:false, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:true, blank:false, shared: "number")
        wydrukGrafikiCena(nullable:true, blank:false, shared: "number")
        dzialaniaMatematyczneCena(nullable:true, blank:false, shared: "number")
        tytulPlatnosciCena(nullable:true, blank:false, shared: "number")
        pierwszaSesjaCena(nullable:true, blank:false, shared: "number")

        akceptantKontaktUlicaTytul(nullable:true, blank:false)
        akceptantKontaktUlica(nullable:true, blank:false)
        akceptantKontaktNrDomu(nullable:true, blank:false)
        akceptantKontaktNrMieszkania(nullable:true, blank:false)
        akceptantKontaktMiasto(nullable:true, blank:false)
        akceptantKontaktKodPocztowy(nullable:true, blank:false)
        akceptantKontaktPoczta(nullable:true, blank:false)
        dataAneksowanejUmowyPos(nullable:true, blank:false, shared: "date")
        dataAneksowanejUmowyPrepaid(nullable:true, blank:false, shared: "date")
        umowaCzas(nullable:true, blank:false)
        umowaOznOd(nullable:true, blank:true, shared: "date")
        umowaOznDo(nullable:true, blank:true, shared: "date")
        akceptantNazwaOficjalna(nullable:false, blank:false) //a1!, M
        akceptantNazwaSieciowa(nullable:true, blank:false) //a1!
        akceptantRegon(nullable:true,blank:false, matches:"[0-9]{9}") //111111111, M
        akceptantNazwaOficjalnaCbd(nullable:true)
        akceptantNazwaSieciowaCbd(nullable:true)
        akceptantRegonCbd(nullable:true)
        nazwaDoWydrukuZTerminalaPos(nullable:true)
        wydrukNazwaDoWyszukwarki(nullable:true)
        wydrukUlicaTytul(nullable:true, blank:false)
        wydrukUlica(nullable:true, blank:false)
        wydrukNrDomu(nullable:true, blank:false)
        wydrukNrMieszkania(nullable:true, blank:false)
        wydrukMiasto(nullable:true, blank:false)
        wydrukKodPocztowy(nullable:true, blank:false)
        wydrukPoczta(nullable:true, blank:false)
        wydrukLinia1(nullable:true, blank:true)
        wydrukLinia2(nullable:true, blank:true)
        oplataVISA(nullable:true, blank:false, shared: "number")
        oplataVISAPr(nullable:true, blank:false, shared: "number")
        oplataMasterCard(nullable:true, blank:false, shared: "number")
        oplataMasterCardPr(nullable:true, blank:false, shared: "number")
        oplataMaestro(nullable:true, blank:false, shared: "number")
        oplataMasteroPr(nullable:true, blank:false, shared: "number")
        dccZakresUruchomienia(nullable:true, blank:false)
        informacjaHandlowa(nullable:true, blank:false)
        oplataZaDzienneZestawienieTransakcji(nullable:true, blank:false, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:true, blank:false, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:true, blank:false, shared: "number")
        oplataZaDostarczeniePapieru(nullable:true, blank:false, shared: "number")
        oplataZaZmianeGrafiki(nullable:true, blank:false, shared: "number")
        oplataZaInstalacjePOS(nullable:true, blank:false, shared: "number")
        oplataZaInstalacjeGPRS(nullable:true, blank:false, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:true, blank:false, shared: "number")
        wydrukGrafikiCena(nullable:true, blank:false, shared: "number")
        dzialaniaMatematyczneCena(nullable:true, blank:false, shared: "number")
        tytulPlatnosciCena(nullable:true, blank:false, shared: "number")
        pierwszaSesjaCena(nullable:true, blank:false, shared: "number")
        mudCena(nullable:true, blank:false, shared:"number")
        weryfikacjaPINCena(nullable:true)
        systemKasowyCena(nullable:true)
//        doladowania_tp(nullable:true)
//        doladowania_tk(nullable:true)
		doladowania_tp(nullable:true, validator: { value, process, errors ->
			if (value == null || value.isEmpty()) {
				if (process.doladowania_tk == null || process.doladowania_tk.isEmpty())
				errors.rejectValue( "doladowania_tp", "default.atLeastOne.option", "Nale\u017Cy zaznaczyc\u0107 przynajmniej jedn\u0105 opcj\u0119")
				errors.rejectValue( "doladowania_tk", "default.atLeastOne.option", "Nale\u017Cy zaznaczyc\u0107 przynajmniej jedn\u0105 opcj\u0119")
				return false
			}
			 
			return true
		})
        srednia_sprzedaz_doladowan(nullable:true, blank:false, shared:"number")
        srednia_sprzedaz_doladowan_slownie(nullable:true, blank:false)
        ifOplataVISA(nullable:true,blank:false, shared: "number") //1.11 %, M
        ifOplataMasterCard(nullable:true, blank:false, shared: "number") //1.11 %, M
        ifOplataDinersClub(nullable:true, blank:false, shared: "number") //1.11 %, M
        ifOplataIKO(nullable:true,blank:false, shared: "number") //1.11 %, M
        ifOplataPKOPB(nullable:true,blank:false, shared: "number") //1.11 %, M
        dzialalnoscForma(nullable:true, blank:false)
        dzialalnoscFormaInna(nullable:true, blank:true)
        dzialalnoscDokument(nullable:true, blank:false)
        dzialalnoscDokumentInny(nullable:true, blank:true)
        okresLojalnosciowy(nullable:true, blank:false)
        oplataZaPlatnoscWInnejWalucie(nullable:true, blank:false)
        kontaktTytul(nullable:true, blank:false)
        kontaktImie(nullable:true, blank:false)
        kontaktNazwisko(nullable:true, blank:false)
//        kontaktTelStacjonarny(nullable:true)
//        kontaktTelKomorkowy(nullable:true)
		kontaktTelStacjonarny(nullable:true, validator: { value, process, errors ->
			if (value == null || value.isEmpty()) {
				if (process.kontaktTelKomorkowy == null || process.kontaktTelKomorkowy.isEmpty())
				errors.rejectValue( "kontaktTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
				errors.rejectValue( "kontaktTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
				return false
			}
			 
			return true
		})
        kontaktEmail(nullable:true, blank:true)
        pozyskujacyTytul(nullable:true, blank:false)
        pozyskujacyImie(nullable:true, blank:false)
        pozyskujacyNazwisko(nullable:true, blank:false)
        pozyskujacyNumer(nullable:true, blank:false)
        reprezentant1Tytul(nullable:true, blank:false)
        reprezentant1Imie(nullable:true, blank:false)
        reprezentant1Nazwisko(nullable:true, blank:false)
        reprezentant2Tytul(nullable:true, blank:false)
        reprezentant2Imie(nullable:true, blank:true)
        reprezentant2Nazwisko(nullable:true, blank:true)
        visaEUKKOPr(nullable:true,blank:false, shared: "percentage")
        visaEUKDPr(nullable:true,blank:false, shared: "percentage")
        visaEUKBPr(nullable:true,blank:false, shared: "percentage")
        visaOutEUKKOPr(nullable:true,blank:false, shared: "percentage")
        visaOutEUKDPr(nullable:true,blank:false, shared: "percentage")
        visaOutEUKBPr(nullable:true,blank:false, shared: "percentage")
        visaPolskaKKO1Pr(nullable:true,blank:false, shared: "percentage")
        visaPolskaKKO2Pr(nullable:true,blank:false, shared: "percentage")
        visaPolskaKD1Pr(nullable:true,blank:false, shared: "percentage")
        visaPolskaKD2Pr(nullable:true,blank:false, shared: "percentage")
        visaPolskaKBPr(nullable:true,blank:false, shared: "percentage")
        mastercardEUKKPr(nullable:true,blank:false, shared: "percentage")
        mastercardEUKDPr(nullable:true,blank:false, shared: "percentage")
        mastercardEUKBLPr(nullable:true,blank:false, shared: "percentage")
        mastercardEUMPr(nullable:true,blank:false, shared: "percentage")
        mastercardOutEUKKPr(nullable:true,blank:false, shared: "percentage")
        mastercardOutEUKDPr(nullable:true,blank:false, shared: "percentage")
        mastercardOutEUKBPr(nullable:true,blank:false, shared: "percentage")
        mastercardOutEUMPr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKK1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKK2Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKK3Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKD1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKD2Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKD3Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaKBPr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaM1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaM2Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPolskaM3Pr(nullable:true,blank:false, shared: "percentage")
        visaPKOBPKKO1Pr(nullable:true,blank:false, shared: "percentage")
        visaPKOBPKKO2Pr(nullable:true,blank:false, shared: "percentage")
        visaPKOBPKD1Pr(nullable:true,blank:false, shared: "percentage")
        visaPKOBPKD2Pr(nullable:true,blank:false, shared: "percentage")
        visaPKOBPKB3Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKK1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKK2Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKK3Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKD1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKD2LPr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKD3Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPKBPr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPM1Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPM2Pr(nullable:true,blank:false, shared: "percentage")
        mastercardPKOBPM3Pr(nullable:true,blank:false, shared: "percentage")
        dinersClubPr(nullable:true,blank:false, shared: "percentage")
        ikoPr(nullable:true, blank:true,  shared: "percentage")
        visaEUKKOSt(nullable:true,blank:false, shared: "number")
        visaEUKDSt(nullable:true,blank:false, shared: "number")
        visaEUKBSt(nullable:true,blank:false, shared: "number")
        visaOutEUKKOSt(nullable:true,blank:false, shared: "number")
        visaOutEUKDSt(nullable:true,blank:false, shared: "number")
        visaOutEUKBSt(nullable:true,blank:false, shared: "number")
        visaPolskaKKO1St(nullable:true,blank:false, shared: "number")
        visaPolskaKKO2St(nullable:true,blank:false, shared: "number")
        visaPolskaKD1St(nullable:true,blank:false, shared: "number")
        visaPolskaKD2St(nullable:true,blank:false, shared: "number")
        visaPolskaKBSt(nullable:true,blank:false, shared: "number")
        mastercardEUKKSt(nullable:true,blank:false, shared: "number")
        mastercardEUKDSt(nullable:true,blank:false, shared: "number")
        mastercardEUKBLSt(nullable:true,blank:false, shared: "number")
        mastercardEUMSt(nullable:true,blank:false, shared: "number")
        mastercardOutEUKKSt(nullable:true,blank:false, shared: "number")
        mastercardOutEUKDSt(nullable:true,blank:false, shared: "number")
        mastercardOutEUKBSt(nullable:true,blank:false, shared: "number")
        mastercardOutEUMSt(nullable:true,blank:false, shared: "number")
        mastercardPolskaKK1St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKK2St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKK3St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKD1St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKD2St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKD3St(nullable:true,blank:false, shared: "number")
        mastercardPolskaKBSt(nullable:true,blank:false, shared: "number")
        mastercardPolskaM1St(nullable:true,blank:false, shared: "number")
        mastercardPolskaM2St(nullable:true,blank:false, shared: "number")
        mastercardPolskaM3St(nullable:true,blank:false, shared: "number")
        visaPKOBPKKO1St(nullable:true,blank:false, shared: "number")
        visaPKOBPKKO2St(nullable:true,blank:false, shared: "number")
        visaPKOBPKD1St(nullable:true,blank:false, shared: "number")
        visaPKOBPKD2St(nullable:true,blank:false, shared: "number")
        visaPKOBPKB3St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKK1St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKK2St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKK3St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKD1St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKD2LSt(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKD3St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPKBSt(nullable:true,blank:false, shared: "number")
        mastercardPKOBPM1St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPM2St(nullable:true,blank:false, shared: "number")
        mastercardPKOBPM3St(nullable:true,blank:false, shared: "number")
        dinersClubSt(nullable:true,blank:true,shared: "number")
        ikoSt(nullable:true, blank:true,shared: "number")
        pp_orange_tk(nullable:true,blank:false, shared: "percentage")
        pp_orange_tp(nullable:true,blank:false, shared: "percentage")
        pp_plus_tk(nullable:true,blank:false, shared: "percentage")
        pp_plus_tp(nullable:true,blank:false, shared: "percentage")
        pp_tmobile_tk(nullable:true,blank:false, shared: "percentage")
        pp_tmobile_tp(nullable:true,blank:false, shared: "percentage")
        pp_heyah_tk(nullable:true,blank:false, shared: "percentage")
        pp_heyah_tp(nullable:true,blank:false, shared: "percentage")
        pp_play_tk(nullable:true,blank:false, shared: "percentage")
        pp_play_tp(nullable:true,blank:false, shared: "percentage")
        pp_telegrosik_tk(nullable:true,blank:false, shared: "percentage")
        pp_virginmobile_tk(nullable:true,blank:false, shared: "percentage")
        pp_lycamobile_tk(nullable:true,blank:false, shared: "percentage")
        pp_gtmobile_tk(nullable:true,blank:false, shared: "percentage")
        pp_vectonemobile_tk(nullable:true,blank:false, shared: "percentage")
        pp_delightmobile_tk(nullable:true,blank:false, shared: "percentage")
        oplataZaOprogramowanieDoDoladowan(nullable:true, blank:false, shared: "number" )
        scoringMcc(nullable:true, blank:false, matches:"[0-9]{4}")
        scoringDzialalnosc(nullable:true, blank:false)
        scoringSzczegolyDzialalnosci(nullable:true, blank:true)
        scoringWlasnosc(nullable:true, blank:false)
        scoringDzialalnoscCzas(nullable:true, blank:false)
        scoringKoncesja(nullable:true, blank:false)
        rodzajZezwolenia(nullable:true, blank:true)
        scoringCharakterystyka(nullable:true, blank:false)
        scoringCharakterystykaInna(nullable:true, blank:true)
        scoringWielkoscPunktu(nullable:true, blank:false)
        scoringAkceptacja(nullable:true, blank:false)
        scoringMonitoring(nullable:true, blank:false)
        scoringLokalizacjaPunktu(nullable:true, blank:false)
        scoringTypPunktu(nullable:true, blank:false)
        scoringTypPunktuInny(nullable:true, blank:true)
        scoringWielkoscMiejscowosci(nullable:true, blank:false)
        scoringOtwartyZamkniety(nullable:true, blank:false)
        scoringStanZadbany(nullable:true)
        scoringSprzedazTowarowEkskluzywnych(nullable:true, blank:false)
        scoringPonad50ProcentObrotowWNocy(nullable:true, blank:false)
        scoringRuchTurystycznyPrzygraniczny(nullable:true, blank:false)
        scoringUslugiPlatneZGory(nullable:true, blank:false)
        scoringCzestoscTransakcji(nullable:true, blank:false)
        scoringIloscTransakcji(nullable:true, blank:false)
        scoringDochodowosc(nullable:true, blank:true)
        scoringDeklaracjaFinansowa(nullable:true, blank:false)
        scoringDeklaracjaFinansowaObrotOgolem(nullable:true, blank:true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniObrot(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable:true, blank:true)
        akceptantUlicaTytul(nullable:true, blank:false)
        akceptantUlica(nullable:true, blank:false)
        akceptantNrDomu(nullable:true, blank:false)
        akceptantNrMieszkania(nullable:true, blank:false)
        akceptantMiasto(nullable:true, blank:false)
        akceptantKodPocztowy(nullable:true, blank:false)
        akceptantPoczta(nullable:true, blank:false)
//        akceptantTelStacjonarny(nullable:true)
		akceptantTelStacjonarny(nullable:true, validator: { value, process, errors ->
		    if (value == null || value.isEmpty()) {
				if (process.akceptantTelKomorkowy == null || process.akceptantTelKomorkowy.isEmpty())
		        errors.rejectValue( "akceptantTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
				errors.rejectValue( "akceptantTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
		        return false
		    }
		     
		    return true
		})
        akceptantFax(nullable:true, blank:true)
//        akceptantTelKomorkowy(nullable:true)
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
        oplPOSDialUpIlosc(nullable:true)
        oplPOSDialUpIloscPP(nullable:true)
        oplPOSDialUpNormalneMies(nullable:true)
        oplPOSDialUpNormalnePP(nullable:true)
        oplPOSDialUpPreferencyjneMies(nullable:true)
        oplPOSDialUpPreferencyjnePP(nullable:true)
        oplPOSVPNTyp(nullable:true)
        oplPOSVPNIlosc(nullable:true)
        oplPOSVPNIloscPP(nullable:true)
        oplPOSVPNNormalneMies(nullable:true)
        oplPOSVPNNormalnePP(nullable:true)
        oplPOSVPNPreferencyjneMies(nullable:true)
        oplPOSVPNPreferencyjnePP(nullable:true)
        oplPOSSSLTyp(nullable:true)
        oplPOSSSLIlosc(nullable:true)
        oplPOSSSLIloscPP(nullable:true)
        oplPOSSSLNormalneMies(nullable:true)
        oplPOSSSLNormalnePP(nullable:true)
        oplPOSSSLPreferencyjneMies(nullable:true)
        oplPOSSSLPreferencyjnePP(nullable:true)
        oplPOSWiFiTyp(nullable:true)
        oplPOSWiFiIlosc(nullable:true)
        oplPOSWiFiIloscPP(nullable:true)
        oplPOSWiFiNormalneMies(nullable:true)
        oplPOSWiFiNormalnePP(nullable:true)
        oplPOSWiFiPreferencyjneMies(nullable:true)
        oplPOSWiFiPreferencyjnePP(nullable:true)
        oplPOSGPRSTyp(nullable:true)
        oplPOSGPRSIlosc(nullable:true)
        oplPOSGPRSIloscPP(nullable:true)
        oplPOSGPRSNormalneMies(nullable:true)
        oplPOSGPRSNormalnePP(nullable:true)
        oplPOSGPRSPreferencyjneMies(nullable:true)
        oplPOSGPRSPreferencyjnePP(nullable:true)
        oplPOSBaza(nullable:true)
        obslugaTyp(nullable:true, blank:false)
        obslugaEkonomicznyCena(nullable:true, blank:false, shared: "number")
        numerRachunkuBankowegoKlienta(nullable:true, blank:false, matches: "[0-9]{26}")
        bankKlienta(nullable:true, blank:false)
        oplataZaUruchomienieDCC(nullable:true, blank:false, shared: "number")
        nip(nullable:true)
        notes(nullable:true) //a1!
        points(nullable:true)
        poses(nullable:true)
        allPoints(nullable:true)
        allPoses(nullable:true)
    }

    def isFromCbd(def property){
        def cbdName = property+"Cbd"
        return (this.metaClass.hasProperty(this, cbdName) && this."$cbdName"?.trim())
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
