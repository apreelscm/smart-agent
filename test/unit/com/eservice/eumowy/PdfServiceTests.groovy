package com.eservice.eumowy

import com.eservice.eumowy.util.ProjectPathHelper
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFImageWriter
import grails.test.mixin.*
import org.junit.Before

import java.awt.image.BufferedImage

import static com.eservice.eumowy.PdfTestDataBuilder.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(PdfService)
class PdfServiceTests {

	static Random random = new Random()
	
	public static final int BOARD_MEMBER_1_X = 85;
	public static final int BOARD_MEMBER_1_Y = 58;
	
	public static final int BOARD_MEMBER_2_X = 56;
	public static final int BOARD_MEMBER_2_Y = 59;
	
	static URL url = new PdfServiceTests().getClass().getResource("PdfServiceTests.class");
	static String fileTemplatePath = "otherResources" +File.separator+"pdf_templates" + File.separator;
	static String fileTemplateOutPath = "otherResources" +File.separator+ "pdf_out" + File.separator;
    static String fontsPath = "web-app" +File.separator+ "fonts" + File.separator;

	static Map<String, String[]> data;
	
	public static String getTemplatePath(){
		return ProjectPathHelper.getProjectPath(url) + fileTemplatePath;
	}

    public static String getFontsPath(){
        return ProjectPathHelper.getProjectPath(url) + fontsPath;
    }

    public static String getTemplateOutPath(){
        String path = ProjectPathHelper.getProjectPath(url) + fileTemplateOutPath
        if (! new File(path).exists()){
            new File(path).mkdirs()
        }
        return path;
    }
	
	@Before
	public void init() {
		data = new HashMap<String, String[]>();
		data.putAll(generateCommonFields())
	   // data.putAll(generateAggrementsFields());

        def mockAppParametersService = mockFor(AppParametersService, true)
        mockAppParametersService.demand.getFontUri(){
            -> getFontsPath()
        }

        service.appParametersService =  mockAppParametersService.createMock()
	}

    static HashMap<String, String[]> generateCommonFields(){
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.put("dataUmowy", ["21.03.2013"] as String[]);
        data.put("dataAneksowanejUmowyPos", ["22.04.2013"] as String[]);
        data.put("dataAneksowanejUmowyPrepaid", ["11.05.2013"] as String[]);
        data.put("akceptantNazwa", ["Firma Handlowo Usługowa 'HandUs'"] as String[]);
        data.put("akceptantSiedziba", ["ul. Marszałkowska 3/4; 01-234 Warszawa"] as String[]);
        data.put("reprezentant1", ["Jan Nowak"] as String[]);
        data.put("reprezentant2", ["Grażyna Prymek"] as String[]);
        data.put("phNumer", ["12345"] as String[]);
        return data;
    }

	static HashMap<String, String[]> generateDataPodpisaniaAneksuPOZField(){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("dataPodpisaniaAneksuPOZ", ["23.04.2013"] as String[]);
		return result;
	}

	private static addCheckbox(def data, def pdfName, def fieldValue, def value){
		data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
	}

	void testAPUNTSS() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData())
        data.putAll(prepareAggrementsFields())

		data.put("wydrukGrafikiCena", ["53"] as String[]);
		data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
		data.put("tytulPlatnosciCena", ["53"] as String[]);
		data.put("pierwszaSesjaCena", ["53"] as String[]);
		data.put("walutaObcaCena", ["53"] as String[]);
		data.put("oplataZaUruchomienieDCC", ["200"] as String []);
		data.put("mudCena", ["53"] as String[]);
		data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
		data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomicznyCena", ["153"] as String[]);
        data.putAll(preparePunktyData())
	//	data.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "4", "65", "165", "74", "43"] as String[]);
	//  data.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature2.jpg").toURI().toURL(), "", "signature", "4", "185", "165", "74", "43"] as String[]);
	//	data.put("zarzad1_podpis", [new File(getTemplatePath()+"signature4.jpg").toURI().toURL(), "", "signature", "4", "305", "165", "85", "56"] as String[]);
	//  data.put("zarzad2_podpis", [new File(getTemplatePath()+"signature5.jpg").toURI().toURL(), "", "signature", "4", "435", "165", "58", "59"] as String[]);
	//	process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)
		
		data.putAll(insertSignatures(4, 65, 165, 74, 43))
		process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)
	
	}

	void testAPUNTSZAPOO() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();				
		data.putAll(this.data);
		data.put("dataPoczatkuUzywaniaPOZ", ["11.02.1998"] as String[]);
		data.put("dataKoncaUzywaniaPOZ", ["11.01.1998"] as String[]);
		data.put("numerPOSA", ["10"] as String[]);
		data.put("numerPOSB", ["20"] as String[]);
		data.put("numerPOSC", ["30"] as String[]);
		data.put("numerPOSD", ["40"] as String[]);
		data.put("numerPOSE", ["50"] as String[]);

        data.put("oplataPOSA", ["50"] as String[]);
		data.put("oplataPOSB", ["50"] as String[]);
		data.put("oplataPOSC", ["50"] as String[]);
		data.put("oplataPOSD", ["50"] as String[]);
		data.put("oplataPOSE", ["50"] as String[]);
        data.putAll(prepareZestawPosOdplatneUzywanieData())
		data.putAll(insertSignatures(1, 85, 288, 74, 43))
		process("APUNTSZAPOO3.00212-01-16.pdf", "APUNTSZAPOO3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZAPOU() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);

        data.putAll(prepareZestawPosOdplatneUzywanieData());

		data.put("wydrukGrafikiCena", ["53"] as String[]);
		data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
		data.put("tytulPlatnosciCena", ["53"] as String[]);
		data.put("systemKasowyCena", ["53"] as String[]);
		data.put("pierwszaSesjaCena", ["53"] as String[]);
		data.put("weryfikacjaPINCena", ["53"] as String[]);
		data.put("punktAkceptacjaKart1", ["To jest pełna czterdziestoczteroliterowa naz"] as String[]);
		data.put("adresAkceptacjaKart1", ["1234567890123456789012345678901234567890123456789012345"] as String[]);
		data.put("punktAkceptacjaKart2", ["Pompeczki"] as String[]);
		data.put("adresAkceptacjaKart2", ["Kaszubska 2"] as String[]);
		data.put("punktAkceptacjaKart3", ["Orencz"] as String[]);
		data.put("adresAkceptacjaKart3", ["Pomarańczowa 11"] as String[]);
		data.put("punktAkceptacjaKart4", ["Timobajl"] as String[]);
		data.put("adresAkceptacjaKart4", ["Rózowa 1"] as String[]);
		data.put("platnoscTN1", ["TAK"] as String[]);
		data.put("platnoscTN2", ["NIE"] as String[]);
		data.put("platnoscTN3", ["NIE"] as String[]);
		data.put("platnoscTN4", ["TAK"] as String[]);
		data.put("integracjaTN1", ["TAK"] as String[]);
		data.put("integracjaTN2", ["TAK"] as String[]);
		data.put("integracjaTN3", ["NIE"] as String[]);
		data.put("integracjaTN4", ["TAK"] as String[]);
		data.put("utaTN1", ["NIE"] as String[]);
		data.put("utaTN2", ["NIE"] as String[]);
		data.put("utaTN3", ["NIE"] as String[]);
		data.put("utaTN4", ["NIE"] as String[]);
		data.putAll(insertSignatures(2, 85, 395, 74, 43))
		process("APUNTSZAPOU3.00212-01-16.pdf", "APUNTSZAPOU3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZAWNZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(this.data);
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		data.putAll(prepareDodatkoweUslugiData())

		data.putAll(insertSignatures(1, 85, 370, 74, 43))
		process("APUNTSZAWNZ3.00212-01-16.pdf", "APUNTSZAWNZ3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZDCC() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("walutaObcaCena", ["40"] as String[]);
		data.put("mudCena", ["510"] as String[]);
		data.put("oplataZaUruchomienieWalutyObcej", ["50"] as String[]);
		data.putAll(insertSignatures(1, 85, 280, 74, 43))
		process("APUNTSZDCC2.00212-01-16.pdf", "APUNTSZDCC2.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZDCCZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("walutaObcaCena", ["40"] as String[]);
		data.put("mudCena", ["510"] as String[]);
		data.put("oplataZaUruchomienieWalutyObcej", ["50"] as String[]);
		data.putAll(insertSignatures(1, 85, 260, 74, 43))
		process("APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków.pdf", "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków_out.pdf", data)
	}

	void testAPUNTSZOKOD() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("okresLojalnosciowy", ["3"] as String[]);
		data.putAll(insertSignatures(1, 85, 250, 74, 43))
		process("APUNTSZOKOD2.00312-01-16.pdf", "APUNTSZOKOD2.00312-01-16_out.pdf", data)
	}

	void testAPUNTWAGOK() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 245, 74, 43))
		process("APUNTWAGOK1.00212-01-16.pdf", "APUNTWAGOK1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWAGON() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.put("czasObslugiCena", ["3"] as String[]);
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 235, 74, 43))
		process("APUNTWAGON1.00212-01-16.pdf", "APUNTWAGON1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWAGOP() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 80, 245, 74, 43))
		process("APUNTWAGOP1.00212-01-16.pdf", "APUNTWAGOP1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWANOD() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		data.putAll(insertSignatures(1, 85, 225, 74, 43))
		process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat.pdf", "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat_out.pdf", data)
	}

	void testAPUNTZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		// result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		data.putAll(this.data);
		data.putAll(prepareZestawPosOdplatneUzywanieData())

		data.put("wydrukGrafikiCena", ["53"] as String[]);
		data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
		data.put("tytulPlatnosciCena", ["53"] as String[]);
		data.put("pierwszaSecjaCena", ["53"] as String[]);
		data.put("walutaObcaCena", ["53"] as String[]);
		data.put("mudCena", ["53"] as String[]);
		data.put("pierwszaSesjaCena", ["53"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("okresLojalnosciowy", ["3"] as String[]);
        data.putAll(prepareAggrementsFields())
		data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
		data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomicznyCena", ["153"] as String[]);

        data.putAll(preparePunktyData())
		
		data.put("oplataZaDzienneZestawienieTransakcji", ["200"] as String[]);
		data.put("oplataZaPotwierdzenieWykonaniaPrzelewu", ["22"] as String[]);
		data.put("oplataZaDostarczeniePapieru", ["333"] as String[]);
		data.put("oplataZaMiesieczneZestawienieTransakcji", ["455"] as String[]);
		data.put("oplataZaZmianeGrafiki", ["566"] as String[]);
		data.put("oplataZaInstalacjePOS", ["6575"] as String[]);
		data.put("oplataZaInstalacjeGPRS", ["987"] as String[]);
		data.put("oplataZaUruchomienieWalutyObcej", ["1345"] as String[]);
		data.putAll(insertSignatures(4, 70, 165, 74, 43))
		
	 // process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", result)
		process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", data)
	}

	void testATUSUFDU() {
		// HashMap<String, String[]> result = new HashMap<String, String[]>();
		HashMap<String, String[]> data = new HashMap<String, String[]>();
	 // result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
	 // result.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
		data.putAll(this.data);
		data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
		data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
		data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
		data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
		data.putAll(insertSignatures(2, 80, 545, 74, 43))
	 //	process("ATUSUFDU4.004.130522.pdf", "ATUSUFDU4.004.130522_out.pdf", result)
		process("ATUSUFDU4.004.130522.pdf", "ATUSUFDU4.004.130522_out.pdf", data)
	}

	void testAPUPZBSAIKO() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 70, 155, 74, 43))
		result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "700", "150", "74", "43"] as String[]);
	 // process("APUPZBSAIKO1.00013-03-25 - Aneks IKO.pdf", "APUPZBSAIKO1.00013-03-25 - Aneks IKO_out.pdf", result)
		process("APUPZBSAIKO1.00013-03-25 - Aneks IKO.pdf", "APUPZBSAIKO1.00013-03-25 - Aneks IKO_out.pdf", data)
	}
	
	//----------------------------------------------------------------------------------------
	
	void testAPUPZ2ACB() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(2, 80, 145, 74, 43))
		process("APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback).pdf", "APUPZ2ACB1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow Cashback)_out.pdf", data)
	}
		
	void testAPUPZ2DCC() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
        data.putAll(prepareDccData())
		data.put("noweZestPos", ["true", "", "checkbox"] as String[]);
		data.put("obecneZestPos", ["true", "", "checkbox"] as String[]);
		data.put("phu", ["true", "", "checkbox"] as String[]);
		data.put("punktZakresUruchomienia1", ["Kodziki"] as String[]);
		data.put("adresZakresUruchomienia1", ["Wąwozowa 7"] as String[]);
		data.put("punktZakresUruchomienia2", ["Pompeczki"] as String[]);
		data.put("adresZakresUruchomienia2", ["Kaszubska 2"] as String[]);
		data.put("pos1", ["7"] as String[]);
		data.put("pos2", ["9"] as String[]);
		data.putAll(insertSignatures(1, 85, 215, 74, 43))
		process("APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC).pdf", "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_out.pdf", data)
	}
	
	void testAPUPZACB() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(2, 85, 220, 74, 43))
		process("APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapł (dod Cashback).pdf", "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zapł (dod Cashback)_out.pdf", data)
	}
	
	void testAPUPZAWNZBS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);

        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

		data.putAll(insertSignatures(1, 85, 180, 74, 43))
		process("APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich).pdf", "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich)_out.pdf", data)
	}
	
	void testAPUPZAWNZS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())
		data.putAll(insertSignatures(1, 80, 180, 74, 43))
		process("APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie).pdf", "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie)_out.pdf", data)
	}
	
	void testAPUPZBS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
		data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);

        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())
        data.putAll(preparePunktyData(30))
		data.putAll(insertSignatures(4, 80, 320, 74, 43))
		process("APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku.pdf", "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku_out.pdf", data)
	}

	void testATUSU() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
		data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
		data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
		data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
		data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
        data.putAll(preparePoziomOplatIWarunkiPlatnosciData())
		data.put("akceptantNip", ["3004005003"] as String[]);
		data.put("numerRachunkuBankowegoKlienta", ["33333333333333"] as String[]);
		data.put("bankKlienta", ["33333333333333"] as String[]);
		data.put("miejsceUmowy", ["Kurniki Podlaskie"] as String[]);
		data.putAll(insertSignatures(3, 80, 580, 74, 43))
		data.put("reprezentant1_podpis_gora", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", 7, 360, 432, 74, 43] as String[]);
		data.put("reprezentant1_podpis_dol", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", 7, 360, 50, 74, 43] as String[]);
		process("ATUSU5.00413-05-22.pdf", "ATUSU5.00413-05-22_out.pdf", data)
	}
	
	void testAPUPZIF() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
        data.putAll(prepareDccData())
		data.put("PKOBP", ["3"] as String[]);
		data.putAll(insertSignatures(2, 80, 460, 74, 43))
		process("APUPZIF2.00113-04-05.pdf", "APUPZIF2.00113-04-05_out.pdf", data)
	} // gotowe
	
	void testAPUPZIF2() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaNiezon", ["true", "", "checkbox"] as String[]);
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
		data.put("punkt6", ["AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSD3456"] as String[]);
		data.put("adres6", ["Nieznana ulica  bo nie wiem 34A/259"] as String[]);

        data.putAll(insertSignatures(4, 80, 305, 74, 43))
		process("APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013.pdf", "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013_out.pdf", data)
	}
	
	void testAPUPZDCC() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(prepareDccData())

		data.put("noweZestPos", ["true", "", "checkbox"] as String[]);
		data.put("obecneZestPos", ["true", "", "checkbox"] as String[]);
		data.put("phu", ["true", "", "checkbox"] as String[]);
		data.put("punktZakresUruchomienia1", ["Kodziki i inne teledoładowania co mają długą nazwę"] as String[]);
		data.put("adresZakresUruchomienia1", ["Wąwozowa 7"] as String[]);
		data.put("punktZakresUruchomienia2", ["Pompeczki"] as String[]);
		data.put("adresZakresUruchomienia2", ["Kaszubska 2"] as String[]);
		data.put("pos1", ["7"] as String[]);
		data.put("pos2", ["9"] as String[]);
		data.put("punkt3", ["SuperTele"] as String[]);
		data.put("adres3", ["Parówkowa"] as String[]);
		data.put("pos3", ["9999"] as String[]);
		data.putAll(insertSignatures(1, 80, 240, 74, 43))
		process("APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapł (wprow DCC).pdf", "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zapł (wprow DCC)_out.pdf", data)
	}
	
	void testAPUPZDCCZ() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("zalNumer", ["6"] as String[]);
        data.putAll(prepareDccData())
		data.putAll(insertSignatures(1, 85, 185, 74, 43))
		process("APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC).pdf", "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC)_out.pdf", data)
	}
	
	void testAPUPZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
        data.putAll(prepareAggrementsFields())
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);
        data.putAll(preparePunktyData())
        data.putAll(preparePozopmOplatIWarunkiPlatnosciKartyData())

		data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.putAll(insertSignatures(4, 90, 308, 74, 43))
		process("APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile.pdf", "APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile_out.pdf", data)
	}
	
	// ---------------------------------------------------------------------------------------
    void testFormularzAplikacyjny() {
        HashMap<String, String[]> data = generateFormularzAplikacyjnyFields();
        data.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
        process("Formularz aplikacyjny_po_zmianach_18.01.2012.pdf", "Formularz aplikacyjny_po_zmianach_18.01.2012_out.pdf", data);
    }
	
	void testFormularzDanychPunktu() {
		process("Formularz danych punktu_zmiany_15.05.2013_edited.pdf", "Formularz danych punktu_zmiany_15.05.2013_edited_out.pdf", generateFormularzDanychPunktuFields());
	}

    void testFormularzScoringowy() {
        HashMap<String, String[]> data = prepareScoringData()
        data.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz Scoringowy (oryginal).pdf", "Formularz Scoringowy (oryginal)_out.pdf", data);
    }

    void testFormularzScoringowyToImage() {
        String outFile =  "Formularz Scoringowy (oryginal)_out2.pdf"
        HashMap<String, String[]> data = prepareScoringData()
        data.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
        process("Formularz Scoringowy (oryginal).pdf", outFile, data);
        processToImage(outFile, 1)
    }

	void processToImage(pdfName, pageNumber) {
		PDDocument document = null
		document = PDDocument.load(getTemplateOutPath()+pdfName)
		int resolution = 300
		PDFImageWriter imageWriter = new PDFImageWriter()
		boolean success = imageWriter.writeImage(document, "png", "",
				pageNumber, pageNumber, getTemplateOutPath()+pdfName+"-TEST-", BufferedImage.TYPE_INT_RGB, resolution)
		
		if (!success) {
			log.error "No writer found for PNG image format"
		}
		
		document.close()
		
	}
	
	void process(templateName, outName, data){
		byte[] pdf = service.fillPdfFormFromURI(getTemplatePath()+templateName, data, PdfService.FontType.ARIAL)

		assert pdf != null

		new File(getTemplateOutPath()+outName).withOutputStream {
			it.write pdf
		}
	}

	
	 private HashMap<String, String[]> insertSignatures(int pageNo, int x, int y, int scaleX, int scaleY){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
	//	result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
		result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
		result.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature2.jpg").toURI().toURL(), "", "signature", pageNo, x+120, y, scaleX, scaleY] as String[]);
		result.put("zarzad1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x+250, y, BOARD_MEMBER_1_X, BOARD_MEMBER_1_Y] as String[]);
		result.put("zarzad2_podpis", [new File(getTemplatePath()+"signature3.jpg").toURI().toURL(), "", "signature", pageNo, x+380, y, BOARD_MEMBER_2_X, BOARD_MEMBER_2_Y] as String[]);
		return result;
	}

	private static addCheckboxes(def data, def pdfKeyValue, def value){
		pdfKeyValue.each{ k, v ->  data.put(k, [v.equals(value), "", "checkbox"] as String[])}
	}
}