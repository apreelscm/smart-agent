package com.eservice.eumowy
import groovy.transform.ToString

@ToString(includeNames = true,ignoreNulls = true)
class Process implements Serializable {

    Date dateCreated
    Date lastUpdated

    ProcessStatus status = ProcessStatus.NEW

    String phNumber
    String phFirstName
    String phSurname
    String calcNumber
    String saleSection // TODO skad ?

    Client client;

    List<DocumentFile> documents
    List<AttachmentFile> attachments
    List<Activity> activities
    List<Signature> signatures
    List<Panel> panels
    List<Subscription> subscriptions

    // TODO kolekcja czynnosci


    String getStringId() {
        return String.format('%06d',this.id)
    }

    static transients = ['stringId']

    static hasMany = [
            documents:DocumentFile,
            attachments:AttachmentFile,
            activities:Activity,
            signatures:Signature,
            subscriptions:Subscription
    ]

    static constraints = {
    }

    static mapping = {
        table name: "PROCESS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_SEQ']
        sort id: "desc"
        client cascade: 'save-update'
    }


    enum ProcessStatus {
        NEW("Nowy"),
        REJECTED("Odrzucony"),
        WAIT_FOR_SUBSRIPTION("Oczekiwanie na podpis"),
        WAITING("Oczekujący"),
        EDIT("Edycja"),
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
