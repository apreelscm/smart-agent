package com.eservice.eumowy.salesmanreport

import com.eservice.eumowy.Activity
import com.eservice.eumowy.Process
import com.eservice.eumowy.Process.ProcessStatus
import com.eservice.eumowy.dto.SalesmanAcceptedActivitiesDTO
import com.eservice.eumowy.dto.SalesmanStatusesDTO
import org.apache.commons.lang.time.DateFormatUtils

class ReportData {
    private final String dateFrom
    private final String dateTo
    private final List<Row> rows

    ReportData(Date dateFrom, Date dateTo, List<Row> rows) {
        this.dateFrom = dateFrom.format("dd-MM-yyyy")
        this.dateTo = dateTo.format("dd-MM-yyyy")
        this.rows = rows
    }

    public String getReportHeader() {
        return String.format("Data raportu %s - %s", dateFrom, dateTo)
    }

    List<Row> getRows() {
        return rows
    }
}
