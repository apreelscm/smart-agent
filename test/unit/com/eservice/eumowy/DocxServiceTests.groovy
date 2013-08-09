package com.eservice.eumowy

import eservice.docx.converter.domain.Acceptor
import eservice.docx.converter.domain.AggreementTime
import eservice.docx.converter.domain.Dokument
import eservice.docx.converter.domain.PH
import eservice.docx.converter.domain.POMPoint
import eservice.docx.converter.domain.POS
import eservice.docx.converter.domain.POSPayment
import eservice.docx.converter.domain.Representant
import grails.test.mixin.*
import org.junit.*

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DocxService)
class DocxServiceTests {

    static Dokument dokument
    static Random random = new Random()

    static String templatePath = "web-app" +File.separator+ "files" +File.separator+"docx_templates" + File.separator
    static String outPath = "web-app"+File.separator+"files"+File.separator+"docx_out" + File.separator
    static String signaturePath = "web-app"+File.separator+"files"+ File.separator

    @BeforeClass
    static void init(){

        List firstNames = ["Zenon", "Jan", "Henryk", "Marian", "Kazimierz", "Henryka", "Apolonia"];
        List lastNames = ["Nowak", "Strus", "Dziurek", "Soltys", "Boruc", "Wojak", "Bluszcz"];
        List signatures = [signaturePath + "signature1.jpg", signaturePath + "signature2.jpg", signaturePath + "signature3.jpg"];
        List names = ["Arabic Oil", "UltraSoft", "Sports Foot", "Toys & Fun", "\"Vendinsor\" - Chemical Enterprise",
                "German Insurance Company", "International Developer \"Investor\""];
        List address = ["3610 Upton Street, NW", " Placu Trzech Krzyży 4/5", "5 Aleja w Nowym Yorku", "Causeway Bay w Hong Kong", "CampNou", "Traffalgar Square 3", "Marszalkowska 44 m. 78"];

        List cities = ["Warszawa", "Krakow", "Siedlce", "Gdynia", "Otwock"]
        List streets = ["Zielona", "Marszalkowska", "Mazurska", "Dluga", "Wojskowa"]

        List type = ["Sklep spozywczy", "Kiosk Ruchu", "Pizzeria", "Kino"]
        List name = ["Bajka", "Okazja", "U Gosi", "Delikatesy", "Dwa kolka"]

        PH ph = new PH(imie: getRandom(firstNames), nazwisko: getRandom(lastNames), numerSprzedazowy: getRandomFormattedInt(100000), nazwaPlikuPodpisu: getRandom(signatures));

        Representant firstRepresentant = new Representant(imie: getRandom(firstNames), nazwisko: getRandom(lastNames), nazwaPlikuPodpisu: getRandom(signatures));
        Acceptor acceptor = new Acceptor(nazwa: getRandom(names), siedziba: getRandom(address),
                pierwszyReprezentant: firstRepresentant);

        if (random.nextInt(2) == 1){
            Representant secondRepresentant = new Representant(imie: getRandom(firstNames), nazwisko: getRandom(lastNames), nazwaPlikuPodpisu: getRandom(signatures));
            acceptor.drugiReprezentant = secondRepresentant;
        }

        List POMPoints = []
        for ( i in 0..random.nextInt(20)) {
            POMPoints.add(new POMPoint(
                    pelnaNazwaPunktu: getRandom(type) + " \"" + getRandom(name) + "\"",
                    ulicaINumerDomu: getRandom(streets) + " 4/6",
                    miejscowosc: getRandom(cities),
                    kodPocztowy: "09-123",
                    liczbaZestawowZUslugaDCC: getRandomFormattedInt(10)))
        }


        List POSPayments = []
        for ( i in 0..random.nextInt(5)) {
            POSPayments.add(new POSPayment(
                numerLogiczny: random.nextInt(2000),
                oplataZaUzytkowanie: getRandomFormattedDouble(300)
            ));
        }

        List POSPrices = []
        for ( i in 0..random.nextInt(3)) {
            POSPrices.add(new POS(
                    ilosc: random.nextInt(20),
                    oplata: getRandomFormattedDouble(300)
            ));
        }

        List POSPreferedPrices = []
        for ( i in 0..random.nextInt(3)) {
            POSPreferedPrices.add(new POS(
                    ilosc: random.nextInt(20),
                    oplata: getRandomFormattedDouble(300)
            ));
        }

        dokument = new Dokument(
                ph: ph,
                akceptant: acceptor,
                dataPodpisaniaUmowy: getRandomFormattedDate(),
                dataPodpisaniaAneksu: getRandomFormattedDate(),
                oplataZaTransakcjeInternetowe: getRandomFormattedDouble(200),
                oplataZaPrzyjmowanieZaplatyKartamiWObcejWalucie: getRandomFormattedDouble(200),
                oplataZaUruchomieniePrzyjmowaniaZaplatyKartamiWObcejWalucie: getRandomFormattedDouble(200),

                marzaPKOPBZaFaktureKorygujacaPr: getRandomFormattedDouble(1),
                oplataZaKarteVISAPr: getRandomFormattedDouble(1),
                oplataZaKarteMasterCardPr: getRandomFormattedDouble(1),
                oplataZaKarteDinersClubPr: getRandomFormattedDouble(1),

                oplataZaTransakcjeKartaVISAPr: getRandomFormattedDouble(1),
                oplataZaTransakcjeKartaVISAPLN: getRandomFormattedDouble(5),
                oplataZaTransakcjeKartaMasterCardPr: getRandomFormattedDouble(1),
                oplataZaTransakcjeKartaMasterCardPLN: getRandomFormattedDouble(5),
                oplataZaTransakcjeKartaMaestroPr: getRandomFormattedDouble(1),
                oplataZaTransakcjeKartaMaestroPLN: getRandomFormattedDouble(5),

                karencjaOdPodpisaniaAneksu: getRandomFormattedInt(48),
                karencjaOdInstalacjiPOZ: getRandomFormattedInt(48),

                zgodaNaElektronicznaFaktureVAT: new Boolean(true),
                zgodaNaOtrzymywanieInformacjiHandlowych: new Boolean(false),

                pomPoints: POMPoints,

                czasUmowy: new AggreementTime(czasOkreslony: new Boolean(false), poczatek: getRandomFormattedDate(), koniec: getRandomFormattedDate()),

                dataStartuSerwisuPOS: getRandomFormattedDate(),
                kosztObslugiSerwisuPOS: getRandomFormattedDouble(20),

                obslugaTransakcji: Dokument.forLocalisation,

                dataPoczatkuUzytkowaniaPOS: getRandomFormattedDate(),
                dataKoncaUzytkowaniaPOS: getRandomFormattedDate(),
                platnosciPOS: POSPayments,

                //_APUNTSS1.00312-01-16_OUT
                dataPoczatkuWydrukuGrafiki: getRandomFormattedDate(),
                oplataZaWydrukGrafiki: getRandomFormattedDouble(10),

                dataPoczatkuDzialanMatematycznych: getRandomFormattedDate(),
                oplataZaDzialaniaMatematyczne: getRandomFormattedDouble(10),

                dataPoczatkuTytuluPlatnosci: getRandomFormattedDate(),
                oplataZaTytulPlatnosci: getRandomFormattedDouble(10),

                dataIntegracjiZSystememKasowym: getRandomFormattedDate(),
                oplataZaIntegracjeZSystememKasowym: getRandomFormattedDouble(15),

                dataWeryfikacjiKoduPIN: getRandomFormattedDate(),
                oplataZaWeryfikacjeKoduPIN: getRandomFormattedDouble(10),

                dataPoczatkuPierwszejSesjiRozliczeniowej: getRandomFormattedDate(),
                oplataZaPierwszaSesjeRozliczeniowa: getRandomFormattedDouble(10),

                dataPoczatkuGwarantowanegoCzasuObslugi: getRandomFormattedDate(),
                opcjaDodatkowegoCzasuObslugi: Dokument.economic,
                oplataZaEkonomicznyCzasObslugi: getRandomFormattedDouble(20),

                oplatyZaPOS: POSPrices,
                preferencyjneOplatyZaPOS: POSPreferedPrices,
                odroczenieOplatyPOS: random.nextInt(12),
                okresUmowyOplatyPOS: random.nextInt(12),

                oplataZaDzienneZestawienieTransakcji: getRandomFormattedDouble(20),
                oplataZaMiesieczneZestawienieTransakcji: getRandomFormattedDouble(10),
                oplataZaPotwierdzenieWykonaniaPrzelewu: getRandomFormattedDouble(30),
                oplataZaDostarczeniePapieru: getRandomFormattedDouble(40),
                oplataZaInstalacjePOS: getRandomFormattedDouble(30),
                oplataZaInstalacjeGPRS: getRandomFormattedDouble(10),
                oplataZaZmianeGrafiki: getRandomFormattedDouble(15)
        );
    }


    void testAPUPZBS2SMALLTemplate() {
        service.convertWithDocxObject(templatePath + "____APUPZBS2.00013-01-25_SMALL_TEMPLATE.docx", outPath + "____APUPZBS2.00013-01-25_SMALL_OUT.docx", dokument);
    }

    void test_APUNTSZAPOU3Template2() {
        service.convertWithDocxObject(templatePath + "____APUNTSZAPOU3.00212-01-16_TABLE_FOOTER_TEMPLATE.docx", outPath + "____APUNTSZAPOU3.00212-01-16_TABLE_FOOTER_OUT.docx", dokument);
    }

    void testTabelkiTemplate() {
        service.convertWithDocxObject(templatePath + "____TestTabelki_TEMPLATE.docx", outPath + "____TestTabelki_OUT.docx", dokument);
    }

    void testKolumnTemplate() {
        service.convertWithDocxObject(templatePath + "____test_TEMPLATE.docx", outPath + "____test_OUT.docx", dokument);
    }

    void testAPUNTSZDCC2Template() {
        service.convertWithDocxObject(templatePath + "APUNTSZDCC2.00212-01-16_TEMPLATE.docx", outPath + "APUNTSZDCC2.00212-01-16_OUT.docx", dokument);
    }

    void testAPUNTSZDCCZ1Template() {
        service.convertWithDocxObject(templatePath + "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow_TEMPLATE.docx", outPath + "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunkow_OUT.docx", dokument);
    }

    void testAPUPZ2ACB1Template() {
        service.convertWithDocxObject(templatePath + "APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_TEMPLATE.docx", outPath + "APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_OUT.docx", dokument);
    }

    void testAPUPZBSAIKO1Template() {
        service.convertWithDocxObject(templatePath + "APUPZBSAIKO1.00013-03-25 - Aneks IKO_TEMPLATE.docx", outPath + "APUPZBSAIKO1.00013-03-25 - Aneks IKO_OUT.docx", dokument);
    }

    void testAPUPZIF2Template() {
        service.convertWithDocxObject(templatePath + "APUPZIF2.00113-04-05_TEMPLATE.docx", outPath + "APUPZIF2.00113-04-05_OUT.docx", dokument);
    }

    void testAPUPZIF2_2013Template() {
        service.convertWithDocxObject(templatePath + "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013_TEMPLATE.docx", outPath + "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013_OUT.docx", dokument);
    }

    void testAPUPZDCC2Template() {
        service.convertWithDocxObject(templatePath + "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapł (wprow DCC)_TEMPLATE.docx", outPath + "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapł (wprow DCC)_OUT.docx", dokument);
    }

    void testAPUPZDCCZ1Template() {
        service.convertWithDocxObject(templatePath + "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC)_TEMPLATE.docx", outPath + "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC)_OUT.docx", dokument);
    }

    void testAPUPZ2Template() {
        service.convertWithDocxObject(templatePath + "APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile_TEMPLATE.docx", outPath + "APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile_OUT.docx", dokument);
    }

    void testAPUPZ2DCC1Template() {
        service.convertWithDocxObject(templatePath + "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_TEMPLATE.docx", outPath + "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_OUT.docx", dokument);
    }

    void testAPUPZACB2Template() {
        service.convertWithDocxObject(templatePath + "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapł (dod Cashback)_TEMPLATE.docx", outPath + "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapł (dod Cashback)_OUT.docx", dokument);
    }

    void testAPUPZBS2Template() {
        service.convertWithDocxObject(templatePath + "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku_TEMPLATE.docx", outPath + "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku_OUT.docx", dokument);
    }

    void testAPUNTSZOKOD2Template() {
        service.convertWithDocxObject(templatePath + "APUNTSZOKOD2.00312-01-16_TEMPLATE.docx", outPath + "APUNTSZOKOD2.00312-01-16_OUT.docx", dokument);
    }

    void testAPUNTWAGOK1Template() {
        service.convertWithDocxObject(templatePath + "APUNTWAGOK1.00212-01-16_TEMPLATE.docx", outPath + "APUNTWAGOK1.00212-01-16_OUT.docx", dokument);
    }

    void testAPUNTWAGON1Template() {
        service.convertWithDocxObject(templatePath + "APUNTWAGON1.00212-01-16_TEMPLATE.docx", outPath + "APUNTWAGON1.00212-01-16_OUT.docx", dokument);
    }

    void testAPUNTWAGOP1Template() {
        service.convertWithDocxObject(templatePath + "APUNTWAGOP1.00212-01-16_TEMPLATE.docx", outPath + "APUNTWAGOP1.00212-01-16_OUT.docx", dokument);
    }

    void testAPUNTWANOD1Template() {
        service.convertWithDocxObject(templatePath + "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat_TEMPLATE.docx", outPath + "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat_OUT.docx", dokument);
    }

    void testAPUPZAWNZBS1Template() {
        service.convertWithDocxObject(templatePath + "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich)_TEMPLATE.docx", outPath + "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich)_OUT.docx", dokument);
    }

    void testAPUPZAWNZS1Template() {
        service.convertWithDocxObject(templatePath + "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie)_TEMPLATE.docx", outPath + "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie)_OUT.docx", dokument);
    }

    void testAPUNTSZAPOO3Template() {
        service.convertWithDocxObject(templatePath + "APUNTSZAPOO3.00212-01-16_TEMPLATE.docx", outPath + "APUNTSZAPOO3.00212-01-16_OUT.docx", dokument);
    }

    void test_APUNTSS1Template() {
        service.convertWithDocxObject(templatePath + "_APUNTSS1.00312-01-16_TEMPLATE.docx", outPath + "_APUNTSS1.00312-01-16_OUT.docx", dokument);
    }

    void test_APUNTSZAPOU3Template() {
        service.convertWithDocxObject(templatePath + "_APUNTSZAPOU3.00212-01-16_TEMPLATE.docx", outPath + "_APUNTSZAPOU3.00212-01-16_OUT.docx", dokument);
    }

    void test_APUNTSZAWNZ3Template() {
        service.convertWithDocxObject(templatePath + "_APUNTSZAWNZ3.00212-01-16_TEMPLATE.docx", outPath + "_APUNTSZAWNZ3.00212-01-16_OUT.docx", dokument);
    }

    void test_APUNTZ2Template() {
        service.convertWithDocxObject(templatePath + "_APUNTZ2.00312-01-16_TEMPLATE.docx", outPath + "_APUNTZ2.00312-01-16_OUT.docx", dokument);
    }

    void test_ATUSU5Template() {
        service.convertWithDocxObject(templatePath + "_ATUSU5.00413-05-22_TEMPLATE.docx", outPath + "_ATUSU5.00413-05-22_OUT.docx", dokument);
    }

    void test_ATUSUFDU4Template() {
        service.convertWithDocxObject(templatePath + "_ATUSUFDU4.004.130522_TEMPLATE.docx", outPath + "_ATUSUFDU4.004.130522_OUT.docx", dokument);
    }

    static String getRandom(List list){
        return list[random.nextInt(list.size())];
    }

    static String getRandomFormattedDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar gc = new GregorianCalendar();
        gc.set(gc.YEAR, randBetween(1900, 2013));
        gc.set(gc.DAY_OF_YEAR, randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR)));

        return dateFormat.format(gc.getTime());
    }

    static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    static String getRandomFormattedDouble(int max){
        return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
    }

    static String getRandomFormattedInt(int max){
        return ""+random.nextInt(max);
    }
}
