<div id="additionalServices2Panel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services2.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 400px" class="centre">
            <table class="table-list centre">
                <tr>
                    <td><g:message code="panel.payment.logo"/></td>
                    <td>
                        <eumowy:currencyField name="wydrukGrafikiCena" value="${data.wydrukGrafikiCena}"  readonly="true"/>
                    </td>
                </tr>
                <tr>
                     <td><g:message code="panel.payment.calc"/></td>
                     <td>
                        <eumowy:currencyField  name="dzialaniaMatematyczneCena" value="${data.dzialaniaMatematyczneCena}"  readonly="true"/>
                    </td>
                </tr>
                <tr>
                     <td><g:message code="panel.payment.title.payment"/></td>
                     <td>
                        <eumowy:currencyField name="tytulPlatnosciCena" validatable="${data}" value="${data.tytulPlatnosciCena}" />
                    </td>
                </tr>
                <tr>
                     <td><g:message code="panel.payment.first.session"/></td>
                     <td>
                        <eumowy:currencyField name="pierwszaSesjaCena" validatable="${data}" value="${data.pierwszaSesjaCena}" />
                    </td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>