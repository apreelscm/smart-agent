<%@ page import="com.eservice.eumowy.enums.options.TelephoneType; com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType; java.util.Collections" %>

<div class="companyData ${additionalClass}">
    <div class="representativeCBDCompanyData" id="representativeCBDCompanyData">
        <div class="acceptorDocumentInfoWrapper ${representative?.position == 'Pełnomocnik' ?: 'hidden'}">
            <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'documentType', 'errorSpan')}">
                <g:hasErrors bean="${representative}" field="documentType">
                    <g:eachError bean="${representative}" field="documentType">
                        <p class="error-message"><g:message error="${it}"/></p>
                    </g:eachError>
                </g:hasErrors>

                <label for="${prefix}[${seqNo}].documentType"><g:message code="panel.identity"/>:</label>

                <g:select id="${prefix}[${seqNo}].companyDocumentType"
                          name="${prefix}[${seqNo}].documentType"
                          from="${IdentityDocumentType.values()}"
                          noSelection="['': '']"
                          valueMessagePrefix="identity.kind"
                          value="${representative?.documentType}"
                          disabled="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                          required="required"
                          style="min-width: 150px"/>
                <g:hiddenField name="${prefix}[${seqNo}].documentType"
                               disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.documentType}"/>
            </div>

            <div>
                <label for="${prefix}[${seqNo}].documentNumber"><g:message code="identity.card.details"/></label>
                <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}"
                                  readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                                  maxlength="20"
                                  validatable="${representative}" validateField="documentNumber"/>
            </div>

            <div class="acceptorIdDatesWrapper ${representative?.documentType == IdentityDocumentType.IDENTITY_CARD ?: 'hidden'} ${hasErrors(bean: representative, field: 'documentExpirationDate', 'errorSpan')} ${hasErrors(bean: representative, field: 'documentIssueDate', 'errorSpan')}">
                <label for="${prefix}[${seqNo}].companyDocumentExpirationDate"><g:message
                        code="document.expiration.label"/></label>
                <g:textField id="${prefix}[${seqNo}].companyDocumentExpirationDate"
                             name="${prefix}[${seqNo}].documentExpirationDate"
                             readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentExpirationDate)}"
                             maxlength="10" class="date-field date-future" required="required"/>

                <label for="${prefix}[${seqNo}].companyDocumentIssueDate"><g:message
                        code="document.issue.label"/></label>
                <g:textField id="${prefix}[${seqNo}].companyDocumentIssueDate"
                             name="${prefix}[${seqNo}].documentIssueDate"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentIssueDate)}"
                             readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                             maxlength="10" class="date-field date-past" required="required"/>
            </div>
        </div>

        <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
            <g:hasErrors bean="${representative}" field="verification">
                <p class="error-message"><g:message code="representative.option.required"/></p>
            </g:hasErrors>

            <div class="acceptorRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].verification"
                         value="PESEL"
                         class="pesel-verification"
                         disabled="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                         checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.verification?.name() == "PESEL"}"/>
                <g:hiddenField name="${prefix}[${seqNo}].verification"
                               disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.verification}"/>
                <div class="label"><g:message code="pesel.label"/></div>

                <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                                  readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                                  maxlength="11" class="pesel-field display-inline-block"
                                  validatable="${representative}" validateField="pesel"/>
            </div>

            <div class="acceptorRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].verification" value="BIRTH_DATE"
                         readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                         checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.verification?.name() == "BIRTH_DATE"}"/>

                <label for="${prefix}[${seqNo}].companyBirthDate"><g:message code="birth.date.country.label"/></label>
                <g:textField id="${prefix}[${seqNo}].companyBirthDate" name="${prefix}[${seqNo}].birthDate"
                             readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10"
                             class="date-field date-past"/>
            </div>
        </div>

        <div class="acceptorBirthCountryAndCity ${hasErrors(bean: representative, field: 'birthCountry', 'errorSpan')} ">
            <label for="${prefix}[${seqNo}].birthCountry"><g:message code="birth.country.label"/></label>
            <dict:countrySelect name="${prefix}[${seqNo}].birthCountry"
                                value="${representative?.birthCountry}"
                                required="required"
                                disabled="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                                validatable="${representative}" validateField="birthCountry"/>
            <g:hiddenField name="${prefix}[${seqNo}].birthCountry"
                           disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                           cbdDataHiddenField="cbdDataHiddenField"
                           value="${representative?.birthCountry}"/>
        </div>

        <div class="address-container address-container-${prefix}-${seqNo} address-container-company"
             style="${!data.isBusinessLegalFormSelected() || (data.isPersonForm() && representative?.hasSignedContract != true) ? 'display: none;' : ''}">
            <div class="${hasErrors(bean: representative, field: 'address', 'errorSpan')}">
                <label for="${prefix}[${seqNo}].address"><g:message code="address.label"/></label>
                <eumowy:textField
                        name="${prefix}[${seqNo}].address"
                        value="${representative?.address}"
                        maxlength="100"
                        style="width: 750px"
                        validatable="${representative}"
                        validateField="address"
                        readonly="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                />
            </div>

            <div class="${hasErrors(bean: representative, field: 'country', 'errorSpan')}">
                <label for="${prefix}[${seqNo}].country"><g:message code="residence.country.label"/></label>
                <dict:countrySelect
                        name="${prefix}[${seqNo}].country"
                        value="${representative?.country}"
                        validatable="${representative}"
                        validateField="country"
                        disabled="${!representative?.isCBDDataChangedManually && !representative?.additionalData}"
                />
                <g:hiddenField name="${prefix}[${seqNo}].country"
                               disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.country}"/>
            </div>
        </div>

        <div class="phone-container phone-container-${prefix}-${seqNo} phone-container-company"
             style="${data.isPersonForm() && representative?.hasSignedContract != true ? 'display: none;' : ''}">
            <g:radio class="telephone-type"
                     name="${prefix}[${seqNo}].telephoneType${data.isPersonForm() == true ? '-disabled' : ''}"
                     value="${TelephoneType.LANDLINE.name()}"
                     checked="${representative?.telephoneType == TelephoneType.LANDLINE}"/>
            <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.landline.phone.number"/></label>

            <g:radio class="telephone-type"
                     name="${prefix}[${seqNo}].telephoneType${data.isPersonForm() == true ? '-disabled' : ''}"
                     value="${TelephoneType.MOBILE.name()}"
                     checked="${representative?.telephoneType == TelephoneType.MOBILE}"/>
            <g:hiddenField name="${prefix}[${seqNo}].telephoneType"
                           disabled="${representative == null || representative?.isCBDDataChangedManually || representative?.additionalData}"
                           cbdDataHiddenField="cbdDataHiddenField"
                           value="${representative?.telephoneType}"/>
            <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.mobile.phone.number"/></label>

            <label style="margin-left: 10px" for="${prefix}[${seqNo}].phoneNumber"><g:message
                    code="panel.number"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].phoneNumber" value="${representative?.mobilePhone}"
                              maxlength="20" style="width: 150px"
                              validatable="${representative}" validateField="phoneNumber"
                              class="phone-number ${representative?.telephoneType == TelephoneType.LANDLINE ? 'phone' : 'mobile-phone'}"/>
        </div>

        <div class="email-container email-container-${prefix}-${seqNo} email-container-company"
             style="${data.isPersonForm() && representative?.hasSignedContract != true ? 'display: none;' : ''}">
            <span>
                <g:message code="panel.email"/>: <eumowy:textField name="${prefix}[${seqNo}].email"
                                                                   value="${representative?.email}"
                                                                   validatable="${representative}" validateField="email"
                                                                   style="width: 150px" email="true"/>
            </span>
        </div>

        <div class="isPolitician ${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
            <g:hasErrors bean="${representative}" field="isPolitician">
                <p class="error-message"><g:message code="representative.option.required"/></p>
            </g:hasErrors>

            <span><g:message code="is.political.position.label"/></span>

            <g:radio name="${prefix}[${seqNo}].isPolitician"
                     value="true"
                     required="required"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isPolitician == true}"/>
            <label for="${prefix}[${seqNo}].isPolitician"><g:message code="yes"/></label>

            <g:radio name="${prefix}[${seqNo}].isPolitician"
                     value="false"
                     required="required"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isPolitician == false}"/>
            <label for="${prefix}[${seqNo}].isPolitician"><g:message code="no"/></label>
        </div>

        <div class="isDirectPep ${hasErrors(bean: representative, field: 'isDirectPep', 'errorSpan')} ${representative?.isPolitician ?: 'hidden'}">
            <g:hasErrors bean="${representative}" field="isDirectPep">
                <p class="error-message"><g:message code="representative.option.required"/></p>
            </g:hasErrors>

            <g:radio name="${prefix}[${seqNo}].isDirectPep" value="true" required="required"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isDirectPep == true}"/>
            <label for="${prefix}[${seqNo}].isDirectPep">
                <g:message code="representative.pep.direct"/>
                <strong class="tooltip" title="${g.message(code: 'representative.pep.direct.description')}"><g:message
                        code="representative.pep.description"/></strong>
            </label>

            <g:radio name="${prefix}[${seqNo}].isDirectPep" value="false" required="required"
                     checked="${(!data.dzialalnoscForma || data.isCompanyForm()) && representative?.isDirectPep == false}"/>
            <label for="${prefix}[${seqNo}].isDirectPep">
                <g:message code="representative.pep.related"/>
                <strong class="tooltip" title="${g.message(code: 'representative.pep.related.description')}"><g:message
                        code="representative.pep.description"/></strong>
            </label>
        </div>

        <div class="citizenShipDiv ${representative?.isProcuratorPosition() ?: 'hidden'}">
            <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
            <dict:countrySelect name="${prefix}[${seqNo}].citizenship"
                                value="${representative?.citizenship}"
                                validatable="${representative}"
                                validateField="citizenship"
                                required="required"/>
        </div>
    </div>
</div>
