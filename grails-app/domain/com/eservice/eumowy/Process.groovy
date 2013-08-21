package com.eservice.eumowy
import groovy.transform.ToString
import org.apache.commons.logging.LogFactory

@ToString(includeNames = true,ignoreNulls = true)
class Process implements Serializable {

    private static final auditLogger = LogFactory.getLog("audit");

    Date dateCreated
    Date lastUpdated

    ProcessStatus status;

    Integer phNumber
    String phFirstName
    String phSurname
    String calcNumber
    String saleSection // TODO skad ?

    boolean observed = false;

    Client client;

    List<DocumentFile> documents
    List<AttachmentFile> attachments
    List<Activity> activities
    List<Signature> signatures
    List<Panel> panels
    List<Subscription> subscriptions
    List<ProcessData> data


    String getStringId() {
        return String.format('%06d',this.id)
    }
    
    String getStringPhNumber(){
        return Integer.toString(this.phNumber);
    }

    static transients = ['stringId']

    static hasMany = [
            documents:DocumentFile,
            attachments:AttachmentFile,
            activities:Activity,
            signatures:Signature,
            subscriptions:Subscription,
            data : ProcessData
    ]

    static constraints = {}

    static mapping = {
        table name: "PROCESS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_SEQ']
        sort id: "desc"
        client cascade: 'save-update'
        subscriptions cascade:"all-delete-orphan"
        attachments cascade:"all-delete-orphan"
        documents cascade:"all-delete-orphan"
        data cascade:"all-delete-orphan"
    }


    def beforeInsert() {
        status = ProcessStatus.NEW;
    }

    def afterInsert() {
        auditLogger.info("Utworzono proces [id:${id}]")
    }

    def afterUpdate() {
        auditLogger.info("Aktualizacja procesu [id:${id}, status:${status}]")
    }

    enum ProcessStatus {
        NEW("Nowy"),
        REJECTED("Odrzucony"),
        WAIT_FOR_SUBSRIPTION("Oczekiwanie na podpis"),
		WAIT_FOR_SUBSCRIPTION_PAPER_VERSION("Oczekiwanie na podpis w wersji papierowej"),
		SUBSCRIPTIONS_DONE("Złożono podpisy"),
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
