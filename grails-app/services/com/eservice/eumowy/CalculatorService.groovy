package com.eservice.eumowy

import com.eservice.eumowy.exception.CalculatorException

class CalculatorService implements Serializable {

    static transactional = false

    static final BRAK_LABEL = "BRAK"
	static final FALSE_LABEL = "NIE"
	
	def cbdService

    def isCalcValid(def calcExt, def calcId, Process process) {
        if(ActivityHelper.isCalculatorRedundant(process)) return true

		boolean isCalculatorSignaturesValid = isCalValidBySignatures(calcExt, process.signatures)
		boolean isCalculaotrActivitiesValid = isCalValidExtendedValidation(calcId, process.activities, process.signatures)
        
		return isCalculatorSignaturesValid && isCalculaotrActivitiesValid
    }
	
	def isCalValidBySignatures(def calcExt, def signatures) {
		Set signaturesCalcNames = []
		signatures.each{signature ->
            def cfs = CalcFieldSignature.getCalcFieldsBySignature(signature);
			signaturesCalcNames.addAll(cfs?.collect{it.calcField.name});
		}

        log.info(calcExt)
		def calcKeyList = calcExt.collect { it.POLEAPREEL }

        log.info("calcKeyList size:"+calcKeyList.size() + " calcKeyList:"+calcKeyList)
        log.info("calcNames size:"+signaturesCalcNames.size() + "calcNames:"+signaturesCalcNames)
        log.info("contains ALL:"+calcKeyList.containsAll(signaturesCalcNames))

		return calcKeyList.containsAll(signaturesCalcNames)
	}
	
	def isCalValidExtendedValidation(def calcId, def activities, def signatures) {
		def activityString = ""
		activities.eachWithIndex { activity, idx  ->
			activityString += activity.code
			if (idx < activities.size() - 1) {
				activityString += ","
			}
		}

        String signaturesString = ""
        signatures.eachWithIndex { sig, idx  ->
            signaturesString += sig.name
            if (idx < signatures.size() - 1) {
                signaturesString += ","
            }
        }

		log.info "isCalcValidByActivities activities [${activityString}], signatures [${signaturesString}]  "
		
		def result = cbdService.checkActivities(activityString, calcId, signaturesString)
		
		return result
	}

    def hasCalcProperty(def key, def value, def calc){
        calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }


    def getCalcProperty(def calc,def key){
        calc?.findResult{ (it.POLEAPREEL == key && !(it.WARTOSCAPREEL in [BRAK_LABEL,FALSE_LABEL])  ) ? it.WARTOSCAPREEL : null }
    }

    def getCalculator(Process process, Client client) {
        if(ActivityHelper.isCalculatorRedundant(process)) return [:]

        def calc = cbdService.findCalculatorByNip(client.nip)

        if(calc.isEmpty()) {
            throw new CalculatorException("calc.fetch.error")
        }

        ActivitiesRequirements requirements = new ActivitiesRequirements(process, calc)

        if(requirements.invalid) {
            throw new CalculatorException(requirements.errorMessage)
        }

        return calc
    }

    long getCalculatorId(Process process, Client client) {
        if(ActivityHelper.isCalculatorRedundant(process)) return -1

        return cbdService.findCalculatorIdByNip(client.nip)
    }
}