<%@ page import="com.eservice.eumowy.Process" %>

<g:javascript>
    function showPdfBox(obj) {
        var el = document.getElementById(obj);
        if ( el.style.display == 'none' ) {
            el.style.display = '';
        }
    }
</g:javascript>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed;">
    <thead>
    <tr>
        <g:sortableColumn title="Dokument" action="list" property="#" />
        <g:sortableColumn property="#" title="Data Aktualizacji" style="width: 150px" />
        <th style="width: 55px"></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstance.documents}" status="i" var="document">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCellLeft" ><g:remoteLink action="showPdfByDocumentId"  class="wrapped"
                                                     title="${fieldValue(bean: document, field: "filename")}"
                                                     update="pdfBox"
                                                     onSuccess="showPdfBox('pdfBox')"
                                                     params="[id: document.id]">
                ${fieldValue(bean: document, field: "filename")}
            </g:remoteLink></td>

            <td class="tableCell"><g:formatDate date="${document.lastUpdated}" /></td>

            <td class="tableCell">
                <g:link class="button action float-left" style="margin: 0 auto"
                        action="downloadDoc"
                        id="${document.id}">Pobierz</g:link>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>