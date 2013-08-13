package com.eservice.eumowy

import java.sql.Blob

class AttachmentContent implements Serializable{

    Blob content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
    }

    static mapping = {
        table name: "AttachmentContent", DomainConsts.SHEMA_NAME
        columns {
            content type:'blob'
        }
    }

}
