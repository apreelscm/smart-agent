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
    String path
    String name
    String extension
    Date dateUploaded
    Integer downloads

    static belongsTo = [process:Process]

    static hasOne = [file:AttachmentContent]

    static constraints = {
        fileSize(min:0L)
        path()
        name()
        extension()
        dateUploaded()
        downloads()
    }

    static mapping = {
        table name: "ATTACHMENT", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.ATTACHMENT_SEQ']
        file cascade:"all-delete-orphan"

        process nullable:false;
        file nullable:false;
    }


    def afterDelete() {
        try {
            File f = new File(path)
            if (f.delete()) {
                log.debug "file [${path}] deleted"
            } else {
                log.error "could not delete file: ${file}"
            }
        } catch (Exception exp) {
            log.error "Error deleting file: ${e.message}"
            log.error exp
        }
    }
}
