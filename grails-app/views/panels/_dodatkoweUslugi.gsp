<div id="additionalServicesPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.additional.services.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <table>
                <tr>
                    <td><g:message code="panel.payment.daily.transaction.report"/></td>
                    <td><g:field type="number" step="any" name="oplataZaDzienneZestawienieTransakcji" value="${data.oplataZaDzienneZestawienieTransakcji}" /></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.monthly.transaction.report"/></td>
                    <td><g:field type="number" step="any" name="oplataZaMiesieczneZestawienieTransakcji" value="${data.oplataZaMiesieczneZestawienieTransakcji}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.confirmation.payment"/></td>
                    <td><g:field type="number" step="any" name="oplataZaPotwierdzenieWykonaniaPrzelewu" value="${data.oplataZaPotwierdzenieWykonaniaPrzelewu}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.paper"/></td>
                    <td><g:field type="number" step="any" name="oplataZaDostarczeniePapieru" value="${data.oplataZaDostarczeniePapieru}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.graphics"/></td>
                    <td><g:field type="number" step="any" name="oplataZaZmianeGrafiki" value="${data.oplataZaZmianeGrafiki}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.instalation"/></td>
                    <td><g:field type="number" step="any" name="oplataZaInstalacjePOS" value="${data.oplataZaInstalacjePOS}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.gprs"/></td>
                    <td><g:field type="number" step="any" name="oplataZaInstalacjeGPRS" value="${data.oplataZaInstalacjeGPRS}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.foreing.currency"/></td>
                    <td><g:field type="number" step="any" name="oplataZaUruchomienieWalutyObcej" value="${data.oplataZaUruchomienieWalutyObcej}"/></td>
                    <td><g:message code="panel.polish.currency"/></td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>