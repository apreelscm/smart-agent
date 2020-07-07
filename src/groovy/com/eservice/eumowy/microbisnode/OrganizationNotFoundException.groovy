package com.eservice.eumowy.microbisnode

class OrganizationNotFoundException extends Exception {

    private String identifier

    OrganizationNotFoundException(String identifier) {
        super("organization not found for identifier " + identifier)
        this.identifier = identifier
    }

    String getIdentifier() {
        return identifier
    }
}
