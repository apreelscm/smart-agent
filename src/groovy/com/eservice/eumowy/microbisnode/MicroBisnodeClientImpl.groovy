package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.microbisnode.model.Organization

import org.apache.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class MicroBisnodeClientImpl implements MicroBisnodeClient {

    def log = Logger.getLogger(MicroBisnodeClientImpl.class)

    private String microBisnodeServiceURI
    private RestTemplate restTemplate

    MicroBisnodeClientImpl(String microBisnodeServiceURI) {
        this.microBisnodeServiceURI = microBisnodeServiceURI
        if (this.microBisnodeServiceURI && !this.microBisnodeServiceURI.endsWith("/")){
            this.microBisnodeServiceURI += "/"
        }
        log.info("bisnode service client configured on address " + microBisnodeServiceURI)

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>()
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter()
        messageConverters.add(converter)
        this.restTemplate = new RestTemplate(messageConverters)
    }

    Organization getOrganizationByIdentifier(String identifierNumber) throws OrganizationNotFoundException, BisnodeConnectionException, BisnodeConnectionException {
        URI url = new URI(microBisnodeServiceURI +
                "organizations?countryISOAlpha2Code=PL&organizationIdentificationNumber=" +
                identifierNumber)
        try {
            return this.restTemplate.getForObject(url, Organization)
        } catch(HttpClientErrorException e){
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                // normal status for this API when organization not found
                throw new OrganizationNotFoundException(identifierNumber)
            } else {
                BisnodeConnectionException ex = new BisnodeConnectionException("error calling microBisnodeService for url " + url, e)
                log.error(ex)
                throw ex
            }
        } catch (HttpMessageConversionException e){
            log.error("error mapping bisnode response for identifier " + identifierNumber, e)
            throw new BisnodeMappingException(e)
        } catch (Exception e){
            BisnodeConnectionException ex = new BisnodeConnectionException("error calling microBisnodeService", e)
            log.error(ex)
            throw ex
        }
    }


}
