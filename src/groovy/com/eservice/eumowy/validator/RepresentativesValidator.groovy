package com.eservice.eumowy.validator

import com.eservice.eumowy.MobilePhoneNumber
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import org.springframework.validation.Errors

import static com.eservice.eumowy.ActivityHelper.DODATKOWY_PUNKT
import static com.eservice.eumowy.ActivityHelper.NOWA_UMOWA
import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY
import static com.eservice.eumowy.ActivityHelper.WYMIANA_UMOWY_PLATNICZEJ
import static com.eservice.eumowy.ActivityHelper.isNewAgreement


class RepresentativesValidator {

    public static Set<String> ACTIVITIES_THAT_REQUIRES_AT_LEAST_ONE_REPRESENTATIVE_TO_SIGN_CONTRACT =
            [NOWA_UMOWA, DODATKOWY_PUNKT, WYMIANA_UMOWY_NAJMU_NA_UMOWE_WSPOLPRACY, WYMIANA_UMOWY_PLATNICZEJ]

    public static def validate = { List<RepresentativeCommand> value, ProcessCommand cmd, Errors errors ->
        boolean hasErrors = false

        value.each { representativeCommand ->
            if (representativeCommand.name && representativeCommand.surname) {
                representativeCommand.processCommand = cmd
                representativeCommand.validate()

                if (representativeCommand.hasErrors()) {
                    errors.reject("representatives.has.errors")
                    hasErrors = true
                }
            }
        }

        if (value.size() > 0) {
            RepresentativeCommand firstRepresentative = value[0]

            if (!(firstRepresentative.name && firstRepresentative.surname)) {
                errors.reject("representatives.atleast.one.required")
                hasErrors = true
            }
        }

        boolean hasAtLeastOneRepresentativeSigningContract = value.any { it.hasSignedContract == Boolean.TRUE }
        boolean hasAtLeastOneRepresentativeWithChangedData = value.any { it.isCBDDataChangedManually == Boolean.TRUE }

        boolean isSigningContractByAtLeastOneRepresentativeRequired = cmd.hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract || hasAtLeastOneRepresentativeWithChangedData

        if (isSigningContractByAtLeastOneRepresentativeRequired && !hasAtLeastOneRepresentativeSigningContract) {
            errors.reject("representatives.atleast.one.signingContract.required")
            hasErrors = true
        }

        if (cmd.process != null && hasDuplicatedPhoneNumbers(value, cmd.process.phMobilePhone)) {
            errors.reject("representatives.mobilePhoneNumbers.duplicates")
            hasErrors = true
        }

        return !hasErrors
    }

    private static boolean hasDuplicatedPhoneNumbers(List<RepresentativeCommand> reps, String phMobilePhone) {
        List<RepresentativeCommand> repsSigningDocs = reps.findAll { Boolean.TRUE.equals(it.hasSignedContract) }
        Set<MobilePhoneNumber> phones = new HashSet<>()
        phones.add(MobilePhoneNumber.of(phMobilePhone))
        repsSigningDocs.each { phones.add(MobilePhoneNumber.of(it.mobilePhone)) }

        return phones.size() < repsSigningDocs.size() + 1 // include ph phone too
    }
}
