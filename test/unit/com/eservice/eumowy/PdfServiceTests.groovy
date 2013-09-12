package com.eservice.eumowy

import grails.test.mixin.*

import java.awt.image.BufferedImage
import java.text.DecimalFormat

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.util.PDFImageWriter
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
		result.put("dataAneksowanejUmowyPos", ["22.04.2013"] as String[]);
		result.put("dataAneksowanejUmowyPrepaid", ["11.05.2013"] as String[]);
		result.put("akceptantNazwa", ["Firma Handlowo Usługowa 'HandUs'"] as String[]);
		result.put("akceptantSiedziba", ["ul. Marszałkowska 3/4; 01-234 Warszawa"] as String[]);
		result.put("reprezentant1", ["Jan Nowak"] as String[]);
		result.put("reprezentant2", ["Grażyna Prymek"] as String[]);
		result.put("phNumer", ["12345"] as String[]);
		return result;
	}

	static HashMap<String, String[]> generateAggrementsFields(){
		HashMap<String, String[]> result = new HashMap<String, String[]>();
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
		
		result.put("nrMerchanta1", ["98765"] as String[]);
		result.put("nrMerchanta2", ["12345"] as String[]);
		result.put("nrMerchanta3", ["66"] as String[]);
		result.put("nrMerchanta4", ["404"] as String[]);
		result.put("sprawaNr1", ["123"] as String[]);
		result.put("sprawaNr2", ["8906"] as String[]);
		result.put("sprawaNr3", ["0"] as String[]);
		result.put("osobaPozyskalaAkceptanta", ["Klej Zdzisiek"] as String[]);
		result.put("osobaPozyskalaAkceptantaNr", ["12345"] as String[]);
		result.put("osobaPodpisalaUmowe", ["Pucki Zyndram"] as String[]);
		result.put("osobaPodpisalaUmoweNr", ["67890"] as String[]);
		result.put("nrUmowy1", ["7890"] as String[]);
		result.put("nrUmowy2", ["1"] as String[]);
		result.put("akceptantNazwaOficjalna1", ["To jest oficjalna nazwa akceptanta"] as String[]);
		result.put("akceptantNazwaOficjalna2", ["To jest druga linia z nazwą akceptanta"] as String[]);
		result.put("akceptantNazwaSieciowa", ["This is the end"] as String[]);
		result.put("akceptantNip", ["1234567890"] as String[]);
		result.put("akceptantRegon", ["987654321"] as String[]);
		result.put("reprezentant1", ["Wiesiek Gabriel"] as String[]);
		result.put("reprezentant2", ["Waldek Złotopolski"] as String[]);
		result.put("pan1", ["true", "", "checkbox"] as String[]);
		result.put("pan2", ["true", "", "checkbox"] as String[]);
		result.put("pani1", ["true", "", "checkbox"] as String[]);
		result.put("pani2", ["true", "", "checkbox"] as String[]);
		result.put("akceptantUlica", ["Klanowa"] as String[]);
		result.put("akceptantMiasto", ["Warszawa"] as String[]);
		result.put("akceptantNrDomu", ["1"] as String[]);
		result.put("akceptantNrMieszkania", ["2"] as String[]);
		result.put("akceptantPoczta", ["Warszawa"] as String[]);
		result.put("akceptantKodPocztowy1", ["90"] as String[]);
		result.put("akceptantKodPocztowy2", ["310"] as String[]);
		result.put("akceptantKontaktUlica", ["Złotopolska"] as String[]);
		result.put("akceptantKontaktMiasto", ["Złotopolice"] as String[]);
		result.put("akceptantKontaktNrDomu", ["3"] as String[]);
		result.put("akceptantKontaktNrMieszkania", ["4"] as String[]);
		result.put("akceptantKontaktPoczta", ["Wrocław"] as String[]);
		result.put("akceptantKontaktKodPocztowy1", ["02"] as String[]);
		result.put("akceptantKontaktKodPocztowy2", ["200"] as String[]);
		result.put("panDoKontaktu", ["true", "", "checkbox"] as String[]);
		result.put("paniDoKontaktu", ["true", "", "checkbox"] as String[]);
		result.put("imieINazwiskoOsobyDoKontaktu", ["Listonosz Józef"] as String[]);
		result.put("kierunkowyStacjonarnyDoKontaktu", ["01"] as String[]);
		result.put("telStacjonarnyDoKontaktu1", ["801"] as String[]);
		result.put("telStacjonarnyDoKontaktu2", ["01"] as String[]);
		result.put("telStacjonarnyDoKontaktu3", ["07"] as String[]);
		result.put("telKomorkowyDoKontaktu1", ["401"] as String[]);
		result.put("telKomorkowyDoKontaktu2", ["301"] as String[]);
		result.put("telKomorkowyDoKontaktu3", ["901"] as String[]);
		result.put("email", ["zlotopolscy@tvp.pl"] as String[]);
		result.put("osobaFizyczna", ["true", "", "checkbox"] as String[]);
		result.put("spolkaCywilna", ["true", "", "checkbox"] as String[]);
		result.put("spolka", ["true", "", "checkbox"] as String[]);
		result.put("zaswiadczenieZEwidencji", ["true", "", "checkbox"] as String[]);
		result.put("umowaSpolkiCywilnej", ["true", "", "checkbox"] as String[]);
		result.put("odpisZKRS", ["true", "", "checkbox"] as String[]);
		result.put("inne1", ["true", "", "checkbox"] as String[]);
		result.put("inne2", ["true", "", "checkbox"] as String[]);
		result.put("spolkaText", ["zoo"] as String[]);
		result.put("inneText", ["coś"] as String[]);
		result.put("inneText2", ["Tekścior"] as String[]);
		result.put("NrSprzedazowyPH1", ["4561"] as String[]);
		result.put("NrSprzedazowyPH2", ["8"] as String[]);
		result.put("kierunkowyStacjonarny", ["04"] as String[]);
		result.put("telStacjonarny1", ["533"] as String[]);
		result.put("telStacjonarny2", ["33"] as String[]);
		result.put("telStacjonarny3", ["34"] as String[]);
		result.put("telKomorkowy1", ["999"] as String[]);
		result.put("telKomorkowy2", ["451"] as String[]);;
		result.put("telKomorkowy3", ["666"] as String[]);
		result.put("kierunkowyFaks", ["11"] as String[]);
		result.put("faks1", ["343"] as String[]);
		result.put("faks2", ["55"] as String[]);
		result.put("faks3", ["78"] as String[]);
		
		result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzDanychPunktuFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("phPozysk", ["12345"] as String[]);
		result.put("opiekaBiznesowa", ["23456"] as String[]);
		result.put("opiekaSerwisowaI", ["34567"] as String[]);
		result.put("opiekaSerwisowaII", ["45678"] as String[]);
		result.put("opiekaSerwisowaIII", ["00000"] as String[]);
		result.put("doladowanieTel", ["true", "", "checkbox"] as String[]);
		result.put("nipPunktu", ["1300000003"] as String[]);
		result.put("kodMCC", ["1453"] as String[]);
		result.put("rodzProwadzDzialalWPraktyce", ["naprawa smartfonow bez klapki"] as String[]);
		result.put("rozszerzenieOPunkt", ["true", "", "checkbox"] as String[]);
		result.put("wydrukUlica", ["Dolnośląska"] as String[]);
		result.put("wydrukNrDomu", ["11A"] as String[]);
		result.put("wydrukNrLokalu", ["13"] as String[]);
		result.put("wydrukMiasto", ["Atomice"] as String[]);
		result.put("wydrukKodPocztowy1", ["20"] as String[]);
		result.put("wydrukKodPocztowy2", ["333"] as String[]);
		result.put("wydrukPoczta", ["Nowa Wieś"] as String[]);
		result.put("wydrukLinia1", ["Linia na uwagi?"] as String[]);
		result.put("wydrukLinia2", ["Linia na uwagiii :)"] as String[]);
		result.put("korespondencjaUlica", ["Dolnośląska"] as String[]);
		result.put("korespondencjaNrDomu", ["11A"] as String[]);
		result.put("korespondencjaNrLokalu", ["13"] as String[]);
		result.put("korespondencjaMiasto", ["Atomice"] as String[]);
		result.put("korespondencjaKodPocztowy1", ["20"] as String[]);
		result.put("korespondencjaKodPocztowy2", ["333"] as String[]);
		result.put("korespondencjaPoczta", ["Nowa Wieś"] as String[]);
		result.put("numerRachunkuBankowego1", ["99"] as String[]);
		result.put("numerRachunkuBankowego2", ["7463"] as String[]);
		result.put("numerRachunkuBankowego3", ["0000"] as String[]);
		result.put("numerRachunkuBankowego4", ["2324"] as String[]);
		result.put("numerRachunkuBankowego5", ["9999"] as String[]);
		result.put("numerRachunkuBankowego6", ["2876"] as String[]);
		result.put("imieINazwisko", ["Jean "] as String[]);
		result.put("pan", ["true", "", "checkbox"] as String[]);
		result.put("pani", ["true", "", "checkbox"] as String[]);
		result.put("email", ["mkniec@apreel.com"] as String[]);
		result.put("kierunkowy1", ["22"] as String[]);
		result.put("stacjonarny1", ["202"] as String[]);
		result.put("stacjonarny2", ["22"] as String[]);
		result.put("stacjonarny3", ["22"] as String[]);
		result.put("kierunkowy2", ["22"] as String[]);
		result.put("nrFaksu1", ["202"] as String[]);
		result.put("nrFaksu2", ["22"] as String[]);
		result.put("nrFaksu3", ["22"] as String[]);
		result.put("komorka1", ["209"] as String[]);
		result.put("komorka2", ["122"] as String[]);
		result.put("komorka3", ["212"] as String[]);
		result.put("nazwaDoWydrukuZTerminalaPos", ["To jest jakaś nazwa trochę inna"] as String[]);
		result.put("wydrukJakWyzej", ["true", "", "checkbox"] as String[]);
		result.put("nazwaDoWyszukiwarki", ["To jest jakaś nazwa"] as String[]);
		
		return result;
	}
	
	static HashMap<String, String[]> generateFormularzScoringowyFields() {
		HashMap<String, String[]> result = new HashMap<String, String[]>();

		result.put("scoringMcc", ["4763"] as String[]);
		result.put("phNumer", ["2333"] as String[]);
		result.put("rodzajZezwolenia", ["Od prezydenta USA - VIP"] as String[]);
		result.put("scoringSzczegolyDzialalnosci", ["Pranie pieniedzy w Perwoolu"] as String[]);
		result.put("scoringCharakterystykaInna", ["Tesco Extra"] as String[]);
		result.put("centrumHandlowe", ["true", "", "checkbox"] as String[]);
		result.put("trasaPrzelotowa", ["true", "", "checkbox"] as String[]);
		result.put("dochodowosc", ["10000"] as String[]);
		result.put("dochodowoscRd", ["4500"] as String [])
		result.put("scoringDeklaracjaFinansowa", ["d"] as String[]);
		result.put("scoringDeklaracjaFinansowaObrotOgolem", ["2 złote"] as String[]);
		result.put("scoringDeklaracjaFinansowaObrotNaKarty", ["1 tysiąc euro"] as String[]);
		result.put("scoringDeklaracjaFinansowaSredniObrot", ["50 centów"] as String[]);
		result.put("scoringDeklaracjaFinansowaSredniaTransakcja", ["1 milion złotych"] as String[]);
		result.put("scoringTypPunktuInny", ["Wieża Eiffla"] as String[]);
        result.put("handel", ["true", "", "checkbox"] as String[]);
	    result.put("uslugi", ["true", "", "checkbox"] as String[]);
		result.put("wlasnosc", ["true", "", "checkbox"] as String[]);
		result.put("wynajem", ["true", "", "checkbox"] as String[]);
		result.put("powyzej5lat", ["true", "", "checkbox"] as String[]);
		result.put("od1do5lat", ["true", "", "checkbox"] as String[]);
		result.put("ponizejRoku", ["true", "", "checkbox"] as String[]);
		result.put("dzialalnoscWymagaLicencjiTak", ["true", "", "checkbox"] as String[]);
		result.put("dzialalnoscWymagaLicencjiNie", ["true", "", "checkbox"] as String[]);
		result.put("salon", ["true", "", "checkbox"] as String[]);
		result.put("sklep", ["true", "", "checkbox"] as String[]);
		result.put("stoisko", ["true", "", "checkbox"] as String[]);
		result.put("stacjaPaliw", ["true", "", "checkbox"] as String[]);
		result.put("inny", ["true", "", "checkbox"] as String[]);
		result.put("centrumMiasta", ["true", "", "checkbox"] as String[]);
		result.put("peryferiaMiasta", ["true", "", "checkbox"] as String[]);
		result.put("pawilonyHandlowe", ["true", "", "checkbox"] as String[]);
		result.put("budynekWolnoStojacy", ["true", "", "checkbox"] as String[]);
		result.put("osiedleMieszkaniowe", ["true", "", "checkbox"] as String[]);
		result.put("targowisko", ["true", "", "checkbox"] as String[]);
		result.put("inna", ["true", "", "checkbox"] as String[]);
		result.put("miastoPonad500tysChb", ["true", "", "checkbox"] as String[]);
		result.put("miastoOd100Do500tysChb", ["true", "", "checkbox"] as String[]);
		result.put("miastoOd50Do90tysChb", ["true", "", "checkbox"] as String[]);
		result.put("miastoPonizej50tysChb", ["true", "", "checkbox"] as String[]);
		result.put("wies", ["true", "", "checkbox"] as String[]);
		result.put("czynne", ["true", "", "checkbox"] as String[]);
		result.put("nieczynne", ["true", "", "checkbox"] as String[]);
		result.put("stanZadbany", ["true", "", "checkbox"] as String[]);
		result.put("wPunktachMonitoringTak", ["true", "", "checkbox"] as String[]);
		result.put("wPunktachMonitoringNie", ["true", "", "checkbox"] as String[]);
		result.put("powyzej400m2", ["true", "", "checkbox"] as String[]);
		result.put("od50do400m2", ["true", "", "checkbox"] as String[]);
		result.put("do50m2", ["true", "", "checkbox"] as String[]);
		/*
		addCheckboxes(result, ["handel":"handel", "uslugi":"uslugi"], "uslugi")
		addCheckbox(result, "stanZadbany", "true", "true")
		addCheckbox(result, "uslugiPlatneZGory", "true", "true")
		addCheckbox(result, "ruchTurystycznyPrzygraniczny", "true", "false") */

		result.put("podpis", [new File(getTemplatePath()+"sign-Mariusz-Kolonko-2.png").toURI().toURL(), "", "signature", "1", "415", "16", "58", "59"] as String[]);
		
		return result;
	}

	private static addCheckbox(def data, def pdfName, def fieldValue, def value){
		data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
	}

	static String getRandomFormattedDouble(int max){
		return new DecimalFormat("0.00").format(random.nextInt(max) + random.nextDouble());
	}

	
	void testAPUNTSS() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("oplatyPOSIloscA", ["10"] as String[]);
		data.put("oplatyPOSIloscB", ["20"] as String[]);
		data.put("oplatyPOSIloscC", ["30"] as String[]);
		data.put("oplatyPOSCenaA", ["40"] as String[]);
		data.put("oplatyPOSCenaB", ["50"] as String[]);
		data.put("oplatyPOSCenaC", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscA", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscB", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscC", ["50"] as String[]);
		data.put("oplatyPOSPrefCenaA", ["11"] as String[]);
		data.put("oplatyPOSPrefCenaB", ["55"] as String[]);
		data.put("oplatyPOSPrefCenaC", ["53"] as String[]);
		data.put("oplatyPOSMiesiacNaliczania", ["1"] as String[]);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
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
		data.put("punkt1", ["Kodziki"] as String[]);
		data.put("adres1", ["Wąwozowa 7"] as String[]);
		data.put("punkt2", ["Pompeczki"] as String[]);
		data.put("adres2", ["Kaszubska 2"] as String[]);
		data.put("punkt3", ["Orencz"] as String[]);
		data.put("adres3", ["Pomarańczowa 11"] as String[]);
		data.put("punkt4", ["Timobajl"] as String[]);
		data.put("adres4", ["Rózowa 1"] as String[]);
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
		data.put("oplatyPOSPrefIloscA", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscB", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscC", ["50"] as String[]);
		data.put("oplatyPOSPrefCenaA", ["11"] as String[]);
		data.put("oplatyPOSPrefCenaB", ["55"] as String[]);
		data.put("oplatyPOSPrefCenaC", ["53"] as String[]);
		data.putAll(insertSignatures(1, 85, 288, 74, 43))
		process("APUNTSZAPOO3.00212-01-16.pdf", "APUNTSZAPOO3.00212-01-16_out.pdf", data)
	}

	void testAPUNTSZAPOU() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);
		data.put("oplatyPOSIloscA", ["10"] as String[]);
		data.put("oplatyPOSIloscB", ["20"] as String[]);
		data.put("oplatyPOSIloscC", ["30"] as String[]);
		data.put("oplatyPOSCenaA", ["40"] as String[]);
		data.put("oplatyPOSCenaB", ["50"] as String[]);
		data.put("oplatyPOSCenaC", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscA", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscB", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscC", ["50"] as String[]);
		data.put("oplatyPOSPrefCenaA", ["11"] as String[]);
		data.put("oplatyPOSPrefCenaB", ["55"] as String[]);
		data.put("oplatyPOSPrefCenaC", ["53"] as String[]);
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
		data.putAll(generateCommonFields())
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		data.putAll(generateAdditionalServicesFields())
		
		data.putAll(this.data);
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
		data.putAll(generateCommonFields())
		data.putAll(generateDataPodpisaniaAneksuPOZField())
		
		data.putAll(this.data);
		data.putAll(insertSignatures(1, 85, 225, 74, 43))
		process("APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat.pdf", "APUNTWANOD1.00312-01-16_Aneks do UN_zmiana Tabeli Opłat_out.pdf", data)
	}

	void testAPUNTZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(generateCommonFields())
		// result.put("podpis", [new File(getTemplatePath()+"signature1.jpg").toURI().toURL(), "", "signature", "1", "435", "15", "74", "43"] as String[]);
		data.putAll(this.data);
		data.put("oplatyPOSIloscA", ["10"] as String[]);
		data.put("oplatyPOSIloscB", ["20"] as String[]);
		data.put("oplatyPOSIloscC", ["30"] as String[]);
		data.put("oplatyPOSCenaA", ["40"] as String[]);
		data.put("oplatyPOSCenaB", ["50"] as String[]);
		data.put("oplatyPOSCenaC", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscA", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscB", ["50"] as String[]);
		data.put("oplatyPOSPrefIloscC", ["50"] as String[]);
		data.put("oplatyPOSPrefCenaA", ["11"] as String[]);
		data.put("oplatyPOSPrefCenaB", ["55"] as String[]);
		data.put("oplatyPOSPrefCenaC", ["53"] as String[]);
		data.put("oplatyPOSMiesiacNaliczania", ["1"] as String[]);
		data.put("wydrukGrafikiCena", ["53"] as String[]);
		data.put("dzialaniaMatematyczneCena", ["53"] as String[]);
		data.put("tytulPlatnosciCena", ["53"] as String[]);
		data.put("pierwszaSecjaCena", ["53"] as String[]);
		data.put("walutaObcaCena", ["53"] as String[]);
		data.put("mudCena", ["53"] as String[]);
		data.put("pierwszaSesjaCena", ["53"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("okresLojalnosciowy", ["3"] as String[]);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("obslugaPrestiz", ["true", "", "checkbox"] as String[]);
		data.put("obslugaKomfort", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomiczny", ["true", "", "checkbox"] as String[]);
		data.put("obslugaEkonomicznyCena", ["153"] as String[]);
		data.put("punkt1", ["Kodziki"] as String[]);
		data.put("adres1", ["Wąwozowa 7"] as String[]);
		data.put("punkt2", ["Pompeczki"] as String[]);
		data.put("adres2", ["Kaszubska 2"] as String[]);
		data.put("punkt3", ["Orencz"] as String[]);
		data.put("adres3", ["Pomarańczowa 11"] as String[]);
		data.put("punkt4", ["Timobajl"] as String[]);
		data.put("adres4", ["Rózowa 1"] as String[]);
		
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
		data.put("pp_orange_tk", ["12"] as String[]);
		data.put("pp_orange_tp", ["22"] as String[]);
		data.put("pp_plus_tk", ["98"] as String[]);
		data.put("pp_plus_tp", ["45"] as String[]);
		data.put("pp_tmobile_tk", ["76"] as String[]);
		data.put("pp_tmobile_tp", ["11"] as String[]);
		data.put("pp_heyah_tk", ["30"] as String[]);
		data.put("pp_heyah_tp", ["49"] as String[]);
		data.put("pp_play_tk", ["20"] as String[]);
		data.put("pp_play_tp", ["72"] as String[]);
		data.put("pp_telegrosik_tk", ["13"] as String[]);
		data.put("pp_virginmobile_tk", ["14"] as String[]);
		data.put("pp_lycamobile_tk", ["15"] as String[]);
		data.put("pp_gtmobile_tk", ["60"] as String[]);
		data.put("pp_vectonemobile_tk", ["71"] as String[]);
		data.put("pp_delightmobile_tk", ["99"] as String[]);
		data.put("oplataZaOprogramowanieDoDoladowan", ["300"] as String[]);
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
		data.put("oplataVISAPr", ["33"] as String[]);
		data.put("oplataVISA", ["34"] as String[]);
		data.put("oplataMasterCardPr", ["90"] as String[]);
		data.put("oplataMasterCard", ["34"] as String[]);
		data.put("oplataMaestroPr", ["56"] as String[]);
		data.put("oplataMaestro", ["83"] as String[]);
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
		data.put("punkt1", ["Kodziki"] as String[]);
		data.put("adres1", ["Wąwozowa 7"] as String[]);
		data.put("punkt2", ["Pompeczki"] as String[]);
		data.put("adres2", ["Kaszubska 2"] as String[]);
		data.put("punkt3", ["Tea Mobile"] as String[]);
		data.put("adres3", ["Starogardzka 7"] as String[]);
		data.put("punkt4", ["O Range"] as String[]);
		data.put("adres4", ["Rolnicza 13"] as String[]);
		data.put("punkt5", ["Minus GSM"] as String[]);
		data.put("adres5", ["Kowalskiego 4"] as String[]);
		data.put("visaEUKKOPr", ["11"] as String[]);
		data.put("visaEUKDPr", ["22"] as String[]);
		data.put("visaEUKBPr", ["22"] as String[]);
		data.put("visaOutEUKKOPr", ["33"] as String[]);
		data.put("visaOutEUKDPr", ["33"] as String[]);
		data.put("visaOutEUKBPr", ["44"] as String[]);
		data.put("visaPolskaKKO1Pr", ["44"] as String[]);
		data.put("visaPolskaKKO2Pr", ["55"] as String[]);
		data.put("visaPolskaKD1Pr", ["55"] as String[]);
		data.put("visaPolskaKD2Pr", ["55"] as String[]);
		data.put("visaPolskaKBPr", ["66"] as String[]);
		data.put("mastercardEUKKPr", ["55"] as String[]);
		data.put("mastercardEUKDPr", ["5"] as String[]);
		data.put("mastercardEUKBLPr", ["5"] as String[]);
		data.put("mastercardEUMPr", ["5"] as String[]);
		data.put("mastercardOutEUKKPr", ["6"] as String[]);
		data.put("mastercardOutEUKDPr", ["6"] as String[]);
		data.put("mastercardOutEUKBPr", ["6"] as String[]);
		data.put("mastercardOutEUMPr", ["6"] as String[]);
		data.put("mastercardPolskaKK1Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK2Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKD1Pr", ["66"] as String[]);
		data.put("mastercardPolskaKD2Pr", ["77"] as String[]);
		data.put("mastercardPolskaKD3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKBPr", ["7"] as String[]);
		data.put("mastercardPolskaM1Pr", ["7"] as String[]);
		data.put("mastercardPolskaM2Pr", ["7"] as String[]);
		data.put("mastercardPolskaM3Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO1Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO2Pr", ["7"] as String[]);
		data.put("visaPKOBPKD1Pr", ["7"] as String[]);
		data.put("visaPKOBPKD2Pr", ["7"] as String[]);
		data.put("visaPKOBPKB3Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK1Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK2Pr", ["66"] as String[]);
		data.put("mastercardPKOBPKK3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD2LPr", ["6"] as String[]);
		data.put("mastercardPKOBPKD3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKBPr", ["6"] as String[]);
		data.put("mastercardPKOBPM1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM2Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM3Pr", ["6"] as String[]);
		data.put("dinersClubPr", ["6"] as String[]);
		data.put("ikoPr", ["6"] as String[]);
		data.put("visaEUKKOSt", ["6"] as String[]);
		data.put("visaEUKDSt", ["6"] as String[]);
		data.put("visaEUKBSt", ["6"] as String[]);
		data.put("visaOutEUKKOSt", ["6"] as String[]);
		data.put("visaOutEUKDSt", ["6"] as String[]);
		data.put("visaOutEUKBSt", ["6"] as String[]);
		data.put("visaPolskaKKO1St", ["6"] as String[]);
		data.put("visaPolskaKKO2St", ["6"] as String[]);
		data.put("visaPolskaKD1St", ["6"] as String[]);
		data.put("visaPolskaKD2St", ["6"] as String[]);
		data.put("visaPolskaKBSt", ["6"] as String[]);
		data.put("mastercardEUKKSt", ["6"] as String[]);
		data.put("mastercardEUKDSt", ["6"] as String[]);
		data.put("mastercardEUKBLSt", ["6"] as String[]);
		data.put("mastercardEUMSt", ["6"] as String[]);
		data.put("mastercardOutEUKKSt", ["6"] as String[]);
		data.put("mastercardOutEUKDSt", ["6"] as String[]);
		data.put("mastercardOutEUKBSt", ["6"] as String[]);
		data.put("mastercardOutEUMSt", ["6"] as String[]);
		data.put("mastercardPolskaKK1St", ["6"] as String[]);
		data.put("mastercardPolskaKK2St", ["6"] as String[]);
		data.put("mastercardPolskaKK3St", ["6"] as String[]);
		data.put("mastercardPolskaKD1St", ["6"] as String[]);
		data.put("mastercardPolskaKD2St", ["6"] as String[]);
		data.put("mastercardPolskaKD3St", ["6"] as String[]);
		data.put("mastercardPolskaKBSt", ["6"] as String[]);
		data.put("mastercardPolskaM1St", ["6"] as String[]);
		data.put("mastercardPolskaM2St", ["6"] as String[]);
		data.put("mastercardPolskaM3St", ["6"] as String[]);
		data.put("visaPKOBPKKO1St", ["6"] as String[]);
		data.put("visaPKOBPKKO2St", ["6"] as String[]);
		data.put("visaPKOBPKD1St", ["6"] as String[]);
		data.put("visaPKOBPKD2St", ["6"] as String[]);
		data.put("visaPKOBPKB3St", ["6"] as String[]);
		data.put("mastercardPKOBPKK1St", ["6"] as String[]);
		data.put("mastercardPKOBPKK2St", ["6"] as String[]);
		data.put("mastercardPKOBPKK3St", ["556"] as String[]);
		data.put("mastercardPKOBPKD1St", ["66"] as String[]);
		data.put("mastercardPKOBPKD2LSt", ["56"] as String[]);
		data.put("mastercardPKOBPKD3St", ["55"] as String[]);
		data.put("mastercardPKOBPKBSt", ["555"] as String[]);
		data.put("mastercardPKOBPM1St", ["55"] as String[]);
		data.put("mastercardPKOBPM2St", ["55"] as String[]);
		data.put("mastercardPKOBPM3St", ["5"] as String[]);
		data.put("dinersClubSt", ["55"] as String[]);
		data.put("ikoSt", ["66"] as String[]);
		data.putAll(insertSignatures(1, 85, 180, 74, 43))
		process("APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich).pdf", "APUPZAWNZBS1.00013-01-25 - Aneks do umowy o przyjm zapł (bez stawek płaskich)_out.pdf", data)
	}
	
	void testAPUPZAWNZS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);
		data.put("punkt1", ["Kodziki"] as String[]);
		data.put("adres1", ["Wąwozowa 7"] as String[]);
		data.put("punkt2", ["Pompeczki"] as String[]);
		data.put("adres2", ["Kaszubska 2"] as String[]);
		data.put("punkt3", ["Tea Mobile"] as String[]);
		data.put("adres3", ["Starogardzka 7"] as String[]);
		data.put("punkt4", ["O Range"] as String[]);
		data.put("adres4", ["Rolnicza 13"] as String[]);
		data.put("punkt5", ["Minus GSM"] as String[]);
		data.put("adres5", ["Kowalskiego 4"] as String[]);
		data.put("visaEUKKOPr", ["11"] as String[]);
		data.put("visaEUKDPr", ["22"] as String[]);
		data.put("visaEUKBPr", ["22"] as String[]);
		data.put("visaOutEUKKOPr", ["33"] as String[]);
		data.put("visaOutEUKDPr", ["33"] as String[]);
		data.put("visaOutEUKBPr", ["44"] as String[]);
		data.put("visaPolskaKKO1Pr", ["44"] as String[]);
		data.put("visaPolskaKKO2Pr", ["55"] as String[]);
		data.put("visaPolskaKD1Pr", ["55"] as String[]);
		data.put("visaPolskaKD2Pr", ["55"] as String[]);
		data.put("visaPolskaKBPr", ["66"] as String[]);
		data.put("mastercardEUKKPr", ["55"] as String[]);
		data.put("mastercardEUKDPr", ["5"] as String[]);
		data.put("mastercardEUKBLPr", ["5"] as String[]);
		data.put("mastercardEUMPr", ["5"] as String[]);
		data.put("mastercardOutEUKKPr", ["6"] as String[]);
		data.put("mastercardOutEUKDPr", ["6"] as String[]);
		data.put("mastercardOutEUKBPr", ["6"] as String[]);
		data.put("mastercardOutEUMPr", ["6"] as String[]);
		data.put("mastercardPolskaKK1Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK2Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKD1Pr", ["66"] as String[]);
		data.put("mastercardPolskaKD2Pr", ["77"] as String[]);
		data.put("mastercardPolskaKD3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKBPr", ["7"] as String[]);
		data.put("mastercardPolskaM1Pr", ["7"] as String[]);
		data.put("mastercardPolskaM2Pr", ["7"] as String[]);
		data.put("mastercardPolskaM3Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO1Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO2Pr", ["7"] as String[]);
		data.put("visaPKOBPKD1Pr", ["7"] as String[]);
		data.put("visaPKOBPKD2Pr", ["7"] as String[]);
		data.put("visaPKOBPKB3Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK1Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK2Pr", ["66"] as String[]);
		data.put("mastercardPKOBPKK3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD2LPr", ["6"] as String[]);
		data.put("mastercardPKOBPKD3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKBPr", ["6"] as String[]);
		data.put("mastercardPKOBPM1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM2Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM3Pr", ["6"] as String[]);
		data.put("dinersClubPr", ["6"] as String[]);
		data.put("ikoPr", ["6"] as String[]);
		data.put("visaEUKKOSt", ["6"] as String[]);
		data.put("visaEUKDSt", ["6"] as String[]);
		data.put("visaEUKBSt", ["6"] as String[]);
		data.put("visaOutEUKKOSt", ["6"] as String[]);
		data.put("visaOutEUKDSt", ["6"] as String[]);
		data.put("visaOutEUKBSt", ["6"] as String[]);
		data.put("visaPolskaKKO1St", ["6"] as String[]);
		data.put("visaPolskaKKO2St", ["6"] as String[]);
		data.put("visaPolskaKD1St", ["6"] as String[]);
		data.put("visaPolskaKD2St", ["6"] as String[]);
		data.put("visaPolskaKBSt", ["6"] as String[]);
		data.put("mastercardEUKKSt", ["6"] as String[]);
		data.put("mastercardEUKDSt", ["6"] as String[]);
		data.put("mastercardEUKBLSt", ["6"] as String[]);
		data.put("mastercardEUMSt", ["6"] as String[]);
		data.put("mastercardOutEUKKSt", ["6"] as String[]);
		data.put("mastercardOutEUKDSt", ["6"] as String[]);
		data.put("mastercardOutEUKBSt", ["6"] as String[]);
		data.put("mastercardOutEUMSt", ["6"] as String[]);
		data.put("mastercardPolskaKK1St", ["6"] as String[]);
		data.put("mastercardPolskaKK2St", ["6"] as String[]);
		data.put("mastercardPolskaKK3St", ["6"] as String[]);
		data.put("mastercardPolskaKD1St", ["6"] as String[]);
		data.put("mastercardPolskaKD2St", ["6"] as String[]);
		data.put("mastercardPolskaKD3St", ["6"] as String[]);
		data.put("mastercardPolskaKBSt", ["6"] as String[]);
		data.put("mastercardPolskaM1St", ["6"] as String[]);
		data.put("mastercardPolskaM2St", ["6"] as String[]);
		data.put("mastercardPolskaM3St", ["6"] as String[]);
		data.put("visaPKOBPKKO1St", ["6"] as String[]);
		data.put("visaPKOBPKKO2St", ["6"] as String[]);
		data.put("visaPKOBPKD1St", ["6"] as String[]);
		data.put("visaPKOBPKD2St", ["6"] as String[]);
		data.put("visaPKOBPKB3St", ["6"] as String[]);
		data.put("mastercardPKOBPKK1St", ["6"] as String[]);
		data.put("mastercardPKOBPKK2St", ["6"] as String[]);
		data.put("mastercardPKOBPKK3St", ["556"] as String[]);
		data.put("mastercardPKOBPKD1St", ["66"] as String[]);
		data.put("mastercardPKOBPKD2LSt", ["56"] as String[]);
		data.put("mastercardPKOBPKD3St", ["55"] as String[]);
		data.put("mastercardPKOBPKBSt", ["555"] as String[]);
		data.put("mastercardPKOBPM1St", ["55"] as String[]);
		data.put("mastercardPKOBPM2St", ["55"] as String[]);
		data.put("mastercardPKOBPM3St", ["5"] as String[]);
		data.put("dinersClubSt", ["55"] as String[]);
		data.put("ikoSt", ["66"] as String[]);
		data.putAll(insertSignatures(1, 80, 180, 74, 43))
		process("APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie).pdf", "APUPZAWNZS1.00013-01-25 - Aneks do umowy o przyjm zapł (narzucone stawki płaskie)_out.pdf", data)
	}
	
	void testAPUPZBS() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);
		data.put("visaEUKKOPr", ["11"] as String[]);
		data.put("visaEUKDPr", ["22"] as String[]);
		data.put("visaEUKBPr", ["22"] as String[]);
		data.put("visaOutEUKKOPr", ["33"] as String[]);
		data.put("visaOutEUKDPr", ["33"] as String[]);
		data.put("visaOutEUKBPr", ["44"] as String[]);
		data.put("visaPolskaKKO1Pr", ["44"] as String[]);
		data.put("visaPolskaKKO2Pr", ["55"] as String[]);
		data.put("visaPolskaKD1Pr", ["55"] as String[]);
		data.put("visaPolskaKD2Pr", ["55"] as String[]);
		data.put("visaPolskaKBPr", ["66"] as String[]);
		data.put("mastercardEUKKPr", ["55"] as String[]);
		data.put("mastercardEUKDPr", ["5"] as String[]);
		data.put("mastercardEUKBLPr", ["5"] as String[]);
		data.put("mastercardEUMPr", ["5"] as String[]);
		data.put("mastercardOutEUKKPr", ["6"] as String[]);
		data.put("mastercardOutEUKDPr", ["6"] as String[]);
		data.put("mastercardOutEUKBPr", ["6"] as String[]);
		data.put("mastercardOutEUMPr", ["6"] as String[]);
		data.put("mastercardPolskaKK1Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK2Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKD1Pr", ["66"] as String[]);
		data.put("mastercardPolskaKD2Pr", ["77"] as String[]);
		data.put("mastercardPolskaKD3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKBPr", ["7"] as String[]);
		data.put("mastercardPolskaM1Pr", ["7"] as String[]);
		data.put("mastercardPolskaM2Pr", ["7"] as String[]);
		data.put("mastercardPolskaM3Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO1Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO2Pr", ["7"] as String[]);
		data.put("visaPKOBPKD1Pr", ["7"] as String[]);
		data.put("visaPKOBPKD2Pr", ["7"] as String[]);
		data.put("visaPKOBPKB3Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK1Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK2Pr", ["66"] as String[]);
		data.put("mastercardPKOBPKK3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD2LPr", ["6"] as String[]);
		data.put("mastercardPKOBPKD3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKBPr", ["6"] as String[]);
		data.put("mastercardPKOBPM1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM2Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM3Pr", ["6"] as String[]);
		data.put("dinersClubPr", ["6"] as String[]);
		data.put("ikoPr", ["6"] as String[]);
		data.put("visaEUKKOSt", ["6"] as String[]);
		data.put("visaEUKDSt", ["6"] as String[]);
		data.put("visaEUKBSt", ["6"] as String[]);
		data.put("visaOutEUKKOSt", ["6"] as String[]);
		data.put("visaOutEUKDSt", ["6"] as String[]);
		data.put("visaOutEUKBSt", ["6"] as String[]);
		data.put("visaPolskaKKO1St", ["6"] as String[]);
		data.put("visaPolskaKKO2St", ["6"] as String[]);
		data.put("visaPolskaKD1St", ["6"] as String[]);
		data.put("visaPolskaKD2St", ["6"] as String[]);
		data.put("visaPolskaKBSt", ["6"] as String[]);
		data.put("mastercardEUKKSt", ["6"] as String[]);
		data.put("mastercardEUKDSt", ["6"] as String[]);
		data.put("mastercardEUKBLSt", ["6"] as String[]);
		data.put("mastercardEUMSt", ["6"] as String[]);
		data.put("mastercardOutEUKKSt", ["6"] as String[]);
		data.put("mastercardOutEUKDSt", ["6"] as String[]);
		data.put("mastercardOutEUKBSt", ["6"] as String[]);
		data.put("mastercardOutEUMSt", ["6"] as String[]);
		data.put("mastercardPolskaKK1St", ["6"] as String[]);
		data.put("mastercardPolskaKK2St", ["6"] as String[]);
		data.put("mastercardPolskaKK3St", ["6"] as String[]);
		data.put("mastercardPolskaKD1St", ["6"] as String[]);
		data.put("mastercardPolskaKD2St", ["6"] as String[]);
		data.put("mastercardPolskaKD3St", ["6"] as String[]);
		data.put("mastercardPolskaKBSt", ["6"] as String[]);
		data.put("mastercardPolskaM1St", ["6"] as String[]);
		data.put("mastercardPolskaM2St", ["6"] as String[]);
		data.put("mastercardPolskaM3St", ["6"] as String[]);
		data.put("visaPKOBPKKO1St", ["6"] as String[]);
		data.put("visaPKOBPKKO2St", ["6"] as String[]);
		data.put("visaPKOBPKD1St", ["6"] as String[]);
		data.put("visaPKOBPKD2St", ["6"] as String[]);
		data.put("visaPKOBPKB3St", ["6"] as String[]);
		data.put("mastercardPKOBPKK1St", ["6"] as String[]);
		data.put("mastercardPKOBPKK2St", ["6"] as String[]);
		data.put("mastercardPKOBPKK3St", ["556"] as String[]);
		data.put("mastercardPKOBPKD1St", ["66"] as String[]);
		data.put("mastercardPKOBPKD2LSt", ["56"] as String[]);
		data.put("mastercardPKOBPKD3St", ["55"] as String[]);
		data.put("mastercardPKOBPKBSt", ["555"] as String[]);
		data.put("mastercardPKOBPM1St", ["55"] as String[]);
		data.put("mastercardPKOBPM2St", ["55"] as String[]);
		data.put("mastercardPKOBPM3St", ["5"] as String[]);
		data.put("dinersClubSt", ["55"] as String[]);
		data.put("ikoSt", ["66"] as String[]);
		
		data.put("punkt1", ["Kodzik"] as String[]);
		data.put("adres1", ["Kodzikowa 2"] as String[]);
		data.put("punkt2", ["Pompka"] as String[]);
		data.put("adres2", ["Pompkowa 3"] as String[]);
		data.put("punkt3", ["Plusik"] as String[]);
		data.put("adres3", ["Plusikowa 1"] as String[]);
		data.put("punkt4", ["Era"] as String[]);
		data.put("adres4", ["Erowa 1"] as String[]);
		data.put("punkt5", ["Idea"] as String[]);
		data.put("adres5", ["Ideowa 1"] as String[]);
		data.put("punkt6", ["Plej"] as String[]);
		data.put("adres6", ["Plejowa 9"] as String[]);
		data.put("punkt7", ["Wodafon"] as String[]);
		data.put("adres7", ["Wodafonowa 4"] as String[]);
		data.put("punkt8", ["Goodtel"] as String[]);
		data.put("adres8", ["Dobrotelefonowa 1"] as String[]);
		data.put("punkt9", ["Test"] as String[]);
		data.put("adres9", ["Testowa 1"] as String[]);
		data.put("punkt10", ["My Phone"] as String[]);
		data.put("adres10", ["Majfonowa 11"] as String[]);
		data.put("punkt11", ["A"] as String[]);
		data.put("adres11", ["A"] as String[]);
		data.put("punkt12", ["A"] as String[]);
		data.put("adres12", ["A"] as String[]);
		data.put("punkt13", ["A"] as String[]);
		data.put("adres13", ["A"] as String[]);
		data.put("punkt14", ["A"] as String[]);
		data.put("adres14", ["A"] as String[]);
		data.put("punkt15", ["A"] as String[]);
		data.put("adres15", ["A"] as String[]);
		data.put("punkt16", ["A"] as String[]);
		data.put("adres16", ["A"] as String[]);
		data.put("punkt17", ["A"] as String[]);
		data.put("adres17", ["A"] as String[]);
		data.put("punkt18", ["A"] as String[]);
		data.put("adres18", ["A"] as String[]);
		data.put("punkt19", ["A"] as String[]);
		data.put("adres19", ["A"] as String[]);
		data.put("punkt20", ["A"] as String[]);
		data.put("adres20", ["A"] as String[]);
		data.put("punkt21", ["A"] as String[]);
		data.put("adres21", ["A"] as String[]);
		data.put("punkt22", ["A"] as String[]);
		data.put("adres22", ["A"] as String[]);
		data.put("punkt23", ["A"] as String[]);
		data.put("adres23", ["A"] as String[]);
		data.put("punkt24", ["A"] as String[]);
		data.put("adres24", ["A"] as String[]);
		data.put("punkt25", ["A"] as String[]);
		data.put("adres25", ["A"] as String[]);
		data.put("punkt26", ["A"] as String[]);
		data.put("adres26", ["A"] as String[]);
		data.put("punkt27", ["A"] as String[]);
		data.put("adres27", ["A"] as String[]);
		data.put("punkt28", ["A"] as String[]);
		data.put("adres28", ["A"] as String[]);
		data.put("punkt29", ["A"] as String[]);
		data.put("adres29", ["A"] as String[]);
		data.put("punkt30", ["A"] as String[]);
		data.put("adres30", ["A"] as String[]);
		data.putAll(insertSignatures(4, 80, 320, 74, 43))
		process("APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku.pdf", "APUPZBS2.00013-01-25 - Umowa o przyjmowanie zapłaty (wersja bez stawek płaskich)_do druku_out.pdf", data)
	}

	void testATUSU() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();	
		data.putAll(this.data);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("doladowania_tp", ["true", "", "checkbox"] as String[]);
		data.put("doladowania_tk", ["true", "", "checkbox"] as String[]);
		data.put("srednia_sprzedaz_doladowan", ["450"] as String[]);
		data.put("srednia_sprzedaz_doladowan_slownie", ["czterysta pięćdziesiąt"] as String[]);
		data.put("pp_orange_tk", ["12"] as String[]);
		data.put("pp_orange_tp", ["22"] as String[]);
		data.put("pp_plus_tk", ["98"] as String[]);
		data.put("pp_plus_tp", ["45"] as String[]);
		data.put("pp_tmobile_tk", ["76"] as String[]);
		data.put("pp_tmobile_tp", ["11"] as String[]);
		data.put("pp_heyah_tk", ["30"] as String[]);
		data.put("pp_heyah_tp", ["49"] as String[]);
		data.put("pp_play_tk", ["20"] as String[]);
		data.put("pp_play_tp", ["72"] as String[]);
		data.put("pp_telegrosik_tk", ["13"] as String[]);
		data.put("pp_virginmobile_tk", ["14"] as String[]);
		data.put("pp_lycamobile_tk", ["15"] as String[]);
		data.put("pp_gtmobile_tk", ["60"] as String[]);
		data.put("pp_vectonemobile_tk", ["71"] as String[]);
		data.put("pp_delightmobile_tk", ["99"] as String[]);
		data.put("oplataZaOprogramowanieDoDoladowan", ["300"] as String[]);
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
		data.put("ifOplataVISA", ["2"] as String[]);
		data.put("ifOplataMasterCard", ["2"] as String[]);
		data.put("ifOplataDinersClub", ["98"] as String[]);
		data.put("PKOBP", ["3"] as String[]);
		data.putAll(insertSignatures(2, 80, 460, 74, 43))
		process("APUPZIF2.00113-04-05.pdf", "APUPZIF2.00113-04-05_out.pdf", data)
	} // gotowe
	
	void testAPUPZIF2() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaNiezon", ["true", "", "checkbox"] as String[]);
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);
		data.put("punkt1", ["Kodzik"] as String[]);
		data.put("adres1", ["Ulica 1"] as String[]);
		data.put("punkt2", ["TeleKodzik"] as String[]);
		data.put("adres2", ["Ulica 2"] as String[]);
		data.put("punkt3", ["SuperKodzik"] as String[]);
		data.put("adres3", ["Ulica 3"] as String[]);
		data.put("punkt4", ["AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSD3456"] as String[]);
		data.put("adres4", ["Nieznana ulica  bo nie wiem 34A/259"] as String[]);
		data.putAll(insertSignatures(4, 80, 305, 74, 43))
		process("APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013.pdf", "APUPZIF2.00013-03-26 - Umowa o przyjmowanie zapłaty IF+_2013_out.pdf", data)
	}
	
	void testAPUPZDCC() {
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("oplataVISAPr", ["33"] as String[]);
		data.put("oplataVISA", ["34"] as String[]);
		data.put("oplataMasterCardPr", ["90"] as String[]);
		data.put("oplataMasterCard", ["34"] as String[]);
		data.put("oplataMaestroPr", ["56"] as String[]);
		data.put("oplataMaestro", ["83"] as String[]);
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
		data.put("oplataVISAPr", ["33"] as String[]);
		data.put("oplataVISA", ["34"] as String[]);
		data.put("oplataMasterCardPr", ["90"] as String[]);
		data.put("oplataMasterCard", ["37"] as String[]);
		data.put("oplataMaestroPr", ["51"] as String[]);
		data.put("oplataMaestro", ["83"] as String[]);
		data.putAll(insertSignatures(1, 85, 185, 74, 43))
		process("APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC).pdf", "APUPZDCCZ1.00213-02-15 - Aneks do Umowy o przyjm zapł. (zm. war. DCC)_out.pdf", data)
	}
	
	void testAPUPZ() { //gotowe
		HashMap<String, String[]> data = new HashMap<String, String[]>();
		data.putAll(this.data);
		data.put("informacjaHandlowaNie", ["true", "", "checkbox"] as String[]);
		data.put("informacjaHandlowaTak", ["true", "", "checkbox"] as String[]);
		data.put("umowaOznOd", ["06.10.2003"] as String[]);
		data.put("umowaOznDo", ["06.10.2010"] as String[]);
		data.put("punkt1", ["Kodziki"] as String[]);
		data.put("adres1", ["Wąwozowa 7"] as String[]);
		data.put("punkt2", ["Pompeczki"] as String[]);
		data.put("adres2", ["Kaszubska 2"] as String[]);
		data.put("punkt3", ["Orencz"] as String[]);
		data.put("adres3", ["Pomarańczowa 11"] as String[]);
		data.put("punkt4", ["Timobajl"] as String[]);
		data.put("adres4", ["Rózowa 1"] as String[]);
		data.put("visaEUKKOPr", ["11"] as String[]);
		data.put("visaEUKDPr", ["22"] as String[]);
		data.put("visaEUKBPr", ["22"] as String[]);
		data.put("visaOutEUKKOPr", ["33"] as String[]);
		data.put("visaOutEUKDPr", ["33"] as String[]);
		data.put("visaOutEUKBPr", ["44"] as String[]);
		data.put("visaPolskaKKO1Pr", ["44"] as String[]);
		data.put("visaPolskaKKO2Pr", ["55"] as String[]);
		data.put("visaPolskaKD1Pr", ["55"] as String[]);
		data.put("visaPolskaKD2Pr", ["55"] as String[]);
		data.put("visaPolskaKBPr", ["66"] as String[]);
		data.put("mastercardEUKKPr", ["55"] as String[]);
		data.put("mastercardEUKDPr", ["5"] as String[]);
		data.put("mastercardEUKBLPr", ["5"] as String[]);
		data.put("mastercardEUMPr", ["5"] as String[]);
		data.put("mastercardOutEUKKPr", ["6"] as String[]);
		data.put("mastercardOutEUKDPr", ["6"] as String[]);
		data.put("mastercardOutEUKBPr", ["6"] as String[]);
		data.put("mastercardOutEUMPr", ["6"] as String[]);
		data.put("mastercardPolskaKK1Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK2Pr", ["6"] as String[]);
		data.put("mastercardPolskaKK3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKD1Pr", ["66"] as String[]);
		data.put("mastercardPolskaKD2Pr", ["77"] as String[]);
		data.put("mastercardPolskaKD3Pr", ["7"] as String[]);
		data.put("mastercardPolskaKBPr", ["7"] as String[]);
		data.put("mastercardPolskaM1Pr", ["7"] as String[]);
		data.put("mastercardPolskaM2Pr", ["7"] as String[]);
		data.put("mastercardPolskaM3Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO1Pr", ["7"] as String[]);
		data.put("visaPKOBPKKO2Pr", ["7"] as String[]);
		data.put("visaPKOBPKD1Pr", ["7"] as String[]);
		data.put("visaPKOBPKD2Pr", ["7"] as String[]);
		data.put("visaPKOBPKB3Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK1Pr", ["7"] as String[]);
		data.put("mastercardPKOBPKK2Pr", ["66"] as String[]);
		data.put("mastercardPKOBPKK3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKD2LPr", ["6"] as String[]);
		data.put("mastercardPKOBPKD3Pr", ["6"] as String[]);
		data.put("mastercardPKOBPKBPr", ["6"] as String[]);
		data.put("mastercardPKOBPM1Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM2Pr", ["6"] as String[]);
		data.put("mastercardPKOBPM3Pr", ["6"] as String[]);
		data.put("dinersClubPr", ["6"] as String[]);
		data.put("ikoPr", ["6"] as String[]);
		data.put("visaEUKKOSt", ["6"] as String[]);
		data.put("visaEUKDSt", ["6"] as String[]);
		data.put("visaEUKBSt", ["6"] as String[]);
		data.put("visaOutEUKKOSt", ["6"] as String[]);
		data.put("visaOutEUKDSt", ["6"] as String[]);
		data.put("visaOutEUKBSt", ["6"] as String[]);
		data.put("visaPolskaKKO1St", ["6"] as String[]);
		data.put("visaPolskaKKO2St", ["6"] as String[]);
		data.put("visaPolskaKD1St", ["6"] as String[]);
		data.put("visaPolskaKD2St", ["6"] as String[]);
		data.put("visaPolskaKBSt", ["6"] as String[]);
		data.put("mastercardEUKKSt", ["6"] as String[]);
		data.put("mastercardEUKDSt", ["6"] as String[]);
		data.put("mastercardEUKBLSt", ["6"] as String[]);
		data.put("mastercardEUMSt", ["6"] as String[]);
		data.put("mastercardOutEUKKSt", ["6"] as String[]);
		data.put("mastercardOutEUKDSt", ["6"] as String[]);
		data.put("mastercardOutEUKBSt", ["6"] as String[]);
		data.put("mastercardOutEUMSt", ["6"] as String[]);
		data.put("mastercardPolskaKK1St", ["6"] as String[]);
		data.put("mastercardPolskaKK2St", ["6"] as String[]);
		data.put("mastercardPolskaKK3St", ["6"] as String[]);
		data.put("mastercardPolskaKD1St", ["6"] as String[]);
		data.put("mastercardPolskaKD2St", ["6"] as String[]);
		data.put("mastercardPolskaKD3St", ["6"] as String[]);
		data.put("mastercardPolskaKBSt", ["6"] as String[]);
		data.put("mastercardPolskaM1St", ["6"] as String[]);
		data.put("mastercardPolskaM2St", ["6"] as String[]);
		data.put("mastercardPolskaM3St", ["6"] as String[]);
		data.put("visaPKOBPKKO1St", ["6"] as String[]);
		data.put("visaPKOBPKKO2St", ["6"] as String[]);
		data.put("visaPKOBPKD1St", ["6"] as String[]);
		data.put("visaPKOBPKD2St", ["6"] as String[]);
		data.put("visaPKOBPKB3St", ["6"] as String[]);
		data.put("mastercardPKOBPKK1St", ["6"] as String[]);
		data.put("mastercardPKOBPKK2St", ["6"] as String[]);
		data.put("mastercardPKOBPKK3St", ["556"] as String[]);
		data.put("mastercardPKOBPKD1St", ["66"] as String[]);
		data.put("mastercardPKOBPKD2LSt", ["56"] as String[]);
		data.put("mastercardPKOBPKD3St", ["55"] as String[]);
		data.put("mastercardPKOBPKBSt", ["555"] as String[]);
		data.put("mastercardPKOBPM1St", ["55"] as String[]);
		data.put("mastercardPKOBPM2St", ["55"] as String[]);
		data.put("mastercardPKOBPM3St", ["5"] as String[]);
		data.put("dinersClubSt", ["55"] as String[]);
		data.put("ikoSt", ["66"] as String[]);
		data.put("umowaNieOzn", ["true", "", "checkbox"] as String[]);
		data.put("umowaOzn", ["true", "", "checkbox"] as String[]);
		data.putAll(insertSignatures(4, 90, 308, 74, 43))
		process("APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile.pdf", "APUPZ2.00013-01-03 - Umowa o przyjmowanie zapłaty v. 2.000_z faksymile_out.pdf", data)
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
	
	void testFormularzScoringowyToImage() {
		processToImage("Formularz Scoringowy (oryginal)_out.pdf", 1)
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
		result.put("reprezentant1_podpis", [new File(getTemplatePath()+"sign-Mariusz-Kolonko-2.png").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[]);
		result.put("reprezentant2_podpis", [new File(getTemplatePath()+"signature2.jpg").toURI().toURL(), "", "signature", pageNo, x+120, y, scaleX, scaleY] as String[]);
		result.put("zarzad1_podpis", [new File(getTemplatePath()+"signature4.jpg").toURI().toURL(), "", "signature", pageNo, x+250, y, BOARD_MEMBER_1_X, BOARD_MEMBER_1_Y] as String[]);
		result.put("zarzad2_podpis", [new File(getTemplatePath()+"signature5.jpg").toURI().toURL(), "", "signature", pageNo, x+380, y, BOARD_MEMBER_2_X, BOARD_MEMBER_2_Y] as String[]);
		return result;
	}

	private static addCheckboxes(def data, def pdfKeyValue, def value){
		pdfKeyValue.each{ k, v ->  data.put(k, [v.equals(value), "", "checkbox"] as String[])}
	}
}