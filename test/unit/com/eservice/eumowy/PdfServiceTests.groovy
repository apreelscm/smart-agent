package com.eservice.eumowy

import grails.test.mixin.*

import java.text.DecimalFormat

import org.junit.*

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
	static String fileTemplatePath = File.separator+"otherResources" +File.separator+"pdf_templates" + File.separator;
	static String fileTemplateOutPath = File.separator+"otherResources" +File.separator+ "pdf_out" + File.separator;
	static Map<String, String[]> data;
	
	private static String getProjectPath() {
		String urlString = url.toString();
		if (urlString.contains("target")){
			return urlString.substring(0,urlString.indexOf("target")).replace("file:/", "");
		} else {
			return urlString.substring(0,urlString.indexOf("out")).replace("file:/", "");
		}
	}
	
	public static String getTemplatePath(){
		return getProjectPath() + fileTemplatePath;
	}
	
	public static String getTemplateOutPath(){
		return getProjectPath() + fileTemplateOutPath;
	}
	
	@BeforeClass
	static void init() {
		data = new HashMap<String, String[]>();

		data.putAll(generateCommonFields());
	   // data.putAll(generateAggrementsFields());
	}

	static HashMap<String, String[]> generateCommonFields(){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("dataUmowy", ["21.03.2013"] as String[]);
		result.put("akceptantNazwa", ["Firma Handlowo Us³ugowa 'HandUs'"] as String[]);
		result.put("akceptantSiedziba", ["ul. Marsza³kowska 3/4; 01-234 Warszawa"] as String[]);
		result.put("reprezentant1", ["Jan Nowak"] as String[]);
		result.put("reprezentant2", ["Gra¿yna Prymek"] as String[]);
		result.put("phNumer", ["12345"] as String[]);
		return result;
	}

	static HashMap<String, String[]> generateAggrementsFields(){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("vatTak", ["true"] as String[]);
		result.put("vatNie", [""] as String[]);
		result.put("informacjaHandlowaTak", [""] as String[]);
		result.put("informacjaHandlowaNie", ["true"] as String[]);
		return result;
	}

	static HashMap<String, String[]> generateAdditionalServicesFields(){
		

		
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("oplataZaDzienneZestawienieTransakcji", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaPotwierdzenieWykonaniaPrzelewu", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaDostarczeniePapieru", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaMiesieczneZestawienieTransakcji", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaZmianeGrafiki", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaInstalacjePOS", [getRandomFormattedDouble(20)] as String[]);
		result.put("oplataZaInstalacjeGPRS", [getRandomFormattedDouble(20)] as String[]);
		return result;
	}

	static HashMap<String, String[]> generateDataPodpisaniaAneksuPOZField(){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("dataPodpisaniaAneksuPOZ", ["23.04.2013"] as String[]);
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzAplikacyjnyFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		
		result.put("NrMerchanta1", ["98765"] as String[]);
		result.put("NrMerchanta2", ["12345"] as String[]);
		result.put("NrMerchanta3", ["66"] as String[]);
		result.put("NrMerchanta4", ["44"] as String[]);
		result.put("SprawaNr1", ["123"] as String[]);
		result.put("SprawaNr2", ["8906"] as String[]);
		result.put("SprawaNr3", ["0"] as String[]);
		result.put("OsobaPozyskalaAkceptantaNr", ["12345"] as String[]);
		result.put("OsobaPodpisalaUmoweNr", ["67890"] as String[]);
		result.put("NrUmowy1", ["7890"] as String[]);
		result.put("NrUmowy2", ["1"] as String[]);
		result.put("OficjalnaNazwaAkceptanta1", ["To jest oficjalna nazwa akceptanta"] as String[]);
		result.put("OficjalnaNazwaAkceptanta2", ["To jest druga linia z nazw¹ akceptanta"] as String[]);
		result.put("NazwaSieciowaAkceptanta", ["This is the end"] as String[]);
		result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzDanychPunktuFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("pHpozysk", [""] as String[]);
		result.put("opiekaBiznesowa", [""] as String[]);
		result.put("opiekaSerwisowaI", [""] as String[]);
		result.put("opiekaSerwisowaII", [""] as String[]);
		result.put("instalujacy", [""] as String[]);
		result.put("doladowanieTel", [""] as String[]);
		result.put("rozszerzenieOPunkt", [""] as String[]);
		result.put("linia1", ["Linia na uwagi?"] as String[]);
		result.put("linia2", ["Linia na uwagiii :)"] as String[]);
		result.put("imieINazwisko", ["Dominik Walczak, Micha³ Knieæ, Pawe³ Szkup"] as String[]);
		result.put("pan", ["true", "", "checkbox"] as String[]);
		result.put("pani", ["false", "", "checkbox"] as String[]);
		result.put("email", ["mkniec@apreel.com"] as String[]);
		result.put("nazwaDoWydrukuZTerminalaPos", ["To jest jakaœ nazwa trochê inna"] as String[]);
		result.put("nazwaDoWyszukiwarki", ["To jest jakaœ nazwa"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzScoringowyFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("MCC", ["4763"] as String[]);
		result.put("rodzajZezwolenia", ["Od prezydenta USA - VIP"] as String[]);
		result.put("centrumHandlowe", ["true"] as String[]);
		result.put("trasaPrzelotowa", ["true"] as String[]);
		result.put("dochodowosc", ["10000"] as String[]);
		result.put("sredniaWartoscTransakcji", ["1 milion z³otych"] as String[]);
		result.put("innaLokalizacja", ["Wie¿a Eiffla"] as String[]);
		
		return result;
	}

	static String getRandomFormattedDouble(int max){
		return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
	}

	
	void testAPUNTSS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);

	//	data.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "4", "65", "165", "74", "43"] as String[]);
	//  data.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature2.jpg").toURI().toURL(), "", "signature", "4", "185", "165", "74", "43"] as String[]);
	//	data.put("zarzad1_podpis", [new File(getTemplatePath()+"signature4.jpg").toURI().toURL(), "", "signature", "4", "305", "165", "85", "56"] as String[]);
	//  data.put("zarzad2_podpis", [new File(getTemplatePath()+"signature5.jpg").toURI().toURL(), "", "signature", "4", "435", "165", "58", "59"] as String[]);
	//	process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)
		
		data.putAll(insertSignatures(4, 65, 165, 74, 43))
		
		process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)
		}

	void testAPUNTSZAPOO() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 288, 74, 43))
		process("APUNTSZAPOO3.00212-01-16.pdf", "APUNTSZAPOO3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZAPOU() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(2, 85, 395, 74, 43))
		process("APUNTSZAPOU3.00212-01-16.pdf", "APUNTSZAPOU3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZAWNZ() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(generateCommonFields())
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		data.putAll(generateAdditionalServicesFields())
		
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 370, 74, 43))
		process("APUNTSZAWNZ3.00212-01-16.pdf", "APUNTSZAWNZ3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZDCC() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 280, 74, 43))
		process("APUNTSZDCC2.00212-01-16.pdf", "APUNTSZDCC2.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZDCCZ() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 260, 74, 43))
		process("APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków.pdf", "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków_out.pdf", data)
	}

	void testAPUNTSZOKOD() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 250, 74, 43))
		process("APUNTSZOKOD2.00312-01-16.pdf", "APUNTSZOKOD2.00312-01-16_out.pdf", data)
	}

	void testAPUNTWAGOK() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 245, 74, 43))
		process("APUNTWAGOK1.00212-01-16.pdf", "APUNTWAGOK1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWAGON() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 235, 74, 43))
		process("APUNTWAGON1.00212-01-16.pdf", "APUNTWAGON1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWAGOP() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 80, 245, 74, 43))
		process("APUNTWAGOP1.00212-01-16.pdf", "APUNTWAGOP1.00212-01-16_out.pdf", data)
	}

	void testAPUNTWANOD() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(generateCommonFields())
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 225, 74, 43))
		process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Op³at.pdf", "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Op³at_out.pdf", data)
	}

	void testAPUNTZ() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
	 // result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		data.putAll(this.data);
		data.putAll(insertSignatures(4, 70, 165, 74, 43))
	 // process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", result)
		process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", data)
	}

	void testATUSUFDU() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();
	 // result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
	 // result.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "2", "435", "15", "74", "43"] as String[]);
		data.putAll(this.data);
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
		data.putAll(insertSignatures(1, 85, 215, 74, 43))
		process("APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC).pdf", "APUPZ2DCC1.00013-02-15 - Aneks do Umowy o przyjm po 2013 r (wprow DCC)_out.pdf", data)
	}
	
	void testAPUPZACB() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(2, 85, 220, 74, 43))
		process("APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zap³ (dod Cashback).pdf", "APUPZACB2.00313-02-15 - Aneks do Umowy o przyjm zap³ (dod Cashback)_out.pdf", data)
	}
	
	void testAPUPZAWNZBS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 180, 74, 43))
		process("APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zap³ (bez stawek p³askich).pdf", "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zap³ (bez stawek p³askich)_out.pdf", data)
	}
	
	void testAPUPZAWNZS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 80, 180, 74, 43))
		process("APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zap³ (narzucone stawki p³askie).pdf", "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zap³ (narzucone stawki p³askie)_out.pdf", data)
	}
	
	void testAPUPZBS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(4, 80, 320, 74, 43))
		process("APUPZBS2.00013-01-25 - Umowa o przyjmowanie zap³aty (wersja bez stawek p³askich)_do druku.pdf", "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zap³aty (wersja bez stawek p³askich)_do druku_out.pdf", data)
	}

	void testATUSU() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(3, 80, 580, 74, 43))
		process("ATUSU5.00413-05-22.pdf", "ATUSU5.00413-05-22_out.pdf", data)
	}
	
	void testAPUPZIF() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(2, 80, 460, 74, 43))
		process("APUPZIF2.00113-04-05.pdf", "APUPZIF2.00113-04-05_out.pdf", data)
	}
	
	void testAPUPZIF2() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(4, 80, 305, 74, 43))
		process("APUPZIF2.00013-03-26 - Umowa o przyjmowanie zap³aty IF+_2013.pdf", "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zap³aty IF+_2013_out.pdf", data)
	}
	
	void testAPUPZDCC() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 80, 240, 74, 43))
		process("APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zap³ (wprow DCC).pdf", "APUPZDCC2.00313-02-15 - Aneks do Umowy o przyjm zap³ (wprow DCC)_out.pdf", data)
	}
	
	void testAPUPZDCCZ() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 185, 74, 43))
		process("APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zap³. (zm. war. DCC).pdf", "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zap³. (zm. war. DCC)_out.pdf", data)
	}
	
	// ---------------------------------------------------------------------------------------
	void testFormularzAplikacyjny() {
		process("Formularz aplikacyjny_po_zmianach_18.01.2012.pdf", "Formularz aplikacyjny_po_zmianach_18.01.2012_out.pdf", generateFormularzAplikacyjnyFields());
	}
	
	void testFormularzDanychPunktu() {
		process("Formularz danych punktu_zmiany_15.05.2013_edited.pdf", "Formularz danych punktu_zmiany_15.05.2013_edited_out.pdf", generateFormularzDanychPunktuFields());
	}
	
	void testFormularzScoringowy() {
		process("Formularz Scoringowy (oryginal).pdf", "Formularz Scoringowy (oryginal)_out.pdf", generateFormularzScoringowyFields());
	}


	void process(templateName, outName, data){
		byte[] pdf = service.fillPdfFormFromFile(getTemplatePath()+templateName, data, PdfService.FontType.ARIAL)

		assert pdf != null

		new File(getTemplateOutPath()+outName).withOutputStream {
			it.write pdf
		}
	}

	
	 private HashMap<String, String[]> insertSignatures(int pageNo, int x, int y, int scaleX, int scaleY){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		result.put("reprezentant1_podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
		result.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature2.jpg").toURI().toURL(), "", "signature", pageNo, x+120, y, scaleX, scaleY] as String[]);
		result.put("zarzad1_podpis", [new File(getTemplatePath()+"signature4.jpg").toURI().toURL(), "", "signature", pageNo, x+250, y, BOARD_MEMBER_1_X, BOARD_MEMBER_1_Y] as String[]);
		result.put("zarzad2_podpis", [new File(getTemplatePath()+"signature5.jpg").toURI().toURL(), "", "signature", pageNo, x+380, y, BOARD_MEMBER_2_X, BOARD_MEMBER_2_Y] as String[]);
		return result;
	}
}
