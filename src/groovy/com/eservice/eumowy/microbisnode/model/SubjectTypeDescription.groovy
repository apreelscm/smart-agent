package com.eservice.eumowy.microbisnode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SubjectTypeDescription implements IgnoreUnknownProperties {

    Integer code

    Integer getCode() {
        return code
    }

    void setCode(Integer code) {
        this.code = code
    }

}
