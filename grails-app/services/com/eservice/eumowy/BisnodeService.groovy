package com.eservice.eumowy

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.webs.dto.MerchantKRSDataDTO
import com.eservice.webs.wsclient.bisnode.BisnodeWebServiceClient
import org.apache.commons.lang.exception.ExceptionUtils

class BisnodeService {
    BisnodeWebServiceClient bisnodeWebServiceClient

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

        try {
            merchantDetails = bisnodeWebServiceClient.searchMerchantData(nip)
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e))
            return null
        }

        if (isMerchantDetailsValid(merchantDetails)) {
            log.info(String.format("Client with NIP %s not found in Bisnode.", nip))
            return null
        }

        log.info(String.format("Client with NIP %s found in Bisnode", nip))
        return new MerchantDetailsDTO(merchantDetails)
    }

    private isMerchantDetailsValid(MerchantKRSDataDTO merchantDetails) {
        return !merchantDetails && !merchantDetails?.id
    }
}
