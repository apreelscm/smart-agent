<fieldset class="border">
    <legend><g:message code="panel.activity.title"/></legend>
    <div style="text-align: center">
        <ul>
            <li>
                <span><g:message code="panel.mcc"/></span>
                <span><dict:mccSelect name="scoringMcc" value="${data.scoringMcc}" required="required" style="width: 200px"/></span>
            </li>
        </ul>
    </div>
    <div style="text-align: left" class="${hasErrors(bean:data,field:'hasScoringDzialalnosc','errorSpan')}">
        <g:radioGroup name="scoringDzialalnosc"
                      labels="['panel.activity.trade','panel.activity.services']"
                      values="['handel','uslugi']"
                      value="${data.scoringDzialalnosc}">
            <p><label> ${it.radio} <g:message code="${it.label}"/></label></p>
        </g:radioGroup>
    </div>
    <div style="text-align: center">
        <ul>
            <li>
                <span><g:message code="panel.activity.details"/></span>
                <span><g:textField name="scoringSzczegolyDzialalnosci" value="${data.scoringSzczegolyDzialalnosci}" readonly="true"/></span>
            </li>
        </ul>
    </div>
</fieldset>

%{--<script type="text/javascript">--}%
    %{--"use strict";--}%
    %{--jQuery("#scoringMcc").change(function() {--}%
        %{--var $this = jQuery(this),--}%
            %{--chosenMcc = $this.find('option:selected').text(),--}%
            %{--activityType = chosenMcc ? chosenMcc.split('-')[1].trim() : '';--}%

        %{--jQuery("#scoringSzczegolyDzialalnosci").val(activityType);--}%
    %{--});--}%
%{--</script>--}%