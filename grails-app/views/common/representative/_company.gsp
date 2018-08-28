<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<div class="companyData ${additionalClass}">
    <div class="acceptorDocumentInfoWrapper ${representative?.position == 'Pełnomocnik' ?: 'hidden'}">
        <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'documentType', 'errorSpan')}">
            <g:hasErrors bean="${representative}" field="documentType">
                <g:eachError bean="${representative}" field="documentType">
                    <p class="error-message"><g:message error="${it}"/></p>
                </g:eachError>
            </g:hasErrors>

            <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].documentType"
                                   radioWrapperClass="inlineRadioWrapper display-inline-block"
                                   value="${representative?.documentType}"/>
        </div>

        <div>
            <label for="${prefix}[${seqNo}].documentNumber"><g:message code="identity.card.details"/></label>
            <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}" maxlength="20"
                              validatable="${representative}" validateField="documentNumber"/>
        </div>

        <div class="acceptorIdDatesWrapper ${representative?.documentType == IdentityDocumentType.IDENTITY_CARD ?: 'hidden'} ${hasErrors(bean: representative, field: 'documentExpirationDate', 'errorSpan')} ${hasErrors(bean: representative, field: 'documentIssueDate', 'errorSpan')}">
            <label for="${prefix}[${seqNo}].companyDocumentExpirationDate"><g:message code="document.expiration.label"/></label>
            <g:textField id="${prefix}[${seqNo}].companyDocumentExpirationDate" name="${prefix}[${seqNo}].documentExpirationDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentExpirationDate)}" maxlength="10" class="date-field date-future" required="required"/>

            <label for="${prefix}[${seqNo}].companyDocumentIssueDate"><g:message code="document.issue.label"/></label>
            <g:textField id="${prefix}[${seqNo}].companyDocumentIssueDate" name="${prefix}[${seqNo}].documentIssueDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentIssueDate)}" maxlength="10" class="date-field date-past" required="required"/>
        </div>
    </div>

    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="verification">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.verification?.name() == "PESEL"}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block"
                              validatable="${representative}" validateField="pesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="BIRTH_DATE"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.verification?.name() == "BIRTH_DATE"}"/>

            <label for="${prefix}[${seqNo}].companyBirthDate"><g:message code="birth.date.country.label"/></label>
            <g:textField id="${prefix}[${seqNo}].companyBirthDate" name="${prefix}[${seqNo}].birthDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10" class="date-field date-past"/>
        </div>
    </div>

    <div class="acceptorBirthCountryAndCity">
        <label for="${prefix}[${seqNo}].birthCountry"><g:message code="birth.country.label"/></label>
        <dict:countrySelect name="${prefix}[${seqNo}].birthCountry" value="${representative?.birthCountry}"
                            required="required"
                            validatable="${representative}" validateField="birthCountry"/>

        <label for="${prefix}[${seqNo}].birthCity"><g:message code="birth.city.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].birthCity" value="${representative?.birthCity}"
                          maxlength="255" required="required"
                          validatable="${representative}" validateField="birthCity"/>
    </div>

    <div class="isPolitician ${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="isPolitician">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <span><g:message code="is.political.position.label"/></span>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="true" required="required"
                 checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isPolitician == true}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].isPolitician" value="false" required="required"
                 checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isPolitician == false}"/>
        <label for="${prefix}[${seqNo}].isPolitician"><g:message code="no"/></label>
    </div>
</div>