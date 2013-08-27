package mock



import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

class PanelMockService {

    def getAdresacjaSeciowa(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd) {
        cmd.korespondencjaUlicaTytul = "Ulica"
        cmd.korespondencjaUlica = "Zielona"
        cmd.korespondencjaNrDomu = "23A"
        cmd.korespondencjaNrMieszkania = "1"

        cmd.korespondencjaMiasto = "Cieszyn"
        cmd.korespondencjaKodPocztowy = "12-234"
        cmd.korespondencjaPoczta = "Kraków"
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd) {
        cmd.dataAneksowanejUmowyPos = DateUtils.getCurrentFormattedDate()
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd) {
        cmd.dataAneksowanejUmowyPrepaid = DateUtils.getCurrentFormattedDate()
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd) {
        cmd.umowaCzas = "nieoznaczony" //TODO -  MOZLIWE, ze pdfach jest to umowaOzn, umowaNieOzn
        cmd.umowaOznOd = "" //DateUtils.getCurrentFormattedDate()
        cmd.umowaOznDo = "" //DateUtils.getCurrentFormattedDate()

    }

    def getDaneAkceptanta(ProcessCommand cmd) {
        cmd.akceptantNazwaOficjalna= "KGHM Polska Miedź S.A."
        cmd.akceptantNazwaSieciowa= "KGHM nazwa sieciowa"
        cmd.akceptantRegon = "1234567895"
    }

    def getDaneDoWydruku(ProcessCommand cmd) {
        cmd.wydrukNazwaPunktu = "PHU \"U Basi\""
        cmd.wydrukNazwaDoWyszukwarki = "U Basi"

        cmd.wydrukUlicaTytul = "Ulica"
        cmd.wydrukUlica = "Kwiatowa"
        cmd.wydrukNrDomu = "34"
        cmd.wydrukNrMieszkania = "123b"

        cmd.wydrukMiasto = "Płock"
        cmd.wydrukKodPocztowy = "02-024"
        cmd.wydrukPoczta = "Olsztyn"

        cmd.wydrukLinia1 = "Dziekujemy za zakupy"
        cmd.wydrukLinia2 = "Zapraszamy ponownie"
    }

    def getDanePos(ProcessCommand cmd) {

    }

    def getDanePunktu(ProcessCommand cmd) {

    }

    def getDcc(ProcessCommand cmd) {
        cmd.oplataVISA = "124.333"
        cmd.oplataVISAPr = "12.4"
        cmd.oplataMasterCard = "0.73"
        cmd.oplataMasterCardPr = "12.6"
        cmd.oplataMaestro = "12.93"
        cmd.oplataMasteroPr = "1.04"
    }

    def getDccZakresUruchomienia(ProcessCommand cmd) {
        cmd.dccZakresUruchomienia = "obecne_i_nowe"
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd) {
        cmd.informacjaHandlowa = "nie"
    }

    def getDodajPos(ProcessCommand cmd) {

    }

    def getDodajPunkt(ProcessCommand cmd) {

    }

    def getDodatkoweUslugi(ProcessCommand cmd) {
        cmd.oplataZaDzienneZestawienieTransakcji = "1.23"
        cmd.oplataZaMiesieczneZestawienieTransakcji = "2.23"
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu = "3.23"
        cmd.oplataZaDostarczeniePapieru = "4.23"
        cmd.oplataZaZmianeGrafiki = "5.23"
        cmd.oplataZaInstalacjePOS = "6.23"
        cmd.oplataZaInstalacjeGPRS = "7.23"
        cmd.oplataZaUruchomienieWalutyObcej = "8.23"
    }

    def getDodatkoweUslugi2(ProcessCommand cmd) {
        cmd.wydrukGrafikiCena = "1.40"
        cmd.dzialaniaMatematyczneCena = "0.32"
        cmd.tytulPlatnosciCena = "23.45"
        cmd.pierwszaSesjaCena = "1.34"
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {
        cmd.mudCena = "54"
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd) {
        cmd.weryfikacjaPINCena = "123.5"
        cmd.systemKasowyCena = "2.50"
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd) {

    }

    def getFormaDoladowania(ProcessCommand cmd) {
        cmd.doladowania_tp = "tak"
        cmd.doladowania_tk = "nie"
        cmd.srednia_sprzedaz_doladowan = "123"
        cmd.srednia_sprzedaz_doladowan_slownie = "sto dwadzieścia trzy"
    }

    def getFunkcjeTerminala(ProcessCommand cmd) {

    }

    def getIfplus(ProcessCommand cmd) {
        cmd.ifOplataVISA = "0.34"
        cmd.ifOplataMasterCard = "1.34"
        cmd.ifOplataDinersClub = "2.34"
        cmd.ifOplataIKO = "3.34"
        cmd.ifOplataPKOPB = "4.43"
    }

    def getInformacjeDodatkowe(ProcessCommand cmd) {
        cmd.dzialalnoscForma = "spolka_zoo"
        cmd.dzialalnoscFormaInna = "Na czarno"
        cmd.dzialalnoscDokument = "krs"
        cmd.dzialalnoscDokumentInny = "lewe papiery"
    }

    def getInformacjeTechniczne(ProcessCommand cmd) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd) {

    }

    def getOpieka(ProcessCommand cmd) {

    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd) {
        cmd.oplataZaUruchomienieDCC = "25.78"
    }

    def getOplatyDCC(ProcessCommand cmd) {
        cmd.oplataZaPlatnoscWInnejWalucie = "345.67"
    }

    def getOsobaDoKontaktu(ProcessCommand cmd) {

        cmd.kontaktTytul = "Pan"
        cmd.kontaktImie = "Krzysztof"
        cmd.kontaktNazwisko = "Krawczyk"
        cmd.kontaktTelStacjonarny = "(22) 1234567"
        cmd.kontaktTelKomorkowy = "509123456"
        cmd.kontaktEmail = "ala.ma.kota@gmail.com"
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd) {
        cmd.pozyskujacyTytul = "Pan"
        cmd.pozyskujacyImie = "Jan"
        cmd.pozyskujacyNazwisko = "Nowak"
        cmd.pozyskujacyNumer ="1234_A"
    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd) {
        cmd.reprezentant1Tytul = "Pani"
        cmd.reprezentant1Imie = "Zofia"
        cmd.reprezentant1Nazwisko = "Kowalska"
        cmd.reprezentant2Tytul = "Pan"
        cmd.reprezentant2Imie = "Mariusz"
        cmd.reprezentant2Nazwisko = "Kolonko"
    }

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd) {
        cmd.card_p_1_1 = "0.02"
        cmd.card_f_1_1 = "2.30"
        cmd.card_p_1_2 = "0.02"
        cmd.card_f_1_2 = "2.30"
        cmd.card_p_1_3 = "0.02"
        cmd.card_f_1_3 = "2.30"

        cmd.card_p_2_1 = "0.02"
        cmd.card_f_2_1 = "2.30"
        cmd.card_p_2_2 = "0.02"
        cmd.card_f_2_2 = "2.30"
        cmd.card_p_2_3 = "0.02"
        cmd.card_f_2_3 = "2.30"

        cmd.card_p_3_1_1 = "0.02"
        cmd.card_f_3_1_1 = "2.30"
        cmd.card_p_3_1_2 = "0.02"
        cmd.card_f_3_1_2 = "2.30"
        cmd.card_p_3_2_1 = "0.02"
        cmd.card_f_3_2_1 = "2.30"
        cmd.card_p_3_2_2 = "0.02"
        cmd.card_f_3_2_2 = "2.30"
        cmd.card_p_3_3 = "0.03"
        cmd.card_f_3_3 = "2.33"

        cmd.card_p_4_1 = "0.02"
        cmd.card_f_4_1 = "2.30"
        cmd.card_p_4_2 = "0.02"
        cmd.card_f_4_2 = "2.30"
        cmd.card_p_4_3 = "0.02"
        cmd.card_f_4_3 = "2.30"
        cmd.card_p_4_4 = "0.02"
        cmd.card_f_4_4 = "2.30"

        cmd.card_p_5_1 = "0.02"
        cmd.card_f_5_1 = "2.30"
        cmd.card_p_5_2 = "0.02"
        cmd.card_f_5_2 = "2.30"
        cmd.card_p_5_3 = "0.02"
        cmd.card_f_5_3 = "2.30"
        cmd.card_p_5_4 = "0.02"
        cmd.card_f_5_4 = "2.30"

        cmd.card_p_6_1_1 = "0.02"
        cmd.card_f_6_1_1 = "2.30"
        cmd.card_p_6_1_2 = "0.02"
        cmd.card_f_6_1_2 = "2.30"
        cmd.card_p_6_1_3 = "0.02"
        cmd.card_f_6_1_3 = "2.30"
        cmd.card_p_6_2_1 = "0.02"
        cmd.card_f_6_2_1 = "2.30"
        cmd.card_p_6_2_2 = "0.02"
        cmd.card_f_6_2_2 = "2.30"
        cmd.card_p_6_2_3 = "0.02"
        cmd.card_f_6_2_3 = "2.30"
        cmd.card_p_6_4_1 = "0.02"
        cmd.card_f_6_4_1 = "2.30"
        cmd.card_p_6_4_2 = "0.02"
        cmd.card_f_6_4_2 = "2.30"
        cmd.card_p_6_4_3 = "0.02"
        cmd.card_f_6_4_3 = "2.30"

        cmd.card_p_7_1_1 = "0.02"
        cmd.card_f_7_1_1 = "2.30"
        cmd.card_p_7_1_2 = "0.02"
        cmd.card_f_7_1_2 = "2.30"
        cmd.card_p_7_2_1 = "0.02"
        cmd.card_f_7_2_1 = "2.30"
        cmd.card_p_7_2_2 = "0.02"
        cmd.card_f_7_2_2 = "2.30"
        cmd.card_p_7_3 = "0.02"
        cmd.card_f_7_3 = "2.30"

        cmd.card_p_8_1_1 = "0.02"
        cmd.card_f_8_1_1 = "2.30"
        cmd.card_p_8_1_2 = "0.02"
        cmd.card_f_8_1_2 = "2.30"
        cmd.card_p_8_1_3 = "0.02"
        cmd.card_f_8_1_3 = "2.30"
        cmd.card_p_8_2_1 = "0.02"
        cmd.card_f_8_2_1 = "2.30"
        cmd.card_p_8_2_2 = "0.02"
        cmd.card_f_8_2_2 = "2.30"
        cmd.card_p_8_2_3 = "0.02"
        cmd.card_f_8_2_3 = "2.30"
        cmd.card_p_8_3 = "0.02"
        cmd.card_f_8_3 = "2.30"
        cmd.card_p_8_4_1 = "0.02"
        cmd.card_f_8_4_1 = "2.30"
        cmd.card_p_8_4_2 = "0.02"
        cmd.card_f_8_4_2 = "2.30"
        cmd.card_p_8_4_3 = "0.02"
        cmd.card_f_8_4_3 = "2.30"
        cmd.card_p_9 = "0.02"
        cmd.card_p_10 = "0.02"
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd) {
        cmd.pp_orange_tk = "2.3"
        cmd.pp_orange_tp = "1.35"
        cmd.pp_plus_tk = "0.48"
        cmd.pp_plus_tp = "23.1"
        cmd.pp_tmobile_tk = "0.01"
        cmd.pp_tmobile_tp = "0.32"
        cmd.pp_heyah_tk = "0.45"
        cmd.pp_heyah_tp = "0.03"
        cmd.pp_play_tk = "34.0"
        cmd.pp_play_tp = "0.25"
        cmd.pp_telegrosik_tk = "1.05"
        cmd.pp_virginmobile_tk = "3"
        cmd.pp_lycamobile_tk = "2.50"
        cmd.pp_gtmobile_tk = "5.02"
        cmd.pp_vectonemobile_tk = "2.34"
        cmd.pp_delightmobile_tk = "1.23"
        cmd.oplataZaOprogramowanieDoDoladowan = "0.003"
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd) {

    }

    def getRachunekBankowyKlienta(ProcessCommand cmd) {
        cmd.numerRachunkuBankowego = "11 1160 2202 0000 0001 9389 8247"
        cmd.bank = "3"
    }

    def getScoring(ProcessCommand cmd) {

        cmd.scoringMcc = "1234"
        cmd.scoringDzialalnosc = "1"
        cmd.scoringSzczegolyDzialalnosci = "Handel zywym towarem"

        cmd.scoringIloscTransakcji = "2"

        cmd.scoringCzestoscTransakcji = "4"

        cmd.scoringOtwartyZamkniety = "nieczynne"
        cmd.scoringStanZadbany = "tak"

        cmd.scoringWielkoscMiejscowosci = "4"

        cmd.scoringLokalizacjaPunktu = "centrum_miasta"
        cmd.scoringTypPunktu = "inny"
        cmd.scoringTypPunktuInny = "butik"

        cmd.scoringWielkoscPunktu = "3"

        cmd.scoringAkceptacja = "tak"

        cmd.scoringMonitoring = "tak"

        cmd.scoringDzialalnoscCzas = "2"

        cmd.scoringCharakterystyka = "inny"
        cmd.scoringCharakterystykaInna = "kwiaciarnia"

        cmd.scoringKoncesja = "tak"
        cmd.rodzajZezwolenia = "Produkcja bimbru"

        cmd.scoringWlasnosc = "1"

        cmd.scoringDzialalnoscCzas = "2"

        cmd.scoringSprzedazTowarowEkskluzywnych = "tak"
        cmd.scoringPonad50ProcentObrotowWNocy = "tak"
        cmd.scoringRuchTurystycznyPrzygraniczny = "tak"
        cmd.scoringUslugiPlatneZGory = "tak"

        cmd.scoringDochodowosc = "500"

        cmd.scoringDeklaracjaFinansowa = "wlasciwe"
        cmd.scoringDeklaracjaFinansowaObrotOgolem = "23.50"
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = "12.45"
        cmd.scoringDeklaracjaFinansowaSredniObrot = "10.45"
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = "3.45"

    }

    def getSerwis(ProcessCommand cmd) {
        cmd.obslugaTyp = "economic"
        cmd.obslugaEkonomicznyCena = "123.50"
    }

    def getSerwisEkonomiczny(ProcessCommand cmd) {
        //korzysta z pola cmd.obslugaEkonomicznyCena z getSerwis
        cmd.obslugaEkonomicznyCena = "-"
    }

    def getSerwisKomfort(ProcessCommand cmd) {
        //puste
    }

    def getSerwisPrzestiz(ProcessCommand cmd) {
        //puste
    }

    def getSiedzibaAkceptanta(ProcessCommand cmd) {
        cmd.akceptantUlicaTytul = "Plac"
        cmd.akceptantUlica = "Wilsona"
        cmd.akceptantNrDomu = "2"
        cmd.akceptantNrMieszkania = "2b"

        cmd.akceptantMiasto = "Warszawa"
        cmd.akceptantKodPocztowy = "98-765"
        cmd.akceptantPoczta = "Olsztyn"

        cmd.akceptantTelStacjonarny = "(22)4556789"
        cmd.akceptantFax = "1234567"
        cmd.akceptantTelKomorkowy ="509876543"
    }

    def getUmowa2(ProcessCommand cmd) {
        cmd.miejsceUmowy = "Warszawa"
        cmd.dataUmowy = new Date().format("yyyy-MM-dd")
    }

    def getUwagi(ProcessCommand cmd) {
        cmd.notes = "Ala ma kota";
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd) {
        /*cmd.punktyTytulPlatnosci= [1,2,3]
        cmd.punktySystemKasowy = [1, 3]
        cmd.punktyUta = [1]
        cmd.punktyWybrane = [2]*/
		
		def p1 = new AllPointsCommand()
		def p2 = new AllPointsCommand()
		def p3 = new AllPointsCommand()
		
        p1.id = 1 
		p1.nazwa = 'Sklep spozywczy'
		p1.ulica = 'Zielona'
		p1.miejscowosc = 'Lubartów'
		p1.nrBudynku = '5A'
		p1.kodPocztowy = '02-123'
        p2.id = 2
		p2.nazwa = 'Kwiaciarnia Róża'
		p2.ulica = 'Zielona'
		p2.miejscowosc = 'Lubartów'
		p2.nrBudynku = '12'
		p2.kodPocztowy = '02-123'
        p3.id = 3
		p3.nazwa = 'Kino Femina'
		p3.ulica = 'Zielona'
		p3.miejscowosc = 'Lubartów'
		p3.nrBudynku = '93'
		p3.kodPocztowy = '02-123'
		
		cmd.allPoints = [p1, p2, p3]
		
    }

    def getZalaczniki(ProcessCommand cmd) {

    }

    def getZestawPos(ProcessCommand cmd) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd) {
        cmd.oplPOSDialUpTyp = ""
        cmd.oplPOSDialUpIlosc = "12"
        cmd.oplPOSDialUpIloscPP = "23"
        cmd.oplPOSDialUpNormalneMies = "1.23"
        cmd.oplPOSDialUpNormalnePP = "1.24"
        cmd.oplPOSDialUpPreferencyjneMies = "1.25"
        cmd.oplPOSDialUpPreferencyjnePP = "1.26"

        cmd.oplPOSVPNTyp = ""
        cmd.oplPOSVPNIlosc = "43"
        cmd.oplPOSVPNIloscPP = "56"
        cmd.oplPOSVPNNormalneMies = "2.23"
        cmd.oplPOSVPNNormalnePP = "2.24"
        cmd.oplPOSVPNPreferencyjneMies = "2.25"
        cmd.oplPOSVPNPreferencyjnePP = "2.26"

        cmd.oplPOSSSLTyp = ""
        cmd.oplPOSSSLIlosc = "67"
        cmd.oplPOSSSLIloscPP = "91"
        cmd.oplPOSSSLNormalneMies = "3.23"
        cmd.oplPOSSSLNormalnePP = "3.24"
        cmd.oplPOSSSLPreferencyjneMies = "3.25"
        cmd.oplPOSSSLPreferencyjnePP = "3.26"

        cmd.oplPOSWiFiTyp = ""
        cmd.oplPOSWiFiIlosc = "81"
        cmd.oplPOSWiFiIloscPP = "52"
        cmd.oplPOSWiFiNormalneMies = "4.23"
        cmd.oplPOSWiFiNormalnePP = "4.24"
        cmd.oplPOSWiFiPreferencyjneMies = "4.25"
        cmd.oplPOSWiFiPreferencyjnePP = "4.26"

        cmd.oplPOSGPRSTyp = ""
        cmd.oplPOSGPRSIlosc = "10"
        cmd.oplPOSGPRSIloscPP = "12"
        cmd.oplPOSGPRSNormalneMies = "5.23"
        cmd.oplPOSGPRSNormalnePP = "5.24"
        cmd.oplPOSGPRSPreferencyjneMies = "5.25"
        cmd.oplPOSGPRSPreferencyjnePP = "5.26"

        cmd.oplPOSBaza = "21"
    }

    def getZestawPosStawkiPreferencyjne(ProcessCommand cmd) {

    }
}