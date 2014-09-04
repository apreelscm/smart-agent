<div data-js-id="${id}" class="newPointPanel">
<fieldset style="text-align: center">
<div class="belka-glowna">
    <g:message code="panel.newpoint.pointdata.title" />
</div>
<div style="text-align: center; padding-top: 20px;" class="centre">
<div style="float: right;">
    <g:submitButton data-point-id="${pointData?.id}" id="removePointButton" name="removePointButton"
                    class="button submit" value="${message(code: 'delete.point.label')}"
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
                    code="panel.mcccode" /></span>
                <span>
                    <dict:mccSelect 
                    	name="${panelType}[${id}].kodMCC"
                    	id="${panelType}[${id}].mccCode" value="${pointData?.kodMCC}"
                        required="required"/>
                </span>
            </li>
            <li><span class="align-right"><g:message
                    code="panel.bussinesstypeinpractice" /></span> <span><eumowy:textField
                    name="${panelType}[${id}].rodzProwadzDzialalWPraktyce"
                    id="${panelType}[${id}].bussinessTypeInPractice"
                    value="${pointData?.rodzProwadzDzialalWPraktyce}"
                    maxlength="255" required="true"
                    style="width: 400px;"
                    /></span></li>
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
                    required="true" class="nazwaField"
                    maxlength="25"/>
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
                        maxlength="6"/></span> <span><g:message
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
                    <span id="${panelType}[${id}].wydrukMiastoSpinner" class="miasto-spinner visibility-hidden"></span>
                </span>
            </li>
            <li><span><g:message code="panel.postal" />
            <eumowy:textField
                    name="${panelType}[${id}].wydrukPoczta"
                    id="${panelType}[${id}].dataforprintingAddressPostOffice"
                    value="${pointData?.wydrukPoczta}" style="width: 280px;"
                    validatable="${pointData}"
                    validateField="wydrukPoczta"/>
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
<g:render template="../panels/zestawPos" />
<g:render template="../panels/informacjeTechniczne" />
<g:render template="../panels/funkcjeTerminala" />
<g:render template="../panels/dodatkoweWyposazenie" />
<g:render template="../panels/adresacjaSeciowa" />
</div>

</fieldset>
</div>
