package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.activity.CashbackValidator
import com.eservice.eumowy.validator.cbd.activity.DccValidator
import org.apache.commons.lang.StringUtils

public final class ProcessCBDValidator {
    private final Process process
    private final Client client
    private final List calc
    private final Validator chain
    private String errorCode

    public ProcessCBDValidator(Process process, Client client, List calc) {
        this.process = process
        this.client = client
        this.calc = calc
        this.chain = createValidatorsChain()
    }

    private Validator createValidatorsChain() {
        return new DccValidator(process, calc)
            .addNext(new CashbackValidator(process, calc)
            .addNext(new MIDValidator(process)
            .addNext(new com.eservice.eumowy.validator.cbd.CashbackValidator(process, calc)
            .addNext(new CashbackTerminalValidator(process, calc)
            .addNext(new DccTerminalValidator(process, calc))))))
    }

    public boolean isValid() {
        return StringUtils.isEmpty(getErrorCode())
    }

    public String getErrorCode() {
        if(errorCode) return errorCode

        errorCode = chain.getValidationErrorCode()

        return errorCode
    }
}
