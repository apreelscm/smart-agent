var globalPanelCount = 0;
var globalPanelPosCount = 0;

function getGlobalPanelCount(prefix) {
	if (prefix == "points") {
		return globalPanelCount;
	}
	else if (prefix == "poses") {
		return globalPanelPosCount;
	}
}

function setupNewPointPanelHandlers(prevPanelId, panelId, prefix) {
	
	//jQuery(document).ready(function() {
		jQuery("#"+prefix+"\\["+panelId+"\\]\\.bankAccountNumber").on("keyup", {p: prefix, pid: panelId}, function(e) {
			if (jQuery(e.target).val() != undefined && jQuery(e.target).val() != null && jQuery(e.target).val().length == 26) {
				jQuery.get("/eumowy/activity/getBankName", {accountNo: jQuery(e.target).val()}, function(data) {
					if (data != undefined && data != null && data != "") {
						var obj = JSON.parse(data);
						jQuery("#"+e.data.p+"\\["+e.data.pid+"\\]\\.bankName").val(obj.name);
						jQuery("#"+e.data.p+"\\["+e.data.pid+"\\]\\.bankId").val(obj.id);
					}
		     	}); 
			}
		});
	
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.plannedInstallationDate").datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dayCloseFrom").datepicker({ 
        	dateFormat: 'yy-mm-dd',
        	onClose: function( selectedDate ) {
				jQuery( "#"+prefix+"\\["+panelId+"\\]\\.dayCloseTo").datepicker( "option", "minDate", selectedDate );
			}
       	});
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dayCloseTo").datepicker({ 
        	dateFormat: 'yy-mm-dd',
       	 	onClose: function( selectedDate ) {
				jQuery( "#"+prefix+"\\["+panelId+"\\]\\.dayCloseFrom").datepicker( "option", "maxDate", selectedDate );
			}
       	});
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAsAbove").on("click", function(e) {
        	if(e.target.checked) {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.pointNameForSearchEngine").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.pointNameForPrintingFromPOSTerminal").val());
        	} else {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.pointNameForSearchEngine").val("");
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAsForMerchant").on("click", function(e) { 
        	//console.log("clicked");
        	if(e.target.checked) {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressStreet").val(jQuery("#akceptantUlica").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressHomeNumber").val(jQuery("#akceptantNrDomu").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressFlatNumber").val(jQuery("#akceptantNrMieszkania").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressCity").val(jQuery("#akceptantMiasto").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostalCode").val(jQuery("#akceptantKodPocztowy").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostOffice").val(jQuery("#akceptantPoczta").val());
        	} else {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressStreet").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressHomeNumber").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressFlatNumber").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressCity").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostalCode").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostOffice").val("");
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAsForMerchant").on("change", function(e) {
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressStreet").val(jQuery("#akceptantUlica").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressHomeNumber").val(jQuery("#akceptantNrDomu").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressFlatNumber").val(jQuery("#akceptantNrMieszkania").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressCity").val(jQuery("#akceptantMiasto").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressPostalCode").val(jQuery("#akceptantKodPocztowy").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressPostOffice").val(jQuery("#akceptantPoczta").val());
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAsOnPrint").on("change", function(e) {
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressStreet").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressStreet").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressHomeNumber").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressHomeNumber").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressFlatNumber").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressFlatNumber").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressCity").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressCity").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressPostalCode").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostalCode").val());
       		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAddressAddressPostOffice").val(jQuery("#"+prefix+"\\["+panelId+"\\]\\.dataforprintingAddressPostOffice").val());
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.persontocontactAsForMerchant").on("click", function(e) {
        	if (e.target.checked) {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointFax").val(jQuery("#akceptantFax").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointPhone").val(jQuery("#akceptantTelStacjonarny").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointMobilePhone").val(jQuery("#akceptantTelKomorkowy").val());
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointEmail").val(jQuery("#").val());
        	} else {
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointFax").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointPhone").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointMobilePhone").val("");
        		jQuery("#"+prefix+"\\["+panelId+"\\]\\.contactAtPointEmail").val("");
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.sameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.possetforselectedpointSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.technicalinformationSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.terminaloptionsSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.additionalequipmentSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
    //});
	
}

function setupNewPointPanelData(prevPanelId, panelId) {
	//jQuery(document).ready(function() {
	
		var pointdata = {};
		var terminaloptions = {};
		var technicalinformation = {};
		var possetforselectedpoint = {};
		var additionalequipment = {};
		
		if (Object.keys(pointdata).length == 0) {
			pointdata['nip'] = jQuery("#"+prevPanelId+"nip").val();
			pointdata['mmccode'] = jQuery("#"+prevPanelId+"mccCode").val();
			pointdata['bussinessTypeInPractice'] = jQuery("#"+prevPanelId+"bussinessTypeInPractice").val();
			pointdata['bankAccountNumber'] = jQuery("#"+prevPanelId+"bankAccountNumber").val();
			pointdata['bankName'] = jQuery("#"+prevPanelId+"bankName").val();
		}
		
		if (Object.keys(possetforselectedpoint).length == 0) {
			possetforselectedpoint['dialupCount'] = jQuery("#"+prevPanelId+"dialupCount").val();
			possetforselectedpoint['dialupPPCount'] = jQuery("#"+prevPanelId+"dialupPPCount").val();
			possetforselectedpoint['dialupPrice'] = jQuery("#"+prevPanelId+"dialupPrice").val();
			possetforselectedpoint['dialupPPPrice'] = jQuery("#"+prevPanelId+"dialupPPPrice").val();
			possetforselectedpoint['vpnCount'] = jQuery("#"+prevPanelId+"vpnCount").val();
			possetforselectedpoint['vpnPPCount'] = jQuery("#"+prevPanelId+"vpnPPCount").val();
			possetforselectedpoint['vpnPrice'] = jQuery("#"+prevPanelId+"vpnPrice").val();
			possetforselectedpoint['vpnPPPrice'] = jQuery("#"+prevPanelId+"vpnPPPrice").val();
			possetforselectedpoint['sslCount'] = jQuery("#"+prevPanelId+"sslCount").val();
			possetforselectedpoint['sslPPCount'] = jQuery("#"+prevPanelId+"sslPPCount").val();
			possetforselectedpoint['sslPrice'] = jQuery("#"+prevPanelId+"sslPrice").val();
			possetforselectedpoint['sslPPPrice'] = jQuery("#"+prevPanelId+"sslPPPrice").val();
			possetforselectedpoint['wifiCount'] = jQuery("#"+prevPanelId+"wifiCount").val();
			possetforselectedpoint['wifiPPCount'] = jQuery("#"+prevPanelId+"wifiPPCount").val();
			possetforselectedpoint['wifiPrice'] = jQuery("#"+prevPanelId+"wifiPrice").val();
			possetforselectedpoint['wifiPPPrice'] = jQuery("#"+prevPanelId+"wifiPPPrice").val();
			possetforselectedpoint['gprsCount'] = jQuery("#"+prevPanelId+"gprsCount").val();
			possetforselectedpoint['gprsPPCount'] = jQuery("#"+prevPanelId+"gprsPPCount").val();
			possetforselectedpoint['gprsPrice'] = jQuery("#"+prevPanelId+"gprsPrice").val();
			possetforselectedpoint['gprsPPPrice'] = jQuery("#"+prevPanelId+"gprsPPPrice").val();
			possetforselectedpoint['baseCount'] = jQuery("#"+prevPanelId+"baseCount").val();
		}
		
		if (Object.keys(technicalinformation).length == 0) {
			technicalinformation['dayCloseFrom'] = jQuery("#"+prevPanelId+"dayCloseFrom").val();
			technicalinformation['dayCloseTo'] = jQuery("#"+prevPanelId+"dayCloseTo").val();
			technicalinformation['plannedInstallationDate'] = jQuery("#"+prevPanelId+"plannedInstallationDate").val();
			technicalinformation['additionalNotes'] = jQuery("#"+prevPanelId+"additionalNotes").val();
		}
		
		if (Object.keys(terminaloptions).length == 0) {
			terminaloptions['preauthorization'] = jQuery("#"+prevPanelId+"preauthorization").prop("checked");
			terminaloptions['noreturnfunction'] = jQuery("#"+prevPanelId+"noreturnfunction").prop("checked");
			terminaloptions['returnWithPassword'] = jQuery("#"+prevPanelId+"returnWithPassword").prop("checked");
			terminaloptions['setAnalysis'] = jQuery("#"+prevPanelId+"setAnalysis").prop("checked");
			terminaloptions['cashMachineSystemIntegration'] = jQuery("#"+prevPanelId+"cashMachineSystemIntegration").prop("checked");
			terminaloptions['returnIKO'] = jQuery("#"+prevPanelId+"returnIKO").prop("checked");
			terminaloptions['loggingBeforeEveryTransaction'] = jQuery("#"+prevPanelId+"loggingBeforeEveryTransaction").prop("checked");
			terminaloptions['logginEveryChange'] = jQuery("#"+prevPanelId+"logginEveryChange").prop("checked");
			terminaloptions['tip1'] = jQuery("#"+prevPanelId+"tip1").prop("checked");
			terminaloptions['telePompka'] = jQuery("#"+prevPanelId+"telePompka").prop("checked");
			terminaloptions['teleKodzik'] = jQuery("#"+prevPanelId+"teleKodzik").prop("checked");
			terminaloptions['giftCard'] = jQuery("#"+prevPanelId+"giftCard").prop("checked");
			terminaloptions['terminalCount'] = jQuery("#"+prevPanelId+"terminalCount").val();
		}
		
		if (Object.keys(additionalequipment).length == 0) {
			additionalequipment['pinPadCount'] = jQuery("#"+prevPanelId+"pinPadCount").val();
			additionalequipment['pinPadPrice'] = jQuery("#"+prevPanelId+"pinPadPrice").val();
			additionalequipment['routerCount'] = jQuery("#"+prevPanelId+"routerCount").val();
			additionalequipment['routerPrice'] = jQuery("#"+prevPanelId+"routerPrice").val();
			additionalequipment['cardReaderCount'] = jQuery("#"+prevPanelId+"cardReaderCount").val();
			additionalequipment['cardReaderPrice'] = jQuery("#"+prevPanelId+"cardReaderPrice").val();
			additionalequipment['otherAdditionalDevice'] = jQuery("#"+prevPanelId+"otherAdditionalDevice").val();
			additionalequipment['otherAdditionalDeviceSsl'] = jQuery("#"+prevPanelId+"otherAdditionalDeviceSsl").prop("checked");
			additionalequipment['otherAdditionalDeviceGprs'] = jQuery("#"+prevPanelId+"otherAdditionalDeviceGprs").prop("checked");
			additionalequipment['otherAdditionalDeviceCount'] = jQuery("#"+prevPanelId+"otherAdditionalDeviceCount").val();
			additionalequipment['otherAdditionalDevicePrice'] = jQuery("#"+prevPanelId+"otherAdditionalDevicePrice").val();
		}
		
		if (panelId != prevPanelId) {
			if (jQuery("#"+prevPanelId+"sameForEveryPoint").is(':checked')) {
				jQuery("#"+panelId+"nip").val(pointdata['nip']);
				jQuery("#"+panelId+"mccCode").val(pointdata['mmccode']);
				jQuery("#"+panelId+"bussinessTypeInPractice").val(pointdata['bussinessTypeInPractice']);
				jQuery("#"+panelId+"bankAccountNumber").val(pointdata['bankAccountNumber']);
				jQuery("#"+panelId+"bankName").val(pointdata['bankName']);
				jQuery("#"+panelId+"sameForEveryPoint").prop("checked", true);
				jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", true);
			}
			
			if (jQuery("#possetforselectedpointSameForEveryPoint").is(':checked')) {
				jQuery("#"+panelId+"dialupCount").val(possetforselectedpoint['dialupCount']);
				jQuery("#"+panelId+"dialupPPCount").val(possetforselectedpoint['dialupPPCount']);
				jQuery("#"+panelId+"dialupPrice").val(possetforselectedpoint['dialupPrice']);
				jQuery("#"+panelId+"dialupPPPrice").val(possetforselectedpoint['dialupPPPrice']);
				jQuery("#"+panelId+"vpnCount").val(possetforselectedpoint['vpnCount']);
				jQuery("#"+panelId+"vpnPPCount").val(possetforselectedpoint['vpnPPCount']);
				jQuery("#"+panelId+"vpnPrice").val(possetforselectedpoint['vpnPrice']);
				jQuery("#"+panelId+"vpnPPPrice").val(possetforselectedpoint['vpnPPPrice']);
				jQuery("#"+panelId+"sslCount").val(possetforselectedpoint['sslCount']);
				jQuery("#"+panelId+"sslPPCount").val(possetforselectedpoint['sslPPCount']);
				jQuery("#"+panelId+"sslPrice").val(possetforselectedpoint['sslPrice']);
				jQuery("#"+panelId+"sslPPPrice").val(possetforselectedpoint['sslPPPrice']);
				jQuery("#"+panelId+"wifiCount").val(possetforselectedpoint['wifiCount']);
				jQuery("#"+panelId+"wifiPPCount").val(possetforselectedpoint['wifiPPCount']);
				jQuery("#"+panelId+"wifiPrice").val(possetforselectedpoint['wifiPrice']);
				jQuery("#"+panelId+"wifiPPPrice").val(possetforselectedpoint['wifiPPPrice']);
				jQuery("#"+panelId+"gprsCount").val(possetforselectedpoint['gprsCount']);
				jQuery("#"+panelId+"gprsPPCount").val(possetforselectedpoint['gprsPPCount']);
				jQuery("#"+panelId+"gprsPrice").val(possetforselectedpoint['gprsPrice']);
				jQuery("#"+panelId+"gprsPPPrice").val(possetforselectedpoint['gprsPPPrice']);
				jQuery("#"+panelId+"baseCount").val(possetforselectedpoint['baseCount']);
				jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", true);
				jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", true);
			}
			
			if (jQuery("#"+prevPanelId+"technicalinformationSameForEveryPoint").is(':checked')) {
				jQuery("#"+panelId+"dayCloseFrom"+panelId).val(technicalinformation['dayCloseFrom']);
				jQuery("#"+panelId+"dayCloseTo"+panelId).val(technicalinformation['dayCloseTo']);
				jQuery("#"+panelId+"plannedInstallationDate"+panelId).val(technicalinformation['plannedInstallationDate']);
				jQuery("#"+panelId+"additionalNotes"+panelId).val(technicalinformation['additionalNotes']);
				jQuery("#"+panelId+"technicalinformationSameForEveryPoint"+panelId).prop("checked", true);
				jQuery("#"+panelId+"technicalinformationSameForEveryPoint"+panelId).prop("disabled", true);
			}

			if (jQuery("#"+prevPanelId+"terminaloptionsSameForEveryPoint").is(':checked')) {
				jQuery("#"+panelId+"preauthorization").prop("checked", terminaloptions['preauthorization']);
				jQuery("#"+panelId+"noreturnfunction").prop("checked", terminaloptions['noreturnfunction']);
				jQuery("#"+panelId+"returnWithPassword").prop("checked", terminaloptions['returnWithPassword']);
				jQuery("#"+panelId+"setAnalysis").prop("checked", terminaloptions['setAnalysis']);
				jQuery("#"+panelId+"cashMachineSystemIntegration").prop("checked", terminaloptions['cashMachineSystemIntegration']);
				jQuery("#"+panelId+"returnIKO").prop("checked", terminaloptions['returnIKO']);
				jQuery("#"+panelId+"loggingBeforeEveryTransaction").prop("checked", terminaloptions['loggingBeforeEveryTransaction']);
				jQuery("#"+panelId+"logginEveryChange").prop("checked", terminaloptions['logginEveryChange']);
				jQuery("#"+panelId+"tip1").prop("checked", terminaloptions['tip1']);
				jQuery("#"+panelId+"telePompka").prop("checked", terminaloptions['telePompka']);
				jQuery("#"+panelId+"teleKodzik").prop("checked", terminaloptions['teleKodzik']);
				jQuery("#"+panelId+"giftCard").prop("checked", terminaloptions['giftCard']);
				jQuery("#"+panelId+"terminalCount").val(terminaloptions['terminalCount']);
				jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("checked", true);
				jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("disabled", true);
			}
			
			if (jQuery("#"+prevPanelId+"additionalequipmentSameForEveryPoint").is(':checked')) {
				jQuery("#"+panelId+"pinPadCount").val(additionalequipment['pinPadCount']);
				jQuery("#"+panelId+"pinPadPrice").val(additionalequipment['pinPadPrice']);
				jQuery("#"+panelId+"routerCount").val(additionalequipment['routerCount']);
				jQuery("#"+panelId+"routerPrice").val(additionalequipment['routerPrice']);
				jQuery("#"+panelId+"cardReaderCount").val(additionalequipment['cardReaderCount']);
				jQuery("#"+panelId+"cardReaderPrice").val(additionalequipment['cardReaderPrice']);
				jQuery("#"+panelId+"otherAdditionalDevice").val(additionalequipment['otherAdditionalDevice']);
				jQuery("#"+panelId+"otherAdditionalDeviceSsl").prop("checked", additionalequipment['otherAdditionalDeviceSsl']);
				jQuery("#"+panelId+"otherAdditionalDeviceGprs").prop("checked", additionalequipment['otherAdditionalDeviceGprs']);
				jQuery("#"+panelId+"otherAdditionalDeviceCount").val(additionalequipment['otherAdditionalDeviceCount']);
				jQuery("#"+panelId+"otherAdditionalDevicePrice").val(additionalequipment['otherAdditionalDevicePrice']);
				jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("checked", true);
				jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("disabled", true);
			}
		}
	//});
}

function clearNewPointData(prevPanelId, panelId) {
	//jQuery(document).ready(function() {
		if (panelId != prevPanelId) {
			if (jQuery("#"+prevPanelId+"sameForEveryPoint").is(':checked') == false) {
				jQuery("#"+panelId+"nip").val("");
				jQuery("#"+panelId+"mccCode").val("");
				jQuery("#"+panelId+"bussinessTypeInPractice").val("");
				jQuery("#"+panelId+"bankAccountNumber").val("");
				jQuery("#"+panelId+"bankName").val("");
				jQuery("#"+panelId+"sameForEveryPoint").prop("checked", false);
				jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", false);
			}
			
			if (jQuery("#"+prevPanelId+"possetforselectedpointSameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#"+panelId+"dialupCount").val("");
				jQuery("#"+panelId+"dialupPPCount").val("");
				jQuery("#"+panelId+"dialupPrice").val("");
				jQuery("#"+panelId+"dialupPPPrice").val("");
				jQuery("#"+panelId+"vpnCount").val("");
				jQuery("#"+panelId+"vpnPPCount").val("");
				jQuery("#"+panelId+"vpnPrice").val("");
				jQuery("#"+panelId+"vpnPPPrice").val("");
				jQuery("#"+panelId+"sslCount").val("");
				jQuery("#"+panelId+"sslPPCount").val("");
				jQuery("#"+panelId+"sslPrice").val("");
				jQuery("#"+panelId+"sslPPPrice").val("");
				jQuery("#"+panelId+"wifiCount").val("");
				jQuery("#"+panelId+"wifiPPCount").val("");
				jQuery("#"+panelId+"wifiPrice").val("");
				jQuery("#"+panelId+"wifiPPPrice").val("");
				jQuery("#"+panelId+"gprsCount").val("");
				jQuery("#"+panelId+"gprsPPCount").val("");
				jQuery("#"+panelId+"gprsPrice").val("");
				jQuery("#"+panelId+"gprsPPPrice").val("");
				jQuery("#"+panelId+"baseCount").val("");
				jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", false);
				jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", false);
			}
			
			if (jQuery("#"+prevPanelId+"technicalinformationSameForEveryPoint").is(':checked') == false) {
				jQuery("#"+panelId+"dayCloseFrom").val("");
				jQuery("#"+panelId+"dayCloseTo").val("");
				jQuery("#"+panelId+"plannedInstallationDate").val("");
				jQuery("#"+panelId+"additionalNotes").val("");
				jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("checked", false);
				jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("disabled", false);
			}
	
			if (jQuery("#"+prevPanelId+"terminaloptionsSameForEveryPoint").is(':checked') == false) {
				jQuery("#"+panelId+"preauthorization").prop("checked", false);
				jQuery("#"+panelId+"noreturnfunction").prop("checked", false);
				jQuery("#"+panelId+"returnWithPassword").prop("checked", false);
				jQuery("#"+panelId+"setAnalysis").prop("checked", false);
				jQuery("#"+panelId+"cashMachineSystemIntegration").prop("checked", false);
				jQuery("#"+panelId+"returnIKO").prop("checked", false);
				jQuery("#"+panelId+"loggingBeforeEveryTransaction").prop("checked", false);
				jQuery("#"+panelId+"logginEveryChange").prop("checked", false);
				jQuery("#"+panelId+"tip1").prop("checked", false);
				jQuery("#"+panelId+"telePompka").prop("checked", false);
				jQuery("#"+panelId+"teleKodzik").prop("checked", false);
				jQuery("#"+panelId+"giftCard").prop("checked", false);
				jQuery("#"+panelId+"terminalCount").val("");
				jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("checked", false);
				jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("disabled", false);
			}
			
			if (jQuery("#"+prevPanelId+"additionalequipmentSameForEveryPoint").is(':checked') == false) {
				jQuery("#"+panelId+"pinPadCount").val("");
				jQuery("#"+panelId+"pinPadPrice").val("");
				jQuery("#"+panelId+"routerCount").val("");
				jQuery("#"+panelId+"routerPrice").val("");
				jQuery("#"+panelId+"cardReaderCount").val("");
				jQuery("#"+panelId+"cardReaderPrice").val("");
				jQuery("#"+panelId+"otherAdditionalDevice").val("");
				jQuery("#"+panelId+"otherAdditionalDeviceSsl").prop("checked", false);
				jQuery("#"+panelId+"otherAdditionalDeviceGprs").prop("checked", false);
				jQuery("#"+panelId+"otherAdditionalDeviceCount").val("");
				jQuery("#"+panelId+"otherAdditionalDevicePrice").val("");
				jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("checked", false);
				jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("disabled", false);
			}
		}
	//});
}

function setupNewPosPanelHandlers(prevPanelId, panelId, prefix) {
	
	//jQuery(document).ready(function() {
		jQuery("#"+prefix+"\\["+panelId+"\\]\\.plannedInstallationDate").datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dayCloseFrom").datepicker({ 
        	dateFormat: 'yy-mm-dd',
        	onClose: function( selectedDate ) {
				jQuery( "#"+prefix+"\\["+panelId+"\\]\\.dayCloseTo").datepicker( "option", "minDate", selectedDate );
			}
       	});
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.dayCloseTo").datepicker({ 
        	dateFormat: 'yy-mm-dd',
       	 	onClose: function( selectedDate ) {
				jQuery( "#"+prefix+"\\["+panelId+"\\]\\.dayCloseFrom").datepicker( "option", "maxDate", selectedDate );
			}
       	});
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.sameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"["+i+"]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"["+i+"]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.possetforselectedpointSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.technicalinformationSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.terminaloptionsSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
        
        jQuery("#"+prefix+"\\["+panelId+"\\]\\.additionalequipmentSameForEveryPoint").on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
        			}
        		}
        	}
        });
    //});
	
}