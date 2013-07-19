<%@ page import="com.eservice.eumowy.Process" %>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed; width: 100%;">
    <thead>
    <tr>
        <g:sortableColumn title="Dokumenty" action="list" property="x"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstance.documents}" status="i" var="document">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            %{--<td class="tableCell">${fieldValue(bean: document, field: "filename")}</td>--}%

            <td class="tableCell" ><g:remoteLink action="showPdfByDocumentId"
                                                 title="${fieldValue(bean: document, field: "filename")}"
                                                 update="pdfBox"
                                                 params="[id: document.id]"
            >


                ${fieldValue(bean: document, field: "filename")}
            </g:remoteLink></td>
        </tr>
    </g:each>
    </tbody>
</table>