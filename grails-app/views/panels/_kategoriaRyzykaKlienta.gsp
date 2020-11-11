<div id="clientRiskCategory">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.clientRiskCategory.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <g:hasErrors bean="${data}" field="kategoriaRyzykaKlienta">
                <g:eachError bean="${data}" field="kategoriaRyzykaKlienta">
                    <p class="error-message"><g:message error="${it}"/></p>
                </g:eachError>
            </g:hasErrors>

            <ul class="table-list centre">
                <li>
                    <div class="${hasErrors(bean:data,field:'kategoriaRyzykaKlienta','errorContainer')}">
                        <span><g:message code="panel.risk"/></span>
                        <span><eumowy:textField id="clientsRiskCategory" name="kategoriaRyzykaKlienta" value="${data.kategoriaRyzykaKlienta}" validatable="${data}" readonly="true"/></span>
                    </div>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<script type="text/javascript">
    jQuery(document).ready(function() {
        var clientsRiskCategory = jQuery('#clientsRiskCategory');

        jQuery('#addNewPointPanelPlaceholder').on('change', '.newPointPanel [id $="risk"]', function () {
            var risks = jQuery.map(jQuery('[id^="points"][id $="risk"]'), function (e) {
                return e.value;
            });
            var hasAnyHighRisk = risks.some(function (v) { return v === 'HIGH' });

            if (hasAnyHighRisk) {
                clientsRiskCategory.val('Wysokie Ryzyko');
                return;
            }

            var hasAnyLowRisk = risks.some(function (v) { return v === 'LOW' });

            if (hasAnyLowRisk) {
                clientsRiskCategory.val('Niskie Ryzyko');
                return;
            }

            clientsRiskCategory.val('');
        });
    });
</script>