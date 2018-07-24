package com.eservice.eumowy.serialidreport

class RepresentativeInfo {
    private final String name
    private final String documentNumber
    private final Date issueDate
    private final Date expirationDate

    RepresentativeInfo(String name, String documentNumber, Date issueDate, Date expirationDate) {
        this.name = name
        this.documentNumber = documentNumber
        this.issueDate = issueDate
        this.expirationDate = expirationDate
    }

    String getName() {
        return name
    }

    String getDocumentNumber() {
        return documentNumber
    }

    Date getIssueDate() {
        return issueDate
    }

    Date getExpirationDate() {
        return expirationDate
    }
}
