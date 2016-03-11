<%@ page import="com.eservice.eumowy.Activity; com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label')}" />
    <title><g:message code="process.list.label"  /></title>

    <asset:javascript src="apreel/process/listProcess.js"/>
    <asset:javascript src="apreel/mask.js"/>
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
            <div class="row">
                <label for="dateFrom"><g:message code="salesman.report.update.dateFrom"/></label>
                <g:textField name="dateFrom" class="dateFromDatepicker" required="required" autocomplete="off"/>
            </div>
            <div class="row">
                <label for="dateTo"><g:message code="salesman.report.update.dateTo"/></label>
                <g:textField name="dateTo" class="dateToDatepicker" required="required" autocomplete="off"/>
            </div>
            <div class="row">
                <label for="nip"><g:message code="salesman.report.nip"/></label>
                <g:textField name="nip"/>
            </div>
            <div class="row">
                <label for="salesSegment"><g:message code="salesman.report.sales.segment"/></label>
                <g:select from="${1..7}" name="salesSegment" noSelection="['':'Wszystkie']"/>
            </div>
            <div class="row">
                <label for="phNumber"><g:message code="salesman.report.ph.number"/></label>
                <g:textField name="phNumber"/>
            </div>
            <div class="row">
                <label for="phSurname"><g:message code="salesman.report.ph.surname"/></label>
                <g:textField name="phSurname"/>
            </div>
            <div class="row">
                <label for="status"><g:message code="salesman.report.status"/></label>
                <g:select from="${com.eservice.eumowy.Process.ProcessStatus}" name="status" noSelection="['':'Wszystkie']"/>
            </div>
            <div class="row">
                <label for="bisnode"><g:message code="salesman.report.bisnode"/></label>
                <g:select from="${[true: 'TAK', 'false': 'NIE']}" optionKey="key" optionValue="value" noSelection="['':'']"
                          name="bisnode"/>
            </div>
            <div class="row">
                <label for="acceptorChange"><g:message code="salesman.report.acceptor.change"/></label>
                <g:select from="${[true: 'TAK', 'false': 'NIE']}" optionKey="key" optionValue="value" noSelection="['':'']"
                          name="acceptorChange"/>
            </div>
            <div class="row">
                <label for="activity"><g:message code="salesman.report.activity"/></label>
                <g:select from="${com.eservice.eumowy.Activity.list()}" name="activity" optionKey="id" optionValue="userFriendlyCode" noSelection="['':'Wszystkie']"/>
            </div>

            <g:submitButton name="generateReport" value="${message(code: 'generate.label')}" class="button action"/>
        </g:form>
    </div>
</section>
</body>
</html>