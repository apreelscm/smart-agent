package com.eservice.eumowy
/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 19.07.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
class AttachmentFile {

    Integer id
    String filename
    Date dateCreated
    Date lastUpdated

    static belongsTo = [process:Process]

    static constraints = {
        id(unique:true,blank:false)
        filename(unique:false,blank:false)
    }

    static mapping = {
        table name: "AttachmentFile", schema: "CBD_UMOWY"
        autoTimestamp true
        version false
    }
}
