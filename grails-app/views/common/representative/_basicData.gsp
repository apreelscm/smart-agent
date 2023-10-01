<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>
<g:hiddenField name="${prefix}[${seqNo}].midCBD" value="${representative?.midCBD}"/>
<g:hiddenField name="index" value="${seqNo}"/>
<g:hiddenField name="prefix" value="${prefix}"/>

<g:if test="${dropdowns}">
    <div>
        <g:radio class="isCBDDataChangedManually"
                 name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 checked="${representative?.isCBDDataChangedManually == true}"
                 value="${representative?.isCBDDataChangedManually}"/>
        <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message
                code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyTrue"/></label>

        <g:radio class="isCBDDataChangedManually"
                 name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 checked="${representative?.isCBDDataChangedManually == false}"
                 value="${!representative?.isCBDDataChangedManually}"/>
        <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message
                code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyFalse"/></label>

        <div>
            <eumowy:textField name="${prefix}[${seqNo}].salutation" value="${representative?.salutation}"
                              readonly="readonly" class="salutationField"
                              validatable="${representative}" validateField="salutation"/>

            <label for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
            <eumowy:select name="${prefix}[${seqNo}].name" from="${firstNames}" value="${representative?.name}"
                           class="nameField"
                           validatable="${representative}" style="min-width: 150px" validateField="name"/>

            <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
            <eumowy:select name="${prefix}[${seqNo}].surname" from="${lastNames}" value="${representative?.surname}"
                           class="surnameField"
                           validatable="${representative}" style="min-width: 150px" validateField="surname"/>

            <label for="${prefix}[${seqNo}].position"><g:message code="panel.position"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].position" value="${representative?.position}"
                              readonly="readonly" class="positionField"
                              validatable="${representative}" validateField="position"/>
        </div>
    </div>
</g:if>
<g:else>
    <div class="isCBDDataChangedManually">
        <g:hasErrors bean="${representative}" field="isCBDDataChangedManually">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 value="true"
                 checked="${representative?.isCBDDataChangedManually == true}"/>
        <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message
                code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyTrue"/></label>

        <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 value="false"
                 checked="${representative?.isCBDDataChangedManually == false}"/>
        <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message
                code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyFalse"/></label>
    </div>

    <div class="representativeCBDBasicData" id="representativeCBDBasicData">
        <g:select name="${prefix}[${seqNo}].salutation" from="['Pan', 'Pani']" valueMessagePrefix="person.title"
                  value="${representative?.salutation}"
                  disabled="${!representative?.isCBDDataChangedManually}"
                  validatable="${representative}" validateField="salutation"/>
        <g:hiddenField name="${prefix}[${seqNo}].salutation"
                       disabled="${representative?.isCBDDataChangedManually}"
                       cbdDataHiddenField="cbdDataHiddenField"
                       value="${representative?.salutation}"/>

        <label for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].name" value="${representative?.name}" maxlength="25"
                          readonly="${!representative?.isCBDDataChangedManually}"
                          validatable="${representative}" validateField="name"/>

        <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
        <eumowy:textField name="${prefix}[${seqNo}].surname" value="${representative?.surname}" maxlength="35"
                          class="surnameField"
                          readonly="${!representative?.isCBDDataChangedManually}"
                          validatable="${representative}" validateField="surname"/>

        <label for="${prefix}[${seqNo}].position"><g:message code="panel.position"/>:</label>
        <dict:positionSelect class="positionField"
                             medium="${representative?.position}"
                             data-index="${seqNo}"
                             disabled="${!representative?.isCBDDataChangedManually}"
                             id="${prefix}[${seqNo}].position"
                             name="${prefix}[${seqNo}].position" from="[]"
                             valueMessagePrefix=""
                             value="${representative?.position}"
                             validatable="${representative}"
                             validateField="position"/>
        <g:hiddenField name="${prefix}[${seqNo}].position"
                       disabled="${representative?.isCBDDataChangedManually}"
                       cbdDataHiddenField="cbdDataHiddenField"
                       value="${representative?.position}"/>
    </div>
</g:else>
