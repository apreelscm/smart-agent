package com.eservice.eumowy

class ActivitySignatures implements Serializable {

    Signature signature;

    boolean mandatory // not shown on any of list

    Integer numberOfList = 0;

    static belongsTo = [activity:Activity]

    static constraints = {
    }

    static mapping = {
        table name: "activitysignatures", schema: "CBD_UMOWY"
    }

}
