package com.eservice.eumowy.validator


import com.eservice.eumowy.command.ProcessCommand
import org.springframework.validation.Errors

import static com.eservice.eumowy.ActivityHelper.isNewAgreement

class ConsentsValidator {
    public static Closure<Boolean> validate = { Boolean value, ProcessCommand cmd, Errors errors ->
        // Consents are required only in "Nowa Umowa" activity flow
        // if it's not - they are always valid, even if empty
        if (!cmd.hasNewUmowa || alreadyHasErrors(errors)) {
            return true
        }

        List<Boolean> consents = [
                cmd.consentsChannelClientPortal,
                cmd.consentsChannelEmail,
                cmd.consentsChannelSMS,
                cmd.consentsChannelPhone,
                cmd.consentsChannelNone,
        ]
        if (consents.every { it != true }) {
            errors.rejectValue("consentsChannelAll", "consents.at_least_one_channel.required.error")
            return false
        } else if (cmd.consentsChannelNone == true && consents.count { it == true } > 1) {
            errors.rejectValue("consentsChannelAll", "consents.agree_to_disagree.error")
            return false
        }
        return true
    }

    private static boolean alreadyHasErrors(Errors errors) {
        return errors.hasFieldErrors("consentsChannelAll")
    }
}
