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
}
