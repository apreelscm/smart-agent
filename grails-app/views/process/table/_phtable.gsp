<%@ page import="com.eservice.eumowy.Process" %>
<div id="list-process" class="content scaffold-list" role="main">
    <h1><g:message code="process.list.label" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <g:form>
        <table>
            <thead>
            <tr>
                <th ></th>
                <g:sortableColumn property="calcNumber" title="${message(code: 'process.calcNumber.label', default: 'Id')}" />
                <g:sortableColumn property="calcNumber" title="${message(code: 'process.calcNumber.label', default: 'Calc Number')}" />
                <g:sortableColumn property="clientName" title="${message(code: 'process.clientName.label', default: 'Client Name')}" />
                <g:sortableColumn property="clientNip" title="${message(code: 'process.clientNip.label', default: 'Client Nip')}" />
                <g:sortableColumn property="dateCreated" title="${message(code: 'process.dateCreated.label', default: 'Date Created')}" />
                <g:sortableColumn property="lastUpdated" title="${message(code: 'process.lastUpdated.label', default: 'Last Updated')}" />
                <g:sortableColumn property="phFirstName" title="${message(code: 'process.phFirstName.label', default: 'Ph First Name')}" />
                <g:sortableColumn property="status" title="${message(code: 'process.phFirstName.label', default: 'Status')}" />
            </tr>
            </thead>
            <tbody>
            <g:each in="${processInstanceList}" status="i" var="processInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td><g:checkBox name="selectedProcess" value="${processInstance.id}" checked="false"/> </td>

                    <td><g:link action="show" id="${processInstance.id}">${fieldValue(bean: processInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: processInstance, field: "calcNumber")}

                    <td>${fieldValue(bean: processInstance, field: "clientName")}</td>

                    <td>${fieldValue(bean: processInstance, field: "clientNip")}</td>

                    <td><g:formatDate date="${processInstance.dateCreated}" /></td>

                    <td><g:formatDate date="${processInstance.lastUpdated}" /></td>

                    <td>${fieldValue(bean: processInstance, field: "phFirstName")}</td>

                    <td>${fieldValue(bean: processInstance, field: "status")}</td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <div class="pagination">
            <g:paginate id="paginateControl"  total="${processInstanceTotal}" />
        </div>

        <fieldset class="buttons">
            <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.deleteSelected.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
    </g:form>

</div>