<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label')}" />
    <title><g:message code="process.list.label"  /></title>
    <r:require module="listProcess"/>
    <r:require module="mask"/>
</head>
<body class="processListPage">

<section id="list-process" >

    <h1 class="ng linia-bottom"><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="" style="margin: .8em .5em; text-align: center">
        <a id="invalidateCache" href="<g:createLink controller="process" action="invalidateCaches"/>"><g:message code="invalidate.cache"/></a>
    </div>
    <div class="search-bar" style="margin: .8em .5em; text-align: center">
        <g:form action="list" style="vertical-align: middle">
            <div class="display-inline">
                <label for="filterStatus"><g:message code="process.status.label"/></label>
                <g:select name="filterStatus" from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                          keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                          noSelection="['': '']"
                          value="${filterStatus}"
                          style="width:200px;"/>
            </div>

            <div class="display-inline" style="margin-left: .4em">
                <label for="filterNip"><g:message code="process.nip.label"/></label>
                <g:textField name="filterNip" style="width: 80px" value="${filterNip}" class="nip"/>
            </div>

            <div class="display-inline" style="margin-left: .4em">
                <label for="filterPhNo"><g:message code="process.phNumber.label"/></label>
                <g:textField name="filterPhNo" style="width: 80px" value="${filterPhNo}" class="ph-number"/>
            </div>

            <div class="display-inline-block" style="margin-left: .4em">
                <div>
                    <label for="filterDateFromDF"><g:message code="process.dateFrom.label"/></label>
                    <g:textField id="filterDateFromDF" name="filterDateFrom" class="dateFromDatepicker" readonly="true" style="width: 70px;" value="${filterDateFrom}"/>
                </div>

                <div style="margin-top: .2em">
                    <label for="filterDateToDF"><g:message code="process.dateTo.label"/></label>
                    <g:textField id="filterDateToDF" name="filterDateTo" class="dateToDatepicker" readonly="true" style="width: 70px;" value="${filterDateTo}"/>
                </div>
            </div>

            <div class="display-inline" style="margin-left: .4em;">
                <label for="filterObserved"><g:message code="process.observed.label"/></label>
                <g:checkBox name="filterObserved" value="isObserved" checked="${filterObserved == 'isObserved'}"
                            style="position:relative;top: 3px"/>
            </div>

            <g:actionSubmit class="button action display-inline" action="list" value="${message(code: 'show.label')}"
                            style="margin-left: 1em; position: relative; bottom: 8px"/>
        </g:form>
    </div>


    <div id="tableBox">
        <g:render template="table/listTable"/>
    </div>

    <div id="generateReportButton">
        <input type="button" value="${message(code: 'generate.report.label')}" class="button action"/>
    </div>

    <div class="display-none" id="reportDialog">
        <g:form controller="report" action="salesmenStatus">
            <label for="startDate"><g:message code="process.dateFrom.label"/></label>
            <g:textField name="startDate" class="dateFromDatepicker" required="required" autocomplete="off"/>

            <label for="endDate"><g:message code="process.dateTo.label"/></label>
            <g:textField name="endDate" class="dateToDatepicker" required="required" autocomplete="off"/>

            <g:submitButton name="generateReport" value="${message(code: 'generate.label')}" class="button action"/>
        </g:form>
    </div>
</section>
</body>
</html>