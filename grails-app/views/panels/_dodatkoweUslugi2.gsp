<div id="additionalServicesTable">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services.table.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 400px" class="centre">
            <table class="table-list centre">
                <tr class="${data.wydrukGrafikiCena ?: 'display-none'}">
                    <td><g:message code="panel.payment.logo"/></td>
                    <td>
                        <eumowy:currencyField type="text" name="wydrukGrafikiCena" value="${data.wydrukGrafikiCena}"  readonly="true"/>
                    </td>
                </tr>
                <tr  class="${data.dzialaniaMatematyczneCena ?: 'display-none'}">
                    <td><g:message code="panel.payment.calc"/></td>
                    <td>
                        <eumowy:currencyField type="text" name="dzialaniaMatematyczneCena" value="${data.dzialaniaMatematyczneCena}" readonly="true"/>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.first.session"/></td>
                    <td>
                        <eumowy:currencyField type="text" name="pierwszaSesjaCena" validatable="${data}" value="${data.pierwszaSesjaCena}"
                                              readonly="${data.isBundleActivity}"/>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="panel.payment.mud"/></td>
                    <td>
                        <eumowy:currencyField type="text" class="float-number" name="mudCena" readonly="${data.isBundleActivity}" validatable="${data}" value="${data.mudCena}" />
                    </td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>