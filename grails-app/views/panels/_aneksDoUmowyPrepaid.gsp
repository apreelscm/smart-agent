<div id="annexPrepaidPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.annex.prepaid.title"/></div>
            <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
                <ul class="table-list centre">
                    <li>
                        <span><g:message code="panel.aggrement.annex.day"/></span>
                        <span><eumowy:textField name="dataAneksowanejUmowyPrepaid"
                                           value="${data.dataAneksowanejUmowyPrepaid}" validatable="${data}" readonly="true" style="width: 120px;"/></span>
                    </li>
                </ul>
            </div>
    </fieldset>
</div>

<script type="text/javascript">
    jQuery(document).ready(function() {
        jQuery("#dataAneksowanejUmowyPrepaid").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
    });
</script>