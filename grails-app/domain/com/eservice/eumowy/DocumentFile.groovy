package com.eservice.eumowy
/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 19.07.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
class DocumentFile {

    String name
    Date dateCreated
    Date lastUpdated

    static belongsTo = [process:Process]

    static constraints = {
        name(unique:false,blank:false)
    }

    static mapping = {
        table name: "DOCUMENT", DomainConsts.SHEMA_NAME
        autoTimestamp true
        version false
    }
}
