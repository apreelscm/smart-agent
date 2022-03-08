package com.eservice.eumowy.validator

import com.eservice.eumowy.AttachmentFile
import com.eservice.eumowy.AttachmentService
import com.eservice.eumowy.command.ProcessCommand

class AttachmentsValidator {

    AttachmentService attachmentService

    AttachmentsValidator(AttachmentService attachmentService) {
        this.attachmentService = attachmentService
    }
    /**
     * return true when no validation errors
     */
    public def validate = { ProcessCommand processCmd, processId ->
        return validateProcurators(processCmd, processId)
    }

    private boolean validateProcurators(ProcessCommand processCmd, processId) {
        if (processCmd.hasNewUmowa) {
            if (processCmd.procurators.size() > 0) {
                boolean hasProcuratorAttachment
                List<AttachmentFile> attachmentsFiles = attachmentService.getListByProcessId(processId)
                attachmentsFiles.each { it ->
                    String name = it.name?.toLowerCase()
                    if (name.contains("pełnomocni") || name.contains("pelnomocni")) {
                        hasProcuratorAttachment = true
                    }
                }

                if (!hasProcuratorAttachment) {
                    processCmd.errors.reject("process.procurator.attachment.required")
                    return false
                }
            }
        }
        return true
    }


}
