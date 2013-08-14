package pdfgenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PdfGenerator {
	private static Logger LOG = Logger.getLogger(PdfGenerator.class);
	
	/**
	 * 
	 * @param urlTemplatePath
	 * @param dataMap
	 * @return
	 */
	public static byte[] generatePdfContentFromURI(String urlTemplatePath, Map<String,String[]> dataMap, String fontPath) {
		Map<String,String> fontsPathMap = new HashMap<String, String>();
		if (fontPath != null && dataMap != null){
			
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				fontsPathMap.put(dataEntry.getKey(), fontPath);
			}
		}
		return generatePdfContentFromURI(urlTemplatePath,dataMap,fontsPathMap);
	}
	
	/**
	 * 
	 * @param fileTemplatePath
	 * @param dataMap
	 * @return
	 */
	public static byte[] generatePdfContentFromFile(String fileTemplatePath, Map<String,String[]> dataMap, String fontPath) {
		Map<String,String> fontsPathMap = new HashMap<String, String>();
		if (fontPath != null && dataMap != null){
			
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				fontsPathMap.put(dataEntry.getKey(), fontPath);
			}
		}
		return generatePdfContentFromFile(fileTemplatePath,dataMap,fontsPathMap);
	}
	
	/**
	 * 
	 * @param urlTemplatePath
	 * @param dataMap
	 * @param fontsPathMap
	 * @return
	 */
	public static byte[] generatePdfContentFromURI(String urlTemplatePath, Map<String,String[]> dataMap, Map<String,String> fontsPathMap) {
		if (urlTemplatePath == null){
			throw new IllegalArgumentException("urlTemplatePath param shouldn't be null");
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfReader templateReader = null;
		PdfStamper stamp = null;
		try {
			URL url = new URL(urlTemplatePath);
			templateReader = new PdfReader(url);
			stamp = new PdfStamper(templateReader, baos);
			AcroFields form = stamp.getAcroFields();
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				
				if (dataEntry.getValue().length > 1){
					form.setFieldProperty(dataEntry.getKey(), "textsize", Float.valueOf(dataEntry.getValue()[1]), null);
				}
				
				if (fontsPathMap != null && fontsPathMap.containsKey(dataEntry.getKey())){
					BaseFont bf = BaseFont.createFont(fontsPathMap.get(dataEntry.getKey()), BaseFont.CP1250, BaseFont.EMBEDDED);
					
					form.setFieldProperty(dataEntry.getKey(), "textfont", bf, null);	
					form.addSubstitutionFont(bf);
					
				}

				form.setField(dataEntry.getKey(),dataEntry.getValue()[0]);

			}

			stamp.setFormFlattening( true );
			stamp.getReader().removeUnusedObjects();
			stamp.getReader().removeAnnotations();
			stamp.setFullCompression();


		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} 
			catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (stamp != null){
				try {
					stamp.close();
				} catch (Exception e) {
					LOG.error(e);
				}
			}
			if (templateReader != null){
				templateReader.close();
			}
			
		}
//			document.close();
		return baos.toByteArray();
	}
	
	/**
	 * 
	 * @param urlTemplatePath
	 * @param dataMap
	 * @param fontsPathMap
	 * @return
	 */
	public static byte[] generatePdfContentFromFile(String fileTemplatePath, Map<String,String[]> dataMap, Map<String,String> fontsPathMap) {
		if (fileTemplatePath == null){
			throw new IllegalArgumentException("urlTemplatePath param shouldn't be null");
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfReader templateReader = null;
		PdfStamper stamp = null;
		try {
			templateReader = new PdfReader(fileTemplatePath);
			stamp = new PdfStamper(templateReader, baos);
			AcroFields form = stamp.getAcroFields();
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				
				if (dataEntry.getValue().length > 1){
					form.setFieldProperty(dataEntry.getKey(), "textsize", Float.valueOf(dataEntry.getValue()[1]), null);
				}
				
				if (fontsPathMap != null && fontsPathMap.containsKey(dataEntry.getKey())){
					BaseFont bf = BaseFont.createFont(fontsPathMap.get(dataEntry.getKey()), BaseFont.CP1250, BaseFont.EMBEDDED);
					
					form.setFieldProperty(dataEntry.getKey(), "textfont", bf, null);	
					form.addSubstitutionFont(bf);
					
				}

				form.setField(dataEntry.getKey(),dataEntry.getValue()[0]);

			}

			stamp.setFormFlattening( true );
			stamp.getReader().removeUnusedObjects();
			stamp.getReader().removeAnnotations();
			stamp.setFullCompression();


		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} 
			catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (stamp != null){
				try {
					stamp.close();
				} catch (Exception e) {
					LOG.error(e);
				}
			}
			if (templateReader != null){
				templateReader.close();
			}
			
		}
//			document.close();
		return baos.toByteArray();
	}
}
