package com.eservice.eumowy.data

import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import com.eservice.eumowy.Process.ProcessStatus
import org.apache.commons.lang.time.DateFormatUtils

class SalesmenReportData {
    String startDate
    String endDate

    List<SalesmanStatusesDTO> salesmenStatuses
    Map<ProcessStatus, Integer> salesmenStatusesTotal

    List<SalesmanAcceptedActivitiesDTO> salesmanAcceptedActivities
    List<String> allActivities
    Map<String, Integer> acceptedActivitiesTotal

    public void setReportDateSpan(Date startDate, Date endDate) {
        this.startDate = DateFormatUtils.format(startDate, "dd-MM-yyyy")
        this.endDate = DateFormatUtils.format(endDate, "dd-MM-yyyy")
    }

    public Integer processStatusesCount() {
        return ProcessStatus.values().size()
    }

    public Integer allActivitiesCount() {
        return allActivities.size()
    }

    public String getReportHeader() {
        return "Data raportu " + startDate + " - " + endDate
    }
}
