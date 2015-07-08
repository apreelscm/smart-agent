package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.ActivityHelper
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.activity.CashbackValidator
import com.eservice.eumowy.validator.cbd.activity.DccValidator
import com.google.common.collect.Lists
import org.apache.commons.lang.StringUtils

public final class ProcessCBDValidator {
    private final Process process
    private final Client client
    private final List calc
    private final List<Validator> validators
    private List<String> errorCodes

    public ProcessCBDValidator(Process process, Client client, List calc) {
        this.process = process
        this.client = client
        this.calc = calc
        this.validators = getValidators()
    }

    private List<Validator> getValidators() {
        List<Validator> validators = Lists.newArrayList()

        if (!calc.isEmpty()) {
            validators.add(new CashbackValidator(process, client, calc))
            validators.add(new DccValidator(process, client, calc))
        }

        if (!ActivityHelper.isNewAgreement(process)) {
            validators.add(new MIDValidator(process, client))
            validators.add(new PkoBpValidator(process, client))
            validators.add(new ExchangeLeaseToCooperationValidator(process, client))
            validators.add(new PrepaidValidator(process, client))

            if (!calc.isEmpty()) {
                validators.add(new CashbackTerminalValidator(process, client, calc))
                validators.add(new DccTerminalValidator(process, client, calc))
            }
        }

        return validators
    }

    public boolean isValid() {
        return getErrorCodes().empty
    }

    public List<String> getErrorCodes() {
        if(errorCodes) return errorCodes

        errorCodes = Lists.newArrayList()

        for (Validator validator : validators) {
            if (!validator.isValid()) {
                errorCodes.add(validator.getErrorMessageCode())
            }
        }

        return errorCodes
    }
}
