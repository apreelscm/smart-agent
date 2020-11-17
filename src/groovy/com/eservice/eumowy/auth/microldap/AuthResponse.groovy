package com.eservice.eumowy.auth.microldap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthResponse {

    private ResponseMessage message

    AuthResponse() {
    }

    Long getCorrelationID() {
        return message?.getCorrelationID()
    }

    User getUser() {
        return message?.getUser()?.size() > 0 ? message.getUser().get(0) : null
    }

    boolean isSuccess(){
        return message?.isSuccess()
    }

    String getResponseCode() {
        return message?.getResponseCode()
    }

    String getResponseMsg() {
        return message?.getResponseMsg()
    }

    ResponseMessage getMessage() {
        return message
    }

    void setMessage(ResponseMessage message) {
        this.message = message
    }
}
