package com.eservice.eumowy.enums.options

import org.apache.commons.lang.NullArgumentException


public enum LegalForm implements RadioOption {
    PERSON("legal.form.person"),
    ZOO_COMPANY("legal.form.zoo_company"),
    STOCK_COMPANY("legal.form.stock_company"),
    PARTNERSHIP_COMPANY("legal.form.partnership_company"),
    LIMITED_COMPANY("legal.form.limited_company"),
    LIMITED_STOCK_COMPANY("legal.form.limited_stock_company"),
    OPEN_COMPANY("legal.form.open_company"),
    PARTNERSHIP("legal.form.partnership"),
    COOPERATIVE("legal.form.cooperative"),
    FOUNDATION("legal.form.foundation"),
    HEALTHCARE_CENTER("legal.form.healthcare_center"),
    INSTITUTE("legal.form.institute"),
    CULTURAL_INSTITUTION("legal.form.cultural_institution"),
    BUDGETARY_UNIT("legal.form.budgetary_unit"),
    EDUCATION_UNIT("legal.form.education_unit"),
    ECONOMIC_SELF_GOVERNMENT("legal.form.economic_self_government")

    String messageCode

    public LegalForm(String messageCode) {
        this.messageCode = messageCode
    }

    public boolean isCompany() {
        return !(isPerson())
    }

    public boolean isPerson() {
        return EnumSet.of(PARTNERSHIP_COMPANY, PERSON).contains(this)
    }

}
