package com.eservice.eumowy

import grails.plugin.cache.Cacheable
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch
import org.springframework.mail.MailException
import pdfgenerator.PdfGenerator

class EmailService {

    boolean transactional = false
	boolean cache = true

    def mailService
    def messageSource
    def processService
    def grailsApplication

    private static final String COA_MAIL = "coa@eservice.com.pl"

	@Cacheable(value="emailTampletByName", key="#name")
	EmailTemplates getEmailTemplatesByName(def name){
		return EmailTemplates.findByName(name,[cache:true])
	}

    def sendNotesToCOA(def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.NOTES_TO_COA)
        sendMailWithTryCatch(emailTemplate, emailTemplate.recipients , null, bodyParams, null)
    }

	def sendDocumentsPaperVersion(List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
		sendMail(emailTemplate, COA_MAIL, null, bodyParams, documents)
	}

    def sendDocumentsPaperVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
    }

    def sendDocumentsTemplateVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
    }

	def sendDocumentsElectronicalVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
		def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, recipient, null, bodyParams, documents)
	}

    def sendDocumentsNotNewAggrementElectronicalVersion(def recipient, List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_NOT_NEW_AGGREMENT_ELECTRONICAL_VERSION)
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
        EmailTemplates emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ACCEPTED)

        Map mailBodyParams = [merchantName: processInstance.client.name, merchantNip: processInstance.client.nip,
                activities: processService.getActivities(processInstance), rejectReason: notesFromZrd]

        sendMail(emailTemplate, processInstance.phEmail, null, mailBodyParams, null)
    }

    void sendPEPnotificationEmail(Process process) {
        EmailTemplates template = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.PEP_NOTIFICATION)

        Map bodyParams = [merchantName: process.client.name, merchantNip: process.client.nip]
        Map subjectParams = [process.client.nip, process.client.name]

        sendMail(template, template.recipient, subjectParams, bodyParams, null)
    }

    def sendDocumentsAcceptedToPostSend(List<DocumentFile> documents, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_MISSING_MAIL)
        sendMail(emailTemplate, emailTemplate.recipients, null, bodyParams, documents)
    }

    def sendDocumentsRejected(def recipient, def merchantName, def merchantNip, def bodyParams) {
        def emailTemplate = getEmailTemplatesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_REJECTED)
        sendMailWithTryCatch(emailTemplate, recipient, [merchantNip, merchantName] as Object[], bodyParams, null)
    }

    private def sendMailWithTryCatch(def emailTemplate, def recipients,  def subjectParams, def bodyParams, def documents){
        try{
            sendMail(emailTemplate, recipients, subjectParams, bodyParams, documents);
        }catch(Exception ex){
            log.error(ex)
            return false
        }
        return true
    }

    private def sendMail(def emailTemplate , def recipients, def subjectParams, def bodyParams, def documents){

        log.info 'Sending: ' + emailTemplate + ', to: ' + recipients + ', subjectParams: ' + subjectParams + ', bodyParams: ' + bodyParams + ', documents count: ' + documents?.size()
        documents.each{
            log.info("document filename:"+(it.clientName ?: it.name))
        }

        StopWatch stopWatch = new Log4JStopWatch();

        if(grailsApplication.config.email.sending.enabled) {
            mailService.sendMail {
                if (documents) {
                    multipart true
                }
                from emailTemplate.sender
                to recipients
                subject messageSource.getMessage("${emailTemplate.name}.email.subject", subjectParams, Locale.default)
                body( view: "/email/template/${emailTemplate.name}", model: bodyParams)

                if (documents){
                    documents.each { doc ->
                        attachBytes doc.clientName ?: doc.name , 'application/pdf', PdfGenerator.closeContent(doc.content.content)
                    }
                }
            }
        } else {
            log.warn("Email sending disabled... If you want to enable it set 'email.sending.enabled=true' in config file.")
        }

        stopWatch.stop('sendMail')
    }
}