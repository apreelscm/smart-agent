<div id="statementChangeConfirmDialog" style="display: none;">
    <g:message code="panel.statementOfRequestToStartTheService.question"/>
</div>

<div id="statementOfRequestToStartTheService" class="${(data.isPersonForm() == false) ? 'hidden' : ''}">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.statementOfRequestToStartTheService.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <g:hasErrors bean="${data}" field="klauWykonaniaUslugi">
                <g:eachError bean="${data}" field="klauWykonaniaUslugi">
                    <p class="error-message"><g:message error="${it}"/></p>
                </g:eachError>
            </g:hasErrors>

            <ul class="table-list centre">
                <li>
                    <div class="${hasErrors(bean:data,field:'klauWykonaniaUslugi','errorContainer')}">
                        <div><g:message code="panel.statementOfRequestToStartTheService.description1"/></div>
                        <div style="margin: 8px 0">
                            <g:radioGroup name="klauWykonaniaUslugi"
                                          labels="['panel.statementOfRequestToStartTheService.true','panel.statementOfRequestToStartTheService.false']"
                                          values="['true', 'false']"
                                          value="${data.klauWykonaniaUslugi}">
                                <div><label> ${it.radio} <g:message code="${it.label}"/></label></div>
                            </g:radioGroup>
                        </div>
                        <div><g:message code="panel.statementOfRequestToStartTheService.description2"/></div>
                    </div>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<script type="text/javascript">
    jQuery(document).ready(function() {
        var panel = jQuery('div#statementOfRequestToStartTheService'),
            requestToStartTheServiceRadio = jQuery('input[type=radio][name="klauWykonaniaUslugi"]'),
            requestToStartTheServiceRadioTrue = jQuery('input[type=radio][name="klauWykonaniaUslugi"][value="true"]');

        jQuery('select#dzialalnoscForma').on('change', function (e) {
            if (e.target.value === 'PERSON' || e.target.value === 'PARTNERSHIP_COMPANY') {
                requestToStartTheServiceRadioTrue.prop('checked', true);
                panel.removeClass('hidden');
            } else {
                panel.addClass('hidden');
                requestToStartTheServiceRadio.removeAttr('checked');
            }
        });

        requestToStartTheServiceRadio.on('change', function (e) {
            if (e.target.value === 'false') {
                jQuery("#statementChangeConfirmDialog").dialog({
                    resizable: true,
                    height:200,
                    width: 450,
                    modal: true,
                    buttons: {
                        "Tak": function() {
                            jQuery(this).dialog("close");
                        },
                        "Nie": function() {
                            requestToStartTheServiceRadioTrue.prop('checked', true);
                            jQuery(this).dialog("close");
                        }
                    }
                });
            }
        })
    });
</script>