package com.eservice.eumowy

class EmailTemplates implements Serializable {

    EmailTemplateType name;
    String recipient;
    String sender;

    static constraints = {
        name(unique:true,blank:false)
        recipient(blank:false, email: true)
        sender(blank:false, email: true)
    }

    static mapping = {
        table name: "EMAIL_TEMPLATES", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.EMAIL_TEMPLATES_SEQ']
    }

    enum EmailTemplateType {
        NOTES_TO_COA("notesToCOA"),
		DOCUMENTS_PAPER_VERSION("documentsPaperVersion"),
		DOCUMENTS_TEMPLATE_VERSION("documentsTemplateVersion"),
		DOCUMENTS_ELECTRONICAL_VERSION("documentsElectronicalVersion"),
		DOCUMENTS_ACCEPTED("documentsAccepted"),
        DOCUMENTS_REJECTED("documentsRejected"),
        DOCUMENTS_MISSING_MAIL("documentsMissingMail")

        private final String text;

        private EmailTemplateType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}