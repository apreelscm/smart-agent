package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 19.07.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
class DocumentFile implements Serializable {

    String name
    Date dateCreated
    Date lastUpdated
	Integer pagesCount

    static belongsTo = [process:Process]
	
	static hasOne = [content:DocumentContent]

    static constraints = {
        name(unique:false,blank:false)
        dateCreated(nullable:true)
        lastUpdated(nullable:true)
        pagesCount()
        content(lazy:true)
        process(nullable:true)
    }

    static mapping = {
        table name: "DOCUMENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.DOCUMENT_SEQ']
    }
	
}
