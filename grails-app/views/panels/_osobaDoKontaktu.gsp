<div id="personToContactPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.person.contact.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li class="align-center">
                    <span>
                        <span><g:select name="kontaktTytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.kontaktTytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><eumowy:textField name="kontaktImie" value="${data.kontaktImie}" validatable="${data}" style="width: 120px" maxlength ="15" required="true"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><eumowy:textField name="kontaktNazwisko" value="${data.kontaktNazwisko}" validatable="${data}" maxlength ="18" required="true"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span><eumowy:textField class="phone" name="kontaktTelStacjonarny" value="${data.kontaktTelStacjonarny}" validatable="${data}" style="width: 100px;" maxlength="9"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span><eumowy:textField class="mobile-phone" name="kontaktTelKomorkowy" value="${data.kontaktTelKomorkowy}" validatable="${data}" style="width: 100px" maxlength="9"/></span>
                    </span>
                    <span>
                        <span style="white-space:nowrap"><g:message code="panel.email"/></span>
                        <span><g:textField class="" name="kontaktEmail" value="${data.kontaktEmail}" validatable="${data}" style="width: 150px" email="true"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>