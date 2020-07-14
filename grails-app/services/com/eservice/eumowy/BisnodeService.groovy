package com.eservice.eumowy

import com.eservice.eumowy.auth.EServiceUserDetails
import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.eumowy.govsync.MerchantKRSDataDTOToMerchantDetailsDTOMapper
import com.eservice.webs.client.govsync.dto.MerchantKRSDataDTO

@Deprecated
/**
 * @see com.eservice.eumowy.microbisnode.MicroBisnodeService
 */
class BisnodeService {
    def websWebServiceClient
    def springSecurityService

    List<MerchantRepresentativeDTO> getRepresentatives(String nip) {
        MerchantDetailsDTO merchantDetails = getMerchantDetails(nip)

        if(!merchantDetails || ! merchantDetails.success()) {
            log.error(String.format("Merchant details not found for NIP %s. Returning empty representatives list.", nip))
            return Collections.emptyList()
        }

        return merchantDetails.representatives
    }

    MerchantDetailsDTO getMerchantDetails(String nip) {
        MerchantKRSDataDTO bisnodeMerchantDetails
        Long userId = ((EServiceUserDetails)springSecurityService.principal).auwId

        try {
            bisnodeMerchantDetails = websWebServiceClient.searchMerchantData(nip, userId)
        } catch (Exception e) {
            log.error("Error during data fetch from bisnode", e)
            return MerchantDetailsDTO.errorResult()
        }

        if (isMerchantDetailsValid(bisnodeMerchantDetails)) {
            log.info(String.format("Client with NIP %s not found in Bisnode.", nip))
            return MerchantDetailsDTO.notFound()
        }

        log.info(String.format("Client with NIP %s found in Bisnode", nip))

        MerchantDetailsDTO merchantDetails = MerchantKRSDataDTOToMerchantDetailsDTOMapper.map(bisnodeMerchantDetails)
        log.info("Merchant details: " + merchantDetails.toString())

        return merchantDetails
    }

    private isMerchantDetailsValid(MerchantKRSDataDTO merchantDetails) {
        return !merchantDetails && !merchantDetails?.id
    }
}
