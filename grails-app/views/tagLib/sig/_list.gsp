<g:if test="${signaturesList.size() > 0}">
    <apreel:selectField data-activity="${activity.id}" data-id="sig1" id="act_${activity.id}_sig1" name="activitySignature_${activity.id}"
                        title="${message(code:'signature' + listNumber + '.sygnaturaDokumentu.' + activity.code + '.name')}"
                        from="${signaturesList}"
                        optionKey="id"
                        optionValue="signature"
                        value="${selectedOption}"
                        noSelection="[null: '']"/>

    <g:each var="sig" in="${signaturesList}">
        <g:if test="${sig.signature.description != null}">
            <p class="sig-${activity.id}-description-1" id="sig_${sig.id}_desc" style="display: none;">${sig.signature.description}</p>
        </g:if>
    </g:each>
</g:if>
