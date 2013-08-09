var globalPanelCount = 0;
var globalPanelPosCount = 0;

function getGlobalPanelCount(prefix) {
	if (prefix == "-point") {
		return globalPanelCount;
	}
	else if (prefix == "-pos") {
		return globalPanelPosCount;
	}
}

function setupNewPointPanelHandlers(prevPanelId, panelId, prefix) {
	
	jQuery(document).ready(function() {
        jQuery("#plannedInstallationDate"+prefix+panelId).datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#dayCloseFrom"+prefix+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
        	onClose: function( selectedDate ) {
				jQuery( "#dayCloseTo"+prefix+panelId).datepicker( "option", "minDate", selectedDate );
			}
       	});
        jQuery("#dayCloseTo"+prefix+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
       	 	onClose: function( selectedDate ) {
				jQuery( "#dayCloseFrom"+prefix+panelId).datepicker( "option", "maxDate", selectedDate );
			}
       	});
        
        jQuery("#dataforprinting-asAbove"+prefix+panelId).on("click", function(e) {
        	if(e.target.checked) {
        		jQuery("#pointNameForSearchEngine"+prefix+panelId).val(jQuery("#pointNameForPrintingFromPOSTerminal"+prefix+panelId).val());
        	} else {
        		jQuery("#pointNameForSearchEngine"+prefix+panelId).val("");
        	}
        });
        
        jQuery("#dataforprinting-asForMerchant"+prefix+panelId).on("click", function(e) { 
        	if(e.target.checked) {
        		jQuery("#dataforprinting-addressStreet"+prefix+panelId).val(jQuery("#addressStreet").val());
        		jQuery("#dataforprinting-addressHomeNumber"+prefix+panelId).val(jQuery("#addressHomeNumber").val());
        		jQuery("#dataforprinting-addressFlatNumber"+prefix+panelId).val(jQuery("#addressFlatNumber").val());
        		jQuery("#dataforprinting-addressCity"+prefix+panelId).val(jQuery("#addressCity").val());
        		jQuery("#dataforprinting-addressPostalCode"+prefix+panelId).val(jQuery("#addressPostalCode").val());
        		jQuery("#dataforprinting-addressPostOffice"+prefix+panelId).val(jQuery("#addressPostal").val());
        	} else {
        		jQuery("#dataforprinting-addressStreet"+prefix+panelId).val("");
        		jQuery("#dataforprinting-addressHomeNumber"+prefix+panelId).val("");
        		jQuery("#dataforprinting-addressFlatNumber"+prefix+panelId).val("");
        		jQuery("#dataforprinting-addressCity"+prefix+panelId).val("");
        		jQuery("#dataforprinting-addressPostalCode"+prefix+panelId).val("");
        		jQuery("#dataforprinting-addressPostOffice"+prefix+panelId).val("");
        	}
        });
        
        jQuery("#contactAddress-asForMerchant").on("change", function(e) {
       		jQuery("#contactAddress-addressStreet"+prefix+panelId).val(jQuery("#addressStreet").val());
       		jQuery("#contactAddress-addressHomeNumber"+prefix+panelId).val(jQuery("#addressHomeNumber").val());
       		jQuery("#contactAddress-addressFlatNumber"+prefix+panelId).val(jQuery("#addressFlatNumber").val());
       		jQuery("#contactAddress-addressCity"+prefix+panelId).val(jQuery("#addressCity").val());
       		jQuery("#contactAddress-addressPostalCode"+prefix+panelId).val(jQuery("#addressPostalCode").val());
       		jQuery("#contactAddress-addressPostOffice"+prefix+panelId).val(jQuery("#addressPostal").val());
        });
        
        jQuery("#contactAddress-asOnPrint").on("change", function(e) {
       		jQuery("#contactAddress-addressStreet"+prefix+panelId).val(jQuery("#dataforprinting-addressStreet"+prefix+panelId).val());
       		jQuery("#contactAddress-addressHomeNumber"+prefix+panelId).val(jQuery("#dataforprinting-addressHomeNumber"+prefix+panelId).val());
       		jQuery("#contactAddress-addressFlatNumber"+prefix+panelId).val(jQuery("#dataforprinting-addressFlatNumber"+prefix+panelId).val());
       		jQuery("#contactAddress-addressCity"+prefix+panelId).val(jQuery("#dataforprinting-addressCity"+prefix+panelId).val());
       		jQuery("#contactAddress-addressPostalCode"+prefix+panelId).val(jQuery("#dataforprinting-addressPostalCode"+prefix+panelId).val());
       		jQuery("#contactAddress-addressPostOffice"+prefix+panelId).val(jQuery("#dataforprinting-addressPostOffice"+prefix+panelId).val());
        });
        
        jQuery("#persontocontact-asForMerchant"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		jQuery("#contactAtPointFax"+prefix+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointPhone"+prefix+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointMobilePhone"+prefix+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointEmail"+prefix+panelId).val(jQuery("#").val());
        	} else {
        		jQuery("#contactAtPointFax"+prefix+panelId).val("");
        		jQuery("#contactAtPointPhone"+prefix+panelId).val("");
        		jQuery("#contactAtPointMobilePhone"+prefix+panelId).val("");
        		jQuery("#contactAtPointEmail"+prefix+panelId).val("");
        	}
        });
        
        jQuery("#sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#possetforselectedpoint-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#technicalinformation-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#terminaloptions-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#additionalequipment-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
    });
	
}

function setupNewPointPanelData(prevPanelId, panelId) {
	jQuery(document).ready(function() {
	
		var pointdata = {};
		var terminaloptions = {};
		var technicalinformation = {};
		var possetforselectedpoint = {};
		var additionalequipment = {};
		
		if (Object.keys(pointdata).length == 0) {
			pointdata['nip'] = jQuery("#nip"+prevPanelId).val();
			pointdata['mmccode'] = jQuery("#mccCode"+prevPanelId).val();
			pointdata['bussinessTypeInPractice'] = jQuery("#bussinessTypeInPractice"+prevPanelId).val();
			pointdata['bankAccountNumber'] = jQuery("#bankAccountNumber"+prevPanelId).val();
			pointdata['bankName'] = jQuery("#bankName"+prevPanelId).val();
		}
		
		if (Object.keys(possetforselectedpoint).length == 0) {
			possetforselectedpoint['dialupCount'] = jQuery("#dialupCount"+prevPanelId).val();
			possetforselectedpoint['dialupPPCount'] = jQuery("#dialupPPCount"+prevPanelId).val();
			possetforselectedpoint['dialupPrice'] = jQuery("#dialupPrice"+prevPanelId).val();
			possetforselectedpoint['dialupPPPrice'] = jQuery("#dialupPPPrice"+prevPanelId).val();
			possetforselectedpoint['vpnCount'] = jQuery("#vpnCount"+prevPanelId).val();
			possetforselectedpoint['vpnPPCount'] = jQuery("#vpnPPCount"+prevPanelId).val();
			possetforselectedpoint['vpnPrice'] = jQuery("#vpnPrice"+prevPanelId).val();
			possetforselectedpoint['vpnPPPrice'] = jQuery("#vpnPPPrice"+prevPanelId).val();
			possetforselectedpoint['sslCount'] = jQuery("#sslCount"+prevPanelId).val();
			possetforselectedpoint['sslPPCount'] = jQuery("#sslPPCount"+prevPanelId).val();
			possetforselectedpoint['sslPrice'] = jQuery("#sslPrice"+prevPanelId).val();
			possetforselectedpoint['sslPPPrice'] = jQuery("#sslPPPrice"+prevPanelId).val();
			possetforselectedpoint['wifiCount'] = jQuery("#wifiCount"+prevPanelId).val();
			possetforselectedpoint['wifiPPCount'] = jQuery("#wifiPPCount"+prevPanelId).val();
			possetforselectedpoint['wifiPrice'] = jQuery("#wifiPrice"+prevPanelId).val();
			possetforselectedpoint['wifiPPPrice'] = jQuery("#wifiPPPrice"+prevPanelId).val();
			possetforselectedpoint['gprsCount'] = jQuery("#gprsCount"+prevPanelId).val();
			possetforselectedpoint['gprsPPCount'] = jQuery("#gprsPPCount"+prevPanelId).val();
			possetforselectedpoint['gprsPrice'] = jQuery("#gprsPrice"+prevPanelId).val();
			possetforselectedpoint['gprsPPPrice'] = jQuery("#gprsPPPrice"+prevPanelId).val();
			possetforselectedpoint['baseCount'] = jQuery("#baseCount"+prevPanelId).val();
		}
		
		if (Object.keys(technicalinformation).length == 0) {
			technicalinformation['dayCloseFrom'] = jQuery("#dayCloseFrom"+prevPanelId).val();
			technicalinformation['dayCloseTo'] = jQuery("#dayCloseTo"+prevPanelId).val();
			technicalinformation['plannedInstallationDate'] = jQuery("#plannedInstallationDate"+prevPanelId).val();
			technicalinformation['additionalNotes'] = jQuery("#additionalNotes"+prevPanelId).val();
		}
		
		if (Object.keys(terminaloptions).length == 0) {
			terminaloptions['preauthorization'] = jQuery("#preauthorization"+prevPanelId).prop("checked");
			terminaloptions['noreturnfunction'] = jQuery("#noreturnfunction"+prevPanelId).prop("checked");
			terminaloptions['returnWithPassword'] = jQuery("#returnWithPassword"+prevPanelId).prop("checked");
			terminaloptions['setAnalysis'] = jQuery("#setAnalysis"+prevPanelId).prop("checked");
			terminaloptions['cashMachineSystemIntegration'] = jQuery("#cashMachineSystemIntegration"+prevPanelId).prop("checked");
			terminaloptions['returnIKO'] = jQuery("#returnIKO"+prevPanelId).prop("checked");
			terminaloptions['loggingBeforeEveryTransaction'] = jQuery("#loggingBeforeEveryTransaction"+prevPanelId).prop("checked");
			terminaloptions['logginEveryChange'] = jQuery("#logginEveryChange"+prevPanelId).prop("checked");
			terminaloptions['tip1'] = jQuery("#tip1"+prevPanelId).prop("checked");
			terminaloptions['telePompka'] = jQuery("#telePompka"+prevPanelId).prop("checked");
			terminaloptions['teleKodzik'] = jQuery("#teleKodzik"+prevPanelId).prop("checked");
			terminaloptions['giftCard'] = jQuery("#giftCard"+prevPanelId).prop("checked");
			terminaloptions['terminalCount'] = jQuery("#terminalCount"+prevPanelId).val();
		}
		
		if (Object.keys(additionalequipment).length == 0) {
			additionalequipment['pinPadCount'] = jQuery("#pinPadCount"+prevPanelId).val();
			additionalequipment['pinPadPrice'] = jQuery("#pinPadPrice"+prevPanelId).val();
			additionalequipment['routerCount'] = jQuery("#routerCount"+prevPanelId).val();
			additionalequipment['routerPrice'] = jQuery("#routerPrice"+prevPanelId).val();
			additionalequipment['cardReaderCount'] = jQuery("#cardReaderCount"+prevPanelId).val();
			additionalequipment['cardReaderPrice'] = jQuery("#cardReaderPrice"+prevPanelId).val();
			additionalequipment['otherAdditionalDevice'] = jQuery("#otherAdditionalDevice"+prevPanelId).val();
			additionalequipment['otherAdditionalDeviceSsl'] = jQuery("#otherAdditionalDeviceSsl"+prevPanelId).prop("checked");
			additionalequipment['otherAdditionalDeviceGprs'] = jQuery("#otherAdditionalDeviceGprs"+prevPanelId).prop("checked");
			additionalequipment['otherAdditionalDeviceCount'] = jQuery("#otherAdditionalDeviceCount"+prevPanelId).val();
			additionalequipment['otherAdditionalDevicePrice'] = jQuery("#otherAdditionalDevicePrice"+prevPanelId).val();
		}
		
		if (panelId != prevPanelId) {
			if (jQuery("#sameForEveryPoint"+prevPanelId).is(':checked')) {
				jQuery("#nip"+panelId).val(pointdata['nip']);
				jQuery("#mccCode"+panelId).val(pointdata['mmccode']);
				jQuery("#bussinessTypeInPractice"+panelId).val(pointdata['bussinessTypeInPractice']);
				jQuery("#bankAccountNumber"+panelId).val(pointdata['bankAccountNumber']);
				jQuery("#bankName"+panelId).val(pointdata['bankName']);
				jQuery("#sameForEveryPoint"+panelId).prop("checked", true);
			}
			
			if (jQuery("#possetforselectedpoint-sameForEveryPoint"+prevPanelId).is(':checked')) {
				jQuery("#dialupCount"+panelId).val(possetforselectedpoint['dialupCount']);
				jQuery("#dialupPPCount"+panelId).val(possetforselectedpoint['dialupPPCount']);
				jQuery("#dialupPrice"+panelId).val(possetforselectedpoint['dialupPrice']);
				jQuery("#dialupPPPrice"+panelId).val(possetforselectedpoint['dialupPPPrice']);
				jQuery("#vpnCount"+panelId).val(possetforselectedpoint['vpnCount']);
				jQuery("#vpnPPCount"+panelId).val(possetforselectedpoint['vpnPPCount']);
				jQuery("#vpnPrice"+panelId).val(possetforselectedpoint['vpnPrice']);
				jQuery("#vpnPPPrice"+panelId).val(possetforselectedpoint['vpnPPPrice']);
				jQuery("#sslCount"+panelId).val(possetforselectedpoint['sslCount']);
				jQuery("#sslPPCount"+panelId).val(possetforselectedpoint['sslPPCount']);
				jQuery("#sslPrice"+panelId).val(possetforselectedpoint['sslPrice']);
				jQuery("#sslPPPrice"+panelId).val(possetforselectedpoint['sslPPPrice']);
				jQuery("#wifiCount"+panelId).val(possetforselectedpoint['wifiCount']);
				jQuery("#wifiPPCount"+panelId).val(possetforselectedpoint['wifiPPCount']);
				jQuery("#wifiPrice"+panelId).val(possetforselectedpoint['wifiPrice']);
				jQuery("#wifiPPPrice"+panelId).val(possetforselectedpoint['wifiPPPrice']);
				jQuery("#gprsCount"+panelId).val(possetforselectedpoint['gprsCount']);
				jQuery("#gprsPPCount"+panelId).val(possetforselectedpoint['gprsPPCount']);
				jQuery("#gprsPrice"+panelId).val(possetforselectedpoint['gprsPrice']);
				jQuery("#gprsPPPrice"+panelId).val(possetforselectedpoint['gprsPPPrice']);
				jQuery("#baseCount"+panelId).val(possetforselectedpoint['baseCount']);
				jQuery("#possetforselectedpoint-sameForEveryPoint"+panelId).prop("checked", true);
			}
			
			if (jQuery("#technicalinformation-sameForEveryPoint"+prevPanelId).is(':checked')) {
				jQuery("#dayCloseFrom"+panelId).val(technicalinformation['dayCloseFrom']);
				jQuery("#dayCloseTo"+panelId).val(technicalinformation['dayCloseTo']);
				jQuery("#plannedInstallationDate"+panelId).val(technicalinformation['plannedInstallationDate']);
				jQuery("#additionalNotes"+panelId).val(technicalinformation['additionalNotes']);
				jQuery("#technicalinformation-sameForEveryPoint"+panelId).prop("checked", true);
			}

			if (jQuery("#terminaloptions-sameForEveryPoint"+prevPanelId).is(':checked')) {
				jQuery("#preauthorization"+panelId).prop("checked", terminaloptions['preauthorization']);
				jQuery("#noreturnfunction"+panelId).prop("checked", terminaloptions['noreturnfunction']);
				jQuery("#returnWithPassword"+panelId).prop("checked", terminaloptions['returnWithPassword']);
				jQuery("#setAnalysis"+panelId).prop("checked", terminaloptions['setAnalysis']);
				jQuery("#cashMachineSystemIntegration"+panelId).prop("checked", terminaloptions['cashMachineSystemIntegration']);
				jQuery("#returnIKO"+panelId).prop("checked", terminaloptions['returnIKO']);
				jQuery("#loggingBeforeEveryTransaction"+panelId).prop("checked", terminaloptions['loggingBeforeEveryTransaction']);
				jQuery("#logginEveryChange"+panelId).prop("checked", terminaloptions['logginEveryChange']);
				jQuery("#tip1"+panelId).prop("checked", terminaloptions['tip1']);
				jQuery("#telePompka"+panelId).prop("checked", terminaloptions['telePompka']);
				jQuery("#teleKodzik"+panelId).prop("checked", terminaloptions['teleKodzik']);
				jQuery("#giftCard"+panelId).prop("checked", terminaloptions['giftCard']);
				jQuery("#terminalCount"+panelId).val(terminaloptions['terminalCount']);
				jQuery("#terminaloptions-sameForEveryPoint"+panelId).prop("checked", true);
			}
			
			if (jQuery("#additionalequipment-sameForEveryPoint"+prevPanelId).is(':checked')) {
				jQuery("#pinPadCount"+panelId).val(additionalequipment['pinPadCount']);
				jQuery("#pinPadPrice"+panelId).val(additionalequipment['pinPadPrice']);
				jQuery("#routerCount"+panelId).val(additionalequipment['routerCount']);
				jQuery("#routerPrice"+panelId).val(additionalequipment['routerPrice']);
				jQuery("#cardReaderCount"+panelId).val(additionalequipment['cardReaderCount']);
				jQuery("#cardReaderPrice"+panelId).val(additionalequipment['cardReaderPrice']);
				jQuery("#otherAdditionalDevice"+panelId).val(additionalequipment['otherAdditionalDevice']);
				jQuery("#otherAdditionalDeviceSsl"+panelId).prop("checked", additionalequipment['otherAdditionalDeviceSsl']);
				jQuery("#otherAdditionalDeviceGprs"+panelId).prop("checked", additionalequipment['otherAdditionalDeviceGprs']);
				jQuery("#otherAdditionalDeviceCount"+panelId).val(additionalequipment['otherAdditionalDeviceCount']);
				jQuery("#otherAdditionalDevicePrice"+panelId).val(additionalequipment['otherAdditionalDevicePrice']);
				jQuery("#additionalequipment-sameForEveryPoint"+panelId).prop("checked", true);
			}
		}
	});
}

function clearNewPointData(prevPanelId, panelId) {
	jQuery(document).ready(function() {
		if (panelId != prevPanelId) {
			if (jQuery("#sameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#nip"+panelId).val("");
				jQuery("#mccCode"+panelId).val("");
				jQuery("#bussinessTypeInPractice"+panelId).val("");
				jQuery("#bankAccountNumber"+panelId).val("");
				jQuery("#bankName"+panelId).val("");
				jQuery("#sameForEveryPoint"+panelId).prop("checked", false);
			}
			
			if (jQuery("#possetforselectedpoint-sameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#dialupCount"+panelId).val("");
				jQuery("#dialupPPCount"+panelId).val("");
				jQuery("#dialupPrice"+panelId).val("");
				jQuery("#dialupPPPrice"+panelId).val("");
				jQuery("#vpnCount"+panelId).val("");
				jQuery("#vpnPPCount"+panelId).val("");
				jQuery("#vpnPrice"+panelId).val("");
				jQuery("#vpnPPPrice"+panelId).val("");
				jQuery("#sslCount"+panelId).val("");
				jQuery("#sslPPCount"+panelId).val("");
				jQuery("#sslPrice"+panelId).val("");
				jQuery("#sslPPPrice"+panelId).val("");
				jQuery("#wifiCount"+panelId).val("");
				jQuery("#wifiPPCount"+panelId).val("");
				jQuery("#wifiPrice"+panelId).val("");
				jQuery("#wifiPPPrice"+panelId).val("");
				jQuery("#gprsCount"+panelId).val("");
				jQuery("#gprsPPCount"+panelId).val("");
				jQuery("#gprsPrice"+panelId).val("");
				jQuery("#gprsPPPrice"+panelId).val("");
				jQuery("#baseCount"+panelId).val("");
				jQuery("#possetforselectedpoint-sameForEveryPoint"+panelId).prop("checked", false);
			}
			
			if (jQuery("#technicalinformation-sameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#dayCloseFrom"+panelId).val("");
				jQuery("#dayCloseTo"+panelId).val("");
				jQuery("#plannedInstallationDate"+panelId).val("");
				jQuery("#additionalNotes"+panelId).val("");
				jQuery("#technicalinformation-sameForEveryPoint"+panelId).prop("checked", false);
			}
	
			if (jQuery("#terminaloptions-sameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#preauthorization"+panelId).prop("checked", false);
				jQuery("#noreturnfunction"+panelId).prop("checked", false);
				jQuery("#returnWithPassword"+panelId).prop("checked", false);
				jQuery("#setAnalysis"+panelId).prop("checked", false);
				jQuery("#cashMachineSystemIntegration"+panelId).prop("checked", false);
				jQuery("#returnIKO"+panelId).prop("checked", false);
				jQuery("#loggingBeforeEveryTransaction"+panelId).prop("checked", false);
				jQuery("#logginEveryChange"+panelId).prop("checked", false);
				jQuery("#tip1"+panelId).prop("checked", false);
				jQuery("#telePompka"+panelId).prop("checked", false);
				jQuery("#teleKodzik"+panelId).prop("checked", false);
				jQuery("#giftCard"+panelId).prop("checked", false);
				jQuery("#terminalCount"+panelId).val("");
				jQuery("#terminaloptions-sameForEveryPoint"+panelId).prop("checked", false);
			}
			
			if (jQuery("#additionalequipment-sameForEveryPoint"+prevPanelId).is(':checked') == false) {
				jQuery("#pinPadCount"+panelId).val("");
				jQuery("#pinPadPrice"+panelId).val("");
				jQuery("#routerCount"+panelId).val("");
				jQuery("#routerPrice"+panelId).val("");
				jQuery("#cardReaderCount"+panelId).val("");
				jQuery("#cardReaderPrice"+panelId).val("");
				jQuery("#otherAdditionalDevice"+panelId).val("");
				jQuery("#otherAdditionalDeviceSsl"+panelId).prop("checked", false);
				jQuery("#otherAdditionalDeviceGprs"+panelId).prop("checked", false);
				jQuery("#otherAdditionalDeviceCount"+panelId).val("");
				jQuery("#otherAdditionalDevicePrice"+panelId).val("");
				jQuery("#additionalequipment-sameForEveryPoint"+panelId).prop("checked", false);
			}
		}
	});
}

function setupNewPosPanelHandlers(prevPanelId, panelId, prefix) {
	
	jQuery(document).ready(function() {
		jQuery("#plannedInstallationDate"+prefix+panelId).datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#dayCloseFrom"+prefix+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
        	onClose: function( selectedDate ) {
				jQuery( "#dayCloseTo"+prefix+panelId).datepicker( "option", "minDate", selectedDate );
			}
       	});
        jQuery("#dayCloseTo"+prefix+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
       	 	onClose: function( selectedDate ) {
				jQuery( "#dayCloseFrom"+prefix+panelId).datepicker( "option", "maxDate", selectedDate );
			}
       	});
        
        jQuery("#sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#possetforselectedpoint-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#technicalinformation-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#terminaloptions-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
        
        jQuery("#additionalequipment-sameForEveryPoint"+prefix+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				setupNewPointPanelData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        	else {
        		for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        			if (i != panelId) {
        				clearNewPointData(prefix+panelId, prefix+i);
        			}
        		}
        	}
        });
    });
	
}