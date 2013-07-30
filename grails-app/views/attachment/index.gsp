<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>

<h1 class="ng linia-bottom">Załączanie plików</h1>

<p style="margin-left:20px;width:80%">
    <fileuploader:form 	upload="attachments"
                          successController="attachment"
                          successAction="index"
                          errorController="attachment"
                          errorAction="index"/>
</p>

<g:if test="${flash.message}">
    <div class="errors" role="status">${flash.message}</div>
</g:if>

<g:each var="f" in="${files}">
    <table>
        <tr>
            <td><b>Name</b></td>
            <td>${f.name}</td>
        </tr>
        <tr>
            <td><b>Path</b></td>
            <td>${f.path}</td>
        </tr>
        <tr>
            <td><b>Size</b></td>
            <td><fileuploader:prettysize size="${f.size}" /> (${f.size})</td>
        </tr>
        <tr>
            <td><b>Extension</b></td>
            <td>${f.extension}</td>
        </tr>
        <tr>
            <td><b>Downloaded</b></td>
            <td>${f.downloads}</td>
        </tr>
        <tr>
            <td><b>Date uploaded</b></td>
            <td><g:formatDate format="MM/dd/yyyy HH:mm" date="${f.dateUploaded}" /></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <fileuploader:download 	id="${f.id}" errorAction="index" errorController="attachment">
                    Pobierz
                </fileuploader:download></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <g:link  action="delete" id="${f.id}">Usuń</g:link></td>
        </tr>
    </table>
</g:each>
</p>
</body>
</html>