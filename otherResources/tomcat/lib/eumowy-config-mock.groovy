environments {
    development{
        eumowySyncWSAddress="http://uat-eumowy.apreel.lan:8080/e-umowy-sync-ws/acceptUmowa/"

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




        /** only for local usage with embeded Tomcat when JNDI datasource is defined **/
        grails.naming.entries = [
                'jdbc/eumowyDS': [
                        type: "javax.sql.DataSource",
                        auth: "Container",
                        driverClassName: "oracle.jdbc.driver.OracleDriver",
                        dialect : "org.hibernate.dialect.Oracle10gDialect",
                        maxActive: "8",
                        maxIdle: "4",
                        url: "jdbc:oracle:thin:@192.168.9.22:1523:tstcbd",
                        username: "eumowy_app",
                        password: "eumowy_app"
                ],
                'java:comp/env/jdbc/eumowyDS': [
                        type: "javax.sql.DataSource",
                        auth: "Container",
                        driverClassName: "oracle.jdbc.driver.OracleDriver",
                        dialect : "org.hibernate.dialect.Oracle10gDialect",
                        maxActive: "8",
                        maxIdle: "4",
                        url: "jdbc:oracle:thin:@192.168.9.22:1523:tstcbd",
                        username: "eumowy_app",
                        password: "eumowy_app"
                ]
        ]

        trustAll = true
    }
}