package com.eservice.eumowy

import java.util.HashMap
import java.util.List
import java.util.Locale
import grails.plugin.cache.Cacheable



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
		log.info "COA Notes Recipient: " + emailTemplate.recipient
        sendMailWithTryCatch(emailTemplate, null, [notes: notes, phNumber: phNumber, phName: phName], null)		
    }

	def sendDocumentsPaperVersion(def recipient, List<DocumentFile> documents, def merchantName) {
		emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
		sendMail(emailTemplate, recipient, null, [merchantName: merchantName], documents)
	}

    def sendDocumentsTemplateVersion(def recipient, List<DocumentFile> documents) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)
        sendMail(emailTemplate, recipient, null, null, documents)
    }
	
	def sendDocumentsElectronicalVersion(def recipient, List<DocumentFile> documents) {
		def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ELECTRONICAL_VERSION)
        sendMail(emailTemplate, null, null, documents)
	}
	
	def sendDocumentsAccepted(def recipient, List<DocumentFile> documents, def merchantName) {
		def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ACCEPTED)
        sendMailWithTryCatch(emailTemplate, null, [merchantName: merchantName], documents)
	}

    def sendDocumentsAcceptedToPostSend(def recipient, List<DocumentFile> documents, def merchantName, def merchantNip) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_MISSING_MAIL)
        sendMail(emailTemplate, null, [merchantName: merchantName, merchantNip: merchantNip], documents)
    }

    def sendDocumentsRejected(def recipient, def merchantName, def merchantNip, def rejectReason, def activities) {
        def emailTemplate = getEmailTampletesByName(EmailTemplates.EmailTemplateType.DOCUMENTS_REJECTED)
        sendMailWithTryCatch(emailTemplate, [merchantNip, merchantName] as Object[], [merchantName: merchantName, merchantNip: merchantNip, rejectReason: rejectReason, activities: activities], null)
    }

    private def sendMailWithTryCatch(def emailTemplate, def subjectParams, def bodyParams, def documents){
        try{
            sendMail(emailTemplate, subjectParams, bodyParams, documents);
        }catch(Exception ex){
            log.error(ex)
            return false
        }
        return true
    }

    private def sendMail(def emailTemplate , def subjectParams, def bodyParams, def documents){

		//wydzielenie adresatów z separatorem ","
		String[] recipients = emailTemplate.recipient.split(",");
		
		println 'Sending: ' + emailTemplate + ', to: ' + recipients + ', subjectParams: ' + subjectParams + ', bodyParams: ' + bodyParams + ', documents count: ' + documents?.size()
		
        mailService.sendMail {
            if (documents){
                multipart true
            }
            from emailTemplate.sender
            to recipients
            subject messageSource.getMessage("${emailTemplate.name}.email.subject", subjectParams, Locale.default)
            body( view: "/email/template/${emailTemplate.name}", model: bodyParams)

            if (documents){
                documents.each { doc ->
                    attachBytes doc.name, 'application/pdf', doc.content.content
                }
            }
        }
    }
}