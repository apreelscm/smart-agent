
package com.eservice.eumowy

import grails.test.mixin.*
import org.junit.*

import com.sun.xml.internal.org.jvnet.mimepull.Data;

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PdfService)
class PdfServiceTests {

	static String fileTemplatePath = "web-app" +File.separator+ "files" +File.separator+"pdf_templates" + File.separator;
	static String fileTemplateOutPath = "web-app" +File.separator+ "files" +File.separator+"pdf_out" + File.separator;
	static Map<String, String[]> data;
	
	@BeforeClass
	static void init() {
		data = new HashMap<String, String[]>();
		
		data.put("dataUmowy", ["21-05-1991"] as String[]);
		data.put("akceptantNazwa", ["ąćźńółęśż Knieć Co."] as String[]);
		data.put("akceptantSiedziba", ["New York Blalal Adres ;)"] as String[]);
		data.put("reprezentant1", ["ąćźńółęśż"] as String[]);
		data.put("reprezentant2", ["Michałek"] as String[]);
		data.put("vatTak", ["true"] as String[]);
		/*data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);
		data.put("", [""] as String[]);*/
	}
	
    void testFillPdfFormFromFile() {
		
		byte[] pdf = service.fillPdfFormFromFile(fileTemplatePath+"APUNTSS1.00312-01-16.pdf", data)
		
		assert pdf != null
		
		new File(fileTemplateOutPath+"APUNTSS1.00312-01-16_out.pdf").withOutputStream {
			it.write pdf
		}
    }
	
}
