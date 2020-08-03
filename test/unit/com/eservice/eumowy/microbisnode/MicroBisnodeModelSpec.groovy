package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.microbisnode.model.Organization
import com.eservice.eumowy.mocks.MicroBisnodeClientMock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Ignore
import spock.lang.Specification

/**
 * specification for microbisnode model verification
 * based on JSON response samples
 *
 * @see com.eservice.eumowy.microbisnode.model.Organization
 */
@TestMixin(GrailsUnitTestMixin)
class MicroBisnodeModelSpec extends Specification {

    private String testJsonResponsePath = new File("test/unit/com/eservice/eumowy/microbisnode").getAbsolutePath()
    private MicroBisnodeClientMock clientMock = new MicroBisnodeClientMock(testJsonResponsePath)

    //@Ignore
    void "should parse correctly JSON response to model for small organization 9441229038 "(){

        when:
        Organization organization = clientMock.getOrganizationByIdentifier("9441229038")

        then:
        noExceptionThrown()
        organization != null

    }

    @Ignore
    void "should parse correctly JSON response to model for medium organization 9532457650 "(){

        when:
        Organization organization = clientMock.getOrganizationByIdentifier("9532457650")

        then:
        noExceptionThrown()
        organization != null

    }




}
