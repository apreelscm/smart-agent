package com.eservice.eumowy.enums.options


public enum AcceptorDetail implements RadioOption{
    PESEL("pesel.label"), BIRTH_DATE("birth.date.label"), COUNTRY_CODE("country.label")

    String messageCode

    public AcceptorDetail(String messageCode) {
        this.messageCode = messageCode
    }
}