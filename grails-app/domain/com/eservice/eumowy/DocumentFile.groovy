package com.eservice.eumowy
/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 19.07.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
class DocumentFile implements Serializable {

    String name
    String clientName
    Date dateCreated
    Date lastUpdated
	Integer pagesCount

    static belongsTo = [process:Process, signature:Signature]

    /** workaround for hibernate limitation one-to-one eager problem GRAILS-5077 **/
    //static  hasOne = [content:DocumentContent]
    static hasMany = [contents: DocumentContent]

    static transients = ['content']

    /**
     * fake method to override many-to-one behaviour to act as one-to-one
     * @param content
     */
    public void setContent(DocumentContent content) {
        if (!contents){
            contents = []
        } else {
            contents.clear()
        }
        addToContents(content)
    }

    /**
     * fake method to override many-to-one behaviour to act as one-to-one
     * @param content
     */
    public DocumentContent getContent() {
        contents?.iterator().next()
    }


    static constraints = {
        name(unique:false,blank:false)
        clientName(nullable:true)
        dateCreated(nullable:true)
        lastUpdated(nullable:true)
        pagesCount()
        contents(cascade:"all-delete-orphan")
        process(nullable:true)
		signature(nullable:true)
    }

    static mapping = {
        table name: "DOCUMENT", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.DOCUMENT_SEQ']
    }
}
