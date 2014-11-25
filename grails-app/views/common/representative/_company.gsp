<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation" %>
<div id="companyData" class="acceptorCountry ${additionalClass}">
    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'locationType', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="locationType">
            <g:eachError bean="${representative}" field="locationType">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                     checked="${representative?.verification}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block"
                              validatable="${representative}" validateField="pesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="BIRTH_DATE"
                     checked="${representative?.verification}"/>
            <label for="${prefix}[${seqNo}].birthDate"><g:message code="birth.date.label"/></label>

            <g:textField name="${prefix}[${seqNo}].birthDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10" class="date-field"/>
        </div>
    </div>

    <div>
        <eumowy:enumRadioGroup values="${AcceptorLocation.values()}" name="${prefix}[${seqNo}].locationType" value="${representative?.locationType}"
                               radioWrapperClass="acceptorLocationRadioWrapper"/>
    </div>

    <div class="isPolitician ${representative?.isRepresentativeLocationAbroad() ?: 'hidden'}">
        <g:radioGroup values="[true, false]" name="${prefix}[${seqNo}].isPolitician" value="${representative?.isPolitician}"
                      labels="['i.am', 'i.am.not']">
            <div class="acceptorRadioWrapper">
                ${it.radio}
                <div class="label"><g:message code="${it.label}"/></div>
            </div>
        </g:radioGroup>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].address"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].address" value="${representative?.address}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="address"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].citizenship" value="${representative?.citizenship}" maxlength="30"
                          validatable="${representative}" validateField="citizenship"/>
    </div>
</div>