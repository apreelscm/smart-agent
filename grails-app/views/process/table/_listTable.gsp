<%@ page import="com.eservice.eumowy.Process" %>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table" style="table-layout: fixed; width: 100%">
    <thead>
        <tr>
            <g:sortableColumn property="id"  title="Id Procesu" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="lastUpdated"  title="Data Aktualizacji" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="client.nip"  title="Nip Klienta" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="client.name"  title="Nazwa Klienta" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="saleSection"  title="Segment Sprzedażowy" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phNumber"  title="Numer PH" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phFirstName"  title="Imię PH" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phSurname"  title="Naziwsko PH" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="status"  title="Status" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
        </tr>
    </thead>
    <tbody>
    <g:each in="${processInstanceList}" status="i" var="processInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCell" style="word-wrap:break-word;"><g:link action="show" id="${processInstance.id}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo,sort:sort,order:order,max:max,offset:offset]">${fieldValue(bean: processInstance, field: "stringId")}</g:link></td>
            <td class="tableCell"><g:formatDate date="${processInstance.lastUpdated}" /></td>
            <td class="tableCell">${fieldValue(bean: processInstance.client ?: null, field: "nip")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance.client ?: null, field: "name")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "saleSection")}
            <td class="tableCell">${fieldValue(bean: processInstance, field: "stringPhNumber")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phFirstName")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phSurname")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "status")}</td>
        </tr>
    </g:each>
    </tbody>
</table>

<div class="pagination">
    <g:paginate id="paginateControl"  total="${processInstanceTotal}"
                params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
</div>