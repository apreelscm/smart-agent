package com.eservice.eumowy

class EmailTemplates implements Serializable {

    EmailTemplateType name;
    String recipent;
    String sender;

    static constraints = {
        name(unique:true,blank:false)
        recipent(blank:false, email: true)
        sender(blank:false, email: true)
    }

    static mapping = {
        table name: "EMAIL_TEMPLATES", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'EMAIL_TEMPLATES_SEQ']
    }

    enum EmailTemplateType {
        NOTES_TO_COA("notesToCOA");

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