package com.eservice.eumowy

import java.sql.Blob

class AttachmentContent implements Serializable{

    Blob content

    static belongsTo = [attachment:AttachmentFile]

    static constraints = {
    }

    static mapping = {
        table name: "AttachmentContent", schema: "CBD_UMOWY"
        columns {
            content type:'blob'
        }
    }

}
