<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<div class="personData ${additionalClass}">
    <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'documentType', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="documentType">
            <g:eachError bean="${representative}" field="documentType">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].documentType"
                               radioWrapperClass="inlineRadioWrapper display-inline-block"
                               value="${data.isPersonForm() ? representative?.documentType : null}"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].documentNumber"><g:message code="identity.card.details"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}" maxlength="20"
                          validatable="${representative}" validateField="documentNumber"/>
    </div>

    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                     checked="${data.isPersonForm() && representative?.verification?.name() == "PESEL"}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block"
                              validatable="${representative}" validateField="pesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="BIRTH_DATE"
                     checked="${data.isPersonForm() && representative?.verification?.name() == "BIRTH_DATE"}"/>
            <label for="${prefix}[${seqNo}].birthDate"><g:message code="birth.date.country.label"/></label>

            <g:textField name="${prefix}[${seqNo}].birthDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10" class="date-field"/>

            <dict:countrySelect name="${prefix}[${seqNo}].birthCountry" value="${representative?.birthCountry}"
                                validatable="${representative}" validateField="birthCountry"/>
        </div>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].address"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].address" value="${representative?.address}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="address" required="required" />
    </div>

    <div class="${hasErrors(bean: representative, field: 'country', 'errorSpan')}">
        <label for="${prefix}[${seqNo}].country"><g:message code="country.name.label"/></label>
        <dict:countrySelect name="${prefix}[${seqNo}].country" value="${representative?.country}" required="required"
                            validatable="${representative}" validateField="country"/>
    </div>

    <div class="isPolitician ${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="isPolitician">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <span><g:message code="is.political.position.label"/></span>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="true" required="required"
                 checked="${data.isPersonForm() && representative?.isPolitician == true}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="false" required="required"
                 checked="${data.isPersonForm() && representative?.isPolitician == false}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="no"/></label>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].citizenship" value="${representative?.citizenship}" maxlength="30" required="required"
                          validatable="${representative}" validateField="citizenship"/>
    </div>
</div>