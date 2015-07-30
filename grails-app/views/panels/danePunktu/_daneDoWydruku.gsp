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
                    maxlength="44"/>
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
                    maxlength="40"
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