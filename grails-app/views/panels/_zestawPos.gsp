<fieldset class="subpanel-fieldset" style="clear: both;">
	<legend><g:message code="panel.newpos.posset.title" /></legend>
	<div class="subpanel-fieldset-centercontent" style="width: auto" >
		<label for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:checkBox id="${panelType}[${id}].possetforselectedpointSameForEveryPoint" name="${panelType}[${id}].zestawPosTakSamoDlaWszystkichPunktow" value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}" /><g:message code="panel.sameforeverypoint" /></label>
		<table class="vertical-center" style="width: auto;" >
            <thead>
            <tr>
                <td colspan="4"></td>
                <td colspan="2" class="text-center"><g:message code="panel.used.fees"/></td>
                <td colspan="2" class="text-center"><g:message code="panel.preferential.rate"/></td>
            </tr>
            <tr>
                <td colspan="4"></td>
                <td>Term./mies</td>
                <td>PP./mies.</td>
                <td>Term./mies</td>
                <td>PP./mies.</td>
            </tr>
            </thead>
        <tbody>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="DIALUP"
                                     id="${panelType}[${id}].possetforselectedpointDialupType"
                                     name="${panelType}[${id}].dialupTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.dialupTyp}"
                                     style="width: 220px" /></td>
            <td style="text-align: right;"><g:message code="panel.dialup" /></td>
            <td><g:textField name="${panelType}[${id}].dialupIlosc"
                             id="${panelType}[${id}].dialupCount"
                             value="${pointData?.dialupIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td><g:textField name="${panelType}[${id}].dialupPPIlosc"
                             id="${panelType}[${id}].dialupPPCount"
                             value="${pointData?.dialupPPIlosc}" readonly="readonly"
                             class="half-width integer-number"/> PP. szt.</td>

            <td><eumowy:textField name="${panelType}[${id}].dialupCena"
                             id="${panelType}[${id}].dialupPrice"
                             value="${pointData?.dialupCena}" 
                             validatable="${pointData}"
                             validateField="dialupCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].dialupPPCena"
                             id="${panelType}[${id}].dialupPPPrice"
                             value="${pointData?.dialupPPCena}" 
                             validatable="${pointData}" readonly="readonly"
                             validateField="dialupPPCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].dialupCenaPreferencyjna"
                             id="${panelType}[${id}].dialupPricePreferencyjna"
                             value="${pointData?.dialupCenaPreferencyjna}" 
                             validatable="${pointData}"
                             validateField="dialupCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].dialupPPCenaPreferencyjna"
                             id="${panelType}[${id}].dialupPPPricePreferencyjna"
                             value="${pointData?.dialupPPCenaPreferencyjna}" readonly="readonly"
                             class="half-width float-number pref-price"/> zł.</td>
        </tr>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="VPN"
                                     id="${panelType}[${id}].possetforselectedpointVpnType"
                                     name="${panelType}[${id}].vpnTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.vpnTyp}"
                                     style="width: 220px" /></td>
            <td style="text-align: right;"><g:message code="panel.vpn" /></td>
            <td><g:textField name="${panelType}[${id}].vpnIlosc"
                             id="${panelType}[${id}].vpnCount"
                             value="${pointData?.vpnIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td><g:textField name="${panelType}[${id}].vpnPPIlosc"
                             id="${panelType}[${id}].vpnPPCount"
                             value="${pointData?.vpnPPIlosc}" readonly="readonly"
                             class="half-width integer-number"/> PP. szt.</td>

            <td><eumowy:textField name="${panelType}[${id}].vpnCena"
                             id="${panelType}[${id}].vpnPrice"
                             value="${pointData?.vpnCena}" 
                             validatable="${pointData}"
                             validateField="vpnCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].vpnPPCena"
                             id="${panelType}[${id}].vpnPPPrice"
                             value="${pointData?.vpnPPCena}" 
                             validatable="${pointData}"
                             validateField="vpnPPCena" readonly="readonly"
                             class="half-width float-number normal-price"/> zł.</td>

            <td><eumowy:textField name="${panelType}[${id}].vpnCenaPreferencyjna"
                             id="${panelType}[${id}].vpnPricePreferencyjna"
                             value="${pointData?.vpnCenaPreferencyjna}" 
                             validatable="${pointData}"
                             validateField="vpnCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].vpnPPCenaPreferencyjna"
                             id="${panelType}[${id}].vpnPPPricePreferencyjna"
                             value="${pointData?.vpnPPCenaPreferencyjna}" 
                             validatable="${pointData}"
                             validateField="vpnPPCenaPreferencyjna" readonly="readonly"
                             class="half-width float-number pref-price"/> zł.</td>
        </tr>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="SSL"
                                     id="${panelType}[${id}].possetforselectedpointSslType"
                                     name="${panelType}[${id}].sslTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.sslTyp}"
                                     style="width: 220px" /></td>
            <td style="text-align: right;"><g:message code="panel.ssl" /></td>
            <td><g:textField name="${panelType}[${id}].sslIlosc"
                             id="${panelType}[${id}].sslCount"
                             value="${pointData?.sslIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td><g:textField name="${panelType}[${id}].sslPPIlosc"
                             id="${panelType}[${id}].sslPPCount"
                             value="${pointData?.sslPPIlosc}" readonly="readonly"
                             class="half-width integer-number"/> PP. szt.</td>
            <td><eumowy:textField name="${panelType}[${id}].sslCena"
                             id="${panelType}[${id}].sslPrice"
                             value="${pointData?.sslCena}" 
                             validatable="${pointData}"
                             validateField="sslCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].sslPPCena"
                             id="${panelType}[${id}].sslPPPrice"
                             value="${pointData?.sslPPCena}" 
                             validatable="${pointData}" readonly="readonly"
                             validateField="sslPPCena"
                             class="half-width float-number normal-price"/> zł.</td>

            <td><eumowy:textField name="${panelType}[${id}].sslCenaPreferencyjna"
                             id="${panelType}[${id}].sslPricePreferencyjna"
                             value="${pointData?.sslCenaPreferencyjna}" 
                             validatable="${pointData}"
                             validateField="sslCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].sslPPCenaPreferencyjna"
                             id="${panelType}[${id}].sslPPPricePreferencyjna"
                             value="${pointData?.sslPPCenaPreferencyjna}" 
                             validatable="${pointData}" readonly="readonly"
                             validateField="sslPPCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
        </tr>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="GPRS"
                                     id="${panelType}[${id}].possetforselectedpointGprsType"
                                     name="${panelType}[${id}].gprsTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                     style="width: 220px" class="gprsType"
                                     onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value)"

            /></td>
            <td style="text-align: right;"><g:message code="panel.gprs" /></td>
            <td><g:textField name="${panelType}[${id}].gprsIlosc"
                             id="${panelType}[${id}].gprsCount"
                             value="${pointData?.gprsIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td><g:textField name="${panelType}[${id}].gprsPPIlosc"
                             id="${panelType}[${id}].gprsPPCount"
                             value="${pointData?.gprsPPIlosc}" readonly="readonly"
                             class="half-width integer-number"/> PP. szt.</td>

            <td><eumowy:textField name="${panelType}[${id}].gprsCena"
                             id="${panelType}[${id}].gprsPrice"
                             value="${pointData?.gprsCena}" 
                             validatable="${pointData}" 
                             validateField="gprsCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].gprsPPCena"
                             id="${panelType}[${id}].gprsPPPrice"
                             value="${pointData?.gprsPPCena}" 
                             validatable="${pointData}" 
                             validateField="gprsPPCena" readonly="readonly"
                             class="half-width float-number normal-price"/> zł.</td>

            <td><eumowy:textField name="${panelType}[${id}].gprsCenaPreferencyjna"
                             id="${panelType}[${id}].gprsPricePreferencyjna"
                             value="${pointData?.gprsCenaPreferencyjna}" 
                             class="half-width float-number pref-price"/> zł.</td>
            <td><eumowy:textField name="${panelType}[${id}].gprsPPCenaPreferencyjna"
                             id="${panelType}[${id}].gprsPPPricePreferencyjna"
                             value="${pointData?.gprsPPCenaPreferencyjna}" 
                             validatable="${pointData}" readonly="readonly"
                             validateField="gprsPPCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
        </tr>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="PINPAD"
                                     id="${panelType}[${id}].possetforselectedpointPinpadType"
                                     name="${panelType}[${id}].pinPadTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.pinPadTyp}"
                                     style="width: 220px"

            /></td>
            <td style="text-align: right;"><g:message code="panel.pinpad" /></td>
            <td><g:textField name="${panelType}[${id}].pinPadIlosc"
                             id="${panelType}[${id}].pinpadCount"
                             value="${pointData?.pinPadIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td></td>

            <td><eumowy:textField name="${panelType}[${id}].pinPadCena"
                             id="${panelType}[${id}].pinpadPrice"
                             value="${pointData?.pinPadCena}" 
                             validatable="${pointData}" 
                             validateField="pinPadCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td></td>

            <td><eumowy:textField name="${panelType}[${id}].pinPadCenaPreferencyjna"
                             id="${panelType}[${id}].pinpadPricePreferencyjna"
                             value="${pointData?.pinPadCenaPreferencyjna}" 
                             validatable="${pointData}" 
                             validateField="pinPadCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
            <td></td>
        </tr>
        <tr>
            <td>typ <dict:typeSelect nip="${nip}"
                                     medium="WiFi"
                                     id="${panelType}[${id}].possetforselectedpointWifiType"
                                     name="${panelType}[${id}].wifiTyp" from="[]"
                                     valueMessagePrefix="" value="${pointData?.wifiTyp}"
                                     style="width: 220px" /></td>
            <td style="text-align: right;"><g:message code="panel.wifi" /></td>
            <td><g:textField name="${panelType}[${id}].wifiIlosc"
                             id="${panelType}[${id}].wifiCount"
                             value="${pointData?.wifiIlosc}" 
                             class="half-width integer-number"/> szt.</td>
            <td></td>

            <td><eumowy:textField name="${panelType}[${id}].wifiCena"
                             id="${panelType}[${id}].wifiPrice"
                             value="${pointData?.wifiCena}" 
                             validatable="${pointData}" 
                             validateField="wifiCena"
                             class="half-width float-number normal-price"/> zł.</td>
            <td></td>

            <td><eumowy:textField name="${panelType}[${id}].wifiCenaPreferencyjna"
                             id="${panelType}[${id}].wifiPricePreferencyjna"
                             value="${pointData?.wifiCenaPreferencyjna}" 
                             validatable="${pointData}" 
                             validateField="wifiCenaPreferencyjna"
                             class="half-width float-number pref-price"/> zł.</td>
            <td></td>
        </tr>
        </tbody>
		</table>
	</div>
</fieldset>