<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>

<g:if test="${dropdowns}">
    <div>
        <eumowy:textField name="${prefix}[${seqNo}].tytul" value="${representative?.tytul}" readonly="readonly" class="tytulField"
                          validatable="${representative}" validateField="tytul" />

        <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
        <eumowy:select name="${prefix}[${seqNo}].imie" from="${firstNames}" value="${representative?.imie}" class="imieField"
                       validatable="${representative}" validateField="imie"/>

        <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
        <eumowy:select name="${prefix}[${seqNo}].nazwisko" from="${lastNames}" value="${representative?.nazwisko}" class="nazwiskoField"
                       validatable="${representative}" validateField="nazwisko"/>

        <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}" readonly="readonly" class="positionField"
                          validatable="${representative}" validateField="stanowisko"/>
    </div>
</g:if>
<g:else>
    <div>
        <g:select name="${prefix}[${seqNo}].tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${representative?.tytul}"
                  validatable="${representative}" validateField="tytul"/>

        <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].imie" value="${representative?.imie}" maxlength ="13"
                          validatable="${representative}" validateField="imie"/>

        <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].nazwisko" value="${representative?.nazwisko}" maxlength ="35" class="nazwiskoField"
                          validatable="${representative}" validateField="nazwisko"/>

        <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}"
                          validatable="${representative}" validateField="stanowisko"/>
    </div>
</g:else>