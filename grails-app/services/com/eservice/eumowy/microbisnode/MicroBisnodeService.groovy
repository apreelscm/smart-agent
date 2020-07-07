package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.eumowy.microbisnode.model.Organization

class MicroBisnodeService {

    MicroBisnodeClient microBisnodeClient

    private OrganizationToMerchantDetailsDTOMapper dtoMapper = new OrganizationToMerchantDetailsDTOMapper()

    List<MerchantRepresentativeDTO> getRepresentatives(String nip) throws BisnodeConnectionException, BisnodeMappingException {
        MerchantDetailsDTO merchantDetails = getMerchantDetailsByIdentifier(nip)

        if(!merchantDetails) {
            log.error(String.format("Merchant details not found for NIP %s. Returning empty representatives list.", nip))
            return Collections.emptyList()
        }

        return merchantDetails.representatives
    }

    MerchantDetailsDTO getMerchantDetailsByIdentifier(String identifier) throws BisnodeConnectionException, BisnodeMappingException {

        Organization organization
        try {
            organization = microBisnodeClient.getOrganizationByIdentifier(identifier)
            log.debug(String.format("Client with identifier %s found in MicroBisnode", identifier))
            MerchantDetailsDTO merchantDetailsDTO = dtoMapper.map(organization)
            log.debug(merchantDetailsDTO)
            return merchantDetailsDTO
        } catch (OrganizationNotFoundException e){
            log.info(String.format("Client with identifier %s not found in MicroBisnode", identifier))
        }
        return null
    }

}
