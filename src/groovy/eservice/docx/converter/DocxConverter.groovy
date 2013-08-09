package eservice.docx.converter

import eservice.docx.converter.domain.Dokument
import fr.opensagres.xdocreport.document.IXDocReport
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.IContext
import fr.opensagres.xdocreport.template.TemplateEngineKind
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour
import org.apache.poi.xwpf.converter.pdf.PdfConverter
import org.apache.poi.xwpf.converter.pdf.PdfOptions
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.event.EventCartridge
import org.apache.velocity.app.event.ReferenceInsertionEventHandler

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 18.07.13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
class DocxConverter {

    def static DOCUMENT_OBJECT_NAME = "d";
    def static FIELD_NAME_SUFFIX = ".fields.xml";

    def static convert(templateFileName, outputFileName, dokument){

        InputStream inputStream = new FileInputStream(templateFileName);
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport( inputStream, TemplateEngineKind.Velocity );

        // 2) Create context Java model
        IContext context = report.createContext();

        EventCartridge eventCartridge = new EventCartridge();
        eventCartridge.addEventHandler(new ReferenceInsertionEventHandler() {
            @Override
            public Object referenceInsert(String string, Object object) {
                (object == null)? "": object;
            }
        });
        eventCartridge.attachToContext((VelocityContext) context);

        FieldsMetadata metadata = report.createFieldsMetadata();
        metadata.addFieldAsImage( "podpisPierwszegoReprezentanta", DOCUMENT_OBJECT_NAME+".Akceptant.PierwszyReprezentant.Podpis", NullImageBehaviour.RemoveImageTemplate );
        metadata.addFieldAsImage( "podpisDrugiegoReprezentanta", DOCUMENT_OBJECT_NAME+".Akceptant.DrugiReprezentant.Podpis", NullImageBehaviour.RemoveImageTemplate);
        metadata.addFieldAsImage( "podpisPh", DOCUMENT_OBJECT_NAME+".Ph.Podpis", NullImageBehaviour.RemoveImageTemplate);

        metadata.addFieldAsList("zestawyPOM.PelnaNazwaPunktu");
        metadata.addFieldAsList("zestawyPOM.UlicaINumerDomu");
        metadata.addFieldAsList("zestawyPOM.Miejscowosc");
        metadata.addFieldAsList("zestawyPOM.KodPocztowy");
        metadata.addFieldAsList("zestawyPOM.LiczbaZestawowZUslugaDCC");

        context.put(DOCUMENT_OBJECT_NAME, dokument);
        context.put("zestawyPOM", dokument.pomPoints);
        context.put("platnosciPOS", dokument.platnosciPOS);
        context.put("oplatyPOS", dokument.oplatyZaPOS);
        context.put("prefrencyjneOplatyPOS", dokument.preferencyjneOplatyZaPOS);
        context.put("myStringUtils", new MyStringUtils());

        // 3) Generate report by merging Java model with the Docx
        OutputStream outputStream = new FileOutputStream(new File(outputFileName));
        report.process( context, outputStream );

        convertToPDFWithXWPDF(outputFileName);
    }

    def static convertToPDFWithXWPDF(fileName){
        XWPFDocument document = new XWPFDocument( new FileInputStream(fileName));

        // 2) Convert POI XWPFDocument 2 PDF with iText
        String pdfName = fileName.substring(0, fileName.indexOf(".docx")).concat(".pdf");

        OutputStream out = new FileOutputStream( pdfName );
        PdfOptions options = PdfOptions.create().fontEncoding( "windows-1250" );

        PdfConverter.getInstance().convert( document, out, options );
    }

    def static generateProjectFieldsFile(String fileName){

        // 1) Create FieldsMetadata by setting Velocity as template engine
        FieldsMetadata fieldsMetadata = new FieldsMetadata(TemplateEngineKind.Velocity.name());

        // 2) Load fields metadata from Java Class
        //fieldsMetadata.load("project", Project.class);
        // Here load is called with true because model is a list of Developer.
        //fieldsMetadata.load("developers", Developer.class, true);

        fieldsMetadata.load(DOCUMENT_OBJECT_NAME, Dokument.class);

        // 3) Generate XML fields in the file "project.fields.xml".
        // Extension *.fields.xml is very important to use it with MS Macro XDocReport.dotm
        // FieldsMetadata#saveXML is called with true to indent the XML.

        File xmlFieldsFile = new File(fileName.endsWith(FIELD_NAME_SUFFIX)?fileName:fileName+FIELD_NAME_SUFFIX);
        fieldsMetadata.saveXML(new FileOutputStream(xmlFieldsFile), true);

    }

//    public static void main(String[] args){
//        DocxConverter.generateProjectFieldsFile("project_test_1_properties");
//    }

}
