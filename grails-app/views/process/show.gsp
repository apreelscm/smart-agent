
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label')}" />
    <g:set var="isWaitingOnlyProcess" value="${processInstance?.status.equals(Process.ProcessStatus.WAITING)}"/>
    <g:set var="isWaitingProcess" value="${processInstance?.status in [Process.ProcessStatus.WAITING,Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION]}" />
    <g:set var="isWaitingForSubscriptionProcess" value="${processInstance?.status == Process.ProcessStatus.WAIT_FOR_SUBSCRIPTION_PAPER_VERSION}" />
    <g:set var="isClosedProcess" value="${processInstance?.status in [Process.ProcessStatus.ACCEPTED,Process.ProcessStatus.REJECTED]}" />
    <g:set var="hasDocuments" value="${processInstance?.documents?.size() > 0}" />

    <title>${entityName}</title>

    <script type="text/javascript">
        var confirmRenewSubscriptionsMessage = '${message(code: 'default.button.renewSubscriptions.confirm.message')}';
    </script>

    <asset:javascript src="apreel/process/show.js"/>
</head>
<body>

<div id="renewingSubsriptionsInProgress" class="hidden"><g:message code="renewSubscriptions.in.progress"/></div>
<div id="resendingEmailsInProgress" class="hidden"><g:message code="resendEmails.in.progress"/></div>

<section id="show-process">
    <h1 class="ng linia-bottom"><g:message code='process.id.label'/>: ${processInstance?.id}</h1>

    <div id="notesMessageBox">
        <g:if test="${flash.message}">
            <g:render template="../activity/message/infoMessage" model="[message: flash.message]"/>
        </g:if>
        <g:if test="${flash.error}">
            <g:render template="../activity/message/errorMessage" model="[message: flash.error]"/>
        </g:if>
    </div>

    <section id="clientDetails">
        <div>
            <label for="clientName"><g:message code="client.name.label"/></label>
            <g:textField name="clientName" value="${processInstance?.client?.name}" disabled="disabled"/>
        </div>

        <div id="bottomInfo">
            <div class="display-inline-block">
                <label for="clientNip"><g:message code="process.nip.label" /></label>
                <g:textField name="clientNip" value="${processInstance?.client?.nip}" disabled="disabled"/>
            </div>

            <div class="display-inline-block">
                <label for="phNumber"><g:message code="process.phNumber.label"/></label>
                <g:textField name="phNumber" value="${processInstance?.phNumber}" disabled="disabled"/>
            </div>

            <div class="display-inline-block">
                <label for="phName"><g:message code="process.fullName.label"/></label>
                <g:textField name="phName" value="${processInstance?.phFirstName + " " + processInstance?.phSurname}"
                             disabled="disabled"/>
            </div>
        </div>
    </section>

    <div id="documentsBox" class="float-left width49">
        <g:render template="table/documentsTable"/>
    </div>

    <div id="attachmentsBox" class="float-right width49">
        <g:render template="table/attachmentsTable"/>
    </div>

    <div id="pdfBox"></div>

    <div class="ui-helper-clearfix"></div>

    <g:form>
        <div id="processDetails">
            <div id="processActivities">
                <label><g:message code="activities.label"/></label>
                <ul>
                    <g:each var="activity" in="${processInstance?.activities}">
                        <li>
                            <g:message code="activity.${activity.code}.name"/>
                        </li>
                    </g:each>
                </ul>
            </div>

            <div id="processObserved">
                <label><g:message code="observe.label"/></label>
                <input type="checkbox" id="observed" name="observed"
                    ${processInstance?.observed ? 'checked' : ''}
                    ${isClosedProcess ? 'disabled' : ''}
                >
            </div>

            <g:if test="${isWaitingForSubscriptionProcess}">
                <div id="contractDate">
                    <label><g:message code="fill.aggrement.date"/></label>
                    <input type="text" name="dataUmowy" id="dataUmowy" value=""
                        ${isWaitingForSubscriptionProcess ? 'required="true"' : '' }
                           readonly = "true"/>
                </div>
            </g:if>

            <div id="notesContainer">
                <label><g:message code="notes.label"/></label>
                <textarea id="notes" maxlength="300" name="notesFromZrd"
                    ${isClosedProcess ? 'disabled' : ''}
                    ${!params.notesFromZrd ? 'required' : ''}>${processInstance?.notesFromZrd}</textarea>

                <g:actionSubmit id="saveNotes" value="${message(code: 'save.notes.label')}" action="saveNotes" class="button submit"
                                disabled="${isClosedProcess}"/>
            </div>

            <div class="ui-helper-clearfix"></div>
        </div>

        <nav>
            <fieldset class="przyciski">
                <g:hiddenField name="id" value="${processInstance.id}"/>
                <g:hiddenField name="filterStatus" value="${params.filterStatus}"/>
                <g:hiddenField name="filterObserved" value="${params.filterObserved}"/>
                <g:hiddenField name="filterNip" value="${params.filterNip}"/>
                <g:hiddenField name="filterPhNo" value="${params.filterPhNo}"/>
                <g:hiddenField name="filterDateFrom" value="${params.filterDateFrom}"/>
                <g:hiddenField name="filterDateTo" value="${params.filterDateTo}"/>
                <g:if test="${params.sort}"><g:hiddenField name="sort" value="${params.sort}"/></g:if>
                <g:if test="${params.order}"><g:hiddenField name="order" value="${params.order}"/></g:if>
                <g:if test="${params.max}"><g:hiddenField name="max" value="${params.max}"/></g:if>
                <g:if test="${params.offset}"><g:hiddenField name="offset" value="${params.offset}"/></g:if>
                <a href="#" id="back" class="button submit float-left"><g:message code="back.label"/></a>

                <g:actionSubmit class="button submit float-left" action="reject" value="${message(code: 'default.navigation.button.reject')}"
                                style="margin-right: 1em"
                                disabled="${isClosedProcess}"
                                formnovalidate=""
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');"/>

                <g:actionSubmit class="button submit float-right" action="accept" value="${message(code: 'default.navigation.button.accept')}"
                                disabled="${!isWaitingProcess || !hasDocuments}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');"/>

                <g:if test="${isWaitingOnlyProcess && hasDocuments}">
                    <g:link class="button submit renewSubscriptions" action="reloadDocuments" id="${processInstance.id}">
                        <g:message code="generate.again.documents.label"/>
                    </g:link>
                </g:if>

                <g:if test="${isWaitingForSubscriptionProcess}">
                    <g:link class="button submit resendEmails" action="resendEmail" id="${processInstance.id}">
                        <g:message code="resend.email.label"/>
                    </g:link>
                </g:if>
            </fieldset>
        </nav>
    </g:form>
</section>
</body>
</html>