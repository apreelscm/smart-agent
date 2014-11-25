package com.eservice.eumowy.enums.options


public enum AcceptorVerification implements RadioOption{
    PESEL("pesel.label"), BIRTH_DATE("birth.date.label"), COUNTRY_CODE("country.label")

    String messageCode

    public AcceptorVerification(String messageCode) {
        this.messageCode = messageCode
    }
}