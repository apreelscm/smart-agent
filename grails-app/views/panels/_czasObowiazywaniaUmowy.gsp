<div id="aggrementTimePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.time.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 400px">
            <div style="text-align: left">
                <p><label><input type="radio" name="aggrementTime" id="notSpecified" /><g:message code="panel.aggrement.time.not.defined"/></label></p>
                <p><label><input type="radio" name="aggrementTime" id="specified" /><g:message code="panel.aggrement.time.defined"/></label></p>
            </div>
            <div id="aggrementDates">
                <div style="display:inline-block"><g:message code="panel.from"/> </div>
                <div style="display:inline-block"><g:textField name="aggrementStart" readonly="true" style="width: 120px;"/></div>
                <div style="display:inline-block"><g:message code="panel.to"/></div>
                <div style="display:inline-block"><g:textField name="aggrementEnd" readonly="true" style="width: 120px;"/></div>
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

        _aggrementStart.datepicker({ dateFormat: 'yy-mm-dd', minDate: new Date() });
        _aggrementEnd.datepicker({ dateFormat: 'yy-mm-dd', minDate: new Date() });

        _aggrementStart.on("change", function(){
            _aggrementEnd.datepicker("option", "minDate",  _aggrementStart.val());
        });

        _aggrementEnd.on("change", function(){
            _aggrementStart.datepicker("option", "maxDate",  _aggrementEnd.val());
        });
    });
</r:script>