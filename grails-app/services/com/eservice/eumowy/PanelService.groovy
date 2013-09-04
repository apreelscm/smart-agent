package com.eservice.eumowy
import com.eservice.eumowy.command.ProcessCommand

class PanelService {

    def cbdService
    def calculatorService
    def springSecurityService
    //domainClass
    //def calcMethods = ["getDcc","getDodatkoweUslugi2","getFormaDoladowania","getIfplus","","","","","","","","","","","",]

    def cbdMethods = ["getAdresDoKorespondencjizAkecptantem","getDaneAkceptanta","getSiedzibaAkceptanta","getSerwis"]

    def getAdresacjaSeciowa(ProcessCommand cmd ,def calc) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd ,def calc) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd ,def calc) {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(cmd.nip);

        cmd.akceptantKontaktUlicaTytul = result?.typ_ulicy ?:  ""
        cmd.akceptantKontaktUlica = result?.ulica  ?: ""
        cmd.akceptantKontaktNrDomu = result?.nr_budynku ?: ""
        cmd.akceptantKontaktNrMieszkania = result?.nr_lokal ?: ""
        cmd.akceptantKontaktMiasto = result?.miejscowosc ?: ""
        cmd.akceptantKontaktKodPocztowy = result?.kod_pocztowy ?: ""
        cmd.akceptantKontaktPoczta = result?.poczta ?: ""
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd,def calc) {
        cmd.dataAneksowanejUmowyPos =  cmd.dataAneksowanejUmowyPos ?: ""
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd,def calc) {
        cmd.dataAneksowanejUmowyPrepaid = cmd.dataAneksowanejUmowyPrepaid ?: ""
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd ,def calc) {
        cmd.umowaCzas = cmd.umowaCzas ?: ""
        cmd.umowaOznOd = cmd.umowaOznOd ?: ""
        cmd.umowaOznDo = cmd.umowaOznDo ?: ""
    }

    def getDaneAkceptanta(ProcessCommand cmd ,def calc) {
        def result = cbdService.getDaneAkceptanta(cmd.nip);

        cmd.akceptantNazwaOficjalna = result?.nazwa ?: ""
        cmd.akceptantNazwaSieciowa = cmd.akceptantNazwaSieciowa ?: "" //TODO
        cmd.akceptantRegon = result?.regon ?: ""

        cmd.akceptantNazwaOficjalnaCbd= cmd.akceptantNazwaOficjalna
        cmd.akceptantNazwaSieciowaCbd= cmd.akceptantNazwaSieciowa
        cmd.akceptantRegonCbd = cmd.akceptantRegon
    }

    def getDaneDoWydruku(ProcessCommand cmd ,def calc) {
        cmd.nazwaDoWydrukuZTerminalaPos = cmd.nazwaDoWydrukuZTerminalaPos ?: ""
        cmd.wydrukNazwaDoWyszukwarki =cmd.wydrukNazwaDoWyszukwarki ?: ""
        cmd.wydrukUlicaTytul =cmd.wydrukUlicaTytul ?: ""
        cmd.wydrukUlica =cmd.wydrukUlica ?: ""
        cmd.wydrukNrDomu =cmd.wydrukNrDomu ?: ""
        cmd.wydrukNrMieszkania =cmd.wydrukNrMieszkania ?: ""
        cmd.wydrukMiasto =cmd.wydrukMiasto ?: ""
        cmd.wydrukKodPocztowy = cmd.wydrukKodPocztowy ?: ""
        cmd.wydrukPoczta =cmd.wydrukPoczta ?: ""
        cmd.wydrukLinia1 =cmd.wydrukLinia1 ?: ""
        cmd.wydrukLinia2 =cmd.wydrukLinia2 ?: ""
    }

    def getDanePos(ProcessCommand cmd ,def calc) {

    }

    def getDanePunktu(ProcessCommand cmd ,def calc) {

    }

    def getDcc(ProcessCommand cmd ,def calc) {
        cmd.oplataVISA = calculatorService.getCalcProperty(calc,"OPLATA_DCC_VISA_ZL")
        cmd.oplataVISAPr =calculatorService.getCalcProperty(calc,"OPLATA_DCC_VISA_PR")
        cmd.oplataMasterCard = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MASTERCARD_ZL")
        cmd.oplataMasterCardPr = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MASTERCARD_PR")
        cmd.oplataMaestro = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MAESTRO_ZL")
        cmd.oplataMasteroPr =calculatorService.getCalcProperty(calc,"OPLATA_DCC_MAESTRO_PR")
    }

    def getDccZakresUruchomienia(ProcessCommand cmd ,def calc) {
        cmd.dccZakresUruchomienia = cmd.dccZakresUruchomienia ?: ""
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd ,def calc) {
        cmd.informacjaHandlowa = cmd.informacjaHandlowa ?: ""
    }

    def getDodajPos(ProcessCommand cmd ,def calc) {

    }

    def getDodajPunkt(ProcessCommand cmd ,def calc) {

    }

    def getDodatkoweUslugi(ProcessCommand cmd ,def calc) {
        cmd.oplataZaDzienneZestawienieTransakcji =cmd.oplataZaDzienneZestawienieTransakcji ?: ""
        cmd.oplataZaMiesieczneZestawienieTransakcji =cmd.oplataZaMiesieczneZestawienieTransakcji ?: ""
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu =cmd.oplataZaPotwierdzenieWykonaniaPrzelewu ?: ""
        cmd.oplataZaDostarczeniePapieru =cmd.oplataZaDostarczeniePapieru ?: ""
        cmd.oplataZaZmianeGrafiki =cmd.oplataZaZmianeGrafiki ?: ""
        cmd.oplataZaInstalacjePOS =cmd.oplataZaInstalacjePOS ?: ""
        cmd.oplataZaInstalacjeGPRS =cmd.oplataZaInstalacjeGPRS ?: ""
        cmd.oplataZaUruchomienieWalutyObcej =cmd.oplataZaUruchomienieWalutyObcej ?: ""
    }

    def getDodatkoweUslugi2(ProcessCommand cmd ,def calc) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty(calc,"OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty(calc,"OPLATA_KALKULATOR")
        cmd.tytulPlatnosciCena =cmd.tytulPlatnosciCena ?: ""
        cmd.pierwszaSesjaCena =cmd.pierwszaSesjaCena ?: ""
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd ,def calc) {

    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {
        cmd.mudCena = ""
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd ,def calc) {
        cmd.weryfikacjaPINCena =cmd.weryfikacjaPINCena ?: ""
        cmd.systemKasowyCena =cmd.systemKasowyCena ?: ""
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd ,def calc) {
    }

    def getFormaDoladowania(ProcessCommand cmd ,def calc) {
        cmd.doladowania_tp = calculatorService.getCalcProperty(calc,"CZY_TELEPOMPKA")
        cmd.doladowania_tk = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_VISA")
        cmd.srednia_sprzedaz_doladowan = calculatorService.getCalcProperty(calc,"DEKLARACJA_SPRZEDAZY_PP")
        cmd.srednia_sprzedaz_doladowan_slownie = cmd.srednia_sprzedaz_doladowan_slownie ?: ""
    }

    def getFunkcjeTerminala(ProcessCommand cmd ,def calc) {

    }

    def getIfplus(ProcessCommand cmd ,def calc) {
        cmd.ifOplataVISA = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_VISA")
        cmd.ifOplataMasterCard = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_MASTERCARD")
        cmd.ifOplataDinersClub = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_DINERSCLUB")
        cmd.ifOplataIKO = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_IKO")
        cmd.ifOplataPKOPB = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_PKOBP")
    }

    def getInformacjeDodatkowe(ProcessCommand cmd ,def calc) {
        cmd.dzialalnoscForma = cmd.dzialalnoscForma ?: ""
        cmd.dzialalnoscFormaInna = cmd.dzialalnoscFormaInna?: ""
        cmd.dzialalnoscDokument = cmd.dzialalnoscDokument ?: ""
        cmd.dzialalnoscDokumentInny = cmd.dzialalnoscDokumentInny ?: ""
    }

    def getInformacjeTechniczne(ProcessCommand cmd ,def calc) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd ,def calc) {
        cmd.okresLojalnosciowy = calculatorService.getCalcProperty(calc,"LICZBA_MIESIECY_LOJ") ?: ""
    }

    def getOpieka(ProcessCommand cmd ,def calc) {

    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd ,def calc) {
        cmd.oplataZaUruchomienieDCC =cmd.oplataZaUruchomienieDCC ?: ""
    }

    def getOplatyDCC(ProcessCommand cmd ,def calc) {
        cmd.oplataZaPlatnoscWInnejWalucie = calculatorService.getCalcProperty(calc,"OPLATA_DCC")
    }

    def getOsobaDoKontaktu(ProcessCommand cmd ,def calc) {
        cmd.kontaktTytul =cmd.kontaktTytul ?: ""
        cmd.kontaktImie =cmd.kontaktImie ?: ""
        cmd.kontaktNazwisko =cmd.kontaktNazwisko ?: ""
        cmd.kontaktTelStacjonarny =cmd.kontaktTelStacjonarny ?: ""
        cmd.kontaktTelKomorkowy =cmd.kontaktTelKomorkowy ?: ""
        cmd.kontaktEmail =cmd.kontaktEmail ?: ""
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd ,def calc) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd ,def calc) {
        def user = springSecurityService.principal;
        cmd.pozyskujacyTytul = ""
        cmd.pozyskujacyImie =  user.imie
        cmd.pozyskujacyNazwisko = user.nazwisko
        cmd.pozyskujacyNumer =  user.nr
    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd ,def calc){

        /*  def osoba1 = cbdService.getOsoba1UprawnionaDoPodpisaniaUmowy(cmd.nip);
          cmd.reprezentant1Tytul =  osoba1.tytul
          cmd.reprezentant1Imie = osoba1.imie
          cmd.reprezentant1Nazwisko = osoba1.nazwisko

          def osoba2 = cbdService.getOsoba2UprawnionaDoPodpisaniaUmowy(cmd.nip);
          cmd.reprezentant2Tytul = osoba2.tytul
          cmd.reprezentant2Imie = osoba2.imie
          cmd.reprezentant2Nazwisko = osoba2.nazwisko*/

        cmd.reprezentant1Tytul =cmd.reprezentant1Tytul ?: ""
        cmd.reprezentant1Imie =cmd.reprezentant1Imie ?: ""
        cmd.reprezentant1Nazwisko =cmd.reprezentant1Nazwisko ?: ""
        cmd.reprezentant2Tytul =cmd.reprezentant2Tytul ?: ""
        cmd.reprezentant2Imie =cmd.reprezentant2Imie ?: ""
        cmd.reprezentant2Nazwisko =cmd.reprezentant2Nazwisko ?: ""

    }

    def setAtLeastAs(def data ,def calcValue){

        println("data : ${data} , cal : ${calcValue}")

        if(!data || !data.toString().isNumber()) {
            return calcValue
        }

        if (!calcValue || !calcValue.toString().isNumber()) {
            return data
        }

        def oldNumber = Double.valueOf(data)
        def calcNumber = Double.valueOf(calcValue)

        println("oldNumber : ${oldNumber} , calcNumber : ${calcNumber}")

         oldNumber < calcNumber ? calcValue : data


    }

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd ,def calc) {
        cmd.visaEUKKOPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_ZL")

        cmd.visaEUKKOSt = setAtLeastAs(cmd.visaEUKKOSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_PROCENT"))
        cmd.visaEUKDPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_PROCENT")
        cmd.visaEUKDSt = setAtLeastAs(cmd.visaEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_ZL"))
        cmd.visaEUKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_PROCENT")
        cmd.visaEUKBSt = setAtLeastAs(cmd.visaEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_ZL"))

        cmd.visaOutEUKKOPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_PROCENT")
        cmd.visaOutEUKKOSt = setAtLeastAs(cmd.visaOutEUKKOSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_ZL"))
        cmd.visaOutEUKDPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_PROCENT")
        cmd.visaOutEUKDSt = setAtLeastAs(cmd.visaOutEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_ZL"))
        cmd.visaOutEUKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_PROCENT")
        cmd.visaOutEUKBSt = setAtLeastAs(cmd.visaOutEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_ZL"))

        cmd.visaPolskaKKO1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_PROCENT")
        cmd.visaPolskaKKO1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_ZL")
        cmd.visaPolskaKKO2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_PROCENT")
        cmd.visaPolskaKKO2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_ZL")
        cmd.visaPolskaKD1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_PROCENT")
        cmd.visaPolskaKD1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_ZL")
        cmd.visaPolskaKD2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_PROCENT")
        cmd.visaPolskaKD2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_ZL")
        cmd.visaPolskaKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_PROCENT")
        cmd.visaPolskaKBSt = setAtLeastAs(cmd.visaPolskaKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_ZL"))

        cmd.mastercardEUKKPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_PROCENT")
        cmd.mastercardEUKKSt = setAtLeastAs(cmd.mastercardEUKKSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_ZL"))
        cmd.mastercardEUKDPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_PROCENT")
        cmd.mastercardEUKDSt = setAtLeastAs(cmd.mastercardEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_ZL"))
        cmd.mastercardEUKBLPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_PROCENT")
        cmd.mastercardEUKBLSt = setAtLeastAs(cmd.mastercardEUKBLSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_ZL"))
        cmd.mastercardEUMPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_PROCENT")
        cmd.mastercardEUMSt = setAtLeastAs(cmd.mastercardEUMSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_ZL"))

        cmd.mastercardOutEUKKPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_PROCENT")
        cmd.mastercardOutEUKKSt = setAtLeastAs(cmd.mastercardOutEUKKSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_ZL"))
        cmd.mastercardOutEUKDPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_PROCENT")
        cmd.mastercardOutEUKDSt = setAtLeastAs(cmd.mastercardOutEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_ZL"))
        cmd.mastercardOutEUKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_PROCENT")
        cmd.mastercardOutEUKBSt = setAtLeastAs(cmd.mastercardOutEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_ZL"))
        cmd.mastercardOutEUMPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_PROCENT")
        cmd.mastercardOutEUMSt = setAtLeastAs(cmd.mastercardOutEUMSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_ZL"))

        cmd.mastercardPolskaKK1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_PROCENT")
        cmd.mastercardPolskaKK1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_ZL")
        cmd.mastercardPolskaKK2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_PROCENT")
        cmd.mastercardPolskaKK2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_ZL")
        cmd.mastercardPolskaKK3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_PROCENT")
        cmd.mastercardPolskaKK3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_ZL")
        cmd.mastercardPolskaKD1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_PROCENT")
        cmd.mastercardPolskaKD1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_ZL")
        cmd.mastercardPolskaKD2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_PROCENT")
        cmd.mastercardPolskaKD2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_ZL")
        cmd.mastercardPolskaKD3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_PROCENT")
        cmd.mastercardPolskaKD3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_ZL")
		cmd.mastercardPolskaKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_625_PROCENT")
		cmd.mastercardPolskaKBSt = calculatorService.getCalcProperty(calc,"OPLATA_MSC_625_ZL")
        cmd.mastercardPolskaM1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_PROCENT")
        cmd.mastercardPolskaM1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_ZL")
        cmd.mastercardPolskaM2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_PROCENT")
        cmd.mastercardPolskaM2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_ZL")
        cmd.mastercardPolskaM3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_PROCENT")
        cmd.mastercardPolskaM3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_ZL")

        cmd.visaPKOBPKKO1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_PROCENT")
        cmd.visaPKOBPKKO1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_ZL")
        cmd.visaPKOBPKKO2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_PROCENT")
        cmd.visaPKOBPKKO2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_ZL")
        cmd.visaPKOBPKD1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_PROCENT")
        cmd.visaPKOBPKD1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_ZL")
        cmd.visaPKOBPKD2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_PROCENT")
        cmd.visaPKOBPKD2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_ZL")
        cmd.visaPKOBPKB3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_PROCENT")
        cmd.visaPKOBPKB3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_ZL")

        cmd.mastercardPKOBPKK1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_PROCENT")
        cmd.mastercardPKOBPKK1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_ZL")
        cmd.mastercardPKOBPKK2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_PROCENT")
        cmd.mastercardPKOBPKK2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_ZL")
        cmd.mastercardPKOBPKK3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_PROCENT")
        cmd.mastercardPKOBPKK3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_ZL")
        cmd.mastercardPKOBPKD1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_PROCENT")
        cmd.mastercardPKOBPKD1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_ZL")
        cmd.mastercardPKOBPKD2LPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_PROCENT")
        cmd.mastercardPKOBPKD2LSt = calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_ZL")
        cmd.mastercardPKOBPKD3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_PROCENT")
        cmd.mastercardPKOBPKD3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_ZL")
        cmd.mastercardPKOBPKBPr   = calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_PROCENT")
        cmd.mastercardPKOBPKBSt   = calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_ZL")
        cmd.mastercardPKOBPM1Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_PROCENT")
        cmd.mastercardPKOBPM1St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_ZL")
        cmd.mastercardPKOBPM2Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_PROCENT")
        cmd.mastercardPKOBPM2St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_ZL")
        cmd.mastercardPKOBPM3Pr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_PROCENT")
        cmd.mastercardPKOBPM3St = calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_ZL")

        cmd.dinersClubPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_9_PROCENT")

        cmd.ikoSt = calculatorService.getCalcProperty(calc,"OPLATA_MSC_10_PROCENT")
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd ,def calc) {
        cmd.pp_orange_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_ORANGE")
        cmd.pp_orange_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_ORANGE")
        cmd.pp_plus_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_PLUS")
        cmd.pp_plus_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_PLUS")
        cmd.pp_tmobile_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_T_MOBILE")
        cmd.pp_tmobile_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_T_MOBILE")
        cmd.pp_heyah_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_HEYAH")
        cmd.pp_heyah_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_HEYAH")
        cmd.pp_play_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_PLAY")
        cmd.pp_play_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_PLAY")
        cmd.pp_telegrosik_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_GALENA")
        cmd.pp_virginmobile_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_VIRGIN")
        cmd.pp_lycamobile_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_LYCA")
        cmd.pp_gtmobile_tk =calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_GTMOBILE")
        cmd.pp_vectonemobile_tk =  calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_VECTOBE")
        cmd.pp_delightmobile_tk =  calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_DELIGHT")
        cmd.oplataZaOprogramowanieDoDoladowan = calculatorService.getCalcProperty(calc,"OPLATA_ZA_APL_PP")
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd ,def calc) {

    }

    def getRachunekBankowyKlienta(ProcessCommand cmd ,def calc) {
        cmd.numerRachunkuBankowegoKlienta =cmd.numerRachunkuBankowegoKlienta ?: ""
        cmd.bankKlienta = cmd.bankKlienta ?: ""
    }

    def getScoring(ProcessCommand cmd ,def calc) {
        cmd.scoringMcc = calculatorService.getCalcProperty(calc,"MCC")
        cmd.scoringDzialalnosc = cmd.scoringDzialalnosc ?: ""
        cmd.scoringIloscTransakcji = cmd.scoringIloscTransakcji ?: ""
        cmd.scoringCzestoscTransakcji = cmd.scoringCzestoscTransakcji ?: ""
        cmd.scoringOtwartyZamkniety = cmd.scoringOtwartyZamkniety ?: ""
        cmd.scoringStanZadbany = cmd.scoringStanZadbany ?: ""
        cmd.scoringWielkoscMiejscowosci = cmd.scoringWielkoscMiejscowosci ?: ""
        cmd.scoringLokalizacjaPunktu = cmd.scoringLokalizacjaPunktu ?: ""
        cmd.scoringTypPunktu = cmd.scoringTypPunktu ?: ""
        cmd.scoringTypPunktuInny = cmd.scoringTypPunktuInny ?: ""
        cmd.scoringWielkoscPunktu = cmd.scoringWielkoscPunktu ?: ""
        cmd.scoringAkceptacja = cmd.scoringAkceptacja ?: ""
        cmd.scoringMonitoring = cmd.scoringMonitoring ?: ""
        cmd.scoringCharakterystyka = cmd.scoringCharakterystyka ?: ""
        cmd.scoringCharakterystykaInna = cmd.scoringCharakterystykaInna ?: ""
        cmd.scoringKoncesja = cmd.scoringKoncesja ?: ""
        cmd.rodzajZezwolenia = cmd.rodzajZezwolenia ?: ""
        cmd.scoringWlasnosc = cmd.scoringWlasnosc ?: ""
        cmd.scoringDzialalnoscCzas = cmd.scoringDzialalnoscCzas ?: ""
        cmd.scoringSprzedazTowarowEkskluzywnych = cmd.scoringSprzedazTowarowEkskluzywnych ?: ""
        cmd.scoringPonad50ProcentObrotowWNocy = cmd.scoringPonad50ProcentObrotowWNocy ?: ""
        cmd.scoringRuchTurystycznyPrzygraniczny = cmd.scoringRuchTurystycznyPrzygraniczny ?: ""
        cmd.scoringUslugiPlatneZGory = cmd.scoringUslugiPlatneZGory ?: ""
        cmd.scoringDochodowosc = cmd.scoringDochodowosc ?: ""
        cmd.scoringDeklaracjaFinansowa = cmd.scoringDeklaracjaFinansowa ?: ""
        cmd.scoringDeklaracjaFinansowaObrotOgolem = cmd.scoringDeklaracjaFinansowaObrotOgolem ?: ""
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = cmd.scoringDeklaracjaFinansowaObrotNaKarty ?: ""
        cmd.scoringDeklaracjaFinansowaSredniObrot = cmd.scoringDeklaracjaFinansowaSredniObrot ?: ""
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = cmd.scoringDeklaracjaFinansowaSredniaTransakcja ?: ""
    }

    def getSerwis(ProcessCommand cmd ,def calc) {

        def SERWIS_TYPES = [[key: "PAKIET_SERWIS_PRESTIZ", value:"prestige"],
                [key: "PAKIET_SERWIS_KOMFORT", value:"comfort"],
                [key: "PAKIET_SERWIS_EKONOMICZNY", value:"economic"]]

        def result = SERWIS_TYPES.find{
            calculatorService.hasCalcProperty(calc,it.key,"TAK")
        }

        cmd.obslugaTyp = result?.value ?: "";
        cmd.obslugaEkonomicznyCena =cmd.obslugaEkonomicznyCena ?: "";
    }

    def getSerwisEkonomiczny(ProcessCommand cmd ,def calc) {

    }

    def getSerwisKomfort(ProcessCommand cmd ,def calc) {

    }

    def getSerwisPrzestiz(ProcessCommand cmd ,def calc) {

    }

    def getSiedzibaAkceptanta(ProcessCommand cmd ,def calc) {

        def result = cbdService.getSiedzibaAkceptanta(cmd.nip);

        cmd.akceptantUlicaTytul = result?.typ_ulicy ?: ""
        cmd.akceptantUlica = result?.ulica ?: ""
        cmd.akceptantNrDomu = result?.nr_budynku ?: ""
        cmd.akceptantNrMieszkania = result?.nr_lokal ?: ""
        cmd.akceptantMiasto = result?.miejscowosc ?: ""
        cmd.akceptantKodPocztowy = result?.kod_pocztowy ?: ""
        cmd.akceptantPoczta = result?.poczta ?: ""
        cmd.akceptantTelStacjonarny = result?.telStac ?: ""
        cmd.akceptantFax = result?.faks ?: ""
        cmd.akceptantTelKomorkowy = result?.telKom ?: ""

        cmd.akceptantUlicaTytulCbd = cmd.akceptantUlicaTytul
        cmd.akceptantUlicaCbd = cmd.akceptantUlica
        cmd.akceptantNrDomuCbd = cmd.akceptantNrDomu
        cmd.akceptantNrMieszkaniaCbd = cmd.akceptantNrMieszkania
        cmd.akceptantMiastoCbd = cmd.akceptantMiasto
        cmd.akceptantKodPocztowyCbd = cmd.akceptantKodPocztowy
        cmd.akceptantPocztaCbd = cmd.akceptantPoczta
        cmd.akceptantTelStacjonarnyCbd = cmd.akceptantTelStacjonarny
        cmd.akceptantFaxCbd = cmd.akceptantFax
        cmd.akceptantTelKomorkowyCbd = cmd.akceptantTelKomorkowy
    }

    def getUmowa2(ProcessCommand cmd ,def calc) {
        cmd.miejsceUmowy= cmd.miejsceUmowy ?: ""
    }

    def getUwagi(ProcessCommand cmd ,def calc) {
        //EMPTY
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd ,def calc) {
       /* cmd.punktyTytulPlatnosci= cmd.punktyTytulPlatnosci?: []
        cmd.punktySystemKasowy = cmd.punktySystemKasowy ?: []
        cmd.punktyUta = cmd.punktyUta ?: []
        cmd.punktyWybrane = cmd.punktyWybrane ?: []*/
    }

    def getZalaczniki(ProcessCommand cmd ,def calc) {

    }

    def getZestawPos(ProcessCommand cmd ,def calc) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd ,def calc) {

    }

    def getZestawPosStawkiPreferencyjne(ProcessCommand cmd ,def calc) {

    }
}