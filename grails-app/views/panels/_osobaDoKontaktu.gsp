<div id="personToContactPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.person.contact.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <div style="display:inline-block">
                <select name="contactTitle">
                    <option value="pan">Pan</option>
                    <option value="pani">Pani</option>
                </select>
            </div>
            <div style="display:inline-block"><g:message code="panel.first.name"/>: </div>
            <div style="display:inline-block"><g:textField name="contactFirstName"  style="width: 120px"/></div>
            <div style="display:inline-block"><g:message code="panel.last.name"/>: </div>
            <div style="display:inline-block"><g:textField name="contactLastName"/></div>
            <div style="clear: both"/>
            <div style="display:inline-block; padding-top: 20px"><g:message code="panel.landline.phone.number"/></div>
            <div style="display:inline-block; "><g:textField name="contactLandlineNumber" style="width: 100px;"/></div>
            <div style="display:inline-block; padding-left: 30px"><g:message code="panel.mobile.phone.number"/></div>
            <div style="display:inline-block; "><g:textField name="contactMobileNumber" style="width: 100px"/></div>
            <div style="display:inline-block; padding-left: 30px"><g:message code="panel.email"/></div>
            <div style="display:inline-block; "><g:textField name="contactEmail" style="width: 150px"/></div>
        </div>
    </fieldset>
</div>