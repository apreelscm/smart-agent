<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.posset.for.selected.point.title"/>
    </legend>

    <div class="subpanel-fieldset-centercontent" style="width: auto">
        <label for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:message
                code="panel.sameforeverypoint"/></label>
        <g:checkBox id="${panelType}[${id}].possetforselectedpointSameForEveryPoint" class="float-left"
                    name="${panelType}[${id}].zestawPosTakSamoDlaWszystkichPunktow"
                    value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}"/>

        <table class="vertical-center" id="zestawPOSTable">
            <thead>
            <tr>
                <td colspan="2"></td>
                <td colspan="2" class="text-center"><g:message code="panel.used.fees"/></td>
            </tr>
            <tr>
                <td colspan="3"></td>
                <td><g:message code="panel.term.price.per.month"/></td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td colspan="4" class="posHeaderType"><g:message code="stationary.label"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="DIALUP" type="STATIONARY" isPINPad="false"
                                             id="${panelType}[${id}].possetforselectedpointDialupType"
                                             name="${panelType}[${id}].dialupTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.dialupTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.dialup"/></td>

                <td><g:textField name="${panelType}[${id}].dialupIlosc"
                                 id="${panelType}[${id}].dialupCount"
                                 value="${pointData?.dialupIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].dialupCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].dialupPrice"
                                 value="${pointData?.dialupCena}"/></td>
            </tr>
            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="DIALUP" type="STATIONARY" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointDialupPPType"
                                             name="${panelType}[${id}].dialupPPTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.dialupPPTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.dialup"/></td>

                <td><g:textField name="${panelType}[${id}].dialupPPIlosc"
                                 id="${panelType}[${id}].dialupPPCount"
                                 value="${pointData?.dialupPPIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].dialupPPCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].dialupPPPrice"
                                 value="${pointData?.dialupPPCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="VPN" type="STATIONARY" isPINPad="false"
                                             id="${panelType}[${id}].possetforselectedpointVpnType"
                                             name="${panelType}[${id}].vpnTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.vpnTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.vpn"/></td>

                <td><g:textField name="${panelType}[${id}].vpnIlosc"
                                 id="${panelType}[${id}].vpnCount"
                                 value="${pointData?.vpnIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>


                <td><g:textField name="${panelType}[${id}].vpnCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].vpnPrice"
                                 value="${pointData?.vpnCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="VPN" type="STATIONARY" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointVpnPPType"
                                             name="${panelType}[${id}].vpnPPTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.vpnPPTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.vpn"/></td>

                <td><g:textField name="${panelType}[${id}].vpnPPIlosc"
                                 id="${panelType}[${id}].vpnPPCount"
                                 value="${pointData?.vpnPPIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].vpnPPCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].vpnPPPrice"
                                 value="${pointData?.vpnPPCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="SSL" type="STATIONARY" isPINPad="false"
                                             id="${panelType}[${id}].possetforselectedpointSslType"
                                             name="${panelType}[${id}].sslTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.sslTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.ssl"/></td>

                <td><g:textField name="${panelType}[${id}].sslIlosc"
                                 id="${panelType}[${id}].sslCount"
                                 value="${pointData?.sslIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].sslCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].sslPrice"
                                 value="${pointData?.sslCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="SSL" type="STATIONARY" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointSslPPType"
                                             name="${panelType}[${id}].sslPPTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.sslPPTyp}"/></td>

                <td style="text-align: right;"><g:message code="panel.ssl"/></td>

                <td><g:textField name="${panelType}[${id}].sslPPIlosc"
                                 id="${panelType}[${id}].sslPPCount"
                                 value="${pointData?.sslPPIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].sslPPCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].sslPPPrice"
                                 value="${pointData?.sslPPCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="GPRS" type="STATIONARY" isPINPad="false"
                                             id="${panelType}[${id}].possetforselectedpointGprsType"
                                             name="${panelType}[${id}].gprsTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                             onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

                <td style="text-align: right;"><g:message code="panel.gprs"/></td>

                <td><g:textField name="${panelType}[${id}].gprsIlosc"
                                 id="${panelType}[${id}].gprsCount"
                                 value="${pointData?.gprsIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].gprsCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].gprsPrice"
                                 value="${pointData?.gprsCena}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="GPRS" type="STATIONARY" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointGprsPPType"
                                             name="${panelType}[${id}].gprsPPTyp" from="[]"
                                             valueMessagePrefix="" value="${pointData?.gprsPPTyp}"
                                             onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

                <td style="text-align: right;"><g:message code="panel.gprs"/></td>

                <td><g:textField name="${panelType}[${id}].gprsPPIlosc"
                                 id="${panelType}[${id}].gprsPPCount"
                                 value="${pointData?.gprsPPIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].gprsPPCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].gprsPPPrice"
                                 value="${pointData?.gprsPPCena}"/></td>
            </tr>

            <tr>
                <td colspan="4" class="posHeaderType"><g:message code="portable.label"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="GPRS" type="PORTABLE" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointGprsTypePortable"
                                             name="${panelType}[${id}].gprsTypPortable" from="[]"
                                             valueMessagePrefix="" value="${pointData?.gprsTypPortable}"
                                             onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

                <td style="text-align: right;"><g:message code="panel.gprs"/></td>

                <td><g:textField name="${panelType}[${id}].gprsIloscPortable"
                                 id="${panelType}[${id}].gprsCountPortable"
                                 value="${pointData?.gprsIloscPortable}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].gprsCenaPortable"
                                 class="decimal-number"
                                 id="${panelType}[${id}].gprsPricePortable"
                                 value="${pointData?.gprsCenaPortable}"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                    <dict:extendedTypeSelect medium="WiFi" type="PORTABLE" isPINPad="true"
                                             id="${panelType}[${id}].possetforselectedpointWifiTypePortable"
                                             name="${panelType}[${id}].wifiTypPortable" from="[]"
                                             valueMessagePrefix="" value="${pointData?.wifiTypPortable}"/></td>

                <td style="text-align: right;"><g:message code="panel.wifi"/></td>

                <td><g:textField name="${panelType}[${id}].wifiIloscPortable"
                                 id="${panelType}[${id}].wifiCountPortable"
                                 value="${pointData?.wifiIloscPortable}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].wifiCenaPortable"
                                 class="decimal-number"
                                 id="${panelType}[${id}].wifiPricePortable"
                                 value="${pointData?.wifiCenaPortable}"/></td>
            </tr>

            <tr>
                <td colspan="4" class="posHeaderType"><g:message code="pinpad.label"/></td>
            </tr>

            <tr>
                <td class="posTypeColumn"><g:message code="type.label"/>
                <dict:typeSelect medium="PINPAD"
                                 id="${panelType}[${id}].possetforselectedpointPinpadType"
                                 name="${panelType}[${id}].pinPadTyp" from="[]"
                                 valueMessagePrefix="" value="${pointData?.pinPadTyp}"
                                 onchange="verifyBaseVisibility(this.value,${id})"/>
                </td>
                <td style="text-align: right;"><g:message code="panel.pinpad"/></td>
                <td><g:textField name="${panelType}[${id}].pinPadIlosc"
                                 id="${panelType}[${id}].pinpadCount"
                                 value="${pointData?.pinPadIlosc}"
                                 class="half-width integer-number"/> <g:message code="panel.unit"/></td>

                <td><g:textField name="${panelType}[${id}].pinPadCena"
                                 class="decimal-number"
                                 id="${panelType}[${id}].pinpadPrice"
                                 value="${pointData?.pinPadCena}"/></td>
            </tr>
            </tbody>
        </table>
    </div>
</fieldset>