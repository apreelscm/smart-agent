package com.eservice.eumowy

class AttachmentContent implements Serializable{

    byte[] content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
    }

    static mapping = {
        table name: "ATTACHMENT_CONTENT", DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:'ATTACHMENT_CONTENT_SEQ']
        content  sqlType: "blob"
    }

}
