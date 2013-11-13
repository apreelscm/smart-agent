package signaturepad;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.UrlValidator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 *	Signature to Image: A supplemental script for Signature Pad that
 *	generates an image of the signature’s JSON output server-side using Java.
 *
 *	@project	signaturetoimage
 *	@author		Vinod Kiran (vinodkiran@usa.net)
 *	@link		http://github.com/vinodkiran/SignatureToImageJava
 *	@version	1.0.0
 */
public class SignatureToImage {
	static Logger LOG = Logger.getLogger(SignatureToImage.class);
	
	public static BufferedImage convertDataToImage(String data) {
		BufferedImage img = null;
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES+UrlValidator.ALLOW_2_SLASHES);
		
		if (data.contains("data:image/png;base64,")) {
			img = convertBase64ToImage(data);
		}
		else if (validator.isValid(data)) {
			img = convertURIFileToImage(data);
		}
		else {
			img = convertJsonToImage(data);
		}
		return img;
	}
	
    public static BufferedImage convertURIFileToImage(String data) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL(data));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("Error while generating BufferedImage from URL! - " + e);
		}
		return img;
	}

	public static BufferedImage convertJsonToImage(String jsonString){
        LOG.info( "convertJsonToImage:"+jsonString);
        Gson gson = new Gson();
        SignatureLine[] signatureLines = gson.fromJson(jsonString, SignatureLine[].class);
        
        BufferedImage offscreenImage = new BufferedImage(700, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = offscreenImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(1,1,1,1));
        g2.fillRect(0,0,700,300);
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        
        for (SignatureLine line : signatureLines) {
            g2.drawLine(line.lx, line.ly, line.mx, line.my);
        }
        return offscreenImage;
    }
    
    public static BufferedImage convertBase64ToImage(String baseimg) {
    	baseimg = baseimg.replace("data:image/png;base64,", "");
    	Base64 decoder = new Base64();   
    	byte[] imgBytes = decoder.decode(baseimg);
    	InputStream in = new ByteArrayInputStream(imgBytes);
    	BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			LOG.info("Could not convert Base64 encoded image to BufferedImage!!");
			e.printStackTrace();
		}
    	return img;
    }
}
