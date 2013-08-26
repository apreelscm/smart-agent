package com.eservice.eumowy

import groovy.transform.ToString
import org.apache.commons.logging.LogFactory

@ToString(includeNames = true,ignoreNulls = true)
class Process implements Serializable {

    private static final auditLogger = LogFactory.getLog("audit")

    Date dateCreated
    Date lastUpdated

    ProcessStatus status;

    Integer phNumber
    String phFirstName
    String phSurname
    String calcNumber
    String saleSection // TODO skad ?
    String notesToCoa
    String notesToZrd

    boolean observed = false

    Client client
	
	List<Panel> panels

    List<Panel> panels
    List<AttachmentFile> attachments
    List<DocumentFile> documents

    String getStringId() {
        return String.format('%06d',this.id)
    }

    String getStringPhNumber(){
        return phNumber ? Integer.toString(this.phNumber) : ''
    }

    static transients = ['stringId']

    static hasMany = [
            documents:DocumentFile,
            attachments:AttachmentFile,
            activities:Activity,
            signatures:Signature,
            panels: Panel,
            subscriptions:Subscription,
            points: PointData,
            poses: PosData,
            processData: ProcessData
    ]

    static constraints = {
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        status(blank:false)
        phNumber(blank:false)
        phFirstName(blank:false)
        phSurname(blank:false)
        calcNumber(blank:false)
        saleSection(nullable: true)
        observed()
        client()
        documents(nullable: true)
        attachments(nullable: true)
        panels(nullable: true)
        subscriptions(nullable: true)
        points(nullable: true)
        poses(nullable: true)
        processData(nullable: true)
        notesToCoa(nullable: true)
        notesToZrd(nullable: true)
    }

    static mapping = {
        table name: "PROCESS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_SEQ']
        sort id: "desc"
        client cascade: 'save-update'
        subscriptions cascade:"all-delete-orphan"
        attachments cascade:"all-delete-orphan"
        documents cascade:"all-delete-orphan"
        points cascade:"all-delete-orphan"
        poses cascade:"all-delete-orphan"
        processData cascade:"all-delete-orphan"
    }


    def beforeInsert() {
        auditLogger.info("Tworzenie procesu [id:${id}]")
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
