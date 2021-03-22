package com.eservice.eumowy.microbisnode

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Ignore
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class MicroBisnodeClientImplSpec extends Specification {

    MicroBisnodeClient tested = new MicroBisnodeClientImpl("http://10.9.23.29:8114/bisnode/")

    def "should throw OrganizationNotFoundException when organization not found"(){

        given: "not existed NIP"
        def nip = '92312443233'

        when:
        tested.getOrganizationByIdentifier(nip)

        then:
        thrown(OrganizationNotFoundException)
    }

    def "should return Organization model when returned from service"(){

        given: "not existed NIP"
        def nip =  '7281704155' // '9441229038'

        when:
        tested.getOrganizationByIdentifier(nip)

        then:
        notThrown(OrganizationNotFoundException)
    }
}
