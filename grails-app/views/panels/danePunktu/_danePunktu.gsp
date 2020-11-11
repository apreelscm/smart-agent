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
            <li>
                <g:hiddenField id="${panelType}[${id}].risk" name="${panelType}[${id}].ryzyko"
                               value="${pointData?.ryzyko}"/>

                <span class="align-right"><g:message code="panel.mcccode" /></span>
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