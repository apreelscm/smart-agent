<div id="additionalServicesMudPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services.mud.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-left"><g:message code="panel.payment.mud"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="mudCena" validatable="${data}" value="${data.mudCena}" offset="30" />
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>