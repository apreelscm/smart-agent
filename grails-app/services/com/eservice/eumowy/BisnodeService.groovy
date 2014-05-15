package com.eservice.eumowy

import com.eservice.eumowy.dto.BisnodeMerchantDetailsDTO
import com.eservice.eumowy.singleton.BisnodeClientSingleton
import com.eservice.webs.dto.MerchantKRSDataDTO
import com.eservice.webs.wsclient.bisnode.BisnodeWebServiceClient
import org.apache.commons.lang.exception.ExceptionUtils
import org.h2.util.StringUtils

class BisnodeService {

    public BisnodeMerchantDetailsDTO getMerchantDetails(String nip) {
        BisnodeWebServiceClient webServiceClient = BisnodeClientSingleton.getInstance()
        MerchantKRSDataDTO merchantDetails

        try {
            merchantDetails = webServiceClient.searchMerchantData(nip)
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e))
            return null
        }

        if (StringUtils.isNullOrEmpty(merchantDetails?.getId())) {
            log.info(String.format("Client with NIP %s not found in Bisnode.", nip))
            return null
        }

        log.info(String.format("Client with NIP %s found in Bisnode", nip))
        return new BisnodeMerchantDetailsDTO(merchantDetails)
    }
}
