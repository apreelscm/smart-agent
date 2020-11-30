package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.eumowy.microbisnode.model.Organization
import org.springframework.context.MessageSource

class MicroBisnodeService {

    MicroBisnodeClient microBisnodeClient
    MessageSource messageSource

    List<MerchantRepresentativeDTO> getRepresentatives(String nip) {
        MerchantDetailsDTO merchantDetails = getMerchantDetailsByIdentifier(nip)

        if(!merchantDetails || !merchantDetails.success()) {
            log.error(String.format("Merchant details not found for NIP %s. Returning empty representatives list.", nip))
            return Collections.emptyList()
        }

        return merchantDetails.representatives
    }

    MerchantDetailsDTO getMerchantDetailsByIdentifier(String identifier) {

        Organization organization
        try {
            organization = microBisnodeClient.getOrganizationByIdentifier(identifier)
            log.debug(String.format("Client with identifier %s found in MicroBisnode", identifier))
            MerchantDetailsDTO merchantDetailsDTO = new OrganizationToMerchantDetailsDTOMapper(messageSource).map(organization)
            log.debug(merchantDetailsDTO)
            return merchantDetailsDTO
        } catch (OrganizationNotFoundException e){
            log.info(String.format("Client with identifier %s not found in MicroBisnode", identifier))
            return MerchantDetailsDTO.notFound()
        } catch (BisnodeConnectionException e){
            return MerchantDetailsDTO.errorResult()
        } catch (BisnodeMappingException e){
            return MerchantDetailsDTO.mappingProblem()
        } catch (Exception e) {
            log.error(e)
            return MerchantDetailsDTO.errorResult()
        }
    }

}
