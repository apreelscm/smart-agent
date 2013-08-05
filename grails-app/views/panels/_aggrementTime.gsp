<div id="aggrementTimePanel">
    <fieldset>
        <div class="belka-glowna">Czas obowiązywania umowy</div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 400px">
            <div style="text-align: left">
                <p><input type="radio" name="aggrementTime" id="notSpecified" /> Czas nieoznaczony</p>
                <p><input type="radio" name="aggrementTime" id="specified" /> Czas oznaczony</p>
            </div>
            <div id="aggrementDates">
                <div style="display:inline-block">od </div>
                <div style="display:inline-block"><g:textField name="aggrementStart" /></div>
                <div style="display:inline-block">do </div>
                <div style="display:inline-block"><g:textField name="aggrementEnd" /></div>
            </div>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        var aggDates = jQuery('#aggrementDates');
        aggDates.hide();
        jQuery('input[name="aggrementTime"]').change(function(){
            if (jQuery("#notSpecified").attr("checked")){
                aggDates.hide();
            } else {
                aggDates.show();
            }
        });


        var _aggrementStart = jQuery("#aggrementStart");
        var _aggrementEnd = jQuery("#aggrementEnd");

        _aggrementStart.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });
        _aggrementEnd.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });

        _aggrementStart.on("change", function(){
            _aggrementEnd.datepicker("option", "minDate",  _aggrementStart.val());
        });

        _aggrementEnd.on("change", function(){
            _aggrementStart.datepicker("option", "maxDate",  _aggrementEnd.val());
        });
    });
</r:script>