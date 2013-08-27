<div id="acceptorAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.address.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect name="akceptantUlicaTytul" value="${data.akceptantUlicaTytul}" />
                            <g:textField name="akceptantUlica" value="${data.akceptantUlica}" style="width: 200px"/>
                        </span>
                        <span>
                            <span><g:message code="panel.house.number" /></span> <span><g:textField name="akceptantNrDomu" value="${data.akceptantNrDomu}" style="width: 50px"/></span>
                            <span><g:message code="panel.flat.number" /></span> <span><g:textField name="akceptantNrMieszkania" value="${data.akceptantNrMieszkania}" style="width: 50px"/></span>
                        </span>
                    </span>
                </li>
                <li>
                    <span>
                    <span><g:message code="panel.city" /></span>
                    <span><g:textField name="akceptantMiasto" value="${data.akceptantMiasto}" style="width: 280px;"/></span>
                    <span>
                        <span><g:message code="panel.postal.code" /></span> <span><g:textField name="akceptantKodPocztowy" value="${data.akceptantKodPocztowy}" style="width: 50px"/></span>
                    </span>
                    </span>
                </li>
                <li>
                    <span>
                    <span><g:message code="panel.postal" /></span>
                    <span><g:textField name="akceptantPoczta" value="${data.akceptantPoczta}" style="width: 280px;"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span><g:textField name="akceptantTelStacjonarny" value="${data.akceptantTelStacjonarny}" style="width: 100px;"/></span>
                        <span><g:message code="panel.fax"/></span>
                        <span><g:textField name="akceptantFax" value="${data.akceptantFax}" style="width: 100px"/></span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span><g:textField name="akceptantTelKomorkowy" value="${data.akceptantTelKomorkowy}" style="width: 100px"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>