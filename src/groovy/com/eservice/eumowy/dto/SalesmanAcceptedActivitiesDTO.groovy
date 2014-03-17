package com.eservice.eumowy.dto

class SalesmanAcceptedActivitiesDTO {
    String phNumber
    String phFirstName
    String phSurname
    Map<String, Integer> activitiesCount = [:]

    public SalesmanAcceptedActivitiesDTO(Object processDetails, List<String> possibleActivities) {
        phNumber = processDetails[0]
        phFirstName = processDetails[1]
        phSurname = processDetails[2]

        possibleActivities.each {
            activitiesCount.put(it, 0)
        }

        addActivity(processDetails)
    }

    public addActivity(Object processDetails) {
        if(activitiesCount.containsKey(processDetails[3])) {
            activitiesCount[processDetails[3]] = processDetails[4]
        }
    }
}
