package com.eservice.eumowy

class DocumentService {

    def download(def id) {

        log.info "Download document id=[${id}]"

        DocumentFile file =  DocumentFile.get(id)

        if (!file) {
            throw new NoSuchElementException()
        }

        file
    }
}