<div id="clientRiskCategory">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.clientRiskCategory.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <g:hasErrors bean="${data}" field="katRyzykaKlienta">
                <g:eachError bean="${data}" field="katRyzykaKlienta">
                    <p class="error-message"><g:message error="${it}"/></p>
                </g:eachError>
            </g:hasErrors>

            <ul class="table-list centre">
                <g:hiddenField id="clientsRiskCategory" name="katRyzykaKlienta" value="${data.katRyzykaKlienta}"/>
                <li>
                    <div class="${hasErrors(bean:data,field:'katRyzykaKlienta','errorContainer')}">
                        <span><g:message code="panel.risk"/></span>
                        <span><eumowy:textField id="clientsRiskCategoryValue" name="katRyzykaKlientaWartosc" value="${data.katRyzykaKlientaWartosc}" readonly="true"/></span>
                    </div>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<script type="text/javascript">

    var mapToText = function(value) {
        if (value === 'HIGH') return 'Wysokie ryzyko';

        if (value === 'LOW') return 'Niskie ryzyko';

        return '';
    }

    function refreshKategorieRyzyka() {
        var clientsRiskCategory = jQuery('#clientsRiskCategory');
        var clientsRiskCategoryValue = jQuery('#clientsRiskCategoryValue');

        var risks = jQuery.map(jQuery('[id^="points"][id $="risk"]'), function (e) {
            return e.value;
        });
        var hasAnyHighRisk = risks.some(function (v) { return v === 'HIGH' });

        if (hasAnyHighRisk) {
            clientsRiskCategory.val('HIGH');
            clientsRiskCategoryValue.val(mapToText('HIGH'));
            return;
        }

        var hasAnyLowRisk = risks.some(function (v) { return v === 'LOW' });

        if (hasAnyLowRisk) {
            clientsRiskCategory.val('LOW');
            clientsRiskCategoryValue.val(mapToText('LOW'));
            return;
        }

        clientsRiskCategory.val('');
        clientsRiskCategoryValue.val('');
    }

    jQuery(document).ready(function() {
        jQuery('.newPointPanel [id $="risk"]').on('change', refreshKategorieRyzyka);

        jQuery('#addNewPointPanelPlaceholder').on('change', '.newPointPanel [id $="risk"]', refreshKategorieRyzyka);
    });
</script>