package signaturepad;

import com.eservice.eumowy.Subscription;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.eservice.eumowy.DocumentsSigningService.*;
import static java.awt.Font.PLAIN;
import static java.awt.RenderingHints.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

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
	private static final String FONT_NAME = "Arial";
	private static final int FONT_SIZE = 48;
	private static final int LINE_PADDING = 5;

	static Logger LOG = Logger.getLogger(SignatureToImage.class);
	
	public static BufferedImage convertDataToImage(Subscription signature) {
		String data = signature.getContent();
		BufferedImage img = null;
		
		if (data.contains("data:image/png;base64,")) {
			img = convertBase64ToImage(data);
		} else if (data.startsWith(CODE_SIGNATURE_MARKER)) {
			img = createImage(data);
		} else {
			img = convertJsonToImage(data);
		}
		return img;
	}
	
	public static BufferedImage convertDataToImage(URL url) {
		return convertURIFileToImage(url);
	}
	
    private static BufferedImage convertURIFileToImage(URL data) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(data);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("Error while generating BufferedImage from URL! - " + e);
		}
		return img;
	}

	private static BufferedImage convertJsonToImage(String jsonString){
        LOG.info( "convertJsonToImage:"+jsonString);
        Gson gson = new Gson();
        SignatureLine[] signatureLines = gson.fromJson(jsonString, SignatureLine[].class);
        
        BufferedImage offscreenImage = new BufferedImage(700, 300, TYPE_INT_ARGB);
        Graphics2D g2 = offscreenImage.createGraphics();
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(1,1,1,1));
        g2.fillRect(0,0,700,300);
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        
        for (SignatureLine line : signatureLines) {
            g2.drawLine(line.lx, line.ly, line.mx, line.my);
        }
        return offscreenImage;
    }
    
    private static BufferedImage convertBase64ToImage(String baseimg) {
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

	private static BufferedImage createImage(String text) {
		/*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
		String[] content = StringUtils.split(text,"|");
		String signatureText = content[PHONE_NUMBER_IDX] + " " + content[DATE_IDX] + " " + content[CODE_IDX];
		BufferedImage img = new BufferedImage(1, 1, TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		Font font = new Font(FONT_NAME, PLAIN, FONT_SIZE);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = calcImageWidth(fm, signatureText);
		int height = calcImageHeight(fm);
		g2d.dispose();

		img = new BufferedImage(width, height, TYPE_INT_ARGB);
		g2d = img.createGraphics();
		g2d.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(KEY_DITHERING, VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setColor(Color.BLACK);
		g2d.drawString(signatureText, 0, fm.getAscent());
		g2d.dispose();

		return img;
	}

	private static int calcImageWidth(FontMetrics fm, String text) {
		return fm.stringWidth(text);
	}

	private static int calcImageHeight(FontMetrics fm) {
		return fm.getHeight();
	}
}
