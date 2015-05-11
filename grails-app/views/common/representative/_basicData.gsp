<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>

<g:if test="${dropdowns}">
    <div>
        <eumowy:textField name="${prefix}[${seqNo}].salutation" value="${representative?.salutation}" readonly="readonly" class="salutationField"
                          validatable="${representative}" validateField="salutation" />

        <label for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
        <eumowy:select name="${prefix}[${seqNo}].name" from="${firstNames}" value="${representative?.name}" class="nameField"
                       validatable="${representative}" style="min-width: 150px" validateField="name"/>

        <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
        <eumowy:select name="${prefix}[${seqNo}].surname" from="${lastNames}" value="${representative?.surname}" class="surnameField"
                       validatable="${representative}" style="min-width: 150px" validateField="surname"/>

        <label for="${prefix}[${seqNo}].position"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].position" value="${representative?.position}" readonly="readonly" class="positionField"
                          validatable="${representative}" validateField="position"/>
    </div>
</g:if>
<g:else>
    <div>
        <g:select name="${prefix}[${seqNo}].salutation" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${representative?.salutation}"
                  validatable="${representative}" validateField="salutation"/>

        <label for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].name" value="${representative?.name}" maxlength ="13"
                          validatable="${representative}" validateField="name"/>

        <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].surname" value="${representative?.surname}" maxlength ="35" class="surnameField"
                          validatable="${representative}" validateField="surname"/>

        <label for="${prefix}[${seqNo}].position"><g:message code="panel.position"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].position" value="${representative?.position}"
                          validatable="${representative}" validateField="position"/>
    </div>
</g:else>