package com.eservice.eumowy

import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

class PanelService {

    def cbdService
    def calculatorService
    def springSecurityService
    //domainClass
    //def calcMethods = ["getDcc","getDodatkoweUslugi2","getFormaDoladowania","getIfplus","","","","","","","","","","","",]

    def cbdMethods = ["getAdresDoKorespondencjizAkecptantem","getDaneAkceptanta","getSiedzibaAkceptanta","getSerwis"]

    def init(ProcessCommand cmd, def calc){
        cmd.scoringMcc = calculatorService.getCalcProperty(calc,"MCC")

        cmd.isDoladowania_tp = calculatorService.getCalcProperty(calc,"CZY_TELEPOMPKA")
        cmd.isDoladowania_tk = calculatorService.getCalcProperty(calc,"CZY_TELEKODZIK")
        cmd.doladowania_tp = nullify(cmd.doladowania_tp)
        cmd.doladowania_tk = nullify(cmd.doladowania_tk)
        cmd.czyRozszerzenie = cmd.process?.activities?.any{it.code.equals('dodatkowyPunkt')} || cmd.process?.activities?.any{it.code.equals('dodatkowyPos')}

        cmd.liczbaTerminali = calculatorService.getCalcProperty(calc,"LICZBA_POS_MAX")
    }

    def getAdresacjaSeciowa(ProcessCommand cmd, def calc ) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd, def calc ) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd, def calc ) {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(cmd.nip);

        cmd.akceptantKontaktUlicaTytul = result?.typ_ulicy ?:  "UL"
        cmd.akceptantKontaktUlica = result?.ulica  ?: ""
        cmd.akceptantKontaktNrDomu = result?.nr_budynku ?: ""
        cmd.akceptantKontaktNrMieszkania = result?.nr_lokal ?: ""
        cmd.akceptantKontaktMiasto = result?.miejscowosc ?: ""
        cmd.akceptantKontaktKodPocztowy = result?.kod_pocztowy ?: ""
        cmd.akceptantKontaktPoczta = result?.poczta ?: ""
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd, def calc ) {
        def result = cbdService.getAneksDoUmowyNajmuZestawuPos(cmd.nip)
        cmd.dataAneksowanejUmowyPos =  DateUtils.getFormattedDate(result?.dataAneksowanejUmowyPos ?: "")
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd, def calc ) {
        def result = cbdService.getAneksDoUmowyPrepaid(cmd.nip)
        cmd.dataAneksowanejUmowyPrepaid = DateUtils.getFormattedDate(result?.dataAneksowanejUmowyPrepaid ?: "")
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd, def calc ) {
        cmd.umowaCzas = nullify(cmd.umowaCzas)
        cmd.umowaOznOd = nullify(cmd.umowaOznOd)
        cmd.umowaOznDo = nullify(cmd.umowaOznDo)
    }

    def getDaneAkceptanta(ProcessCommand cmd, def calc ) {
        def result = cbdService.getDaneAkceptanta(cmd.nip);

        cmd.akceptantNazwaOficjalna = result?.nazwa ?: ""
        cmd.akceptantNazwaSieciowa = nullify(cmd.akceptantNazwaSieciowa)
        cmd.akceptantRegon = result?.regon ?: ""

        cmd.akceptantNazwaOficjalnaCbd= cmd.akceptantNazwaOficjalna
        cmd.akceptantNazwaSieciowaCbd= cmd.akceptantNazwaSieciowa
        cmd.akceptantRegonCbd = cmd.akceptantRegon
    }

    def getDaneDoWydruku(ProcessCommand cmd, def calc ) {
        cmd.nazwaDoWydrukuZTerminalaPos = nullify(cmd.nazwaDoWydrukuZTerminalaPos)
        cmd.wydrukNazwaDoWyszukwarki = nullify(cmd.wydrukNazwaDoWyszukwarki)
        cmd.wydrukUlicaTytul = nullify(cmd.wydrukUlicaTytul, "UL")
        cmd.wydrukUlica = nullify(cmd.wydrukUlica)
        cmd.wydrukNrDomu = nullify(cmd.wydrukNrDomu)
        cmd.wydrukNrMieszkania = nullify(cmd.wydrukNrMieszkania)
        cmd.wydrukMiasto = nullify(cmd.wydrukMiasto)
        cmd.wydrukKodPocztowy = nullify(cmd.wydrukKodPocztowy)
        cmd.wydrukPoczta = nullify(cmd.wydrukPoczta)
        cmd.wydrukLinia1 = nullify(cmd.wydrukLinia1)
        cmd.wydrukLinia2 = nullify(cmd.wydrukLinia2)
    }

    def getDanePos(ProcessCommand cmd, def calc ) {

    }

    def getDanePunktu(ProcessCommand cmd, def calc ) {

    }

    def getDcc(ProcessCommand cmd, def calc ) {
        cmd.oplataVISA = calculatorService.getCalcProperty(calc,"OPLATA_DCC_VISA_ZL")
        cmd.oplataVISAPr =calculatorService.getCalcProperty(calc,"OPLATA_DCC_VISA_PR")
        cmd.oplataMasterCard = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MASTERCARD_ZL")
        cmd.oplataMasterCardPr = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MASTERCARD_PR")
        cmd.oplataMaestro = calculatorService.getCalcProperty(calc,"OPLATA_DCC_MAESTRO_ZL")
        cmd.oplataMaestroPr =calculatorService.getCalcProperty(calc,"OPLATA_DCC_MAESTRO_PR")
    }

    def getDccZakresUruchomienia(ProcessCommand cmd, def calc ) {
        cmd.dccZakresUruchomienia = nullify(cmd.dccZakresUruchomienia)
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd, def calc ) {
        cmd.informacjaHandlowa = nullify(cmd.informacjaHandlowa)
    }

    def getDodajPos(ProcessCommand cmd, def calc ) {
		def pointData = new PointCommand()
		
		pointData.dialupTyp = calculatorService.getCalcProperty(calc,"TYP_DIALUP") //K RW
		pointData.dialupCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_DIALUP_TERM_CENA"))
		pointData.dialupPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_DIALUP_PP_CENA"))

		pointData.vpnTyp = calculatorService.getCalcProperty(calc,"TYP_VPN") //K RW
		pointData.vpnCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_VPN_TERM_CENA"))
		pointData.vpnPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_VPN_PP_CENA"))

		pointData.sslTyp = calculatorService.getCalcProperty(calc,"TYP_SSL") //K RW
		pointData.sslCena = toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
		pointData.sslPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_PP_CENA"))

		pointData.wifiTyp = calculatorService.getCalcProperty(calc,"TYP_WIFI") //K RW
		pointData.wifiCena = toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_WIFI_TERM_CENA"))
		pointData.wifiPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_WIFI_PP_CENA"))

		pointData.gprsTyp = calculatorService.getCalcProperty(calc,"TYP_GPRS") //K RW
		pointData.gprsCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
		pointData.gprsPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_GPRS_PP_CENA"))

		cmd.defaultPosData = pointData
        cmd.czyGift = cbdService.czyGift(cmd.nip)
    }

    def getDodajPunkt(ProcessCommand cmd, def calc ) {
        def pointData = new PointCommand()

        pointData.dialupTyp = calculatorService.getCalcProperty(calc,"TYP_DIALUP") //K RW
        pointData.dialupCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_DIALUP_TERM_CENA"))
        pointData.dialupPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_DIALUP_PP_CENA"))

        pointData.vpnTyp = calculatorService.getCalcProperty(calc,"TYP_VPN") //K RW
        pointData.vpnCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_VPN_TERM_CENA"))
        pointData.vpnPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_VPN_PP_CENA"))

        pointData.sslTyp = calculatorService.getCalcProperty(calc,"TYP_SSL") //K RW
        pointData.sslCena = toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        pointData.sslPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_PP_CENA"))

        pointData.wifiTyp = calculatorService.getCalcProperty(calc,"TYP_WIFI") //K RW
        pointData.wifiCena = toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_WIFI_TERM_CENA"))
        pointData.wifiPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_WIFI_PP_CENA"))

        pointData.gprsTyp = calculatorService.getCalcProperty(calc,"TYP_GPRS") //K RW
        pointData.gprsCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        pointData.gprsPPCena =  toBigDecimal(calculatorService.getCalcProperty(calc,"TYP_GPRS_PP_CENA"))

        cmd.defaultPointData = pointData
        cmd.czyGift = cbdService.czyGift(cmd.nip)
    }

    def getDodatkoweUslugi(ProcessCommand cmd, def calc ) {
        cmd.oplataZaDzienneZestawienieTransakcji = nullify(cmd.oplataZaDzienneZestawienieTransakcji)
        cmd.oplataZaMiesieczneZestawienieTransakcji = nullify(cmd.oplataZaMiesieczneZestawienieTransakcji)
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu = nullify(cmd.oplataZaPotwierdzenieWykonaniaPrzelewu)
        cmd.oplataZaDostarczeniePapieru = nullify(cmd.oplataZaDostarczeniePapieru)
        cmd.oplataZaZmianeGrafiki = nullify(cmd.oplataZaZmianeGrafiki)
        cmd.oplataZaInstalacjePOS = nullify(cmd.oplataZaInstalacjePOS)
        cmd.oplataZaInstalacjeGPRS = nullify(cmd.oplataZaInstalacjeGPRS)
        cmd.oplataZaUruchomienieWalutyObcej = nullify(cmd.oplataZaUruchomienieWalutyObcej)
    }

    def getDodatkoweUslugi2(ProcessCommand cmd, def calc ) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty(calc,"OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty(calc,"OPLATA_KALKULATOR")
        cmd.pierwszaSesjaCena = nullify(cmd.pierwszaSesjaCena)
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd, def calc ) {
        cmd.mudCena = nullify(cmd.mudCena)
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd, def calc ) {
        cmd.weryfikacjaPINCena = nullify(cmd.weryfikacjaPINCena)
        cmd.systemKasowyCena = nullify(cmd.systemKasowyCena)
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd, def calc ) {
    }

    def getFormaDoladowania(ProcessCommand cmd, def calc ) {
        cmd.srednia_sprzedaz_doladowan = calculatorService.getCalcProperty(calc,"DEKLARACJA_SPRZEDAZY_PP")
        cmd.srednia_sprzedaz_doladowan_slownie = nullify(cmd.srednia_sprzedaz_doladowan_slownie)
    }

    def getFormaDoladowaniaOrazWartosciUpustow(ProcessCommand cmd, def calc){
        cmd.srednia_sprzedaz_doladowan = calculatorService.getCalcProperty(calc,"DEKLARACJA_SPRZEDAZY_PP")
        cmd.srednia_sprzedaz_doladowan_slownie = nullify(cmd.srednia_sprzedaz_doladowan_slownie)

        cmd.pp_orange_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_ORANGE")
        cmd.pp_orange_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_ORANGE")
        cmd.pp_plus_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_PLUS")
        cmd.pp_plus_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_PLUS")
        cmd.pp_tmobile_tk = calculatorService.getCalcProperty(calc,"STAWKA_TELEKODZIK_TAKTAK")
        cmd.pp_tmobile_tp = calculatorService.getCalcProperty(calc,"STAWKA_TELEPOMPKA_TAKTAK")
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

    def getFunkcjeTerminala(ProcessCommand cmd, def calc ) {
        cmd.czyGift = cbdService.czyGift(cmd.nip)
    }

    def getIfplus(ProcessCommand cmd, def calc ) {
        cmd.ifOplataVISA = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_VISA")
        cmd.ifOplataMasterCard = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_MASTERCARD")
        cmd.ifOplataDinersClub = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_DINERSCLUB")
        cmd.ifOplataIKO = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_IKO")
        cmd.ifOplataPKOPB = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_PKOBP")
    }

    def getInformacjeDodatkowe(ProcessCommand cmd, def calc ) {
        cmd.dzialalnoscForma = nullify(cmd.dzialalnoscForma)
        cmd.dzialalnoscFormaInna = nullify(cmd.dzialalnoscFormaInna)
        cmd.dzialalnoscDokument = nullify(cmd.dzialalnoscDokument)
        cmd.dzialalnoscDokumentInny = nullify(cmd.dzialalnoscDokumentInny)
    }

    def getInformacjeTechniczne(ProcessCommand cmd, def calc ) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd, def calc ) {
        cmd.okresLojalnosciowy = calculatorService.getCalcProperty(calc,"LICZBA_MIESIECY_LOJ") ?: ""
    }

    def getOpieka(ProcessCommand cmd, def calc ) {

    }

    def setCzyDcc(ProcessCommand cmd, def calc){
        cmd.czyDcc = "TAK".equalsIgnoreCase(calculatorService.getCalcProperty(calc,"CZY_DCC")) ? true : false
    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd, def calc ) {
        setCzyDcc(cmd,calc)
        cmd.oplataZaUruchomienieDCC = cmd.czyDcc ? nullify(cmd.oplataZaUruchomienieDCC) : "-"
    }

    def getOplatyDCC(ProcessCommand cmd, def calc ) {
        setCzyDcc(cmd,calc)
        if (! cmd.czyDcc && ! calculatorService.getCalcProperty(calc,"OPLATA_DCC")){
            cmd.oplataZaPlatnoscWInnejWalucie = "-"
        }
        else {
            cmd.oplataZaPlatnoscWInnejWalucie = calculatorService.getCalcProperty(calc,"OPLATA_DCC") ?: ""
        }
    }

    def getOsobaDoKontaktu(ProcessCommand cmd, def calc ) {
        cmd.kontaktTytul = nullify(cmd.kontaktTytul)
        cmd.kontaktImie = nullify(cmd.kontaktImie)
        cmd.kontaktNazwisko = nullify(cmd.kontaktNazwisko)
        cmd.kontaktTelStacjonarny = nullify(cmd.kontaktTelStacjonarny)
        cmd.kontaktTelKomorkowy = nullify(cmd.kontaktTelKomorkowy)
        cmd.kontaktEmail = nullify(cmd.kontaktEmail)
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd, def calc ) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd, def calc ) {
        def user = springSecurityService.principal;
        cmd.pozyskujacyTytul = nullify(cmd.pozyskujacyTytul)
        cmd.pozyskujacyImie =  user.imie
        cmd.pozyskujacyNazwisko = user.nazwisko
        cmd.pozyskujacyNumer =  user.nr
    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd, def calc ){

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

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd, def calc ) {
        cmd.visaEUKKOPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_PROCENT")
        cmd.visaEUKKOSt = setAtLeastAs(cmd.visaEUKKOSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_ZL"))
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
        cmd.mastercardPolskaKBPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_63_PROCENT")
        cmd.mastercardPolskaKBSt = calculatorService.getCalcProperty(calc,"OPLATA_MSC_63_ZL")
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

        cmd.ikoPr = calculatorService.getCalcProperty(calc,"OPLATA_MSC_10_PROCENT")
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd, def calc ) {
        getFormaDoladowaniaOrazWartosciUpustow(cmd, calc)
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd, def calc ) {
    }

    def getRachunekBankowyKlienta(ProcessCommand cmd, def calc ) {
        cmd.numerRachunkuBankowegoKlienta = nullify(cmd.numerRachunkuBankowegoKlienta)
        cmd.bankKlienta = nullify(cmd.bankKlienta)
    }

    def getScoring(ProcessCommand cmd, def calc ) {
        cmd.scoringDzialalnosc = nullify(cmd.scoringDzialalnosc)

        //scoringMcc pobierany jest globalnie w metodzie init()
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

        cmd.scoringDeklaracjaFinansowa = nullify(cmd.scoringDeklaracjaFinansowa)

        cmd.scoringDochodowosc = calculatorService.getCalcProperty(calc,"DOCHODOWOSC") ?: 0;
        cmd.scoringDeklaracjaFinansowaObrotOgolem = calculatorService.getCalcProperty(calc,"OBROT_OGOLEM") ?: 0;
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = calculatorService.getCalcProperty(calc,"OBROT_MIESIECZNY") ?: 0;
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = calculatorService.getCalcProperty(calc,"WARTOSC_SREDNIA") ?: 0;

        cmd.progrnozaMiesieczna =  calculatorService.getCalcProperty(calc,"PROGNOZA_MIESIECZNA") ?: 0;
        cmd.liczbaPtkCbd = calculatorService.getCalcProperty(calc,"LICZBA_PUNKTOW_CBD") ?: 0;

//        cmd.scoringDeklaracjaFinansowaSredniObrot = Double.valueOf(progrnozaMies) / (Double.valueOf(liczbaPtkCbd) + 0)


        //PROGNOZA_MIESIECZNA/(LICZBA_PUNKTOW_CBD + N)
    }

    def getSerwis(ProcessCommand cmd, def calc ) {

        def SERWIS_TYPES = [[key: "PAKIET_SERWIS_PRESTIZ", value:"prestige"],
                [key: "PAKIET_SERWIS_KOMFORT", value:"comfort"],
                [key: "PAKIET_SERWIS_EKONOMICZNY", value:"economic"]]

        def result = SERWIS_TYPES.find{
            calculatorService.hasCalcProperty(it.key,"TAK",calc)
        }

        setSerwisZablokowany(cmd, calc, SERWIS_TYPES)

        cmd.obslugaTyp = result?.value ?: "";
        //serwis ekonomiczny zaczytujemy w dwoch panelach
        cmd.obslugaEkonomicznyCena = nullify(cmd.obslugaEkonomicznyCena, "0");
    }

    def getSerwisEkonomiczny(ProcessCommand cmd, def calc ) {
        //serwis ekonomiczny zaczytujemy w dwoch panelach
        cmd.obslugaEkonomicznyCena = nullify(cmd.obslugaEkonomicznyCena, "0");
    }

    def getSerwisKomfort(ProcessCommand cmd, def calc ) {

    }

    def getSerwisPrzestiz(ProcessCommand cmd, def calc ) {

    }

    def getSiedzibaAkceptanta(ProcessCommand cmd, def calc ) {

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

    def getUmowa2(ProcessCommand cmd, def calc ) {
        cmd.miejsceUmowy= nullify(cmd.miejsceUmowy)
    }

    def getUwagi(ProcessCommand cmd, def calc ) {
        //EMPTY
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd, def calc ) {
        //EMPTY
    }

    def getZalaczniki(ProcessCommand cmd, def calc ) {

    }

    def getZestawPos(ProcessCommand cmd, def calc ) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd, def calc ) {
        cmd.isZestawPosOdplatneUzywanieShown = nullify(cmd.isZestawPosOdplatneUzywanieShown)
        cmd.oplPOSDialUpTyp = calculatorService.getCalcProperty(calc,"TYP_DIALUP") //K RW
        cmd.oplPOSDialUpIlosc = nullify(cmd.oplPOSDialUpIlosc)
        cmd.oplPOSDialUpIloscPP = nullify(cmd.oplPOSDialUpIloscPP)
        cmd.oplPOSDialUpNormalneMies =  setAtLeastAs(cmd.oplPOSDialUpNormalneMies,calculatorService.getCalcProperty(calc,"TYP_DIALUP_TERM_CENA"))
        cmd.oplPOSDialUpNormalnePP =  setAtLeastAs(cmd.oplPOSDialUpNormalnePP,calculatorService.getCalcProperty(calc,"TYP_DIALUP_PP_CENA"))
        cmd.oplPOSDialUpPreferencyjneMies =  setAtLeastAs(cmd.oplPOSDialUpPreferencyjneMies,calculatorService.getCalcProperty(calc,"TYP_DIALUP_TERM_CENA"))
        cmd.oplPOSDialUpPreferencyjnePP =  setAtLeastAs(cmd.oplPOSDialUpPreferencyjnePP,calculatorService.getCalcProperty(calc,"TYP_DIALUP_PP_CENA"))

        cmd.oplPOSVPNTyp = calculatorService.getCalcProperty(calc,"TYP_VPN") //K RW
        cmd.oplPOSVPNIlosc =  nullify(cmd.oplPOSVPNIlosc)
        cmd.oplPOSVPNIloscPP =  nullify(cmd.oplPOSVPNIloscPP)
        cmd.oplPOSVPNNormalneMies =  setAtLeastAs(cmd.oplPOSVPNNormalneMies ,calculatorService.getCalcProperty(calc,"TYP_VPN_TERM_CENA"))
        cmd.oplPOSVPNNormalnePP =  setAtLeastAs(cmd.oplPOSVPNNormalnePP ,calculatorService.getCalcProperty(calc,"TYP_VPN_PP_CENA"))
        cmd.oplPOSVPNPreferencyjneMies =  setAtLeastAs(cmd.oplPOSVPNPreferencyjneMies ,calculatorService.getCalcProperty(calc,"TYP_VPN_TERM_CENA"))
        cmd.oplPOSVPNPreferencyjnePP =  setAtLeastAs(cmd.oplPOSVPNPreferencyjnePP ,calculatorService.getCalcProperty(calc,"TYP_VPN_PP_CENA"))

        cmd.oplPOSSSLTyp = calculatorService.getCalcProperty(calc,"TYP_SSL") //K RW
        cmd.oplPOSSSLIlosc =  nullify(cmd.oplPOSSSLIlosc)
        cmd.oplPOSSSLIloscPP =  nullify(cmd.oplPOSSSLIloscPP)
        cmd.oplPOSSSLNormalneMies =  setAtLeastAs(cmd.oplPOSSSLNormalneMies ,calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        cmd.oplPOSSSLNormalnePP =  setAtLeastAs(cmd.oplPOSSSLNormalnePP ,calculatorService.getCalcProperty(calc,"TYP_SSL_PP_CENA"))
        cmd.oplPOSSSLPreferencyjneMies =  setAtLeastAs(cmd.oplPOSSSLPreferencyjneMies ,calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        cmd.oplPOSSSLPreferencyjnePP =  setAtLeastAs(cmd.oplPOSSSLPreferencyjnePP ,calculatorService.getCalcProperty(calc,"TYP_SSL_PP_CENA"))

        cmd.oplPOSWiFiTyp = calculatorService.getCalcProperty(calc,"TYP_WIFI") //K RW
        cmd.oplPOSWiFiIlosc =  nullify(cmd.oplPOSWiFiIlosc)
        cmd.oplPOSWiFiIloscPP = nullify(cmd.oplPOSWiFiIloscPP)
        cmd.oplPOSWiFiNormalneMies =  setAtLeastAs(cmd.oplPOSWiFiNormalneMies ,calculatorService.getCalcProperty(calc,"TYP_WIFI_TERM_CENA"))
        cmd.oplPOSWiFiNormalnePP =  setAtLeastAs(cmd.oplPOSWiFiNormalnePP ,calculatorService.getCalcProperty(calc,"TYP_WIFI_PP_CENA"))
        cmd.oplPOSWiFiPreferencyjneMies =  setAtLeastAs(cmd.oplPOSWiFiPreferencyjneMies ,calculatorService.getCalcProperty(calc,"TYP_WIFI_TERM_CENA"))
        cmd.oplPOSWiFiPreferencyjnePP =  setAtLeastAs(cmd.oplPOSWiFiPreferencyjnePP ,calculatorService.getCalcProperty(calc,"TYP_WIFI_PP_CENA"))

        cmd.oplPOSGPRSTyp = calculatorService.getCalcProperty(calc,"TYP_GPRS") //K RW
        cmd.oplPOSGPRSIlosc =  nullify(cmd.oplPOSGPRSIlosc)
        cmd.oplPOSGPRSIloscPP =  nullify(cmd.oplPOSGPRSIloscPP)
        cmd.oplPOSGPRSNormalneMies =  setAtLeastAs(cmd.oplPOSGPRSNormalneMies ,calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        cmd.oplPOSGPRSNormalnePP =  setAtLeastAs(cmd.oplPOSGPRSNormalnePP ,calculatorService.getCalcProperty(calc,"TYP_GPRS_PP_CENA"))
        cmd.oplPOSGPRSPreferencyjneMies =  setAtLeastAs(cmd.oplPOSGPRSPreferencyjneMies ,calculatorService.getCalcProperty(calc,"TYP_SSL_TERM_CENA"))
        cmd.oplPOSGPRSPreferencyjnePP =  setAtLeastAs(cmd.oplPOSGPRSPreferencyjnePP ,calculatorService.getCalcProperty(calc,"TYP_GPRS_PP_CENA"))

        cmd.oplPOSBaza = nullify(cmd.oplPOSBaza)
    }

    def nullify(def value){
        nullify(value, "")
    }

    def nullify(def value, def defaultValue){
        value != ProcessCommand.DEFAULT_VALUE ? value : defaultValue
    }

    def toBigDecimal(def value){
        value ? new BigDecimal(value) : null
    }

    private setSerwisZablokowany(ProcessCommand cmd, def calc, def serwisy){
        def results = serwisy.findAll{
            calculatorService.hasCalcProperty(it.key,"BRAK",calc)
        }

        cmd.serwisZablokowany = results.size() == 3
    }
}