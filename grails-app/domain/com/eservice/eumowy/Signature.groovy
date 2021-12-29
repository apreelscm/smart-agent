package com.eservice.eumowy

class Signature implements Serializable {

    String name
    Boolean active = true
	String templatePath
    Boolean forPoint = true
    Boolean forPos = true
	String description
    String filename
    Integer signatureOrder
    Boolean sendToClient = true
    Boolean showOnPreview = true
    Boolean showOnZRD = true
    Boolean shouldBeMerged = true

    static hasMany = [
            calcFieldsSignature: CalcFieldSignature,
            panelsSignature: SignaturePanel,
			documentFile: DocumentFile,
            subscriptionDefinitions: SubscriptionDefinition,
            signatureDetails: SignatureDetail
    ]

    static constraints = {
       name(unique:true,blank:false)
       templatePath()
       filename(nullable:true,blank:true)
	   description()
    }

    static mapping = {
        table name: "SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_SEQ']
        sendToClient column: "SEND_TO_CLIENT"
        showOnPreview column: "SHOW_ON_PREVIEW"
        showOnZRD column: "SHOW_ON_ZRD"
        shouldBeMerged column: "SHOULD_BE_MERGED"
    }

    String toString(){
        return description
    }

    boolean hasDetails() {
        return signatureDetails.size() > 0
    }

    boolean hasPurpose(SignatureDetail.SignaturePurpose purpose) {
        boolean hasPurpose = false

        signatureDetails.each { detail ->
            if (purpose.equals(detail.typ)) {
                hasPurpose = true
            }
        }

        return hasPurpose
    }
}
