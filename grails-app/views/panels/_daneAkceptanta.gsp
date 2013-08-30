<div id="acceptorDataPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.data.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li><span><g:message code="panel.acceptor.full.name"/></span></li>
                <li>
                    <span><g:textField name="akceptantNazwaOficjalna" value="${data.akceptantNazwaOficjalna}" readonly="${data.isFromCbd('akceptantNazwaOficjalna')}" style="width: 400px" maxlength ="80"/></span>
                    <g:hiddenField name="akceptantNazwaOficjalnaCbd" value="${data.akceptantNazwaOficjalnaCbd}"/>
                </li>
                <li><span><g:message code="panel.acceptor.network.name"/></span></li>
                <li>
                    <span><g:textField name="akceptantNazwaSieciowa" value="${data.akceptantNazwaSieciowa}" readonly="${data.isFromCbd('akceptantNazwaSieciowa')}" style="width: 400px;" maxlength ="22"/></span>
                    <g:hiddenField name="akceptantNazwaSieciowaCbd" value="${data.akceptantNazwaSieciowaCbd}"/>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.acceptor.nip"/></span>
                        <span><g:textField class="nip" name="akceptantNip" value="${data.nip}" readonly="true" maxlength ="10"/></span>
                        <span><g:message code="panel.acceptor.regon" maxlength ="9"/></span>
                        <span>
                            <g:textField class="regon" name="akceptantRegon" value="${data.akceptantRegon}" readonly="${data.isFromCbd('akceptantRegon')}"/>
                            <g:hiddenField name="akceptantRegonCbd" value="${data.akceptantRegonCbd}"/>
                        </span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>