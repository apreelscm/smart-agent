package mock
import com.eservice.eumowy.command.AllPointsCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.util.DateUtils

class PanelMockService {

    def init(ProcessCommand cmd){
        cmd.scoringMcc = "1234"
    }

    def getAdresacjaSeciowa(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd) {
        cmd.akceptantKontaktUlicaTytul = "Ulica"
        cmd.akceptantKontaktUlica = "Zielona"
        cmd.akceptantKontaktNrDomu = "23A"
        cmd.akceptantKontaktNrMieszkania = "1"

        cmd.akceptantKontaktMiasto = "Cieszyn"
        cmd.akceptantKontaktKodPocztowy = "12-234"
        cmd.akceptantKontaktPoczta = "Kraków"
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd) {
        cmd.dataAneksowanejUmowyPos = DateUtils.getCurrentFormattedDate()
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd) {
        cmd.dataAneksowanejUmowyPrepaid = DateUtils.getCurrentFormattedDate()
    }

    def getListaAktywnychPunktow(ProcessCommand cmd) {
    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd) {
        cmd.umowaCzas = "nieoznaczony" //TODO -  MOZLIWE, ze pdfach jest to umowaOzn, umowaNieOzn
        cmd.umowaOznOd = "" //DateUtils.getCurrentFormattedDate()
        cmd.umowaOznDo = "" //DateUtils.getCurrentFormattedDate()

    }

    def getDaneAkceptanta(ProcessCommand cmd) {
        cmd.akceptantNazwaOficjalna= "KGHM Polska Miedź S.A."
        cmd.akceptantNazwaSieciowa= ""// "KGHM nazwa sieciowa"
        cmd.akceptantRegon = "123456789"

        cmd.akceptantNazwaOficjalnaCbd= cmd.akceptantNazwaOficjalna
        cmd.akceptantNazwaSieciowaCbd= cmd.akceptantNazwaSieciowa
        cmd.akceptantRegonCbd = cmd.akceptantRegon
    }

    def getDaneDoWydruku(ProcessCommand cmd) {
        cmd.nazwaDoWydrukuZTerminalaPos = "PHU \"U Basi\""
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
        cmd.oplataVISA = "24.33"
        cmd.oplataVISAPr = "12.42"
        cmd.oplataMasterCard = "0.73"
        cmd.oplataMasterCardPr = "12.61"
        cmd.oplataMaestro = "12.93"
        cmd.oplataMaestroPr = "1.04"
    }

    def getDccZakresUruchomienia(ProcessCommand cmd) {
        cmd.dccZakresUruchomienia = "obecne_i_nowe"

		def p1 = new AllPointsCommand()
		def p2 = new AllPointsCommand()
		def p3 = new AllPointsCommand()

		p1.id = 1
		p1.nazwa = 'Sklep spozywczy'
		p1.ulica = 'Zielona'
		p1.miejscowosc = 'Lubartów'
		p1.nrBudynku = 12
		p1.kodPocztowy = '02-123'
		p1.liczbaPos = 23
		p2.id = 2
		p2.nazwa = 'Kwiaciarnia Róża'
		p2.ulica = 'Zielona'
		p2.miejscowosc = 'Lubartów'
		p2.nrBudynku = 12
		p2.kodPocztowy = '02-123'
		p2.liczbaPos = 6
		p3.id = 3
		p3.nazwa = 'Kino Femina'
		p3.ulica = 'Zielona'
		p3.miejscowosc = 'Lubartów'
		p3.nrBudynku = 12
		p3.kodPocztowy = '02-123'
		p3.liczbaPos = 2

		cmd.allPoints = [p1, p2, p3]
    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd) {
        cmd.informacjaHandlowa = "false"
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
    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {
        cmd.mudCena = "54"
    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd) {
        cmd.weryfikacjaPINCena = "123.51"
        cmd.systemKasowyCena = "2.50"
    }

    def getDodatkoweWyposazenie(ProcessCommand cmd) {

    }

    def getFormaDoladowania(ProcessCommand cmd) {
        cmd.doladowania_tp = "true"
        cmd.doladowania_tk = "false"
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
        cmd.okresLojalnosciowy = "1"
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
        cmd.kontaktEmail = "szkup.pawel@gmail.com"
    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd) {
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
        cmd.visaEUKKOPr = "0.02"
        cmd.visaEUKKOSt = "2.30"
        cmd.visaEUKDPr = "0.02"
        cmd.visaEUKDSt = "2.30"
        cmd.visaEUKBPr = "0.02"
        cmd.visaEUKBSt = "2.30"

        cmd.visaOutEUKKOPr = "0.02"
        cmd.visaOutEUKKOSt = "2.30"
        cmd.visaOutEUKDPr = "0.02"
        cmd.visaOutEUKDSt = "2.30"
        cmd.visaOutEUKBPr = "0.02"
        cmd.visaOutEUKBSt = "2.30"

        cmd.visaPolskaKKO1Pr = "0.02"
        cmd.visaPolskaKKO1St = "2.30"
        cmd.visaPolskaKKO2Pr = "0.02"
        cmd.visaPolskaKKO2St = "2.30"
        cmd.visaPolskaKD1Pr = "0.02"
        cmd.visaPolskaKD1St = "2.30"
        cmd.visaPolskaKD2Pr = "0.02"
        cmd.visaPolskaKD2St = "2.30"
        cmd.visaPolskaKBPr = "0.03"
        cmd.visaPolskaKBSt = "2.33"

        cmd.mastercardEUKKPr = "0.02"
        cmd.mastercardEUKKSt = "2.30"
        cmd.mastercardEUKDPr = "0.02"
        cmd.mastercardEUKDSt = "2.30"
        cmd.mastercardEUKBLPr = "0.02"
        cmd.mastercardEUKBLSt = "2.30"
        cmd.mastercardEUMPr = "0.02"
        cmd.mastercardEUMSt = "2.30"

        cmd.mastercardOutEUKKPr = "0.02"
        cmd.mastercardOutEUKKSt = "2.30"
        cmd.mastercardOutEUKDPr = "0.02"
        cmd.mastercardOutEUKDSt = "2.30"
        cmd.mastercardOutEUKBPr = "0.02"
        cmd.mastercardOutEUKBSt = "2.30"
        cmd.mastercardOutEUMPr = "0.02"
        cmd.mastercardOutEUMSt = "2.30"

        cmd.mastercardPolskaKK1Pr = "0.02"
        cmd.mastercardPolskaKK1St = "2.30"
        cmd.mastercardPolskaKK2Pr = "0.02"
        cmd.mastercardPolskaKK2St = "2.30"
        cmd.mastercardPolskaKK3Pr = "0.02"
        cmd.mastercardPolskaKK3St = "2.30"
        cmd.mastercardPolskaKD1Pr = "0.02"
        cmd.mastercardPolskaKD1St = "2.30"
        cmd.mastercardPolskaKD2Pr = "0.02"
        cmd.mastercardPolskaKD2St = "2.30"
        cmd.mastercardPolskaKD3Pr = "0.02"
        cmd.mastercardPolskaKD3St = "2.30"
		cmd.mastercardPolskaKBPr  = "0.02"
		cmd.mastercardPolskaKBSt  = "2.30"
        cmd.mastercardPolskaM1Pr = "0.02"
        cmd.mastercardPolskaM1St = "2.30"
        cmd.mastercardPolskaM2Pr = "0.02"
        cmd.mastercardPolskaM2St = "2.30"
        cmd.mastercardPolskaM3Pr = "0.02"
        cmd.mastercardPolskaM3St = "2.30"

        cmd.visaPKOBPKKO1Pr = "0.02"
        cmd.visaPKOBPKKO1St = "2.30"
        cmd.visaPKOBPKKO2Pr = "0.02"
        cmd.visaPKOBPKKO2St = "2.30"
        cmd.visaPKOBPKD1Pr = "0.02"
        cmd.visaPKOBPKD1St = "2.30"
        cmd.visaPKOBPKD2Pr = "0.02"
        cmd.visaPKOBPKD2St = "2.30"
        cmd.visaPKOBPKB3Pr = "0.02"
        cmd.visaPKOBPKB3St = "2.30"

        cmd.mastercardPKOBPKK1Pr = "0.02"
        cmd.mastercardPKOBPKK1St = "2.30"
        cmd.mastercardPKOBPKK2Pr = "0.02"
        cmd.mastercardPKOBPKK2St = "2.30"
        cmd.mastercardPKOBPKK3Pr = "0.02"
        cmd.mastercardPKOBPKK3St = "2.30"
        cmd.mastercardPKOBPKD1Pr = "0.02"
        cmd.mastercardPKOBPKD1St = "2.30"
        cmd.mastercardPKOBPKD2LPr = "0.02"
        cmd.mastercardPKOBPKD2LSt = "2.30"
        cmd.mastercardPKOBPKD3Pr = "0.02"
        cmd.mastercardPKOBPKD3St = "2.30"
        cmd.mastercardPKOBPKBPr = "0.02"
        cmd.mastercardPKOBPKBSt = "2.30"
        cmd.mastercardPKOBPM1Pr = "0.02"
        cmd.mastercardPKOBPM1St = "2.30"
        cmd.mastercardPKOBPM2Pr = "0.02"
        cmd.mastercardPKOBPM2St = "2.30"
        cmd.mastercardPKOBPM3Pr = "0.02"
        cmd.mastercardPKOBPM3St = "2.30"
        cmd.dinersClubPr = "0.02"
        cmd.ikoSt = "0.02"
    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd) {
        cmd.pp_orange_tk = "2.31"
        cmd.pp_orange_tp = "1.35"
        cmd.pp_plus_tk = "0.48"
        cmd.pp_plus_tp = "23.21"
        cmd.pp_tmobile_tk = "0.01"
        cmd.pp_tmobile_tp = "0.32"
        cmd.pp_heyah_tk = "0.45"
        cmd.pp_heyah_tp = "0.03"
        cmd.pp_play_tk = "34.01"
        cmd.pp_play_tp = "0.25"
        cmd.pp_telegrosik_tk = "1.05"
        cmd.pp_virginmobile_tk = "3"
        cmd.pp_lycamobile_tk = "2.50"
        cmd.pp_gtmobile_tk = "5.02"
        cmd.pp_vectonemobile_tk = "2.34"
        cmd.pp_delightmobile_tk = "1.23"
        cmd.oplataZaOprogramowanieDoDoladowan = "0.03"
    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd) {

		/*def p1 = new AllPosCommand()
		def p2 = new AllPosCommand()
		def p3 = new AllPosCommand()

		p1.id = 1
		p1.numerZestawuPos = '234'
		p1.dataOd = '2011-05-21'
		p1.dataDo = '2012-05-20'
		p1.wysokoscOplaty = '12.25'
		p1.czyWybrany = true

		p2.id = 2
		p2.numerZestawuPos = '567'
		p2.dataOd = '2012-05-21'
		p2.dataDo = '2013-05-20'
		p2.wysokoscOplaty = '15.99'
		p2.czyWybrany = true

		p3.id = 3
		p3.numerZestawuPos = '154'
		p3.dataOd = '2013-05-21'
		p3.dataDo = '2014-05-20'
		p3.wysokoscOplaty = '23.40'
		p3.czyWybrany = true

		cmd.allPoses = [p1, p2, p3]*/
    }

    def getZestawPosWymianaTerminala(ProcessCommand cmd){
    }

    def getRachunekBankowyKlienta(ProcessCommand cmd) {
        cmd.numerRachunkuBankowegoKlienta = "11 1160 2202 0000 0001 9389 8247"
        cmd.bankKlienta = "3"
    }

    def getScoring(ProcessCommand cmd) {

        cmd.scoringDzialalnosc = "uslugi"
        cmd.scoringSzczegolyDzialalnosci = "Handel zywym towarem"

        cmd.scoringIloscTransakcji = "5-10"

        cmd.scoringCzestoscTransakcji = "kilka_tygodniowo"

        cmd.scoringOtwartyZamkniety = "nieczynne"
        cmd.scoringStanZadbany = "true"

        cmd.scoringWielkoscMiejscowosci = "100-500"

        cmd.scoringLokalizacjaPunktu = "centrum_miasta"
        cmd.scoringTypPunktu = "centrum_handlowe"
        cmd.scoringTypPunktuInny = "butik"

        cmd.scoringWielkoscPunktu = "400<"

        cmd.scoringAkceptacja = "true"

        cmd.scoringMonitoring = "false"

        cmd.scoringDzialalnoscCzas = "5<"

        cmd.scoringCharakterystyka = "inny"
        cmd.scoringCharakterystykaInna = "kwiaciarnia"

        cmd.scoringKoncesja = "true"
        cmd.rodzajZezwolenia = "Produkcja bimbru"

        cmd.scoringWlasnosc = "wlasnosc"

        cmd.scoringDzialalnoscCzas = "<1"

        cmd.scoringSprzedazTowarowEkskluzywnych = "true"
        cmd.scoringPonad50ProcentObrotowWNocy = "false"
        cmd.scoringRuchTurystycznyPrzygraniczny = "true"
        cmd.scoringUslugiPlatneZGory = "false"

        cmd.scoringDochodowosc = "500"

        cmd.scoringDeklaracjaFinansowa = "w"
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
        cmd.obslugaEkonomicznyCena = "12"
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
        cmd.akceptantNrDomu = "5"
        cmd.akceptantNrMieszkania = "7"

        cmd.akceptantMiasto = "Warszawa"
        cmd.akceptantKodPocztowy = "98-765"
        cmd.akceptantPoczta = "Olsztyn"

        cmd.akceptantTelStacjonarny = "(22)4556789"
        cmd.akceptantFax = "1234567"
        cmd.akceptantTelKomorkowy ="509876543"

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

    def getUmowa2(ProcessCommand cmd) {
        cmd.miejsceUmowy = "Warszawa"
    }

    def getUwagi(ProcessCommand cmd) {
        cmd.notes = "Ala ma kota";
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd) {
        /*
		
		def p1 = new AllPointsCommand()
		def p2 = new AllPointsCommand()
		def p3 = new AllPointsCommand()
		
        p1.id = 1 
		p1.nazwa = 'Sklep spozywczy'
		p1.ulica = 'Zielona'
		p1.miejscowosc = 'Lubartów'
		p1.nrBudynku = '5A'
		p1.kodPocztowy = '02-123'
		p1.czyWybrany = true
        p2.id = 2
		p2.nazwa = 'Kwiaciarnia Róża'
		p2.ulica = 'Zielona'
		p2.miejscowosc = 'Lubartów'
		p2.nrBudynku = '12'
		p2.kodPocztowy = '02-123'
		p2.uta = true
        p3.id = 3
		p3.nazwa = 'Kino Femina'
		p3.ulica = 'Zielona'
		p3.miejscowosc = 'Lubartów'
		p3.nrBudynku = '93'
		p3.kodPocztowy = '02-123'
		p3.systemKasowy = true
		
		cmd.allPoints = [p1, p2, p3]
		log.info "Punkty: " + cmd.allPoints*/
    }

    def getZalaczniki(ProcessCommand cmd) {

    }

    def getZestawPos(ProcessCommand cmd) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd) {

    }

}