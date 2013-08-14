
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

	static String fileTemplatePath = "web-app" +File.separator+ "files" +File.separator+"pdf_templates" + File.separator;
	static String fileTemplateOutPath = "web-app" +File.separator+ "files" +File.separator+"pdf_out" + File.separator;
	static Map<String, String[]> data;
	
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

    static String getRandomFormattedDouble(int max){
        return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
    }

    void testAPUNTSS() {
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


    void process(templateName, outName, data){
        byte[] pdf = service.fillPdfFormFromFile(fileTemplatePath+templateName, data)

        assert pdf != null

        new File(fileTemplateOutPath+outName).withOutputStream {
            it.write pdf
        }
    }

}
