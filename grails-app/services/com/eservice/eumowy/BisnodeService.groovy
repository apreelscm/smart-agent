package com.eservice.eumowy

import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.webs.dto.MerchantKRSDataDTO
import com.eservice.webs.wsclient.bisnode.BisnodeWebServiceClient
import org.apache.commons.lang.exception.ExceptionUtils

class BisnodeService {
    BisnodeWebServiceClient bisnodeWebServiceClient

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
        return !merchantDetails && !merchantDetails.id
    }
}
