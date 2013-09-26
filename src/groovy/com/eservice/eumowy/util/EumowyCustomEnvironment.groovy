package com.eservice.eumowy.util

/**
 * defines other environments than standard grails.util.Environment
 *
 * User: Dominik Walczak
 * Date: 24.09.13 Time: 22:15
 *
 */
public enum EumowyCustomEnvironment {

    /** The development mock environment */
    MOCK,
    /** The UAT environment */
    UAT;

    private String name;

    EumowyCustomEnvironment() {
        initialize();
    }

    private void initialize() {
        name = toString().toLowerCase();
    }

    public String getName() {
        return name;
    }

}
