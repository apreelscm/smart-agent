<div id="additionalServicesPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.additional.services.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <table>
                <tr>
                    <td><g:message code="panel.payment.daily.transaction.report"/></td>
                    <td><eumowy:currencyField  name="oplataZaDzienneZestawienieTransakcji" validatable="${data}" value="${data.oplataZaDzienneZestawienieTransakcji}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.monthly.transaction.report"/></td>
                    <td><eumowy:currencyField name="oplataZaMiesieczneZestawienieTransakcji" validatable="${data}" value="${data.oplataZaMiesieczneZestawienieTransakcji}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.confirmation.payment"/></td>
                    <td><eumowy:currencyField name="oplataZaPotwierdzenieWykonaniaPrzelewu" validatable="${data}" value="${data.oplataZaPotwierdzenieWykonaniaPrzelewu}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.paper"/></td>
                    <td><eumowy:currencyField name="oplataZaDostarczeniePapieru" validatable="${data}"  value="${data.oplataZaDostarczeniePapieru}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.graphics"/></td>
                    <td><eumowy:currencyField name="oplataZaZmianeGrafiki" validatable="${data}" value="${data.oplataZaZmianeGrafiki}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.instalation"/></td>
                    <td><eumowy:currencyField name="oplataZaInstalacjePOS" validatable="${data}" value="${data.oplataZaInstalacjePOS}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.gprs"/></td>
                    <td><eumowy:currencyField name="oplataZaInstalacjeGPRS" validatable="${data}" value="${data.oplataZaInstalacjeGPRS}"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.foreing.currency"/></td>
                    <td><eumowy:currencyField name="oplataZaUruchomienieWalutyObcej" validatable="${data}" value="${data.oplataZaUruchomienieWalutyObcej}"/></td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>