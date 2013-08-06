<div id="servicePanel">
    <fieldset>
        <div class="belka-glowna">Serwis</div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 400px">
            <div style="text-align: left">
                <p><input type="radio" name="serviceType" id="prestige" /> Prestiż</p>
                <p><input type="radio" name="serviceType" id="comfort" /> Komfort</p>
                <p><input type="radio" name="serviceType" id="economic" /> Ekonomiczny</p>
            </div>
            <div id="servicePayment">
                <div style="display:inline-block">opłata mies. </div>
                <div style="display:inline-block"><g:textField name="serviceEconomicPrice" /></div>
                <div style="display:inline-block"> zł</div>
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

//        var _aggrementStart = jQuery("#aggrementStart");
//        var _aggrementEnd = jQuery("#aggrementEnd");
//
//        _aggrementStart.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });
//        _aggrementEnd.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });
//
//        _aggrementStart.on("change", function(){
//            _aggrementEnd.datepicker("option", "minDate",  _aggrementStart.val());
//        });
//
//        _aggrementEnd.on("change", function(){
//            _aggrementStart.datepicker("option", "maxDate",  _aggrementEnd.val());
//        });
    });
</r:script>