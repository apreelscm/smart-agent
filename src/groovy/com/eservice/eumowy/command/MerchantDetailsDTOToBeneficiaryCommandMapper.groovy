package com.eservice.eumowy.command

import com.eservice.eumowy.dto.MerchantBeneficiaryDTO
import com.eservice.eumowy.enums.options.AcceptorVerification

class MerchantDetailsDTOToBeneficiaryCommandMapper {

    static map(List<MerchantBeneficiaryDTO> sourceBeneficiaries, List<BeneficiaryCommand> targetBeneficiaries){
        if (targetBeneficiaries.size() == 0 && sourceBeneficiaries?.size() > 0){
            sourceBeneficiaries.each {it ->
                BeneficiaryCommand beneficiaryCommand = new BeneficiaryCommand()
                if (it.pesel){
                    beneficiaryCommand.pesel = it.pesel
                    beneficiaryCommand.verification = AcceptorVerification.PESEL
                }
                beneficiaryCommand.citizenship = it.nationality
                beneficiaryCommand.name = it.firstName
                beneficiaryCommand.surname = it.lastName
                beneficiaryCommand.votesPercentage = it.ownershipPercentage
                beneficiaryCommand.overQuarterOfVotes = it.ownershipPercentage >= 25
                targetBeneficiaries.add(beneficiaryCommand)
            }
        }
    }
}
