<div class="sharedRepresentativeData">
    <div class="hasSignedContract ${hasErrors(bean: representative, field: 'hasSignedContract', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="hasSignedContract">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <span><g:message code="person.signed.contract"/></span>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract" value="true"
                 checked="${data.isPersonForm() && representative?.hasSignedContract == true}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract" value="false"
                 checked="${data.isPersonForm() && representative?.hasSignedContract == false}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="no"/></label>
    </div>
</div>