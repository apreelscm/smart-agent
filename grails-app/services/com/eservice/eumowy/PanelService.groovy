package com.eservice.eumowy
import com.eservice.eumowy.command.ProcessCommand

class PanelService {

    def cbdService
    def calculatorService
    def springSecurityService
    //domainClass

    def getAdresacjaSeciowa(ProcessCommand cmd ,def calc) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd ,def calc) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd ,def calc) {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(cmd.nip);

        cmd.korespondencjaUlicaTytul = result?.typ_ulicy ?: "-"
        cmd.korespondencjaUlica = result?.ulica ?: "-"
        cmd.korespondencjaNrDomu = result?.nr_budynku ?: "-"
        cmd.korespondencjaNrMieszkania = result?.nr_lokal ?: "-"
        cmd.korespondencjaMiasto = result?.miejscowosc ?: "-"
        cmd.korespondencjaKodPocztowy = result?.kod_pocztowy ?: "-"
        cmd.korespondencjaPoczta = result?.poczta ?: "-"
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd,def calc) {
        cmd.dataAneksowanejUmowyPos = ""
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd,def calc) {
        cmd.dataAneksowanejUmowyPrepaid = ""
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd ,def calc) {
        cmd.umowaCzas =  ""
        cmd.umowaOznOd = ""
        cmd.umowaOznDo = ""
    }

    def getDaneAkceptanta(ProcessCommand cmd ,def calc) {
        def result = cbdService.getDaneAkceptanta(cmd.nip);

        cmd.akceptantNazwaOficjalna= result?.nazwa ?: "-"
        cmd.akceptantNazwaSieciowa= "-" //TODO
        cmd.akceptantRegon = result?.regon ?: "-"
    }

    def getDaneDoWydruku(ProcessCommand cmd ,def calc) {
        cmd.wydrukNazwaPunktu = "-"
        cmd.wydrukNazwaDoWyszukwarki = "-"
        cmd.wydrukUlicaTytul = "-"
        cmd.wydrukUlica = "-"
        cmd.wydrukNrDomu = "-"
        cmd.wydrukNrMieszkania = "-"
        cmd.wydrukMiasto = "-"
        cmd.wydrukKodPocztowy = "-"
        cmd.wydrukPoczta = "-"
        cmd.wydrukLinia1 = "-"
        cmd.wydrukLinia2 = "-"
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
        cmd.dccZakresUruchomienia = ""
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd ,def calc) {
        cmd.informacjaHandlowa = ""
    }

    def getDodajPos(ProcessCommand cmd ,def calc) {

    }

    def getDodajPunkt(ProcessCommand cmd ,def calc) {

    }

    def getDodatkoweUslugi(ProcessCommand cmd ,def calc) {
        cmd.oplataZaDzienneZestawienieTransakcji = "-"
        cmd.oplataZaMiesieczneZestawienieTransakcji = "-"
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu = "-"
        cmd.oplataZaDostarczeniePapieru = "-"
        cmd.oplataZaZmianeGrafiki = "-"
        cmd.oplataZaInstalacjePOS = "-"
        cmd.oplataZaInstalacjeGPRS = "-"
        cmd.oplataZaUruchomienieWalutyObcej = "-"
    }

    def getDodatkoweUslugi2(ProcessCommand cmd ,def calc) {
        cmd.wydrukGrafikiCena = calculatorService.getCalcProperty(calc,"OPLATA_LOGO")
        cmd.dzialaniaMatematyczneCena = calculatorService.getCalcProperty(calc,"OPLATA_KALKULATOR")
        cmd.tytulPlatnosciCena = "-"
        cmd.pierwszaSesjaCena = "-"

    }

    def getDodatkoweUslugiMud(ProcessCommand cmd ,def calc) {

    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {
        cmd.mudCena = ""
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd ,def calc) {
        cmd.weryfikacjaPINCena = "-"
        cmd.systemKasowyCena = "-"
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd ,def calc) {
    }

    def getFormaDoladowania(ProcessCommand cmd ,def calc) {
        cmd.doladowania_tp = calculatorService.getCalcProperty(calc,"CZY_TELEPOMPKA")
        cmd.doladowania_tk = calculatorService.getCalcProperty(calc,"OPLATA_IFPLUS_VISA")
        cmd.srednia_sprzedaz_doladowan = calculatorService.getCalcProperty(calc,"DEKLARACJA_SPRZEDAZY_PP")
        cmd.srednia_sprzedaz_doladowan_slownie = ""
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
        cmd.dzialalnoscForma = "-"
        cmd.dzialalnoscFormaInna = "-"
        cmd.dzialalnoscDokument = "-"
        cmd.dzialalnoscDokumentInny = "-"
    }

    def getInformacjeTechniczne(ProcessCommand cmd ,def calc) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd ,def calc) {
        def attr = "LICZBA_MIESIECY_LOJ"
        def result = calculatorService.getCalcProperty(calc,attr)
        cmd.okresLojalnosciowy = result ?: "-"
    }

    def getOpieka(ProcessCommand cmd ,def calc) {

    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd ,def calc) {
        cmd.oplataZaUruchomienieDCC = "-"
    }

    def getOplatyDCC(ProcessCommand cmd ,def calc) {
        cmd.oplataZaPlatnoscWInnejWalucie = calculatorService.getCalcProperty(calc,"OPLATA_DCC")
    }

    def getOsobaDoKontaktu(ProcessCommand cmd ,def calc) {
        cmd.kontaktTytul = "-"
        cmd.kontaktImie = "-"
        cmd.kontaktNazwisko = "-"
        cmd.kontaktTelStacjonarny = "-"
        cmd.kontaktTelKomorkowy = "-"
        cmd.kontaktEmail = "-"
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd ,def calc) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd ,def calc) {
        cmd.pozyskujacyTytul =  "-"
        cmd.pozyskujacyImie =  "-"
        cmd.pozyskujacyNazwisko =  "-"
        cmd.pozyskujacyNumer =  "-"
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

        cmd.reprezentant1Tytul = "-"
        cmd.reprezentant1Imie = "-"
        cmd.reprezentant1Nazwisko = "-"
        cmd.reprezentant2Tytul = "-"
        cmd.reprezentant2Imie = "-"
        cmd.reprezentant2Nazwisko = "-"

    }

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd ,def calc) {
        cmd.card_p_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_ZL")
        cmd.card_f_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_11_PROCENT")
        cmd.card_p_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_PROCENT")
        cmd.card_f_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_12_ZL")
        cmd.card_p_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_PROCENT")
        cmd.card_f_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_13_ZL")

        cmd.card_p_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_PROCENT")
        cmd.card_f_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_21_ZL")
        cmd.card_p_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_PROCENT")
        cmd.card_f_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_22_ZL")
        cmd.card_p_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_PROCENT")
        cmd.card_f_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_23_ZL")

        cmd.card_p_3_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_PROCENT")
        cmd.card_f_3_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_311_ZL")
        cmd.card_p_3_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_PROCENT")
        cmd.card_f_3_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_312_ZL")
        cmd.card_p_3_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_PROCENT")
        cmd.card_f_3_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_321_ZL")
        cmd.card_p_3_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_PROCENT")
        cmd.card_f_3_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_322_ZL")
        cmd.card_p_3_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_PROCENT")
        cmd.card_f_3_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_33_ZL")

        cmd.card_p_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_PROCENT")
        cmd.card_f_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_41_ZL")
        cmd.card_p_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_PROCENT")
        cmd.card_f_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_42_ZL")
        cmd.card_p_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_PROCENT")
        cmd.card_f_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_43_ZL")
        cmd.card_p_4_4 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_PROCENT")
        cmd.card_f_4_4 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_44_ZL")

        cmd.card_p_5_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_PROCENT")
        cmd.card_f_5_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_51_ZL")
        cmd.card_p_5_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_PROCENT")
        cmd.card_f_5_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_52_ZL")
        cmd.card_p_5_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_PROCENT")
        cmd.card_f_5_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_53_ZL")
        cmd.card_p_5_4 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_PROCENT")
        cmd.card_f_5_4 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_54_ZL")

        cmd.card_p_6_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_PROCENT")
        cmd.card_f_6_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_611_ZL")
        cmd.card_p_6_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_PROCENT")
        cmd.card_f_6_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_612_ZL")
        cmd.card_p_6_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_PROCENT")
        cmd.card_f_6_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_613_ZL")
        cmd.card_p_6_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_PROCENT")
        cmd.card_f_6_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_621_ZL")
        cmd.card_p_6_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_PROCENT")
        cmd.card_f_6_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_622_ZL")
        cmd.card_p_6_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_PROCENT")
        cmd.card_f_6_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_623_ZL")
        cmd.card_p_6_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_PROCENT")
        cmd.card_f_6_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_641_ZL")
        cmd.card_p_6_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_PROCENT")
        cmd.card_f_6_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_642_ZL")
        cmd.card_p_6_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_PROCENT")
        cmd.card_f_6_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_643_ZL")

        cmd.card_p_7_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_PROCENT")
        cmd.card_f_7_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_711_ZL")
        cmd.card_p_7_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_PROCENT")
        cmd.card_f_7_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_712_ZL")
        cmd.card_p_7_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_PROCENT")
        cmd.card_f_7_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_721_ZL")
        cmd.card_p_7_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_PROCENT")
        cmd.card_f_7_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_722_ZL")
        cmd.card_p_7_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_PROCENT")
        cmd.card_f_7_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_73_ZL")

        cmd.card_p_8_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_PROCENT")
        cmd.card_f_8_1_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_811_ZL")
        cmd.card_p_8_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_PROCENT")
        cmd.card_f_8_1_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_812_ZL")
        cmd.card_p_8_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_PROCENT")
        cmd.card_f_8_1_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_813_ZL")
        cmd.card_p_8_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_PROCENT")
        cmd.card_f_8_2_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_821_ZL")
        cmd.card_p_8_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_PROCENT")
        cmd.card_f_8_2_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_822_ZL")
        cmd.card_p_8_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_PROCENT")
        cmd.card_f_8_2_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_823_ZL")
        cmd.card_p_8_3   = calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_PROCENT")
        cmd.card_f_8_3   = calculatorService.getCalcProperty(calc,"OPLATA_MSC_83_ZL")
        cmd.card_p_8_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_PROCENT")
        cmd.card_f_8_4_1 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_841_ZL")
        cmd.card_p_8_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_PROCENT")
        cmd.card_f_8_4_2 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_842_ZL")
        cmd.card_p_8_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_PROCENT")
        cmd.card_f_8_4_3 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_843_ZL")

        cmd.card_p_9 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_9_PROCENT")

        cmd.card_p_10 = calculatorService.getCalcProperty(calc,"OPLATA_MSC_10_PROCENT")
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
        cmd.numerRachunkuBankowego = "-"
        cmd.bank = ""
    }

    def getScoring(ProcessCommand cmd ,def calc) {
        cmd.scoringMcc = calculatorService.getCalcProperty(calc,"MCC")
        cmd.scoringDzialalnosc = ""
        cmd.scoringIloscTransakcji = ""
        cmd.scoringCzestoscTransakcji = ""
        cmd.scoringOtwartyZamkniety = ""
        cmd.scoringStanZadbany = ""
        cmd.scoringWielkoscMiejscowosci = ""
        cmd.scoringLokalizacjaPunktu = ""
        cmd.scoringTypPunktu = ""
        cmd.scoringTypPunktuInny = ""
        cmd.scoringWielkoscPunktu = ""
        cmd.scoringAkceptacja = ""
        cmd.scoringMonitoring = ""
        cmd.scoringCharakterystyka = ""
        cmd.scoringCharakterystykaInna = ""
        cmd.scoringKoncesja = ""
        cmd.rodzajZezwolenia = ""
        cmd.scoringWlasnosc = ""
        cmd.scoringDzialalnoscCzas = ""
        cmd.scoringSprzedazTowarowEkskluzywnych = ""
        cmd.scoringPonad50ProcentObrotowWNocy = ""
        cmd.scoringRuchTurystycznyPrzygraniczny = ""
        cmd.scoringUslugiPlatneZGory = ""
        cmd.scoringDochodowosc = ""
        cmd.scoringDeklaracjaFinansowa = ""
        cmd.scoringDeklaracjaFinansowaObrotOgolem = ""
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = ""
        cmd.scoringDeklaracjaFinansowaSredniObrot = ""
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = ""
    }

    def getSerwis(ProcessCommand cmd ,def calc) {

        def SERWIS_TYPES = [[key: "PAKIET_SERWIS_PRESTIZ", value:"prestige"],
                [key: "PAKIET_SERWIS_KOMFORT", value:"comfort"],
                [key: "PAKIET_SERWIS_EKONOMICZNY", value:"economic"]]

        def result = SERWIS_TYPES.find{
            calculatorService.hasCalcProperty(calc,it.key,"TAK")
        }

        cmd.obslugaTyp = result?.value ?: "-";
        cmd.obslugaEkonomicznyCena = "-";
    }

    def getSerwisEkonomiczny(ProcessCommand cmd ,def calc) {

    }

    def getSerwisKomfort(ProcessCommand cmd ,def calc) {

    }

    def getSerwisPrzestiz(ProcessCommand cmd ,def calc) {

    }

    def getSiedzibaAkceptanta(ProcessCommand cmd ,def calc) {

        def result = cbdService.getSiedzibaAkceptanta(cmd.nip);

        println(result)
        cmd.akceptantUlicaTytul = result?.typ_ulicy ?: "-"
        cmd.akceptantUlica = result?.ulica ?: "-"
        cmd.akceptantNrDomu = result?.nr_budynku ?: "-"
        cmd.akceptantNrMieszkania = result?.nr_lokal ?: "-"
        cmd.akceptantMiasto = result?.miejscowosc ?: "-"
        cmd.akceptantKodPocztowy = result?.kod_pocztowy ?: "-"
        cmd.akceptantPoczta = result?.poczta ?: "-"
        cmd.akceptantTelStacjonarny = result?.telStac ?: "-"
        cmd.akceptantFax = result?.faks ?: "-"
        cmd.akceptantTelKomorkowy = result?.telKom ?: "-"
    }

    def getUmowa2(ProcessCommand cmd ,def calc) {
        cmd.miejsceUmowy= "-";
        cmd.dataUmowy = new Date().format("yyyy-MM-dd")
        println(cmd.dataUmowy)
    }

    def getUwagi(ProcessCommand cmd ,def calc) {
        //EMPTY
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd ,def calc) {
        cmd.punktyTytulPlatnosci= []
        cmd.punktySystemKasowy = []
        cmd.punktyUta = []
        cmd.punktyWybrane = []
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