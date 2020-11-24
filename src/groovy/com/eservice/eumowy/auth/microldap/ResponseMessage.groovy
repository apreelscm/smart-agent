package com.eservice.eumowy.auth.microldap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ResponseMessage {

    private Long correlationID
    private String responseCode
    private String responseMsg
    private String version
    private List<User> user

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

    List<User> getUser() {
        return user
    }

    void setUser(List<User> users) {
        this.user = users
    }

    boolean isSuccess(){
        return responseCode == "0" && responseMsg == "AUTH"
    }
}
