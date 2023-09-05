<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>
<g:hiddenField name="${prefix}[${seqNo}].midCBD" value="${representative?.midCBD}"/>
<g:hiddenField name="index" value="${seqNo}"/>
<g:hiddenField name="prefix" value="${prefix}"/>

<div class="isCBDDataChangedManually ${hasErrors(bean: representative, field: 'isCBDDataChangedManually', 'errorSpan')}">
    <g:hasErrors bean="${representative}" field="isCBDDataChangedManually">
        <p class="error-message"><g:message code="representative.option.required"/></p>
    </g:hasErrors>

    <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually" value="true"
             checked="${representative?.isCBDDataChangedManually == true}"/>
    <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyTrue"/></label>

    <g:radio name="${prefix}[${seqNo}].isCBDDataChangedManually" value="false"
             checked="${representative?.isCBDDataChangedManually == false}"/>
    <label for="${prefix}[${seqNo}].isCBDDataChangedManually"><g:message code="com.eservice.eumowy.command.RepresentativeCommand.isCBDDataChangedManuallyFalse"/></label>
</div>

<div>
    <g:select name="${prefix}[${seqNo}].salutation"
              from="['Pan','Pani']"
              valueMessagePrefix="person.title"
              value="${representative?.salutation}"
              disabled="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
              validatable="${representative}" validateField="salutation" required="required"/>
    <g:hiddenField name="${prefix}[${seqNo}].salutation"
                   disabled="${czyNowaUmowa || representative?.isCBDDataChangedManually}"
                   cbdDataHiddenField="cbdDataHiddenField"
                   value="${representative?.salutation}"/>

    <label style="margin-left: 10px" for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
    <eumowy:textField name="${prefix}[${seqNo}].name" value="${representative?.name}" maxlength ="25"
                      readonly="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                      validatable="${representative}" validateField="name" required="required"/>

    <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
    <eumowy:textField name="${prefix}[${seqNo}].surname"
                      value="${representative?.surname}"
                      maxlength ="35"
                      class="surnameField"
                      readonly="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                      validatable="${representative}"
                      validateField="surname"
                      required="required"/>
</div>

<div style="margin-top: 15px">
    <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
    <dict:countrySelect name="${prefix}[${seqNo}].citizenship"
                        value="${representative?.citizenship}"
                        disabled="${(!czyNowaUmowa && !representative?.isCBDDataChangedManually && representative?.citizenship != null)}"
                        validatable="${representative}"
                        validateField="citizenship" required="required"/>
    <g:hiddenField name="${prefix}[${seqNo}].citizenship"
                   disabled="${czyNowaUmowa || representative?.isCBDDataChangedManually || representative?.citizenship == null }"
                   cbdDataHiddenField="cbdDataHiddenField"
                   value="${representative?.citizenship}"/>
</div>

<div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'verification', 'errorSpan')}">
    <div class="acceptorRadioWrapper">
        <g:radio name="${prefix}[${seqNo}].verification" value="PESEL"
                 disabled="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                 checked="${representative?.verification?.name() == "PESEL"}"/>
        <g:hiddenField name="${prefix}[${seqNo}].verification"
                       disabled="${czyNowaUmowa || representative?.isCBDDataChangedManually}"
                       cbdDataHiddenField="cbdDataHiddenField"
                       value="${representative?.verification}"/>
        <div class="label"><g:message code="pesel.label"/></div>

        <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                          maxlength="11" class="pesel-field display-inline-block"
                          readonly="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                          validatable="${representative}" validateField="pesel"/>
    </div>
    <div class="acceptorRadioWrapper">
        <g:radio name="${prefix}[${seqNo}].verification"
                 value="BIRTH_DATE"
                 disabled="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                 checked="${representative?.verification?.name() == "BIRTH_DATE"}"/>

        <label for="${prefix}[${seqNo}].personBirthDate"><g:message code="birth.date.country.label"/></label>
        <g:textField id="${prefix}[${seqNo}].personBirthDate"
                     name="${prefix}[${seqNo}].birthDate"
                     readonly="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                     value="${formatDate(format: 'yyyy-MM-dd', date: representative?.birthDate)}"
                     maxlength="10"
                     class="date-field date-past"/>
    </div>
</div>

<div class="isPolitician ${hasErrors(bean: representative, field: 'isPolitician', 'errorSpan')}">
    <g:hasErrors bean="${representative}" field="isPolitician">
        <p class="error-message"><g:message code="representative.option.required"/></p>
    </g:hasErrors>

    <span><g:message code="is.political.position.label"/></span>

    <g:radio name="${prefix}[${seqNo}].isPolitician"
             value="true"
             checked="${representative?.isPolitician == true}"/>
    <label for="${prefix}[${seqNo}].isPolitician"><g:message code="yes"/></label>

    <g:radio name="${prefix}[${seqNo}].isPolitician" value="false"
             checked="${representative?.isPolitician == false}"/>
    <label for="${prefix}[${seqNo}].isPolitician"><g:message code="no"/></label>
</div>

<div class="isDirectPep ${hasErrors(bean: representative, field: 'isDirectPep', 'errorSpan')} ${representative?.isPolitician ?: 'hidden'}">
    <g:hasErrors bean="${representative}" field="isDirectPep">
        <p class="error-message"><g:message code="representative.option.required"/></p>
    </g:hasErrors>

    <g:radio name="${prefix}[${seqNo}].isDirectPep"
             value="true"
             required="required"
             checked="${representative?.isDirectPep == true}"/>
    <label for="${prefix}[${seqNo}].isDirectPep">
        <g:message code="representative.pep.direct"/>
        <strong class="tooltip" title="${g.message(code: 'representative.pep.direct.description')}"><g:message code="representative.pep.description"/></strong>
    </label>

    <g:radio name="${prefix}[${seqNo}].isDirectPep"
             value="false"
             required="required"
             checked="${representative?.isDirectPep == false}"/>
    <label for="${prefix}[${seqNo}].isDirectPep">
        <g:message code="representative.pep.related"/>
        <strong class="tooltip" title="${g.message(code: 'representative.pep.related.description')}"><g:message code="representative.pep.description"/></strong>
    </label>
</div>

<div style="margin-top: 15px">
    <p><g:message code="beneficiary.relation.with.acceptor.label"/></p>

    <div class="${hasErrors(bean: representative, field: 'ownsAcceptor', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="ownsAcceptor">
            <g:eachError bean="${representative}" field="ownsAcceptor">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].ownsAcceptor"
                        checked="${representative?.ownsAcceptor}"/>
            <label for="beneficiaries[${seqNo}].ownsAcceptor"><g:message code="beneficiary.owns.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].controlsAcceptor"
                        checked="${representative?.controlsAcceptor}"/>
            <label for="beneficiaries[${seqNo}].controlsAcceptor"><g:message code="beneficiary.controls.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].overQuarterOfVotes"
                        checked="${representative?.overQuarterOfVotes}"/>
            <label for="beneficiaries[${seqNo}].overQuarterOfVotes"><g:message code="beneficiary.majority.acceptor.label"/></label>

            <eumowy:textField name="beneficiaries[${seqNo}].votesPercentage" class="percent-short"
                              value="${representative?.overQuarterOfVotes ? representative?.votesPercentage : ""}"
                              validatable="${representative}" validateField="votesPercentage"/>
            <g:message code="beneficiary.majority.acceptor.closing.label"/>
        </div>
    </div>

    <div id="confirm-submit-without-subscription-dialog" style="display: none;">
        <p><g:message code="representatives.cancelCBDDataChange.confirm"/></p>
    </div>
</div>
