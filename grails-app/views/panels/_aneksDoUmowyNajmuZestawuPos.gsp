<div id="annexHirePOSPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.annex.hire.pos.title"/> </div>
            <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
                <div style="display:inline-block; float: left; "><g:message code="panel.aggrement.annex.day"/> </div>
                <div style="display:inline-block; float: right;"><g:textField name="aggrementPOSDate" readonly="true" style="width: 120px;"/></div>
            </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#aggrementPOSDate").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</r:script>