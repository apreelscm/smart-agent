<%@ page import="com.eservice.eumowy.Process" %>
<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed;">
    <thead>
    <tr>
        <g:sortableColumn title="Załącznik" action="list" property="name" />
        <g:sortableColumn property="dateUploaded" title="Data Aktualizacji" style="width: 150px" />
        <th style="width: 55px"></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstance.attachments}" status="i" var="attachment">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCellLeft">${fieldValue(bean: attachment, field: "name")}</td>
            <td class="tableCell"><g:formatDate date="${attachment.dateUploaded}" /></td>

            <td class="tableCell">
                <g:link class="button action float-left" style="margin: 0 auto"
                        action="downloadAttachment"
                        id="${attachment.id}">Pobierz</g:link>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>