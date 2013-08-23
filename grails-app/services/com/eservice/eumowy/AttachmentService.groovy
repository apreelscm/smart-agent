package com.eservice.eumowy

class AttachmentService {

    def deleteFile(def id) {

        def fileId = java.lang.Integer.valueOf(id);
        AttachmentFile ufile = AttachmentFile.get(fileId);
        ufile.delete()
    }

    def getListByProcessId(def id) {
        def attachmentCriteria = AttachmentFile.createCriteria()
        def searchResult = attachmentCriteria.list{
            process {
                eq("id", Long.valueOf(id))
            }
        }
        searchResult
    }

    def uploadFile( def config, def request, def messageSource) {
        def file = request.getFile("file")

        /**************************
         check if file exists
         ************************* */
        if (file.size == 0) {
            def msg = messageSource.getMessage("fileupload.upload.nofile", null, request.locale)
            log.warn(msg)
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
                log.warn(msg)
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
                log.warn(msg)
                return msg;
            }
        }

        //save it on the database
        def currentTime = System.currentTimeMillis()
        def ufile = new AttachmentFile()
        ufile.name = file.originalFilename
        ufile.fileSize = file.size
        ufile.extension = fileExtension
        ufile.dateUploaded = new Date(currentTime)
        ufile.downloads = 0
        ufile.file = new AttachmentContent(content:file.bytes)

        return ufile;
    }

    def download(def id, def request, def messageSource) {

        AttachmentFile ufile =  AttachmentFile.get(id)

        if (!ufile) {
            def msg = messageSource.getMessage("fileupload.download.nofile", [id] as Object[], request.locale)
            log.warn msg
            return
        }

        if (ufile != null) {
            log.info "Serving file id=[${ufile.id}] for the ${ufile.downloads}"
            ufile.downloads = ufile.downloads ?:  0
            ufile.downloads++
            ufile.save()
        } else {
            def msg = messageSource.getMessage("fileupload.download.filenotfound", [ufile.name] as Object[], request.locale)
            log.error msg
        }

        ufile
    }

}
