package com.eservice.eumowy

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class RepresentativeSpec extends Specification {

    def "should properly concat address fields in one"(){

        given:
        def representative = new Representative(streetTitle: "UL", street: "Kielecka", houseNumber: "444", flatNumber: "4", city: "Warszawa", postalCode: "05-500", postOffice: "Nowa Iwiczna")
        when:
        def address = representative.getAddress()
        then:
        address == "ul. Kielecka 444 m. 4, 05-500 Warszawa"
    }

    def "should properly concat address fields in one when no flat number"(){

        given:
        def representative = new Representative(streetTitle: "UL", street: "Kielecka", houseNumber: "444", city: "Warszawa", postalCode: "05-500", postOffice: "Nowa Iwiczna")
        when:
        def address = representative.getAddress()
        then:
        address == "ul. Kielecka 444, 05-500 Warszawa"
    }

    def "should return old address when no separate fields"(){

        given:
        def ONE_ADDRESS = "UL. Kielecka 444, 05-500 Warszawa"
        def representative = new Representative(address: ONE_ADDRESS)
        when:
        def address = representative.getAddress()
        then:
        address == ONE_ADDRESS
    }

}