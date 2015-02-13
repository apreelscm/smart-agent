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

    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'locationType', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="locationType">
            <g:eachError bean="${representative}" field="locationType">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                     checked="${data.isPersonForm() && "PESEL".equals(representative?.verification?.name())}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block"
                              validatable="${representative}" validateField="pesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="COUNTRY_CODE"
                     checked="${data.isPersonForm() && "COUNTRY_CODE".equals(representative?.verification?.name())}"/>
            <div class="label"><g:message code="country.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].countryCode" value="${representative?.countryCode}"
                         maxlength="30" class="display-inline-block"
                         validatable="${representative}" validateField="countryCode"/>
        </div>
    </div>

    <div class="${hasErrors(bean: representative, field: 'locationType', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="locationType">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <eumowy:enumRadioGroup values="${AcceptorLocation.values()}" name="${prefix}[${seqNo}].locationType"
                               value="${data.isPersonForm() ? representative?.locationType : null}"
                               radioWrapperClass="acceptorLocationRadioWrapper"/>
    </div>

    <div class="isPolitician ${representative?.isRepresentativeLocationAbroad() ?: 'hidden'} ${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="isPolitician">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="true"
                 checked="${data.isPersonForm() && representative?.isPolitician}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="i.am"/></label>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="false"
                 checked="${data.isPersonForm() && !representative?.isPolitician}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="i.am.not"/></label>
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