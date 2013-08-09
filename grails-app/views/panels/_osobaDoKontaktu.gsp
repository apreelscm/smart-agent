<div id="personToContactPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.person.contact.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list">
                <li class="align-center">
                    <span>
                        <span>
                            <select name="contactTitle">
                                <option value="pan">Pan</option>
                                <option value="pani">Pani</option>
                            </select>
                        </span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><g:textField name="contactFirstName"  style="width: 120px"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><g:textField name="contactLastName"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span><g:textField name="contactLandlineNumber" style="width: 100px;"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span><g:textField name="contactMobileNumber" style="width: 100px"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.email"/></span>
                        <span><g:textField name="contactEmail" style="width: 150px"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>