package com.eservice.eumowy.mocks

import com.eservice.eumowy.microbisnode.MicroBisnodeClient
import com.eservice.eumowy.microbisnode.OrganizationNotFoundException
import com.eservice.eumowy.microbisnode.model.Organization
import groovy.json.JsonSlurper
import org.apache.log4j.Logger

class MicroBisnodeClientMock implements MicroBisnodeClient {

    def log = Logger.getLogger(MicroBisnodeClientMock.class)
    String mockResponseDirectory

    MicroBisnodeClientMock(mockResponseDirectory) {
        this.mockResponseDirectory = mockResponseDirectory
        if (!this.mockResponseDirectory.endsWith("/")){
            this.mockResponseDirectory += "/"
        }
    }

    @Override
    Organization getOrganizationByIdentifier(String identifierNumber) {

        Organization organization = null
        String jsonFilePath = mockResponseDirectory  +identifierNumber + ".json"
        File file = new File(jsonFilePath)
        log.debug("looking for mock response file " + jsonFilePath)
        if (file.exists()){
            //JSONObject jsonObject = new JSONObject(resource)
            def jsonSlurper = new JsonSlurper() // jsonObject.toString()
            //String jsonString = jsonObject.toString()
            def jsonMap = jsonSlurper.parse(file, 'UTF-8')
            log.debug(jsonMap)
            organization = new Organization(jsonMap)
            //organization = jsonSlurper.parseText(jsonObject.toString())

        } else {
            log.info("no mock data for identifier " + identifierNumber + " -> generate")

            throw new OrganizationNotFoundException(identifierNumber) // or create fake data as alternative

        }
        return organization
    }

}
