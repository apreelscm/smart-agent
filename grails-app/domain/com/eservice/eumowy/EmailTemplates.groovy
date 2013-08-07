package com.eservice.eumowy

class EmailTemplates implements Serializable {

    EmailTemplateType name;
    String recipent;
    String sender;

    static mapping = {
        table name: "emailtemplates", schema: "CBD_UMOWY"
    }

    static constraints = {
        name(unique:true,blank:false)
        recipent(blank:false, email: true)
        sender(blank:false, email: true)
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