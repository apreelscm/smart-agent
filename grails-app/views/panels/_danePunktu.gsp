<div data-js-id="${id}" class="newPointPanel">
<fieldset style="text-align: center">
<div class="belka-glowna">
    <g:message code="panel.newpoint.pointdata.title" />
</div>
<div style="text-align: center; padding-top: 20px;" class="centre">
<div style="float: right;">
    <g:submitButton data-point-id="${pointData?.id}" id="removePointButton" name="removePointButton"
                    class="button submit" value="Usuń punkt"
                    style="margin-right: 2em; margin-bottom: 1em;" />
</div>
<div style="clear: both;"></div>
<input type="hidden" id="${panelType}[${id}].id" name="${panelType}[${id}].id"
       value="${pointData?.id}" />
<input type="hidden" name="${panelType}[${id}].parentPosId" value="${pointData?.parentPosId}" />
<g:render template="../panels/opieka" />
<fieldset class="subpanel-fieldset">
    <legend>
        <g:message code="panel.newpoint.pointdata.title" />
    </legend>
    <div class="subpanel-fieldset-centercontent">
        <label for="${panelType}[${id}].sameForEveryPoint">
            <g:checkBox id="${panelType}[${id}].sameForEveryPoint"
                        name="${panelType}[${id}].takSamoDlaWszystkichPunktow"
                        value="${pointData?.takSamoDlaWszystkichPunktow}" />
            <g:message code="panel.sameforeverypoint" /></label>
        <ul class="table-list vertical-center">
            <li><span class="align-right"><g:message
                    code="panel.nip" /></span> <span><eumowy:textField class="nip"
                                                                       name="${panelType}[${id}].nipPunktu"
                                                                       id="${panelType}[${id}].nip" value="${pointData?.nipPunktu}"
                                                                       maxlength="10" required="true" readonly="true"
                                                                       validatable="${pointData}"
                                                                       validateField="nipPunktu"/></span></li>
            <li><span class="align-right"><g:message
                    code="panel.mcccode" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].kodMCC"
                    id="${panelType}[${id}].mccCode" value="${pointData?.kodMCC}"
                    maxlength="4" required="true"
                    validatable="${pointData}"
                    validateField="kodMCC" />
                </span>
            </li>
            <li><span class="align-right"><g:message
                    code="panel.bussinesstypeinpractice" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].rodzProwadzDzialalWPraktyce"
                    id="${panelType}[${id}].bussinessTypeInPractice"
                    value="${pointData?.rodzProwadzDzialalWPraktyce}"
                    maxlength="255" required="true"/></span></li>
            <li><span class="align-right"><g:message
                    code="panel.bankaccountnumber" /></span> <span><eumowy:textField
                    class="bank-account"
                    name="${panelType}[${id}].numerRachunkuBankowego"
                    id="${panelType}[${id}].bankAccountNumber"
                    value="${pointData?.numerRachunkuBankowego}"
                    validatable="${pointData}"
                    validateField="numerRachunkuBankowego"
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
                    validatable="${pointData}"
                    validateField="nazwaDoWydrukuZTerminalaPos"
                    required="true" class="nazwaField"/>
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
                              validatable="${pointData}"
                              validateField="nazwaDoWyszukiwarki"
                              required="true" class="nazwaField"/>
        </p>
        <p>
            <label for="${panelType}[${id}].dataforprintingAsForMerchant"><g:checkBox
                    name="${panelType}[${id}].wydrukJakDlaMerchanta"
                    id="${panelType}[${id}].dataforprintingAsForMerchant"
                    value="${pointData?.wydrukJakDlaMerchanta}" />
                <g:message code="panel.as.merchant" /></label>
        </p>
        <ul class="table-list">
            <li><span><g:message code="panel.street" />
            <dict:streetSelect name="${panelType}[${id}].wydrukUlicaTytul"
                               id="${panelType}[${id}].dataforprintingAddressStreetType"
                               value="${pointData?.wydrukUlicaTytul}"
                               default="UL"/>
            <eumowy:textField
                    name="${panelType}[${id}].wydrukUlica"
                    id="${panelType}[${id}].dataforprintingAddressStreet"
                    style="width: 200px" value="${pointData?.wydrukUlica}"
                    validatable="${pointData}"
                    validateField="wydrukUlica"
                    required="true"/>
            </span> <span> <span><g:message code="panel.house.number" /></span>
                <span><eumowy:textField
                        name="${panelType}[${id}].wydrukNrDomu"
                        id="${panelType}[${id}].dataforprintingAddressHomeNumber"
                        style="width: 50px" value="${pointData?.wydrukNrDomu}"
                        validatable="${pointData}"
                        validateField="wydrukNrDomu"
                        required="true"
                        maxlength="4"/></span> <span><g:message
                        code="panel.flat.number" required="true"/></span> <span><eumowy:textField
                        name="${panelType}[${id}].wydrukNrLokalu"
                        id="${panelType}[${id}].dataforprintingAddressFlatNumber"
                        style="width: 50px" value="${pointData?.wydrukNrLokalu}"
                        maxlength="4"/></span>
            </span></li>
            <li>
                <span>
                    <span style="float: left"><g:message code="panel.postal.code"/>
                    <eumowy:textField
                            class="postal-code"
                            name="${panelType}[${id}].wydrukKodPocztowy"
                            id="${panelType}[${id}].dataforprintingAddressPostalCode"
                            value="${pointData?.wydrukKodPocztowy}" style="width: 50px"
                            validatable="${pointData}"
                            validateField="wydrukKodPocztowy"
                            required="true"/>
                    </span>
                    <span style="float: left"> <g:message code="panel.city" />
                    <g:select  id="${panelType}[${id}].dataforprintingAddressCity"
                               name="${panelType}[${id}].wydrukMiasto" value="${pointData?.wydrukMiasto}"
                               from="[pointData?.wydrukMiasto ?: '']"
                               style="width: 200px;"  required="required"
                               validateField="wydrukMiasto"/>
                    </span>
                </span>
            </li>
            <li><span><g:message code="panel.postal" />
            <eumowy:textField
                    name="${panelType}[${id}].wydrukPoczta"
                    id="${panelType}[${id}].dataforprintingAddressPostOffice"
                    value="${pointData?.wydrukPoczta}" style="width: 280px;"
                    validatable="${pointData}"
                    validateField="wydrukPoczta"
                    required="true"/>
            </span></li>
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
    <div class="subpanel-fieldset-centercontent" style="width: auto">
        <label
                for="${panelType}[${id}].possetforselectedpointSameForEveryPoint"><g:checkBox
                id="${panelType}[${id}].possetforselectedpointSameForEveryPoint"
                name="${panelType}[${id}].zestawPosTakSamoDlaWszystkichPunktow"
                value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}" />
            <g:message code="panel.sameforeverypoint" /></label>
        <table class="vertical-center">
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
                <td><eumowy:currencyField name="${panelType}[${id}].dialupCenaPreferencyjna"
                                 id="${panelType}[${id}].dialupPricePreferencyjna"
                                 value="${pointData?.dialupCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="dialupCenaPreferencyjna"
                                 class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].dialupPPCenaPreferencyjna"
                                 id="${panelType}[${id}].dialupPPPricePreferencyjna"
                                 value="${pointData?.dialupPPCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="dialupPPCenaPreferencyjna" readonly="readonly"
                                 class="half-width float-number"/></td>
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
                <td><eumowy:currencyField name="${panelType}[${id}].vpnCenaPreferencyjna"
                                 id="${panelType}[${id}].vpnPricePreferencyjna"
                                 value="${pointData?.vpnCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="vpnCenaPreferencyjna"
                                 class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].vpnPPCenaPreferencyjna"
                                 id="${panelType}[${id}].vpnPPPricePreferencyjna"
                                 value="${pointData?.vpnPPCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="vpnPPCenaPreferencyjna" readonly="readonly"
                                 class="half-width float-number"/></td>
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
                <td><eumowy:currencyField name="${panelType}[${id}].sslCena"
                                 value="${pointData?.sslCena}"
                                 validatable="${pointData}"
                                 validateField="sslCena"
                                 class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].sslPPCena"
                                  value="${pointData?.sslPPCena}"
                                  validatable="${pointData}" readonly="readonly"
                                  validateField="sslPPCena"
                                  class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].sslCenaPreferencyjna"
                                 id="${panelType}[${id}].sslPricePreferencyjna"
                                 value="${pointData?.sslCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="sslCenaPreferencyjna"
                                 class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].sslPPCenaPreferencyjna"
                                 id="${panelType}[${id}].sslPPPricePreferencyjna"
                                 value="${pointData?.sslPPCenaPreferencyjna}" 
                                 validatable="${pointData}" readonly="readonly"
                                 validateField="sslPPCenaPreferencyjna"
                                 class="half-width float-number"/></td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         medium="GPRS"
                                         id="${panelType}[${id}].possetforselectedpointGprsType"
                                         name="${panelType}[${id}].gprsTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.gprsTyp}"
                                         style="width: 220px"
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

                <td><eumowy:currencyField name="${panelType}[${id}].gprsCenaPreferencyjna"
                                 id="${panelType}[${id}].gprsPricePreferencyjna"
                                 value="${pointData?.gprsCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="gprsCenaPreferencyjna"
                                 class="half-width float-number"/></td>
                <td><eumowy:currencyField name="${panelType}[${id}].gprsPPCenaPreferencyjna"
                                 id="${panelType}[${id}].gprsPPPricePreferencyjna"
                                 value="${pointData?.gprsPPCenaPreferencyjna}" 
                                 validatable="${pointData}" readonly="readonly"
                                 validateField="gprsPPCenaPreferencyjna"
                                 class="half-width float-number"/></td>
            </tr>
            <tr>
                <td>typ <dict:typeSelect nip="${nip}"
                                         medium="PINPAD"
                                         id="${panelType}[${id}].possetforselectedpointPinpadType"
                                         name="${panelType}[${id}].pinPadTyp" from="[]"
                                         valueMessagePrefix="" value="${pointData?.pinPadTyp}"
                                         style="width: 220px"
                                         onchange="verifyBaseVisibility(this.value,${id})"

                /></td>
                <td style="text-align: right;"><g:message code="panel.pinpad" /></td>
                <td><g:textField name="${panelType}[${id}].pinPadIlosc"
                                 id="${panelType}[${id}].pinpadCount"
                                 value="${pointData?.pinPadIlosc}" 
                                 class="half-width integer-number"/> szt.</td>
                <td></td>

                <td><eumowy:currencyField name="${panelType}[${id}].pinPadCena"
                                 id="${panelType}[${id}].pinpadPrice"
                                 value="${pointData?.pinPadCena}" 
                                 validatable="${pointData}"
                                 validateField="pinPadCena"
                                 class="half-width float-number"/></td>
                <td></td>

                <td><eumowy:currencyField name="${panelType}[${id}].pinPadCenaPreferencyjna"
                                 id="${panelType}[${id}].pinpadPricePreferencyjna"
                                 value="${pointData?.pinPadCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="pinPadCenaPreferencyjna"
                                 class="half-width float-number"/></td>
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

                <td><eumowy:currencyField name="${panelType}[${id}].wifiCena"
                                 id="${panelType}[${id}].wifiPrice"
                                 value="${pointData?.wifiCena}" 
                                 validatable="${pointData}"
                                 validateField="wifiCena"
                                 class="half-width float-number"/></td>
                <td></td>

                <td><eumowy:currencyField name="${panelType}[${id}].wifiCenaPreferencyjna"
                                 id="${panelType}[${id}].wifiPricePreferencyjna"
                                 value="${pointData?.wifiCenaPreferencyjna}" 
                                 validatable="${pointData}"
                                 validateField="wifiCenaPreferencyjna"
                                 class="half-width float-number"/></td>
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
    function setFieldPropertiesInDodatkoweWyposazenie(element, value){
        var punkt = jQuery(element).closest("div.newPointPanel");
        if(value == "Verifone Vx670 GPRS"){
            punkt.find("tr.baseRow").show();
            setRequiredForSimCard(true, punkt)
        } else {
            var id = jQuery(element).attr('id');
            var bazaXPath = id.substring(0,id.indexOf('.')) + ".bazaIlosc";
            jQuery("[name='"+bazaXPath+"']").val("");
            punkt.find("tr.baseRow").hide();
            if (value !== ""){
                setRequiredForSimCard(true, punkt)
            }
            else {
                setRequiredForSimCard(false, punkt)
            }
        }
    }

    function setRequiredForSimCard(isRequired, punkt){
        if(isRequired){
            punkt.find("select.kartaSimTyp").attr("required", true);
            punkt.find("input.kartaSimIlosc").attr("required", true);
        } else {
            punkt.find("select.kartaSimTyp").removeAttr("required", true).removeClass("error");
            punkt.find("input.kartaSimIlosc").removeAttr("required", true).removeClass("error");
        }

    }
</r:script>
