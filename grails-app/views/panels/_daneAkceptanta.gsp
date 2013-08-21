<div id="acceptorDataPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.data.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li><span><g:message code="panel.acceptor.full.name"/></span></li>
                <li><span><g:textField name="akceptantNazwaOficjalna" style="width: 400px"/></span></li>
                <li><span><g:message code="panel.acceptor.network.name"/></span></li>
                <li><span><g:textField name="akceptantNazwaSieciowa" style="width: 400px;"/></span></li>
                <li>
                    <span>
                        <span><g:message code="panel.acceptor.nip"/></span>
                        <span><g:textField name="akceptantNip" value="${nip}"/></span>
                        <span><g:message code="panel.acceptor.regon"/></span>
                        <span><g:textField name="akcpetantRegon" /></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>