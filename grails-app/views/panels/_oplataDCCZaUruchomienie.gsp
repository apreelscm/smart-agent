<div id="dccStartupPaymentPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.dcc.startup.payment.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <ul class="table-list">
                <li>
                    <span class="align-right"><g:message code="panel.payment.startup"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="oplataZaUruchomienieDCC" validatable="${data}" value="${data.oplataZaUruchomienieDCC}" required="true"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>