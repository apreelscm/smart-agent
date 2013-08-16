
package com.eservice.eumowy

import grails.test.mixin.*
import org.junit.*

import java.text.DecimalFormat;

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PdfService)
class PdfServiceTests {

    static Random random = new Random()
	
	static URL url = new PdfServiceTests().getClass().getResource("PdfServiceTests.class");
	static String fileTemplatePath = File.separator+"otherResources" +File.separator+"pdf_templates" + File.separator;
	static String fileTemplateOutPath = File.separator+"otherResources" +File.separator+ "pdf_out" + File.separator;
	static Map<String, String[]> data;
	
	private static String getProjectPath() {  
		return url.toString().substring(0,url.toString().indexOf('target')).replace("file:/", "");
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
        result.put("akceptantNazwa", ["Firma Handlowo Usługowa 'HandUs'"] as String[]);
        result.put("akceptantSiedziba", ["ul. Marszałkowska 3/4; 01-234 Warszawa"] as String[]);
        result.put("reprezentant1", ["Jan Nowak"] as String[]);
        result.put("reprezentant2", ["Grażyna Prymek"] as String[]);
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
		result.put("OficjalnaNazwaAkceptanta2", ["To jest druga linia z nazwą akceptanta"] as String[]);
		result.put("NazwaSieciowaAkceptanta", ["This is the end"] as String[]);
		result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzDanychPunktuFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("PHpozysk", [""] as String[]);
		result.put("OpiekaBiznesowa", [""] as String[]);
		result.put("OpiekaSerwisowaI", [""] as String[]);
		result.put("OpiekaSerwisowaII", [""] as String[]);
		result.put("Instalujacy", [""] as String[]);
		result.put("DoladowanieTel", [""] as String[]);
		result.put("RozszerzenieOPunkt", [""] as String[]);
		result.put("Linia1", ["Linia na uwagi?"] as String[]);
		result.put("Linia2", ["Linia na uwagiii :)"] as String[]);
		result.put("ImieINazwisko", ["Dominik Walczak, Michał Knieć, Paweł Szkup"] as String[]);
		result.put("Pan", ["true", "", "checkbox"] as String[]);
		result.put("Pani", ["false", "", "checkbox"] as String[]);
		result.put("email", ["mkniec@apreel.com"] as String[]);
		result.put("NazwaDoWyszukiwarki", ["To jest jakaś nazwa"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzScoringowyFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("MCC", ["4763"] as String[]);
		result.put("rodzajZezwolenia", ["Od prezydenta USA - VIP"] as String[]);
		result.put("centrumHandlowe", ["true"] as String[]);
		result.put("trasaPrzelotowa", ["true"] as String[]);
		result.put("dochodowosc", ["10000"] as String[]);
		result.put("sredniaWartoscTransakcji", ["1 milion złotych"] as String[]);
		result.put("innaLokalizacja", ["Wieża Eiffla"] as String[]);
		
		return result;
	}

    static String getRandomFormattedDouble(int max){
        return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
    }

    void testAPUNTSS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "4", "65", "150", "74", "43"] as String[]);
        process("APUNTSS1.00312-01-16.pdf", "APUNTSS1.00312-01-16_out.pdf", data)
    }

    void testAPUNTSZAPOO() {
        process("APUNTSZAPOO3.00212-01-16.pdf", "APUNTSZAPOO3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZAPOU() {
        process("APUNTSZAPOU3.00212-01-16.pdf", "APUNTSZAPOU3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZAWNZ() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(generateCommonFields())
        data.putAll(generateDataPodpisaniaAneksuPOZField())
        data.putAll(generateAdditionalServicesFields())

        process("APUNTSZAWNZ3.00212-01-16.pdf", "APUNTSZAWNZ3.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZDCC() {
        process("APUNTSZDCC2.00212-01-16.pdf", "APUNTSZDCC2.00212-01-16_out.pdf", data)
    }

    void testAPUNTSZDCCZ() {
        process("APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków.pdf", "APUNTSZDCCZ1 00112-10-05_Aneks do UN_DCC zmiana warunków_out.pdf", data)
    }

    void testAPUNTSZOKOD() {
        process("APUNTSZOKOD2.00312-01-16.pdf", "APUNTSZOKOD2.00312-01-16_out.pdf", data)
    }

    void testAPUNTWAGOK() {
        process("APUNTWAGOK1.00212-01-16.pdf", "APUNTWAGOK1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWAGON() {
        process("APUNTWAGON1.00212-01-16.pdf", "APUNTWAGON1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWAGOP() {
        process("APUNTWAGOP1.00212-01-16.pdf", "APUNTWAGOP1.00212-01-16_out.pdf", data)
    }

    void testAPUNTWANOD() {
        HashMap<String, String[]> data = new HashMap<String, String[]>();
        data.putAll(generateCommonFields())
        data.putAll(generateDataPodpisaniaAneksuPOZField())

        process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat.pdf", "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat_out.pdf", data)
    }

    void testAPUNTZ() {
        process("APUNTZ2.00312-01-16.pdf", "APUNTZ2.00312-01-16_out.pdf", data)
    }
	
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
        byte[] pdf = service.fillPdfFormFromFile(getTemplatePath()+templateName, data)

        assert pdf != null

        new File(getTemplateOutPath()+outName).withOutputStream {
            it.write pdf
        }
    }

}
