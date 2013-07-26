package com.eservice.eumowy

import com.sun.pdfview.PDFFile
import com.sun.pdfview.PDFPage

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class PdfService {

    def generateImagesFormPDF(){

        def file = new File("web-app\\files\\1.pdf.pdf");
        FileChannel channel;
        try {
            def raf = new RandomAccessFile(file, "rws");

            channel = raf.getChannel();

            ByteBuffer  buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());

            PDFFile pdffile = new PDFFile(buf);

            // draw the first page to an image
            int num=pdffile.getNumPages();
            def img =new BufferedImage[num]

            for(int i=0;i<num;i++)
            {
                PDFPage page = pdffile.getPage(i+1);

                //get the width and height for the doc at the default zoom
                int width=(int)page.getBBox().getWidth();
                int height=(int)page.getBBox().getHeight();

                Rectangle rect = new Rectangle(0,0,width,height);
                int rotation=page.getRotation();
                Rectangle rect1=rect;
                if(rotation==90 || rotation==270)
                    rect1=new Rectangle(0,0,(int)rect.height,(int)rect.width);

                //generate the image
                img[i] = (BufferedImage)page.getImage(
                        width,height , //width & height
                        rect1, // clip rect
                        null, // null for the ImageObserver
                        true, // fill background with white
                        true  // block until drawing is done
                );

                //save the image to .pdf file
                ImageIO.write(img[i], "png",new File("web-app/pdf-images/"+(i+1)+"eumowy.png"));
            }
        }
        catch (FileNotFoundException e1) {
            System.err.println(e1.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }finally{
            channel.close();
            file = null;
        }
    }
}
