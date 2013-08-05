<div id="aggrementPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna">Umowa</div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
            <div style="display:inline-block">Miejsce podpisania umowy: </div>
            <div style="display:inline-block"><g:textField name="aggrementPlace"/></div>
            <div style="display:inline-block">Data: </div>
            <div style="display:inline-block"><g:textField name="aggrementDate" style="width: 120px;"/></div>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#aggrementDate").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</r:script>