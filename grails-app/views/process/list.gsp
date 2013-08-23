<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
    <r:require module="listProcess"/>


</head>
<body class="processListPage">

<section id="list-process" >

    <h1 class="ng linia-bottom"><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="search-bar" style="margin: .8em .5em ">
        <g:form action="list" style="vertical-align: middle">
            <div class="display-inline">
                <label for="filterStatus"><g:message code="process.phFirstName.label" default="Status Procesu:" /></label>
                <g:select name="filterStatus" from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                          keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                          noSelection="['': '']"
                          value="${filterStatus}"
                          style="width:200px;"/>
                %{--onchange="${remoteFunction(action: 'filterByStatus', update: 'tableBox', params: '\'status=\' + this.value')}"--}%
            </div>

            <div class="display-inline" style="margin-left: .4em">
                <label for="filterNip"><g:message code="process.nip.label" default="NIP:" /></label>
                <g:textField name="filterNip" style="width: 80px" value="${filterNip}" maxlength="10"/>
            </div>

            <div class="display-inline" style="margin-left: .4em">
                <label for="filterPhNo"><g:message code="process.phNumber.label" default="Nr PH:" /></label>
                <g:textField name="filterPhNo" style="width: 80px" value="${filterPhNo}" maxlength="6"/>
            </div>

            <div class="display-inline-block" style="margin-left: .4em">

                <div>
                    <label for="filterDateFromDF"><g:message code="process.dateFrom.label" default="Data od:" /></label>
                    <g:textField id="filterDateFromDF" name="filterDateFrom" readonly="true" style="width: 70px;" value="${filterDateFrom}"/>
                </div>

                <div style="margin-top: .2em">
                    <label for="filterDateToDF"><g:message code="process.dateTo.label" default="Data do:" /></label>
                    <g:textField id="filterDateToDF" name="filterDateTo" readonly="true" style="width: 70px;" value="${filterDateTo}"/>
                </div>
            </div>

            <div class="display-inline" style="margin-left: .4em;">
                <label for="filterObserved"><g:message code="process.observed.label" default="Obserwowany:" /></label>
                <g:checkBox name="filterObserved" value="isObserved" checked="${filterObserved == 'isObserved'}"
                            style="position:relative;top: 3px"/>
            </div>

            <g:actionSubmit class="button action display-inline" action="list" value="Pokaż"
                            style="margin-left: 1em; position: relative; bottom: 8px"/>
        </g:form>
    </div>


    <div id="tableBox">
        <g:render template="table/listTable"/>
    </div>

</section>

</body>
</html>