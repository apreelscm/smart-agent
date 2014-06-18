<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>

<g:if test="${dropdowns}">
    <div>
        <eumowy:textField name="${prefix}[${seqNo}].tytul" value="${representative?.tytul}" validatable="${representative}" readonly="readonly" class="tytulField"/>

        <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
        <g:select name="${prefix}[${seqNo}].imie" from="${firstNames}" value="${representative?.imie}" validatable="${representative}" class="imieField"/>

        <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
        <g:select name="${prefix}[${seqNo}].nazwisko" from="${lastNames}" value="${representative?.nazwisko}" validatable="${representative}" class="nazwiskoField"/>

        <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}" validatable="${representative}" readonly="readonly" class="positionField"/>
    </div>
</g:if>
<g:else>
    <div>
        <g:select name="${prefix}[${seqNo}].tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${representative?.tytul}" validatable="${representative}"/>

        <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].imie" value="${representative?.imie}" validatable="${representative}" maxlength ="13" required="true"/>

        <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].nazwisko" value="${representative?.nazwisko}" validatable="${representative}" maxlength ="35" required="true" class="nazwiskoField"/>

        <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}" validatable="${representative}"/>
    </div>
</g:else>