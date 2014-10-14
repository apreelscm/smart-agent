package com.eservice.eumowy.enums


public enum Disposition {
    PHONE("disposition.phone"), EMAIL("disposition.email"), PAPER("disposition.paper"), FAX("disposition.fax"),
    PERSONALLY("disposition.personally");

    String messageCode;

    Disposition(String messageCode) {
        this.messageCode = messageCode;
    }
}
