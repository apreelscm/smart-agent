package com.eservice.eumowy

import com.eservice.eumowy.command.AllPosCommand
import com.eservice.eumowy.command.PointCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

import static com.eservice.eumowy.ActivityHelper.*
import static com.google.common.collect.Lists.newArrayList

class PanelService {

    def cbdService
    def calculatorService
    def springSecurityService

    def init(ProcessCommand cmd, def calc){
        cmd.scoringMcc = calculatorService.getCalcProperty(calc,"MCC")

        cmd.isDoladowania_tp = calculatorService.getCalcProperty(calc,"CZY_TELEPOMPKA")
        cmd.isDoladowania_tk = calculatorService.getCalcProperty(calc,"CZY_TELEKODZIK")
        cmd.doladowania_tp = nullify(cmd.doladowania_tp)
        cmd.doladowania_tk = nullify(cmd.doladowania_tk)
        cmd.isRozszerzenie = contains(cmd.process, DODATKOWY_PUNKT) || contains(cmd.process, DODATKOWY_POS)
        cmd.isOnlyRozszerzenie = containsOnly(cmd.process, newArrayList(DODATKOWY_PUNKT)) ||
                containsOnly(cmd.process, newArrayList(DODATKOWY_POS)) ||
                containsOnly(cmd.process, newArrayList(DODATKOWY_POS, DODATKOWY_PUNKT))
        cmd.hasPrepaid = cbdService.getPrepaidEvoucher(cmd.nip) || cbdService.getPrepaidTopup(cmd.nip)
        cmd.hasDodaniePrepaid = contains(cmd.process, 'dodaniePrepaid')
        cmd.hasNewUmowa = isNewAgreement(cmd.process)
        cmd.hasNewUmowaAndPrepaid = isNewAgreement(cmd.process) && cmd.hasDodaniePrepaid
        cmd.isBundleActivity = isBundleActivity(cmd.process)
        cmd.promObjNaj1 = calculatorService.getCalcProperty(calc,"E_PROM_OBN_NAJ_1")

        if (SignatureHelper.containsAtLeastOne(cmd.process, newArrayList(SignatureName.APUW.currentVersion)) &&
            !contains(cmd.process, WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY)) {
            def val = calculatorService.getCalcProperty(calc,"E_MIES_NAL_OPL_NAJ")
            cmd.promObjNaj1 = val ? val : 1
        }

        cmd.promObjNajLiczbaTerminali = calculatorService.getCalcProperty(calc,"LICZBA_ZEST_POS_PROM_CEN_NAJ_1")

        if(isOnlyActivity(cmd.process, DODATKOWY_PUNKT) || isOnlyActivity(cmd.process, DODATKOWY_POS)) {
            cmd.minCenaNajmu = cbdService.getMinRentPrice(cmd.nip)
        }

        cmd.liczbaTerminali = calculatorService.getCalcProperty(calc,"LICZBA_POS_MAX") ?: 0
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
        cmd.czyAneks = "TAK".equals(calculatorService.getCalcProperty(calc, "S_DATA_PODPISANIA_ANEKSU"))
        cmd.liczbaMiesiecyLojalnosciowych = calculatorService.getCalcProperty(calc, "E_LICZBA_MIESIECY_LOJ")
    }

    def getDaneAkceptanta(ProcessCommand cmd, def calc ) {
        def result = cbdService.getDaneAkceptanta(cmd.nip);

        cmd.isAcceptorDataChanged = nullify(cmd.isAcceptorDataChanged, false)

        cmd.akceptantNazwaOficjalna = result?.nazwa ?: ""
        cmd.akceptantNazwaSieciowa = calculatorService.getCalcProperty(calc, "S_POROZUMIENIA")
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
        cmd.dccKartyZagranicznePr = calculatorService.getDecimalCalcProperty(calc, "OPLATA_DCC_MASTERCARD_PR")
    }

    def getUpustDcc(ProcessCommand cmd, def calc ) {
        cmd.dccKartyZagranicznePr = calculatorService.getDecimalCalcProperty(calc, "DCC_UPUST")
    }

    def getDccZakresUruchomienia(ProcessCommand cmd, def calc ) {
        cmd.dccZakresUruchomienia = nullify(cmd.dccZakresUruchomienia)
    }

    def getListaAktywnychPunktow(ProcessCommand cmd, def calc ) {
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd, def calc ) {
        cmd.informacjaHandlowa = nullify(cmd.informacjaHandlowa)
    }

    def getDodajPos(ProcessCommand cmd, def calc ) {
		PointCommand pointData = new PointCommand()
		
		pointData.calc = calc
		
		pointData.dialupTyp = calculatorService.getCalcProperty(calc,"TYP_DIALUP")
		pointData.dialupCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_TERM_CENA")
		pointData.dialupPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_PP_CENA")

		pointData.vpnTyp = calculatorService.getCalcProperty(calc,"TYP_VPN")
		pointData.vpnCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_TERM_CENA")
		pointData.vpnPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_PP_CENA")

		pointData.sslTyp = calculatorService.getCalcProperty(calc,"TYP_SSL")
		pointData.sslCena = calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_TERM_CENA")
		pointData.sslPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_PP_CENA")

		pointData.gprsTyp = calculatorService.getCalcProperty(calc,"TYP_GPRS")
		pointData.gprsCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA")
		pointData.gprsPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_PP_CENA")

        pointData.gprsTypPortable = calculatorService.getCalcProperty(calc,"TYP_GPRS")
        pointData.gprsCenaPortable =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA")

        pointData.hasDodaniePrepaid = cmd.hasDodaniePrepaid

		cmd.defaultPosData = pointData
        cmd.czyGift = cbdService.czyGift(cmd.nip)
		
    }

    def getDodajPunkt(ProcessCommand cmd, def calc ) {
        PointCommand pointData = new PointCommand()

		pointData.calc = calc
		
        pointData.dialupTyp = calculatorService.getCalcProperty(calc,"TYP_DIALUP")
        pointData.dialupCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_TERM_CENA")
        pointData.dialupPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_PP_CENA")

        pointData.vpnTyp = calculatorService.getCalcProperty(calc,"TYP_VPN")
        pointData.vpnCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_TERM_CENA")
        pointData.vpnPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_PP_CENA")

        pointData.sslTyp = calculatorService.getCalcProperty(calc,"TYP_SSL")
        pointData.sslCena = calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_TERM_CENA")
        pointData.sslPPCena = calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_PP_CENA")

        pointData.gprsTyp = calculatorService.getCalcProperty(calc,"TYP_GPRS")
        pointData.gprsCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA")
        pointData.gprsPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_PP_CENA")

        pointData.gprsTypPortable = calculatorService.getCalcProperty(calc,"TYP_GPRS")
        pointData.gprsCenaPortable =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA")

        pointData.wifiTypPortable = calculatorService.getCalcProperty(calc,"TYP_WIFI")
        pointData.wifiCenaPortable =  calculatorService.getDecimalCalcProperty(calc,"TYP_WIFI_TERM_CENA")

        pointData.hasDodaniePrepaid = cmd.hasDodaniePrepaid

        pointData.kartaSimCena =  calculatorService.getDecimalCalcProperty(calc,"E_OPLATA_SIM")

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
        cmd.oplataZaUruchomienieWalutyObcej = calculatorService.getCalcProperty(calc,"DCC_OPLATA_URUCHOMIENIE")
    }

    def getKategoriaRyzykaKlienta(ProcessCommand cmd, def calc ) {
        cmd.katRyzykaKlienta = nullify(cmd.katRyzykaKlienta)
    }

    def getOswiadczenieZadaniaRozpoczeciaWykonaniaUslugi(ProcessCommand cmd, def calc ) {
        cmd.klauWykonaniaUslugi = nullify(cmd.klauWykonaniaUslugi)
    }

    def getTabelaUslugDodatkowych(ProcessCommand cmd, def calc ) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty(calc,"OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty(calc,"E_OPLATA_KARTA_SIM")
        cmd.mudCena = nullify(cmd.mudCena)
    }

    @Deprecated
    def getDodatkoweUslugi2(ProcessCommand cmd, def calc) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty(calc,"OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty(calc,"OPLATA_KALKULATOR")
        cmd.mudCena = nullify(cmd.mudCena)
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
        cmd.ifJCB = calculatorService.getCalcProperty(calc,"E_JCB")
        cmd.ifUPI = calculatorService.getCalcProperty(calc,"E_UPI")
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
        cmd.okresLojalnosciowy = cmd.okresLojalnosciowy ?: calculatorService.getCalcProperty(calc,"LICZBA_MIESIECY_LOJ")
        cmd.oplataDeinstalacyjna = cmd.oplataDeinstalacyjna ?: calculatorService.getCalcProperty(calc, "OPLATA_DEINST_WARTOSC")
    }

    def getOpieka(ProcessCommand cmd, def calc ) {

    }

    def setCzyDcc(ProcessCommand cmd, def calc){
        def czyDcc = calculatorService.getCalcProperty(calc,"CZY_DCC")
        if (czyDcc != null) {
            czyDcc = ((String)czyDcc).trim()
        }
        cmd.czyDcc = "TAK".equalsIgnoreCase(czyDcc)
    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd, def calc ) {
        setCzyDcc(cmd,calc)
        cmd.oplataZaUruchomienieDCC = cmd.czyDcc ? calculatorService.getCalcProperty(calc,"DCC_OPLATA_URUCHOMIENIE") : "-"
    }

    def getOplatyDCC(ProcessCommand cmd, def calc ) {
        setCzyDcc(cmd,calc)
        if (!cmd.czyDcc && calculatorService.getCalcProperty(calc,"OPLATA_DCC") == null) {
            cmd.oplataZaPlatnoscWInnejWalucie = "-"
        } else {
            def oplataDcc = calculatorService.getCalcProperty(calc,"OPLATA_DCC")
            cmd.oplataZaPlatnoscWInnejWalucie = oplataDcc != null ? oplataDcc : ""
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

    def getUzgodnienieDyspozycji(ProcessCommand cmd, def calc) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd, def calc ) {
        def user = springSecurityService.principal;
        cmd.pozyskujacyImie =  user.imie
        cmd.pozyskujacyNazwisko = user.nazwisko
        cmd.pozyskujacyNumer =  user.nr
    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd, def calc ){
        cmd.beneficjentKRS = nullify(cmd.beneficjentKRS)
    }

    def setCurrentValue(def data, def calcValue){
        (data && data.toString().isNumber())? data : calcValue;
    }

    def setAtLeastAs(def data, def calcValue){
        //log.info("data : ${data} , cal : ${calcValue}")
        if(!data || data == "null" || !data.toString().isNumber()) {
            return calcValue
        }

        if (!calcValue || !calcValue.toString().isNumber()) {
            return data
        }


        def oldNumber = Double.valueOf(data)
        def calcNumber = Double.valueOf(calcValue)

        oldNumber < calcNumber ? calcValue : data
    }

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd, def calc ) {

        // 1
        cmd.visaEUKKOPr = setAtLeastAs(cmd.visaEUKKOPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_PROCENT"))
        cmd.visaEUKKOSt = setAtLeastAs(cmd.visaEUKKOSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_ZL"))
        cmd.visaEUKDPr = setAtLeastAs(cmd.visaEUKDPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_PROCENT"))
        cmd.visaEUKDSt = setAtLeastAs(cmd.visaEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_ZL"))
        cmd.visaEUKBPr = setAtLeastAs(cmd.visaEUKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_PROCENT"))
        cmd.visaEUKBSt = setAtLeastAs(cmd.visaEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_ZL"))

        // 2
        cmd.visaOutEUKKOPr = setAtLeastAs(cmd.visaOutEUKKOPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_PROCENT"))
        cmd.visaOutEUKKOSt = setAtLeastAs(cmd.visaOutEUKKOSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_ZL"))
        cmd.visaOutEUKDPr = setAtLeastAs(cmd.visaOutEUKDPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_PROCENT"))
        cmd.visaOutEUKDSt = setAtLeastAs(cmd.visaOutEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_ZL"))
        cmd.visaOutEUKBPr = setAtLeastAs(cmd.visaOutEUKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_PROCENT"))
        cmd.visaOutEUKBSt = setAtLeastAs(cmd.visaOutEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_ZL"))

        // 3
        cmd.visaPolskaKKO1Pr = setAtLeastAs(cmd.visaPolskaKKO1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_PROCENT"))
        cmd.visaPolskaKKO1St = setAtLeastAs(cmd.visaPolskaKKO1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_ZL"))
        cmd.visaPolskaKKO2Pr = setAtLeastAs(cmd.visaPolskaKKO2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_PROCENT"))
        cmd.visaPolskaKKO2St = setAtLeastAs(cmd.visaPolskaKKO2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_ZL"))
        cmd.visaPolskaKD1Pr = setAtLeastAs(cmd.visaPolskaKD1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_PROCENT"))
        cmd.visaPolskaKD1St = setAtLeastAs(cmd.visaPolskaKD1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_ZL"))
        cmd.visaPolskaKD2Pr = setAtLeastAs(cmd.visaPolskaKD2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_PROCENT"))
        cmd.visaPolskaKD2St = setAtLeastAs(cmd.visaPolskaKD2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_ZL"))
        cmd.visaPolskaKBPr = setAtLeastAs(cmd.visaPolskaKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_PROCENT"))
        cmd.visaPolskaKBSt = setCurrentValue(cmd.visaPolskaKBSt, calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_ZL"))

        // 4
        cmd.mastercardEUKKPr = setAtLeastAs(cmd.mastercardEUKKPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_PROCENT"))
        cmd.mastercardEUKKSt = setAtLeastAs(cmd.mastercardEUKKSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_ZL"))
        cmd.mastercardEUKDPr = setAtLeastAs(cmd.mastercardEUKDPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_PROCENT"))
        cmd.mastercardEUKDSt = setAtLeastAs(cmd.mastercardEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_ZL"))
        cmd.mastercardEUKBLPr = setAtLeastAs(cmd.mastercardEUKBLPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_PROCENT"))
        cmd.mastercardEUKBLSt = setAtLeastAs(cmd.mastercardEUKBLSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_ZL"))
        cmd.mastercardEUMPr = setAtLeastAs(cmd.mastercardEUMPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_PROCENT"))
        cmd.mastercardEUMSt = setAtLeastAs(cmd.mastercardEUMSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_ZL"))

        // 5
        cmd.mastercardOutEUKKPr = setAtLeastAs(cmd.mastercardOutEUKKPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_PROCENT"))
        cmd.mastercardOutEUKKSt = setAtLeastAs(cmd.mastercardOutEUKKSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_ZL"))
        cmd.mastercardOutEUKDPr = setAtLeastAs(cmd.mastercardOutEUKDPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_PROCENT"))
        cmd.mastercardOutEUKDSt = setAtLeastAs(cmd.mastercardOutEUKDSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_ZL"))
        cmd.mastercardOutEUKBPr = setAtLeastAs(cmd.mastercardOutEUKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_PROCENT"))
        cmd.mastercardOutEUKBSt = setAtLeastAs(cmd.mastercardOutEUKBSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_ZL"))
        cmd.mastercardOutEUMPr = setAtLeastAs(cmd.mastercardOutEUMPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_PROCENT"))
        cmd.mastercardOutEUMSt = setAtLeastAs(cmd.mastercardOutEUMSt,calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_ZL"))

        // 6
        cmd.mastercardPolskaKK1Pr = setAtLeastAs(cmd.mastercardPolskaKK1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_PROCENT"))
        cmd.mastercardPolskaKK1St = setAtLeastAs(cmd.mastercardPolskaKK1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_ZL"))
        cmd.mastercardPolskaKK2Pr = setAtLeastAs(cmd.mastercardPolskaKK2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_PROCENT"))
        cmd.mastercardPolskaKK2St = setAtLeastAs(cmd.mastercardPolskaKK2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_ZL"))
        cmd.mastercardPolskaKK3Pr = setAtLeastAs(cmd.mastercardPolskaKK3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_PROCENT"))
        cmd.mastercardPolskaKK3St = setAtLeastAs(cmd.mastercardPolskaKK3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_ZL"))
        cmd.mastercardPolskaKD1Pr = setAtLeastAs(cmd.mastercardPolskaKD1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_PROCENT"))
        cmd.mastercardPolskaKD1St = setAtLeastAs(cmd.mastercardPolskaKD1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_ZL"))
        cmd.mastercardPolskaKD2Pr = setAtLeastAs(cmd.mastercardPolskaKD2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_PROCENT"))
        cmd.mastercardPolskaKD2St = setAtLeastAs(cmd.mastercardPolskaKD2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_ZL"))
        cmd.mastercardPolskaKD3Pr = setAtLeastAs(cmd.mastercardPolskaKD3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_PROCENT"))
        cmd.mastercardPolskaKD3St = setAtLeastAs(cmd.mastercardPolskaKD3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_ZL"))
        cmd.mastercardPolskaKBPr = setAtLeastAs(cmd.mastercardPolskaKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_63_PROCENT"))
        cmd.mastercardPolskaKBSt = setCurrentValue(cmd.mastercardPolskaKBSt, calculatorService.getCalcProperty(calc,"OPLATA_MSC_63_ZL"))
        cmd.mastercardPolskaM1Pr = setAtLeastAs(cmd.mastercardPolskaM1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_PROCENT"))
        cmd.mastercardPolskaM1St = setAtLeastAs(cmd.mastercardPolskaM1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_ZL"))
        cmd.mastercardPolskaM2Pr = setAtLeastAs(cmd.mastercardPolskaM2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_PROCENT"))
        cmd.mastercardPolskaM2St = setAtLeastAs(cmd.mastercardPolskaM2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_ZL"))
        cmd.mastercardPolskaM3Pr = setAtLeastAs(cmd.mastercardPolskaM3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_PROCENT"))
        cmd.mastercardPolskaM3St = setAtLeastAs(cmd.mastercardPolskaM3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_ZL"))

        // 7
        cmd.visaPKOBPKKO1Pr = setAtLeastAs(cmd.visaPKOBPKKO1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_PROCENT"))
        cmd.visaPKOBPKKO1St = setAtLeastAs(cmd.visaPKOBPKKO1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_ZL"))
        cmd.visaPKOBPKKO2Pr = setAtLeastAs(cmd.visaPKOBPKKO2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_PROCENT"))
        cmd.visaPKOBPKKO2St = setAtLeastAs(cmd.visaPKOBPKKO2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_ZL"))
        cmd.visaPKOBPKD1Pr = setAtLeastAs(cmd.visaPKOBPKD1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_PROCENT"))
        cmd.visaPKOBPKD1St = setAtLeastAs(cmd.visaPKOBPKD1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_ZL"))
        cmd.visaPKOBPKD2Pr = setAtLeastAs(cmd.visaPKOBPKD2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_PROCENT"))
        cmd.visaPKOBPKD2St = setAtLeastAs(cmd.visaPKOBPKD2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_ZL"))
        cmd.visaPKOBPKB3Pr = setAtLeastAs(cmd.visaPKOBPKB3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_PROCENT"))
        cmd.visaPKOBPKB3St = setCurrentValue(cmd.visaPKOBPKB3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_ZL"))

        // 8
        cmd.mastercardPKOBPKK1Pr = setAtLeastAs(cmd.mastercardPKOBPKK1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_PROCENT"))
        cmd.mastercardPKOBPKK1St = setAtLeastAs(cmd.mastercardPKOBPKK1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_ZL"))
        cmd.mastercardPKOBPKK2Pr = setAtLeastAs(cmd.mastercardPKOBPKK2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_PROCENT"))
        cmd.mastercardPKOBPKK2St = setAtLeastAs(cmd.mastercardPKOBPKK2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_ZL"))
        cmd.mastercardPKOBPKK3Pr = setAtLeastAs(cmd.mastercardPKOBPKK3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_PROCENT"))
        cmd.mastercardPKOBPKK3St = setAtLeastAs(cmd.mastercardPKOBPKK3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_ZL"))
        cmd.mastercardPKOBPKD1Pr = setAtLeastAs(cmd.mastercardPKOBPKD1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_PROCENT"))
        cmd.mastercardPKOBPKD1St = setAtLeastAs(cmd.mastercardPKOBPKD1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_ZL"))
        cmd.mastercardPKOBPKD2LPr = setAtLeastAs(cmd.mastercardPKOBPKD2LPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_PROCENT"))
        cmd.mastercardPKOBPKD2LSt = setAtLeastAs(cmd.mastercardPKOBPKD2LSt, calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_ZL"))
        cmd.mastercardPKOBPKD3Pr = setAtLeastAs(cmd.mastercardPKOBPKD3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_PROCENT"))
        cmd.mastercardPKOBPKD3St = setAtLeastAs(cmd.mastercardPKOBPKD3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_ZL"))
        cmd.mastercardPKOBPKBPr   = setAtLeastAs(cmd.mastercardPKOBPKBPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_PROCENT"))
        cmd.mastercardPKOBPKBSt = setCurrentValue(cmd.mastercardPKOBPKBSt, calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_ZL"))
        cmd.mastercardPKOBPM1Pr = setAtLeastAs(cmd.mastercardPKOBPM1Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_PROCENT"))
        cmd.mastercardPKOBPM1St = setAtLeastAs(cmd.mastercardPKOBPM1St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_ZL"))
        cmd.mastercardPKOBPM2Pr = setAtLeastAs(cmd.mastercardPKOBPM2Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_PROCENT"))
        cmd.mastercardPKOBPM2St = setAtLeastAs(cmd.mastercardPKOBPM2St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_ZL"))
        cmd.mastercardPKOBPM3Pr = setAtLeastAs(cmd.mastercardPKOBPM3Pr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_PROCENT"))
        cmd.mastercardPKOBPM3St = setAtLeastAs(cmd.mastercardPKOBPM3St, calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_ZL"))

        //9
        cmd.dinersClubPr = setAtLeastAs(cmd.dinersClubPr, calculatorService.getCalcProperty(calc,"OPLATA_MSC_9_PROCENT"))
        // 10a
        cmd.ikoPr = calculatorService.getCalcProperty(calc,"E_PKO_6")
        // 10b
        cmd.blikPr = calculatorService.getCalcProperty(calc,"E_PKO_6")

        cmd.jcbPr = calculatorService.getCalcProperty(calc,"E_JCB")
        cmd.upiPr = calculatorService.getCalcProperty(calc,"E_UPI")
        cmd.oplataAutoryzacyjnaSt = calculatorService.getCalcProperty(calc,"E_OPLATA_ZA_AUTORYZACJE")
        cmd.oplataAutoryzacyjnaNr = cmd.oplataAutoryzacyjnaSt //Used for documents with currency fixed in the template
        cmd.cardsOutOfEU = calculatorService.getRawCalcPropertyOr(calc, 'SELECT_DOD_OPL_VISA_MASTERCARD', 'NIE')
        cmd.cardsInEUNotInPL = calculatorService.getRawCalcPropertyOr(calc, 'SELECT_DOD_OPL_VISA_MASTERCARD_BIZNUE', 'NIE')
        cmd.cardsInPL = calculatorService.getRawCalcPropertyOr(calc, 'SELECT_DO_OPL_VISA_MASTERCARD_BIZNPOL', 'NIE')
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd, def calc ) {
        getFormaDoladowaniaOrazWartosciUpustow(cmd, calc)
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd, def calc ) {
    }

    def getZestawPosWymianaTerminala(ProcessCommand cmd, def calc ){
    }

    def getRachunekBankowyKlienta(ProcessCommand cmd, def calc ) {
        cmd.numerRachunkuBankowegoKlienta = nullify(cmd.numerRachunkuBankowegoKlienta)
        cmd.bankKlienta = nullify(cmd.bankKlienta)
    }

    def getScoring(ProcessCommand cmd, def calc ) {
        cmd.scoringDzialalnosc = nullify(cmd.scoringDzialalnosc)

        //scoringMcc pobierany jest globalnie w metodzie init()
        def result = cmd.scoringMcc ? cbdService.getRodzajDzialalnosciByMCC(cmd.scoringMcc) : null
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

		log.info "Liczba punktow z CBD: " + cmd.liczbaPtkCbd
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
        def obslugaEkonomicznyCenaCalc = calculatorService.getCalcProperty(calc, 'E_PAKIET_SERWIS_2')
        cmd.obslugaEkonomicznyCena = obslugaEkonomicznyCenaCalc ? obslugaEkonomicznyCenaCalc : nullify(cmd.obslugaEkonomicznyCena, "5");
    }

    def getSerwisEkonomiczny(ProcessCommand cmd, def calc ) {
        //serwis ekonomiczny zaczytujemy w dwoch panelach
        def obslugaEkonomicznyCenaCalc = calculatorService.getCalcProperty(calc, 'E_PAKIET_SERWIS_2')
        cmd.obslugaEkonomicznyCena = obslugaEkonomicznyCenaCalc ? obslugaEkonomicznyCenaCalc : nullify(cmd.obslugaEkonomicznyCena, "5");
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

        cmd.emailDoWysylkiDokumentu = nullify(cmd.emailDoWysylkiDokumentu)
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
        cmd.isOdplatneUzywanieShown = nullify(cmd.isOdplatneUzywanieShown)
        cmd.odplatneUzywanie = nullify(cmd.odplatneUzywanie)

        def result2 = cbdService.getHirePaymentByProcess(cmd.nip);
        cmd.odpUzyTermSzt = result2?.ile ?: ""
        //powinno przyjsc cbd, gdy po stronie eService beda zaimplementowane zmiany
        cmd.odpUzyPpSzt = ""
        cmd.odpUzyTermMies = setAtLeastAs(cmd.odpUzyTermMies,calculatorService.getDecimalCalcProperty(calc,"CENA_NAJMU"))
        cmd.odpUzyTermCalc = calculatorService.getDecimalCalcProperty(calc,"CENA_NAJMU")

        //nie mniej niz z kalkulatora powinno byc
        cmd.odpUzyPpMies = ""
    }

    def getCenaPakietu(ProcessCommand cmd, def calc) {
        cmd.cenaPakietu = "150"
    }

    def getCashbackInfo(ProcessCommand cmd, def calc) {
        String calcCashbackUpust = calculatorService.getCalcProperty(calc, "CASHBACK_D")
        cmd.cashbackUpust = calcCashbackUpust == "0" ? "-" : calcCashbackUpust
        cmd.cashbackAbonament = "5"
    }

    def getUpustCashback(ProcessCommand cmd, def calc) {
        String calcCashbackUpust = calculatorService.getCalcProperty(calc, "CASHBACK_D")
        cmd.isCashbackUpustEditable = calculatorService.hasCalcProperty("CASHBACK_A", "TAK", calc) && calcCashbackUpust != "0"
        cmd.cashbackUpust = calcCashbackUpust == "0" ? "-" : calcCashbackUpust
    }

    def getPoziomOplatIWarunkiPlatnosci(ProcessCommand cmd, def calc) {
        cmd.oplatyIPlatnosciDo = "5"
        cmd.oplatyIPlatnosciPowyzej = "10"
        cmd.oplataPrDo = "2"
        cmd.oplataPrPowyzej = "7"
        cmd.dinersClubDo = "3.5"
        cmd.dinersClubPowyzej = "3.5"
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
	
	def setupPointDataFromCalc(PointCommand cmd, def calc) {
		cmd.calc = calc
		cmd.dialupCena =  setAtLeastAs(cmd.dialupCena, calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_TERM_CENA"))
		cmd.dialupPPCena =  setAtLeastAs(cmd.dialupPPCena, calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_PP_CENA"))

		cmd.vpnCena =  setAtLeastAs(cmd.vpnCena, calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_TERM_CENA"))
		cmd.vpnPPCena =  setAtLeastAs(cmd.vpnPPCena, calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_PP_CENA"))

		cmd.sslCena = setAtLeastAs(cmd.sslCena, calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_TERM_CENA"))
		cmd.sslPPCena =  setAtLeastAs(cmd.sslPPCena, calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_PP_CENA"))

		cmd.gprsCena =  setAtLeastAs(cmd.gprsCena, calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA"))
		cmd.gprsPPCena =  setAtLeastAs(cmd.gprsPPCena, calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_PP_CENA"))
	}
	
	def setupPosDataFromCalc(PointCommand cmd, def calc) {
		cmd.calc = calc

        cmd.dialupCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_TERM_CENA")
        cmd.dialupPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_DIALUP_PP_CENA")

        cmd.vpnCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_TERM_CENA")
        cmd.vpnPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_VPN_PP_CENA")

        cmd.sslCena = calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_TERM_CENA")
        cmd.sslPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_SSL_PP_CENA")

        cmd.gprsCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_TERM_CENA")
        cmd.gprsPPCena =  calculatorService.getDecimalCalcProperty(calc,"TYP_GPRS_PP_CENA")
	}
	
	def setupAllPosDataFromCalc(AllPosCommand apc, def calc) {
		apc.calc = calc
		apc.wysokoscOplaty = setAtLeastAs(apc.wysokoscOplaty, calculatorService.getDecimalCalcProperty(calc,"OPLATA_POS_PROM_CENA_NAJMU"))
	}

    private setSerwisZablokowany(ProcessCommand cmd, def calc, def serwisy){
        def results = serwisy.findAll{
            calculatorService.hasCalcProperty(it.key,"NIE",calc)
        }

        cmd.serwisZablokowany = results.size() == 3
    }
}
