<div id="loyaltyPeriodPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.loyalty.period.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-right"><g:message code="panel.loyality.period"/></span>
                    <span class="align-left">
                        <eumowy:textField name="okresLojalnosciowy" validatable="${data}" value="${data.okresLojalnosciowy}" readonly="true"
                                            postfix="${message(code:"panel.months")}" class="align-right"/>
                    </span>
                </li>

                <li>
                    <span class="align-right"><g:message code="uninstall.price.title"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="oplataDeinstalacyjna" validatable="${data}" value="${data.oplataDeinstalacyjna}" readonly="readonly"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>