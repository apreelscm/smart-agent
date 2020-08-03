package com.eservice.eumowy.mocks

import com.eservice.eumowy.microbisnode.model.Address
import com.eservice.eumowy.microbisnode.model.BeneficialOwner
import com.eservice.eumowy.microbisnode.model.BeneficialOwnership
import com.eservice.eumowy.microbisnode.model.LegalFormDetails
import com.eservice.eumowy.microbisnode.model.Linkage
import com.eservice.eumowy.microbisnode.model.Location
import com.eservice.eumowy.microbisnode.model.Organization
import com.eservice.eumowy.microbisnode.model.OrganizationIdentificationNumberDetail
import com.eservice.eumowy.microbisnode.model.OrganizationName
import com.eservice.eumowy.microbisnode.model.Principal
import com.eservice.eumowy.microbisnode.model.PrincipalIdentificationNumberDetail
import com.eservice.eumowy.microbisnode.model.PrincipalName
import com.eservice.eumowy.microbisnode.model.PrincipalsAndManagement
import com.eservice.eumowy.microbisnode.model.RegisteredDetail
import com.eservice.eumowy.microbisnode.model.SubjectTypeDescription

class OrganizationBuilder {

    Organization organization

    OrganizationBuilder() {
        organization = new Organization(organizationName: new OrganizationName(organizationPrimaryName : ["Firma wymyslona"]),
                registeredDetail: new RegisteredDetail(organizationIdentificationNumberDetail :
                        [new OrganizationIdentificationNumberDetail(id: 2, organizationIdentificationNumber : "9591256060"),
                         new OrganizationIdentificationNumberDetail(id: 1, organizationIdentificationNumber : "414480347")],
                        legalFormDetails : new LegalFormDetails( legalFormText: "BV Normal Structure")),
                principalsAndManagement : new PrincipalsAndManagement(currentPrincipal: new ArrayList<Principal>()),
                linkage : new Linkage( beneficialOwnership: new BeneficialOwnership( beneficialOwners : new ArrayList<BeneficialOwner>())))
    }

    OrganizationBuilder withAddress(String postalCode, String city, String type, String street, String houseNr){
        organization.location = new Location(primaryAddress: [new Address(postalCode: postalCode, primaryTownName: city, streetAddressLine: [type + "|" + street + "|" + houseNr + "|"])])
        return this
    }

    OrganizationBuilder withRepresentative(String firstName, String lastName, String pesel, String position) {
        organization.principalsAndManagement.currentPrincipal.add(new Principal(principalName: new PrincipalName(title: "Pan", firstName: firstName, lastName: lastName),
                principalIdentificationNumberDetail : [new PrincipalIdentificationNumberDetail(typeText: "PESEL", principalIdentificationNumber : pesel)],
                currentManagementResponsibility: [position]))
        return this
    }

    OrganizationBuilder withBeneficiary(String firstName, String lastName, String pesel, Integer ownershipPercentage){
        organization.linkage.beneficialOwnership.beneficialOwners.add(new BeneficialOwner(primaryName: "Pan|" + firstName + "|" + lastName,
                nationality: "polskie", personId: pesel, subjectTypeDescription : new SubjectTypeDescription(code:119), beneficialOwnershipPercentage : ownershipPercentage))
        return this
    }

    Organization build(){
        return organization
    }

}
