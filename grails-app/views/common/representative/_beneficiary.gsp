<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<g:hiddenField name="${prefix}[${seqNo}].id" value="${representative?.id}"/>

<div>
    <g:select name="${prefix}[${seqNo}].salutation" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${representative?.salutation}"
              validatable="${representative}" validateField="salutation"/>

    <label style="margin-left: 10px" for="${prefix}[${seqNo}].name"><g:message code="panel.first.name"/>:</label>
    <eumowy:textField name="${prefix}[${seqNo}].name" value="${representative?.name}" maxlength ="13"
                      validatable="${representative}" validateField="name"/>

    <label for="${prefix}[${seqNo}].surname"><g:message code="panel.last.name"/>:</label>
    <eumowy:textField name="${prefix}[${seqNo}].surname" value="${representative?.surname}" maxlength ="35" class="surnameField"
                      validatable="${representative}" validateField="surname"/>
</div>

<div style="margin-top: 15px">
    <label for="${prefix}[${seqNo}].citizenship"><g:message code="citizenship.label"/></label>
    <eumowy:textField name="${prefix}[${seqNo}].citizenship" value="${representative?.citizenship}" maxlength="30"
                      validatable="${representative}" validateField="citizenship"/>
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
