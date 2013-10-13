package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class EmailTemplates implements Serializable {

    EmailTemplateType name;
    String recipient;
    String sender;

    public String[] getRecipients(){
        recipient.split(",");
    }

    static constraints = {
        name(unique:true)
        recipient(blank:false, email: true)
        sender(blank:false, email: true)
    }

    static mapping = {
        table name: "EMAIL_TEMPLATES", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.EMAIL_TEMPLATES_SEQ']
		cache true
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