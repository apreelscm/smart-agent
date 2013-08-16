<%@ page contentType="text/html;charset=UTF-8" %>
<div id="attachmentsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.attachments.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
        <div style="text-align: left">

            <iframe id="hidden-upload-frame" name="hidden-upload-frame" style="display: none"></iframe>

            <div id="attachmentsBox">
                <g:render template="/attachment/list" model="[files: files]"/>
            </div>

            <div id="statusBox">
                %{--SUCCES / ERROR INFO FROM REMOTE CALL--}%
            </div>

            <g:form name="uploadForm" action="upload" method="post" enctype="multipart/form-data"
                    target="hidden-upload-frame">
                <input type='hidden' name='upload' value='attachments'/>

                <input id="fileUploadInput" name="file" type="file" class="filestyle"
                       data-icon="false"
                       data-classButton="button action"
                       data-buttonText="Dodaj plik"
                       data-input="false"/>

                <div id="spinner2" class="spinner-big display-inline-block display-none" style="margin-left: 5px" ></div>

            </g:form>

        </div>
</fieldset>
</div>