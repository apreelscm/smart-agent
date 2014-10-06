package com.eservice.eumowy


final class ActivitiesRequirements {
    private final Process process
    private final List calc

    public ActivitiesRequirements(Process process, List calc) {
        this.process = process
        this.calc = calc
    }

    public boolean isInvalid() {
        return dccActivityRequired || cashBackActivityRequired
    }

    public String getErrorMessage() {
        if(dccActivityRequired) return "dcc.activity.required"

        if(cashBackActivityRequired) return "cashback.activity.required"

        return null
    }

    private boolean isDccActivityRequired() {
        return hasCalcProperty("S_DCC", "TAK") && !ActivityHelper.hasAtLeastOne(process, ["dodanieDcc", "zmianaWarunkowDcc"])
    }

    private boolean isCashBackActivityRequired() {
        return hasCalcProperty("CASHBACK_A", "TAK") && !ActivityHelper.contains(process, "dodanieCashBack")
    }

    private boolean hasCalcProperty(String key, String value){
        return calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }

}
