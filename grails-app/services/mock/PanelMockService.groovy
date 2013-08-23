package mock

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.dictionary.ScoringDictionary

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
        cmd.dataPodpisaniaAneksuPOZ = new Date()
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd) {
        cmd.dataPodpisaniaAneksuPrepaid = new Date()
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd) {
        cmd.umowaCzas =  "nieoznaczony" //TODO -  MOZLIWE, ze pdfach jest to umowaOzn, umowaNieOzn
        cmd.umowaOznOd = new Date()
        cmd.umowaOznDo = new Date()

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
        cmd.oplataVISA = new BigDecimal("124.333")
        cmd.oplataVISAPr = new BigDecimal("12.4")
        cmd.oplataMasterCard = new BigDecimal("0.73")
        cmd.oplataMasterCardPr = new BigDecimal("12.6")
        cmd.oplataMaestro = new BigDecimal("12.93")
        cmd.oplataMasteroPr = new BigDecimal("1.04")
    }

    def getDccZakresUruchomienia(ProcessCommand cmd) {
        cmd.dccZakresUruchomienia = "wskazane"
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd) {
        cmd.informacjaHandlowa = "nie"
    }

    def getDodajPos(ProcessCommand cmd) {

    }

    def getDodajPunkt(ProcessCommand cmd) {

    }

    def getDodatkoweUslugi(ProcessCommand cmd) {
        cmd.oplataZaDzienneZestawienieTransakcji = new BigDecimal("1.23")
        cmd.oplataZaMiesieczneZestawienieTransakcji = new BigDecimal("2.23")
        cmd.oplataZaPotwierdzenieWykonaniaPrzelewu = new BigDecimal("3.23")
        cmd.oplataZaDostarczeniePapieru = new BigDecimal("4.23")
        cmd.oplataZaZmianeGrafiki = new BigDecimal("5.23")
        cmd.oplataZaInstalacjePOS = new BigDecimal("6.23")
        cmd.oplataZaInstalacjeGPRS = new BigDecimal("7.23")
        cmd.oplataZaUruchomienieWalutyObcej = new BigDecimal("8.23")
    }

    def getDodatkoweUslugi2(ProcessCommand cmd) {
        cmd.wydrukGrafikiCena = new BigDecimal("1.40")
        cmd.dzialaniaMatematyczneCena = new BigDecimal("0.32")
        cmd.tytulPlatnosciCena = new BigDecimal("23.45")
        cmd.pierwszaSesjaCena = new BigDecimal("1.34")
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {
        cmd.mudCena = new BigDecimal("54")
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd) {
        cmd.weryfikacjaPINCena = new BigDecimal("123.5")
        cmd.systemKasowyCena = new BigDecimal("2.50")
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd) {

    }

    def getFormaDoladowania(ProcessCommand cmd) {
        cmd.doladowania_tp = true
        cmd.doladowania_tk = false
        cmd.srednia_sprzedaz_doladowan = 123
        cmd.srednia_sprzedaz_doladowan_slownie = "sto dwadzieścia trzy"
    }

    def getFunkcjeTerminala(ProcessCommand cmd) {

    }

    def getIfplus(ProcessCommand cmd) {
        cmd.ifOplataVISA = new BigDecimal("0.34")
        cmd.ifOplataMasterCard = new BigDecimal("1.34")
        cmd.ifOplataDinersClub = new BigDecimal("2.34")
        cmd.ifOplataIKO = new BigDecimal("3.34")
        cmd.ifOplataPKOPB = new BigDecimal("4.43")
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
        cmd.oplataZaUruchomienieDCC = new BigDecimal("25.78")
    }

    def getOplatyDCC(ProcessCommand cmd) {
        cmd.oplataZaPlatnoscWInnejWalucie = new BigDecimal("345.67")
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
        cmd.card_p_1_1 = 0.02
        cmd.card_f_1_1 = new BigDecimal("2.30")
        cmd.card_p_1_2 = 0.02
        cmd.card_f_1_2 = new BigDecimal("2.30")
        cmd.card_p_1_3 = 0.02
        cmd.card_f_1_3 = new BigDecimal("2.30")

        cmd.card_p_2_1 = 0.02
        cmd.card_f_2_1 = new BigDecimal("2.30")
        cmd.card_p_2_2 = 0.02
        cmd.card_f_2_2 = new BigDecimal("2.30")
        cmd.card_p_2_3 = 0.02
        cmd.card_f_2_3 = new BigDecimal("2.30")

        cmd.card_p_3_1_1 = 0.02
        cmd.card_f_3_1_1 = new BigDecimal("2.30")
        cmd.card_p_3_1_2 = 0.02
        cmd.card_f_3_1_2 = new BigDecimal("2.30")
        cmd.card_p_3_2_1 = 0.02
        cmd.card_f_3_2_1 = new BigDecimal("2.30")
        cmd.card_p_3_2_2 = 0.02
        cmd.card_f_3_2_2 = new BigDecimal("2.30")

        cmd.card_p_4_1 = 0.02
        cmd.card_f_4_1 = new BigDecimal("2.30")
        cmd.card_p_4_2 = 0.02
        cmd.card_f_4_2 = new BigDecimal("2.30")
        cmd.card_p_4_3 = 0.02
        cmd.card_f_4_3 = new BigDecimal("2.30")
        cmd.card_p_4_4 = 0.02
        cmd.card_f_4_4 = new BigDecimal("2.30")

        cmd.card_p_5_1 = 0.02
        cmd.card_f_5_1 = new BigDecimal("2.30")
        cmd.card_p_5_2 = 0.02
        cmd.card_f_5_2 = new BigDecimal("2.30")
        cmd.card_p_5_3 = 0.02
        cmd.card_f_5_3 = new BigDecimal("2.30")
        cmd.card_p_5_4 = 0.02
        cmd.card_f_5_4 = new BigDecimal("2.30")

        cmd.card_p_6_1_1 = 0.02
        cmd.card_f_6_1_1 = new BigDecimal("2.30")
        cmd.card_p_6_1_2 = 0.02
        cmd.card_f_6_1_2 = new BigDecimal("2.30")
        cmd.card_p_6_1_3 = 0.02
        cmd.card_f_6_1_3 = new BigDecimal("2.30")
        cmd.card_p_6_2_1 = 0.02
        cmd.card_f_6_2_1 = new BigDecimal("2.30")
        cmd.card_p_6_2_2 = 0.02
        cmd.card_f_6_2_2 = new BigDecimal("2.30")
        cmd.card_p_6_2_3 = 0.02
        cmd.card_f_6_2_3 = new BigDecimal("2.30")
        cmd.card_p_6_4_1 = 0.02
        cmd.card_f_6_4_1 = new BigDecimal("2.30")
        cmd.card_p_6_4_2 = 0.02
        cmd.card_f_6_4_2 = new BigDecimal("2.30")
        cmd.card_p_6_4_3 = 0.02
        cmd.card_f_6_4_3 = new BigDecimal("2.30")

        cmd.card_p_7_1_1 = 0.02
        cmd.card_f_7_1_1 = new BigDecimal("2.30")
        cmd.card_p_7_1_2 = 0.02
        cmd.card_f_7_1_2 = new BigDecimal("2.30")
        cmd.card_p_7_2_1 = 0.02
        cmd.card_f_7_2_1 = new BigDecimal("2.30")
        cmd.card_p_7_2_2 = 0.02
        cmd.card_f_7_2_2 = new BigDecimal("2.30")
        cmd.card_p_7_3 = 0.02
        cmd.card_f_7_3 = new BigDecimal("2.30")

        cmd.card_p_8_1_1 = 0.02
        cmd.card_f_8_1_1 = new BigDecimal("2.30")
        cmd.card_p_8_1_2 = 0.02
        cmd.card_f_8_1_2 = new BigDecimal("2.30")
        cmd.card_p_8_1_3 = 0.02
        cmd.card_f_8_1_3 = new BigDecimal("2.30")
        cmd.card_p_8_2_1 = 0.02
        cmd.card_f_8_2_1 = new BigDecimal("2.30")
        cmd.card_p_8_2_2 = 0.02
        cmd.card_f_8_2_2 = new BigDecimal("2.30")
        cmd.card_p_8_2_3 = 0.02
        cmd.card_f_8_2_3 = new BigDecimal("2.30")
        cmd.card_p_8_3 = 0.02
        cmd.card_f_8_3 = new BigDecimal("2.30")
        cmd.card_p_8_4_1 = 0.02
        cmd.card_f_8_4_1 = new BigDecimal("2.30")
        cmd.card_p_8_4_2 = 0.02
        cmd.card_f_8_4_2 = new BigDecimal("2.30")
        cmd.card_p_8_4_3 = 0.02
        cmd.card_f_8_4_3 = new BigDecimal("2.30")
        cmd.card_p_9 = 0.02
        cmd.card_f_9 = new BigDecimal("2.30")
        cmd.card_p_10 = 0.02
        cmd.card_f_10 = new BigDecimal("2.30")
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd) {
        cmd.pp_orange_tk = 2.3
        cmd.pp_orange_tp = 1.35
        cmd.pp_plus_tk = 0.48
        cmd.pp_plus_tp = 23.1
        cmd.pp_tmobile_tk = 0.01
        cmd.pp_tmobile_tp = 0.32
        cmd.pp_heyah_tk = 0.45
        cmd.pp_heyah_tp = 0.03
        cmd.pp_play_tk = 34.0
        cmd.pp_play_tp = 0.25
        cmd.pp_telegrosik_tk = 1.05
        cmd.pp_virginmobile_tk = 3
        cmd.pp_lycamobile_tk = 2.50
        cmd.pp_gtmobile_tk = 5.02
        cmd.pp_vectonemobile_tk = 2.34
        cmd.pp_delightmobile_tk = 1.23
        cmd.oplataZaOprogramowanieDoDoladowan = new BigDecimal(0.003.toString())
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd) {

    }

    def getRachunekBankowyKlienta(ProcessCommand cmd) {
        cmd.numerRachunkuBankowego = "11 1160 2202 0000 0001 9389 8247"
        cmd.bank = "3"
    }

    def getScoring(ProcessCommand cmd) {

        cmd.scoringDzialalnosc = "1"

        cmd.scoringIloscTransakcji = 2

        cmd.scoringCzestoscTransakcji = 4

        cmd.scoringOtwartyZamkniety = "nieczynne"
        cmd.scoringStanZadbany = true

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

        cmd.scoringSprzedazTowarowEkskluzywnych = true
        cmd.scoringPonad50ProcentObrotowWNocy = true
        cmd.scoringRuchTurystycznyPrzygraniczny = true
        cmd.scoringUslugiPlatneZGory = true

        cmd.scoringDochodowosc = new BigDecimal("500")

        cmd.scoringDeklaracjaFinansowa = "wlasciwe"
        cmd.scoringDeklaracjaFinansowaObrotOgolem = new BigDecimal("23.50")
        cmd.scoringDeklaracjaFinansowaObrotNaKarty = new BigDecimal("12.45")
        cmd.scoringDeklaracjaFinansowaSredniObrot = new BigDecimal("10.45")
        cmd.scoringDeklaracjaFinansowaSredniaTransakcja = new BigDecimal("3.45")

    }

    def getSerwis(ProcessCommand cmd) {
        cmd.obslugaTyp = "economic"
        cmd.obslugaEkonomicznyCena = new BigDecimal("123.50")
    }

    def getSerwisEkonomiczny(ProcessCommand cmd) {
        //korzysta z pola cmd.obslugaEkonomicznyCena z getSerwis
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
        cmd.dataUmowy = new Date()
        cmd.miejsceUmowy = "Warszawa"
    }

    def getUwagi(ProcessCommand cmd) {
        cmd.notes = "Ala ma kota";
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd) {

    }

    def getZalaczniki(ProcessCommand cmd) {

    }

    def getZestawPos(ProcessCommand cmd) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd) {

    }

    def getZestawPosStawkiPreferencyjne(ProcessCommand cmd) {

    }
}