<fieldset id="cashbackInfo">
    <header class="belka-glowna"><g:message code="cashback.info.title"/></header>

    <div class="centre">
        <table class="centre" style="width: 720px; padding-left: 120px">
            <tbody>
                <tr>
                    <td><g:message code="cashback.payment.discount.label"/></td>
                    <td>${data.cashbackUpust} zł</td>
                </tr>
                <tr>
                    <td><g:message code="cashback.subscription.label"/></td>
                    <td>${data.cashbackAbonament} zł</td>
                </tr>
            </tbody>
        </table>

        <g:hiddenField name="cashbackUpust" value="0"/>
        <g:hiddenField name="cashbackAbonament" value="5"/>
    </div>
</fieldset>