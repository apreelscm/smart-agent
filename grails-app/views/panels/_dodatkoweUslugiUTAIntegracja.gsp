<div id="additionalServicesUTAPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services.uta.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 600px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-left"><g:message code="panel.payment.uta"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any" name="weryfikacjaPINCena" value="${data.weryfikacjaPINCena}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.payment.integration.with.system"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any" name="systemKasowyCena" value="${data.systemKasowyCena}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>