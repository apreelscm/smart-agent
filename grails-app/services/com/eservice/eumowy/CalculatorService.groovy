package com.eservice.eumowy

import com.eservice.eumowy.exception.CalculatorException
import com.eservice.eumowy.validator.cbd.ProcessCBDValidator

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
            def cfs = CalcFieldSignature.getCalcFieldsBySignature(signature)
			signaturesCalcNames.addAll(cfs?.collect{it.calcField.name})
		}

        log.info(calcExt)
		def calcKeyList = calcExt.collect { it.POLEAPREEL }

        log.info("calcKeyList size:"+calcKeyList.size() + " calcKeyList:"+calcKeyList)
        log.info("calcNames size:"+signaturesCalcNames.size() + "calcNames:"+signaturesCalcNames)
        log.info("contains ALL:"+calcKeyList.containsAll(signaturesCalcNames))

        List missing = new ArrayList(signaturesCalcNames)
        missing.removeAll(calcKeyList)
        log.info("missing signatures calc names " + missing)

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


    def getCalcProperty(def calc, String key){
        calc?.findResult{ (it.POLEAPREEL == key && !(it.WARTOSCAPREEL in [BRAK_LABEL,FALSE_LABEL])  ) ? it.WARTOSCAPREEL : null }
    }

    def getDecimalCalcProperty(def calc, String key) {
        def value = getCalcProperty(calc, key)
        return value ? new BigDecimal(value) : null
    }

    def getCalculator(Process process, Client client) {
        boolean isCalculatorNeeded = !ActivityHelper.isCalculatorRedundant(process)
        List calculator = []

        if (isCalculatorNeeded) {
            calculator = cbdService.findCalculatorByNip(client.nip)

            if(calculator.isEmpty()) {
                throw new CalculatorException("calc.fetch.error")
            }
        }

        ProcessCBDValidator processCBDValidator = new ProcessCBDValidator(process, client, calculator)

        if(!processCBDValidator.isValid()) {
            throw new CalculatorException(processCBDValidator.getErrorCodes())
        }

        return calculator
    }

    long getCalculatorId(Process process, Client client) {
        if(ActivityHelper.isCalculatorRedundant(process)) return -1

        return cbdService.findCalculatorIdByNip(client.nip)
    }
}