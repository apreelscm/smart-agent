<%@ page import="com.eservice.eumowy.Process" %>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed; width: 100%">
    <thead>
    <tr>
        <util:remoteSortableColumn property="stringId"  title="Id Procesu"  update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="lastUpdated" title="Data Aktualizacji" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="clientNip"  title="Nip Klienta"  update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="clientName"  title="Nazwa Klienta"  update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="saleSection"  title="Segment Sprzedażowy" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="phNumber"  title="Numer PH" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="phFirstName"  title="Imię PH" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="phSurname"  title="Naziwsko PH" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
        <util:remoteSortableColumn property="status"  title="Status" update="tableBox" action="filter" params="[filterStatus:filterStatus,filterObserved:filterObserved]"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstanceList}" status="i" var="processInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCell" style="word-wrap:break-word;"><g:link action="show" id="${processInstance.id}">${fieldValue(bean: processInstance, field: "stringId")}</g:link></td>
            <td class="tableCell"><g:formatDate date="${processInstance.lastUpdated}" /></td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "clientNip")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "clientName")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "saleSection")}
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phNumber")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phFirstName")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phSurname")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "status")}</td>
        </tr>
    </g:each>
    </tbody>
</table>

<div class="pagination">
    <g:paginate id="paginateControl"  total="${processInstanceTotal}"
                params="[filterStatus:filterStatus,filterObserved:filterObserved]" />
</div>