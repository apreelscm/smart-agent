<div id="servicePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.service.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 400px">
            <div style="text-align: left">
                <p><input type="radio" name="serviceType" id="prestige" /> <g:message code="panel.service.prestige.name"/></p>
                <p><input type="radio" name="serviceType" id="comfort" /> <g:message code="panel.service.comfort.name"/></p>
                <p><input type="radio" name="serviceType" id="economic" /> <g:message code="panel.service.economic.name"/></p>
            </div>
            <div id="servicePayment">
                <div style="display:inline-block"><g:message code="panel.monthly.payment"/> </div>
                <div style="display:inline-block"><g:textField name="serviceEconomicPrice" /></div>
                <div style="display:inline-block"> <g:message code="panel.polish.currency"/></div>
            </div>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        var servicePayment = jQuery('#servicePayment');
        servicePayment.hide();
        jQuery('input[name="serviceType"]').change(function(){
            if (jQuery("#prestige").attr("checked")){
                servicePayment.hide();
            } else if (jQuery("#comfort").attr("checked")){
                servicePayment.hide();
            } else {
                servicePayment.show();
            }
        });
    });
</r:script>