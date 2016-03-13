package com.eservice.eumowy.salesmanreport

import com.eservice.eumowy.Process

class SalesmanRow {
    private final String ph
    private final List<ProcessDetails> details

    public SalesmanRow(String ph, List<ProcessDetails> details) {
        this.ph = ph
        this.details = details
    }

    String getPh() {
        return ph
    }

    List<ProcessDetails> getDetails() {
        return details
    }

    public int getDetailsCount() {
        return details.size()
    }
}
