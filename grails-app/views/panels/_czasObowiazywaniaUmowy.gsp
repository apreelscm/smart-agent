<div id="aggrementTimePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.time.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li><span class="align-left"><label><input type="radio" name="aggrementTime" id="notSpecified" /><g:message code="panel.aggrement.time.not.defined"/></label></span></li>
                <li><span class="align-left"><label><input type="radio" name="aggrementTime" id="specified" /><g:message code="panel.aggrement.time.defined"/></label></span></li>
            </ul>
            <ul>
                <li id="aggrementDates">
                    <span>
                        <span><g:message code="panel.from"/></span>
                        <span><g:textField name="aggrementStart" readonly="true" style="width: 120px;"/></span>
                        <span><g:message code="panel.to"/></span>
                        <span><g:textField name="aggrementEnd" readonly="true" style="width: 120px;"/></span>
                    </span>
                </li>
            </ul>
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