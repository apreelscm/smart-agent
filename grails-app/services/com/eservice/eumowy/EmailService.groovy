package com.eservice.eumowy

import com.eservice.eumowy.auth.EServiceUserDetails
import com.eservice.eumowy.email.EmailAttachment
import com.google.common.base.Strings
import com.google.common.collect.Lists
import grails.plugin.cache.Cacheable
import org.apache.commons.lang.StringUtils
import org.apache.poi.ss.usermodel.Workbook
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch
import org.springframework.mail.MailException
import pdfgenerator.PdfGenerator

import java.text.SimpleDateFormat

import static com.eservice.eumowy.EmailTemplates.EmailTemplateType.*

class EmailService {

    boolean transactional = false
	boolean cache = true

    def mailService
    def messageSource
    def processService
    def grailsApplication
    def springSecurityService

    private static final String COA_MAIL = "coa@eservice.com.pl"

	@Cacheable(value="emailTampletByName", key="#name")
	EmailTemplates getEmailTemplatesByName(def name){
		return EmailTemplates.findByName(name,[cache:true])
	}

    def sendNotesToCOA(def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(NOTES_TO_COA)
        sendMailWithTryCatch(emailTemplate, emailTemplate.recipients , null, bodyParams, null)
    }

	def sendDocumentsPaperVersion(List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_PAPER_VERSION)

        String mailAddress = COA_MAIL
        EServiceUserDetails user = springSecurityService.principal
        Process process = documents?.size() > 0 ? documents.get(0).process : null

        if ((StringUtils.startsWith(user.nr, "98") || StringUtils.startsWith(user.nr, "999")) && process) {
            if(user.email) {
                mailAddress = user.email
            } else {
                log.info(String.format("Sales number of user %s starts with 98 and process has new agreement but user " +
                        "has no email. Sending email to coa@eservice.com.pl...", user.fullName));
            }
        }

		sendMail(emailTemplate, mailAddress, null, bodyParams, documents)
	}

    def sendDocumentsPaperVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_PAPER_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
    }

    def sendDocumentsTemplateVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_TEMPLATE_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
    }

    def sendNewAgreementDocuments(List recipients, List<DocumentFile> documents, def bodyParams) {
        Process process = documents.first()?.process
        String acceptanceEmail = process?.getProcessData("emailDoWysylkiDokumentu")?.value

        log.info "Sending new agreement documents to " + recipients

        if (Strings.isNullOrEmpty(acceptanceEmail)) {
            log.warn "Cannot find email for sending acceptance form for process " + process.id
        } else {
            sendAcceptanceForm(acceptanceEmail, documents, bodyParams)
        }

        sendDocumentsElectronicalVersion(recipients, documents, bodyParams)
    }

    def sendSerialIdReport(Workbook workbook) {
        EmailTemplates emailTemplate = getEmailTemplatesByName(SERIAL_ID_REPORT)
        String recipient = emailTemplate.recipient

        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        workbook.write(bos)

        String attachmentName = "eU_pozsykCWK_${new SimpleDateFormat("ddmmyyyy").format(new Date())}.xls"

        EmailAttachment attachment = new EmailAttachment(attachmentName, "application/vnd.ms-excel", bos.toByteArray())
        sendMail(emailTemplate, recipient, null, [:], [attachment])
    }

    private void sendAcceptanceForm(String recipient, List<DocumentFile> documents, def bodyParams) {
        EmailTemplates emailTemplate = getEmailTemplatesByName(DOCUMENTS_NEW_AGREEMENT)
        List<DocumentFile> acceptanceForm = documents.findAll { "Formularz Akceptanta.pdf".equals(it.clientName) }
        List<String> email = Lists.newArrayList(recipient);

        log.info "Trying to send acceptance form"

        if (!email.isEmpty()) {
            log.info "Sending acceptance form to " + email
            sendMail(emailTemplate, email, null, bodyParams, acceptanceForm)
        } else {
            log.warn "Cannot find email for sending acceptance form"
        }
    }

	def sendDocumentsElectronicalVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
		def emailTemplate = getEmailTemplatesByName(DOCUMENTS_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
	}

    def sendDocumentsNotNewAggrementElectronicalVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_NOT_NEW_AGGREMENT_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
    }

	boolean sendProcessAcceptedMails(Process processInstance, String notesFromZrd) {
        try {
            if (processService.hasPEPdeclarations(processInstance)) {
                sendPEPnotificationEmail(processInstance)
            }
            sendDocumentsAcceptedInfoMail(processInstance, notesFromZrd)
            return true
        } catch (MailException e) {
            e.printStackTrace()
            return false
        }
	}

    void sendDocumentsAcceptedInfoMail(Process processInstance, String notesFromZrd) {
        EmailTemplates emailTemplate = getEmailTemplatesByName(DOCUMENTS_ACCEPTED)

        Map mailBodyParams = [merchantName: processInstance.client.name, merchantNip: processInstance.client.nip,
                activities: processService.getActivities(processInstance), rejectReason: notesFromZrd]

        sendMail(emailTemplate, processInstance.phEmail, null, mailBodyParams, null)
    }

    void sendPEPnotificationEmail(Process process) {
        EmailTemplates template = getEmailTemplatesByName(PEP_NOTIFICATION)

        Map bodyParams = [merchantName: process.client.name, merchantNip: process.client.nip]
        Map subjectParams = [process.client.nip, process.client.name]

        sendMail(template, template.recipient, subjectParams, bodyParams, null)
    }

    def sendDocumentsAcceptedToPostSend(List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_MISSING_MAIL)
        sendMail(emailTemplate, emailTemplate.recipients, null, bodyParams, documents)
    }

    def sendDocumentsRejected(def recipient, def merchantName, def merchantNip, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(DOCUMENTS_REJECTED)
        sendMailWithTryCatch(emailTemplate, recipient, [merchantNip, merchantName] as Object[], bodyParams, null)
    }

    private def sendMailWithTryCatch(def emailTemplate, def recipients,  def subjectParams, def bodyParams, List<DocumentFile> documents){
        try {
            sendMailDocuments(emailTemplate, recipients, subjectParams, bodyParams, documents)
        } catch(Exception ex) {
            log.error(ex)
            return false
        }
        return true
    }

    private def sendMailDocuments(EmailTemplates emailTemplate, def recipients, def subjectParams, def bodyParams, List<DocumentFile> documents) {
        List<EmailAttachment> attachments = []
        documents.each {
            EmailAttachment attachment = new EmailAttachment(it.clientName ?: it.name, 'application/pdf', PdfGenerator.getClosedContent(it.content.content))
            attachments.add(attachment)
        }

        sendMail(emailTemplate, recipients, subjectParams, bodyParams, attachments)
    }

    private def sendMail(EmailTemplates emailTemplate, def recipients, def subjectParams, def bodyParams, List<EmailAttachment> attachments) {
        log.info "Sending ${emailTemplate.name.name()}, to: ${recipients}, subjectParams: ${subjectParams}, bodyParams: ${bodyParams}, attachments count: ${attachments.size()}"

        attachments.each { log.info("Attachment filename: ${it.name}") }

        StopWatch stopWatch = new Log4JStopWatch()

//        if (grailsApplication.config.email.sending.enabled) {
            mailService.sendMail {
                if (attachments) {
                    multipart true
                }
                from emailTemplate.sender
                to recipients
                subject messageSource.getMessage("${emailTemplate.name}.email.subject", subjectParams, Locale.default)
                body( view: "/email/template/${emailTemplate.name}", model: bodyParams)

                if (attachments) {
                    attachments.each { attachment ->
                        attachBytes attachment.name , attachment.contentType, attachment.content
                    }
                }
            }
//        } else {
//            log.warn("Email sending disabled... If you want to enable it set 'email.sending.enabled=true' in config file.")
//        }

        stopWatch.stop('sendMail')
    }
}
