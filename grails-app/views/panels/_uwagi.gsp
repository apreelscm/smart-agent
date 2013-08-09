<div id="notesPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.notes.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
            <div style="text-align: left">

                <div style="padding-bottom: 10px"><g:message code="panel.notes"/></div>

                <g:textArea name="note" style="width: 100%"/>

                <g:form name="uploadForm" enctype="multipart/form-data" style="display: table; margin: 10px auto 0px">
                    <input type="hidden" name="execution" value="e9s1" id="execution" />
                    <input type='hidden' name='upload' value='attachments' />
                    <input id="fileUploadInput" type='file' name='file' style="width: 267px"/>
                    <g:hiddenField name="_eventId_uploadFile" id="_eventId_uploadFile" />

                    <div style="width: 285px">
                        <g:if test="${uploadInfoMessage}">
                            <g:render template="message/infoMessage" model="[message: uploadInfoMessage]"/>
                        </g:if>
                        <g:if test="${uploadErrorMessage}">
                            <g:render template="message/errorMessage" model="[message: uploadErrorMessage]"/>
                        </g:if>
                    </div>
                </g:form>
            </div>
        </div>
    </fieldset>
</div>