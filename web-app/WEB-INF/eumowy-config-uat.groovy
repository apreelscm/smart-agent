environments {
    uat{
        grails.mail.host = "192.168.3.140"
        grails.mail.port = 25
        grails.mail.username = "ldamiecki@testeservice.com"
        grails.mail.password = "Standard1"
        grails.mail.props = ["mail.smtp.auth": "true",
                "mail.smtp.socketFactory.port": "25",
                "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback": "false",
                "mail.smtp.ssl.trust": "*"]
    }
}