<div class="newPointPanel">
<fieldset style="text-align: center">
<div class="belka-glowna">
    <g:message code="panel.newpoint.pointdata.title" />
</div>
<div style="text-align: center; padding-top: 20px;" class="centre">
<div style="float: right;">
    <g:submitButton id="removePointButton" name="removePointButton"
                    class="button submit" value="Usuń punkt"
                    style="margin-right: 2em; margin-bottom: 1em;" />
</div>
<div style="clear: both;"></div>
<input type="hidden" name="${panelType}[${id}].id"
       value="${pointData?.id}" />
<g:render template="../panels/opieka" />
<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.pointdata.title" />
    </legend>
    <div class="subpanel-fieldset-centercontent">
        <ul class="table-list vertical-center">
            <li><span class="align-right"><g:message
                    code="panel.nip" /></span> <span><eumowy:textField class="nip"
                                                                       name="${panelType}[${id}].nipPunktu"
                                                                       id="${panelType}[${id}].nip" value="${pointData?.nipPunktu}"
                                                                       maxlength="10" required="true" readonly="true"/></span></li>
            <li><span class="align-right"><g:message
                    code="panel.mcccode" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].kodMCC"
                    id="${panelType}[${id}].mccCode" value="${pointData?.kodMCC}"
                    maxlength="4" required="true" /> <label
                    for="${panelType}[${id}].sameForEveryPoint"><g:checkBox
                        id="${panelType}[${id}].sameForEveryPoint"
                        name="${panelType}[${id}].takSamoDlaWszystkichPunktow"
                        value="${pointData?.takSamoDlaWszystkichPunktow}" />
                <g:message code="panel.sameforeverypoint" /></label></span></li>
            <li><span class="align-right"><g:message
                    code="panel.bussinesstypeinpractice" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].rodzProwadzDzialalWPraktyce"
                    id="${panelType}[${id}].bussinessTypeInPractice"
                    value="${pointData?.rodzProwadzDzialalWPraktyce}"
                    maxlength="60" required="true"/></span></li>
            <li><span class="align-right"><g:message
                    code="panel.bankaccountnumber" /></span> <span><eumowy:textField
                    class="bank-account"
                    name="${panelType}[${id}].numerRachunkuBankowego"
                    id="${panelType}[${id}].bankAccountNumber"
                    value="${pointData?.numerRachunkuBankowego}"
                    style="width: 250px;"
                    required="true"/></span></li>
            <li><span class="align-right"><g:message
                    code="panel.bankname" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].bank"
                    id="${panelType}[${id}].bankName" value="${pointData?.bank}"
                    style="width: 400px;"
                    required="true"/><input type="hidden"
                                            name="${panelType}[${id}].bankId"
                                            id="${panelType}[${id}].bankId" value="${pointData?.bankId}" /></span></li>
        </ul>
    </div>
</fieldset>
<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.dataforprinting" />
    </legend>
    <div class="subpanel-fieldset-centercontent">
        <p>
            <g:message
                    code="panel.newpoint.pointnameforprintingfromposterminal" />
        </p>
        <p>
            <eumowy:textField
                    name="${panelType}[${id}].nazwaDoWydrukuZTerminalaPos"
                    id="${panelType}[${id}].pointNameForPrintingFromPOSTerminal"
                    value="${pointData?.nazwaDoWydrukuZTerminalaPos}"
                    required="true"/>
        </p>
        <p>
            <g:message code="panel.newpoint.pointnameforsearchengine" />
            <label for="${panelType}[${id}].dataforprintingAsAbove"><g:checkBox
                    name="${panelType}[${id}].wydrukJakPowyzej"
                    id="${panelType}[${id}].dataforprintingAsAbove"
                    value="${pointData?.wydrukJakPowyzej}"/>
                <g:message code="panel.as.above" /></label>
        </p>
        <p>
            <eumowy:textField name="${panelType}[${id}].nazwaDoWyszukiwarki"
                              id="${panelType}[${id}].pointNameForSearchEngine"
                              value="${pointData?.nazwaDoWyszukiwarki}"
                              required="true"/>
        </p>
        <p>
            <label for="${panelType}[${id}].dataforprintingAsForMerchant"><g:checkBox
                    name="${panelType}[${id}].wydrukJakDlaMerchanta"
                    id="${panelType}[${id}].dataforprintingAsForMerchant"
                    value="${pointData?.wydrukJakDlaMerchanta}" />
                <g:message code="panel.as.merchant" /></label>
        </p>
        <ul class="table-list">
            <li><span><g:message code="panel.street" /></span> <span>
                <dict:streetSelect name="${panelType}[${id}].wydrukUlicaTytul"
                                   id="${panelType}[${id}].dataforprintingAddressStreetType"
                                   value="${pointData?.wydrukUlicaTytul}"
                                   default="UL"/>
                <eumowy:textField
                        name="${panelType}[${id}].wydrukUlica"
                        id="${panelType}[${id}].dataforprintingAddressStreet"
                        style="width: 200px" value="${pointData?.wydrukUlica}"
                        required="true"/>
            </span> <span> <span><g:message code="panel.house.number" /></span>
                <span><eumowy:textField
                        name="${panelType}[${id}].wydrukNrDomu"
                        id="${panelType}[${id}].dataforprintingAddressHomeNumber"
                        style="width: 50px" value="${pointData?.wydrukNrDomu}"
                        maxlength="4"/></span> <span><g:message
                        code="panel.flat.number" required="true"/></span> <span><eumowy:textField
                        name="${panelType}[${id}].wydrukNrLokalu"
                        id="${panelType}[${id}].dataforprintingAddressFlatNumber"
                        style="width: 50px" value="${pointData?.wydrukNrLokalu}"
                        maxlength="4"/></span>
            </span></li>
            <li><span><g:message code="panel.city" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].wydrukMiasto"
                    id="${panelType}[${id}].dataforprintingAddressCity"
                    value="${pointData?.wydrukMiasto}" style="width: 280px;"
                    required="true"/></span> <span>
                <span><g:message code="panel.postal.code" /></span> <span><eumowy:textField
                        class="postal-code"
                        name="${panelType}[${id}].wydrukKodPocztowy"
                        id="${panelType}[${id}].dataforprintingAddressPostalCode"
                        value="${pointData?.wydrukKodPocztowy}" style="width: 50px"
                        required="true"/></span>
            </span></li>
            <li><span><g:message code="panel.postal" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].wydrukPoczta"
                    id="${panelType}[${id}].dataforprintingAddressPostOffice"
                    value="${pointData?.wydrukPoczta}" style="width: 280px;"
                    required="true"/></span></li>
        </ul>
        <p>
            <g:message code="panel.newpoint.otherdataforprintingfromterminal" />
        </p>
        <p>
            <g:message code="panel.line1" />
            <g:textField name="${panelType}[${id}].wydrukLinia1"
                         id="${panelType}[${id}].otherDataForPrintingFromTerminal1"
                         value="${pointData?.wydrukLinia1}" style="width: 90%;" />
        </p>
        <p>
            <g:message code="panel.line2" />
            <g:textField name="${panelType}[${id}].wydrukLinia2"
                         id="${panelType}[${id}].otherDataForPrintingFromTerminal1"
                         value="${pointData?.wydrukLinia2}" style="width: 90%;" />
        </p>
    </div>
</fieldset>
<g:render template="../panels/adresDoKorespondencjiPunktu" />
<g:render template="../panels/osobaDoKontaktuWPunkcie" />
<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.posset.for.selected.point.title" />
    </legend>
    <div class="subpanel-fieldset-centercontent" style="width: 800px;">
        <label
                for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:checkBox
                id="${panelType}[${id}].possetforselectedpointSameForEveryPoint"
                name="${panelType}[${id}].zestawPosTakSamoDlaWszystkichPunktow"
                value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}" />
            <g:message code="panel.sameforeverypoint" /></label>
        <table class="vertical-center" style="width: 800px;">
            <thead>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td colspan="2" style="text-align: center;">Cena</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>Term./mies</td>
                <td>PP./mies.</td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         id="${panelType}[${id}].possetforselectedpointDialupType"
                                         name="${panelType}[${id}].dialupTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.dialupTyp}"
                                         style="width: 220px" /></td>
                <td style="text-align: right;"><g:message
                        code="panel.dialup" /></td>
                <td><g:textField name="${panelType}[${id}].dialupIlosc"
                                 id="${panelType}[${id}].dialupCount"
                                 value="${pointData?.dialupIlosc}" style="width: 50px" /> szt.</td>
                <td><g:textField name="${panelType}[${id}].dialupPPIlosc"
                                 id="${panelType}[${id}].dialupPPCount"
                                 value="${pointData?.dialupPPIlosc}" style="width: 50px" /> PP.
                szt.</td>
                <td><g:textField name="${panelType}[${id}].dialupCena"
                                 id="${panelType}[${id}].dialupPrice"
                                 value="${pointData?.dialupCena}" style="width: 50px" /> zł.</td>
                <td><g:textField name="${panelType}[${id}].dialupPPCena"
                                 id="${panelType}[${id}].dialupPPPrice"
                                 value="${pointData?.dialupPPCena}" style="width: 50px" /> zł.</td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         id="${panelType}[${id}].possetforselectedpointVpnType"
                                         name="${panelType}[${id}].vpnTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.vpnTyp}"
                                         style="width: 220px" /></td>
                <td style="text-align: right;"><g:message code="panel.vpn" /></td>
                <td><g:textField name="${panelType}[${id}].vpnIlosc"
                                 id="${panelType}[${id}].vpnCount"
                                 value="${pointData?.vpnIlosc}" style="width: 50px" /> szt.</td>
                <td><g:textField name="${panelType}[${id}].vpnPPIlosc"
                                 id="${panelType}[${id}].vpnPPCount"
                                 value="${pointData?.vpnPPIlosc}" style="width: 50px" /> PP.
                szt.</td>
                <td><g:textField name="${panelType}[${id}].vpnCena"
                                 id="${panelType}[${id}].vpnPrice"
                                 value="${pointData?.vpnCena}" style="width: 50px" /> zł.</td>
                <td><g:textField name="${panelType}[${id}].vpnPPCena"
                                 id="${panelType}[${id}].vpnPPPrice"
                                 value="${pointData?.vpnPPCena}" style="width: 50px" /> zł.</td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         id="${panelType}[${id}].possetforselectedpointSslType"
                                         name="${panelType}[${id}].sslTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.sslTyp}"
                                         style="width: 220px" /></td>
                <td style="text-align: right;"><g:message code="panel.ssl" /></td>
                <td><g:textField name="${panelType}[${id}].sslIlosc"
                                 id="${panelType}[${id}].sslCount"
                                 value="${pointData?.sslIlosc}" style="width: 50px" /> szt.</td>
                <td><g:textField name="${panelType}[${id}].sslPPIlosc"
                                 id="${panelType}[${id}].sslPPCount"
                                 value="${pointData?.sslPPIlosc}" style="width: 50px" /> PP.
                szt.</td>
                <td><g:textField name="${panelType}[${id}].sslCena"
                                 id="${panelType}[${id}].sslPrice"
                                 value="${pointData?.sslCena}" style="width: 50px" /> zł.</td>
                <td><g:textField name="${panelType}[${id}].sslPPCena"
                                 id="${panelType}[${id}].sslPPPrice"
                                 value="${pointData?.sslPPCena}" style="width: 50px" /> zł.</td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         id="${panelType}[${id}].possetforselectedpointWifiType"
                                         name="${panelType}[${id}].wifiTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.wifiTyp}"
                                         style="width: 220px" /></td>
                <td style="text-align: right;"><g:message code="panel.wifi" /></td>
                <td><g:textField name="${panelType}[${id}].wifiIlosc"
                				 id="${panelType}[${id}].wifiCount"
                                 value="${pointData?.wifiIlosc}" style="width: 50px" /> szt.</td>
                <td><g:textField name="${panelType}[${id}].wifiPPIlosc"
                                 id="${panelType}[${id}].wifiPPCount"
                                 value="${pointData?.wifiPPIlosc}" style="width: 50px" /> PP.
                szt.</td>
                <td><g:textField name="${panelType}[${id}].wifiCena"
                                 id="${panelType}[${id}].wifiPrice"
                                 value="${pointData?.wifiCena}" style="width: 50px" /> zł.</td>
                <td><g:textField name="${panelType}[${id}].wifiPPCena"
                                 id="${panelType}[${id}].wifiPPPrice"
                                 value="${pointData?.wifiPPCena}" style="width: 50px" /> zł.</td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         id="${panelType}[${id}].possetforselectedpointGprsType"
                                         name="${panelType}[${id}].gprsTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                         style="width: 220px"
                                         onchange="verifyBaseVisibility(this.value,${id})"

                /></td>
                <td style="text-align: right;"><g:message code="panel.gprs" /></td>
                <td><g:textField name="${panelType}[${id}].gprsIlosc"
                                 id="${panelType}[${id}].gprsCount"
                                 value="${pointData?.gprsIlosc}" style="width: 50px" /></td>
                <td><g:textField name="${panelType}[${id}].gprsPPIlosc"
                                 id="${panelType}[${id}].gprsPPCount"
                                 value="${pointData?.gprsPPIlosc}" style="width: 50px" /> PP.
                szt.</td>
                <td><g:textField name="${panelType}[${id}].gprsCena"
                                 id="${panelType}[${id}].gprsPrice"
                                 value="${pointData?.gprsCena}" style="width: 50px" /> zł.</td>
                <td><g:textField name="${panelType}[${id}].gprsPPCena"
                                 id="${panelType}[${id}].gprsPPPrice"
                                 value="${pointData?.gprsPPCena}" style="width: 50px" /> zł.</td>
            </tr>
            <tr id="trBase${id}">
                <td></td>
                <td style="text-align: right;"><g:message code="panel.base" /></td>
                <td><g:textField id="${panelType}[${id}].baseCount"
                                 name="${panelType}[${id}].bazaIlosc"
                                 value="${pointData?.bazaIlosc}" style="width: 50px" /> szt.</td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div>
</fieldset>
<g:render template="../panels/informacjeTechniczne" />
<g:render template="../panels/funkcjeTerminala" />
<g:render template="../panels/dodatkoweWyposazenie" />
<g:render template="../panels/adresacjaSeciowa" />
</div>

</fieldset>
</div>

<r:require module="jquery_ui" />
<r:require module="jquery_timepicker_pl" />


<r:script>

    function verifyBaseVisibility(value, trId){
        if(value == "Verifone Vx670 GPRS"){
            jQuery("#trBase"+trId).show()
        }
        else{
            jQuery("#trBase"+trId).hide()
        }
    }
</r:script>
