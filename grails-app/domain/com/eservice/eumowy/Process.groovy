package com.eservice.eumowy

import groovy.transform.ToString

@ToString(includeNames = true,ignoreNulls = true)
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
        id(unique:true,blank:false)
    }

    static mapping = {
        table name: "process", schema: "CBD_UMOWY"
    //    id column: 'ID', generator:'assigned', type: 'integer'
        autoTimestamp true
        version true
    }

    enum ProcessStatus {
        NEW("Nowy"),
        ACCEPTED("Zaakceptowany");

        private final String text;

        private ProcessStatus(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
