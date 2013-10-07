var globalPanelCount = 0;
var globalPanelPosCount = 0;

function getCurrentTerminalCount(prefix) {

	var counter = globalPanelPosCount

	for(var i = 0; i < globalPanelCount; i++) {
		var prefixPanel = "#"+prefix+"\\["+i+"\\]\\"
		counter += jQuery(prefixPanel + ".dialupCount").val();
		counter += jQuery(prefixPanel + ".vpnCount").val();
		counter += jQuery(prefixPanel + ".sslCount").val();
		counter += jQuery(prefixPanel + ".wifiCount").val();
		counter += jQuery(prefixPanel + ".gprsCount").val();
		counter += jQuery(prefixPanel + ".baseCount").val();
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

function setupNewPointPanelHandlers(prevPanelId, panelId, prefix) {
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

    addDateHandlers(prefixPanel, prefix, panelId);

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
            jQuery(prefixPanel + ".dataforprintingAddressCity").val(jQuery("#akceptantMiasto").val()).keyup();
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
        jQuery(prefixPanel + ".contactAddressAddressCity").val(jQuery("#akceptantMiasto").val()).keyup();
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

    sameForEveryPoint(prefixPanel + ".sameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".possetforselectedpointSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".technicalinformationSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".terminaloptionsSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".additionalequipmentSameForEveryPoint", prefix, panelId);

    // pobieranie numeru PH do ph pozysk oraz opieka biznesowa
    jQuery(prefixPanel + ".phGain").val(jQuery("#pozyskujacyNumer").val());
    jQuery(prefixPanel + ".businessCare").val(jQuery("#pozyskujacyNumer").val());

    //});

}

function setupNewPointPanelData(prevPanelId, panelId) {

    var terminaloptions = {};
    var technicalinformation = {};
    var possetforselectedpoint = {};
    var additionalequipment = {};

    var nip = jQuery("#akceptantNip").val();
    var globalMCC = jQuery("#globalMCC").val();
    var mmccode = jQuery("#"+prevPanelId+"mccCode").val();
    var bankAccount = jQuery("#"+prevPanelId+"bankAccountNumber").val();

    if (Object.keys(possetforselectedpoint).length == 0) {
        possetforselectedpoint['possetforselectedpointDialupType'] = jQuery("#"+prevPanelId+"possetforselectedpointDialupType").val();
        possetforselectedpoint['dialupCount'] = jQuery("#"+prevPanelId+"dialupCount").val();
        possetforselectedpoint['dialupPPCount'] = jQuery("#"+prevPanelId+"dialupPPCount").val();
        possetforselectedpoint['dialupPrice'] = jQuery("#"+prevPanelId+"dialupPrice").val();
        possetforselectedpoint['dialupPPPrice'] = jQuery("#"+prevPanelId+"dialupPPPrice").val();
        possetforselectedpoint['possetforselectedpointVpnType'] = jQuery("#"+prevPanelId+"possetforselectedpointVpnType").val();
        possetforselectedpoint['vpnCount'] = jQuery("#"+prevPanelId+"vpnCount").val();
        possetforselectedpoint['vpnPPCount'] = jQuery("#"+prevPanelId+"vpnPPCount").val();
        possetforselectedpoint['vpnPrice'] = jQuery("#"+prevPanelId+"vpnPrice").val();
        possetforselectedpoint['vpnPPPrice'] = jQuery("#"+prevPanelId+"vpnPPPrice").val();
        possetforselectedpoint['possetforselectedpointSslType'] = jQuery("#"+prevPanelId+"possetforselectedpointSslType").val();
        possetforselectedpoint['sslCount'] = jQuery("#"+prevPanelId+"sslCount").val();
        possetforselectedpoint['sslPPCount'] = jQuery("#"+prevPanelId+"sslPPCount").val();
        possetforselectedpoint['sslPrice'] = jQuery("#"+prevPanelId+"sslPrice").val();
        possetforselectedpoint['sslPPPrice'] = jQuery("#"+prevPanelId+"sslPPPrice").val();
        possetforselectedpoint['possetforselectedpointWifiType'] = jQuery("#"+prevPanelId+"possetforselectedpointWifiType").val();
        possetforselectedpoint['wifiCount'] = jQuery("#"+prevPanelId+"wifiCount").val();
        possetforselectedpoint['wifiPPCount'] = jQuery("#"+prevPanelId+"wifiPPCount").val();
        possetforselectedpoint['wifiPrice'] = jQuery("#"+prevPanelId+"wifiPrice").val();
        possetforselectedpoint['wifiPPPrice'] = jQuery("#"+prevPanelId+"wifiPPPrice").val();
        possetforselectedpoint['possetforselectedpointGprsType'] = jQuery("#"+prevPanelId+"possetforselectedpointGprsType").val();
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

    jQuery("#"+panelId+"nip").val(nip).keyup();
    jQuery("#"+panelId+"mccCode").val(globalMCC).keyup();

    if (panelId != prevPanelId) {
        if (jQuery("#"+prevPanelId+"sameForEveryPoint").is(':checked')) {
            jQuery("#"+panelId+"mccCode").val(mmccode).keyup();
            jQuery("#"+panelId+"bankAccountNumber").val(bankAccount).keyup();
            jQuery("#"+panelId+"sameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"sameForEveryPoint").prop("disabled", true);
        }

        if (jQuery("#"+prevPanelId+"possetforselectedpointSameForEveryPoint").is(':checked')) {
            jQuery("#"+panelId+"possetforselectedpointDialupType").val(possetforselectedpoint['possetforselectedpointDialupType']);
            jQuery("#"+panelId+"dialupCount").val(possetforselectedpoint['dialupCount']);
            jQuery("#"+panelId+"dialupPPCount").val(possetforselectedpoint['dialupPPCount']);
            jQuery("#"+panelId+"dialupPrice").val(possetforselectedpoint['dialupPrice']);
            jQuery("#"+panelId+"dialupPPPrice").val(possetforselectedpoint['dialupPPPrice']);
            jQuery("#"+panelId+"possetforselectedpointVpnType").val(possetforselectedpoint['possetforselectedpointVpnType']);
            jQuery("#"+panelId+"vpnCount").val(possetforselectedpoint['vpnCount']);
            jQuery("#"+panelId+"vpnPPCount").val(possetforselectedpoint['vpnPPCount']);
            jQuery("#"+panelId+"vpnPrice").val(possetforselectedpoint['vpnPrice']);
            jQuery("#"+panelId+"vpnPPPrice").val(possetforselectedpoint['vpnPPPrice']);
            jQuery("#"+panelId+"possetforselectedpointSslType").val(possetforselectedpoint['possetforselectedpointSslType']);
            jQuery("#"+panelId+"sslCount").val(possetforselectedpoint['sslCount']);
            jQuery("#"+panelId+"sslPPCount").val(possetforselectedpoint['sslPPCount']);
            jQuery("#"+panelId+"sslPrice").val(possetforselectedpoint['sslPrice']);
            jQuery("#"+panelId+"sslPPPrice").val(possetforselectedpoint['sslPPPrice']);
            jQuery("#"+panelId+"possetforselectedpointWifiType").val(possetforselectedpoint['possetforselectedpointWifiType']);
            jQuery("#"+panelId+"wifiCount").val(possetforselectedpoint['wifiCount']);
            jQuery("#"+panelId+"wifiPPCount").val(possetforselectedpoint['wifiPPCount']);
            jQuery("#"+panelId+"wifiPrice").val(possetforselectedpoint['wifiPrice']);
            jQuery("#"+panelId+"wifiPPPrice").val(possetforselectedpoint['wifiPPPrice']);
            jQuery("#"+panelId+"possetforselectedpointGprsType").val(possetforselectedpoint['possetforselectedpointGprsType']);
            jQuery("#"+panelId+"gprsCount").val(possetforselectedpoint['gprsCount']);
            jQuery("#"+panelId+"gprsPPCount").val(possetforselectedpoint['gprsPPCount']);
            jQuery("#"+panelId+"gprsPrice").val(possetforselectedpoint['gprsPrice']);
            jQuery("#"+panelId+"gprsPPPrice").val(possetforselectedpoint['gprsPPPrice']);
            jQuery("#"+panelId+"baseCount").val(possetforselectedpoint['baseCount']);
            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"possetforselectedpointSameForEveryPoint").prop("disabled", true);
        }

        if (jQuery("#"+prevPanelId+"technicalinformationSameForEveryPoint").is(':checked')) {
            jQuery("#"+panelId+"dayCloseFrom").val(technicalinformation['dayCloseFrom']).keyup();
            jQuery("#"+panelId+"dayCloseTo").val(technicalinformation['dayCloseTo']).keyup();
            jQuery("#"+panelId+"plannedInstallationDate").val(technicalinformation['plannedInstallationDate']).keyup();
            jQuery("#"+panelId+"additionalNotes").val(technicalinformation['additionalNotes']).keyup();
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("checked", true);
            jQuery("#"+panelId+"technicalinformationSameForEveryPoint").prop("disabled", true);
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
    var prefixPanel = "#"+prefix+"\\["+panelId+"\\]\\";

    addDateHandlers(prefixPanel, prefix, panelId);

    sameForEveryPoint(prefixPanel + ".sameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".possetforselectedpointSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".technicalinformationSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".terminaloptionsSameForEveryPoint", prefix, panelId);
    sameForEveryPoint(prefixPanel + ".additionalequipmentSameForEveryPoint", prefix, panelId);
}

function addDateHandlers(prefixPanel, prefix, panelId){
    var dayCloseFrom = jQuery(prefixPanel + ".dayCloseFrom"),
        dayCloseTo = jQuery(prefixPanel + ".dayCloseTo"),
        dayCloseToDefault = new Date();

    dayCloseToDefault.setHours(23);
    dayCloseToDefault.setMinutes(59);

    jQuery(prefixPanel + ".plannedInstallationDate").datepicker({ dateFormat: 'yy-mm-dd', minDate: 0 });
    dayCloseFrom.timepicker({
        controlType: 'select',
        timeFormat: 'HH:mm',
        onClose: function(dateText, inst){
            onCloseDayCloseFrom(prefix, panelId, dayCloseFrom, dayCloseTo);
        },
        onSelect: function (selectedDateTime){
            onSelectDayCloseFrom(prefix, panelId, dayCloseTo);
        }
    });

    dayCloseTo.timepicker({
        controlType: 'select',
        timeFormat: 'HH:mm',
        onClose: function(dateText, inst){
            onCloseDayCloseTo(prefix, panelId, dayCloseTo)
        },
        onSelect: function (selectedDateTime) {}
    });

    for(var i = 0; i < dayCloseTo.length; i++){
        var dayCloseToItem = dayCloseTo[i],
            dayCloseToValue = dayCloseToItem.value;
        if(!dayCloseToValue || dayCloseToValue === ""){
            jQuery(dayCloseToItem).datetimepicker('setDate', dayCloseToDefault);
        }
    }
}

function onCloseDayCloseTo(prefix, panelId, dayCloseTo) {
    var hours = jQuery("[data-unit='hour']").val(),
        minutes = jQuery("[data-unit='minute']").val(),
        selectedDate = hours+":"+minutes;

    dayCloseTo.datetimepicker('setDate', getDateFromTime(selectedDate));
}

function onCloseDayCloseFrom(prefix, panelId, dayCloseFrom, dayCloseTo) {
    var dayCloseToValue = dayCloseTo.val();
    if (dayCloseTo.val() != '') {
        var dayCloseFromDate = dayCloseFrom.datetimepicker('getDate'),
            testEndDate = dayCloseTo.datetimepicker('getDate');
        if (dayCloseFromDate > testEndDate) {
            dayCloseTo.datetimepicker('setDate', dayCloseFromDate);
            dayCloseToValue = dayCloseTo.val();
        }
    }
    //hack
    if(dayCloseFrom.val() !== ""){
        var minimumDate = getDateFromTime(dayCloseFrom.val());
        dayCloseTo.datetimepicker('option', 'minDateTime', minimumDate);
        dayCloseTo.val(dayCloseToValue);
    }
}

function onSelectDayCloseFrom(prefix, panelId, dayCloseTo){
    var dayCloseToValue = dayCloseTo.val();

    dayCloseTo.val(dayCloseToValue);
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
        for(var i = 0; i < getGlobalPanelCount(prefix); i++) {
            if (i != panelId) {
                if (e.target.checked) {
                    setupNewPointPanelData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
                } else {
                    clearNewPointData(prefix+"\\["+panelId+"\\]\\.", prefix+"\\["+i+"\\]\\.");
                }
            }
        }
    });
}
