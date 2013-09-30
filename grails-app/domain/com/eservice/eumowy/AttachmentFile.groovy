package com.eservice.eumowy
/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 19.07.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
class AttachmentFile implements Serializable{

    Long fileSize
    String name
    String extension
    Date dateUploaded
    Integer downloads

    static belongsTo = [process:Process]

    /** workaround for hibernate limitation one-to-one eager problem GRAILS-5077 **/
    //static hasOne = [file:AttachmentContent]
    static hasMany = [files: AttachmentContent]

    static transients = ['file']

    //List<AttachmentContent> files = []

    /**
     * fake method to override many-to-one behaviour to act as one-to-one
     * @param file
     */
    public void setFile(AttachmentContent file) {
        if (!files){
            files = []
        } else {
            files.clear()
        }

        addToFiles(file)
    }

    /**
     * fake method to override many-to-one behaviour to act as one-to-one
     * @param file
     */
    public AttachmentContent getFile() {
        files?.iterator().next()
    }


    static constraints = {
        fileSize(min:0L)
        name()
        extension()
        dateUploaded(nullable:true)
        downloads(nullable: true)
        files(cascade:"all-delete-orphan")
        process(nullable:true)
    }

    static mapping = {
        table name: "ATTACHMENT", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ATTACHMENT_SEQ']
        file cascade:"all-delete-orphan"
    }

    def beforeInsert() {
        log.info("Tworzenie załącznika [id:${id}, name:${name}]")
    }

    def afterInsert() {
        log.info("Utworzono załącznik [id:${id}, name:${name}]")
    }

}
