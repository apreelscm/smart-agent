package com.eservice.eumowy

import org.springframework.web.multipart.commons.CommonsMultipartFile

import javax.sql.rowset.serial.SerialBlob
import java.sql.Blob


class AttachmentService {


    def deleteFile(def id) {

        println("fileId"+id)
        def fileId = java.lang.Integer.valueOf(id);
        AttachmentFile ufile = AttachmentFile.get(fileId);
        ufile.delete()
    }

    def getList() {
        println("params"+ AttachmentFile.list())
        AttachmentFile.list()
    }

    def uploadFile(def upload, def config, def request, def messageSource) {

        CommonsMultipartFile file = request.getFile("file")

        //base path to save file
        def path = config.path
        if (!path.endsWith('/'))
            path = path + "/"

        /**************************
         check if file exists
         ************************* */
        if (file.size == 0) {
            def msg = messageSource.getMessage("fileupload.upload.nofile", null, request.locale)
            return msg;
        }

        /***********************
         check extensions
         *********************** */

        println "extensions start - allowedExtensions:" + config.allowedExtensions
        def fileExtension = file.originalFilename.substring(file.originalFilename.lastIndexOf('.') + 1)
        if (!config.allowedExtensions[0].equals("*")) {
            if (!config.allowedExtensions.contains(fileExtension)) {
                def msg = messageSource.getMessage("fileupload.upload.unauthorizedExtension", [fileExtension, config.allowedExtensions] as Object[], request.locale)
                return msg
            }
        }
        println "extensions end"

        /*********************
         check file size
         ********************* */
        if (config.maxSize) { //if maxSize config exists
            def maxSizeInKb = ((int) (config.maxSize / 1024))
            if (file.size > config.maxSize) { //if filesize is bigger than allowed
                def msg = messageSource.getMessage("fileupload.upload.fileBiggerThanAllowed", [maxSizeInKb] as Object[], request.locale)
                return msg;
            }
        }

        //reaches here if file.size is smaller or equal config.maxSize or if config.maxSize ain't configured (in this case
        //plugin will accept any size of files).

        //sets new path

       /* path = path + currentTime + "/"
        if (!new File(path).mkdirs())
            log.error "FileUploader plugin couldn't create directories: [${path}]"
        path = path + file.originalFilename

        //move file
        log.info "FileUploader plugin received a ${file.size}b file. Moving to ${new File(path).absolutePath}"
        file.transferTo(new File(path))*/

        //save it on the database
        def currentTime = System.currentTimeMillis()
        def ufile = new AttachmentFile()
        ufile.name = file.originalFilename
        ufile.fileSize = file.size
        ufile.extension = fileExtension
        ufile.dateUploaded = new Date(currentTime)
        ufile.path = path
        ufile.downloads = 0
        ufile.save(flush:true)

        byte[] byteArray = file.bytes;
        Blob blob = new SerialBlob(byteArray);

        def attContent = new AttachmentContent()
        attContent.attachment = ufile;
        attContent.content =  blob;
        println("aaa:"+attContent)
        attContent.save(flush:true);

        return true;
    }


}
