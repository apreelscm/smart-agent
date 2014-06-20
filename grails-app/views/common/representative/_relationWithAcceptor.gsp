<p><g:message code="beneficiary.relation.with.acceptor.label"/></p>

<div class="${hasErrors(bean: representative, field: 'posiadaAkceptanta', 'errorSpan')}">
    <g:hasErrors bean="${representative}" field="posiadaAkceptanta">
        <g:eachError bean="${representative}" field="posiadaAkceptanta">
            <p class="error-message"><g:message error="${it}"/></p>
        </g:eachError>
    </g:hasErrors>

    <div>
        <g:checkBox name="beneficiaries[${seqNo}].posiadaAkceptanta" checked="${representative?.posiadaAkceptanta}"/>
        <label for="beneficiaries[${seqNo}].posiadaAkceptanta"><g:message code="beneficiary.owns.acceptor.label"/></label>
    </div>

    <div>
        <g:checkBox name="beneficiaries[${seqNo}].kontrolujeAkceptanta" checked="${representative?.kontrolujeAkceptanta}"/>
        <label for="beneficiaries[${seqNo}].kontrolujeAkceptanta"><g:message code="beneficiary.controls.acceptor.label"/></label>
    </div>

    <div>
        <g:checkBox name="beneficiaries[${seqNo}].znaczaceUdzialy" checked="${representative?.znaczaceUdzialy}"/>
        <label for="beneficiaries[${seqNo}].znaczaceUdzialy"><g:message code="beneficiary.majority.acceptor.label"/></label>

        <eumowy:textField name="beneficiaries[${seqNo}].procentUdzialow" class="percent-short"
                          value="${representative?.znaczaceUdzialy ? representative?.procentUdzialow : ""}"
                          validatable="${representative}" validateField="procentUdzialow"/>
        <g:message code="beneficiary.majority.acceptor.closing.label"/>
    </div>
</div>