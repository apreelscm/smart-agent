<%@ page import="com.eservice.eumowy.enums.options.TelephoneType; com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType; java.util.Collections" %>

<div class="personData ${additionalClass}">
    <div class="acceptorDocumentInfoWrapper">
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
            <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}"
                              maxlength="20"
                              validatable="${representative}" validateField="documentNumber"/>
        </div>

        <div class="acceptorIdDatesWrapper ${representative?.documentType == IdentityDocumentType.IDENTITY_CARD ?: 'hidden'} ${hasErrors(bean: representative, field: 'documentExpirationDate', 'errorSpan')} ${hasErrors(bean: representative, field: 'documentIssueDate', 'errorSpan')}">
            <label for="${prefix}[${seqNo}].personDocumentExpirationDate"><g:message
                    code="document.expiration.label"/></label>
            <g:textField id="${prefix}[${seqNo}].personDocumentExpirationDate"
                         name="${prefix}[${seqNo}].documentExpirationDate"
                         value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentExpirationDate)}"
                         maxlength="10" class="date-field date-future" required="required"/>

            <label for="${prefix}[${seqNo}].personDocumentIssueDate"><g:message code="document.issue.label"/></label>
            <g:textField id="${prefix}[${seqNo}].personDocumentIssueDate" name="${prefix}[${seqNo}].documentIssueDate"
                         value="${formatDate(format: 'yyyy-MM-dd', date: representative?.documentIssueDate)}"
                         maxlength="10" class="date-field date-past" required="required"/>
        </div>
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

            <label for="${prefix}[${seqNo}].personBirthDate"><g:message code="birth.date.country.label"/></label>
            <g:textField id="${prefix}[${seqNo}].personBirthDate" name="${prefix}[${seqNo}].birthDate"
                         value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}" maxlength="10"
                         class="date-field date-past"/>
        </div>
    </div>

    <div class="acceptorBirthCountryAndCity ${hasErrors(bean: representative, field: 'birthCountry', 'errorSpan')} ">
        <label for="${prefix}[${seqNo}].birthCountry"><g:message code="birth.country.label"/></label>
        <dict:countrySelect name="${prefix}[${seqNo}].birthCountry" value="${representative?.birthCountry}"
                            required="required"
                            validatable="${representative}" validateField="birthCountry"/>

    </div>

    <div>
        <ul class="table-list-rep">
            <li>
                <span>
                    <span><label for="${prefix}[${seqNo}].street"><g:message code="panel.street" /></label></span>
                    <span>
                        <dict:streetSelect class="streetTitle" name="${prefix}[${seqNo}].streetTitle" value="${representative?.streetTitle}" default="UL"/>
                        <eumowy:textField name="${prefix}[${seqNo}].street" style="width: 200px" value="${representative?.street}" validatable="${representative}" validateField="street" required="true"/>
                    </span>
                    <span>
                        <span><label for="${prefix}[${seqNo}].houseNumber"><g:message code="panel.house.number" /></label></span>
                        <span><eumowy:textField name="${prefix}[${seqNo}].houseNumber" value="${representative?.houseNumber}" validatable="${representative}" validateField="houseNumber" style="width: 50px" maxlength ="6" required="true"/></span>
                        <span><label for="${prefix}[${seqNo}].flatNumber"><g:message code="panel.flat.number" /></label></span>
                        <span><eumowy:textField name="${prefix}[${seqNo}].flatNumber" value="${representative?.flatNumber}" validatable="${representative}" validateField="flatNumber" style="width: 50px" maxlength ="4"/></span>
                    </span>
                </span>
            </li>

            <li>
                <span>
                    <span><label for="${prefix}[${seqNo}].postalCode"><g:message code="panel.postal.code" /></label></span>
                    <span><eumowy:textField class="postal-code" name="${prefix}[${seqNo}].postalCode" value="${representative?.postalCode}" validatable="${representative}" validateField="postalCode" style="width: 50px" maxlength ="6" required="true"/></span>
                    <span><label for="${prefix}[${seqNo}].city"><g:message code="panel.city" /></label></span>
                    <span> <g:select name="${prefix}[${seqNo}].city"
                                     value="${representative?.city}"
                                     from="[representative?.city ?: '']"
                                     style="width: 280px;" required="required"/></span>
                </span>
            </li>

            <li>
                <span>
                    <span><label for="${prefix}[${seqNo}].postOffice"><g:message code="panel.postal" /></label></span>
                    <span><eumowy:textField name="${prefix}[${seqNo}].postOffice" value="${representative?.postOffice}" validatable="${representative}" validateField="postOffice" style="width: 280px;" /></span>
                </span>
            </li>
        </ul>

    </div>

    <div class="${hasErrors(bean: representative, field: 'country', 'errorSpan')} ${hasErrors(bean: representative, field: 'phoneNumber', 'errorSpan')}">

        <g:radio class="telephone-type" name="${prefix}[${seqNo}].telephoneType" value="${TelephoneType.LANDLINE.name()}"
                 checked="${data.isPersonForm() && representative?.telephoneType == TelephoneType.LANDLINE}"/>
        <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.landline.phone.number"/></label>

        <g:radio class="telephone-type" name="${prefix}[${seqNo}].telephoneType" value="${TelephoneType.MOBILE.name()}"
                 checked="${data.isPersonForm() && representative?.telephoneType == TelephoneType.MOBILE}"/>
        <label for="${prefix}[${seqNo}].telephoneType"><g:message code="panel.mobile.phone.number"/></label>

        <label style="margin-left: 10px" for="${prefix}[${seqNo}].phoneNumber"><g:message code="panel.number"/>: </label>
        <eumowy:textField name="${prefix}[${seqNo}].phoneNumber" value="${representative?.phoneNumber}" maxlength="20" style="width: 150px"
                          validatable="${representative}" validateField="phoneNumber" class="phone-number ${representative?.telephoneType == TelephoneType.LANDLINE ? 'phone' : 'mobile-phone' }"/>

        <label style="margin-left: 20px" for="${prefix}[${seqNo}].country"><g:message code="country.name.label"/></label>
        <dict:countrySelect name="${prefix}[${seqNo}].country" value="${representative?.country}" required="required"
                            validatable="${representative}" validateField="country"/>
    </div>

    <div>
        <span>
            <g:message code="panel.email"/>: <g:textField name="${prefix}[${seqNo}].email" value="${representative?.email}" validatable="${representative}" style="width: 150px" email="true"/>
        </span>
    </div>

    <div class="hasSignedContract ${hasErrors(bean: representative, field: 'hasSignedContract', 'errorSpan')}">

        <span><g:message code="person.signed.contract"/></span>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract" value="true"
                 checked="${data.isPersonForm() && representative?.hasSignedContract == true}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract" value="false"
                 checked="${data.isPersonForm() && representative?.hasSignedContract == false}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="no"/></label>
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

    <div class="isDirectPep ${hasErrors(bean: representative, field: 'isDirectPep', 'errorSpan')} ${representative?.isPolitician ?: 'hidden'}">
        <g:hasErrors bean="${representative}" field="isDirectPep">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <g:radio name="${prefix}[${seqNo}].isDirectPep" value="true" required="required"
                 checked="${data.isPersonForm() && representative?.isDirectPep == true}"/>
        <label for="${prefix}[${seqNo}].isDirectPep">
            <g:message code="representative.pep.direct"/>
            <strong class="tooltip" title="${g.message(code: 'representative.pep.direct.description')}"><g:message code="representative.pep.description"/></strong>
        </label>

        <g:radio name="${prefix}[${seqNo}].isDirectPep" value="false" required="required"
                 checked="${data.isPersonForm() && representative?.isDirectPep == false}"/>
        <label for="${prefix}[${seqNo}].isDirectPep">
            <g:message code="representative.pep.related"/>
            <strong class="tooltip" title="${g.message(code: 'representative.pep.related.description')}"><g:message code="representative.pep.description"/></strong>
        </label>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].citizenship" value="${representative?.citizenship}" maxlength="30"
                          required="required"
                          validatable="${representative}" validateField="citizenship"/>
    </div>
</div>