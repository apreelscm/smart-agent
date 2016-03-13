package com.eservice.eumowy.salesmanreport

class ReportData {
    private final String dateFrom
    private final String dateTo
    private final List<SalesmanRow> rows

    ReportData(Date dateFrom, Date dateTo, List<SalesmanRow> rows) {
        this.dateFrom = dateFrom.format("dd-MM-yyyy")
        this.dateTo = dateTo.format("dd-MM-yyyy")
        this.rows = rows
    }

    public String getReportHeader() {
        return String.format("Data raportu %s - %s", dateFrom, dateTo)
    }

    List<SalesmanRow> getRows() {
        return rows
    }
}
