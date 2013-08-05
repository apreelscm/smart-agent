<div id="annexPrepaidPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna">Aneks do Umowy Prepaid</div>
            <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
                <div style="display:inline-block; float: left;">Aneks z dnia</div>
                <div style="display:inline-block; float: right;"><g:textField name="annexPrepaidDate"/></div>
                <div style="clear: both;"/>
                <div style="display:inline-block; float: left; ">Do umowy zawartej w dniu</div>
                <div style="display:inline-block; float: right;"><g:textField name="aggrementPrepaidDate"/></div>
            </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#annexPrepaidDate").datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date() });
        jQuery("#aggrementPrepaidDate").datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date() });
    });
</r:script>