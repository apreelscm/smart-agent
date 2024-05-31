<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>
<g:hiddenField name="${prefix}[${seqNo}].midCBD" value="${representative?.midCBD}"/>
<g:hiddenField name="index" value="${seqNo}"/>
<g:hiddenField name="prefix" value="${prefix}"/>

<div class="isCBDDataChangedManually">
    <g:hasErrors bean="${representative}" field="isCBDDataChangedManually">
        <p class="error-message"><g:message code="representative.option.required"/></p>
    </g:hasErrors>

    <label>
        <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 value="true"
                 checked="${representative?.isCBDDataChangedManually == true}"/>

        <g:message code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyTrue"/>
    </label>

    <label>
        <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually"
                 value="false"
                 checked="${representative?.isCBDDataChangedManually == false}"/>

        <g:message code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyFalse"/>
    </label>
</div>

<div class="additionalData ${data.isFromBisnode ?: 'hidden'}">
    <label>
        <g:checkBox name="${prefix}[${seqNo}].additionalData"
                    disabled="${representative?.isCBDDataChangedManually == true}"
                    value="${representative?.isCBDDataChangedManually == false && representative?.additionalData}"/>

        <g:message code="com.eservice.eumowy.command.RepresentativeCommand.additionalData"/>

        <strong class="tooltip" title="Do ustalenia"><g:message code="representative.pep.description"/></strong>
    </label>
</div>

<div class="representativeCBDBasicData" id="representativeCBDBasicData">
    <g:select name="${prefix}[${seqNo}].salutation" from="['Pan', 'Pani']" valueMessagePrefix="person.title"
              value="${representative?.salutation}"
              disabled="${!representative?.isCBDDataChangedManually}"
              validatable="${representative}" validateField="salutation"/>
    <g:hiddenField name="${prefix}[${seqNo}].salutation"
                   disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
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
                   disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                   cbdDataHiddenField="cbdDataHiddenField"
                   value="${representative?.position}"/>
</div>
