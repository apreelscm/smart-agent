package com.eservice.eumowy.auth.microldap


import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MicroLDAPClientImpl implements MicroLDAPClient {

    def log = Logger.getLogger(MicroLDAPClientImpl.class)

    def String microLdapServiceURI
    def RestTemplate restTemplate
    def headers = new HttpHeaders();

    MicroLDAPClientImpl(String microLdapServiceURI) {
        this.microLdapServiceURI = microLdapServiceURI
        if (this.microLdapServiceURI && !this.microLdapServiceURI.endsWith("/")){
            this.microLdapServiceURI += "/"
        }
        log.info("micro ldap service client configured on address " + microLdapServiceURI)

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>()
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter()
        messageConverters.add(converter)
        this.restTemplate = new RestTemplate(messageConverters)
        this.headers.setContentType(MediaType.APPLICATION_JSON);

    }

    @Override
    AuthResponse authAdUser(String login, String password) {
        log.info("try to authenticate user " + login)
        URI url = new URI(microLdapServiceURI + "/authAdUser")
        def correlationId = generateCorrelationID()
        HttpEntity<String> request = new HttpEntity<String>(createAuthRequest(login, password, correlationId).toString(), headers);
        AuthResponse authResponse = this.restTemplate.postForObject(url, request, AuthResponse.class)
        if (!correlationId.equals(authResponse.correlationID)){
            throw new IllegalStateException("request correlationId doesn't match response one")
        }
        return authResponse
    }

    private JSONObject createAuthRequest(String login, String password, Long correlationId){
        JSONObject requestJson = new JSONObject()
        requestJson.put("user", new User(username: login, password: password))
        requestJson.put("correlationId", correlationId)
        return requestJson
    }

    private Long generateCorrelationID(){
        return 123123
    }
}
