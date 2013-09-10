<%@ page import="com.eservice.eumowy.Process" %>
<r:require module="jquery" />
<g:javascript>
    function showPdfBox(obj) {
        var el = document.getElementById(obj);
        if ( el.style.display == 'none' ) {
            el.style.display = '';
        }
        
    }
    
    function loadedPdfBox(obj) {
    	console.log("Cleaning...");
    	jQuery("#"+obj).html('');
    }
    
    function showPdfBoxSpinner(obj) {
    	console.log("Loading...");
    	jQuery("#"+obj).show().html('<div style="text-align: center; width: 200px; display: block; margin: 0 auto;"><h2 style="padding-top: 100px;"><g:message code="process.subscriptions.loadingPage" /></h2><img style="width: 40px;" src="/eumowy/images/document-loading.gif" /></div>')
    }
</g:javascript>

<table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="table-layout: fixed;">
    <thead>
    <tr>
        <g:sortableColumn title="Dokument" action="list" property="name" />
        <g:sortableColumn property="lastUpdated" title="Data Aktualizacji" style="width: 150px" />
        <th style="width: 55px"></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${processInstance.documents}" status="i" var="document">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td class="tableCellLeft"  class="wrapped"><g:remoteLink action="showPdfByDocumentId" class="wrapped"
                                                     title="${fieldValue(bean: document, field: "name")}"
                                                     update="pdfBox"
                                                     onLoading="showPdfBoxSpinner('pdfBox')"
                                                     onLoaded ="loadedPdfBox('pdfBox')"
                                                     onSuccess="showPdfBox('pdfBox')"
                                                     params="[id: document.id]">
                ${fieldValue(bean: document, field: "name")}
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