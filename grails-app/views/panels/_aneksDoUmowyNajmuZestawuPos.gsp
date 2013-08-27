<div id="annexHirePOSPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.annex.hire.pos.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-right"><g:message code="panel.aggrement.annex.day"/></span>
                    <span><g:textField name="dataAneksowanejUmowyPos" value="${data.dataAneksowanejUmowyPos ? Date.parse('yyyy-MM-dd',data.dataAneksowanejUmowyPos):''}"
                                       style="width: 120px;" readonly="true"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#dataAneksowanejUmowyPos").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</r:script>
