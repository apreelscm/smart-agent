package com.eservice.eumowy

class PosData {

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
	
	static belongsTo = [process: Process, point: PointData]
	
	static mapping = {
		table name: "POS", schema:DomainConsts.SHEMA_NAME
		id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.POS_SEQ']
	}
	
    static constraints = {
    }
}
