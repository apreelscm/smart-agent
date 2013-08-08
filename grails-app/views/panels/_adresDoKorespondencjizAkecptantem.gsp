<div id="acceptorCorrespondenceAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.correspondence.addres.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list">
                <li>
                    <span><label><input type="checkbox" name="merchantAddress" value="1" /><g:message code="panel.as.merchant"/></label></span>
                </li>
                <li>
                    <span><g:message code="panel.street" /></span>
                    <span>
                        <select name="correspondenceAddressStreetTitle">
                            <option value="ulica">ulica</option>
                            <option value="osiedle">osiedle</option>
                            <option value="aleja">aleja</option>
                            <option value="plac">plac</option>
                        </select>
                        <g:textField name="correspondenceAddressStreet" style="width: 200px"/>
                    </span>
                    <span>
                        <span><g:message code="panel.house.number" /></span> <span><g:textField name="correspondenceAddressHomeNumber" style="width: 50px"/></span>
                        <span><g:message code="panel.flat.number" /></span> <span><g:textField name="correspondenceAddressFlatNumber" style="width: 50px"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.city" /></span>
                    <span><g:textField name="correspondenceAddressCity" style="width: 280px;"/></span>
                    <span>
                        <span><g:message code="panel.postal.code" /></span> <span><g:textField name="correspondenceAddressPostalCode" style="width: 50px"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.postal" /></span>
                    <span><g:textField name="correspondenceAddressPostal" style="width: 280px;"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>