package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class LegalFormDetails implements IgnoreUnknownProperties {

    private String code
    private String legalFormText

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    String getLegalFormText() {
        return legalFormText
    }

    void setLegalFormText(String legalFormText) {
        this.legalFormText = legalFormText
    }

}
