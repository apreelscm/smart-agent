<div class="acceptorCountry ${additionalClass}">
    <div class="acceptorPESELWrapper">
        <label for="${prefix}[${seqNo}].pesel"><g:message code="pesel.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}" maxlength="11" class="pesel-field"
                          validatable="${representative}" validateField="pesel"/>
    </div>
</div>