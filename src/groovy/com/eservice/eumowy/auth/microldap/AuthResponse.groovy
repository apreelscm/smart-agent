package com.eservice.eumowy.auth.microldap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthResponse {

    private Long correlationID
    private String responseCode
    private String responseMsg
    private String version
    private List<User> users

    Long getCorrelationID() {
        return correlationID
    }

    void setCorrelationID(Long correlationID) {
        this.correlationID = correlationID
    }

    String getResponseCode() {
        return responseCode
    }

    void setResponseCode(String responseCode) {
        this.responseCode = responseCode
    }

    String getResponseMsg() {
        return responseMsg
    }

    void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg
    }

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
    }

    List<User> getUsers() {
        return users
    }

    User getUser() {
        return users?.size() > 0 ? users.get(0) : null
    }

    void setUsers(List<User> users) {
        this.users = users
    }

    boolean isSuccess(){
        return responseCode = "0"
    }
}
