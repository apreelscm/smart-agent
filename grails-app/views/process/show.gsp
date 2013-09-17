
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
    <g:set var="isNewProcess" value="${processInstance?.status.equals(Process.ProcessStatus.NEW)}" />
    <title>${entityName}</title>
</head>
<body>

<section id="show-process">
    <h1 class="ng linia-bottom">Id Procesu: ${processInstance?.id}</h1>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <ul class="property-list" style="text-align: center">
        <li style="margin:0em 0em 1em 0em;">
            <label for="clientName" style="display: inline ">
                <g:message code="process.clientName.label" default="Nazwa Klienta" />
            </label>
            <g:textField name="clientName" value="${processInstance?.client?.name}" disabled="disabled"
                         style="width: 89%;display: inline"/>
        </li>


        <li style="margin:0em 0em 0em 0em;display:inline;">
            <label for="clientNip"  style="display: inline ">
                <g:message code="process.clientNip.label" default="NIP" />

            </label>
            <g:textField name="clientNip" value="${processInstance?.client?.nip}" disabled="disabled"/>
        </li>

        <li style="margin:0em 0em 0em 0em;display:inline;">
            <label for="phNumber">
                <g:message code="process.phNumber.label" default="Numer Przedstawiciela" />

            </label>
            <g:textField name="phNumber" value="${processInstance?.phNumber}" disabled="disabled"/>
        </li>

        <li style="margin:0em 0em 0em 0em;display:inline;">
            <label for="phFirstName">
                <g:message code="process.phFirstName.label" default="Imię i Naziwsko" />
            </label>
            <g:textField name="phFirstName" value="${processInstance?.phFirstName + " " + processInstance?.phSurname}"
                         disabled="disabled" style="width: 250px"/>
        </li>
    </ul>

    <div>
        <div id="documentsBox" class="float-left" style="width: 49%">
            <g:render template="table/documentsTable"/>
        </div>

        <div id="attachmentsBox" class="float-right" style="width: 49%">
            <g:render template="table/attachmentsTable"/>
        </div>
    </div>

    <div id="pdfBox" class="display-block"
         style="height: 500px; width: 98% ;overflow: hidden;border: solid 1px; border-radius: 5px; display: none; margin: 20px auto">
        %{--tutaj zostanie render pdf template--}%
    </div>

    <div class="clear"/>

    <g:form>
        <div style="margin: 0 auto; width: 600px">
            <div>
                <label style="width: 70px; text-align: right">
                    <g:message code="todo" default="Obserwuj:"/>
                </label>
                    <g:checkBox name="observed" checked="${processInstance?.observed}" style="position: relative; top: 3px; left: 3px"/>
            </div>

            <div style="margin-top: 15px">
                <label style="width: 70px; text-align: right">
                    <g:message code="todo" default="Uwagi:"/>
                </label>
                <g:if test="${params.notes == ""}">
                    <g:textArea id="notes" name="notes" style="margin-left: 8px; height: 100px; min-width: 400px" maxlength="300" required="required"/>
                </g:if>
                <g:else>
                    <g:textArea id="notes" name="notes" style="margin-left: 8px; height: 100px; min-width: 400px" maxlength="300"/>
                </g:else>
            </div>
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
                <g:link class="button submit float-left" action="list" >Wróć</g:link>
                <g:if test="${!(processInstance?.status in [Process.ProcessStatus.ACCEPTED, Process.ProcessStatus.REJECTED])}">
                    <g:actionSubmit class="button submit" action="reject" value="Odrzuć"
                                    style="float: left;margin-right: 1em;display:${!isNewProcess ? 'block' : 'block'}"
                                    formnovalidate=""
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    <g:actionSubmit class="button submit" action="accept" value="Zaakceptuj"
                                    style="float: right;display:${!isNewProcess ? 'block' : 'block'}"
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </g:if>
            </fieldset>
        </nav>
    </g:form>

</section>
</body>
</html>


