<div id="acceptorDataPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.data.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li><span><g:message code="panel.acceptor.full.name"/></span></li>
                <li>
                    <span>
                        <eumowy:textField name="akceptantNazwaOficjalna" value="${data.akceptantNazwaOficjalna}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantNazwaOficjalna')}" style="width: 400px" required="true" maxlength="88"/>
                        <g:hiddenField name="akceptantNazwaOficjalnaCbd" value="${data.akceptantNazwaOficjalnaCbd}"/>
                    </span>
                </li>
                <li><span><g:message code="panel.acceptor.network.name"/></span></li>
                <li>
                    <span><eumowy:textField name="akceptantNazwaSieciowa" value="${data.akceptantNazwaSieciowa}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantNazwaSieciowa')}" style="width: 400px;" maxlength="22"/></span>
                    <g:hiddenField name="akceptantNazwaSieciowaCbd" value="${data.akceptantNazwaSieciowaCbd}"/>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.acceptor.nip"/></span>
                        <span><eumowy:textField class="nip" name="akceptantNip" value="${data.nip}" readonly="true"/></span>
                        %{--required="true" minlength="10" maxlength="10" digits="true"--}%
                        <span style="padding-left: 8px"><g:message code="panel.acceptor.regon"/></span>
                        <span>
                            <eumowy:textField class="regon" name="akceptantRegon" value="${data.akceptantRegon}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantRegon')}" required="true" minlength="9" maxlength="9" digits="true"/>
                            <g:hiddenField name="akceptantRegonCbd" value="${data.akceptantRegonCbd}"/>
                        </span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

