package com.eservice.eumowy.enums.options;

public enum AcceptorLocation implements RadioOption {
    COUNTRY("acceptor.location.country"), ABROAD("acceptor.location.abroad")

    String messageCode

    public AcceptorLocation(String messageCode) {
        this.messageCode = messageCode
    }
}
