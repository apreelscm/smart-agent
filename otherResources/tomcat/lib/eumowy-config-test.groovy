environments {
    test{
        eumowySyncWSAddress="http://uat-eumowy.apreel.net:8080/e-umowy-sync-ws/acceptUmowa/"

		appParametersPaths = [
			"pdfTemplates": "/opt/eumowy/pdf_templates/",
			"pdfImages": "/opt/eumowy/pdf_images/",
			"pdfPreviews": "/opt/eumowy/pdf_previews/",
            "mobileAppPath":"/opt/eumowy/mobile/"
		]
		appParametersDisallowDownloads = ["pdfTemplates"]

        grails.mail.host = "mail.your-server.de"
        grails.mail.port = 465
        grails.mail.username = "atest@apreel.com"
        grails.mail.password = "atest"
        grails.mail.props = ["mail.smtp.auth": "true",
                "mail.smtp.socketFactory.port": "465",
                "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback": "false",
                "mail.smtp.ssl.trust": "*"]

        trustAll = true
    }
}