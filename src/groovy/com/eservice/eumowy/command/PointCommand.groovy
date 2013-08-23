package com.eservice.eumowy.command

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 10:28
 *
 */
class PointCommand implements Serializable {
	
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
	
	Integer cbdId
	
	String dialupType
	Integer dialupCount
	Integer dialupPPCount
	BigDecimal dialupPrice
	BigDecimal dialupPPPrice
	String vpnType
	Integer vpnCount
	Integer vpnPPCount
	BigDecimal vpnPrice
	BigDecimal vpnPPPrice
	String sslType
	Integer sslCount
	Integer sslPPCount
	BigDecimal sslPrice
	BigDecimal sslPPPrice
	String wifiType
	Integer wifiCount
	Integer wifiPPCount
	BigDecimal wifiPrice
	BigDecimal wifiPPPrice
	String gprsType
	Integer gprsCount
	Integer gprsPPCount
	BigDecimal gprsPrice
	BigDecimal gprsPPPrice
	Integer baseCount
	
	Date dayCloseFrom
	Date dayCloseTo
	Date plannedInstallationDate
	String additionalNotes
	
	Boolean preauthorization
	Boolean noreturnfunction
	Boolean returnWithPassword
	Boolean setAnalysis
	Boolean cashMachineSystemIntegration
	Boolean returnIKO
	
	Boolean loggingBeforeEveryTransaction
	Boolean logginEveryChange
	
	Boolean tip1
	Boolean telePompka
	Boolean teleKodzik
	
	Boolean giftCard
	
	String pinPadType
	Integer pinPadCount
	BigDecimal pinPadPrice
	String routerType
	Integer routerCount
	BigDecimal routerPrice
	String cardReaderType
	Integer cardReaderCount
	BigDecimal cardReaderPrice
	
	String otherAdditionalDevice
	Boolean otherAdditionalDeviceSsl
	Boolean otherAdditionalDeviceGprs
	String otherAdditionalDeviceType
	Integer otherAdditionalDeviceCount
	BigDecimal otherAdditionalDevicePrice
	
	String staticDeviceMask
	String staticDeviceGateway
	String staticDeviceIp
	String staticDeviceSupportContact
	String staticDeviceSupportContactName
	String staticDeviceSupportContactSurname
	
	String dynamicDeviceSupportContact
	String dynamicDeviceSupportName
	String dynamicDeviceSupportSurname
	
}
