<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<div>
    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="verification">
            <g:eachError bean="${representative}" field="verification">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                     checked="${"PESEL".equals(representative?.verification?.name())}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block"
                              validatable="${representative}" validateField="pesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].verification" value="COUNTRY_CODE"
                     checked="${"COUNTRY_CODE".equals(representative?.verification?.name())}"/>
            <div class="label"><g:message code="country.label"/></div>
            <eumowy:textField name="${prefix}[${seqNo}].countryCode" value="${representative?.countryCode}"
                              maxlength="30" class="display-inline-block"
                              validatable="${representative}" validateField="countryCode"/>
        </div>
    </div>

    <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'documentType', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="documentType">
            <g:eachError bean="${representative}" field="documentType">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].documentType"
                               radioWrapperClass="inlineRadioWrapper display-inline-block" value="${representative?.documentType}"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].documentNumber"><g:message code="identity.card.details"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].documentNumber" value="${representative?.documentNumber}" maxlength="20"
                          validatable="${representative}" validateField="documentNumber"/>

        <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].citizenship" value="${representative?.citizenship}" maxlength="30"
                          validatable="${representative}" validateField="citizenship"/>

        <label for="${prefix}[${seqNo}].birthDate"><g:message code="birth.date.label"/></label>
        <g:textField name="${prefix}[${seqNo}].birthDate" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}"
                     maxlength="10" class="date-field"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].address"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].address" value="${representative?.address}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="address"/>
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

    <div class="${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="isPolitician">
            <g:eachError bean="${representative}" field="isPolitician">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <label><g:message code="political.position.label"/></label>
        <g:checkBox name="${prefix}[${seqNo}].isPolitician" checked="${representative?.isPolitician}"/>
    </div>
</div>

<div style="margin-top: 25px">
    <p><g:message code="beneficiary.relation.with.acceptor.label"/></p>

    <div class="${hasErrors(bean: representative, field: 'ownsAcceptor', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="ownsAcceptor">
            <g:eachError bean="${representative}" field="ownsAcceptor">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].ownsAcceptor" checked="${representative?.ownsAcceptor}"/>
            <label for="beneficiaries[${seqNo}].ownsAcceptor"><g:message code="beneficiary.owns.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].controlsAcceptor" checked="${representative?.controlsAcceptor}"/>
            <label for="beneficiaries[${seqNo}].controlsAcceptor"><g:message code="beneficiary.controls.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].overQuarterOfVotes" checked="${representative?.overQuarterOfVotes}"/>
            <label for="beneficiaries[${seqNo}].overQuarterOfVotes"><g:message code="beneficiary.majority.acceptor.label"/></label>

            <eumowy:textField name="beneficiaries[${seqNo}].votesPercentage" class="percent-short"
                              value="${representative?.overQuarterOfVotes ? representative?.votesPercentage : ""}"
                              validatable="${representative}" validateField="votesPercentage"/>
            <g:message code="beneficiary.majority.acceptor.closing.label"/>
        </div>
    </div>
</div>
