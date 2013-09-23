
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
    <g:set var="isWaitingProcess" value="${processInstance?.status.equals(Process.ProcessStatus.WAITING)}" />

    <title>${entityName}</title>
  %{--  <r:script>
        jQuery(document).ready(function(){
            var isProcessAccepted = ${isProcessAccepted};
            if(isProcessAccepted){
                jQuery("input.submit").attr('disabled', 'disabled');
            }
        })
    </r:script>--}%
</head>
<body>

<section id="show-process">
    <h1 class="ng linia-bottom">Id Procesu: ${processInstance?.id}</h1>

    <div id="notesMessageBox">
        <g:if test="${flash.message}">
            <g:render template="../activity/message/infoMessage" model="[message: flash.message]"/>
        </g:if>
        <g:if test="${flash.error}">
            <g:render template="../activity/message/errorMessage" model="[message: flash.error]"/>
        </g:if>
    </div>

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
             %{--   <g:checkBox name="observed" checked="true" style="position: relative; top: 3px; left: 3px"/>--}%

                <input type="checkbox" id="observed" style="position: relative; top: 3px; left: 3px" name="observed"
                    ${processInstance?.observed ? 'checked' : ''}
                    ${!isWaitingProcess ? 'disabled' : ''}
                >
            </div>

            <div style="margin-top: 15px">
                <label style="width: 70px; text-align: right">
                    <g:message code="todo" default="Uwagi:"/>
                </label>

                <textarea id="notes" maxlength="300" style="margin-left: 8px; height: 100px; min-width: 400px" name="notesFromZrd"
                    ${!isWaitingProcess ? 'disabled' : ''}
                    ${!params.notesFromZrd ? 'required' : '' }>${processInstance?.notesFromZrd}</textarea>

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
                <g:link class="button submit float-left" action="list">Wróć</g:link>

                <g:actionSubmit class="button submit" action="reject" value="Odrzuć"
                                style="float: left;margin-right: 1em;display:block"
                                disabled="${!isWaitingProcess}"
                                formnovalidate=""
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                <g:actionSubmit class="button submit" action="accept" value="Zaakceptuj"
                                disabled="${!isWaitingProcess}"
                                style="float: right;display:block"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </nav>
    </g:form>

</section>
</body>
</html>