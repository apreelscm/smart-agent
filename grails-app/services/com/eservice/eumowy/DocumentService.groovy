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

    def findDocumentByName(List<DocumentFile> documents, String name) {
        if (documents != null) {
            for(DocumentFile df : documents) {
                if (df.name.equals(name))
                    return df
            }
        }

        return null
    }
}