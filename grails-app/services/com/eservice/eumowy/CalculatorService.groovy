package com.eservice.eumowy

import com.eservice.eumowy.dao.CbdDAO;

import grails.util.Environment


/**
 * !!
 * !! OBIEKT SESYJNY DO TRZYMANIA KALKULATORA I OPERACJI NA NIM
 * !!
 * */
class CalculatorService implements Serializable{

    static transactional = false

    static final BRAK_LABEL = "BRAK"
	static final FALSE_LABEL = "NIE"
	
	def cbdService

    def isCalcValid(def calcExt, def calcId, def process) {

		def isValidBySigsResult = isCalValidBySignatures(calcExt, process.signatures)
		def isValidByActivResult = isCalValidExtendedValidation(calcId, process.activities, process.signatures)
        
		return isValidBySigsResult == true && isValidByActivResult == true
    }
	
	def isCalValidBySignatures(def calcExt, def signatures) {

		Set signaturesCalcNames = []
		signatures.each{signature ->
			signaturesCalcNames.addAll(signature.calcFieldsSignature?.collect{it.calcField.name});
		}

		println(calcExt)
		def calcKeyList = calcExt.collect { it.POLEAPREEL }

		println("calcKeyList:"+calcKeyList+ " size:"+calcKeyList.size())
		println("calcNames:"+signaturesCalcNames+ " size:"+signaturesCalcNames.size())
		println("contains ALL:"+calcKeyList.containsAll(signaturesCalcNames))


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


    def getCalcProperty(  def calc,def key){
        calc?.findResult{ (it.POLEAPREEL == key && !(it.WARTOSCAPREEL in [BRAK_LABEL,FALSE_LABEL])  ) ? it.WARTOSCAPREEL : null }
    }


}