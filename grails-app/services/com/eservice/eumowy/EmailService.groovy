package com.eservice.eumowy

class EmailService {

    boolean transactional = false

    def mailService
    def messageSource

    def sendNotesToCOA(def notes, def phNumber, def phName) {
        def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.NOTES_TO_COA)
        //if(!emailTemplate) return;
		log.info "COA Notes Recipient: " + emailTemplate.recipient

        try{
            sendMail(emailTemplate, emailTemplate.sender, emailTemplate.recipient, null, [notes: notes, phNumber: phNumber, phName: phName], null)
        }catch(Exception ex){
            log.error(ex)
            return false
        }
        return true
    }

	def sendDocumentsPaperVersion(def recipient, List<DocumentFile> documents, def merchantName) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
        //if(!emailTemplate) return;

        sendMail(emailTemplate, emailTemplate.sender, recipient, null, [merchantName: merchantName], documents)
	}

    def sendDocumentsTemplateVersion(def recipient, List<DocumentFile> documents) {
        def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)
        //if(!emailTemplate) return;

        sendMail(emailTemplate, emailTemplate.sender, recipient, null, null, documents)
    }
	
	def sendDocumentsElectronicalVersion(def recipient, List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ELECTRONICAL_VERSION)
        //if(!emailTemplate) return;

        sendMail(emailTemplate, emailTemplate.sender, recipient, null, null, documents)
	}
	
	def sendDocumentsAccepted(def recipient, List<DocumentFile> documents, def merchantName) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ACCEPTED)
        //if(!emailTemplate) return;

        try{
            sendMail(emailTemplate, emailTemplate.sender, recipient, null, [merchantName: merchantName], documents)
        }catch(Exception ex){
            log.error(ex)
            return false
        }
        return true
	}

    def sendDocumentsAcceptedToPostSend(def recipient, List<DocumentFile> documents, def merchantName, def merchantNip) {
        def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_MISSING_MAIL)
        //if(!emailTemplate) return;

        sendMail(emailTemplate, emailTemplate.sender, emailTemplate.recipient, null, [merchantName: merchantName, merchantNip: merchantNip], documents)
    }

    def sendDocumentsRejected(def recipient, def merchantName, def merchantNip, def rejectReason, def activities) {
        def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_REJECTED)
        //if(!emailTemplate) return;

        try{
            sendMail(emailTemplate, emailTemplate.sender, recipient, [merchantNip, merchantName] as Object[], [merchantName: merchantName, merchantNip: merchantNip, rejectReason: rejectReason, activities: activities], null)
        }catch(Exception ex){
            log.error(ex)
            return false
        }
        return true
    }


    private def sendMail(def emailTemplate, def sender, def recipient, def subjectParams, def bodyParams, def documents){

        println 'Sending: ' + emailTemplate + ', to: ' + recipient + ', subjectParams: ' + subjectParams + ', bodyParams: ' + bodyParams + ', documents count: ' + documents?.size()

        mailService.sendMail {
            if (documents){
                multipart true
            }
            from sender
            to recipient
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