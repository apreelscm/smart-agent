package com.eservice.eumowy

import groovy.transform.ToString

@ToString(includeNames = true, ignoreNulls = true)
class Activity implements Serializable {

    String code
    Integer numerPozycji

    List<ActivitySignatures> selectedActivitySignatures

    static transients = ['selectedActivitySignatures']

    static hasMany = [
        activitySignatures: ActivitySignatures
    ]

    static constraints = {
        code(unique: true, blank: false)
        numerPozycji(nullable: false)
        activitySignatures()
    }

    static mapping = {
        sort numerPozycji: "asc"
        table name: "ACTIVITY", schema: DomainConsts.SHEMA_NAME
        id generator: 'sequence', params: [sequence: DomainConsts.SHEMA_NAME + '.ACTIVITY_SEQ']
    }

    public String getUserFriendlyCode() {
        return code.capitalize().split("(?<=[a-z])(?=[A-Z])").join(" ")
    }
}
