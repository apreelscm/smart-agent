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
	Integer pagesCount

    Process process

    static belongsTo = [process:Process]
	
	static hasOne = [content:DocumentContent]

    static constraints = {
        name(unique:false,blank:false)
    }

    static mapping = {
        table name: "DOCUMENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.DOCUMENT_SEQ']
    }
}