package com.eservice.eumowy.enums.options

import org.apache.commons.lang.NullArgumentException


public enum LegalForm implements RadioOption {
    STOCK_COMPANY("legal.form.stock"), ZOO_COMPANY("legal.form.zoo"), PARTNERSHIP_COMPANY("legal.form.partnership"),
    PERSON("legal.form.person"), LIMITED_COMPANY("legal.form.limited"), OPEN_COMPANY("legal.form.open")

    String messageCode

    public LegalForm(String messageCode) {
        this.messageCode = messageCode
    }

    public boolean isCompany() {
        return !(isPerson())
    }

    public boolean isPerson() {
        return PARTNERSHIP_COMPANY.equals(this)|| PERSON.equals(this)
    }

}
