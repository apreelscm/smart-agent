package com.eservice.eumowy.auth.microldap

class AuthRequest {

    RequestMessage message

    AuthRequest() {
    }

    AuthRequest(Long correlationId, User user) {
        message = new RequestMessage(correlationId, Collections.singletonList(user))
    }

    RequestMessage getMessage() {
        return message
    }

    void setMessage(RequestMessage message) {
        this.message = message
    }

}
