<div id="personToContactPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.person.contact.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 800px">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span>
                            <g:select name="kontaktTytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.kontaktTytul}"/>
                        </span>
                        <span>
                            <g:message code="panel.first.name"/>: <eumowy:textField name="kontaktImie" value="${data.kontaktImie}" validatable="${data}" style="width: 120px" maxlength ="15" required="true"/>
                        </span>
                        <span>
                            <g:message code="panel.last.name"/>: <eumowy:textField name="kontaktNazwisko" value="${data.kontaktNazwisko}" validatable="${data}" maxlength ="35" required="true" class="nazwiskoField"/>
                        </span>
                    </span>
                </li>
                <li>
                    <span style="width: 800px;">
                        <g:hiddenField name="hasKontaktTel" value="true"/>
                        <div class="${hasErrors(bean:data,field:'hasKontaktTel','errorSpan')} float-left">
                            <span><g:message code="panel.landline.phone.number"/>: <eumowy:textField class="phone" name="kontaktTelStacjonarny" value="${data.kontaktTelStacjonarny}" validatable="${data}"  maxlength="9"/> </span>
                            <span><g:message code="panel.mobile.phone.number"/>: <eumowy:textField class="mobile-phone" name="kontaktTelKomorkowy" value="${data.kontaktTelKomorkowy}" validatable="${data}"  maxlength="9"/> </span>
                        </div>
                        <span>
                            <g:message code="panel.email"/>: <g:textField id="kontaktEmail" class="kontaktEmail" name="kontaktEmail" value="${data.kontaktEmail}" validatable="${data}" style="width: 150px" email="true"/>
                        </span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>