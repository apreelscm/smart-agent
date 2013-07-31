package com.eservice.eumowy

class Activity implements Serializable {

    String code;
    Integer numerPozycji;

    static constraints = {
        code(unique:true,blank:false)
        numerPozycji(unique:true,blank:false)
    }

    static mapping = {
        table name: "activity", schema: "CBD_UMOWY"
    }

    static hasMany = [
            activitySignatures:ActivitySignatures
    ]

    enum ClientType {
        REPRESENTIVE("Reprezentant"),
        OTHER("Inny");

        private final String text;

        private ClientType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
