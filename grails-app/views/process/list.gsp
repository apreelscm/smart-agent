<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body class="processListPage">

<section id="list-process" >

    <h1 class="ng linia-bottom"><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="search-bar" style="margin: .8em .5em ">
        <g:form action="list">
            <div class="display-inline">
                <label for="filterStatus"><g:message code="process.phFirstName.label" default="Status Procesu:" /></label>
                <g:select name="filterStatus" from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                          keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                          noSelection="['': '']"
                          value="${filterStatus}"/>
                %{--onchange="${remoteFunction(action: 'filterByStatus', update: 'tableBox', params: '\'status=\' + this.value')}"--}%
            </div>

            <div class="display-inline" style="margin-left: .4em">
                <label for="filterObserved"><g:message code="process.phFirstName.label" default="Obserwowany:" /></label>
                <g:checkBox name="filterObserved" value="isObserved" checked="${filterObserved == 'isObserved'}" />
            </div>

            <g:actionSubmit class="button action display-inline" action="list" value="Pokaż"
                            style="margin-left: .4em"/>
        </g:form>
    </div>



    <div id="tableBox">
        <g:render template="table/listTable"/>
    </div>

</section>

</body>
</html>