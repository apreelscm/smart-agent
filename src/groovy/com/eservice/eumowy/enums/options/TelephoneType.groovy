package com.eservice.eumowy.enums.options

enum TelephoneType {

    LANDLINE("panel.landline.phone.number"),
    MOBILE("panel.mobile.phone.number")

    String messageCode

    TelephoneType(String messageCode) {
        this.messageCode = messageCode
    }

}