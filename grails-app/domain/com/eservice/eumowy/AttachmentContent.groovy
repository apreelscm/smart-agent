package com.eservice.eumowy

class AttachmentContent implements Serializable{

    byte[] content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
    }

    static mapping = {
        table name: "AttachmentContent", DomainConsts.SHEMA_NAME
        content  sqlType: "blob"
    }

}
