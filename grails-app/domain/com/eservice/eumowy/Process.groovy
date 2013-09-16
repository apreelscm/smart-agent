package com.eservice.eumowy

import groovy.transform.ToString

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
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
    String phEmail
    String calcNumber
    String saleSection // TODO skad ?
    String notesToCoa
    String notesToZrd

    boolean observed = false

    Client client
	
    List<Panel> panels
    Set<AttachmentFile> attachments
    List<DocumentFile> documents
    Set<ProcessData> processData

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
            processData: ProcessData
    ]

    static constraints = {
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        status(blank:false)
        phNumber(blank:false)
        phFirstName(blank:false)
        phSurname(blank:false)
        phEmail(blank: false)
        calcNumber(blank:false)
        saleSection(nullable: true)
        observed()
        client()
        activities(nullable: true)
        signatures(nullable: true)
        documents(nullable: true)
        attachments(nullable: true)
        panels(nullable: true)
        subscriptions(nullable: true)
        points(nullable: true)
        processData(nullable: true)
        notesToCoa(nullable: true, maxSize: 1000)
        notesToZrd(blank: false, maxSize: 300)
    }

    static mapping = {
		cache false
        table name: "PROCESS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_SEQ']
        sort id: "desc"
       // client cascade: 'save-update'
        subscriptions cascade:"all-delete-orphan"
        attachments cascade:"all-delete-orphan"
        documents cascade:"all-delete-orphan"
        points cascade:"all-delete-orphan"
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
        NEW("Edycja"),
        REJECTED("Odrzucony"),
        WAIT_FOR_SUBSCRIPTION("Oczekiwanie na podpis"),
        WAIT_FOR_SUBSCRIPTION_PAPER_VERSION("Oczekiwanie na podpis w wersji papierowej"),
        SUBSCRIPTIONS_DONE("Złożono podpisy"),
        WAITING("Oczekujący"),
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).append(version).toHashCode()
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
		  return false;
		}
		Process rhs = (Process) obj;
		return new EqualsBuilder()
					  .appendSuper(super.equals(obj))
					  .append(id, rhs.id)
					  .append(version, rhs.version)
					  .isEquals();
	}
	
	
	
}
