<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body class="processListPage">

<section id="list-process" class="content scaffold-list" role="main">
    <h1 class="ng"><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div style="margin: 1em 0 1em 0">
        <label for="status">
            <g:message code="process.phFirstName.label" default="Status Dokumentu" />
        </label>
        <g:select id="statusSelect" name="status" from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                  keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                  noSelection="['': '']"
                  onchange="${remoteFunction(action: 'filterByStatus',
                          update: 'tableBox',
                          params: '\'status=\' + this.value')}"
        />
    </div>

    <div id="tableBox">
        <g:render template="table/listTable"/>
    </div>

</section>

</body>
</html>