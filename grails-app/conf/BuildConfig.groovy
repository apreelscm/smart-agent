grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.test.otherResources
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.reload.enabled = true
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]
grails.project.dependency.resolver = "maven" // or ivy
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
//
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
        compile 'org.springframework:spring-oxm:4.0.5.RELEASE'
        compile 'org.springframework.ws:spring-ws-core:2.2.0.RELEASE'
        compile 'org.springframework.ws:spring-xml:2.2.0.RELEASE'

        runtime 'com.google.code.gson:gson:2.2.4'
        runtime 'joda-time:joda-time:2.3'
        runtime 'com.lowagie:itext:2.1.7'
        runtime 'org.apache.poi:poi:3.10-FINAL'
        runtime 'com.google.guava:guava:18.0'

        test 'org.mockito:mockito-all:1.9.5'
        test 'org.powermock:powermock-core:1.5.5'
        test 'org.powermock:powermock-api-mockito:1.5.5'
    }

    plugins {
        build ":tomcat:7.0.54"

        compile ":scaffolding:2.1.1"
        compile ":cache:1.1.7"
        compile ":asset-pipeline:2.0.19"
        compile ":mail:1.0.6"
        compile ":quartz:1.0.1"
        compile ":build-info-tag:0.3.1"
        compile ":webflow:2.1.0"

        runtime ":hibernate4:4.3.5.3"
        runtime ":jquery:1.11.1"
        runtime ":spring-security-core:2.0-RC4"
    }
}
