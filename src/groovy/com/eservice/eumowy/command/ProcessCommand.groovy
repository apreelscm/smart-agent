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

    static def nullableTrueBlankFalse = {return it == null || it.toString().size() > 0}

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

//    aneksDoUmowyPrepaid - FINISH
    String dataAneksowanejUmowyPrepaid

//    czasObowiazywaniaUmowy - FINISH
    String umowaCzas
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
    Boolean doladowania_tp
    Boolean doladowania_tk
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
//    TODO - co wpisac w scoringNrUmowy????

    String scoringMcc
    String scoringDzialalnosc
    String scoringSzczegolyDzialalnosci
//    TODO - co to sa za pola: szczegolowyRodzajDzialalnosciWPraktyce ??

    String scoringWlasnosc
    String scoringDzialalnoscCzas
    String scoringKoncesja
    String rodzajZezwolenia
    String scoringCharakterystyka
    String scoringCharakterystykaInna
    String scoringWielkoscPunktu
    String scoringAkceptacja
    String scoringMonitoring
    String scoringLokalizacjaPunktu
    String scoringTypPunktu
    String scoringTypPunktuInny
    String scoringWielkoscMiejscowosci
    String scoringOtwartyZamkniety
    Boolean scoringStanZadbany
    Boolean scoringSprzedazTowarowEkskluzywnych
    Boolean scoringPonad50ProcentObrotowWNocy
    Boolean scoringRuchTurystycznyPrzygraniczny
    Boolean scoringUslugiPlatneZGory
    String scoringCzestoscTransakcji
    String scoringIloscTransakcji
    String scoringDochodowosc
    String scoringDeklaracjaFinansowa
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

        oplataZaDzienneZestawienieTransakcji(nullable:true, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:true, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:true, shared: "number")
        oplataZaDostarczeniePapieru(nullable:true, shared: "number")
        oplataZaZmianeGrafiki(nullable:true, shared: "number")
        oplataZaInstalacjePOS(nullable:true, shared: "number")
        oplataZaInstalacjeGPRS(nullable:true, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:true, shared: "number")
        wydrukGrafikiCena(nullable:true, shared: "number")
        dzialaniaMatematyczneCena(nullable:true, shared: "number")
        tytulPlatnosciCena(nullable:true, shared: "number")
        pierwszaSesjaCena(nullable:true, shared: "number")

        akceptantKontaktUlicaTytul(nullable:true)
        akceptantKontaktUlica(nullable:true)
        akceptantKontaktNrDomu(nullable:true)
        akceptantKontaktNrMieszkania(nullable:true)
        akceptantKontaktMiasto(nullable:true)
        akceptantKontaktKodPocztowy(nullable:true)
        akceptantKontaktPoczta(nullable:true)
        dataAneksowanejUmowyPos(nullable:true, shared: "date")
        dataAneksowanejUmowyPrepaid(nullable:true, shared: "date")
        umowaCzas(nullable:true)
        umowaOznOd(nullable:true, blank:true, shared: "date")
        umowaOznDo(nullable:true, blank:true, shared: "date")
        akceptantNazwaOficjalna(nullable:false, blank:false) //a1!, M
        akceptantNazwaSieciowa(nullable:true) //a1!
        akceptantRegon(nullable:true,blank:false, matches:"[0-9]{9}") //111111111, M
        akceptantNazwaOficjalnaCbd(nullable:true)
        akceptantNazwaSieciowaCbd(nullable:true)
        akceptantRegonCbd(nullable:true)
        nazwaDoWydrukuZTerminalaPos(nullable:true)
        wydrukNazwaDoWyszukwarki(nullable:true)
        wydrukUlicaTytul(nullable:true)
        wydrukUlica(nullable:true)
        wydrukNrDomu(nullable:true)
        wydrukNrMieszkania(nullable:true)
        wydrukMiasto(nullable:true)
        wydrukKodPocztowy(nullable:true)
        wydrukPoczta(nullable:true)
        wydrukLinia1(nullable:true, blank:true)
        wydrukLinia2(nullable:true, blank:true)
        oplataVISA(nullable:true, shared: "number")
        oplataVISAPr(nullable:true, shared: "percentage")
        oplataMasterCard(nullable:true, shared: "number")
        oplataMasterCardPr(nullable:true, shared: "percentage")
        oplataMaestro(nullable:true, shared: "number")
        oplataMasteroPr(nullable:true, shared: "percentage")
        dccZakresUruchomienia(nullable:true)
        informacjaHandlowa(nullable:true)
        oplataZaDzienneZestawienieTransakcji(nullable:true, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(nullable:true, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(nullable:true, shared: "number")
        oplataZaDostarczeniePapieru(nullable:true, shared: "number")
        oplataZaZmianeGrafiki(nullable:true, shared: "number")
        oplataZaInstalacjePOS(nullable:true, shared: "number")
        oplataZaInstalacjeGPRS(nullable:true, shared: "number")
        oplataZaUruchomienieWalutyObcej(nullable:true, shared: "number")
        wydrukGrafikiCena(nullable:true, shared: "number")
        dzialaniaMatematyczneCena(nullable:true, shared: "number")
        tytulPlatnosciCena(nullable:true, shared: "number")
        pierwszaSesjaCena(nullable:true, shared: "number")
        mudCena(nullable:true, shared:"number")
        weryfikacjaPINCena(nullable:true)
        systemKasowyCena(nullable:true)
        doladowania_tk(nullable:true)
		doladowania_tp(nullable:true, validator: { value, process, errors ->
			if (value == null && process.doladowania_tk == null){
                //panel nie jest wyswietlony
                return true
            }

            if (!(value || process.doladowania_tk)) {
                errors.rejectValue( "doladowania_tp", "default.atLeastOne.option", "Nale\u017Cy zaznaczy\u0107 przynajmniej jedn\u0105 opcj\u0119")
                errors.rejectValue( "doladowania_tk", "default.atLeastOne.option", "Nale\u017Cy zaznaczy\u0107 przynajmniej jedn\u0105 opcj\u0119")
                return false
			}
			return true
		})
        srednia_sprzedaz_doladowan(nullable:true, shared:"number")
        srednia_sprzedaz_doladowan_slownie(nullable:true)
        ifOplataVISA(nullable:true, shared: "percentage") //1.11 %, M
        ifOplataMasterCard(nullable:true, shared: "percentage") //1.11 %, M
        ifOplataDinersClub(nullable:true, shared: "percentage") //1.11 %, M
        ifOplataIKO(nullable:true, shared: "percentage") //1.11 %, M
        ifOplataPKOPB(nullable:true, shared: "percentage") //1.11 %, M
        dzialalnoscForma(nullable:true)
        dzialalnoscFormaInna(nullable:true, blank:true)
        dzialalnoscDokument(nullable:true)
        dzialalnoscDokumentInny(nullable:true, blank:true)
        okresLojalnosciowy(nullable:true)
        oplataZaPlatnoscWInnejWalucie(nullable:true)
        kontaktTytul(nullable:true)
        kontaktImie(nullable:true)
        kontaktNazwisko(nullable:true)
        kontaktTelKomorkowy(nullable:true)
		kontaktTelStacjonarny(nullable:true, validator: { value, process, errors ->
			if (value == null || process.kontaktTelKomorkowy == null) {
                return true;
            }

            if (value.isEmpty() && process.kontaktTelKomorkowy.isEmpty()){
                errors.rejectValue( "kontaktTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                errors.rejectValue( "kontaktTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                return false
            }
			return true
		})
        kontaktEmail(nullable:true, blank:true)
        pozyskujacyTytul(nullable:true)
        pozyskujacyImie(nullable:true)
        pozyskujacyNazwisko(nullable:true)
        pozyskujacyNumer(nullable:true)
        reprezentant1Tytul(nullable:true)
        reprezentant1Imie(nullable:true)
        reprezentant1Nazwisko(nullable:true)
        reprezentant2Tytul(nullable:true)
        reprezentant2Imie(nullable:true, blank:true)
        reprezentant2Nazwisko(nullable:true, blank:true)
        visaEUKKOPr(nullable:true, shared: "percentage")
        visaEUKDPr(nullable:true, shared: "percentage")
        visaEUKBPr(nullable:true, shared: "percentage")
        visaOutEUKKOPr(nullable:true, shared: "percentage")
        visaOutEUKDPr(nullable:true, shared: "percentage")
        visaOutEUKBPr(nullable:true, shared: "percentage")
        visaPolskaKKO1Pr(nullable:true, shared: "percentage")
        visaPolskaKKO2Pr(nullable:true, shared: "percentage")
        visaPolskaKD1Pr(nullable:true, shared: "percentage")
        visaPolskaKD2Pr(nullable:true, shared: "percentage")
        visaPolskaKBPr(nullable:true, shared: "percentage")
        mastercardEUKKPr(nullable:true, shared: "percentage")
        mastercardEUKDPr(nullable:true, shared: "percentage")
        mastercardEUKBLPr(nullable:true, shared: "percentage")
        mastercardEUMPr(nullable:true, shared: "percentage")
        mastercardOutEUKKPr(nullable:true, shared: "percentage")
        mastercardOutEUKDPr(nullable:true, shared: "percentage")
        mastercardOutEUKBPr(nullable:true, shared: "percentage")
        mastercardOutEUMPr(nullable:true, shared: "percentage")
        mastercardPolskaKK1Pr(nullable:true, shared: "percentage")
        mastercardPolskaKK2Pr(nullable:true, shared: "percentage")
        mastercardPolskaKK3Pr(nullable:true, shared: "percentage")
        mastercardPolskaKD1Pr(nullable:true, shared: "percentage")
        mastercardPolskaKD2Pr(nullable:true, shared: "percentage")
        mastercardPolskaKD3Pr(nullable:true, shared: "percentage")
        mastercardPolskaKBPr(nullable:true, shared: "percentage")
        mastercardPolskaM1Pr(nullable:true, shared: "percentage")
        mastercardPolskaM2Pr(nullable:true, shared: "percentage")
        mastercardPolskaM3Pr(nullable:true, shared: "percentage")
        visaPKOBPKKO1Pr(nullable:true, shared: "percentage")
        visaPKOBPKKO2Pr(nullable:true, shared: "percentage")
        visaPKOBPKD1Pr(nullable:true, shared: "percentage")
        visaPKOBPKD2Pr(nullable:true, shared: "percentage")
        visaPKOBPKB3Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKK1Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKK2Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKK3Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKD1Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKD2LPr(nullable:true, shared: "percentage")
        mastercardPKOBPKD3Pr(nullable:true, shared: "percentage")
        mastercardPKOBPKBPr(nullable:true, shared: "percentage")
        mastercardPKOBPM1Pr(nullable:true, shared: "percentage")
        mastercardPKOBPM2Pr(nullable:true, shared: "percentage")
        mastercardPKOBPM3Pr(nullable:true, shared: "percentage")
        dinersClubPr(nullable:true, shared: "percentage")
        ikoPr(nullable:true, blank:true,  shared: "percentage")
        visaEUKKOSt(nullable:true, shared: "number")
        visaEUKDSt(nullable:true, shared: "number")
        visaEUKBSt(nullable:true, shared: "number")
        visaOutEUKKOSt(nullable:true, shared: "number")
        visaOutEUKDSt(nullable:true, shared: "number")
        visaOutEUKBSt(nullable:true, shared: "number")
        visaPolskaKKO1St(nullable:true, shared: "number")
        visaPolskaKKO2St(nullable:true, shared: "number")
        visaPolskaKD1St(nullable:true, shared: "number")
        visaPolskaKD2St(nullable:true, shared: "number")
        visaPolskaKBSt(nullable:true, shared: "number")
        mastercardEUKKSt(nullable:true, shared: "number")
        mastercardEUKDSt(nullable:true, shared: "number")
        mastercardEUKBLSt(nullable:true, shared: "number")
        mastercardEUMSt(nullable:true, shared: "number")
        mastercardOutEUKKSt(nullable:true, shared: "number")
        mastercardOutEUKDSt(nullable:true, shared: "number")
        mastercardOutEUKBSt(nullable:true, shared: "number")
        mastercardOutEUMSt(nullable:true, shared: "number")
        mastercardPolskaKK1St(nullable:true, shared: "number")
        mastercardPolskaKK2St(nullable:true, shared: "number")
        mastercardPolskaKK3St(nullable:true, shared: "number")
        mastercardPolskaKD1St(nullable:true, shared: "number")
        mastercardPolskaKD2St(nullable:true, shared: "number")
        mastercardPolskaKD3St(nullable:true, shared: "number")
        mastercardPolskaKBSt(nullable:true, shared: "number")
        mastercardPolskaM1St(nullable:true, shared: "number")
        mastercardPolskaM2St(nullable:true, shared: "number")
        mastercardPolskaM3St(nullable:true, shared: "number")
        visaPKOBPKKO1St(nullable:true, shared: "number")
        visaPKOBPKKO2St(nullable:true, shared: "number")
        visaPKOBPKD1St(nullable:true, shared: "number")
        visaPKOBPKD2St(nullable:true, shared: "number")
        visaPKOBPKB3St(nullable:true, shared: "number")
        mastercardPKOBPKK1St(nullable:true, shared: "number")
        mastercardPKOBPKK2St(nullable:true, shared: "number")
        mastercardPKOBPKK3St(nullable:true, shared: "number")
        mastercardPKOBPKD1St(nullable:true, shared: "number")
        mastercardPKOBPKD2LSt(nullable:true, shared: "number")
        mastercardPKOBPKD3St(nullable:true, shared: "number")
        mastercardPKOBPKBSt(nullable:true, shared: "number")
        mastercardPKOBPM1St(nullable:true, shared: "number")
        mastercardPKOBPM2St(nullable:true, shared: "number")
        mastercardPKOBPM3St(nullable:true, shared: "number")
        dinersClubSt(nullable:true, blank:true, shared: "number")
        ikoSt(nullable:true, blank:true, shared: "number")
        pp_orange_tk(nullable:true, shared: "percentage")
        pp_orange_tp(nullable:true, shared: "percentage")
        pp_plus_tk(nullable:true, shared: "percentage")
        pp_plus_tp(nullable:true, shared: "percentage")
        pp_tmobile_tk(nullable:true, shared: "percentage")
        pp_tmobile_tp(nullable:true, shared: "percentage")
        pp_heyah_tk(nullable:true, shared: "percentage")
        pp_heyah_tp(nullable:true, shared: "percentage")
        pp_play_tk(nullable:true, shared: "percentage")
        pp_play_tp(nullable:true, shared: "percentage")
        pp_telegrosik_tk(nullable:true, shared: "percentage")
        pp_virginmobile_tk(nullable:true, shared: "percentage")
        pp_lycamobile_tk(nullable:true, shared: "percentage")
        pp_gtmobile_tk(nullable:true, shared: "percentage")
        pp_vectonemobile_tk(nullable:true, shared: "percentage")
        pp_delightmobile_tk(nullable:true, shared: "percentage")
        oplataZaOprogramowanieDoDoladowan(nullable:true, shared: "number" )
		scoringMcc(nullable:true, matches:"[0-9]{4}")
        scoringDzialalnosc(nullable:true)
        scoringSzczegolyDzialalnosci(nullable:true, blank:true)
        scoringWlasnosc(nullable:true)
        scoringDzialalnoscCzas(nullable:true)
        scoringKoncesja(nullable:true)
        rodzajZezwolenia(nullable:true, blank:true)
        scoringCharakterystyka(nullable:true)
        scoringCharakterystykaInna(nullable:true, blank:true)
        scoringWielkoscPunktu(nullable:true)
        scoringAkceptacja(nullable:true)
        scoringMonitoring(nullable:true)
        scoringLokalizacjaPunktu(nullable:true)
        scoringTypPunktu(nullable:true)
        scoringTypPunktuInny(nullable:true, blank:true)
        scoringWielkoscMiejscowosci(nullable:true)
        scoringOtwartyZamkniety(nullable:true)
        scoringStanZadbany(nullable:true)
        scoringSprzedazTowarowEkskluzywnych(nullable:true)
        scoringPonad50ProcentObrotowWNocy(nullable:true)
        scoringRuchTurystycznyPrzygraniczny(nullable:true)
        scoringUslugiPlatneZGory(nullable:true)
        scoringCzestoscTransakcji(nullable:true)
        scoringIloscTransakcji(nullable:true)
        scoringDochodowosc(nullable:true, blank:true)
        scoringDeklaracjaFinansowa(nullable:true)
        scoringDeklaracjaFinansowaObrotOgolem(nullable:true, blank:true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniObrot(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable:true, blank:true)
        akceptantUlicaTytul(nullable:true)
        akceptantUlica(nullable:true)
        akceptantNrDomu(nullable:true)
        akceptantNrMieszkania(nullable:true)
        akceptantMiasto(nullable:true)
        akceptantKodPocztowy(nullable:true)
        akceptantPoczta(nullable:true)
        akceptantTelKomorkowy()
		akceptantTelStacjonarny(nullable:true, validator: { value, process, errors ->
		    if (value == null || process.akceptantTelKomorkowy == null) {
                return true
            }
            if (value.isEmpty() && process.akceptantTelKomorkowy.isEmpty()){
                errors.rejectValue( "akceptantTelStacjonarny", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                errors.rejectValue( "akceptantTelKomorkowy", "default.atLeastOne.phoneNumber", "Nale\u017Cy poda\u0107 przynajmniej jeden numer telefonu")
                return false
            }
		    return true
		})
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
        oplPOSDialUpIlosc(nullable:true, shared: "number")
        oplPOSDialUpIloscPP(nullable:true, shared: "number")
        oplPOSDialUpNormalneMies(nullable:true, shared: "number")
        oplPOSDialUpNormalnePP(nullable:true, shared: "number")
        oplPOSDialUpPreferencyjneMies(nullable:true, shared: "number")
        oplPOSDialUpPreferencyjnePP(nullable:true, shared: "number")
        oplPOSVPNTyp(nullable:true)
        oplPOSVPNIlosc(nullable:true, shared: "number")
        oplPOSVPNIloscPP(nullable:true, shared: "number")
        oplPOSVPNNormalneMies(nullable:true, shared: "number")
        oplPOSVPNNormalnePP(nullable:true, shared: "number")
        oplPOSVPNPreferencyjneMies(nullable:true, shared: "number")
        oplPOSVPNPreferencyjnePP(nullable:true, shared: "number")
        oplPOSSSLTyp(nullable:true)
        oplPOSSSLIlosc(nullable:true, shared: "number")
        oplPOSSSLIloscPP(nullable:true, shared: "number")
        oplPOSSSLNormalneMies(nullable:true, shared: "number")
        oplPOSSSLNormalnePP(nullable:true, shared: "number")
        oplPOSSSLPreferencyjneMies(nullable:true, shared: "number")
        oplPOSSSLPreferencyjnePP(nullable:true, shared: "number")
        oplPOSWiFiTyp(nullable:true)
        oplPOSWiFiIlosc(nullable:true, shared: "number")
        oplPOSWiFiIloscPP(nullable:true, shared: "number")
        oplPOSWiFiNormalneMies(nullable:true, shared: "number")
        oplPOSWiFiNormalnePP(nullable:true, shared: "number")
        oplPOSWiFiPreferencyjneMies(nullable:true, shared: "number")
        oplPOSWiFiPreferencyjnePP(nullable:true, shared: "number")
        oplPOSGPRSTyp(nullable:true)
        oplPOSGPRSIlosc(nullable:true, shared: "number")
        oplPOSGPRSIloscPP(nullable:true, shared: "number")
        oplPOSGPRSNormalneMies(nullable:true, shared: "number")
        oplPOSGPRSNormalnePP(nullable:true, shared: "number")
        oplPOSGPRSPreferencyjneMies(nullable:true, shared: "number")
        oplPOSGPRSPreferencyjnePP(nullable:true, shared: "number")
        oplPOSBaza(nullable:true, shared: "number")
        obslugaTyp(nullable:true)
        obslugaEkonomicznyCena(nullable:true, shared: "number")
        numerRachunkuBankowegoKlienta(nullable:true, matches: "[0-9]{26}")
        bankKlienta(nullable:true)
        oplataZaUruchomienieDCC(nullable:true, shared: "number")
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

}
