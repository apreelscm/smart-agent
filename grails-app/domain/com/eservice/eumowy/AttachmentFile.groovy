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

    static hasOne = [file:AttachmentContent]

    static constraints = {
        fileSize(min:0L)
        name()
        extension()
        dateUploaded(nullable:true)
        downloads(nullable: true)
        file(lazy:true)
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
