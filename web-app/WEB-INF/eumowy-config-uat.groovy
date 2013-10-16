environments {
    uat{
        eumowySyncWSAddress="http://localhost:8080/e-umowy-sync-ws/acceptUmowa/"

        pdfTemplatePath="/opt/eumowy/pdf_templates/"
        tempPdfPreviewStoragePath="tmp"
        tempPdfImageStoragePath="files/pdf_images/"
        tempPdfImageStorageUri="/eumowy/files/pdf_images/"
        fontUri="eumowy/fonts"
        subscriptionsPath="files/"

        grails.mail.host = "192.168.3.140"
        grails.mail.port = 25
        grails.mail.username = "ldamiecki@testeservice.com"
        grails.mail.password = "Standard1"
    }
}