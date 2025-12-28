<fieldset class="subpanel-fieldset">
    <legend><g:message code="panel.newpoint.staticaddressdata.title"/></legend>

    <div class="subpanel-fieldset-centercontent">
        <ul class="table-list vertical-center">
            <li>
                <span class="align-right"><g:message code="panel.mask"/></span>
                <span>
                    <eumowy:textField class="ip" name="${panelType}[${id}].maska" id="${panelType}[${id}].staticDeviceMask"
                                      value="${pointData?.maska}"/>
                </span>
            </li>
            <li>
                <span class="align-right"><g:message code="panel.gateway"/></span>
                <span>
                    <eumowy:textField class="ip" name="${panelType}[${id}].bramka" id="${panelType}[${id}].staticDeviceGateway"
                                      value="${pointData?.bramka}"/>
                </span>
            </li>
            <li>
                <span class="align-right"><g:message code="panel.ipaddress"/></span>
                <span>
                    <eumowy:textField class="ip" name="${panelType}[${id}].adresIp" id="${panelType}[${id}].staticDeviceIp"
                                      value="${pointData?.adresIp}"/>
                </span>
            </li>
            <li>
                <span class="align-right"><g:message code="panel.techniciancontact"/></span>
                <span>
                    <eumowy:textField class="mobile-phone" name="${panelType}[${id}].kontaktInformatykStatyczna"
                                      pattern="\\d{9,9}"
                                      id="${panelType}[${id}].staticDeviceSupportContact"
                                      value="${pointData?.kontaktInformatykStatyczna}"/>
                </span>
            </li>
            <li>
                <span class="align-right">
                    <g:select name="${panelType}[${id}].tytulInformatykStatyczna"
                              from="['', 'Pan', 'Pani']" valueMessagePrefix="person.title"
                              value="${pointData?.tytulInformatykStatyczna}"/>

                    <g:message code="panel.first.name"/>
                </span>
                <span>
                    <eumowy:textField name="${panelType}[${id}].imieInformatykStatyczna" id="${panelType}[${id}].staticDeviceSupportContactName"
                                      value="${pointData?.imieInformatykStatyczna}"/>
                </span>
            </li>
            <li>
                <span class="align-right"><g:message code="panel.last.name"/></span>
                <span>
                    <eumowy:textField name="${panelType}[${id}].nazwiskoInformatykStatyczna" id="${panelType}[${id}].staticDeviceSupportContactSurname"
                    value="${pointData?.nazwiskoInformatykStatyczna}" class="nazwiskoField"/>
                </span>
            </li>
        </ul>
    </div>
</fieldset>
