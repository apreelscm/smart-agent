<div class="acceptorCountry ${additionalClass}">
    <div class="acceptorPESELWrapper">
        <label for="${prefix}[${seqNo}].pesel"><g:message code="pesel.label"/></label>
        <g:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}" maxlength="11" class="pesel-field"/>
    </div>
</div>