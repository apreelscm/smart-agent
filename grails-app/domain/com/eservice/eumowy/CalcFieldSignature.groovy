package com.eservice.eumowy

class CalcFieldSignature implements Serializable {

    CalcField calcField;

    static belongsTo = [signature:Signature]

    static constraints = {
        calcField()
    }

    static mapping = {
        table name: "CALCFIELD_SIGNATURE", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.CALCFIELD_SIGNATURE_SEQ']
    }

    public static def getCalcFieldsBySignature(def signature) {
        def crit = CalcFieldSignature.createCriteria()
        def result = crit.list {
            eq("signature", signature)
            join 'calcField'
        }
        println "getCalcFieldsBySignature - signature = ${signature.name} + result size = ${result.size()}"
        return result
    }
}