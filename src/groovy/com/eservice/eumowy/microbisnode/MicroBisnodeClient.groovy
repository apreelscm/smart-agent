package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.microbisnode.model.Organization

interface MicroBisnodeClient {

    Organization getOrganizationByIdentifier(String identifierNumber) throws OrganizationNotFoundException, BisnodeConnectionException, BisnodeMappingException

}