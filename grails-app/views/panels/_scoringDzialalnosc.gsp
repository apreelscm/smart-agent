<fieldset class="border">
    <legend><g:message code="panel.activity.title"/></legend>
    <div style="text-align: center">
        <ul>
            <li>
                <span><g:message code="panel.mcc"/></span>
                <span><eumowy:textField name="scoringMcc" value="${data.scoringMcc}" validatable="${data}" readonly="true"/></span>
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