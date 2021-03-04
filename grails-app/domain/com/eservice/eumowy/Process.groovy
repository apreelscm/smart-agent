package com.eservice.eumowy

import com.eservice.eumowy.enums.options.LegalForm
import com.google.common.collect.Sets
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

    Date signingDate // data nadania statusu Oczekujący/Oczekiwanie na podpis w wersji papierowej/Złożono podpisy/Oczekiwanie na podpis
    Date acceptanceDate // data nadania statusu Zaakceptowany
    Date updateDate // data nadania statusu Edycja/Odrzucony/Przekazane do korekty

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

    String getPhFullInfo() {
        return String.format("%s - %s %s", phNumber, phFirstName, phSurname)
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
        signingDate(nullable: true)
        acceptanceDate(nullable: true)
        updateDate(nullable: true)
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
        CORRECTION("Przekazano do korekty"),
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

    public boolean hasData(String key) {
        def value = getData(key)
        return value != null && value != "-" && !value.isEmpty()
    }

    public String getData(String key) {
        return processData.find{it.name.equals(key)}?.value
    }

    public ProcessData getProcessData(String key) {
        return processData.find {it.name.equals(key)}
    }

    public boolean getBooleanData(String key) {
        return Boolean.parseBoolean(getData(key))
    }

    public boolean isAkceptantOsobaPrawna() {
        return !isAkceptantOsobaFizyczna()
    }

    public boolean isAkceptantOsobaFizyczna() {
        return Sets.newHashSet(LegalForm.PARTNERSHIP_COMPANY.name(), LegalForm.PERSON.name()).contains(getData("dzialalnoscForma"))
    }

    public boolean isAkceptantJednostkaNieposiadajacaOsobyPrawnej() {
        return StringUtils.isNotEmpty(getData("dzialalnoscFormaInna"))
    }

    public List<DocumentFile> getDocumentsForZRD() {
        return documents.findAll{it.signature.showOnZRD}?.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}
    }

    public List<DocumentFile> getDocumentsForPreview() {
        return documents?.findAll{it.signature.showOnPreview}?.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}
    }

    public List<Representative> getAllRepresentatives() {
        return representatives.findAll{Representative.Type.REPRESENTATIVE.equals(it.type)}
    }

    public Set<PointData> getLocalPoints() {
        return points.findAll {it.czyLokalny}
    }

    List<Signature> getAllSelectedSignatures() {
        List<Signature> allSelectedSignatures = []
        activities?.each { activity ->
            activity.selectedActivitySignatures?.each {
                allSelectedSignatures.addAll(it.signature)
            }
        }
        return allSelectedSignatures
    }

    public List<PosData> getChosenPoses() {
        List<PosData> chosenPoses = []

        points.each{ PointData point ->
            chosenPoses.addAll(point?.posDatas.findAll {pos -> pos && pos?.czyWybrany})
        }

        return chosenPoses
    }
}
