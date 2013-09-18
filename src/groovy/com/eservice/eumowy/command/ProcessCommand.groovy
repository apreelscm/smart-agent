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

        oplataZaDzienneZestawienieTransakcji(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaDostarczeniePapieru(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaZmianeGrafiki(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaInstalacjePOS(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaInstalacjeGPRS(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaUruchomienieWalutyObcej(validator: nullableTrueBlankFalse, shared: "number")
        wydrukGrafikiCena(validator: nullableTrueBlankFalse, shared: "number")
        dzialaniaMatematyczneCena(validator: nullableTrueBlankFalse, shared: "number")
        tytulPlatnosciCena(validator: nullableTrueBlankFalse, shared: "number")
        pierwszaSesjaCena(validator: nullableTrueBlankFalse, shared: "number")

        akceptantKontaktUlicaTytul(validator: nullableTrueBlankFalse)
        akceptantKontaktUlica(validator: nullableTrueBlankFalse)
        akceptantKontaktNrDomu(validator: nullableTrueBlankFalse)
        akceptantKontaktNrMieszkania(validator: nullableTrueBlankFalse)
        akceptantKontaktMiasto(validator: nullableTrueBlankFalse)
        akceptantKontaktKodPocztowy(validator: nullableTrueBlankFalse)
        akceptantKontaktPoczta(validator: nullableTrueBlankFalse)
        dataAneksowanejUmowyPos(validator: nullableTrueBlankFalse, shared: "date")
        dataAneksowanejUmowyPrepaid(validator: nullableTrueBlankFalse, shared: "date")
        umowaCzas(validator: nullableTrueBlankFalse)
        umowaOznOd(nullable:true, blank:true, shared: "date")
        umowaOznDo(nullable:true, blank:true, shared: "date")
        akceptantNazwaOficjalna(nullable:false, blank:false) //a1!, M
        akceptantNazwaSieciowa(validator: nullableTrueBlankFalse) //a1!
        akceptantRegon(nullable:true,blank:false, matches:"[0-9]{9}") //111111111, M
        akceptantNazwaOficjalnaCbd(nullable:true)
        akceptantNazwaSieciowaCbd(nullable:true)
        akceptantRegonCbd(nullable:true)
        nazwaDoWydrukuZTerminalaPos(nullable:true)
        wydrukNazwaDoWyszukwarki(nullable:true)
        wydrukUlicaTytul(validator: nullableTrueBlankFalse)
        wydrukUlica(validator: nullableTrueBlankFalse)
        wydrukNrDomu(validator: nullableTrueBlankFalse)
        wydrukNrMieszkania(validator: nullableTrueBlankFalse)
        wydrukMiasto(validator: nullableTrueBlankFalse)
        wydrukKodPocztowy(validator: nullableTrueBlankFalse)
        wydrukPoczta(validator: nullableTrueBlankFalse)
        wydrukLinia1(nullable:true, blank:true)
        wydrukLinia2(nullable:true, blank:true)
        oplataVISA(validator: nullableTrueBlankFalse, shared: "number")
        oplataVISAPr(validator: nullableTrueBlankFalse, shared: "number")
        oplataMasterCard(validator: nullableTrueBlankFalse, shared: "number")
        oplataMasterCardPr(validator: nullableTrueBlankFalse, shared: "number")
        oplataMaestro(validator: nullableTrueBlankFalse, shared: "number")
        oplataMasteroPr(validator: nullableTrueBlankFalse, shared: "number")
        dccZakresUruchomienia(validator: nullableTrueBlankFalse)
        informacjaHandlowa(validator: nullableTrueBlankFalse)
        oplataZaDzienneZestawienieTransakcji(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaMiesieczneZestawienieTransakcji(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaPotwierdzenieWykonaniaPrzelewu(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaDostarczeniePapieru(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaZmianeGrafiki(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaInstalacjePOS(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaInstalacjeGPRS(validator: nullableTrueBlankFalse, shared: "number")
        oplataZaUruchomienieWalutyObcej(validator: nullableTrueBlankFalse, shared: "number")
        wydrukGrafikiCena(validator: nullableTrueBlankFalse, shared: "number")
        dzialaniaMatematyczneCena(validator: nullableTrueBlankFalse, shared: "number")
        tytulPlatnosciCena(validator: nullableTrueBlankFalse, shared: "number")
        pierwszaSesjaCena(validator: nullableTrueBlankFalse, shared: "number")
        mudCena(validator: nullableTrueBlankFalse, shared:"number")
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
        srednia_sprzedaz_doladowan(validator: nullableTrueBlankFalse, shared:"number")
        srednia_sprzedaz_doladowan_slownie(validator: nullableTrueBlankFalse)
        ifOplataVISA(validator: nullableTrueBlankFalse, shared: "number") //1.11 %, M
        ifOplataMasterCard(validator: nullableTrueBlankFalse, shared: "number") //1.11 %, M
        ifOplataDinersClub(validator: nullableTrueBlankFalse, shared: "number") //1.11 %, M
        ifOplataIKO(validator: nullableTrueBlankFalse, shared: "number") //1.11 %, M
        ifOplataPKOPB(validator: nullableTrueBlankFalse, shared: "number") //1.11 %, M
        dzialalnoscForma(validator: nullableTrueBlankFalse)
        dzialalnoscFormaInna(nullable:true, blank:true)
        dzialalnoscDokument(validator: nullableTrueBlankFalse)
        dzialalnoscDokumentInny(nullable:true, blank:true)
        okresLojalnosciowy(validator: nullableTrueBlankFalse)
        oplataZaPlatnoscWInnejWalucie(validator: nullableTrueBlankFalse)
        kontaktTytul(validator: nullableTrueBlankFalse)
        kontaktImie(validator: nullableTrueBlankFalse)
        kontaktNazwisko(validator: nullableTrueBlankFalse)
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
        pozyskujacyTytul(validator: nullableTrueBlankFalse)
        pozyskujacyImie(validator: nullableTrueBlankFalse)
        pozyskujacyNazwisko(validator: nullableTrueBlankFalse)
        pozyskujacyNumer(validator: nullableTrueBlankFalse)
        reprezentant1Tytul(validator: nullableTrueBlankFalse)
        reprezentant1Imie(validator: nullableTrueBlankFalse)
        reprezentant1Nazwisko(validator: nullableTrueBlankFalse)
        reprezentant2Tytul(validator: nullableTrueBlankFalse)
        reprezentant2Imie(nullable:true, blank:true)
        reprezentant2Nazwisko(nullable:true, blank:true)
        visaEUKKOPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaEUKDPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaEUKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaOutEUKKOPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaOutEUKDPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaOutEUKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPolskaKKO1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPolskaKKO2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPolskaKD1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPolskaKD2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPolskaKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardEUKKPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardEUKDPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardEUKBLPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardEUMPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardOutEUKKPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardOutEUKDPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardOutEUKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardOutEUMPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKK1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKK2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKK3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKD1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKD2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKD3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaM1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaM2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPolskaM3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPKOBPKKO1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPKOBPKKO2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPKOBPKD1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPKOBPKD2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        visaPKOBPKB3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKK1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKK2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKK3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKD1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKD2LPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKD3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPKBPr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPM1Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPM2Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        mastercardPKOBPM3Pr(validator: nullableTrueBlankFalse, shared: "percentage")
        dinersClubPr(validator: nullableTrueBlankFalse, shared: "percentage")
        ikoPr(nullable:true, blank:true,  shared: "percentage")
        visaEUKKOSt(validator: nullableTrueBlankFalse, shared: "number")
        visaEUKDSt(validator: nullableTrueBlankFalse, shared: "number")
        visaEUKBSt(validator: nullableTrueBlankFalse, shared: "number")
        visaOutEUKKOSt(validator: nullableTrueBlankFalse, shared: "number")
        visaOutEUKDSt(validator: nullableTrueBlankFalse, shared: "number")
        visaOutEUKBSt(validator: nullableTrueBlankFalse, shared: "number")
        visaPolskaKKO1St(validator: nullableTrueBlankFalse, shared: "number")
        visaPolskaKKO2St(validator: nullableTrueBlankFalse, shared: "number")
        visaPolskaKD1St(validator: nullableTrueBlankFalse, shared: "number")
        visaPolskaKD2St(validator: nullableTrueBlankFalse, shared: "number")
        visaPolskaKBSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardEUKKSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardEUKDSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardEUKBLSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardEUMSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardOutEUKKSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardOutEUKDSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardOutEUKBSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardOutEUMSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKK1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKK2St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKK3St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKD1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKD2St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKD3St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaKBSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaM1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaM2St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPolskaM3St(validator: nullableTrueBlankFalse, shared: "number")
        visaPKOBPKKO1St(validator: nullableTrueBlankFalse, shared: "number")
        visaPKOBPKKO2St(validator: nullableTrueBlankFalse, shared: "number")
        visaPKOBPKD1St(validator: nullableTrueBlankFalse, shared: "number")
        visaPKOBPKD2St(validator: nullableTrueBlankFalse, shared: "number")
        visaPKOBPKB3St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKK1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKK2St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKK3St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKD1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKD2LSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKD3St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPKBSt(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPM1St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPM2St(validator: nullableTrueBlankFalse, shared: "number")
        mastercardPKOBPM3St(validator: nullableTrueBlankFalse, shared: "number")
        dinersClubSt(nullable:true, blank:true, shared: "number")
        ikoSt(nullable:true, blank:true, shared: "number")
        pp_orange_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_orange_tp(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_plus_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_plus_tp(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_tmobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_tmobile_tp(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_heyah_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_heyah_tp(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_play_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_play_tp(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_telegrosik_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_virginmobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_lycamobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_gtmobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_vectonemobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        pp_delightmobile_tk(validator: nullableTrueBlankFalse, shared: "percentage")
        oplataZaOprogramowanieDoDoladowan(validator: nullableTrueBlankFalse, shared: "number" )
        scoringMcc(nullable:true, blank:false, matches:"[0-9]{4}")
        scoringDzialalnosc(validator: nullableTrueBlankFalse)
        scoringSzczegolyDzialalnosci(nullable:true, blank:true)
        scoringWlasnosc(validator: nullableTrueBlankFalse)
        scoringDzialalnoscCzas(validator: nullableTrueBlankFalse)
        scoringKoncesja(validator: nullableTrueBlankFalse)
        rodzajZezwolenia(nullable:true, blank:true)
        scoringCharakterystyka(validator: nullableTrueBlankFalse)
        scoringCharakterystykaInna(nullable:true, blank:true)
        scoringWielkoscPunktu(validator: nullableTrueBlankFalse)
        scoringAkceptacja(validator: nullableTrueBlankFalse)
        scoringMonitoring(validator: nullableTrueBlankFalse)
        scoringLokalizacjaPunktu(validator: nullableTrueBlankFalse)
        scoringTypPunktu(validator: nullableTrueBlankFalse)
        scoringTypPunktuInny(nullable:true, blank:true)
        scoringWielkoscMiejscowosci(validator: nullableTrueBlankFalse)
        scoringOtwartyZamkniety(validator: nullableTrueBlankFalse)
        scoringStanZadbany(nullable:true)
        scoringSprzedazTowarowEkskluzywnych(validator: nullableTrueBlankFalse)
        scoringPonad50ProcentObrotowWNocy(validator: nullableTrueBlankFalse)
        scoringRuchTurystycznyPrzygraniczny(validator: nullableTrueBlankFalse)
        scoringUslugiPlatneZGory(validator: nullableTrueBlankFalse)
        scoringCzestoscTransakcji(validator: nullableTrueBlankFalse)
        scoringIloscTransakcji(validator: nullableTrueBlankFalse)
        scoringDochodowosc(nullable:true, blank:true)
        scoringDeklaracjaFinansowa(validator: nullableTrueBlankFalse)
        scoringDeklaracjaFinansowaObrotOgolem(nullable:true, blank:true)
        scoringDeklaracjaFinansowaObrotNaKarty(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniObrot(nullable:true, blank:true)
        scoringDeklaracjaFinansowaSredniaTransakcja(nullable:true, blank:true)
        akceptantUlicaTytul(validator: nullableTrueBlankFalse)
        akceptantUlica(validator: nullableTrueBlankFalse)
        akceptantNrDomu(validator: nullableTrueBlankFalse)
        akceptantNrMieszkania(validator: nullableTrueBlankFalse)
        akceptantMiasto(validator: nullableTrueBlankFalse)
        akceptantKodPocztowy(validator: nullableTrueBlankFalse)
        akceptantPoczta(validator: nullableTrueBlankFalse)
        akceptantTelKomorkowy()
		akceptantTelStacjonarny(validator: { value, process, errors ->
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
        obslugaTyp(validator: nullableTrueBlankFalse)
        obslugaEkonomicznyCena(validator: nullableTrueBlankFalse, shared: "number")
        numerRachunkuBankowegoKlienta(nullable:true, blank:false, matches: "[0-9]{26}")
        bankKlienta(validator: nullableTrueBlankFalse)
        oplataZaUruchomienieDCC(validator: nullableTrueBlankFalse, shared: "number")
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
