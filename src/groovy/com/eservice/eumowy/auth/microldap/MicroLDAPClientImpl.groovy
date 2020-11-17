package com.eservice.eumowy.auth.microldap


import groovy.json.JsonBuilder
import org.apache.log4j.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MicroLDAPClientImpl implements MicroLDAPClient {

    def log = Logger.getLogger(MicroLDAPClientImpl.class)

    String microLdapServiceURI
    String microLdapDomain
    String microLdapGroups
    String microLdapSystem = 'EUM'
    String microLdapDepartment

    RestTemplate restTemplate
    HttpHeaders headers = new HttpHeaders()

    MicroLDAPClientImpl(String microLdapServiceURI, String microLdapDomain, String microLdapGroups, String microLdapDepartment) {
        this.microLdapServiceURI = microLdapServiceURI
        if (this.microLdapServiceURI && !this.microLdapServiceURI.endsWith("/")){
            this.microLdapServiceURI += "/"
        }
        this.microLdapDomain = microLdapDomain
        this.microLdapGroups = microLdapGroups
        this.microLdapDepartment = microLdapDepartment
        log.info("micro ldap service client configured on address " + microLdapServiceURI)

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>()
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter()
        messageConverters.add(converter)
        this.restTemplate = new RestTemplate()
        //this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory())
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))

    }

    @Override
    AuthResponse authAdUser(String login, String password) {
        log.info("try to authenticate user " + login)
        URI url = new URI("http://uat-eumowy.apreel.net:8080/microLDAP/authAdUser")
        def correlationId = generateCorrelationID()
        def body = createAuthRequestBody(login, password, correlationId)
        HttpEntity<String> request = new HttpEntity<String>(body, headers)
        ResponseEntity<AuthResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, AuthResponse.class)
        return responseEntity.getBody()
    }

    private String createAuthRequestBody(String login, String password, Long correlationId){
        AuthRequest request = new AuthRequest(correlationId, new User(userName: login, password: password, domain: microLdapDomain, groups: microLdapGroups, system: microLdapSystem, department: microLdapDepartment))
        return new JsonBuilder(request).toString()
    }

    private Long generateCorrelationID(){
        return Math.abs(UUID.randomUUID().getMostSignificantBits())
    }
}
