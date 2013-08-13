package com.eservice.eumowy

class AttachmentContent implements Serializable{

    byte[] content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
    }

    static mapping = {
        table name: "AttachmentContent", schema: "CBD_UMOWY"
        content  sqlType: "blob"
    }

}
