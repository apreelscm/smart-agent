<div id="posUsagePanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.pos.usage.title"/></div>
        <div style="text-align: center; padding-top: 20px;" class="centre">
            <div>
                <g:hiddenField name="isZestawPosOdplatneUzywanieShown" value="tak"/>
                <table class="vertical-center" >
                    <thead>
                        <tr>
                            <td colspan="4"></td><td colspan="2" style="text-align: center;"><g:message code="panel.used.fees"/></td>
                            <td colspan="2" style="text-align: center;"><g:message code="panel.preferential.rate"/></td>
                        </tr>
                        <tr>
                            <td colspan="4"></td><td><g:message code="panel.term.price.per.month"/></td>
                            <td><g:message code="panel.pp.price.per.month"/></td>
                            <td><g:message code="panel.term.price.per.month"/></td>
                            <td><g:message code="panel.pp.price.per.month"/></td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><g:message code="panel.type"/> <dict:typeSelect id="oplPOSDialUpTyp"  medium="DIALUP" name="oplPOSDialUpTyp" value="${data.oplPOSDialUpTyp}" style="width: 220px"/></td>
                            <td style="text-align: right;"><g:message code="panel.dialup"/></td>
                            <td><g:field name="oplPOSDialUpIlosc" type="text" class="integer-number" value="${data.oplPOSDialUpIlosc}" style="width: 50px"/> <g:message code="panel.unit"/></td>
                            <td><g:field type="text" class="integer-number" name="oplPOSDialUpIloscPP" value="${data.oplPOSDialUpIloscPP}" style="width: 50px"/> <g:message code="panel.pp.unit"/></td>
                            <td><g:field type="text" class="float-number oplPOSDialUpCena" name="oplPOSDialUpNormalneMies" value="${data.oplPOSDialUpNormalneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSDialUpCena" name="oplPOSDialUpNormalnePP" value="${data.oplPOSDialUpNormalnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSDialUpCena" name="oplPOSDialUpPreferencyjneMies" value="${data.oplPOSDialUpPreferencyjneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSDialUpCena" name="oplPOSDialUpPreferencyjnePP" value="${data.oplPOSDialUpPreferencyjnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                        </tr>
                        <tr>
                            <td><g:message code="panel.type"/> <dict:typeSelect id="oplPOSVPNTyp" medium="VPN" name="oplPOSVPNTyp" value="${data.oplPOSVPNTyp}" style="width: 220px" /></td>
                            <td style="text-align: right;"><g:message code="panel.vpn"/></td>
                            <td><g:field name="oplPOSVPNIlosc" type="text" class="integer-number" value="${data.oplPOSVPNIlosc}" style="width: 50px"/> <g:message code="panel.unit"/></td>
                            <td><g:field type="text" class="integer-number" name="oplPOSVPNIloscPP" value="${data.oplPOSVPNIloscPP}" style="width: 50px"/> <g:message code="panel.pp.unit"/></td>
                            <td><g:field type="text" class="float-number oplPOSVPNCena" name="oplPOSVPNNormalneMies" value="${data.oplPOSVPNNormalneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSVPNCena" name="oplPOSVPNNormalnePP" value="${data.oplPOSVPNNormalnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSVPNCena" name="oplPOSVPNPreferencyjneMies" value="${data.oplPOSVPNPreferencyjneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSVPNCena" name="oplPOSVPNPreferencyjnePP" value="${data.oplPOSVPNPreferencyjnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                        </tr>
                        <tr>
                            <td><g:message code="panel.type"/> <dict:typeSelect id="oplPOSSSLTyp"  medium="SSL" name="oplPOSSSLTyp" value="${data.oplPOSSSLTyp}" style="width: 220px" /></td>
                            <td style="text-align: right;"><g:message code="panel.ssl"/></td>
                            <td><g:field name="oplPOSSSLIlosc" type="text" class="integer-number" value="${data.oplPOSSSLIlosc}" style="width: 50px"/> <g:message code="panel.unit"/></td>
                            <td><g:field type="text" class="integer-number" name="oplPOSSSLIloscPP" value="${data.oplPOSSSLIloscPP}" style="width: 50px"/> <g:message code="panel.pp.unit"/></td>
                            <td><g:field type="text" class="float-number oplPOSSSLCena" name="oplPOSSSLNormalneMies" value="${data.oplPOSSSLNormalneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSSSLCena" name="oplPOSSSLNormalnePP" value="${data.oplPOSSSLNormalnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSSSLCena" name="oplPOSSSLPreferencyjneMies" value="${data.oplPOSSSLPreferencyjneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSSSLCena" name="oplPOSSSLPreferencyjnePP" value="${data.oplPOSSSLPreferencyjnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                        </tr>
                        <tr>
                            <td><g:message code="panel.type"/> <dict:typeSelect id="oplPOSWiFiTyp"  medium="WiFi" name="oplPOSWiFiTyp" value="${data.oplPOSWiFiTyp}" style="width: 220px" /></td>
                            <td style="text-align: right;"><g:message code="panel.wifi"/></td>
                            <td><g:field name="oplPOSWiFiIlosc" type="text" class="integer-number" value="${data.oplPOSWiFiIlosc}" style="width: 50px"/> <g:message code="panel.unit"/></td>
                            <td><g:field type="text" class="integer-number" name="oplPOSWiFiIloscPP" value="${data.oplPOSWiFiIloscPP}" style="width: 50px"/> <g:message code="panel.pp.unit"/></td>
                            <td><g:field type="text" class="float-number oplPOSWiFiCena" name="oplPOSWiFiNormalneMies" value="${data.oplPOSWiFiNormalneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSWiFiCena" name="oplPOSWiFiNormalnePP" value="${data.oplPOSWiFiNormalnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSWiFiCena"  name="oplPOSWiFiPreferencyjneMies" value="${data.oplPOSWiFiPreferencyjneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSWiFiCena" name="oplPOSWiFiPreferencyjnePP" value="${data.oplPOSWiFiPreferencyjnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                        </tr>
                        <tr>
                            <td><g:message code="panel.type"/> <dict:typeSelect id="oplPOSGPRSTyp"  medium="GPRS" name="oplPOSGPRSTyp" value="${data.oplPOSGPRSTyp}" style="width: 220px" /></td>
                            <td style="text-align: right;"><g:message code="panel.gprs"/></td>
                            <td><g:field name="oplPOSGPRSIlosc" type="text" class="integer-number" value="${data.oplPOSGPRSIlosc}" style="width: 50px"/> <g:message code="panel.unit"/></td>
                            <td><g:field type="text" class="integer-number" name="oplPOSGPRSIloscPP" value="${data.oplPOSGPRSIloscPP}" style="width: 50px"/> <g:message code="panel.pp.unit"/></td>
                            <td><g:field type="text" class="float-number oplPOSGPRSCena" name="oplPOSGPRSNormalneMies" value="${data.oplPOSGPRSNormalneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSGPRSCena" name="oplPOSGPRSNormalnePP" value="${data.oplPOSGPRSNormalnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSGPRSCena"  name="oplPOSGPRSPreferencyjneMies" value="${data.oplPOSGPRSPreferencyjneMies}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                            <td><g:field type="text" class="float-number oplPOSGPRSCena" name="oplPOSGPRSPreferencyjnePP" value="${data.oplPOSGPRSPreferencyjnePP}" style="width: 50px"/> <g:message code="panel.polish.currency"/></td>
                        </tr>
                        <tr id="trBase">
                            <td></td>
                            <td style="text-align: right;"><g:message code="panel.base"/></td>
                            <td><g:field id="oplPOSBaza" name="oplPOSBaza" type="text" class="integer-number" value="${data.oplPOSBaza}" style="width: 50px"/> <g:message code="panel.unit"/> </td>
                            <td colspan="5"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#oplPOSGPRSTyp").change(verifyBaseVisibility)
        verifyBaseVisibility()

        if(jQuery("#oplPOSDialUpTyp").val() == ""){
            jQuery("input.oplPOSDialUpCena").val("");
        }
        if(jQuery("#oplPOSVPNTyp").val() == ""){
            jQuery("input.oplPOSVPNCena").val("");
        }
        if(jQuery("#oplPOSSSLTyp").val() == ""){
            jQuery("input.oplPOSSSLCena").val("");
        }
        if(jQuery("#oplPOSWiFiTyp").val() == ""){
            jQuery("input.oplPOSWiFiCena").val("");
        }
        if(jQuery("#oplPOSGPRSTyp").val() == ""){
            jQuery("input.oplPOSGPRSCena").val("");
        }
    });

    function verifyBaseVisibility(){
        var value = jQuery("#oplPOSGPRSTyp").val().toString()
        if(value == "Verifone Vx670 GPRS"){
            jQuery("#trBase").show()
        }
        else{
            jQuery("#trBase").hide()
        }
    }
</r:script>
