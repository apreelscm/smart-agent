<%@ page import="com.eservice.eumowy.enums.options.TelephoneType; com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType; java.util.Collections" %>

<div class="personData ${additionalClass}">
    <div class="representativeCBDPersonData" id="representativeCBDPersonData">
        <div class="acceptorDocumentInfoWrapper">
            <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'documentType', 'errorSpan')}">
                <g:hasErrors bean="${representative}" field="documentType">
                    <g:eachError bean="${representative}" field="documentType">
                        <p class="error-message"><g:message error="${it}"/></p>
                    </g:eachError>
                </g:hasErrors>

                <label for="${prefix}[${seqNo}].documentType"><g:message code="panel.identity"/>:</label>

                <g:select id="${prefix}[${seqNo}].personDocumentType"
                          name="${prefix}[${seqNo}].documentType"
                          from="${IdentityDocumentType.values()}"
                          noSelection="['': '']"
                          valueMessagePrefix="identity.kind"
                          value="${representative?.documentType}"
                          disabled="${!representative?.isCBDDataChangedManually}"
                          required="required"
                          style="min-width: 150px"/>
                <g:hiddenField name="${prefix}[${seqNo}].documentType"
                               disabled="${representative?.isCBDDataChangedManually}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.documentType}"/>
            </div>

            <div>
                <label for="${prefix}[${seqNo}].documentNumber"><g:message code="identity.card.details"/></label>
                <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}"
                                  maxlength="20"
                                  readonly="${!representative?.isCBDDataChangedManually}"
                                  validatable="${representative}" validateField="documentNumber"/>
            </div>

            <div class="acceptorIdDatesWrapper ${hasErrors(bean: representative, field: 'documentExpirationDate', 'errorSpan')} ${hasErrors(bean: representative, field: 'documentIssueDate', 'errorSpan')}">
                <label for="${prefix}[${seqNo}].personDocumentExpirationDate"><g:message
                        code="document.expiration.label"/></label>
                <g:textField id="${prefix}[${seqNo}].personDocumentExpirationDate"
                             name="${prefix}[${seqNo}].documentExpirationDate"
                             readonly="${!representative?.isCBDDataChangedManually}"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentExpirationDate)}"
                             maxlength="10" class="date-field date-future" required="required"/>

                <label for="${prefix}[${seqNo}].personDocumentIssueDate"><g:message code="document.issue.label"/></label>
                <g:textField id="${prefix}[${seqNo}].personDocumentIssueDate" name="${prefix}[${seqNo}].documentIssueDate"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentIssueDate)}"
                             readonly="${!representative?.isCBDDataChangedManually}"
                             maxlength="10" class="date-field date-past" required="required"/>
            </div>

        </div>

        <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
            <div class="acceptorRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].verification"
                         value="PESEL"
                         class="pesel-verification"
                         disabled="${!representative?.isCBDDataChangedManually}"
                         checked="${data.isPersonForm() && representative?.verification?.name() == "PESEL"}"/>
                <g:hiddenField name="${prefix}[${seqNo}].verification"
                               disabled="${representative?.isCBDDataChangedManually}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.verification}"/>
                <div class="label"><g:message code="pesel.label"/></div>

                <eumowy:textField name="${prefix}[${seqNo}].pesel"
                                  value="${representative?.pesel}"
                                  maxlength="11"
                                  class="pesel-field display-inline-block"
                                  readonly="${!representative?.isCBDDataChangedManually}"
                                  validatable="${representative}"
                                  validateField="pesel"/>
            </div>

            <div class="acceptorRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].verification"
                         value="BIRTH_DATE"
                         disabled="${!representative?.isCBDDataChangedManually}"
                         checked="${data.isPersonForm() && representative?.verification?.name() == "BIRTH_DATE"}"/>

                <label for="${prefix}[${seqNo}].birthDate"><g:message code="birth.date.country.label"/></label>
                <g:textField id="${prefix}[${seqNo}].birthDate" name="${prefix}[${seqNo}].birthDate"
                             readonly="${!representative?.isCBDDataChangedManually}"
                             value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10"
                             class="date-field date-past"/>
            </div>
        </div>

        <div class="acceptorBirthCountryAndCity ${hasErrors(bean: representative, field: 'birthCountry', 'errorSpan')} ">
            <label for="${prefix}[${seqNo}].birthCountry"><g:message code="birth.country.label"/></label>
            <dict:countrySelect name="${prefix}[${seqNo}].birthCountry"
                                value="${representative?.birthCountry}"
                                required="required"
                                disabled="${!representative?.isCBDDataChangedManually}"
                                validatable="${representative}"
                                validateField="birthCountry"/>
            <g:hiddenField name="${prefix}[${seqNo}].birthCountry"
                           disabled="${representative?.isCBDDataChangedManually}"
                           cbdDataHiddenField="cbdDataHiddenField"
                           value="${representative?.birthCountry}"/>
        </div>

        <div>
            <ul class="table-list-rep">
                <li>
                    <span>
                        <span><label for="${prefix}[${seqNo}].street"><g:message code="panel.street" /></label></span>
                        <span>
                            <dict:streetSelect class="streetTitle" name="${prefix}[${seqNo}].streetTitle" value="${representative?.streetTitle}" default="UL"
                                               readonly="${!representative?.isCBDDataChangedManually}"/>
                            <eumowy:textField name="${prefix}[${seqNo}].street" style="width: 200px" value="${representative?.street}" validatable="${representative}"
                                              readonly="${!representative?.isCBDDataChangedManually}" validateField="street" required="true"/>
                        </span>
                        <span>
                            <span><label for="${prefix}[${seqNo}].houseNumber"><g:message code="panel.house.number" /></label></span>
                            <span><eumowy:textField name="${prefix}[${seqNo}].houseNumber" value="${representative?.houseNumber}" validatable="${representative}" validateField="houseNumber" style="width: 50px" maxlength ="6" required="true"
                                                    readonly="${!representative?.isCBDDataChangedManually}"/></span>
                            <span><label for="${prefix}[${seqNo}].flatNumber"><g:message code="panel.flat.number" /></label></span>
                            <span><eumowy:textField name="${prefix}[${seqNo}].flatNumber" value="${representative?.flatNumber}" validatable="${representative}"
                                                    readonly="${!representative?.isCBDDataChangedManually}" validateField="flatNumber" style="width: 50px" maxlength ="4"/></span>
                        </span>
                    </span>
                </li>

                <li>
                    <span>
                        <span><label for="${prefix}[${seqNo}].postalCode"><g:message code="panel.postal.code" /></label></span>
                        <span><eumowy:textField class="postal-code"
                                                name="${prefix}[${seqNo}].postalCode"
                                                value="${representative?.postalCode}"
                                                validatable="${representative}"
                                                validateField="postalCode" style="width: 50px" maxlength ="6"
                                                readonly="${!representative?.isCBDDataChangedManually}"
                                                required="true"/></span>
                        <span><label for="${prefix}[${seqNo}].city"><g:message code="panel.city" /></label></span>
                        <span>
                            <g:select name="${prefix}[${seqNo}].city"
                                      value="${representative?.city}"
                                      from="[representative?.city ?: '']"
                                      disabled="${!representative?.isCBDDataChangedManually}"
                                      style="width: 280px;" required="required"/>
                            <g:hiddenField name="${prefix}[${seqNo}].city"
                                           disabled="${representative?.isCBDDataChangedManually}"
                                           cbdDataHiddenField="cbdDataHiddenField"
                                           value="${representative?.city}"/>
                        </span>
                    </span>
                </li>

                <li>
                    <span>
                        <span><label for="${prefix}[${seqNo}].postOffice"><g:message code="panel.postal" /></label></span>
                        <span><eumowy:textField name="${prefix}[${seqNo}].postOffice"
                                                value="${representative?.postOffice}"
                                                validatable="${representative}"
                                                readonly="${!representative?.isCBDDataChangedManually}"
                                                validateField="postOffice"
                                                style="width: 280px;" />
                        </span>
                    </span>
                </li>
            </ul>

        </div>

        <div class="${hasErrors(bean: representative, field: 'country', 'errorSpan')} ${hasErrors(bean: representative, field: 'phoneNumber', 'errorSpan')}">

            <div class="phone-container-${prefix}-${seqNo} phone-container-person" style="${data.isPersonForm() && representative?.hasSignedContract != true ? 'display: none;' : ''}">
                <g:radio class="telephone-type"
                         name="${prefix}[${seqNo}].telephoneType${data.isPersonForm() == false ? '-disabled' : ''}"
                         value="${TelephoneType.LANDLINE.name()}"
                         checked="${representative?.telephoneType == TelephoneType.LANDLINE}"
                         disabled="${!representative?.isCBDDataChangedManually}"/>
                <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.landline.phone.number"/></label>

                <g:radio class="telephone-type"
                         name="${prefix}[${seqNo}].telephoneType${data.isPersonForm() == false ? '-disabled' : ''}"
                         value="${TelephoneType.MOBILE.name()}"
                         checked="${representative?.telephoneType == TelephoneType.MOBILE}"
                         disabled="${!representative?.isCBDDataChangedManually}"/>
                <g:hiddenField name="${prefix}[${seqNo}].telephoneType"
                               disabled="${representative?.isCBDDataChangedManually}"
                               cbdDataHiddenField="cbdDataHiddenField"
                               value="${representative?.telephoneType}"/>
                <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.mobile.phone.number"/></label>

                <label style="margin-left: 10px" for="${prefix}[${seqNo}].phoneNumber"><g:message code="panel.number"/>: </label>
                <eumowy:textField name="${prefix}[${seqNo}].phoneNumber"
                                  value="${representative?.phoneNumber}"
                                  maxlength="20"
                                  readonly="${!representative?.isCBDDataChangedManually}"
                                  style="width: 150px"
                                  validatable="${representative}"
                                  validateField="phoneNumber"
                                  class="phone-number ${representative?.telephoneType == TelephoneType.LANDLINE ? 'phone' : 'mobile-phone' }"/>
            </div>
            <label style="margin-left: 20px" for="${prefix}[${seqNo}].country"><g:message code="country.name.label"/></label>
            <dict:countrySelect name="${prefix}[${seqNo}].country"
                                value="${representative?.country}"
                                required="required"
                                disabled="${!representative?.isCBDDataChangedManually}"
                                validatable="${representative}"
                                validateField="country"/>
            <g:hiddenField name="${prefix}[${seqNo}].country"
                           disabled="${representative?.isCBDDataChangedManually}"
                           cbdDataHiddenField="cbdDataHiddenField"
                           value="${representative?.country}"/>
        </div>

        <div class="email-container-${prefix}-${seqNo}" style="${data.isPersonForm() && representative?.hasSignedContract != true ? 'display: none;' : ''}">
            <span>
                <g:message code="panel.email"/>: <eumowy:textField name="${prefix}[${seqNo}].email"
                                                                   value="${representative?.email}"
                                                                   validatable="${representative}"
                                                                   readonly="${!representative?.isCBDDataChangedManually}"
                                                                   validateField="email"
                                                                   style="width: 150px"
                                                                   email="true"/>
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
                     checked="${data.isPersonForm() && representative?.isPolitician == true}"/>
            <label for="${prefix}[${seqNo}].isPolitician"><g:message code="yes"/></label>

            <g:radio name="${prefix}[${seqNo}].isPolitician"
                     value="false"
                     required="required"
                     checked="${data.isPersonForm() && representative?.isPolitician == false}"/>
            <label for="${prefix}[${seqNo}].isPolitician"><g:message code="no"/></label>
        </div>

        <div class="isDirectPep ${hasErrors(bean: representative, field: 'isDirectPep', 'errorSpan')} ${representative?.isPolitician ?: 'hidden'}">
            <g:hasErrors bean="${representative}" field="isDirectPep">
                <p class="error-message"><g:message code="representative.option.required"/></p>
            </g:hasErrors>

            <g:radio name="${prefix}[${seqNo}].isDirectPep"
                     value="true"
                     required="required"
                     checked="${data.isPersonForm() && representative?.isDirectPep == true}"/>
            <label for="${prefix}[${seqNo}].isDirectPep">
                <g:message code="representative.pep.direct"/>
                <strong class="tooltip" title="${g.message(code: 'representative.pep.direct.description')}"><g:message code="representative.pep.description"/></strong>
            </label>

            <g:radio name="${prefix}[${seqNo}].isDirectPep"
                     value="false"
                     required="required"
                     checked="${data.isPersonForm() && representative?.isDirectPep == false}"/>
            <label for="${prefix}[${seqNo}].isDirectPep">
                <g:message code="representative.pep.related"/>
                <strong class="tooltip" title="${g.message(code: 'representative.pep.related.description')}"><g:message code="representative.pep.description"/></strong>
            </label>
        </div>

        <div>
            <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
            <dict:countrySelect name="${prefix}[${seqNo}].citizenship"
                                value="${representative?.citizenship}"
                                disabled="${!representative?.isCBDDataChangedManually}"
                                validatable="${representative}"
                                validateField="citizenship"
                                required="required"/>
            <g:hiddenField name="${prefix}[${seqNo}].citizenship"
                           disabled="${representative?.isCBDDataChangedManually}"
                           cbdDataHiddenField="cbdDataHiddenField"
                           value="${representative?.citizenship}"/>
        </div>
    </div>
</div>
