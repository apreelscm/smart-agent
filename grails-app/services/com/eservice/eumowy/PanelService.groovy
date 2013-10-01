package com.eservice.eumowy
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

class PanelService {

    def cbdService
    def calculatorService
    def springSecurityService
    //domainClass
    //def calcMethods = ["getDcc","getDodatkoweUslugi2","getFormaDoladowania","getIfplus","","","","","","","","","","","",]

    def cbdMethods = ["getAdresDoKorespondencjizAkecptantem","getDaneAkceptanta","getSiedzibaAkceptanta","getSerwis"]

    def getAdresacjaSeciowa(ProcessCommand cmd ) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd ) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd ) {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(cmd.nip);

        cmd.akceptantKontaktUlicaTytul = result?.typ_ulicy ?:  "UL"
        cmd.akceptantKontaktUlica = result?.ulica  ?: ""
        cmd.akceptantKontaktNrDomu = result?.nr_budynku ?: ""
        cmd.akceptantKontaktNrMieszkania = result?.nr_lokal ?: ""
        cmd.akceptantKontaktMiasto = result?.miejscowosc ?: ""
        cmd.akceptantKontaktKodPocztowy = result?.kod_pocztowy ?: ""
        cmd.akceptantKontaktPoczta = result?.poczta ?: ""
    }
	
	def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd) {
		def result = cbdService.getAneksDoUmowyNajmuZestawuPos(cmd.nip)
		cmd.dataAneksowanejUmowyPos =  DateUtils.getFormattedDate(result?.dataAneksowanejUmowyPos ?: "")
	}

	def getAneksDoUmowyPrepaid(ProcessCommand cmd) {
		def result = cbdService.getAneksDoUmowyPrepaid(cmd.nip)
		cmd.dataAneksowanejUmowyPrepaid = DateUtils.getFormattedDate(result?.dataAneksowanejUmowyPrepaid ?: "")
	}

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd ) {
        cmd.umowaCzas = nullify(cmd.umowaCzas)
        cmd.umowaOznOd = nullify(cmd.umowaOznOd)
        cmd.umowaOznDo = nullify(cmd.umowaOznDo)
    }

    def getDaneAkceptanta(ProcessCommand cmd ) {
        def result = cbdService.getDaneAkceptanta(cmd.nip);

        cmd.akceptantNazwaOficjalna = result?.nazwa ?: ""
        cmd.akceptantNazwaSieciowa = nullify(cmd.akceptantNazwaSieciowa) //TODO
        cmd.akceptantRegon = result?.regon ?: ""

        cmd.akceptantNazwaOficjalnaCbd= cmd.akceptantNazwaOficjalna
        cmd.akceptantNazwaSieciowaCbd= cmd.akceptantNazwaSieciowa
        cmd.akceptantRegonCbd = cmd.akceptantRegon
    }

    def getDaneDoWydruku(ProcessCommand cmd ) {
        cmd.nazwaDoWydrukuZTerminalaPos = nullify(cmd.nazwaDoWydrukuZTerminalaPos)
        cmd.wydrukNazwaDoWyszukwarki = nullify(cmd.wydrukNazwaDoWyszukwarki)
        cmd.wydrukUlicaTytul = nullify(cmd.wydrukUlicaTytul)
        cmd.wydrukUlica = nullify(cmd.wydrukUlica)
        cmd.wydrukNrDomu = nullify(cmd.wydrukNrDomu)
        cmd.wydrukNrMieszkania = nullify(cmd.wydrukNrMieszkania)
        cmd.wydrukMiasto = nullify(cmd.wydrukMiasto)
        cmd.wydrukKodPocztowy = nullify(cmd.wydrukKodPocztowy)
        cmd.wydrukPoczta = nullify(cmd.wydrukPoczta)
        cmd.wydrukLinia1 = nullify(cmd.wydrukLinia1)
        cmd.wydrukLinia2 = nullify(cmd.wydrukLinia2)
    }

    def getDanePos(ProcessCommand cmd ) {

    }

    def getDanePunktu(ProcessCommand cmd ) {

    }

    def getDcc(ProcessCommand cmd ) {
        cmd.oplataVISA = calculatorService.getCalcProperty("OPLATA_DCC_VISA_ZL")
        cmd.oplataVISAPr =calculatorService.getCalcProperty("OPLATA_DCC_VISA_PR")
        cmd.oplataMasterCard = calculatorService.getCalcProperty("OPLATA_DCC_MASTERCARD_ZL")
        cmd.oplataMasterCardPr = calculatorService.getCalcProperty("OPLATA_DCC_MASTERCARD_PR")
        cmd.oplataMaestro = calculatorService.getCalcProperty("OPLATA_DCC_MAESTRO_ZL")
        cmd.oplataMaestroPr =calculatorService.getCalcProperty("OPLATA_DCC_MAESTRO_PR")
    }

    def getDccZakresUruchomienia(ProcessCommand cmd ) {
        cmd.dccZakresUruchomienia = nullify(cmd.dccZakresUruchomienia)
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd ) {
        cmd.informacjaHandlowa = nullify(cmd.informacjaHandlowa)
    }

    def getDodajPos(ProcessCommand cmd ) {

    }

    def getDodajPunkt(ProcessCommand cmd ) {
        cmd.liczbaTerminali = calculatorService.getCalcProperty("LICZBA_POS_MAX")
    }

    def getDodatkoweUslugi(ProcessCommand cmd ) {
        cmd.oplataZaDzienneZestawienieTransakcji = nullify(cmd.oplataZaDzienneZestawienieTransakcji)
        cmd.oplataZaMiesieczneZestawienieTransakcji = nullify(cmd.oplataZaMiesieczneZestawienieTransakcji)
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu = nullify(cmd.oplataZaPotwierdzenieWykonaniaPrzelewu)
        cmd.oplataZaDostarczeniePapieru = nullify(cmd.oplataZaDostarczeniePapieru)
        cmd.oplataZaZmianeGrafiki = nullify(cmd.oplataZaZmianeGrafiki)
        cmd.oplataZaInstalacjePOS = nullify(cmd.oplataZaInstalacjePOS)
        cmd.oplataZaInstalacjeGPRS = nullify(cmd.oplataZaInstalacjeGPRS)
        cmd.oplataZaUruchomienieWalutyObcej = nullify(cmd.oplataZaUruchomienieWalutyObcej)
    }

    def getDodatkoweUslugi2(ProcessCommand cmd ) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty("OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty("OPLATA_KALKULATOR")
        cmd.tytulPlatnosciCena = nullify(cmd.tytulPlatnosciCena)
        cmd.pierwszaSesjaCena = nullify(cmd.pierwszaSesjaCena)
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd ) {
        cmd.mudCena = nullify(cmd.mudCena)
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd ) {
        cmd.weryfikacjaPINCena = nullify(cmd.weryfikacjaPINCena)
        cmd.systemKasowyCena = nullify(cmd.systemKasowyCena)
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd ) {
    }

    def getFormaDoladowania(ProcessCommand cmd ) {
        cmd.doladowania_tp = calculatorService.getCalcProperty("CZY_TELEPOMPKA")
        cmd.doladowania_tk = calculatorService.getCalcProperty("CZY_TELEKODZIK")

        cmd.srednia_sprzedaz_doladowan = calculatorService.getCalcProperty("DEKLARACJA_SPRZEDAZY_PP")

        cmd.srednia_sprzedaz_doladowan_slownie = nullify(cmd.srednia_sprzedaz_doladowan_slownie)
    }

    def getFunkcjeTerminala(ProcessCommand cmd ) {
    }

    def getIfplus(ProcessCommand cmd ) {
        cmd.ifOplataVISA = calculatorService.getCalcProperty("OPLATA_IFPLUS_VISA")
        cmd.ifOplataMasterCard = calculatorService.getCalcProperty("OPLATA_IFPLUS_MASTERCARD")
        cmd.ifOplataDinersClub = calculatorService.getCalcProperty("OPLATA_IFPLUS_DINERSCLUB")
        cmd.ifOplataIKO = calculatorService.getCalcProperty("OPLATA_IFPLUS_IKO")
        cmd.ifOplataPKOPB = calculatorService.getCalcProperty("OPLATA_IFPLUS_PKOBP")
    }

    def getInformacjeDodatkowe(ProcessCommand cmd ) {
        cmd.dzialalnoscForma = nullify(cmd.dzialalnoscForma)
        cmd.dzialalnoscFormaInna = nullify(cmd.dzialalnoscFormaInna)
        cmd.dzialalnoscDokument = nullify(cmd.dzialalnoscDokument)
        cmd.dzialalnoscDokumentInny = nullify(cmd.dzialalnoscDokumentInny)
    }

    def getInformacjeTechniczne(ProcessCommand cmd ) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd ) {
        cmd.okresLojalnosciowy = calculatorService.getCalcProperty("LICZBA_MIESIECY_LOJ") ?: ""
    }

    def getOpieka(ProcessCommand cmd ) {

    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd ) {
        cmd.oplataZaUruchomienieDCC = nullify(cmd.oplataZaUruchomienieDCC)
    }

    def getOplatyDCC(ProcessCommand cmd ) {
        cmd.oplataZaPlatnoscWInnejWalucie = calculatorService.getCalcProperty("OPLATA_DCC") ?: ""
    }

    def getOsobaDoKontaktu(ProcessCommand cmd ) {
        cmd.kontaktTytul = nullify(cmd.kontaktTytul)
        cmd.kontaktImie = nullify(cmd.kontaktImie)
        cmd.kontaktNazwisko = nullify(cmd.kontaktNazwisko)
        cmd.kontaktTelStacjonarny = nullify(cmd.kontaktTelStacjonarny)
        cmd.kontaktTelKomorkowy = nullify(cmd.kontaktTelKomorkowy)
        cmd.kontaktEmail = nullify(cmd.kontaktEmail)
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd ) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd ) {
        def user = springSecurityService.principal;
        cmd.pozyskujacyTytul = nullify(cmd.pozyskujacyTytul)
        cmd.pozyskujacyImie =  user.imie
        cmd.pozyskujacyNazwisko = user.nazwisko
        cmd.pozyskujacyNumer =  user.nr
    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd ){

        /*  def osoba1 = cbdService.getOsoba1UprawnionaDoPodpisaniaUmowy(cmd.nip);
          cmd.reprezentant1Tytul =  osoba1.tytul
          cmd.reprezentant1Imie = osoba1.imie
          cmd.reprezentant1Nazwisko = osoba1.nazwisko

          def osoba2 = cbdService.getOsoba2UprawnionaDoPodpisaniaUmowy(cmd.nip);
          cmd.reprezentant2Tytul = osoba2.tytul
          cmd.reprezentant2Imie = osoba2.imie
          cmd.reprezentant2Nazwisko = osoba2.nazwisko*/

        cmd.reprezentant1Tytul = nullify(cmd.reprezentant1Tytul)
        cmd.reprezentant1Imie = nullify(cmd.reprezentant1Imie)
        cmd.reprezentant1Nazwisko = nullify(cmd.reprezentant1Nazwisko)
        cmd.reprezentant2Tytul = nullify(cmd.reprezentant2Tytul)
        cmd.reprezentant2Imie = nullify(cmd.reprezentant2Imie)
        cmd.reprezentant2Nazwisko = nullify(cmd.reprezentant2Nazwisko)

    }

    def setAtLeastAs(def data, def calcValue){
        //println("data : ${data} , cal : ${calcValue}")
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

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd ) {
        cmd.visaEUKKOPr = calculatorService.getCalcProperty("OPLATA_MSC_11_PROCENT")
        cmd.visaEUKKOSt = setAtLeastAs(cmd.visaEUKKOSt,calculatorService.getCalcProperty("OPLATA_MSC_11_ZL"))
        cmd.visaEUKDPr = calculatorService.getCalcProperty("OPLATA_MSC_12_PROCENT")
        cmd.visaEUKDSt = setAtLeastAs(cmd.visaEUKDSt,calculatorService.getCalcProperty("OPLATA_MSC_12_ZL"))
        cmd.visaEUKBPr = calculatorService.getCalcProperty("OPLATA_MSC_13_PROCENT")
        cmd.visaEUKBSt = setAtLeastAs(cmd.visaEUKBSt,calculatorService.getCalcProperty("OPLATA_MSC_13_ZL"))

        cmd.visaOutEUKKOPr = calculatorService.getCalcProperty("OPLATA_MSC_21_PROCENT")
        cmd.visaOutEUKKOSt = setAtLeastAs(cmd.visaOutEUKKOSt,calculatorService.getCalcProperty("OPLATA_MSC_21_ZL"))
        cmd.visaOutEUKDPr = calculatorService.getCalcProperty("OPLATA_MSC_22_PROCENT")
        cmd.visaOutEUKDSt = setAtLeastAs(cmd.visaOutEUKDSt,calculatorService.getCalcProperty("OPLATA_MSC_22_ZL"))
        cmd.visaOutEUKBPr = calculatorService.getCalcProperty("OPLATA_MSC_23_PROCENT")
        cmd.visaOutEUKBSt = setAtLeastAs(cmd.visaOutEUKBSt,calculatorService.getCalcProperty("OPLATA_MSC_23_ZL"))

        cmd.visaPolskaKKO1Pr = calculatorService.getCalcProperty("OPLATA_MSC_311_PROCENT")
        cmd.visaPolskaKKO1St = calculatorService.getCalcProperty("OPLATA_MSC_311_ZL")
        cmd.visaPolskaKKO2Pr = calculatorService.getCalcProperty("OPLATA_MSC_312_PROCENT")
        cmd.visaPolskaKKO2St = calculatorService.getCalcProperty("OPLATA_MSC_312_ZL")
        cmd.visaPolskaKD1Pr = calculatorService.getCalcProperty("OPLATA_MSC_321_PROCENT")
        cmd.visaPolskaKD1St = calculatorService.getCalcProperty("OPLATA_MSC_321_ZL")
        cmd.visaPolskaKD2Pr = calculatorService.getCalcProperty("OPLATA_MSC_322_PROCENT")
        cmd.visaPolskaKD2St = calculatorService.getCalcProperty("OPLATA_MSC_322_ZL")
        cmd.visaPolskaKBPr = calculatorService.getCalcProperty("OPLATA_MSC_33_PROCENT")
        cmd.visaPolskaKBSt = setAtLeastAs(cmd.visaPolskaKBSt,calculatorService.getCalcProperty("OPLATA_MSC_33_ZL"))

        cmd.mastercardEUKKPr = calculatorService.getCalcProperty("OPLATA_MSC_41_PROCENT")
        cmd.mastercardEUKKSt = setAtLeastAs(cmd.mastercardEUKKSt,calculatorService.getCalcProperty("OPLATA_MSC_41_ZL"))
        cmd.mastercardEUKDPr = calculatorService.getCalcProperty("OPLATA_MSC_42_PROCENT")
        cmd.mastercardEUKDSt = setAtLeastAs(cmd.mastercardEUKDSt,calculatorService.getCalcProperty("OPLATA_MSC_42_ZL"))
        cmd.mastercardEUKBLPr = calculatorService.getCalcProperty("OPLATA_MSC_43_PROCENT")
        cmd.mastercardEUKBLSt = setAtLeastAs(cmd.mastercardEUKBLSt,calculatorService.getCalcProperty("OPLATA_MSC_43_ZL"))
        cmd.mastercardEUMPr = calculatorService.getCalcProperty("OPLATA_MSC_44_PROCENT")
        cmd.mastercardEUMSt = setAtLeastAs(cmd.mastercardEUMSt,calculatorService.getCalcProperty("OPLATA_MSC_44_ZL"))

        cmd.mastercardOutEUKKPr = calculatorService.getCalcProperty("OPLATA_MSC_51_PROCENT")
        cmd.mastercardOutEUKKSt = setAtLeastAs(cmd.mastercardOutEUKKSt,calculatorService.getCalcProperty("OPLATA_MSC_51_ZL"))
        cmd.mastercardOutEUKDPr = calculatorService.getCalcProperty("OPLATA_MSC_52_PROCENT")
        cmd.mastercardOutEUKDSt = setAtLeastAs(cmd.mastercardOutEUKDSt,calculatorService.getCalcProperty("OPLATA_MSC_52_ZL"))
        cmd.mastercardOutEUKBPr = calculatorService.getCalcProperty("OPLATA_MSC_53_PROCENT")
        cmd.mastercardOutEUKBSt = setAtLeastAs(cmd.mastercardOutEUKBSt,calculatorService.getCalcProperty("OPLATA_MSC_53_ZL"))
        cmd.mastercardOutEUMPr = calculatorService.getCalcProperty("OPLATA_MSC_54_PROCENT")
        cmd.mastercardOutEUMSt = setAtLeastAs(cmd.mastercardOutEUMSt,calculatorService.getCalcProperty("OPLATA_MSC_54_ZL"))

        cmd.mastercardPolskaKK1Pr = calculatorService.getCalcProperty("OPLATA_MSC_611_PROCENT")
        cmd.mastercardPolskaKK1St = calculatorService.getCalcProperty("OPLATA_MSC_611_ZL")
        cmd.mastercardPolskaKK2Pr = calculatorService.getCalcProperty("OPLATA_MSC_612_PROCENT")
        cmd.mastercardPolskaKK2St = calculatorService.getCalcProperty("OPLATA_MSC_612_ZL")
        cmd.mastercardPolskaKK3Pr = calculatorService.getCalcProperty("OPLATA_MSC_613_PROCENT")
        cmd.mastercardPolskaKK3St = calculatorService.getCalcProperty("OPLATA_MSC_613_ZL")
        cmd.mastercardPolskaKD1Pr = calculatorService.getCalcProperty("OPLATA_MSC_621_PROCENT")
        cmd.mastercardPolskaKD1St = calculatorService.getCalcProperty("OPLATA_MSC_621_ZL")
        cmd.mastercardPolskaKD2Pr = calculatorService.getCalcProperty("OPLATA_MSC_622_PROCENT")
        cmd.mastercardPolskaKD2St = calculatorService.getCalcProperty("OPLATA_MSC_622_ZL")
        cmd.mastercardPolskaKD3Pr = calculatorService.getCalcProperty("OPLATA_MSC_623_PROCENT")
        cmd.mastercardPolskaKD3St = calculatorService.getCalcProperty("OPLATA_MSC_623_ZL")
		cmd.mastercardPolskaKBPr = calculatorService.getCalcProperty("OPLATA_MSC_625_PROCENT")
		cmd.mastercardPolskaKBSt = calculatorService.getCalcProperty("OPLATA_MSC_625_ZL")
        cmd.mastercardPolskaM1Pr = calculatorService.getCalcProperty("OPLATA_MSC_641_PROCENT")
        cmd.mastercardPolskaM1St = calculatorService.getCalcProperty("OPLATA_MSC_641_ZL")
        cmd.mastercardPolskaM2Pr = calculatorService.getCalcProperty("OPLATA_MSC_642_PROCENT")
        cmd.mastercardPolskaM2St = calculatorService.getCalcProperty("OPLATA_MSC_642_ZL")
        cmd.mastercardPolskaM3Pr = calculatorService.getCalcProperty("OPLATA_MSC_643_PROCENT")
        cmd.mastercardPolskaM3St = calculatorService.getCalcProperty("OPLATA_MSC_643_ZL")

        cmd.visaPKOBPKKO1Pr = calculatorService.getCalcProperty("OPLATA_MSC_711_PROCENT")
        cmd.visaPKOBPKKO1St = calculatorService.getCalcProperty("OPLATA_MSC_711_ZL")
        cmd.visaPKOBPKKO2Pr = calculatorService.getCalcProperty("OPLATA_MSC_712_PROCENT")
        cmd.visaPKOBPKKO2St = calculatorService.getCalcProperty("OPLATA_MSC_712_ZL")
        cmd.visaPKOBPKD1Pr = calculatorService.getCalcProperty("OPLATA_MSC_721_PROCENT")
        cmd.visaPKOBPKD1St = calculatorService.getCalcProperty("OPLATA_MSC_721_ZL")
        cmd.visaPKOBPKD2Pr = calculatorService.getCalcProperty("OPLATA_MSC_722_PROCENT")
        cmd.visaPKOBPKD2St = calculatorService.getCalcProperty("OPLATA_MSC_722_ZL")
        cmd.visaPKOBPKB3Pr = calculatorService.getCalcProperty("OPLATA_MSC_73_PROCENT")
        cmd.visaPKOBPKB3St = calculatorService.getCalcProperty("OPLATA_MSC_73_ZL")

        cmd.mastercardPKOBPKK1Pr = calculatorService.getCalcProperty("OPLATA_MSC_811_PROCENT")
        cmd.mastercardPKOBPKK1St = calculatorService.getCalcProperty("OPLATA_MSC_811_ZL")
        cmd.mastercardPKOBPKK2Pr = calculatorService.getCalcProperty("OPLATA_MSC_812_PROCENT")
        cmd.mastercardPKOBPKK2St = calculatorService.getCalcProperty("OPLATA_MSC_812_ZL")
        cmd.mastercardPKOBPKK3Pr = calculatorService.getCalcProperty("OPLATA_MSC_813_PROCENT")
        cmd.mastercardPKOBPKK3St = calculatorService.getCalcProperty("OPLATA_MSC_813_ZL")
        cmd.mastercardPKOBPKD1Pr = calculatorService.getCalcProperty("OPLATA_MSC_821_PROCENT")
        cmd.mastercardPKOBPKD1St = calculatorService.getCalcProperty("OPLATA_MSC_821_ZL")
        cmd.mastercardPKOBPKD2LPr = calculatorService.getCalcProperty("OPLATA_MSC_822_PROCENT")
        cmd.mastercardPKOBPKD2LSt = calculatorService.getCalcProperty("OPLATA_MSC_822_ZL")
        cmd.mastercardPKOBPKD3Pr = calculatorService.getCalcProperty("OPLATA_MSC_823_PROCENT")
        cmd.mastercardPKOBPKD3St = calculatorService.getCalcProperty("OPLATA_MSC_823_ZL")
        cmd.mastercardPKOBPKBPr   = calculatorService.getCalcProperty("OPLATA_MSC_83_PROCENT")
        cmd.mastercardPKOBPKBSt   = calculatorService.getCalcProperty("OPLATA_MSC_83_ZL")
        cmd.mastercardPKOBPM1Pr = calculatorService.getCalcProperty("OPLATA_MSC_841_PROCENT")
        cmd.mastercardPKOBPM1St = calculatorService.getCalcProperty("OPLATA_MSC_841_ZL")
        cmd.mastercardPKOBPM2Pr = calculatorService.getCalcProperty("OPLATA_MSC_842_PROCENT")
        cmd.mastercardPKOBPM2St = calculatorService.getCalcProperty("OPLATA_MSC_842_ZL")
        cmd.mastercardPKOBPM3Pr = calculatorService.getCalcProperty("OPLATA_MSC_843_PROCENT")
        cmd.mastercardPKOBPM3St = calculatorService.getCalcProperty("OPLATA_MSC_843_ZL")

        cmd.dinersClubPr = calculatorService.getCalcProperty("OPLATA_MSC_9_PROCENT")

        cmd.ikoSt = calculatorService.getCalcProperty("OPLATA_MSC_10_PROCENT")
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd ) {
        cmd.pp_orange_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_ORANGE")
        cmd.pp_orange_tp = calculatorService.getCalcProperty("STAWKA_TELEPOMPKA_ORANGE")
        cmd.pp_plus_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_PLUS")
        cmd.pp_plus_tp = calculatorService.getCalcProperty("STAWKA_TELEPOMPKA_PLUS")
        cmd.pp_tmobile_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_TAKTAK")
        cmd.pp_tmobile_tp = calculatorService.getCalcProperty("STAWKA_TELEPOMPKA_TAKTAK")
        cmd.pp_heyah_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_HEYAH")
        cmd.pp_heyah_tp = calculatorService.getCalcProperty("STAWKA_TELEPOMPKA_HEYAH")
        cmd.pp_play_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_PLAY")
        cmd.pp_play_tp = calculatorService.getCalcProperty("STAWKA_TELEPOMPKA_PLAY")
        cmd.pp_telegrosik_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_GALENA")
        cmd.pp_virginmobile_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_VIRGIN")
        cmd.pp_lycamobile_tk = calculatorService.getCalcProperty("STAWKA_TELEKODZIK_LYCA")
        cmd.pp_gtmobile_tk =calculatorService.getCalcProperty("STAWKA_TELEKODZIK_GTMOBILE")
        cmd.pp_vectonemobile_tk =  calculatorService.getCalcProperty("STAWKA_TELEKODZIK_VECTOBE")
        cmd.pp_delightmobile_tk =  calculatorService.getCalcProperty("STAWKA_TELEKODZIK_DELIGHT")
        cmd.oplataZaOprogramowanieDoDoladowan = calculatorService.getCalcProperty("OPLATA_ZA_APL_PP")
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd ) {

    }

    def getRachunekBankowyKlienta(ProcessCommand cmd ) {
        cmd.numerRachunkuBankowegoKlienta = nullify(cmd.numerRachunkuBankowegoKlienta)
        cmd.bankKlienta = nullify(cmd.bankKlienta)
    }

    def getScoring(ProcessCommand cmd ) {
        cmd.scoringMcc = calculatorService.getCalcProperty("MCC")
        cmd.scoringDzialalnosc = nullify(cmd.scoringDzialalnosc)

        def result = cbdService.getRodzajDzialalnosciByMCC(cmd.scoringMcc);
        cmd.scoringSzczegolyDzialalnosci = result?.slm_nazwa ?: ""

        cmd.scoringIloscTransakcji = nullify(cmd.scoringIloscTransakcji)
        cmd.scoringCzestoscTransakcji = nullify(cmd.scoringCzestoscTransakcji)
        cmd.scoringOtwartyZamkniety = nullify(cmd.scoringOtwartyZamkniety)
        cmd.scoringStanZadbany = nullify(cmd.scoringStanZadbany)
        cmd.scoringWielkoscMiejscowosci = nullify(cmd.scoringWielkoscMiejscowosci)
        cmd.scoringLokalizacjaPunktu = nullify(cmd.scoringLokalizacjaPunktu)
        cmd.scoringTypPunktu = nullify(cmd.scoringTypPunktu)
        cmd.scoringTypPunktuInny = nullify(cmd.scoringTypPunktuInny)
        cmd.scoringWielkoscPunktu = nullify(cmd.scoringWielkoscPunktu)
        cmd.scoringAkceptacja = nullify(cmd.scoringAkceptacja)
        cmd.scoringMonitoring = nullify(cmd.scoringMonitoring)
        cmd.scoringCharakterystyka = nullify(cmd.scoringCharakterystyka)
        cmd.scoringCharakterystykaInna = nullify(cmd.scoringCharakterystykaInna)
        cmd.scoringKoncesja = nullify(cmd.scoringKoncesja)
        cmd.rodzajZezwolenia = nullify(cmd.rodzajZezwolenia)
        cmd.scoringWlasnosc = nullify(cmd.scoringWlasnosc)
        cmd.scoringDzialalnoscCzas = nullify(cmd.scoringDzialalnoscCzas)
        cmd.scoringSprzedazTowarowEkskluzywnych = nullify(cmd.scoringSprzedazTowarowEkskluzywnych)
        cmd.scoringPonad50ProcentObrotowWNocy = nullify(cmd.scoringPonad50ProcentObrotowWNocy)
        cmd.scoringRuchTurystycznyPrzygraniczny = nullify(cmd.scoringRuchTurystycznyPrzygraniczny)
        cmd.scoringUslugiPlatneZGory = nullify(cmd.scoringUslugiPlatneZGory)
        cmd.scoringDochodowosc = nullify(cmd.scoringDochodowosc)
        cmd.scoringDeklaracjaFinansowa = nullify(cmd.scoringDeklaracjaFinansowa)
        cmd.scoringDeklaracjaFinansowaObrotOgolem = nullify(cmd.scoringDeklaracjaFinansowaObrotOgolem)
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = nullify(cmd.scoringDeklaracjaFinansowaObrotNaKarty)
        cmd.scoringDeklaracjaFinansowaSredniObrot = nullify(cmd.scoringDeklaracjaFinansowaSredniObrot)
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = nullify(cmd.scoringDeklaracjaFinansowaSredniaTransakcja)
    }

    def getSerwis(ProcessCommand cmd ) {

        def SERWIS_TYPES = [[key: "PAKIET_SERWIS_PRESTIZ", value:"prestige"],
                [key: "PAKIET_SERWIS_KOMFORT", value:"comfort"],
                [key: "PAKIET_SERWIS_EKONOMICZNY", value:"economic"]]

        def result = SERWIS_TYPES.find{
            calculatorService.hasCalcProperty(it.key,"TAK")
        }

        cmd.obslugaTyp = result?.value ?: "";
        cmd.obslugaEkonomicznyCena = nullify(cmd.obslugaEkonomicznyCena);
    }

    def getSerwisEkonomiczny(ProcessCommand cmd ) {

    }

    def getSerwisKomfort(ProcessCommand cmd ) {

    }

    def getSerwisPrzestiz(ProcessCommand cmd ) {

    }

    def getSiedzibaAkceptanta(ProcessCommand cmd ) {

        def result = cbdService.getSiedzibaAkceptanta(cmd.nip);

        cmd.akceptantUlicaTytul = result?.typ_ulicy ?: "UL"
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

    def getUmowa2(ProcessCommand cmd ) {
        cmd.miejsceUmowy= nullify(cmd.miejsceUmowy)
    }

    def getUwagi(ProcessCommand cmd ) {
        //EMPTY
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd ) {
       /* cmd.punktyTytulPlatnosci= cmd.punktyTytulPlatnosci?: []
        cmd.punktySystemKasowy = cmd.punktySystemKasowy ?: []
        cmd.punktyUta = cmd.punktyUta ?: []
        cmd.punktyWybrane = cmd.punktyWybrane ?: []*/
    }

    def getZalaczniki(ProcessCommand cmd ) {

    }

    def getZestawPos(ProcessCommand cmd ) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd ) {

        cmd.isZestawPosOdplatneUzywanieShown = nullify(cmd.isZestawPosOdplatneUzywanieShown)
        cmd.oplPOSDialUpTyp = calculatorService.getCalcProperty("TYP_DIALUP") //K RW
        cmd.oplPOSDialUpIlosc = nullify(cmd.oplPOSDialUpIlosc)
        cmd.oplPOSDialUpIloscPP = nullify(cmd.oplPOSDialUpIloscPP)
        cmd.oplPOSDialUpNormalneMies =  setAtLeastAs(cmd.oplPOSDialUpNormalneMies,calculatorService.getCalcProperty("TYP_DIALUP_TERM_CENA"))
        cmd.oplPOSDialUpNormalnePP =  setAtLeastAs(cmd.oplPOSDialUpNormalnePP,calculatorService.getCalcProperty("TYP_DIALUP_PP_CENA"))
        cmd.oplPOSDialUpPreferencyjneMies =  setAtLeastAs(cmd.oplPOSDialUpPreferencyjneMies,calculatorService.getCalcProperty("TYP_DIALUP_TERM_CENA"))
        cmd.oplPOSDialUpPreferencyjnePP =  setAtLeastAs(cmd.oplPOSDialUpPreferencyjnePP,calculatorService.getCalcProperty("TYP_DIALUP_PP_CENA"))

        cmd.oplPOSVPNTyp = calculatorService.getCalcProperty("TYP_VPN") //K RW
        cmd.oplPOSVPNIlosc =  nullify(cmd.oplPOSVPNIlosc)
        cmd.oplPOSVPNIloscPP =  nullify(cmd.oplPOSVPNIloscPP)
        cmd.oplPOSVPNNormalneMies =  setAtLeastAs(cmd.oplPOSVPNNormalneMies ,calculatorService.getCalcProperty("TYP_VPN_TERM_CENA"))
        cmd.oplPOSVPNNormalnePP =  setAtLeastAs(cmd.oplPOSVPNNormalnePP ,calculatorService.getCalcProperty("TYP_VPN_PP_CENA"))
        cmd.oplPOSVPNPreferencyjneMies =  setAtLeastAs(cmd.oplPOSVPNPreferencyjneMies ,calculatorService.getCalcProperty("TYP_VPN_TERM_CENA"))
        cmd.oplPOSVPNPreferencyjnePP =  setAtLeastAs(cmd.oplPOSVPNPreferencyjnePP ,calculatorService.getCalcProperty("TYP_VPN_PP_CENA"))

        cmd.oplPOSSSLTyp = calculatorService.getCalcProperty("TYP_SSL") //K RW
        cmd.oplPOSSSLIlosc =  nullify(cmd.oplPOSSSLIlosc)
        cmd.oplPOSSSLIloscPP =  nullify(cmd.oplPOSSSLIloscPP)
        cmd.oplPOSSSLNormalneMies =  setAtLeastAs(cmd.oplPOSSSLNormalneMies ,calculatorService.getCalcProperty("TYP_SSL_TERM_CENA"))
        cmd.oplPOSSSLNormalnePP =  setAtLeastAs(cmd.oplPOSSSLNormalnePP ,calculatorService.getCalcProperty("TYP_SSL_PP_CENA"))
        cmd.oplPOSSSLPreferencyjneMies =  setAtLeastAs(cmd.oplPOSSSLPreferencyjneMies ,calculatorService.getCalcProperty("TYP_SSL_TERM_CENA"))
        cmd.oplPOSSSLPreferencyjnePP =  setAtLeastAs(cmd.oplPOSSSLPreferencyjnePP ,calculatorService.getCalcProperty("TYP_SSL_PP_CENA"))

        cmd.oplPOSWiFiTyp = calculatorService.getCalcProperty("TYP_WIFI") //K RW
        cmd.oplPOSWiFiIlosc =  nullify(cmd.oplPOSWiFiIlosc)
        cmd.oplPOSWiFiIloscPP = nullify(cmd.oplPOSWiFiIloscPP)
        cmd.oplPOSWiFiNormalneMies =  setAtLeastAs(cmd.oplPOSWiFiNormalneMies ,calculatorService.getCalcProperty("TYP_WIFI_TERM_CENA"))
        cmd.oplPOSWiFiNormalnePP =  setAtLeastAs(cmd.oplPOSWiFiNormalnePP ,calculatorService.getCalcProperty("TYP_WIFI_PP_CENA"))
        cmd.oplPOSWiFiPreferencyjneMies =  setAtLeastAs(cmd.oplPOSWiFiPreferencyjneMies ,calculatorService.getCalcProperty("TYP_WIFI_TERM_CENA"))
        cmd.oplPOSWiFiPreferencyjnePP =  setAtLeastAs(cmd.oplPOSWiFiPreferencyjnePP ,calculatorService.getCalcProperty("TYP_WIFI_PP_CENA"))

        cmd.oplPOSGPRSTyp = calculatorService.getCalcProperty("TYP_GPRS") //K RW
        cmd.oplPOSGPRSIlosc =  nullify(cmd.oplPOSGPRSIlosc)
        cmd.oplPOSGPRSIloscPP =  nullify(cmd.oplPOSGPRSIloscPP)
        cmd.oplPOSGPRSNormalneMies =  setAtLeastAs(cmd.oplPOSGPRSNormalneMies ,calculatorService.getCalcProperty("TYP_SSL_TERM_CENA"))
        cmd.oplPOSGPRSNormalnePP =  setAtLeastAs(cmd.oplPOSGPRSNormalnePP ,calculatorService.getCalcProperty("TYP_GPRS_PP_CENA"))
        cmd.oplPOSGPRSPreferencyjneMies =  setAtLeastAs(cmd.oplPOSGPRSPreferencyjneMies ,calculatorService.getCalcProperty("TYP_SSL_TERM_CENA"))
        cmd.oplPOSGPRSPreferencyjnePP =  setAtLeastAs(cmd.oplPOSGPRSPreferencyjnePP ,calculatorService.getCalcProperty("TYP_GPRS_PP_CENA"))

        cmd.oplPOSBaza = nullify(cmd.oplPOSBaza)

    }

    def nullify(def value){
        value != ProcessCommand.DEFAULT_VALUE ? value : ""
    }
}