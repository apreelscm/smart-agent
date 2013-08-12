package com.eservice.eumowy

import com.lucastex.grails.fileuploader.UFile

class AttachmentService {

    def deleteFile(def id) {

        println("fileId"+id)
        def fileId = java.lang.Integer.valueOf(id);
        UFile ufile = UFile.get(fileId);
        ufile.delete()
    }

    def getList() {
        println("params"+ UFile.list())
        UFile.list()
    }
}
