package com.eservice.eumowy

class DocumentsSigningRestController {
    DocumentsSigningService documentsSigningService

    def signDocuments(SignDocumentCommand cmd) {
        SignDocumentsResult result = documentsSigningService.signDocuments(cmd);
        if (result.isError()) {
            render(status: 400)
            return
        }
        render(text: "{\"signatureId\": " + result.signatureId() + "}")
    }

    def refreshSigningCode(ResetSigningCodeCommand cmd) {
        RefreshSigningCodeResult result = documentsSigningService.refreshSigningCode(cmd);
        if (result.isError()) {
            render(status: 400)
            return
        }
        render(status: 200)
    }
}
