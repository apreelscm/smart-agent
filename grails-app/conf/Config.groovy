import grails.plugin.springsecurity.SecurityConfigType
import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

def dataSource
def grailsApplication

// referencja do konfiguracji srodowisk, poza dev parametr ustawiany przy starcie tomcata
grails.config.locations = ["classpath:${appName}-config-${grails.util.Environment.current.name}.groovy",
        "file:${userHome}/.grails/${appName}-config-${grails.util.Environment.current.name}.groovy"]


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.reload.enabled = true
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
isPanelsValidationOn = true;

grails.databinding.dateFormats = ['yyyy-MM-dd']


grails.cache.config = {
    cache = {
        name = 'eumowyCacheShort'
        maxElementsInMemory = 2000
        eternal = false
        timeToLiveSeconds = 600
        overflowToDisk = false
        memoryStoreEvictionPolicy = 'LRU'
    }
    cache = {
        name = 'eumowyCacheLong'
        maxElementsInMemory = 1000
        eternal = false
        timeToLiveSeconds = 36000
        overflowToDisk = false
        memoryStoreEvictionPolicy = 'LRU'
    }
}

grails.gorm.default.constraints = {
    '*'(nullable: false, blank:true)
    percentage(matches:'~|\\-|^(?:100(?:.0(?:0)?)?|\\d{1,2}(?:.\\d{1,2})?)$')
    number(matches:'~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,2})?$')
    number4Precision(matches:'~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,4})?$')
    number3Precision(matches:'~|\\-|^(?:[1-9]\\d*|0)?(?:\\.\\d{1,3})?$')
    natural(matches:'~|\\-|^[0-9]*')
    lettersOnly(matches:'~|^[A-Za-z\\-\\\'\\s\\u0104-\\u017c\\u00d3\\u00f3]*')
    alphanumericWithSlash(matches:'~|^[A-Za-z0-9\\s.\\-,/\\u0104-\\u017c\\u00d3\\u00f3]*')
    alphanumeric(matches:'~|^[A-Za-z0-9\\s.\\-,\\u0104-\\u017c\\u00d3\\u00f3]*')
    alphanumericWithBrackets(matches:'~|^[A-Za-z0-9()\\s.\\-,\\u0104-\\u017c\\u00d3\\u00f3]*')
    date(matches:'~|^\\d{4}$|^\\d{4}[\\-]((((0[13578])|([13578])|(1[02]))[\\-](([1-9])|([0-2][0-9])|(3[01])))|(((0[469])|([469])|(11))[\\-](([1-9])|([0-2][0-9])|(30)))|((2|02)[\\-](([1-9])|([0-2][0-9]))))$')
    email(matches:'~|^(.+)@(.+)$')
    postalCodeValidator(matches:'~|^[0-9]{2}[\\-][0-9]{3}$')
}

// log4j configuration
log4j = {


// Set level for all application artefacts
    info    "grails.app"
    debug   "grails.app.controller.com.eservice.umowy.activity"
    debug   "grails.app.controller.com.eservice.umowy"
    debug   "grails.app.controller.process"
    info    "grails.app.domain"
    info    "grails.app.taglib"
    info    performanceStatsAppender: 'org.perf4j.TimingLogger'
//    info    calcAppender: 'grails.app.services.com.eservice.eumowy.CbdService'

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

    // http REST logging
    //debug 'org.springframework.web.client'
    //debug 'org.apache.http' use with HttpComponentsClientHttpRequestFactory

    //show sql values
    //trace 'org.hibernate.type' , "org.codehaus.groovy.grails.orm.hibernate", 'org.hibernate.SQL'


    appenders {
        console name: 'console', layout: pattern(conversionPattern: '%d{dd-MM-yyyy HH:mm:ss,SSS} %5p %c - %m%n')
        /*appender new EumowyJDBCAppender(ConfigurationHolder.config.dataSource,
                "database",
                "INSERT INTO EUMOWY.LOGS (login, log_date, log_message) VALUES ('%X{sessionUserName}','%d{yyyy.MM.dd HH:mm:ss}', '%m')",
                org.apache.log4j.Level.INFO
        )*/

        String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"
        println("catalina.base dir : "+logDirectory)

        appender new DailyRollingFileAppender(
                name: 'file-roll',
                datePattern: "'.'yyyy-MM-dd",
                fileName: logDirectory+'/eumowy.log',  //storage path of log file
                layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n')
        )

        //----------------------------------------------
        // file appender that writes out the textual, aggregated performance stats generated by the performanceStatsAppender
        def performanceStatsFileAppender  = new org.apache.log4j.FileAppender(
                fileName: logDirectory+"/eumowy-perfStats.log",
                layout: pattern(conversionPattern: '%m%n')
        )
        appender name: 'performanceStatsFileAppender', performanceStatsFileAppender

        //----------------------------------------------
        def performanceStatsAppender = new org.perf4j.log4j.AsyncCoalescingStatisticsAppender(
                timeSlice: 300000    // ms
        )
        performanceStatsAppender.addAppender(performanceStatsFileAppender)
        appender name: 'performanceStatsAppender', performanceStatsAppender

        //----------------------------------------------
        def calcAppender = new DailyRollingFileAppender(
                name: 'file-calc-roll',
                datePattern: "'.'yyyy-MM-dd",
                fileName: logDirectory+'/eUmowy-kalkulator.log',  //storage path of log file
                layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n')
        )
        appender name: 'calcAppender', calcAppender
    }

    environments {
        mock        { root { additivity: false; info 'console' } }
        development { root { additivity: false; info 'console', 'file-calc-roll' } }
        test        { root { additivity: false; info 'console', 'file-roll', 'file-calc-roll' } }
        uat         { root { additivity: false; info 'file-roll', 'file-calc-roll' } }
        production  { root { additivity: false; info 'file-roll', 'file-calc-roll' } }
    }

    info database: ["audit"]
    additivity: false
}


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.eservice.eumowy.secure.SecUser'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.eservice.eumowy.secure.SecUserSecRole'
grails.plugin.springsecurity.authority.className = 'com.eservice.eumowy.secure.SecRole'


grails.plugin.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugin.springsecurity.interceptUrlMap = [
        '/process/**':	['hasRole("EUM_ZRD")'],
        '/activity/**': ['hasRole("EUM_PH_BZOS")'],
        '/login/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],  //leave the page open
        '/fonts*/**':	['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/assets*/**':  ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/tmp/**':		['IS_AUTHENTICATED_FULLY'],
        '/**':			['IS_AUTHENTICATED_FULLY']
]

xssSanitizer.enabled = true

fileuploader {
    attachments {
        maxSize =  10 * (1*1000*1024)
        allowedExtensions = ["jpg", "tiff", "pdf", "gif", "doc", "docx"]
        //path = "/tmp/attachment/"
    }
}

grails.gsp.reload.enable = true
