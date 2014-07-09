package com.eservice.eumowy

import groovy.transform.ToString
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.logging.LogFactory
import org.apache.log4j.MDC

@ToString(includeNames = true,ignoreNulls = true)
class Process implements Serializable {

    private static final auditLogger = LogFactory.getLog("audit")

    Date dateCreated
    Date lastUpdated

    ProcessStatus status;

    String phNumber
    String phFirstName
    String phSurname
    String phEmail
    String calcNumber
    String saleSection
    String notesToCoa
    String notesFromZrd

    boolean observed = false

    Client client
	
    List<Panel> panels
    List<DocumentFile> documents
    List<Representative> representatives

    Set<AttachmentFile> attachments
    Set<ProcessData> processData

    String getStringId() {
        return String.format('%06d',this.id)
    }

    String getStringPhNumber(){
        return phNumber
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
            processData: ProcessData,
            hirePayments: HirePayment,
            posExchanges: PosExchange,
            representatives: Representative
    ]

    static constraints = {
        dateCreated(nullable: true)
        lastUpdated(nullable: true)
        status()
        phNumber()
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
        notesFromZrd(nullable: true, maxSize: 300)
        hirePayments(nullable: true)
        posExchanges(nullable: true)
        representatives(nullable: true)
    }

    static mapping = {
		cache false
        table name: "PROCESS", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.PROCESS_SEQ']
        sort id: "desc"
        client cascade: 'all-delete-orphan'
        subscriptions cascade:"all-delete-orphan"
        attachments cascade:"all-delete-orphan"
        documents cascade:"all-delete-orphan"
        points cascade:"all-delete-orphan"
        processData cascade:"all-delete-orphan"
        hirePayments cascade:"all-delete-orphan"
        posExchanges cascade:"all-delete-orphan"
        representatives cascade:"all-delete-orphan"
    }

    def afterInsert() {
        auditLogger.info("Utworzono proces [id:${id}]")
    }

    def afterUpdate() {
        log.info("mdc:"+MDC.get("sessionUserName"));
        auditLogger.info("Aktualizacja procesu [id:${id}, version:${version}, status:${status}]")
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

    public String getData(String key) {
        return processData.find{it.name.equals(key)}?.value
    }

    public ProcessData getProcessData(String key) {
        return processData.find {it.name.equals(key)}
    }

    public List<DocumentFile> getDocumentsForPreview() {
        return documents?.findAll{it.signature.showOnPreview}
                .sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}
    }
	
	public String getData(String key) {
        return processData.find{it.name.equals(key)}?.value
    }

    public boolean getBooleanData(String key) {
        return Boolean.parseBoolean(getData(key))
    }

    public boolean isAkceptantOsobaPrawna() {
        List<String> osobaPrawnaIndicators = ["spolka_akcyjna", "spolka_zoo", "spolka_komandytowa", "spolka_jawna"]

        return osobaPrawnaIndicators.contains(getData("dzialalnoscForma"))
    }

    public boolean isAkceptantOsobaFizyczna() {
        List<String> osobaFizycznaIndicators = ["spolka_cywilna", "osoba_fizyczna"]

        return osobaFizycznaIndicators.contains(getData("dzialalnoscForma"))
    }

    public boolean isAkceptantJednostkaNieposiadajacaOsobyPrawnej() {
        return StringUtils.isNotEmpty(getData("dzialalnoscFormaInna"))
    }

    public List<DocumentFile> getDocumentsForZRD() {
        return documents.findAll{it.signature.showOnZRD}
                        .sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}
    }

    public List<DocumentFile> getDocumentsForPreview() {
        return documents?.findAll{it.signature.showOnPreview}
                         .sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}
    }
}
