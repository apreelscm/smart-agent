package com.eservice.eumowy.enums.options

import org.apache.commons.lang.NullArgumentException


public enum LegalForm implements RadioOption {
    PERSON(),
    ZOO_COMPANY(),
    STOCK_COMPANY(),
    PARTNERSHIP_COMPANY(),
    LIMITED_COMPANY(),
    LIMITED_STOCK_COMPANY(),
    OPEN_COMPANY(),
    PARTNERSHIP(),
    COOPERATIVE(),
    FOUNDATION(),
    HEALTHCARE_CENTER(),
    INSTITUTE(),
    CULTURAL_INSTITUTION(),
    BUDGETARY_UNIT(),
    EDUCATION_UNIT(),
    ECONOMIC_SELF_GOVERNMENT()

    public boolean isCompany() {
        return !(isPerson())
    }

    public boolean isPerson() {
        return EnumSet.of(PARTNERSHIP_COMPANY, PERSON).contains(this)
    }

    String getMessageCode(){
        return "legal.form." + this.name()
    }

    String getMessageCodeForBisnode(){
        return "legal.form.bisnode." + this.name()
    }

}
