package com.eservice.eumowy

class EmailService {

    boolean transactional = false

    def mailService
    def messageSource

    def sendNotesToCOA(notes) {
        final def templateType = EmailTemplates.EmailTemplateType.NOTES_TO_COA;

        def emailTemplate = EmailTemplates.findByName(templateType)

        if(!emailTemplate) return;

        mailService.sendMail{
            to emailTemplate.recipent
            from emailTemplate.sender
            subject messageSource.getMessage("${emailTemplate.name}.email.subject",null, Locale.default)
            body( view:"/email/template/${emailTemplate.name}", model: [body: notes])
        }
    }
	
	def sendDocumentsPaperVersion(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_PAPER_VERSION)

        if(!emailTemplate) return;

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
	
	def sendDocumentsTemplateVersionWithBlackFaksymile(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)

        if(!emailTemplate) return;

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
	
	def sendDocumentsTemplateVersionWithoutFaksymile(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_TEMPLATE_VERSION)

        if(!emailTemplate) return;

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
	
	def sendDocumentsElectronicalVersion(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ELECTRONICAL_VERSION)

        if(!emailTemplate) return;

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
	
	def sendDocumentsAccepted(List<DocumentFile> documents) {
		def emailTemplate = EmailTemplates.findByName(EmailTemplates.EmailTemplateType.DOCUMENTS_ACCEPTED)

        if(!emailTemplate) return;

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

