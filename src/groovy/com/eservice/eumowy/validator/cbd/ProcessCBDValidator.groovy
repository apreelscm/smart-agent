package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.Process
import com.eservice.eumowy.validator.cbd.activity.CashbackValidator
import com.eservice.eumowy.validator.cbd.activity.DccValidator
import org.apache.commons.lang.StringUtils

final class ProcessCBDValidator {
    private final Process process
    private final List calc
    private final Validator chain
    private String errorCode

    public ProcessCBDValidator(Process process, List calc) {
        this.process = process
        this.calc = calc
        this.chain = createValidatorsChain()
    }

    private Validator createValidatorsChain() {
        return new DccValidator(process, calc).
            addNext(new CashbackValidator(process, calc))
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
