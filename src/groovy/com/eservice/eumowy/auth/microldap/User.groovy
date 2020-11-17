package com.eservice.eumowy.auth.microldap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class User {

    private String userName
    private String password
    private String firstName
    private String lastName
    private String email
    private String system
    private String domain
    private String groups
    private String department

    String getUserName() {
        return userName
    }

    void setUserName(String userName) {
        this.userName = userName
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
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

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }

    String getSystem() {
        return system
    }

    void setSystem(String system) {
        this.system = system
    }

    String getDomain() {
        return domain
    }

    void setDomain(String domain) {
        this.domain = domain
    }

    String getGroups() {
        return groups
    }

    void setGroups(String groups) {
        this.groups = groups
    }

    String getDepartment() {
        return department
    }

    void setDepartment(String department) {
        this.department = department
    }
}
