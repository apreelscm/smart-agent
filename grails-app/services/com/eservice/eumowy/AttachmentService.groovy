package com.eservice.eumowy

class AttachmentService {

    def deleteFile(def id) {

        def fileId = java.lang.Integer.valueOf(id);
        AttachmentFile ufile = AttachmentFile.get(fileId);
        ufile.delete()
    }

    def getList() {
        AttachmentFile.list()
    }

    def uploadFile(def upload, def config, def request, def messageSource) {

        def file = request.getFile("file")

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

        //save it on the database
        def currentTime = System.currentTimeMillis()
        def ufile = new AttachmentFile()
        ufile.name = file.originalFilename
        ufile.fileSize = file.size
        ufile.extension = fileExtension
        ufile.dateUploaded = new Date(currentTime)
        ufile.path = path
        ufile.downloads = 0
        ufile.file = new AttachmentContent(content:file.bytes)
        ufile.save(flush:true)

        return true;
    }


}
