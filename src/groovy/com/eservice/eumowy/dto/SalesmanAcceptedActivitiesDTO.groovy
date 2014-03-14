package com.eservice.eumowy.dto

class SalesmanAcceptedActivitiesDTO {
    String phNumber
    String phFirstName
    String phSurname
    Map<String, Integer> activitiesCount = [:]

    public SalesmanAcceptedActivitiesDTO(Object processDetails) {
        phNumber = processDetails[0]
        phFirstName = processDetails[1]
        phSurname = processDetails[2]
        activitiesCount.put(processDetails[3], processDetails[4])
    }

    public addActivity(Object processDetails) {
        activitiesCount.put(processDetails[3], processDetails[4])
    }
}
