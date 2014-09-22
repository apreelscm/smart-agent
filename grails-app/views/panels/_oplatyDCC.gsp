<div id="dccPaymentPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.dcc.payment.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li class="align-center">
                    <span class="align-right"><g:message code="panel.payment.foreing.currency2"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="oplataZaPlatnoscWInnejWalucie" validatable="${data}" value="${data.oplataZaPlatnoscWInnejWalucie}" readonly="readonly"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>