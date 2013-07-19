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

    List<DocumentFile> documents // TODO skad ?

    // TODO kolekcja czynnosci

    // TODO guid dokument, co to jest ?

    String uid

    static hasMany = [documents:DocumentFile]

    static constraints = {
        id(unique:true,blank:false)
        uid(unique: true)
    }


    static mapping = {
        table name: "process", schema: "CBD_UMOWY"
        autoTimestamp true
        version true
    }

    def beforeValidate() {
        if (uid == null) {
            uid = UUID.randomUUID().toString() // works
        }
    }

    enum ProcessStatus {
        NEW("Nowy"),
        REJECTED("Odrzucony"),
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
