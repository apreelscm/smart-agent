<%@ page import="com.eservice.eumowy.Process" %>
<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed;">
    <thead>
    <tr>
        <g:sortableColumn title="Załącznik" action="list" property="#" />
        <g:sortableColumn property="#" title="Data Aktualizacji" style="width: 150px" />
        <th style="width: 55px"></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstance.attachments}" status="i" var="attachment">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCellLeft" ><g:remoteLink action="showPdfByDocumentId" class="wrapped"
                                                     title="${fieldValue(bean: attachment, field: "name")}"
                                                     update="pdfBox"
                                                     params="[id: attachment.id]">${fieldValue(bean: attachment, field: "name")}</g:remoteLink></td>
            <td class="tableCell"><g:formatDate date="${attachment.lastUpdated}" /></td>

            <td class="tableCell">
                <g:link class="button action float-left" style="margin: 0 auto"
                        action="downloadAtt"
                        id="${attachment.id}">Pobierz</g:link>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>