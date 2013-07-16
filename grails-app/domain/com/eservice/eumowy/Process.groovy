package com.eservice.eumowy

class Process {

    Date dateCreated

    Date lastUpdated

    ProcessStatus status = ProcessStatus.NEW


    String phNumber

    String phFirstName

    String phSurname


    String clientNip

    String clientName

    String calcNumber


    String saleSection // TODO skad ?

    // TODO kolekcja czynnosci

    // TODO guid dokument, co to jest ?

    static constraints = {

    }

    enum ProcessStatus {
        NEW,
        ACCEPTED
    }
}
