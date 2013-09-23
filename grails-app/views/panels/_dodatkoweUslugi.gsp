<div id="additionalServicesPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 800px">
            <table class="table-list centre">
                <tr>
                    <td><g:message code="panel.payment.daily.transaction.report"/></td>
                    <td style="width: 170px"><eumowy:currencyField  name="oplataZaDzienneZestawienieTransakcji" validatable="${data}" value="${data.oplataZaDzienneZestawienieTransakcji}" required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.monthly.transaction.report"/></td>
                    <td style="width: 170px" style="width: 170px"><eumowy:currencyField name="oplataZaMiesieczneZestawienieTransakcji" validatable="${data}" value="${data.oplataZaMiesieczneZestawienieTransakcji}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.confirmation.payment"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaPotwierdzenieWykonaniaPrzelewu" validatable="${data}" value="${data.oplataZaPotwierdzenieWykonaniaPrzelewu}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.paper"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaDostarczeniePapieru" validatable="${data}"  value="${data.oplataZaDostarczeniePapieru}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.graphics"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaZmianeGrafiki" validatable="${data}" value="${data.oplataZaZmianeGrafiki}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.instalation"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaInstalacjePOS" validatable="${data}" value="${data.oplataZaInstalacjePOS}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.gprs"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaInstalacjeGPRS" validatable="${data}" value="${data.oplataZaInstalacjeGPRS}"  required="true" width="120px"/></td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.foreing.currency"/></td>
                    <td style="width: 170px"><eumowy:currencyField name="oplataZaUruchomienieWalutyObcej" validatable="${data}" value="${data.oplataZaUruchomienieWalutyObcej}"  required="true" width="120px"/></td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>