package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantSearchStatus
import com.eservice.eumowy.microbisnode.model.Organization
import grails.test.mixin.TestFor
import org.springframework.context.support.StaticMessageSource
import spock.lang.Shared
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MicroBisnodeService)
class MicroBisnodeServiceSpec extends Specification {

    @Shared
    def messageSource = new StaticMessageSource()

    def NON_EXISTING_NIP = "9591582323"

    def setup() {
        def microBisnodeClient = mockFor(MicroBisnodeClient)
        microBisnodeClient.demand.getOrganizationByIdentifier { String nip ->
            if (nip != NON_EXISTING_NIP){
                return new Organization()
            }
            throw new OrganizationNotFoundException(nip)
        }
        service.microBisnodeClient = microBisnodeClient.createMock()
    }

    def cleanup() {
    }

    def "getOrganizationByIdentifier should return nothing when user not found in microBisnode "() {

        when:
        MerchantDetailsDTO merchantDetailsDTO = service.getMerchantDetailsByIdentifier(NON_EXISTING_NIP)

        then:
        noExceptionThrown()
        merchantDetailsDTO.status == MerchantSearchStatus.NOT_FOUND

    }
}
