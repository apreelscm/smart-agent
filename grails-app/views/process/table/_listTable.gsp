<%@ page import="com.eservice.eumowy.ActivityHelper; com.eservice.eumowy.Process" %>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table" style="table-layout: fixed; width: 100%">
    <thead>
        <tr>
            <g:sortableColumn property="id"  title="${message(code: 'process.id.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="lastUpdated"  title="${message(code: 'last.updated.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="client.nip"  title="${message(code: 'client.list.nip.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="client.name"  title="${message(code: 'client.name.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="saleSection"  title="${message(code: 'sale.section.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phNumber"  title="${message(code: 'ph.number.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phFirstName"  title="${message(code: 'ph.firstName.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="phSurname"  title="${message(code: 'ph.lastName.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="signingDate"  title="${message(code: 'signing.date.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="acceptanceDate"  title="${message(code: 'acceptance.date.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="updateDate"  title="${message(code: 'update.date.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="status"  title="${message(code: 'status.label')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="isAcceptorDataChanged" title="${message(code: 'acceptor.changed.manually')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
            <g:sortableColumn property="isFromBisnode" title="${message(code: 'is.from.bisnode')}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
        </tr>
    </thead>
    <tbody>
    <g:each in="${processInstanceList}" status="i" var="processInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCell" style="word-wrap:break-word;"><g:link action="show" id="${processInstance.id}" params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo,sort:sort,order:order,max:max,offset:offset]">${fieldValue(bean: processInstance, field: "stringId")}</g:link></td>
            <td class="tableCell"><g:formatDate date="${processInstance.lastUpdated}" format="yyyy-MM-dd HH:mm"/></td>
            <td class="tableCell">${fieldValue(bean: processInstance.client ?: null, field: "nip")}</td>
            <td class="tableCell" style="word-wrap: break-word;">${fieldValue(bean: processInstance.client ?: null, field: "name")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "saleSection")}
            <td class="tableCell">${fieldValue(bean: processInstance, field: "stringPhNumber")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phFirstName")}</td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "phSurname")}</td>
            <td class="tableCell"><g:formatDate date="${processInstance.signingDate}" format="yyyy-MM-dd HH:mm"/></td>
            <td class="tableCell"><g:formatDate date="${processInstance.acceptanceDate}" format="yyyy-MM-dd HH:mm"/></td>
            <td class="tableCell"><g:formatDate date="${processInstance.updateDate}" format="yyyy-MM-dd HH:mm"/></td>
            <td class="tableCell">${fieldValue(bean: processInstance, field: "status")}</td>
            <td class="tableCell">
                <g:if test="${processInstance.getBooleanData("isRepresentativesChangedManually") ||
                        (!processInstance.getBooleanData("isFromBisnode") && ActivityHelper.isNewAgreement(processInstance))}">
                    <g:checkBox name="isRepresentativesChangedManually" value="true" checked="true" disabled="disabled"/>
                </g:if>
            </td>
            <td class="tableCell">
                <g:if test="${processInstance.getBooleanData("isAcceptorDataChanged")}">
                    <g:checkBox name="isAcceptorDataChanged" value="true" checked="true" disabled="disabled"/>
                </g:if>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>

<div class="pagination">
    <g:paginate id="paginateControl"  total="${processInstanceTotal}"
                params="[filterStatus:filterStatus,filterObserved:filterObserved,filterNip:filterNip,filterPhNo:filterPhNo,filterDateFrom:filterDateFrom,filterDateTo:filterDateTo]"/>
</div>