package pdfgenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PdfGenerator {
	private static Logger LOG = Logger.getLogger(PdfGenerator.class);
	
	private static String ARIAL_FONT_PATH = "web-app"+File.separator+"fonts"+File.separator+"arial.ttf";
	private static String ARIALBOLD_FONT_PATH = "web-app"+File.separator+"fonts"+File.separator+"arialbd.ttf";
	
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
			
			URL url;
			try {
				url = new URL(urlTemplatePath);
			} catch (Exception e) {
				url = new File(urlTemplatePath).toURI().toURL();
			}
			LOG.info("URL to PDF file: " + url);
			templateReader = new PdfReader(url);
			stamp = new PdfStamper(templateReader, baos);
			AcroFields form = stamp.getAcroFields();
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				
				if (dataEntry.getValue().length > 1 && dataEntry.getValue()[1].isEmpty() == false){
					form.setFieldProperty(dataEntry.getKey(), "textsize", Float.valueOf(dataEntry.getValue()[1]), null);
				}
				
				if (fontsPathMap != null && fontsPathMap.containsKey(dataEntry.getKey())){
					BaseFont bf = null;
					if("HELVETICA".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else if ("ARIAL".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(ARIAL_FONT_PATH, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else if ("ARIALBOLD".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(ARIALBOLD_FONT_PATH, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else {
						bf = BaseFont.createFont(fontsPathMap.get(dataEntry.getKey()), BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					
					form.setFieldProperty(dataEntry.getKey(), "textfont", bf, null);	
					form.addSubstitutionFont(bf);
					
				}
				
				if (dataEntry.getValue().length > 2) {
					
					if ("checkbox".equals(dataEntry.getValue()[2])) {
						String[] states = form.getAppearanceStates(dataEntry.getKey());

                        if (states != null){
                            if ("false".equals(dataEntry.getValue()[0])) {
                                form.setField(dataEntry.getKey(), states[0]);
                            } else {
                                form.setField(dataEntry.getKey(), states[1]);
                            }
                        }
					}
					else if ("signature".equals(dataEntry.getValue()[2])) {
						Integer pageNo = Integer.valueOf(dataEntry.getValue()[3]);
						Integer x = Integer.valueOf(dataEntry.getValue()[4]);
						Integer y = Integer.valueOf(dataEntry.getValue()[5]);
						Integer xScale = Integer.valueOf(dataEntry.getValue()[6]);
						Integer yScale = Integer.valueOf(dataEntry.getValue()[7]);
						Image img = Image.getInstance(new URL(dataEntry.getValue()[0]));
						
						PdfContentByte content = stamp.getOverContent(pageNo);
						
						img.setAbsolutePosition(x,y);
						img.scaleAbsolute(xScale,yScale);
						content.addImage(img);
					}
				}
				else {
					if (dataEntry.getValue() != null && dataEntry.getValue().length > 0 && dataEntry.getValue()[0] != null) {
						form.setField(dataEntry.getKey(),dataEntry.getValue()[0]);
					}
				}

			}

			stamp.setFormFlattening( true );
			stamp.getReader().removeUnusedObjects();
			stamp.getReader().removeAnnotations();
			stamp.setFullCompression();


		} catch (DocumentException e) {
			//throw new RuntimeException(e);
		} 
		catch (IOException e) {
			//throw new RuntimeException(e);
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
				
				if (dataEntry.getValue().length > 1 && dataEntry.getValue()[1].isEmpty() == false){
					form.setFieldProperty(dataEntry.getKey(), "textsize", Float.valueOf(dataEntry.getValue()[1]), null);
				}
				
				if (fontsPathMap != null && fontsPathMap.containsKey(dataEntry.getKey())){
					BaseFont bf = null;
					if("HELVETICA".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else if ("ARIAL".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(ARIAL_FONT_PATH, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else if ("ARIALBOLD".equals(fontsPathMap.get(dataEntry.getKey()))) {
						bf = BaseFont.createFont(ARIALBOLD_FONT_PATH, BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					else {
						bf = BaseFont.createFont(fontsPathMap.get(dataEntry.getKey()), BaseFont.CP1250, BaseFont.EMBEDDED);
					}
					
					form.setFieldProperty(dataEntry.getKey(), "textfont", bf, null);	
					form.addSubstitutionFont(bf);
					
				}
				
				if (dataEntry.getValue().length > 2) {
					
					if (dataEntry.getValue()[2].equals("checkbox")) {
						String[] states = form.getAppearanceStates(dataEntry.getKey());
						
						if (states != null) {
							if ("false".equals(dataEntry.getValue()[0])) {
								form.setField(dataEntry.getKey(), states[0]);
							}
							else {
								form.setField(dataEntry.getKey(), states[1]);
							}
						}
					}
					else if (dataEntry.getValue()[2].equals("signature")) {
						Integer pageNo = Integer.valueOf(dataEntry.getValue()[3]);
						Integer x = Integer.valueOf(dataEntry.getValue()[4]);
						Integer y = Integer.valueOf(dataEntry.getValue()[5]);
						Integer xScale = Integer.valueOf(dataEntry.getValue()[6]);
						Integer yScale = Integer.valueOf(dataEntry.getValue()[7]);
						Image img = Image.getInstance(new URL(dataEntry.getValue()[0]));
						
						PdfContentByte content = stamp.getOverContent(pageNo);
						
						img.setAbsolutePosition(x,y);
						img.scaleAbsolute(xScale,yScale);
						content.addImage(img);
					}
				}
				else {
					form.setField(dataEntry.getKey(),dataEntry.getValue()[0]);
				}

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
