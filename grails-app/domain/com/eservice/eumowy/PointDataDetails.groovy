package com.eservice.eumowy

import java.util.List;

class PointDataDetails {
	List<PosData> posDatas
	
	BigDecimal phGain
	BigDecimal businessCare
	BigDecimal serviceCare1
	BigDecimal serviceCare2
	BigDecimal serviceCare3
	
	String nip
	String mccCode
	String bussinessTypeInPractice
	String bankAccountNumber
	String bankName
	
	String pointNameForPrintingFromPOSTerminal
	String pointNameForSearchEngine
	
	String dataforprintingAddressStreetType
	String dataforprintingAddressStreet
	Integer dataforprintingAddressHomeNumber
	Integer dataforprintingAddressFlatNumber
	String dataforprintingAddressCity
	String dataforprintingAddressPostalCode
	String dataforprintingAddressPostOffice
	
	String otherDataForPrintingFromTerminal1
	String otherDataForPrintingFromTerminal2
	
	String contactAddressStreetType
	String contactAddressAddressStreet
	Integer contactAddressAddressHomeNumber
	Integer contactAddressAddressFlatNumber
	String contactAddressAddressCity
	String contactAddressAddressPostalCode
	String contactAddressAddressPostOffice
	
	String contactAtPointFirstName
	String contactAtPointLastName
	String contactAtPointFax
	String contactAtPointPhone
	String contactAtPointMobilePhone
	String contactAtPointEmail
	
	static belongsTo = [point: PointData]
	
	static mapping = {
		table name: "POINT_DETAILS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POINT_DETAILS_SEQ']
	}
	
	static constraints = {
	}
}
