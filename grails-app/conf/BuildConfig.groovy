grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.test.otherResources
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        mavenRepo "https://repository.apache.org/content/repositories/snapshots/"

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        // runtime 'mysql:mysql-connector-java:5.1.22'
        provided 'jdbc:ojdbc:6'
        runtime 'cbd1:cbd-core-security:2.6-SNAPSHOT'
        runtime 'cbd2:cbd-orm:2.6-SNAPSHOT'
        runtime 'cbd3:cbd-orm-security:2.6-SNAPSHOT'
        runtime 'cbd4:cbd-orm-e-kalkulator:2.6-SNAPSHOT'
        runtime 'PDFRenderer:PDFRenderer:0.9.1'
        runtime 'com.google.code.gson:gson:2.2.4'
		runtime 'com.lowagie:itext:2.1.7'
//		runtime 'org.apache.pdfbox:pdfbox:1.8.2'
		runtime 'org.apache.pdfbox:pdfbox:2.0.0-SNAPSHOT'
        runtime 'joda-time:joda-time:2.3'
        runtime 'com.eservice:e-umowy-sync-ws-client:1.0'
        //runtime 'org.springframework.ws:spring-ws:2.1.2.RELEASE-all'
        runtime 'org.springframework.ws:spring-ws-core:2.1.3.RELEASE'
        runtime 'org.springframework.ws:spring-xml:2.1.3.RELEASE'
        runtime 'org.springframework.ws:spring-oxm:3.2.2.RELEASE'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":database-migration:1.3.2"
        runtime ":resources:1.2"
        runtime ":spring-security-core:1.2.7.3"
        runtime ":modernizr:2.6.2"
        compile ":quartz:1.0.1"
        /*runtime ":prototype:1.0"*/

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"

        compile ":build-info-tag:0.3.1"
        compile ':webflow:2.0.8.1'
        compile ":cache:1.1.1"
		//compile ":file-server:0.2"
      //  compile ":springcache:1.3.2-SNAPSHOT"
    }

}
