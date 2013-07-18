<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>

</head>
<body>
<a href="#list-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

<div id="list-process" class="content scaffold-list" role="main">
    <h1><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <g:form >
        <table width='350'  border="0" align="center" cellpadding="3" cellspacing="1" class="table"
               style="table-layout: fixed; width: 100%">
            <thead>
            <tr>
                <g:sortableColumn property="id" title="Id"/>
                <g:sortableColumn property="lastUpdated" title="Data Akutalizacji" />
                <g:sortableColumn property="x"  title="Rodziaj Działania"  />
                <g:sortableColumn property="clientNip"  title="Nip Klienta" />
                <g:sortableColumn property="clientName"  title="Nazwa Klienta" />
                <g:sortableColumn property="saleSection"  title="Segment Sprzedażowy"/>
                <g:sortableColumn property="phNumber"  title="Numer PH" />
                <g:sortableColumn property="phFirstName"  title="Imię PH" />
                <g:sortableColumn property="phSurname"  title="Naziwsko PH" />
                <g:sortableColumn property="x"  title="Guid Dokumentu" />
            </tr>
            </thead>
            <tbody>
            <g:each in="${processInstanceList}" status="i" var="processInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <td ><g:link action="show" id="${processInstance.id}">${fieldValue(bean: processInstance, field: "id")}</g:link></td>
                    <td ><g:formatDate date="${processInstance.lastUpdated}" /></td>
                    <td >${fieldValue(bean: processInstance, field: "")}</td>
                    <td>${fieldValue(bean: processInstance, field: "clientNip")}</td>
                    <td>${fieldValue(bean: processInstance, field: "clientName")}</td>
                    <td >${fieldValue(bean: processInstance, field: "saleSection")}
                    <td>${fieldValue(bean: processInstance, field: "phNumber")}</td>
                    <td>${fieldValue(bean: processInstance, field: "phFirstName")}</td>
                    <td>${fieldValue(bean: processInstance, field: "phSurname")}</td>
                    <td>${fieldValue(bean: processInstance, field: "")}</td>
                    %{--<td>${fieldValue(bean: processInstance, field: "status")}</td>--}%
                </tr>
            </g:each>
            </tbody>
        </table>

        <div class="pagination">
            <g:paginate id="paginateControl"  total="${processInstanceTotal}"   />
        </div>
    </g:form>
</div>

</body>
</html>