package com.eservice.eumowy

class DocumentService {

    def pdfService

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

    void reloadDataAndSubscriptionsOnDocuments(Process process, def calculator) {
        log.info("Renewing documents")
        Map processWithPages = pdfService.workWithDocuments(process, calculator)
        process = processWithPages.processInstance
        process.save(flush: true)

        process.documents.each { DocumentFile doc ->
            byte[] newContent = pdfService.addClientSubscriptionsToDocument(doc.content.content, doc.signature.id, process.subscriptions)
            doc.content.content = newContent
            doc.content.discard()
        }
        log.info("Subscriptions and documents data renewed.")
    }
}