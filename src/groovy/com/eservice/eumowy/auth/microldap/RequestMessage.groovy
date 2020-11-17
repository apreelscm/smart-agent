package com.eservice.eumowy.auth.microldap

class RequestMessage {

    List<User> user
    Long correlationID

    RequestMessage() {
    }

    RequestMessage(Long correlationID, List<User> users) {
        this.user = users
        this.correlationID = correlationID
    }

    List<User> getUser() {
        return user
    }

    void setUser(List<User> user) {
        this.user = user
    }

    Long getCorrelationID() {
        return correlationID
    }

    void setCorrelationID(Long correlationID) {
        this.correlationID = correlationID
    }

}
