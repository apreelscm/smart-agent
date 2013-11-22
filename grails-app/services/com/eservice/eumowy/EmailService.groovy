package com.eservice.eumowy

import grails.plugin.cache.Cacheable
import org.perf4j.StopWatch
import org.perf4j.log4j.Log4JStopWatch
import pdfgenerator.PdfGenerator

class EmailService {

    boolean transactional = false
	boolean cache = true

    def mailService
    def messageSource

	@Cacheable(value="emailTampletByName", key="#name")
	def getEmailTampletesByName(def name){
		return EmailTemplates.findByName(name,[cache:true])
	}

    def sendNotesToCOA(def notes, def phNumber, def phName) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.NOTES_TO_COA)
        sendMailWithTryCatch(emailTemplate, emailTemplate.recipients , null, [notes: notes, phNumber: phNumber, phName: phName], null)
    }

	def sendDocumentsPaperVersion(def recipient, List<DocumentFile> documents, def merchantName) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
		sendMail(emailTemplate, recipient, null, [merchantName: merchantName], documents)
	}

    def sendDocumentsTemplateVersion(def recipient, List<DocumentFile> documents) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)
        sendMail(emailTemplate, recipient, null, null, documents)
    }
	
	def sendDocumentsElectronicalVersion(def recipient, List<DocumentFile> documents) {
		def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, recipient, null, null, documents)
	}

    def sendDocumentsNotNewAggrementElectronicalVersion(def recipient, List<DocumentFile> documents) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_NOT_NEW_AGGREMENT_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, recipient, null, null, documents)
    }

	def sendDocumentsAccepted(def recipient, List<DocumentFile> documents, def merchantName) {
		def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ACCEPTED)
        sendMailWithTryCatch(emailTemplate, recipient, null, [merchantName: merchantName], documents)
	}

    def sendDocumentsAcceptedToPostSend(List<DocumentFile> documents, def merchantName, def merchantNip) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_MISSING_MAIL)
        sendMail(emailTemplate, emailTemplate.recipients, null, [merchantName: merchantName, merchantNip: merchantNip], documents)
    }

    def sendDocumentsRejected(def recipient, def merchantName, def merchantNip, def rejectReason, def activities) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_REJECTED)
        sendMailWithTryCatch(emailTemplate,
                recipient,
                [merchantNip, merchantName] as Object[],
                [merchantName: merchantName, merchantNip: merchantNip, rejectReason: rejectReason, activities: activities],
                null)
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
                    attachBytes doc.clientName ?: doc.name , 'application/pdf', PdfGenerator.closeContent(doc.content.content.clone())
                }
            }
        }
        stopWatch.stop('sendMail')
    }
}