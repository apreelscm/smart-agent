var globalPanelCount = 0;
var globalPanelPosCount = 0;
var panelPosInternalCount = 0;

var sameForEveryPointSourcePanelId = {
	"sameForEveryPoint": -1,
	"possetforselectedpointSameForEveryPoint": -1,
	"technicalinformationSameForEveryPoint": -1,
	"terminaloptionsSameForEveryPoint": -1,
	"additionalequipmentSameForEveryPoint": -1
};

var sameForEveryPointSourcePosId = {
	"sameForEveryPoint": -1,
	"possetforselectedpointSameForEveryPoint": -1,
	"technicalinformationSameForEveryPoint": -1,
	"terminaloptionsSameForEveryPoint": -1,
	"additionalequipmentSameForEveryPoint": -1
};

function getCurrentTerminalCount(prefix) {

    var counter = 0;

    if (panelPosInternalCount != undefined) {
        counter += panelPosInternalCount;
    }

    for(var i = 0; i < globalPanelCount; i++) {
        var prefixPanel = "#"+prefix+"\\["+i+"\\]\\";
        var dialupCount = jQuery(prefixPanel + ".dialupCount").val();
        var vpnCount = jQuery(prefixPanel + ".vpnCount").val();
        var sslCount = jQuery(prefixPanel + ".sslCount").val();
        var wifiCount = jQuery(prefixPanel + ".wifiCount").val();
        var gprsCount = jQuery(prefixPanel + ".gprsCount").val();
        var baseCount = jQuery(prefixPanel + ".baseCount").val();

        counter += dialupCount != "" ? parseInt(dialupCount) : 0;
        counter += vpnCount != "" ? parseInt(vpnCount) : 0;
        counter += sslCount != "" ? parseInt(sslCount) : 0;
        counter += wifiCount != "" ? parseInt(wifiCount) : 0;
        counter += gprsCount != "" ? parseInt(gprsCount) : 0;
        counter += baseCount != "" ? parseInt(baseCount) : 0;
    }

    return counter;
}

function getGlobalPanelCount(prefix) {
    if (prefix == "points") {
        return globalPanelCount;
    }
    else if (prefix == "poses") {
        return globalPanelPosCount;
    }
}

function setupNewPointPanelHandlers(panelId, prefix) {
    var prefixPanel = "#"+prefix+"\\["+panelId+"\\]\\";

    jQuery(prefixPanel + ".bankAccountNumber").on("keyup", {p: prefix, pid: panelId}, function(e) {
        var accountNr = jQuery(e.target).val();
        if ( accountNr != undefined && accountNr != null){

            var bankNameInput = jQuery(prefixPanel + ".bankName");
            var bankIdInput = jQuery(prefixPanel + ".bankId");

            if (validateAccountNumber(accountNr)){
                jQuery.get("/eumowy/activity/getBankName", {accountNo: accountNr.replace(/\s+/g, '')}, function(data) {
                    if (data != undefined && data != null && data != "") {
                        var obj = JSON.parse(data);
                        bankNameInput.val(obj.name).keyup();
                        bankIdInput.val(obj.id);
                    }
                });
            } else {
                bankNameInput.val('').attr('placeholder', 'Numer konta jest nieprawidłowy');
                bankIdInput.val('');
            }
        }
    });

    //adres do korespondencji punktu
    var $codeField = jQuery(prefixPanel+".contactAddressAddressPostalCode")
    var $cityField = jQuery(prefixPanel+".contactAddressAddressCity")

    refreshCityField( $codeField.val(),  $cityField )

    $codeField.on("keyup", {p: prefix, pid: panelId}, function(e) {
        refreshCityField(jQuery(e.target).val(),  $cityField)
    });

    //adres dowydruku
    var $codeField2 = jQuery(prefixPanel+".dataforprintingAddressPostalCode")
    var $cityField2 = jQuery(prefixPanel+".dataforprintingAddressCity")

    refreshCityField( $codeField2.val(),  $cityField2 )
    $codeField2.on("keyup", {p: prefix, pid: panelId}, function(e) {
        refreshCityField(jQuery(e.target).val(),  $cityField2)
    });

    jQuery(prefixPanel + ".mccCode").on("keyup", {p: prefix, pid: panelId}, function(e) {
        var mcc = jQuery(e.target).val();
        if ( mcc != undefined && mcc != null){
            jQuery.get("/eumowy/activity/getRodzajDzialalnosci", {mcc: mcc}, function(data) {
                if (data != undefined && data != null && data != "") {
                    var obj = JSON.parse(data);
                    jQuery(prefixPanel + ".bussinessTypeInPractice").val(obj.id);
                }
            });
        }
    });

    addDateHandlers(prefixPanel);

    jQuery(prefixPanel + ".dataforprintingAsAbove").on("click", function(e) {
        if(e.target.checked) {
            jQuery(prefixPanel + ".pointNameForSearchEngine").val(jQuery(prefixPanel + ".pointNameForPrintingFromPOSTerminal").val()).keyup();
        } else {
            jQuery(prefixPanel + ".pointNameForSearchEngine").val("");
        }
    });

    jQuery(prefixPanel + ".dataforprintingAsForMerchant").on("click", function(e) {
        if(e.target.checked) {
            jQuery(prefixPanel + ".dataforprintingAddressStreetType").val(jQuery("#akceptantUlicaTytul").val()).keyup();
            jQuery(prefixPanel + ".dataforprintingAddressStreet").val(jQuery("#akceptantUlica").val()).keyup();
            jQuery(prefixPanel + ".dataforprintingAddressHomeNumber").val(jQuery("#akceptantNrDomu").val()).keyup();
            jQuery(prefixPanel + ".dataforprintingAddressFlatNumber").val(jQuery("#akceptantNrMieszkania").val()).keyup();

            var $dataforprintingAddressCity =  jQuery(prefixPanel + ".dataforprintingAddressCity")
            $dataforprintingAddressCity.append('<option value="'+jQuery('#akceptantMiasto').val()+'">'+jQuery('#akceptantMiasto').val()+'</option>')
            $dataforprintingAddressCity.val(jQuery('#akceptantMiasto').val()).keyup();

            jQuery(prefixPanel + ".dataforprintingAddressPostalCode").val(jQuery("#akceptantKodPocztowy").val()).keyup();
            jQuery(prefixPanel + ".dataforprintingAddressPostOffice").val(jQuery("#akceptantPoczta").val()).keyup();
        } else {
            jQuery(prefixPanel + ".dataforprintingAddressStreetType").val("");
            jQuery(prefixPanel + ".dataforprintingAddressStreet").val("");
            jQuery(prefixPanel + ".dataforprintingAddressHomeNumber").val("");
            jQuery(prefixPanel + ".dataforprintingAddressFlatNumber").val("");
            jQuery(prefixPanel + ".dataforprintingAddressCity").val("");
            jQuery(prefixPanel + ".dataforprintingAddressPostalCode").val("");
            jQuery(prefixPanel + ".dataforprintingAddressPostOffice").val("");
        }
    });

    jQuery(prefixPanel + ".contactAddressAsForMerchant").on("change", function(e) {
        jQuery(prefixPanel + ".contactAddressAddressStreetType").val(jQuery("#akceptantUlicaTytul").val());
        jQuery(prefixPanel + ".contactAddressAddressStreet").val(jQuery("#akceptantUlica").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressHomeNumber").val(jQuery("#akceptantNrDomu").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressFlatNumber").val(jQuery("#akceptantNrMieszkania").val()).keyup();

        //jQuery(prefixPanel + ".contactAddressAddressCity").val(jQuery("#akceptantMiasto").val()).keyup();
        var $contactAddressAddressCity =  jQuery(prefixPanel + ".contactAddressAddressCity")
        $contactAddressAddressCity.append('<option value="'+jQuery('#akceptantMiasto').val()+'">'+jQuery('#akceptantMiasto').val()+'</option>')
        $contactAddressAddressCity.val(jQuery('#akceptantMiasto').val()).keyup();

        jQuery(prefixPanel + ".contactAddressAddressPostalCode").val(jQuery("#akceptantKodPocztowy").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressPostOffice").val(jQuery("#akceptantPoczta").val()).keyup();
    });

    jQuery(prefixPanel + ".contactAddressAsOnPrint").on("change", function(e) {
        jQuery(prefixPanel + ".contactAddressAddressStreetType").val(jQuery(prefixPanel + ".dataforprintingAddressStreetType").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressStreet").val(jQuery(prefixPanel + ".dataforprintingAddressStreet").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressHomeNumber").val(jQuery(prefixPanel + ".dataforprintingAddressHomeNumber").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressFlatNumber").val(jQuery(prefixPanel + ".dataforprintingAddressFlatNumber").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressCity").val(jQuery(prefixPanel + ".dataforprintingAddressCity").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressPostalCode").val(jQuery(prefixPanel + ".dataforprintingAddressPostalCode").val()).keyup();
        jQuery(prefixPanel + ".contactAddressAddressPostOffice").val(jQuery(prefixPanel + ".dataforprintingAddressPostOffice").val()).keyup();
    });

    jQuery(prefixPanel + ".persontocontactAsForMerchant").on("click", function(e) {
        if (e.target.checked) {
            jQuery(prefixPanel + ".contactAtPointTitle").val(jQuery("#kontaktTytul").val()).keyup();
            jQuery(prefixPanel + ".contactAtPointFirstName").val(jQuery("#kontaktImie").val()).keyup();
            jQuery(prefixPanel + ".contactAtPointLastName").val(jQuery("#kontaktNazwisko").val()).keyup();
            jQuery(prefixPanel + ".contactAtPointPhone").val(jQuery("#kontaktTelStacjonarny").val()).keyup();
            jQuery(prefixPanel + ".contactAtPointMobilePhone").val(jQuery("#kontaktTelKomorkowy").val()).keyup();
            jQuery(prefixPanel + ".contactAtPointEmail").val(jQuery("#kontaktEmail").val()).keyup();
        } else {
            jQuery(prefixPanel + ".contactAtPointTitle").val('');
            jQuery(prefixPanel + ".contactAtPointFirstName").val('');
            jQuery(prefixPanel + ".contactAtPointLastName").val('');
            jQuery(prefixPanel + ".contactAtPointPhone").val("");
            jQuery(prefixPanel + ".contactAtPointMobilePhone").val("");
            jQuery(prefixPanel + ".contactAtPointEmail").val("");
        }
    });

    jQuery(prefixPanel + ".wifiCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".vpnCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val());
        unlockStaticAddress(lock, prefixPanel);
        unlockDynamicAddress(testNumber(e.target.value), prefixPanel);
    })
    jQuery(prefixPanel + ".vpnCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val());
        unlockStaticAddress(lock, prefixPanel);
    })
    jQuery(prefixPanel + ".sslCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".vpnCount").val());
        unlockStaticAddress(lock, prefixPanel);
    })

    sameForEveryPoint(prefixPanel + ".sameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".possetforselectedpointSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".technicalinformationSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".terminaloptionsSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".additionalequipmentSameForEveryPoint", prefix, panelId);

    unlockStaticAddress(testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".vpnCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val()), prefixPanel);
    unlockDynamicAddress(testNumber(jQuery(prefixPanel + ".wifiCount").val()), prefixPanel);
}

function testNumber(value){
    return parseInt(value)>0;
}

function unlockStaticAddress(lock, prefixPanel){
    if (lock){
        jQuery(prefixPanel + ".tytulInformatykStatyczna").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceMask").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceGateway").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceIp").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceSupportContact").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceSupportContactName").prop("disabled", false);
        jQuery(prefixPanel + ".staticDeviceSupportContactSurname").prop("disabled", false);
    }  else {
        jQuery(prefixPanel + ".tytulInformatykStatyczna").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceMask").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceGateway").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceIp").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceSupportContact").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceSupportContactName").val('').prop("disabled", true);
        jQuery(prefixPanel + ".staticDeviceSupportContactSurname").val('').prop("disabled", true);
    }
}

function unlockDynamicAddress(lock, prefixPanel){
    if (lock){
        jQuery(prefixPanel + ".tytulInformatykDynamiczna").prop("disabled", false);
        jQuery(prefixPanel + ".dynamicDeviceSupportContact").prop("disabled", false);
        jQuery(prefixPanel + ".dynamicDeviceSupportSurname").prop("disabled", false);
        jQuery(prefixPanel + ".dynamicDeviceSupportName").prop("disabled", false);
    }  else {
        jQuery(prefixPanel + ".tytulInformatykDynamiczna").val('').prop("disabled", true);
        jQuery(prefixPanel + ".dynamicDeviceSupportContact").val('').prop("disabled", true);
        jQuery(prefixPanel + ".dynamicDeviceSupportSurname").val('').prop("disabled", true);
        jQuery(prefixPanel + ".dynamicDeviceSupportName").val('').prop("disabled", true);
    }
}

function setupNewPointPanelData(prefix, ppid, pid) {
	var prevPanelId = prefix+"\\["+ppid+"\\]\\.",
	    panelId = prefix+"\\["+pid+"\\]\\.",
        terminaloptions = {},
        technicalinformation = {},
        possetforselectedpoint = {},
        additionalequipment = {};
	var panelIdsContainer;
	if (prefix == "poses") {
    	panelIdsContainer = sameForEveryPointSourcePosId;
    }
    else {
    	panelIdsContainer = sameForEveryPointSourcePanelId;
    }

    var nip = jQuery("#akceptantNip").val(),
        globalMCC = jQuery("#globalMCC").val();

    var idPrefix = prefix + "\\[" + sameForEveryPointSourcePanelId['sameForEveryPoint'] + "\\]\\.",
        mmccode = jQuery("#" + idPrefix + "mccCode").val(),
        bankAccount = jQuery("#" +  idPrefix + "bankAccountNumber").val();

    if (Object.keys(possetforselectedpoint).length == 0) {
    	if (panelIdsContainer['possetforselectedpointSameForEveryPoint'] != -1) {
    		prevPanelId = prefix+"\\["+panelIdsContainer['possetforselectedpointSameForEveryPoint']+"\\]\\.";
    	
	        possetforselectedpoint['possetforselectedpointDialupType'] = jQuery("#"+prevPanelId+"possetforselectedpointDialupType").val();
	        possetforselectedpoint['dialupCount'] = jQuery("#"+prevPanelId+"dialupCount").val();
	        possetforselectedpoint['dialupPPCount'] = jQuery("#"+prevPanelId+"dialupPPCount").val();
	        possetforselectedpoint['dialupPrice'] = jQuery("#"+prevPanelId+"dialupPrice").val();
	        possetforselectedpoint['dialupPPPrice'] = jQuery("#"+prevPanelId+"dialupPPPrice").val();
	        possetforselectedpoint['dialupPricePreferencyjna'] = jQuery("#"+prevPanelId+"dialupPricePreferencyjna").val();
	        possetforselectedpoint['dialupPPPricePreferencyjna'] = jQuery("#"+prevPanelId+"dialupPPPricePreferencyjna").val();
	
	        possetforselectedpoint['possetforselectedpointVpnType'] = jQuery("#"+prevPanelId+"possetforselectedpointVpnType").val();
	        possetforselectedpoint['vpnCount'] = jQuery("#"+prevPanelId+"vpnCount").val();
	        possetforselectedpoint['vpnPPCount'] = jQuery("#"+prevPanelId+"vpnPPCount").val();
	        possetforselectedpoint['vpnPrice'] = jQuery("#"+prevPanelId+"vpnPrice").val();
	        possetforselectedpoint['vpnPPPrice'] = jQuery("#"+prevPanelId+"vpnPPPrice").val();
	        possetforselectedpoint['vpnPricePreferencyjna'] = jQuery("#"+prevPanelId+"vpnPricePreferencyjna").val();
	        possetforselectedpoint['vpnPPPricePreferencyjna'] = jQuery("#"+prevPanelId+"vpnPPPricePreferencyjna").val();
	
	        possetforselectedpoint['possetforselectedpointSslType'] = jQuery("#"+prevPanelId+"possetforselectedpointSslType").val();
	        possetforselectedpoint['sslCount'] = jQuery("#"+prevPanelId+"sslCount").val();
	        possetforselectedpoint['sslPPCount'] = jQuery("#"+prevPanelId+"sslPPCount").val();
	        possetforselectedpoint['sslPrice'] = jQuery("#"+prevPanelId+"sslPrice").val();
	        possetforselectedpoint['sslPPPrice'] = jQuery("#"+prevPanelId+"sslPPPrice").val();
	        possetforselectedpoint['sslPricePreferencyjna'] = jQuery("#"+prevPanelId+"sslPricePreferencyjna").val();
	        possetforselectedpoint['sslPPPricePreferencyjna'] = jQuery("#"+prevPanelId+"sslPPPricePreferencyjna").val();
	
	        possetforselectedpoint['possetforselectedpointWifiType'] = jQuery("#"+prevPanelId+"possetforselectedpointWifiType").val();
	        possetforselectedpoint['wifiCount'] = jQuery("#"+prevPanelId+"wifiCount").val();
	        possetforselectedpoint['wifiPrice'] = jQuery("#"+prevPanelId+"wifiPrice").val();
	        possetforselectedpoint['wifiPricePreferencyjna'] = jQuery("#"+prevPanelId+"wifiPricePreferencyjna").val();
	
	        possetforselectedpoint['possetforselectedpointPinpadType'] = jQuery("#"+prevPanelId+"possetforselectedpointPinpadType").val();
	        possetforselectedpoint['pinpadCount'] = jQuery("#"+prevPanelId+"pinpadCount").val();
	        possetforselectedpoint['pinpadPrice'] = jQuery("#"+prevPanelId+"pinpadPrice").val();
	        possetforselectedpoint['pinpadPricePreferencyjna'] = jQuery("#"+prevPanelId+"pinpadPricePreferencyjna").val();
	
	        possetforselectedpoint['possetforselectedpointGprsType'] = jQuery("#"+prevPanelId+"possetforselectedpointGprsType").val();
	        possetforselectedpoint['gprsCount'] = jQuery("#"+prevPanelId+"gprsCount").val();
	        possetforselectedpoint['gprsPPCount'] = jQuery("#"+prevPanelId+"gprsPPCount").val();
	        possetforselectedpoint['gprsPrice'] = jQuery("#"+prevPanelId+"gprsPrice").val();
	        possetforselectedpoint['gprsPPPrice'] = jQuery("#"+prevPanelId+"gprsPPPrice").val();
	        possetforselectedpoint['gprsPricePreferencyjna'] = jQuery("#"+prevPanelId+"gprsPricePreferencyjna").val();
	        possetforselectedpoint['gprsPPPricePreferencyjna'] = jQuery("#"+prevPanelId+"gprsPPPricePreferencyjna").val();
    	}
	}

    if (Object.keys(technicalinformation).length == 0) {
    	if (panelIdsContainer['technicalinformationSameForEveryPoint'] != -1) {
    		prevPanelId = prefix+"\\["+panelIdsContainer['technicalinformationSameForEveryPoint']+"\\]\\.";
    		
	        technicalinformation['dayCloseFrom'] = jQuery("#"+prevPanelId+"dayCloseFrom").val();
	        technicalinformation['dayCloseTo'] = jQuery("#"+prevPanelId+"dayCloseTo").val();
	        technicalinformation['plannedInstallationDate'] = jQuery("#"+prevPanelId+"plannedInstallationDate").val();
	        technicalinformation['additionalNotes'] = jQuery("#"+prevPanelId+"additionalNotes").val();
    	}
    }

    if (Object.keys(terminaloptions).length == 0) {
    	if (panelIdsContainer['terminaloptionsSameForEveryPoint'] != -1) {
    		prevPanelId = prefix+"\\["+panelIdsContainer['terminaloptionsSameForEveryPoint']+"\\]\\.";
    		
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
    }

    if (Object.keys(additionalequipment).length == 0) {
    	if (panelIdsContainer['additionalequipmentSameForEveryPoint'] != -1) {
    		prevPanelId = prefix+"\\["+panelIdsContainer['additionalequipmentSameForEveryPoint']+"\\]\\.";
		
			additionalequipment['bazaCount'] = jQuery("#"+prevPanelId+"bazaCount").val();
	        additionalequipment['routerCount'] = jQuery("#"+prevPanelId+"routerCount").val();
	        additionalequipment['cardReaderCount'] = jQuery("#"+prevPanelId+"cardReaderCount").val();
	        additionalequipment['simCardCount'] = jQuery("#"+prevPanelId+"simCardCount").val();
	        additionalequipment['simCardType'] = jQuery("#"+prevPanelId+"simCardType").val();
	        additionalequipment['otherAdditionalDevice'] = jQuery("#"+prevPanelId+"otherAdditionalDevice").val();
	        additionalequipment['otherAdditionalDeviceCount'] = jQuery("#"+prevPanelId+"otherAdditionalDeviceCount").val();
    	}
    }

    jQuery("#"+panelId+"nip").val(nip).keyup();
    jQuery("#"+panelId+"mccCode").val(globalMCC).keyup();

    if (panelId != prevPanelId) {
        if (panelIdsContainer['sameForEveryPoint'] != -1) {
            jQuery("#"+panelId+"mccCode").val(mmccode).keyup();
            jQuery("#"+panelId+"bankAccountNumber").val(bankAccount).keyup();
            jQuery("#"+panelId+"sameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", true);
        }

        if (panelIdsContainer['possetforselectedpointSameForEveryPoint'] != -1) {
            jQuery("#"+panelId+"possetforselectedpointDialupType").val(possetforselectedpoint['possetforselectedpointDialupType']);
            jQuery("#"+panelId+"dialupCount").val(possetforselectedpoint['dialupCount']);
            jQuery("#"+panelId+"dialupPPCount").val(possetforselectedpoint['dialupPPCount']);
            jQuery("#"+panelId+"dialupPrice").val(possetforselectedpoint['dialupPrice']);
            jQuery("#"+panelId+"dialupPPPrice").val(possetforselectedpoint['dialupPPPrice']);
            jQuery("#"+panelId+"dialupPricePreferencyjna").val(possetforselectedpoint['dialupPricePreferencyjna']);
            jQuery("#"+panelId+"dialupPPPricePreferencyjna").val(possetforselectedpoint['dialupPPPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointVpnType").val(possetforselectedpoint['possetforselectedpointVpnType']);
            jQuery("#"+panelId+"vpnCount").val(possetforselectedpoint['vpnCount']);
            jQuery("#"+panelId+"vpnPPCount").val(possetforselectedpoint['vpnPPCount']);
            jQuery("#"+panelId+"vpnPrice").val(possetforselectedpoint['vpnPrice']);
            jQuery("#"+panelId+"vpnPPPrice").val(possetforselectedpoint['vpnPPPrice']);
            jQuery("#"+panelId+"vpnPricePreferencyjna").val(possetforselectedpoint['vpnPricePreferencyjna']);
            jQuery("#"+panelId+"vpnPPPricePreferencyjna").val(possetforselectedpoint['vpnPPPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointSslType").val(possetforselectedpoint['possetforselectedpointSslType']);
            jQuery("#"+panelId+"sslCount").val(possetforselectedpoint['sslCount']);
            jQuery("#"+panelId+"sslPPCount").val(possetforselectedpoint['sslPPCount']);
            jQuery("#"+panelId+"sslPrice").val(possetforselectedpoint['sslPrice']);
            jQuery("#"+panelId+"sslPPPrice").val(possetforselectedpoint['sslPPPrice']);
            jQuery("#"+panelId+"sslPricePreferencyjna").val(possetforselectedpoint['sslPricePreferencyjna']);
            jQuery("#"+panelId+"sslPPPricePreferencyjna").val(possetforselectedpoint['sslPPPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointWifiType").val(possetforselectedpoint['possetforselectedpointWifiType']);
            jQuery("#"+panelId+"wifiCount").val(possetforselectedpoint['wifiCount']);
            jQuery("#"+panelId+"wifiPrice").val(possetforselectedpoint['wifiPrice']);
            jQuery("#"+panelId+"wifiPricePreferencyjna").val(possetforselectedpoint['wifiPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointGprsType").val(possetforselectedpoint['possetforselectedpointGprsType']);
            jQuery("#"+panelId+"gprsCount").val(possetforselectedpoint['gprsCount']);
            jQuery("#"+panelId+"gprsPPCount").val(possetforselectedpoint['gprsPPCount']);
            jQuery("#"+panelId+"gprsPrice").val(possetforselectedpoint['gprsPrice']);
            jQuery("#"+panelId+"gprsPPPrice").val(possetforselectedpoint['gprsPPPrice']);
            jQuery("#"+panelId+"gprsPricePreferencyjna").val(possetforselectedpoint['gprsPricePreferencyjna']);
            jQuery("#"+panelId+"gprsPPPricePreferencyjna").val(possetforselectedpoint['gprsPPPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointPinpadType").val(possetforselectedpoint['possetforselectedpointPinpadType']);
            jQuery("#"+panelId+"pinpadCount").val(possetforselectedpoint['pinpadCount']);
            jQuery("#"+panelId+"pinpadPrice").val(possetforselectedpoint['pinpadPrice']);
            jQuery("#"+panelId+"pinpadPricePreferencyjna").val(possetforselectedpoint['pinpadPricePreferencyjna']);

            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", true);
        }

        if (panelIdsContainer['technicalinformationSameForEveryPoint'] != -1) {
            jQuery("#"+panelId+"dayCloseFrom").val(technicalinformation['dayCloseFrom']).keyup();
            jQuery("#"+panelId+"dayCloseTo").val(technicalinformation['dayCloseTo']).keyup();
            jQuery("#"+panelId+"plannedInstallationDate").val(technicalinformation['plannedInstallationDate']).keyup();
            jQuery("#"+panelId+"additionalNotes").val(technicalinformation['additionalNotes']).keyup();
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("disabled", true);
        }

        if (panelIdsContainer['terminaloptionsSameForEveryPoint'] != -1) {
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

        if (panelIdsContainer['additionalequipmentSameForEveryPoint'] != -1) {
            jQuery("#"+panelId+"bazaCount").val(additionalequipment['bazaCount']);
            jQuery("#"+panelId+"routerCount").val(additionalequipment['routerCount']);
            jQuery("#"+panelId+"cardReaderCount").val(additionalequipment['cardReaderCount']);
            jQuery("#"+panelId+"simCardCount").val(additionalequipment['simCardCount']);
            jQuery("#"+panelId+"simCardType").val(additionalequipment['simCardType']);
            jQuery("#"+panelId+"otherAdditionalDevice").val(additionalequipment['otherAdditionalDevice']);
            jQuery("#"+panelId+"otherAdditionalDeviceCount").val(additionalequipment['otherAdditionalDeviceCount']);
            jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("disabled", true);
        }
    }

    var dialupType = jQuery("#"+panelId+"possetforselectedpointDialupType"),
        vpnType = jQuery("#"+panelId+"possetforselectedpointVpnType"),
        sslType = jQuery("#"+panelId+"possetforselectedpointSslType"),
        wifiType = jQuery("#"+panelId+"possetforselectedpointWifiType"),
        gprsType = jQuery("#"+panelId+"possetforselectedpointGprsType");

    if(dialupType.val() == ''){
        dialupType.closest('tr').find('input.float-number').removeAttr('value');
    }

    if(vpnType.val() == ''){
        vpnType.closest('tr').find('input.float-number').removeAttr('value');
    }

    if(sslType.val() == ''){
        sslType.closest('tr').find('input.float-number').removeAttr('value');
    }

    if(wifiType.val() == ''){
        wifiType.closest('tr').find('input.float-number').removeAttr('value');
    }

    if(gprsType.val() == ''){
        gprsType.closest('tr').find('input.float-number').removeAttr('value');
    }

    if(gprsType.val() === ""){
        setRequiredForSimCardForPanelId(false, panelId)
    } else {
        setRequiredForSimCardForPanelId(true, panelId)
    }

    verifyBaseVisibility(gprsType.val(),panelId)
}

function verifyBaseVisibility(value, panelId){

    var str = panelId.replace(".","")
    if(value == "Verifone Vx670 GPRS"){
        $j('tr[name="'+str+'.trBase"]').show()
    }
    else{
        $j('tr[name="'+str+'.trBase"]').hide()
    }
}

function setRequiredForSimCardForPanelId(isRequired, panelId){
    if(isRequired){
        jQuery("#"+panelId+"simCardType").attr("required", true);
        jQuery("#"+panelId+"simCardCount").attr("required", true);
    } else {
        jQuery("#"+panelId+"simCardType").removeAttr("required");
        jQuery("#"+panelId+"simCardCount").removeAttr("required");
    }
}

function clearNewPointDataAfterParentDeletion(prefix, ppid, pid) {
	var panelIdsContainer;
	if (prefix == "poses") {
    	panelIdsContainer = sameForEveryPointSourcePosId;
    }
    else {
    	panelIdsContainer = sameForEveryPointSourcePanelId;
    }
	
	if (panelIdsContainer['sameForEveryPoint'] == pid) {
		panelIdsContainer['sameForEveryPoint'] = -1;
	}
	
	if (panelIdsContainer['possetforselectedpointSameForEveryPoint'] == pid) {
		panelIdsContainer['possetforselectedpointSameForEveryPoint'] = -1;
	}
	
	if (panelIdsContainer['technicalinformationSameForEveryPoint'] == pid) {
		panelIdsContainer['technicalinformationSameForEveryPoint'] = -1;
	}
	
	if (panelIdsContainer['terminaloptionsSameForEveryPoint'] == pid) {
		panelIdsContainer['terminaloptionsSameForEveryPoint'] = -1;
	}
	
	if (panelIdsContainer['additionalequipmentSameForEveryPoint'] == pid) {
		panelIdsContainer['additionalequipmentSameForEveryPoint'] = -1;
	}
	
	for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
        if (i != pid) {
        	var panelId = prefix + "\\["+i+"\\]\\.";
        	
        	if (panelIdsContainer['sameForEveryPoint'] == -1) {
        		jQuery("#"+panelId+"sameForEveryPoint").prop("checked", false);
                jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", false);
        	}
        	
        	if (panelIdsContainer['possetforselectedpointSameForEveryPoint'] == -1) {
        		jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", false);
                jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", false);
        	}
        	
        	if (panelIdsContainer['technicalinformationSameForEveryPoint'] == -1) {
        		jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("checked", false);
                jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("disabled", false);
        	}
        	
        	if (panelIdsContainer['terminaloptionsSameForEveryPoint'] == -1) {
        		jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("checked", false);
                jQuery("#"+panelId+"terminaloptionsSameForEveryPoint").prop("disabled", false);
        	}
        	
        	if (panelIdsContainer['additionalequipmentSameForEveryPoint'] == -1) {
        		jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("checked", false);
                jQuery("#"+panelId+"additionalequipmentSameForEveryPoint").prop("disabled", false);
        	}
        }
    }
}

function clearNewPointData(prefix, ppid, pid) {
	var panelIdsContainer;
	var prevPanelId = prefix+"\\["+ppid+"\\]\\.";
	var panelId = prefix+"\\["+pid+"\\]\\.";
	if (prefix == "poses") {
    	panelIdsContainer = sameForEveryPointSourcePosId;
    }
    else {
    	panelIdsContainer = sameForEveryPointSourcePanelId;
    }
	
	if (panelId != prevPanelId) {
        if (panelIdsContainer['sameForEveryPoint'] == -1) {
            jQuery("#"+panelId+"nip").val("");
            jQuery("#"+panelId+"mccCode").val("");
            jQuery("#"+panelId+"bussinessTypeInPractice").val("");
            jQuery("#"+panelId+"bankAccountNumber").val("");
            jQuery("#"+panelId+"bankName").val("");
            jQuery("#"+panelId+"sameForEveryPoint").prop("checked", false);
            jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", false);
        }

        if (panelIdsContainer['possetforselectedpointSameForEveryPoint'] == -1) {
            jQuery("#"+panelId+"possetforselectedpointDialupType").val("");
            jQuery("#"+panelId+"dialupCount").val("");
            jQuery("#"+panelId+"dialupPPCount").val("");
            jQuery("#"+panelId+"dialupPrice").val("");
            jQuery("#"+panelId+"dialupPPPrice").val("");
            jQuery("#"+panelId+"possetforselectedpointVpnType").val("");
            jQuery("#"+panelId+"vpnCount").val("");
            jQuery("#"+panelId+"vpnPPCount").val("");
            jQuery("#"+panelId+"vpnPrice").val("");
            jQuery("#"+panelId+"vpnPPPrice").val("");
            jQuery("#"+panelId+"possetforselectedpointSslType").val("");
            jQuery("#"+panelId+"sslCount").val("");
            jQuery("#"+panelId+"sslPPCount").val("");
            jQuery("#"+panelId+"sslPrice").val("");
            jQuery("#"+panelId+"sslPPPrice").val("");
            jQuery("#"+panelId+"possetforselectedpointWifiType").val("");
            jQuery("#"+panelId+"wifiCount").val("");
            jQuery("#"+panelId+"wifiPPCount").val("");
            jQuery("#"+panelId+"wifiPrice").val("");
            jQuery("#"+panelId+"wifiPPPrice").val("");
            jQuery("#"+panelId+"possetforselectedpointGprsType").val("");
            jQuery("#"+panelId+"gprsCount").val("");
            jQuery("#"+panelId+"gprsPPCount").val("");
            jQuery("#"+panelId+"gprsPrice").val("");
            jQuery("#"+panelId+"gprsPPPrice").val("");
            jQuery("#"+panelId+"baseCount").val("");
            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", false);
            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", false);
        }

        if (panelIdsContainer['technicalinformationSameForEveryPoint'] == -1) {
            jQuery("#"+panelId+"dayCloseFrom").val("");
            jQuery("#"+panelId+"dayCloseTo").val("");
            jQuery("#"+panelId+"plannedInstallationDate").val("");
            jQuery("#"+panelId+"additionalNotes").val("");
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("checked", false);
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("disabled", false);
        }

        if (panelIdsContainer['terminaloptionsSameForEveryPoint'] == -1) {
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

        if (panelIdsContainer['additionalequipmentSameForEveryPoint'] == -1) {
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

function setupNewPosPanelHandlers(panelId, prefix) {
    var prefixPanel = "#"+prefix+"\\["+panelId+"\\]\\";

    jQuery(prefixPanel + ".wifiCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".vpnCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val());
        unlockStaticAddress(lock, prefixPanel);
        unlockDynamicAddress(testNumber(e.target.value), prefixPanel);
    })
    jQuery(prefixPanel + ".vpnCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val());
        unlockStaticAddress(lock, prefixPanel);
    })
    jQuery(prefixPanel + ".sslCount").on( "blur", function(e){
        var lock = testNumber(e.target.value) || testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".vpnCount").val());
        unlockStaticAddress(lock, prefixPanel);
    })

    unlockStaticAddress(testNumber(jQuery(prefixPanel + ".wifiCount").val()) || testNumber(jQuery(prefixPanel + ".vpnCount").val()) || testNumber(jQuery(prefixPanel + ".sslCount").val()), prefixPanel);
    unlockDynamicAddress(testNumber(jQuery(prefixPanel + ".wifiCount").val()), prefixPanel);

    addDateHandlers(prefixPanel);

    sameForEveryPoint(prefixPanel + ".sameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".possetforselectedpointSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".technicalinformationSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".terminaloptionsSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".additionalequipmentSameForEveryPoint", prefix, panelId);
}

function addDateHandlers(prefixPanel){
    var dayCloseFrom = jQuery(prefixPanel + ".dayCloseFrom"),
        dayCloseTo = jQuery(prefixPanel + ".dayCloseTo");

    jQuery(prefixPanel + ".plannedInstallationDate").datepicker({ dateFormat: 'yy-mm-dd', minDate: 0 });

    dayCloseFrom.timepicker({
        controlType: 'select',
        timeFormat: 'HH:mm',
        onClose: function(){
            onCloseDayCloseFrom(dayCloseFrom, dayCloseTo);
        }
    });

    dayCloseTo.timepicker({
        controlType: 'select',
        timeFormat: 'HH:mm',
        onClose: function(){
            onCloseDayCloseTo(dayCloseTo)
        }
    });

    dayCloseTo.val('23:59'); //default value
}

function onCloseDayCloseTo(dayCloseTo) {
    var hours = jQuery("[data-unit='hour']").val(),
        minutes = jQuery("[data-unit='minute']").val(),
        selectedDate = hours+":"+minutes;

    dayCloseTo.val(selectedDate);
}

function onCloseDayCloseFrom(dayCloseFrom, dayCloseTo) {
    var dayCloseToValue = dayCloseTo.val(),
        dayCloseFromValue = dayCloseFrom.val();
    if (dayCloseToValue != '') {
        var dayCloseFromDate = dayCloseFrom.datetimepicker('getDate'),
            testEndDate = dayCloseTo.datetimepicker('getDate');

        if (dayCloseFromDate > testEndDate) {
            dayCloseTo.val(dayCloseFromDate.getHours() + ":" + (dayCloseFromDate.getMinutes() < 10 ? '0' : '') + dayCloseFromDate.getMinutes())
            dayCloseToValue = dayCloseTo.val();
        }
    }
    //hack
    if(dayCloseFromValue !== ""){
        var minimumDate = getDateFromTime(dayCloseFromValue);
        dayCloseTo.timepicker('option', 'minDateTime', minimumDate);
        dayCloseTo.val(dayCloseToValue);
    }
}

function getDateFromTime(time){
    var hoursAndMinutes = time.split(":");
    var minimumDate = new Date();
    minimumDate.setHours(parseInt(hoursAndMinutes[0]));
    minimumDate.setMinutes(parseInt(hoursAndMinutes[1]));
    minimumDate.setSeconds(00);
    return minimumDate;
}

function sameForEveryPoint(selector, prefix, panelId){
    jQuery(selector).on("click", function(e) {
    	var index = selector.substring(selector.indexOf('.')+1, selector.length);
        var panel, panelJsId;
        var panelIdsContainer = sameForEveryPointSourcePanelId;

        /*if(jQuery(e.target).parents(".newPointPanel").length === 0){ //newPos
            panel = jQuery(e.target).parents(".newPosPanel");
        } else { //newPoint
            panel = jQuery(e.target).parents(".newPointPanel");
        }*/
        if (prefix == "poses") {
        	panel = jQuery(e.target).parents(".newPosPanel");
        	panelIdsContainer = sameForEveryPointSourcePosId;
        }
        else {
        	panel = jQuery(e.target).parents(".newPointPanel");
        	panelIdsContainer = sameForEveryPointSourcePanelId;
        }
        panelJsId = panel.attr('data-js-id');

    	if (e.target.checked) {
    		panelIdsContainer[index] = panelJsId;
    	}
    	else {
    		panelIdsContainer[index] = -1;
    	}

        for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
            if (i != panelId) {
                if (e.target.checked) {
                    setupNewPointPanelData(prefix, panelId, i);
                } else {
                    clearNewPointData(prefix, panelId, i);
                }
            }
        }
    });
}
