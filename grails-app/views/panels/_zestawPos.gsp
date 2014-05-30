<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.posset.for.selected.point.title"/>
    </legend>

    <div class="subpanel-fieldset-centercontent" style="width: auto">
        <label for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:message code="panel.sameforeverypoint"/></label>
        <g:checkBox id="${panelType}[${id}].possetforselectedpointSameForEveryPoint" class="float-left"
            name="${panelType}[${id}].zestawPosTakSamoDlaWszystkichPunktow"
            value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}"/>

        <table class="vertical-center">
            <thead>
                <tr>
                    <td colspan="4"></td>
                    <td colspan="2" class="text-center"><g:message code="panel.used.fees"/></td>
                </tr>
                <tr>
                    <td colspan="4"></td>
                    <td><g:message code="panel.term.price.per.month"/></td>
                    <td><g:message code="panel.pp.price.per.month"/></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><g:message code="type.label"/> <dict:typeSelect nip="${nip}"
                                                                        medium="DIALUP"
                                                                        id="${panelType}[${id}].possetforselectedpointDialupType"
                                                                        name="${panelType}[${id}].dialupTyp" from="[]"
                                                                        valueMessagePrefix="" value="${pointData?.dialupTyp}"
                                                                        style="width: 220px"/></td>
                    <td style="text-align: right;"><g:message code="panel.dialup"/></td>
                    <td><g:textField name="${panelType}[${id}].dialupIlosc"
                                     id="${panelType}[${id}].dialupCount"
                                     value="${pointData?.dialupIlosc}"
                                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>
                    <td><g:textField name="${panelType}[${id}].dialupPPIlosc"
                                     id="${panelType}[${id}].dialupPPCount"
                                     value="${pointData?.dialupPPIlosc}" readonly="readonly"
                                     class="half-width integer-number"/> <g:message code="panel.pp.unit"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].dialupCena"
                                              id="${panelType}[${id}].dialupPrice"
                                              value="${pointData?.dialupCena}"
                                              validatable="${pointData}"
                                              validateField="dialupCena"
                                              class="half-width float-number"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].dialupPPCena"
                                              id="${panelType}[${id}].dialupPPPrice"
                                              value="${pointData?.dialupPPCena}"
                                              validatable="${pointData}"
                                              validateField="dialupPPCena" readonly="readonly"
                                              class="half-width float-number"/></td>
                </tr>

                <tr>
                    <td><g:message code="type.label"/> <dict:typeSelect nip="${nip}"
                                                                        medium="VPN"
                                                                        id="${panelType}[${id}].possetforselectedpointVpnType"
                                                                        name="${panelType}[${id}].vpnTyp" from="[]"
                                                                        valueMessagePrefix="" value="${pointData?.vpnTyp}"
                                                                        style="width: 220px"/></td>
                    <td style="text-align: right;"><g:message code="panel.vpn"/></td>
                    <td><g:textField name="${panelType}[${id}].vpnIlosc"
                                     id="${panelType}[${id}].vpnCount"
                                     value="${pointData?.vpnIlosc}"
                                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>
                    <td><g:textField name="${panelType}[${id}].vpnPPIlosc"
                                     id="${panelType}[${id}].vpnPPCount"
                                     value="${pointData?.vpnPPIlosc}" readonly="readonly"
                                     class="half-width integer-number"/> PP. <g:message code="panel.unit"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].vpnCena"
                                              id="${panelType}[${id}].vpnPrice"
                                              value="${pointData?.vpnCena}"
                                              validatable="${pointData}"
                                              validateField="vpnCena"
                                              class="half-width float-number"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].vpnPPCena"
                                              id="${panelType}[${id}].vpnPPPrice"
                                              value="${pointData?.vpnPPCena}"
                                              validatable="${pointData}"
                                              validateField="vpnPPCena" readonly="readonly"
                                              class="half-width float-number"/></td>
                </tr>

                <tr>
                    <td><g:message code="type.label"/> <dict:typeSelect nip="${nip}"
                                                                        medium="SSL"
                                                                        id="${panelType}[${id}].possetforselectedpointSslType"
                                                                        name="${panelType}[${id}].sslTyp" from="[]"
                                                                        valueMessagePrefix="" value="${pointData?.sslTyp}"
                                                                        style="width: 220px"/></td>
                    <td style="text-align: right;"><g:message code="panel.ssl"/></td>
                    <td><g:textField name="${panelType}[${id}].sslIlosc"
                                     id="${panelType}[${id}].sslCount"
                                     value="${pointData?.sslIlosc}"
                                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>
                    <td><g:textField name="${panelType}[${id}].sslPPIlosc"
                                     id="${panelType}[${id}].sslPPCount"
                                     value="${pointData?.sslPPIlosc}" readonly="readonly"
                                     class="half-width integer-number"/> PP. <g:message code="panel.unit"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].sslCena"
                                              id="${panelType}[${id}].sslPrice"
                                              value="${pointData?.sslCena}"
                                              validatable="${pointData}"
                                              validateField="sslCena"
                                              class="half-width float-number"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].sslPPCena"
                                              value="${pointData?.sslPPCena}"
                                              validatable="${pointData}" readonly="readonly"
                                              validateField="sslPPCena"
                                              class="half-width float-number"/></td>
                </tr>

                <tr>
                    <td><g:message code="type.label"/> <dict:typeSelect nip="${nip}"
                                                                        medium="GPRS"
                                                                        id="${panelType}[${id}].possetforselectedpointGprsType"
                                                                        name="${panelType}[${id}].gprsTyp" from="[]"
                                                                        valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                                                        style="width: 220px"
                                                                        onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>
                    <td style="text-align: right;"><g:message code="panel.gprs"/></td>
                    <td><g:textField name="${panelType}[${id}].gprsIlosc"
                                     id="${panelType}[${id}].gprsCount"
                                     value="${pointData?.gprsIlosc}"
                                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>
                    <td><g:textField name="${panelType}[${id}].gprsPPIlosc"
                                     id="${panelType}[${id}].gprsPPCount"
                                     value="${pointData?.gprsPPIlosc}" readonly="readonly"
                                     class="half-width integer-number"/> PP. <g:message code="panel.unit"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].gprsCena"
                                              id="${panelType}[${id}].gprsPrice"
                                              value="${pointData?.gprsCena}"
                                              validatable="${pointData}"
                                              validateField="gprsCena"
                                              class="half-width float-number"/></td>
                    <td><eumowy:currencyField name="${panelType}[${id}].gprsPPCena"
                                              id="${panelType}[${id}].gprsPPPrice"
                                              value="${pointData?.gprsPPCena}"
                                              validatable="${pointData}"
                                              validateField="gprsPPCena" readonly="readonly"
                                              class="half-width float-number"/></td>
                </tr>

                <tr>
                    <td><g:message code="type.label"/> <dict:typeSelect nip="${nip}"
                                                                        medium="PINPAD"
                                                                        id="${panelType}[${id}].possetforselectedpointPinpadType"
                                                                        name="${panelType}[${id}].pinPadTyp" from="[]"
                                                                        valueMessagePrefix="" value="${pointData?.pinPadTyp}"
                                                                        style="width: 220px"
                                                                        onchange="verifyBaseVisibility(this.value,${id})"/></td>
                    <td style="text-align: right;"><g:message code="panel.pinpad"/></td>
                    <td><g:textField name="${panelType}[${id}].pinPadIlosc"
                                     id="${panelType}[${id}].pinpadCount"
                                     value="${pointData?.pinPadIlosc}"
                                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>
                    <td></td>

                    <td><eumowy:currencyField name="${panelType}[${id}].pinPadCena"
                                              id="${panelType}[${id}].pinpadPrice"
                                              value="${pointData?.pinPadCena}"
                                              validatable="${pointData}"
                                              validateField="pinPadCena"
                                              class="half-width float-number"/></td>
                    <td></td>
                </tr>
            </tbody>
        </table>
    </div>
</fieldset>