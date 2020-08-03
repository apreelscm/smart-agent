package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.microbisnode.model.Organization
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MicroBisnodeService)
class MicroBisnodeServiceSpec extends Specification {

    def NON_EXISTING_NIP = "9591582323"

    def setup() {
        MicroBisnodeClient.metaClass.getOrganizationByIdentifier = { def nip ->
            if (nip != NON_EXISTING_NIP){
                return new Organization()
            }
            return null
        }
    }

    def cleanup() {
    }

    def "getOrganizationByIdentifier should return nothing when user not found in microBisnode "() {

        when:
        MerchantDetailsDTO merchantDetailsDTO = service.getMerchantDetailsByIdentifier(NON_EXISTING_NIP)

        then:
        noExceptionThrown()
        merchantDetailsDTO == null

    }
}
