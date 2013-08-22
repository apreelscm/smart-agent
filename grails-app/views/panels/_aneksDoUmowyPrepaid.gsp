<div id="annexPrepaidPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.annex.prepaid.title"/></div>
            <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
                <ul class="table-list centre">
                    <li>
                        <span><g:message code="panel.aggrement.annex.day"/></span>
                        <span><g:textField name="dataPodpisaniaAneksuPOZ" readonly="true" style="width: 120px;"/></span>
                    </li>
                </ul>
            </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#dataPodpisaniaAneksuPOZ").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</r:script>