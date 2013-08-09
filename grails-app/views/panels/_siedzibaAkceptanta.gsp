<div id="acceptorAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.address.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span>
                    <span><g:message code="panel.street" /></span>
                    <span>
                        <select name="addressStreetTitle">
                            <option value="ulica">ulica</option>
                            <option value="osiedle">osiedle</option>
                            <option value="aleja">aleja</option>
                            <option value="plac">plac</option>
                        </select>
                        <g:textField name="addressStreet" style="width: 200px"/>
                    </span>
                    <span>
                        <span><g:message code="panel.house.number" /></span> <span><g:textField name="addressHomeNumber" style="width: 50px"/></span>
                        <span><g:message code="panel.flat.number" /></span> <span><g:textField name="addressFlatNumber" style="width: 50px"/></span>
                    </span>
                    </span>
                </li>
                <li>
                    <span>
                    <span><g:message code="panel.city" /></span>
                    <span><g:textField name="addressCity" style="width: 280px;"/></span>
                    <span>
                        <span><g:message code="panel.postal.code" /></span> <span><g:textField name="addressPostalCode" style="width: 50px"/></span>
                    </span>
                    </span>
                </li>
                <li>
                    <span>
                    <span><g:message code="panel.postal" /></span>
                    <span><g:textField name="addressPostal" style="width: 280px;"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span><g:textField name="addressHomeNumber" style="width: 100px;"/></span>
                        <span><g:message code="panel.fax"/></span>
                        <span><g:textField name="addressFlatNumber" style="width: 100px"/></span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span><g:textField name="addressFlatNumber" style="width: 100px"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>