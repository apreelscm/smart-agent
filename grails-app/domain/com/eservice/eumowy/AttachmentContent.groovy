package com.eservice.eumowy

class AttachmentContent implements Serializable{

    byte[] content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
        content nullable:false;
    }

    static mapping = {
        table name: "ATTACHMENT_CONTENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ATTACHMENT_CONTENT_SEQ']
        content  sqlType: "blob"
    }
	
}