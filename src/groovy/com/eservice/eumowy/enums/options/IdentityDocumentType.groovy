package com.eservice.eumowy.enums.options;

public enum IdentityDocumentType {
    IDENTITY_CARD("identity.card.label"), PASSPORT("passport.label");

    String messageCode;

    IdentityDocumentType(String messageCode) {
        this.messageCode = messageCode;
    }
}
