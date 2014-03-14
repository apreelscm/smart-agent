package com.eservice.eumowy.dto

import com.eservice.eumowy.Process.ProcessStatus

class SalesmanStatusesDTO {
    String phNumber
    String phFirstName
    String phSurname
    Map<ProcessStatus, Integer> statusesCount = [:]

    public SalesmanStatusesDTO(Object processDetails) {
        phNumber = processDetails[0]
        phFirstName = processDetails[1]
        phSurname = processDetails[2]
        for(status in ProcessStatus.values()) {
            statusesCount.put(status, 0)
        }
        statusesCount[processDetails[3]] = processDetails[4]
    }

    public void addStatus(Object processDetails) {
        statusesCount[processDetails[3]] = processDetails[4]
    }
}
