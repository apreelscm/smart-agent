package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class SignaturePanel implements Serializable {

    Panel panel;

    static belongsTo = [signature:Signature]

    static constraints = {
    }

    static mapping = {
        table name: "SIGNATURE_PANEL", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SIGNATURE_PANEL_SEQ']
    }
	
}
