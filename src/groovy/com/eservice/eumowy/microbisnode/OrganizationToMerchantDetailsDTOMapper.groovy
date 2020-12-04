package com.eservice.eumowy.microbisnode

import com.eservice.eumowy.dto.MerchantBeneficiaryDTO
import com.eservice.eumowy.dto.MerchantDetailsDTO
import com.eservice.eumowy.dto.MerchantRepresentativeDTO
import com.eservice.eumowy.enums.options.LegalForm
import com.eservice.eumowy.microbisnode.model.BeneficialOwner
import com.eservice.eumowy.microbisnode.model.BeneficialOwnership
import com.eservice.eumowy.microbisnode.model.Organization
import com.eservice.eumowy.microbisnode.model.Principal
import com.eservice.eumowy.microbisnode.model.PrincipalsAndManagement
import org.springframework.context.MessageSource

class OrganizationToMerchantDetailsDTOMapper {

    private MessageSource messageSource
    def msgParams = [].toArray()

    OrganizationToMerchantDetailsDTOMapper(MessageSource messageSource) {
        this.messageSource = messageSource
    }

    MerchantDetailsDTO map(Organization organization){
        MerchantDetailsDTO merchantDetailsDTO = new MerchantDetailsDTO()

        if (organization.organizationName.organizationPrimaryName?.size() > 0){
            merchantDetailsDTO.akceptantNazwaOficjalna = organization.organizationName.organizationPrimaryName.get(0)
        }

        merchantDetailsDTO.nip = organization.registeredDetail.nipIdentifier?.organizationIdentificationNumber
        merchantDetailsDTO.akceptantRegon = organization.registeredDetail.regonIdentifier?.organizationIdentificationNumber
        merchantDetailsDTO.formaPrawna = mapLegalForm(organization.registeredDetail.legalFormDetails?.legalFormText)

        if (organization.location.firstPrimaryAddress.streetAddressLine?.size() > 0){
            String[] streetAddressLine = organization.location.firstPrimaryAddress.streetAddressLine.get(0).split("\\|")
            merchantDetailsDTO.akceptantUlicaTytul = mapStreetType(streetAddressLine[0])
            merchantDetailsDTO.akceptantUlica = streetAddressLine[1]
            merchantDetailsDTO.akceptantNrDomu = streetAddressLine[2]
            if (streetAddressLine.size() > 3){
                merchantDetailsDTO.akceptantNrMieszkania = streetAddressLine[3]
            }
        }

        merchantDetailsDTO.akceptantKodPocztowy = organization.location.firstPrimaryAddress.postalCode
        merchantDetailsDTO.akceptantMiasto = organization.location.firstPrimaryAddress.primaryTownName

        merchantDetailsDTO.representatives = createRepresentativesList(organization.principalsAndManagement)

        if (organization.isSOHO()){
            merchantDetailsDTO.beneficiaries = createBeneficiariesListForSOHO(organization.principalsAndManagement)
        } else if (organization.linkage?.beneficialOwnership) {
            merchantDetailsDTO.beneficiaries = createBeneficiariesList(organization.linkage.beneficialOwnership)
        }

        return merchantDetailsDTO

    }

    private String mapLegalForm(String legalFormValue){
        if (legalFormValue){
            for (LegalForm form : LegalForm.values()){
                def existingFormDescription = messageSource.getMessage(form.getMessageCode(), msgParams, Locale.default)
                if (existingFormDescription == legalFormValue){
                    return form
                }
            }
        }
        return null
    }

    private mapStreetType(String streetType){
        if (streetType?.contains(".")){
            streetType = streetType.replaceAll(".", "")
        }
        return streetType.toUpperCase()
    }

    private List<MerchantRepresentativeDTO> createRepresentativesList(PrincipalsAndManagement principalsAndManagement) {
        List<MerchantRepresentativeDTO> representatives = new ArrayList<>()
        for(Principal principal: principalsAndManagement?.currentPrincipal) {
            MerchantRepresentativeDTO representative = new MerchantRepresentativeDTO()
            representative.firstName = principal.principalName.firstName
            representative.lastName = principal.principalName.lastName
            representative.title = principal.principalName.title
            representative.position = principal.currentManagementResponsibility?.get(0)
            representative.pesel = principal.peselIdentifier?.principalIdentificationNumber
            representative.nationality = principal.nationality

            if (representative.isValid()) {
                representatives.add(representative)
            }
        }
        return representatives
    }

    private List<MerchantBeneficiaryDTO> createBeneficiariesList(BeneficialOwnership beneficialOwnership){
        List<MerchantBeneficiaryDTO> beneficiaries = new ArrayList<>()
        List<BeneficialOwner> filteredBeneficiaries = beneficialOwnership?.beneficialOwners?.findAll{ BeneficialOwner it -> it.isSignificant() }
        for (BeneficialOwner beneficialOwner : filteredBeneficiaries){
            MerchantBeneficiaryDTO beneficiary = new MerchantBeneficiaryDTO()
            if (beneficialOwner.primaryName && beneficialOwner.primaryName.contains("|")){
                String[] primaryNameArray = beneficialOwner.primaryName?.split("\\|")
                beneficiary.title = primaryNameArray[0]
                beneficiary.firstName = primaryNameArray[1]
                beneficiary.lastName = primaryNameArray[2]
            }
            beneficiary.nationality = beneficialOwner.nationality
            beneficiary.pesel = beneficialOwner.personId
            beneficiary.ownershipPercentage = beneficialOwner.beneficialOwnershipPercentage.toInteger()
            if (beneficiary.isValid()){
                beneficiaries.add(beneficiary)
            }
        }
        return beneficiaries
    }

    private List<MerchantBeneficiaryDTO> createBeneficiariesListForSOHO(PrincipalsAndManagement principalsAndManagement) {
        List<MerchantBeneficiaryDTO> beneficiaries = new ArrayList<>()
        Principal owner = principalsAndManagement?.currentPrincipal?.find{ Principal it -> it.isOwner() }
        if (owner){
            MerchantBeneficiaryDTO beneficiary = new MerchantBeneficiaryDTO()
            beneficiary.title = owner.principalName?.title
            beneficiary.firstName = owner.principalName?.firstName
            beneficiary.lastName = owner.principalName?.lastName
            beneficiary.nationality = owner.nationality
            beneficiary.pesel = owner.peselIdentifier?.principalIdentificationNumber
            if (beneficiary.isValid()){
                beneficiaries.add(beneficiary)
            }
        }
        return beneficiaries
    }
}
