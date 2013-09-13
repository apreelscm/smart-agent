package signaturepad;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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

    public static BufferedImage convertJsonToImage(String jsonString){
        System.out.println( "convertJsonToImage:"+jsonString);
                Gson gson = new Gson();
        SignatureLine[] signatureLines = gson.fromJson(jsonString, SignatureLine[].class);
        
        BufferedImage offscreenImage = new BufferedImage(600, 250, BufferedImage.TYPE_INT_ARGB);
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
}
