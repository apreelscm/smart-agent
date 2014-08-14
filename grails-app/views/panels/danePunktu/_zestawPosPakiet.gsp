<fieldset class="subpanel-fieldset">
<legend>
    <g:message code="panel.newpoint.posset.for.selected.point.title"/>
</legend>

<div class="subpanel-fieldset-centercontent" style="width: auto">
<label for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:message code="panel.sameforeverypoint"/></label>
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

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="DIALUP" type="STATIONARY" isPINPad="false"
                                 id="${panelType}[${id}].possetforselectedpointDialupType"
                                 name="${panelType}[${id}].dialupTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.dialupTyp}"
                                 valueMessagePrefix="" value="${pointData?.dialupTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.dialup"/></td>

    <td><g:textField name="${panelType}[${id}].dialupIlosc"
                     id="${panelType}[${id}].dialupCount"
                     value="1" disabled="${pointData?.dialupTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].dialupCena"
                              id="${panelType}[${id}].dialupPrice"
                              value="0" disabled="${pointData?.dialupTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="dialupCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="DIALUP" type="STATIONARY" isPINPad="true"
                                 id="${panelType}[${id}].possetforselectedpointDialupPPType"
                                 name="${panelType}[${id}].dialupPPTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.dialupPPTyp}"
                                 valueMessagePrefix="" value="${pointData?.dialupPPTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.dialup"/></td>

    <td><g:textField name="${panelType}[${id}].dialupPPIlosc"
                     id="${panelType}[${id}].dialupPPCount"
                     value="1" disabled="${pointData?.dialupPPTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].dialupPPCena"
                              id="${panelType}[${id}].dialupPPPrice"
                              value="0" disabled="${pointData?.dialupPPTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="dialupPPCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="VPN" type="STATIONARY" isPINPad="false"
                                 id="${panelType}[${id}].possetforselectedpointVpnType"
                                 name="${panelType}[${id}].vpnTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.vpnTyp}"
                                 valueMessagePrefix="" value="${pointData?.vpnTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.vpn"/></td>

    <td><g:textField name="${panelType}[${id}].vpnIlosc"
                     id="${panelType}[${id}].vpnCount"
                     value="1" disabled="${pointData?.vpnTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].vpnCena"
                              id="${panelType}[${id}].vpnPrice"
                              value="0" disabled="${pointData?.vpnTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="vpnCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="VPN" type="STATIONARY" isPINPad="true"
                                 id="${panelType}[${id}].possetforselectedpointVpnPPType"
                                 name="${panelType}[${id}].vpnPPTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.vpnPPTyp}"
                                 valueMessagePrefix="" value="${pointData?.vpnPPTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.vpn"/></td>

    <td><g:textField name="${panelType}[${id}].vpnPPIlosc"
                     id="${panelType}[${id}].vpnPPCount"
                     value="1" disabled="${pointData?.vpnPPTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].vpnPPCena"
                              id="${panelType}[${id}].vpnPPPrice"
                              value="0" disabled="${pointData?.vpnPPTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="vpnPPCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="SSL" type="STATIONARY" isPINPad="false"
                                 id="${panelType}[${id}].possetforselectedpointSslType"
                                 name="${panelType}[${id}].sslTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.sslTyp}"
                                 valueMessagePrefix="" value="${pointData?.sslTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.ssl"/></td>

    <td><g:textField name="${panelType}[${id}].sslIlosc"
                     id="${panelType}[${id}].sslCount"
                     value="1" disabled="${pointData?.sslTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].sslCena"
                              id="${panelType}[${id}].sslPrice"
                              value="0" disabled="${pointData?.sslTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="sslCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="SSL" type="STATIONARY" isPINPad="true"
                                 id="${panelType}[${id}].possetforselectedpointSslPPType"
                                 name="${panelType}[${id}].sslPPTyp" from="[]"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.sslPPTyp}"
                                 valueMessagePrefix="" value="${pointData?.sslPPTyp}"/></td>

    <td style="text-align: right;"><g:message code="panel.ssl"/></td>

    <td><g:textField name="${panelType}[${id}].sslPPIlosc"
                     id="${panelType}[${id}].sslPPCount"
                     value="1" disabled="${pointData?.sslPPTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].sslPPCena"
                              id="${panelType}[${id}].sslPPPrice"
                              value="0" disabled="${pointData?.sslPPTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="sslPPCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="GPRS" type="STATIONARY" isPINPad="false"
                                 id="${panelType}[${id}].possetforselectedpointGprsType"
                                 name="${panelType}[${id}].gprsTyp" from="[]"
                                 valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.gprsTyp}"
                                 onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

    <td style="text-align: right;"><g:message code="panel.gprs"/></td>

    <td><g:textField name="${panelType}[${id}].gprsIlosc"
                     id="${panelType}[${id}].gprsCount"
                     value="1" disabled="${pointData?.gprsTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].gprsCena"
                              id="${panelType}[${id}].gprsPrice"
                              value="0" disabled="${pointData?.gprsTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="gprsCena"
                              class="half-width float-number"/></td>
</tr>

<tr class="stationary">
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="GPRS" type="STATIONARY" isPINPad="true"
                                 id="${panelType}[${id}].possetforselectedpointGprsPPType"
                                 name="${panelType}[${id}].gprsPPTyp" from="[]"
                                 valueMessagePrefix="" value="${pointData?.gprsPPTyp}"
                                 disabled="${pointData?.hasStationaryTypeChoosen() && !pointData?.gprsPPTyp}"
                                 onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

    <td style="text-align: right;"><g:message code="panel.gprs"/></td>

    <td><g:textField name="${panelType}[${id}].gprsPPIlosc"
                     id="${panelType}[${id}].gprsPPCount"
                     value="1" disabled="${pointData?.gprsPPTyp ? false : true}" readonly="readonly"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].gprsPPCena"
                              id="${panelType}[${id}].gprsPPPrice"
                              value="0" disabled="${pointData?.gprsPPTyp ? false : true}" readonly="readonly"
                              validatable="${pointData}"
                              validateField="gprsPPCena"
                              class="half-width float-number"/></td>
</tr>

<tr>
    <td colspan="4" class="posHeaderType"><g:message code="portable.label"/></td>
</tr>

<tr>
    <td class="posTypeColumn"><g:message code="type.label"/>
        <dict:extendedTypeSelect medium="GPRS" type="PORTABLE" isPINPad="true"
                                 id="${panelType}[${id}].possetforselectedpointGprsTypePortable"
                                 name="${panelType}[${id}].gprsTypPortable" from="[]"
                                 valueMessagePrefix="" disabled="disabled"
                                 onchange="setFieldPropertiesInDodatkoweWyposazenie(this, this.value, false)"/></td>

    <td style="text-align: right;"><g:message code="panel.gprs"/></td>

    <td><g:textField name="${panelType}[${id}].gprsIloscPortable"
                     id="${panelType}[${id}].gprsCountPortable"
                     disabled="disabled"
                     class="half-width integer-number"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].gprsCenaPortable"
                              id="${panelType}[${id}].gprsPricePortable"
                              validatable="${pointData}"
                              validateField="gprsCenaPortable"
                              class="half-width float-number" disabled="disabled"/></td>
</tr>

<tr>
    <td colspan="4" class="posHeaderType"><g:message code="pinpad.label"/></td>
</tr>

<tr>
    <td class="posTypeColumn"><g:message code="type.label"/>
    <dict:typeSelect medium="PINPAD"
                     id="${panelType}[${id}].possetforselectedpointPinpadType"
                     name="${panelType}[${id}].pinPadTyp" from="[]"
                     valueMessagePrefix=""
                     onchange="verifyBaseVisibility(this.value,${id})" disabled="disabled"/>
    </td>
    <td style="text-align: right;"><g:message code="panel.pinpad"/></td>
    <td><g:textField name="${panelType}[${id}].pinPadIlosc"
                     id="${panelType}[${id}].pinpadCount"
                     class="half-width integer-number" disabled="disabled"/> <g:message code="panel.unit"/></td>

    <td><eumowy:currencyField name="${panelType}[${id}].pinPadCena"
                              id="${panelType}[${id}].pinpadPrice"
                              validatable="${pointData}"
                              validateField="pinPadCena"
                              class="half-width float-number" disabled="disabled"/></td>
</tr>
</tbody>
</table>
</div>
</fieldset>

<script type="text/javascript">
    var posTable = jQuery("#zestawPOSTable"),
        stationaryPOSes = posTable.find("tr.stationary");

    jQuery("#zestawPOSTable select").change(function() {
        var $this = jQuery(this),
            row = $this.parents('tr');

        if(this.value === '') {
            stationaryPOSes.find('select').removeAttr('disabled');
            row.find('input').attr('disabled', 'disabled');
        } else {
            stationaryPOSes.find('input,select').attr('disabled', 'disabled');
            row.find('input').removeAttr('disabled');
            row.find('select').removeAttr('disabled');
        }
    });
</script>