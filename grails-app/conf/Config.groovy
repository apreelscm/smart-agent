import grails.plugins.springsecurity.SecurityConfigType

import org.apache.catalina.security.SecurityConfig;
import org.apache.log4j.jdbc.JDBCAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
        all:           '*/*',
        atom:          'application/atom+xml',
        css:           'text/css',
        csv:           'text/csv',
        form:          'application/x-www-form-urlencoded',
        html:          ['text/html','application/xhtml+xml'],
        js:            'text/javascript',
        json:          ['application/json', 'text/json'],
        multipartForm: 'multipart/form-data',
        rss:           'application/rss+xml',
        text:          'text/plain',
		pdf:		   'application/pdf',
        xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*', '/tmp/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

grails.views.javascript.library="jquery"
grails.logging.jul.usebridge = true

environments {
    development {
        isPanelsValidationOn = true;
    }
    test {
        isPanelsValidationOn = true;
    }
    production {
        isPanelsValidationOn = true;
    }
}

grails.gorm.default.constraints = {
    '*'(nullable: false, blank:true)
    percentage(matches:'^(?:100(?:.0(?:0)?)?|\\d{1,2}(?:.\\d{1,2})?)$')
    number(matches:'^(?:[1-9]\\d*|0)?(?:\\.\\d{2})?$')
	natural(matches:'^[0-9]*')
	lettersonly(matches:'^[A-Za-z\\s\\u0104-\\u017c\\u00d3\\u00f3]*')
	alpha(matches:'^[A-Za-z0-9\\s.,\\u0104-\\u017c\\u00d3\\u00f3]*')
    //TODO - ten regexp nie jest doskonaly przepuszcza np. 30 lutego. Mozna/Trzeba to poprawic
	date(matches:'^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$')
}


// log4j configuration
log4j = {

// Set level for all application artefacts
    info "grails.app"
    debug "grails.app.controller.com.eservice.umowy.activity"
    debug "grails.app.controller.com.eservice.umowy"
    debug "grails.app.controller.process"
    info "grails.app.domain"
    info "grails.app.tagLib"

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration*//*
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    trace 'grails.plugin.mail'

    //show sql values
  /*  info "org.hibernate.SQL", "org.hibernate.type", "org.codehaus.groovy.grails.orm.hibernate"
    trace 'org.hibernate.type' debug 'org.hibernate.SQL'*/

  //  trace 'org.hibernate.type'
    info 'org.hibernate.SQL'


    appenders {
       console name: 'console', layout: pattern(conversionPattern: '%d{dd-MM-yyyy HH:mm:ss,SSS} %5p %c - %m%n')

        environments {
            development {
                appender new JDBCAppender(
                        name: "database",
                        URL: "jdbc:h2:mem:CbdDb;MODE=Oracle;MVCC=TRUE",
                        user: "sa",
                        password: "",
                        driver: "org.h2.Driver",
                        sql: "INSERT INTO EUMOWY.LOGS (login, log_date, log_message) VALUES ('%X{sessionUserName}','%d{yyyy.MM.dd HH:mm:ss}', '%m');",
                        threshold: org.apache.log4j.Level.INFO
                )
            }
            test {
                appender new JDBCAppender(
                        name: "database",
                        URL: "jdbc:oracle:thin:@db-eservice.apreel.lan:1521:cbd01out",
                        user: "eumowy_app",
                        password: "eumowy_app",
                        driver: "oracle.jdbc.driver.OracleDriver",
                        sql: "INSERT INTO EUMOWY.LOGS (login, log_date, log_message) VALUES ('%X{sessionUserName}','%d{yyyy.MM.dd HH:mm:ss}', '%m')",
                        threshold: org.apache.log4j.Level.INFO
                )
            }
        }
    }

    root {
        additivity: false
        info 'console'
    }

    info database: ["audit"]
    additivity: false
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.eservice.eumowy.secure.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.eservice.eumowy.secure.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'com.eservice.eumowy.secure.SecRole'


grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugins.springsecurity.interceptUrlMap = [
		'/process/**':	['hasRole("EUM_ZRD")'],
		'/activity/**': ['hasRole("EUM_PH_BZOS")'],
        '/login/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],  //leave the page open
        '/images*/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css*/**':		['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/fonts*/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/js*/**':		['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/tmp/**':		['IS_AUTHENTICATED_FULLY'],
        '/**':			['IS_AUTHENTICATED_FULLY']
]

// mail config
//grails {
//    mail {
//        host = "smtp.gmail.com"
//        port = 465
//        username = "apreel.eUmowy@gmail.com"
//        password = "apreel1234"
//        props = ["mail.smtp.auth": "true",
//                "mail.smtp.socketFactory.port": "465",
//                "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//                "mail.smtp.socketFactory.fallback": "false",
//                "mail.smtp.ssl.trust": "*"]
//    }
//}
grails {
    mail {
        host = "mail.your-server.de"
        port = 465
        username = "atest@apreel.com"
        password = "atest"
        props = ["mail.smtp.auth": "true",
                "mail.smtp.socketFactory.port": "465",
                "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback": "false",
                "mail.smtp.ssl.trust": "*"]
    }
}
trustAll = true

fileuploader {
    attachments {
        maxSize =  10 * FileUploaderTagLib._mbyte;
        allowedExtensions = ["jpg", "tiff", "pdf", "gif"]
        //path = "/tmp/attachment/"
    }
}

grails.gsp.reload.enable = true
