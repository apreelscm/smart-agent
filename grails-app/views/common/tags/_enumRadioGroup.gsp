<g:if test="${required}">
    <g:radioGroup values="${values}" name="${name}"
                  labels="${values*.messageCode}"
                  value="${value}" required="required">
        <div class="${radioWrapperClass ?: ''}">
            ${it.radio}
            <div class="label"><g:message code="${it.label}"/></div>

            <g:if test="${additionalView}">
                <g:render template="/common/tags/enumRadioGroupViews/${additionalView}" model="['radio': it]"/>
            </g:if>
        </div>
    </g:radioGroup>
</g:if>
<g:else>
    <g:radioGroup values="${values}" name="${name}"
                  labels="${values*.messageCode}"
                  value="${value}">
        <div class="${radioWrapperClass ?: ''}">
            ${it.radio}
            <div class="label"><g:message code="${it.label}"/></div>

            <g:if test="${additionalView}">
                <g:render template="/common/tags/enumRadioGroupViews/${additionalView}" model="['radio': it]"/>
            </g:if>
        </div>
    </g:radioGroup>
</g:else>
