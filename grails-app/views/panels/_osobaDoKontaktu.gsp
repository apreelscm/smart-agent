<div id="personToContactPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.person.contact.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li class="align-center">
                    <span>
                        <span><g:select name="kontaktTytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.kontaktTytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><g:textField name="kontaktImie" value="${data.kontaktImie}" style="width: 120px"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><g:textField name="kontaktNazwisko" value="${data.kontaktNazwisko}"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span><g:textField class="phone" name="kontaktTelStacjonarny" value="${data.kontaktTelStacjonarny}" style="width: 100px;"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span><g:textField class="mobile-phone" name="kontaktTelKomorkowy" value="${data.kontaktTelKomorkowy}" style="width: 100px"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.email"/></span>
                        <span><g:textField class="" name="kontaktEmail" value="${data.kontaktEmail}" style="width: 150px"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>