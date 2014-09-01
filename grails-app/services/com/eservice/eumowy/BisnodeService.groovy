package com.eservice.eumowy

import com.eservice.eumowy.auth.EServiceUserDetails
import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.webs.client.WebsClient
import com.eservice.webs.client.govsync.dto.MerchantKRSDataDTO
import grails.plugin.springsecurity.SpringSecurityService

class BisnodeService {
    def websWebServiceClient
    def springSecurityService

    public List<MerchantRepresentativeDTO> getRepresentatives(String nip) {
        MerchantDetailsDTO merchantDetails = getMerchantDetails(nip)

        if(!merchantDetails) {
            log.error(String.format("Merchant details not found for NIP %s. Returning empty representatives list.", nip))
            return Collections.emptyList()
        }

        return merchantDetails.representatives
    }

    public List<MerchantRepresentativeDTO> getRepresentatives(MerchantDetailsDTO merchantDetails) {
        if(!merchantDetails) {
            return Collections.emptyList()
        }

        return merchantDetails.representatives
    }

    public MerchantDetailsDTO getMerchantDetails(String nip) {
        MerchantKRSDataDTO merchantDetails
        Long userId = ((EServiceUserDetails)springSecurityService.principal).auwId

        try {
            merchantDetails = websWebServiceClient.searchMerchantData(nip, userId)
        } catch (Exception e) {
            log.error("Error during data fetch from bisnode", e)
            return null
        }

        if (isMerchantDetailsInvalid(merchantDetails)) {
            log.info(String.format("Client with NIP %s not found in Bisnode.", nip))
            return null
        }

        log.info(String.format("Client with NIP %s found in Bisnode", nip))
        return new MerchantDetailsDTO(merchantDetails)
    }

    private isMerchantDetailsInvalid(MerchantKRSDataDTO merchantDetails) {
        return !merchantDetails || !merchantDetails?.id
    }
}
