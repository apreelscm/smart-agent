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
	
	def cbdService

    def isCalcValid(def calcExt, def calcId, def process) {

		def isValidBySigsResult = isCalValidBySignatures(calcExt, process.signatures)
		def isValidByActivResult = isCalcValidByActivities(calcId, process.activities)
        
		return isValidBySigsResult == true && isValidByActivResult == true
    }
	
	def isCalValidBySignatures(def calcExt, def signatures) {
		// TODO tymczasowo
		if(Environment.isDevelopmentMode() ||
				Environment.TEST.getName().equalsIgnoreCase(Environment.getCurrent().name)){ return true }

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
	
	def isCalcValidByActivities(def calcId, def activities) {
		def activityString = ""
		activities.eachWithIndex { activity, idx  ->
			activityString += activity.code
			if (idx < activities.size() - 1) {
				activityString += ","
			}
		}
		log.info "isCalcValidByActivities - " + activityString
		
		def result = cbdService.checkActivities(activityString, calcId)
		
		return result
	}

    def hasCalcProperty(def key, def value, def calc){
        calc?.contains([POLEAPREEL:key, WARTOSCAPREEL:value])
    }


    def getCalcProperty(  def calc,def key){
        calc?.findResult{ (it.POLEAPREEL == key && it.WARTOSCAPREEL != BRAK_LABEL  ) ? it.WARTOSCAPREEL : null }
    }
}