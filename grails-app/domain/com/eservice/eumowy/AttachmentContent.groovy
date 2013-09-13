package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class AttachmentContent implements Serializable{

    byte[] content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
        content nullable:false, blank:false;
    }

    static mapping = {
        table name: "ATTACHMENT_CONTENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ATTACHMENT_CONTENT_SEQ']
        content  sqlType: "blob"
    }
	
}