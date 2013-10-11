<div id="loyaltyPeriodPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.loyalty.period.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-right"><g:message code="panel.loyality.period"/></span>
                    <span class="align-left">
                        %{--<g:field type="number" step="any" name="okresLojalnosciowy" readonly="true" style="width: 100px"/> --}%
                        <eumowy:textField name="okresLojalnosciowy" validatable="${data}" value="${data.okresLojalnosciowy}"  readonly="true"
                                            postfix="${message(code:"panel.months")}"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>