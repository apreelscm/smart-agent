
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

<section id="show-process" class="content scaffold-list" role="main">
    <h1 class="ng linia-bottom">${processInstance?.uid}</h1>

    <ul class="property-list" >
        <li style="margin:0em 0em 1em 0em;">
            <label for="clientName" style="display: inline ">
                <g:message code="process.clientName.label" default="Nazwa Klienta" />
            </label>
            <g:textField name="clientName" value="${processInstance?.clientName}" disabled="disabled"
                         style="width: 89%;display: inline"/>
        </li>


        <li style="margin:0em 0em 0em 0em;display:inline;">
            <label for="clientNip"  style="display: inline ">
                <g:message code="process.clientNip.label" default="NIP" />

            </label>
            <g:textField name="clientNip" value="${processInstance?.clientNip}" disabled="disabled"/>
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
                         disabled="disabled"/>
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


    <div id="pdfBox">
        %{--<g:render template="pdf/embedDocument"/>--}%
    </div>

    <div class="clear"/>

    <p style="width: 100">
        <g:checkBox name="observe"/> Obserwuj
    </p>

    <nav>
        <g:form>
            <fieldset class="przyciski">
                <g:hiddenField name="uid" value="${processInstance.uid}"/>
                <g:actionSubmit class="przycisk-submit" value="Wróć" action="list" style="float: left" />
                <g:actionSubmit class="przycisk-submit" action="accept" value="Zaakceptuj"
                                style="float: right;display:${!isNewProcess ? 'none' : 'block'}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                <g:actionSubmit class="przycisk-submit" action="reject" value="Odrzuć"
                                style="float: right;margin-right: 1em;display:${!isNewProcess ? 'none' : 'block'}"
                                formnovalidate=""
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />

            </fieldset>
        </g:form>
    </nav>

</section>
</body>
</html>


