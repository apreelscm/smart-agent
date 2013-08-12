<div id="servicePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.service.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span class="align-left"><label><input type="radio" name="serviceType" id="prestige" /> <g:message code="panel.service.prestige.name"/></label></span>
                </li>
                <li>
                    <span class="align-left"><label><input type="radio" name="serviceType" id="comfort" /> <g:message code="panel.service.comfort.name"/></label></span>
                </li>
                <li>
                    <span class="align-left"><label><input type="radio" name="serviceType" id="economic" /> <g:message code="panel.service.economic.name"/></label></span>
                </li>
            </ul>
            <ul table-list>
                <li id="servicePayment">
                    <span>
                        <span><g:message code="panel.monthly.payment"/> <g:textField name="serviceEconomicPrice" style="width: 150px"/> <g:message code="panel.polish.currency"/></span>
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