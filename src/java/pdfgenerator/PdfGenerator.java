package pdfgenerator;

import com.eservice.eumowy.DocumentContent;
import com.eservice.eumowy.PdfService;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfGenerator {
	private static Logger LOG = Logger.getLogger(PdfGenerator.class);

	public static byte[] addImageToPdfContent(String templatePath, byte[] content, Map<String, Object[]> imageMap) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfReader templateReader = null;
		PdfStamper stamp = null;
		
		try {
			templateReader = new PdfReader(content);
			stamp = new PdfStamper(templateReader, baos);
			
			for(Map.Entry<String, Object[]> dataEntry : imageMap.entrySet()) {
				try {
					Integer pageNo = (Integer)dataEntry.getValue()[1];
					Integer x = (Integer)dataEntry.getValue()[2];
					Integer y = (Integer)dataEntry.getValue()[3];
					Integer xScale = (Integer)dataEntry.getValue()[4];
					Integer yScale = (Integer)dataEntry.getValue()[5];
					Image img = Image.getInstance((java.awt.Image)dataEntry.getValue()[0], null);
					
					PdfContentByte pdfContent = stamp.getOverContent(pageNo);
					
					img.setAbsolutePosition(x,y);
					img.scaleAbsolute(xScale,yScale);
					pdfContent.addImage(img);
				}
				catch (Exception e) {
					LOG.info("Error while adding signature to document! Template path: " + templatePath);
					e.printStackTrace();
				}
			}
			
		} catch (DocumentException e) {
	        LOG.info("addImageToPdfContent - DocumentException ("+templatePath+"): ", e);
		} 
		catch (IOException e) {
	        LOG.info("addImageToPdfContent - IOException ("+templatePath+"): ", e);
		}
		catch (Exception e) {
			LOG.info("addImageToPdfContent - Exception ("+templatePath+"): ", e);
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
	//		document.close();
		return baos.toByteArray();
	}
	
	/**
	 * 
	 * @param urlTemplatePath
	 * @param dataMap
	 * @return
	 */
	public static byte[] generatePdfContentFromURI(String urlTemplatePath, Map<String,String[]> dataMap, PdfService.FontType fontType, String fPath) {
        Map<String,PdfService.FontType> fontsPathMap = new HashMap<String, PdfService.FontType>();
		if (fontType != null && dataMap != null){
			for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()){
				fontsPathMap.put(dataEntry.getKey(), fontType);
			}
		}
		return generatePdfContentFromURI(urlTemplatePath,dataMap,fontsPathMap, fPath);
	}
	
	/**
	 * 
	 * @param urlTemplatePath
	 * @param dataMap
	 * @param fontsPathMap
	 * @return
	 */
	private static byte[] generatePdfContentFromURI(String urlTemplatePath, Map<String,String[]> dataMap, Map<String,PdfService.FontType> fontsPathMap, String fPath) {
        if (urlTemplatePath == null){
			throw new IllegalArgumentException("urlTemplatePath param shouldn't be null");
		}

        StopWatch stopWatch = new Log4JStopWatch();

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
                    PdfService.FontType fontType = fontsPathMap.get(dataEntry.getKey());

                    switch(fontType){
                        case HELVETICA:
                            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
                            break;
                        case ARIAL:
                        case ARIALBOLD:
                            bf = BaseFont.createFont(fPath+File.separator+fontType.field, BaseFont.CP1250, BaseFont.EMBEDDED);
                            break;
                        case TIMES_NEW_ROMAN_PSMT:
                            break;
                        default:
                            bf = BaseFont.createFont(fontType.name(), BaseFont.CP1250, BaseFont.EMBEDDED);
                            break;
                    }
					
					if(dataEntry.getValue().length <= 2 || (dataEntry.getValue().length > 2 && "checkbox".equals(dataEntry.getValue()[2]) == false)) {
						form.setFieldProperty(dataEntry.getKey(), "textfont", bf, null);
						form.addSubstitutionFont(bf);
					}
					
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
						try {
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
						catch (Exception e) {
							LOG.info("Error while adding signature to document! URI Template Path: " + urlTemplatePath );
							e.printStackTrace();
						}
					}
				}
				else {
					if (dataEntry.getValue() != null && dataEntry.getValue().length > 0 && dataEntry.getValue()[0] != null) {
						form.setField(dataEntry.getKey(),dataEntry.getValue()[0]);
					}
				}

			}

            //konieczne jest zahashowanie tych dwoch opcji, gdy sa odhashowane nie ma mozliwosci
            //ponownej edycji dokumentu (usuniecie pola 'dataUmowy' i jego pochodnych
			//stamp.setFormFlattening( true );
			//stamp.getReader().removeAnnotations();
            stamp.getReader().removeUnusedObjects();
			stamp.setFullCompression();


		} catch (DocumentException e) {
            LOG.info("DocumentException (" + urlTemplatePath + "): ", e);
		} 
		catch (IOException e) {
            LOG.info("IOException (" + urlTemplatePath + "): ", e);
		}
		catch (Exception e) {
			LOG.info("Exception (" + urlTemplatePath + "): ", e);
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

            stopWatch.stop("generatePdfContentFromURI");

        }
//			document.close();
		return baos.toByteArray();
    }

    public static DocumentContent cleanValuesContent(DocumentContent dc, List<String> fieldsToClean){

        PdfReader templateReader = null;
        PdfStamper stamp = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
            templateReader = new PdfReader(dc.getContent());
            stamp = new PdfStamper(templateReader, baos);

            AcroFields form = stamp.getAcroFields();
            for (String field: fieldsToClean){
                form.setField(field, "");
            }
        } catch (IOException e){
            LOG.error(e);
        } catch (DocumentException e) {
            LOG.error(e);
        } finally {
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

        dc.setContent(baos.toByteArray());
        return dc;
    }

}
