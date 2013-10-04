<div id="servicePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="default.atLeastOne.obslugaTyp"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <div class="${hasErrors(bean:data,field:'hasObslugaTyp','errorSpan')}">
                <g:hiddenField name="hasObslugaTyp" value="true"/>
                <g:radioGroup name="obslugaTyp"
                              labels="['panel.service.prestige.name','panel.service.comfort.name', 'panel.service.economic.name']"
                              values="['prestige', 'comfort', 'economic']"
                              value="${data.obslugaTyp}">
                    <li><span class="align-left"><label> ${it.radio} <g:message code="${it.label}"/></label></span></li>
                </g:radioGroup>
                </div>
            </ul>
            <ul class="table-list centre">
                <li id="servicePayment">
                    <span class="align-left"><g:message code="panel.monthly.payment"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="obslugaEkonomicznyCena" validatable="${data}" value="${data.obslugaEkonomicznyCena}" />
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