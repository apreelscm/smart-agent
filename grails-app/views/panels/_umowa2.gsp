<div id="aggrementPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
            <div style="display:inline-block"><g:message code="panel.aggrement.place"/>: </div>
            <div style="display:inline-block"><g:textField name="aggrementPlace"/></div>
            <div style="display:inline-block"><g:message code="panel.aggrement.date"/>: </div>
            <div style="display:inline-block"><g:textField name="aggrementDate" style="width: 120px;" readonly="true"/></div>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#aggrementDate").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</r:script>