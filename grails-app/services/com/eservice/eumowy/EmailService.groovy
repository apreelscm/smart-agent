package com.eservice.eumowy

class EmailService {

    boolean transactional = false

    def mailService
    def messageSource

    def sendNotesToCOA(notes) {
        final def templateType = EmailTemplates.EmailTemplateType.NOTES_TO_COA;

        def emailTemplate = EmailTemplates.findByName(templateType)

        mailService.sendMail{
            to emailTemplate.recipent
            from emailTemplate.sender
            subject messageSource.getMessage("${emailTemplate.name}.email.subject",null, Locale.default)
            body( view:"/email/template/${emailTemplate.name}", model: [body: notes])
        }
    }
	
	def sendDocuments(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)
		
		mailService.sendMail {
			multipart true
			to emailTemplate.recipent
			from emailTemplate.sender
			subject ""
			body( view: "/email/template/${emailTemplate.name}")
			
			documents.each { doc ->
				attach doc.name, doc.content.content 
			}
		}
	}
}