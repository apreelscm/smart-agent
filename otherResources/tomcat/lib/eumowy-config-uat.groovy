environments {
    uat{
        eumowySyncWSAddress="http://localhost:8080/e-umowy-sync-ws/acceptUmowa/"
        eumowyCOAEmail="coa@eservice.com.pl"

		appParametersPaths = [
			"pdfTemplates": "/opt/eumowy/pdf_templates/",
			"pdfImages": "/opt/eumowy/pdf_images/",
			"pdfPreviews": "/opt/eumowy/pdf_previews/",
            "mobileAppPath":"/opt/eumowy/mobile/",
            "beneficiary": "/opt/eumowy/beneficiary/"
		]
		appParametersDisallowDownloads = ["pdfTemplates"]

        grails.mail.host = "192.168.3.140"
        grails.mail.port = 25
        grails.mail.username = "ldamiecki@testeservice.com"
        grails.mail.password = "Standard1"

        trustAll = true

        email.sending.enabled = true
    }
}