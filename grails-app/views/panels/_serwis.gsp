<div id="servicePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.service.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <g:radioGroup name="obslugaTyp"
                              labels="['panel.service.prestige.name','panel.service.comfort.name', 'panel.service.economic.name']"
                              values="['prestige', 'comfort', 'economic']"
                              value="${data.obslugaTyp}">
                    <li><span class="align-left"><label> ${it.radio} <g:message code="${it.label}"/></label></span></li>
                </g:radioGroup>
            </ul>
            <ul table-list>
                <li id="servicePayment">
                    <span>
                        <span><g:message code="panel.monthly.payment"/> <g:textField name="obslugaEkonomicznyCena" value="${data.obslugaEkonomicznyCena}" style="width: 150px"/> <g:message code="panel.polish.currency"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        var servicePayment = jQuery('#servicePayment');

        if (jQuery('input[name="obslugaTyp"]:checked').val() != 'economic'){
            servicePayment.hide();
        };

        jQuery('input[name="obslugaTyp"]').change(function(e){
            if (e.target.value == 'prestige'){
                servicePayment.hide();
            } else if (e.target.value == 'comfort'){
                servicePayment.hide();
            } else {
                servicePayment.show();
            }
        });
    });
</r:script>