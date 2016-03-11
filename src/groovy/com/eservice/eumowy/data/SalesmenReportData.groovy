package com.eservice.eumowy.data

import com.eservice.eumowy.Activity
import com.eservice.eumowy.Process
import com.eservice.eumowy.Process.ProcessStatus
import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import org.apache.commons.lang.time.DateFormatUtils

class SalesmenReportData {
    private final String dateFrom
    private final String dateTo
    private final List<Process> processes

    SalesmenReportData(String dateFrom, String dateTo, List<Process> processes) {
        this.dateFrom = dateFrom
        this.dateTo = dateTo
        this.processes = processes
    }

    public String getReportHeader() {
        return String.format("Data raportu %s - %s", dateFrom, dateTo)
    }

    public List<String> qwe() {
        return processes.collect { it.status.toString() }.unique()
    }


}
