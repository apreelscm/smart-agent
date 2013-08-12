<%@ page contentType="text/html;charset=UTF-8" %>
<div id="attachmentsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.attachments.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
        <div style="text-align: left">

            <g:form name="uploadForm" enctype="multipart/form-data" style="width: 100%" >
                <g:hiddenField name="_eventId_uploadFile" id="_eventId_uploadFile" />
                <input type="hidden" name="execution" value="e9s1" id="execution" />
                <input type='hidden' name='upload' value='attachments' />

                <g:message code="todo" default="Dodaj:"/>
                <input id="fileUploadInput" type='file' name='file' style="width: 267px"/>

                <div>
                    <g:if test="${uploadInfoMessage}">
                        <g:render template="message/infoMessage" model="[message: uploadInfoMessage]"/>
                    </g:if>
                    <g:if test="${uploadErrorMessage}">
                        <g:render template="message/errorMessage" model="[message: uploadErrorMessage]"/>
                    </g:if>
                </div>


                <table style="margin-top: 10px" class="t">
                    <thead>
                    <tr>
                        <td>Nazwa</td>
                        <td>Rozmiar</td>
                        <td>Rozszerzenie</td>
                        <td>Data dodania</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${files}" status="i" var="file">
                        <tr>
                            <td>${fieldValue(bean: file, field: "name")}
                            <td><fileuploader:prettysize size="${file.size}" /></td>
                            <td>${fieldValue(bean: file, field: "extension")}</td>
                            <td><g:formatDate date="${file.dateUploaded}" /></td>
                            <td> <g:link  id="${file.id}"  event="deleteFile" name="${file.id}" class="button small action" >Usuń</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
                </div>
            </g:form>
        </div>
</fieldset>
</div>