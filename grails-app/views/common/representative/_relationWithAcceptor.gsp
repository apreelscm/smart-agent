<p><g:message code="beneficiary.relation.with.acceptor.label"/></p>

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

    <g:textField name="beneficiaries[${seqNo}].procentUdzialow" class="percent-short" value="${representative?.procentUdzialow}"/>
    <g:message code="beneficiary.majority.acceptor.closing.label"/>
</div>