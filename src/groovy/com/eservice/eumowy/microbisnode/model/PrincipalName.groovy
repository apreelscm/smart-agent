package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PrincipalName implements IgnoreUnknownProperties {

    String title
    String firstName
    String lastName

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    String getFirstName() {
        return firstName
    }

    void setFirstName(String firstName) {
        this.firstName = firstName
    }

    String getLastName() {
        return lastName
    }

    void setLastName(String lastName) {
        this.lastName = lastName
    }

}
